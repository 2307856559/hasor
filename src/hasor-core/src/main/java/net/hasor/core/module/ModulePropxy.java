/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.core.module;
import static net.hasor.core.AppContext.ModuleEvent_Started;
import static net.hasor.core.AppContext.ModuleEvent_Stoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hasor.core.ApiBinder;
import net.hasor.core.ApiBinder.ModuleSettings;
import net.hasor.core.AppContext;
import net.hasor.core.Dependency;
import net.hasor.core.Hasor;
import net.hasor.core.Module;
import net.hasor.core.ModuleInfo;
import org.more.UnhandledException;
import org.more.util.exception.ExceptionUtils;
/**
 * 
 * @version : 2013-7-26
 * @author ������ (zyc@hasor.net)
 */
public abstract class ModulePropxy implements ModuleInfo/*�ṩģ�������Ϣ*/, ModuleSettings, Module {
    private String           displayName;
    private String           description;
    private Module           targetModule;
    private String           namespace;
    private AppContext       appContext;
    private List<Dependency> dependency;
    private boolean          isReady;
    private boolean          isStart;
    //
    public ModulePropxy(Module targetModule, AppContext appContext) {
        this.targetModule = Hasor.assertIsNotNull(targetModule);
        this.appContext = Hasor.assertIsNotNull(appContext);
        //
        this.description = targetModule.getClass().getName();
        this.displayName = targetModule.getClass().getSimpleName();
        this.dependency = new ArrayList<Dependency>();
        this.isReady = false;
    }
    //
    //
    //----------------------------------------------------------------------------------Base Method
    public String getSettingsNamespace() {
        return this.namespace;
    }
    public void bindingSettingsNamespace(String settingsNamespace) {
        if (isReady())
            /*ģ���Ѿ�׼���ã�ֻ�е�ģ����׼���ڲſ���ʹ�ø÷���*/
            throw new IllegalStateException("Module is ready, only can use this method in run-up.");
        //
        this.namespace = settingsNamespace;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public String getDescription() {
        return this.description;
    }
    public Module getTarget() {
        return this.targetModule;
    }
    public List<Dependency> getDependency() {
        return Collections.unmodifiableList(this.dependency);
    }
    public boolean isDependencyReady() {
        for (Dependency dep : this.dependency)
            if (dep.getModuleInfo().isReady() == false)
                if (dep.isOption() == false)
                    return false;
        return true;
    }
    public boolean isReady() {
        if (isDependencyReady() == false)
            return false;
        return this.isReady && this.appContext.isReady();
    }
    public boolean isStart() {
        return this.isStart;
    }
    public String toString() {
        return String.format("displayName is %s, class is %s",//
                this.displayName, this.targetModule.getClass());
    }
    //
    //
    //----------------------------------------------------------------------------Dependency Method
    /**���Դ������л�ȡģ��Ĵ������*/
    protected abstract ModulePropxy getInfo(Class<? extends Module> targetModule, AppContext appContext);
    //
    public void reverse(Class<? extends Module> targetModule) {
        if (isReady())
            /*ģ���Ѿ�׼���ã�ֻ�е�ģ����׼���ڲſ���ʹ�ø÷���*/
            throw new IllegalStateException("Module is ready, only can use this method in run-up.");
        //
        ModulePropxy moduleInfo = this.getInfo(targetModule, this.appContext);
        moduleInfo.weak(this.targetModule.getClass());
    }
    public void weak(Class<? extends Module> targetModule) {
        this._addDep(targetModule, true);
    }
    public void forced(Class<? extends Module> targetModule) {
        this._addDep(targetModule, false);
    }
    private void _addDep(Class<? extends Module> targetModule, boolean forced) {
        if (isReady())
            /*ģ���Ѿ�׼���ã�ֻ�е�ģ����׼���ڲſ���ʹ�ø÷���*/
            throw new IllegalStateException("Module is ready, only can use this method in run-up.");
        //
        ModulePropxy moduleInfo = this.getInfo(targetModule, this.appContext);
        for (Dependency dep : this.dependency)
            if (dep.getModuleInfo() == moduleInfo)
                throw new IllegalStateException("before dependence is included.");
        //
        Dependency dep = new DependencyBean(moduleInfo, forced);
        this.dependency.add(dep);
    }
    //
    //
    //
    //
    //--------------------------------------------------------------------------------Lifety Method
    private boolean isFullStart() {
        return this.appContext.getSettings().getBoolean("hasor.fullStart", false);
    }
    /*����쳣*/
    private void proForceModule(Throwable e) {
        e = ExceptionUtils.getRootCause(e);
        if (e instanceof RuntimeException)
            throw (RuntimeException) e;
        else if (e instanceof Error)
            throw (Error) e;
        else
            throw new UnhandledException(e);
    }
    public final void init(ApiBinder apiBinder) {
        try {
            Module forModule = this.getTarget();
            this.onInit(forModule, apiBinder);
            Hasor.logInfo("init Event on : %s", this.getDisplayName());
            this.isReady = true;
        } catch (Throwable e) {
            this.isReady = false;
            Hasor.logError("%s is not init! %s", this.getDisplayName(), e);
            if (isFullStart())
                this.proForceModule(e);
        }
    }
    public final void start(AppContext appContext) {
        if (this.isReady() == false/*׼��ʧ��*/|| this.isStart() == true/*�Ѿ�����*/)
            return;
        //
        try {
            Module forModule = this.getTarget();
            this.onStart(forModule, appContext);
            Hasor.logInfo("start Event on : %s", this.getDisplayName());
            this.isStart = true;
        } catch (Throwable e) {
            this.isStart = false;
            Hasor.logError("%s in the start phase encounters an error.\n%s", this.getDisplayName(), e);
            if (isFullStart())
                this.proForceModule(e);
        }
    }
    public final void stop(AppContext appContext) {
        if (this.isStart() == false/*�Ѿ�����ֹͣ״̬*/)
            return;
        //
        Module forModule = this.getTarget();
        try {
            this.onStop(forModule, appContext);
            Hasor.logInfo("stop Event on : %s", this.getDisplayName());
            this.isStart = false;
        } catch (Throwable e) {
            Hasor.logError("%s in the stop phase encounters an error.\n%s", this.getDisplayName(), e);
        }
    }
    /*���� AppContext �� KEY ���Ա�֤�ڲ�ͬ�����¾�̬�ֶ����ݵ���ȷ��*/
    private static Map<AppContext, ModuleInfo> loacalModuleInfo = new HashMap<AppContext, ModuleInfo>();
    /**���� AppContext ������ȡ��ǰ ModuleInfo��<p>
     * ע�⣺ֻ�е� ModuleInfo λ�� start stop �����ڣ��÷����Ż᷵�����Ӧ��ModuleInfo�����򷵻�ֵΪ�ա�*/
    public static ModuleInfo getLocalModuleInfo(AppContext appContext) {
        return (appContext != null) ? loacalModuleInfo.get(appContext) : null;
    }
    /**ģ��� init �������ڵ���*/
    protected void onInit(Module forModule, ApiBinder apiBinder) throws Throwable {
        forModule.init(apiBinder);
    }
    /**����ģ�������ź�*/
    protected void onStart(Module forModule, AppContext appContext) throws Throwable {
        try {
            loacalModuleInfo.put(this.appContext, this);
            forModule.start(appContext);
            appContext.getEventManager().doSync(ModuleEvent_Started, forModule, appContext);
        } catch (Throwable e) {
            throw e;
        } finally {
            loacalModuleInfo.remove(this.appContext);
        }
    }
    /**����ģ��ֹͣ�ź�*/
    protected void onStop(Module forModule, AppContext appContext) throws Throwable {
        try {
            loacalModuleInfo.put(this.appContext, this);
            forModule.stop(appContext);
            appContext.getEventManager().doSync(ModuleEvent_Stoped, forModule, appContext);
        } catch (Throwable e) {
            throw e;
        } finally {
            loacalModuleInfo.remove(this.appContext);
        }
    }
}