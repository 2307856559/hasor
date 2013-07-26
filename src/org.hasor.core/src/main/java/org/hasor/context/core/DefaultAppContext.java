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
import java.util.List;
import org.hasor.Hasor;
import org.hasor.context.AdvancedEventManager;
import org.hasor.context.ApiBinder;
import org.hasor.context.AppContext;
import org.hasor.context.Environment;
import org.hasor.context.EventManager;
import org.hasor.context.HasorEventListener;
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
    private List<ModuleInfo> haosrModuleSet;
    private boolean          running;
    private Injector         guice;
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
    protected List<ModuleInfo> getModuleList() {
        if (this.haosrModuleSet == null)
            this.haosrModuleSet = new ArrayList<ModuleInfo>();
        return haosrModuleSet;
    }
    //
    /**���ģ��*/
    public void addModule(HasorModule hasorModule) {
        for (ModuleInfo info : this.getModuleList())
            if (info.getModuleObject() == hasorModule)
                return;
        this.getModuleList().add(new ModuleInfoBean(hasorModule, this));
    }
    /**ɾ��ģ��*/
    public void removeModule(HasorModule hasorModule) {
        ModuleInfo targetInfo = null;
        for (ModuleInfo info : this.getModuleList())
            if (info.getModuleObject() == hasorModule) {
                targetInfo = info;
                break;
            }
        if (targetInfo != null)
            this.getModuleList().remove(targetInfo);
    }
    /**�������ģ��*/
    public ModuleInfo[] getModules() {
        List<ModuleInfo> haosrModuleSet = getModuleList();
        return haosrModuleSet.toArray(new ModuleInfo[haosrModuleSet.size()]);
    }
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
    @Override
    public synchronized void start() {
        if (this.running == true)
            return;
        /*����Guice���󣬲�������ģ���init�¼�*/
        if (this.guice == null) {
            Hasor.info("send init sign...");
            this.getEventManager().doSyncEvent(LifeCycleEnum.PhaseEvent_Init.getValue(), (InitContext) this);//���ͽ׶��¼�
            this.guice = this.createInjector(null);
            Hasor.assertIsNotNull(this.guice, "can not be create Injector.");
        }
        /*������ɳ�ʼ���ź�*/
        this.running = true;
        Hasor.info("send start sign.");
        this.getEventManager().doSyncEvent(LifeCycleEnum.PhaseEvent_Start.getValue(), (AppContext) this);//���ͽ׶��¼�
        ModuleInfo[] hasorModules = this.getModules();
        if (hasorModules != null) {
            for (ModuleInfo mod : hasorModules) {
                if (mod == null)
                    continue;
                Hasor.info("start Event on : %s", mod.getClass());
                this.onStart(mod);
            }
        }
        /*ע���ʱ��*/
        if (this.getAdvancedEventManager() != null) {
            Hasor.info("addTimer for event %s.", LifeCycleEnum.PhaseEvent_Timer.getValue());
            this.getAdvancedEventManager().addTimer(LifeCycleEnum.PhaseEvent_Timer.getValue(), new HasorEventListener() {
                @Override
                public void onEvent(String event, Object[] params) {
                    onTimer();
                }
            });
        }
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
        if (hasorModules != null) {
            for (ModuleInfo mod : hasorModules) {
                if (mod == null)
                    continue;
                Hasor.info("stop Event on : %s", mod.getClass());
                this.onStop(mod);
            }
        }
        Hasor.info("hasor stoped!");
    }
    /**����*/
    public synchronized void destroy() {
        Hasor.info("send destroy sign.");
        this.getEventManager().doSyncEvent(LifeCycleEnum.PhaseEvent_Destroy.getValue(), (AppContext) this);//���ͽ׶��¼�
        this.getEventManager().clean();
        this.stop();
        ModuleInfo[] hasorModules = this.getModules();
        if (hasorModules != null) {
            for (ModuleInfo mod : hasorModules) {
                if (mod == null)
                    continue;
                Hasor.info("destroy Event on : %s", mod.getClass());
                this.onDestroy(mod);
            }
        }
        Hasor.info("hasor destroy!");
    }
    @Override
    public boolean isRunning() {
        return this.running;
    }
    //
    //
    /**��ʱ�������¼�*/
    protected void onTimer() {
        HasorEventListener[] eventListener = this.getEventManager().getEventListener(LifeCycleEnum.PhaseEvent_Timer.getValue());
        Object[] params = new Object[] { this };
        for (HasorEventListener event : eventListener)
            event.onEvent(LifeCycleEnum.PhaseEvent_Timer.getValue(), params);
    }
    /**ģ��� init �������ڵ��á�*/
    protected void onInit(ModuleInfo forModule, Binder binder) {
        ApiBinder apiBinder = this.newApiBinder(forModule, binder);
        forModule.getModuleObject().init(apiBinder);
        if (apiBinder instanceof Module)
            binder.install((Module) apiBinder);
    }
    /**����ģ�������ź�*/
    protected void onStart(ModuleInfo forModule) {
        forModule.getModuleObject().start(this);
    }
    /**����ģ��ֹͣ�ź�*/
    protected void onStop(ModuleInfo forModule) {
        forModule.getModuleObject().stop(this);
    }
    /**ģ��� destroy �������ڵ��á�*/
    protected void onDestroy(ModuleInfo forModule) {
        forModule.getModuleObject().destroy(this);
    }
}
class MasterModule implements Module {
    private DefaultAppContext appContet = null;
    public MasterModule(DefaultAppContext appContet) {
        this.appContet = appContet;
    }
    //
    @Override
    public void configure(Binder binder) {
        /*ȷ������˳��*/
        Hasor.info("compile startup sequence!");
        ModuleInfo[] hasorModules = this.appContet.getModules();
        if (hasorModules != null)
            for (ModuleInfo mod : hasorModules)
                if (mod != null)
                    mod.getModuleObject().configuration((ModuleSettings) mod);
        hasorModules = new ModuleReactor().getResult(hasorModules);
        /*����ģ��init��������*/
        if (hasorModules != null)
            for (ModuleInfo mod : hasorModules) {
                if (mod == null)
                    continue;
                Hasor.info("init Event on : %s", mod.getClass());
                appContet.onInit(mod, binder);//�����¼�
            }
        Hasor.info("init modules finish.");
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
        /*��PhaseEventManager�����Provider*/
        binder.bind(AdvancedEventManager.class).toProvider(new Provider<AdvancedEventManager>() {
            @Override
            public AdvancedEventManager get() {
                return (AdvancedEventManager) appContet.getEventManager();
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