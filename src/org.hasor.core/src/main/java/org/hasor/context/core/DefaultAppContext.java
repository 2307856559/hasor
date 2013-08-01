/*
 * Copyright 2008-2009 the original author or authors.
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
package org.hasor.context.core;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hasor.Hasor;
import org.hasor.context.ApiBinder;
import org.hasor.context.AppContext;
import org.hasor.context.Environment;
import org.hasor.context.EventManager;
import org.hasor.context.HasorModule;
import org.hasor.context.InitContext;
import org.hasor.context.ModuleInfo;
import org.hasor.context.ModuleSettings;
import org.hasor.context.Settings;
import org.hasor.context.WorkSpace;
import org.hasor.context.binder.ApiBinderModule;
import org.hasor.context.module.ModuleInfoBean;
import org.hasor.context.module.ModuleReactor;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
/**
 * {@link AppContext}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public class DefaultAppContext extends AbstractAppContext {
    private boolean  running;
    private Injector guice;
    private boolean  forceModule;
    //
    //
    //
    //
    public DefaultAppContext() throws IOException {
        super();
    }
    public DefaultAppContext(String mainConfig) throws IOException {
        super(mainConfig);
    }
    public DefaultAppContext(String mainConfig, Object context) throws IOException {
        super(mainConfig, context);
    }
    @Override
    protected void initContext() throws IOException {
        super.initContext();
        this.running = false;
        this.guice = null;
        this.forceModule = this.getSettings().getBoolean("framework.forceModule", false);
    }
    //
    //
    //
    //
    /**���ģ��*/
    public ModuleInfo addModule(HasorModule hasorModule) {
        for (ModuleInfo info : this.getModuleList())
            if (info.getModuleObject() == hasorModule)
                return info;
        ModuleInfo info = new ModuleInfoBean(hasorModule, this);
        this.getModuleList().add(info);
        return info;
    }
    /**ɾ��ģ��*/
    public ModuleInfo removeModule(HasorModule hasorModule) {
        ModuleInfo targetInfo = null;
        for (ModuleInfo info : this.getModuleList())
            if (info.getModuleObject() == hasorModule) {
                targetInfo = info;
                break;
            }
        if (targetInfo != null)
            this.getModuleList().remove(targetInfo);
        return targetInfo;
    }
    /**�������ģ��*/
    public ModuleInfo[] getModules() {
        List<ModuleInfo> haosrModuleSet = getModuleList();
        return haosrModuleSet.toArray(new ModuleInfo[haosrModuleSet.size()]);
    }
    private List<ModuleInfo> haosrModuleSet;
    /**�������߻�����ڴ������ModuleInfo�ļ��϶���*/
    protected List<ModuleInfo> getModuleList() {
        if (this.haosrModuleSet == null)
            this.haosrModuleSet = new ArrayList<ModuleInfo>();
        return haosrModuleSet;
    }
    //
    //
    //
    //
    public synchronized void init() {
        if (this.guice != null)
            return;
        /*ִ��׼������*/
        Hasor.info("compile startup sequence!");
        List<ModuleInfo> hasorModules = this.getModuleList();
        for (ModuleInfo mod : hasorModules)
            this.onReady(mod);
        /*ʹ�÷�Ӧ�Ѷ�ģ�����ѭ����������*/
        List<ModuleInfo> readOnlyModules = Collections.unmodifiableList(hasorModules);
        ModuleReactor reactor = new ModuleReactor(readOnlyModules);
        List<ModuleInfo> result = reactor.process();
        {
            /*����˳��*/
            List<ModuleInfo> infoList = getModuleList();
            infoList.clear();
            infoList.addAll(result);
        }
        /*����guice���Ҵ���init����*/
        this.getEventManager().doSyncEvent(LifeCycleEnum.PhaseEvent_Init.getValue(), (InitContext) this);//���ͽ׶��¼�
        this.guice = this.createInjector(null);
        Hasor.assertIsNotNull(this.guice, "can not be create Injector.");
    }
    @Override
    public synchronized void start() {
        if (this.running == true)
            return;
        /*����Guice���󣬲�������ģ���init�¼�*/
        if (this.guice == null)
            this.init();
        /*������ɳ�ʼ���ź�*/
        this.running = true;
        Hasor.info("send start sign.");
        this.getEventManager().doSyncEvent(LifeCycleEnum.PhaseEvent_Start.getValue(), (AppContext) this);//���ͽ׶��¼�
        ModuleInfo[] hasorModules = this.getModules();
        for (ModuleInfo mod : hasorModules) {
            if (mod == null)
                continue;
            this.onStart(mod);
        }
        /*��ӡģ��״̬*/
        this.printModState();
        Hasor.info("hasor started!");
    }
    @Override
    public synchronized void stop() {
        if (this.running == false)
            return;
        /*����ֹͣ�ź�*/
        this.running = false;
        Hasor.info("send stop sign.");
        this.getEventManager().doSyncEvent(LifeCycleEnum.PhaseEvent_Stop.getValue(), (AppContext) this);//���ͽ׶��¼�
        this.getEventManager().clean();
        //
        ModuleInfo[] hasorModules = this.getModules();
        for (ModuleInfo mod : hasorModules) {
            if (mod == null)
                continue;
            this.onStop(mod);
        }
        Hasor.info("hasor stoped!");
    }
    @Override
    public synchronized void destroy() {
        Hasor.info("send destroy sign.");
        this.getEventManager().doSyncEvent(LifeCycleEnum.PhaseEvent_Destroy.getValue(), (AppContext) this);//���ͽ׶��¼�
        this.getEventManager().clean();
        this.stop();
        ModuleInfo[] hasorModules = this.getModules();
        for (ModuleInfo mod : hasorModules) {
            if (mod == null)
                continue;
            this.onDestroy(mod);
        }
        Hasor.info("hasor destroy!");
    }
    @Override
    public boolean isRunning() {
        return this.running;
    }
    @Override
    public boolean isInit() {
        return this.guice != null;
    }
    //
    //
    //
    //
    /**׼���¼�*/
    protected void onReady(ModuleInfo forModule) {
        HasorModule modObj = forModule.getModuleObject();
        String eventName = modObj.getClass().getName();
        try {
            modObj.configuration((ModuleSettings) forModule);
            getEventManager().doSyncEvent(eventName, ModuleInfoBean.Prop_Ready, true);
        } catch (RuntimeException e) {
            getEventManager().doSyncEvent(eventName, ModuleInfoBean.Prop_Ready, false, e);
            if (this.forceModule)
                throw e;
        }
    }
    /**ģ��� init �������ڵ��á�*/
    protected void onInit(ModuleInfo forModule, Binder binder) {
        if (!forModule.isReady())
            return;/*������û��׼���õ�ģ��*/
        if (forModule.isDependencyInit() == false)
            return;/*������ģ����δ׼����*/
        ApiBinder apiBinder = this.newApiBinder(forModule, binder);
        HasorModule modObj = forModule.getModuleObject();
        String eventName = modObj.getClass().getName();
        try {
            modObj.init(apiBinder);
            if (apiBinder instanceof Module)
                binder.install((Module) apiBinder);
            //
            Hasor.info("init Event on : %s", modObj.getClass());
            getEventManager().doSyncEvent(eventName, ModuleInfoBean.Prop_Init, true);
        } catch (RuntimeException e) {
            getEventManager().doSyncEvent(eventName, ModuleInfoBean.Prop_Init, false, e);
            Hasor.error("%s in the init phase encounters an error.\n%s", forModule.getDisplayName(), e);
            if (this.forceModule)
                throw e;
        }
    }
    /**����ģ�������ź�*/
    protected void onStart(ModuleInfo forModule) {
        if (!forModule.isReady() /*׼��ʧ��*/|| !forModule.isInit()/*��ʼ��ʧ��*/|| forModule.isRunning()/*��������*/)
            return;
        if (forModule.isDependencyRunning() == false)
            return;/*������ģ����δ����*/
        //
        HasorModule modObj = forModule.getModuleObject();
        String eventName = modObj.getClass().getName();
        try {
            modObj.start(this);
            Hasor.info("start Event on : %s", modObj.getClass());
            getEventManager().doSyncEvent(eventName, ModuleInfoBean.Prop_Running, true);
        } catch (RuntimeException e) {
            Hasor.error("%s in the start phase encounters an error.\n%s", forModule.getDisplayName(), e);
            if (this.forceModule)
                throw e;
        }
    }
    /**����ģ��ֹͣ�ź�*/
    protected void onStop(ModuleInfo forModule) {
        if (!forModule.isReady() || !forModule.isRunning())
            return;/*������û��׼���õ�ģ�飬�������Ѿ�ֹͣ��ģ��*/
        //
        HasorModule modObj = forModule.getModuleObject();
        String eventName = modObj.getClass().getName();
        try {
            modObj.stop(this);
            Hasor.info("stop Event on : %s", modObj.getClass());
            getEventManager().doSyncEvent(eventName, ModuleInfoBean.Prop_Running, false);
        } catch (Exception e) {
            Hasor.error("%s in the stop phase encounters an error.\n%s", forModule.getDisplayName(), e);
        }
    }
    /**ģ��� destroy �������ڵ��á�*/
    protected void onDestroy(ModuleInfo forModule) {
        if (!forModule.isReady())
            return;/*������û��׼���õ�ģ��*/
        /*����δֹͣ��ģ�鷢��ֹͣ�ź�*/
        if (forModule.isRunning())
            this.onStop(forModule);
        //
        HasorModule modObj = forModule.getModuleObject();
        try {
            modObj.destroy(this);
            Hasor.info("destroy Event on : %s", modObj.getClass());
        } catch (Exception e) {
            Hasor.error("%s in the destroy phase encounters an error.\n%s", e);
        }
    }
    //
    //
    //
    //
    /**��ȡGuice�ӿ�*/
    public Injector getGuice() {
        return this.guice;
    }
    /**Ϊģ�鴴��ApiBinder*/
    protected ApiBinder newApiBinder(final ModuleInfo forModule, final Binder binder) {
        return new ApiBinderModule(this, forModule) {
            @Override
            public Binder getGuiceBinder() {
                return binder;
            }
        };
    }
    /**ͨ��guice����{@link Injector}���÷������ʹ����ģ��init��������*/
    protected Injector createInjector(Module[] guiceModules) {
        ArrayList<Module> guiceModuleSet = new ArrayList<Module>();
        guiceModuleSet.add(new MasterModule(this));
        if (guiceModules != null)
            for (Module mod : guiceModules)
                guiceModuleSet.add(mod);
        return Guice.createInjector(guiceModuleSet.toArray(new Module[guiceModuleSet.size()]));
    }
    /**��ӡģ��״̬*/
    protected void printModState() {
        List<ModuleInfo> modList = this.getModuleList();
        StringBuilder sb = new StringBuilder("");
        int size = String.valueOf(modList.size() - 1).length();
        for (int i = 0; i < modList.size(); i++) {
            ModuleInfo info = modList.get(i);
            sb.append(String.format("%0" + size + "d", i));
            sb.append('.');
            sb.append("-->[");
            //Running:������(start)��Initial:׼��ʧ�ܡ�Stopped:ֹͣ(stop)
            sb.append(!info.isReady() ? "Initial" : info.isRunning() ? "Running" : "Stopped");
            sb.append("] ");
            sb.append(info.getDisplayName());
            sb.append(" (");
            sb.append(info.getModuleObject().getClass());
            sb.append(")\n");
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        Hasor.info("Modules State List:\n%s", sb);
    }
}
/**���ฺ����ģ����Guice.configure�ڼ�ĳ�ʼ������*/
class MasterModule implements Module {
    private DefaultAppContext appContet;
    public MasterModule(DefaultAppContext appContet) {
        this.appContet = appContet;
    }
    @Override
    public void configure(Binder binder) {
        Hasor.info("send init sign...");
        ModuleInfo[] hasorModules = this.appContet.getModules();
        /*����ģ��init��������*/
        for (ModuleInfo mod : hasorModules)
            appContet.onInit(mod, binder);//������������
        Hasor.info("init modules finish.");
        ExtBind.doBind(binder, appContet);
    }
}
class ExtBind {
    public static void doBind(final Binder binder, final AppContext appContet) {
        /*��InitContext�����Provider*/
        binder.bind(InitContext.class).toProvider(new Provider<InitContext>() {
            @Override
            public InitContext get() {
                return appContet;
            }
        });
        /*��AppContext�����Provider*/
        binder.bind(AppContext.class).toProvider(new Provider<AppContext>() {
            @Override
            public AppContext get() {
                return appContet;
            }
        });
        /*��EventManager�����Provider*/
        binder.bind(EventManager.class).toProvider(new Provider<EventManager>() {
            @Override
            public EventManager get() {
                return appContet.getEventManager();
            }
        });
        /*��Settings�����Provider*/
        binder.bind(Settings.class).toProvider(new Provider<Settings>() {
            @Override
            public Settings get() {
                return appContet.getSettings();
            }
        });
        /*��WorkSpace�����Provider*/
        binder.bind(WorkSpace.class).toProvider(new Provider<WorkSpace>() {
            @Override
            public WorkSpace get() {
                return appContet.getWorkSpace();
            }
        });
        /*��Environment�����Provider*/
        binder.bind(Environment.class).toProvider(new Provider<Environment>() {
            @Override
            public Environment get() {
                return appContet.getEnvironment();
            }
        });
    }
}