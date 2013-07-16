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
import org.hasor.context.ApiBinder;
import org.hasor.context.AppContext;
import org.hasor.context.Environment;
import org.hasor.context.EventManager;
import org.hasor.context.HasorModule;
import org.hasor.context.InitContext;
import org.hasor.context.PhaseEventManager;
import org.hasor.context.Settings;
import org.hasor.context.WorkSpace;
import org.hasor.context.binder.ApiBinderModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
/**
 * {@link AppContext}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public class DefaultAppContext extends AbstractAppContext {
    private List<HasorModule> haosrModuleSet;
    private boolean           running;
    private Injector          guice;
    //
    public DefaultAppContext() throws IOException {
        super();
    }
    public DefaultAppContext(String mainConfig) throws IOException {
        super(mainConfig);
    }
    protected List<HasorModule> getModuleList() {
        if (this.haosrModuleSet == null)
            this.haosrModuleSet = new ArrayList<HasorModule>();
        return haosrModuleSet;
    }
    //
    /**���ģ��*/
    public void addModule(HasorModule contextListener) {
        if (this.getModuleList().contains(contextListener) == false)
            this.getModuleList().add(contextListener);
    }
    /**ɾ��ģ��*/
    public void removeModule(HasorModule contextListener) {
        if (this.getModuleList().contains(contextListener) == true)
            this.getModuleList().remove(contextListener);
    }
    /**�������ģ��*/
    public HasorModule[] getModules() {
        List<HasorModule> haosrModuleSet = getModuleList();
        return haosrModuleSet.toArray(new HasorModule[haosrModuleSet.size()]);
    }
    /**��ȡGuice�ӿ�*/
    public Injector getGuice() {
        return this.guice;
    }
    /**Ϊģ�鴴��ApiBinder*/
    protected ApiBinder newApiBinder(final HasorModule forModule, final Binder binder) {
        return new ApiBinderModule(this) {
            @Override
            public Binder getGuiceBinder() {
                return binder;
            }
        };
    }
    /**ͨ��guice����{@link Injector}���÷������ʹ����ģ��init��������*/
    protected Injector createInjector(com.google.inject.Module[] guiceModules) {
        //1.����ApiBinderModule��
        final AppContext appContet = this;
        final com.google.inject.Module masterBind = new com.google.inject.Module() {
            @Override
            public void configure(Binder binder) {
                /*����ģ��init��������*/
                HasorModule[] hasorModules = getModules();
                if (hasorModules != null)
                    for (HasorModule mod : hasorModules) {
                        if (mod == null)
                            continue;
                        Hasor.info("init Event on : %s", mod.getClass());
                        onInit(mod, binder);//�����¼�
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
                binder.bind(PhaseEventManager.class).toProvider(new Provider<PhaseEventManager>() {
                    @Override
                    public PhaseEventManager get() {
                        return (PhaseEventManager) appContet.getEventManager();
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
        };
        //2.
        ArrayList<com.google.inject.Module> guiceModuleSet = new ArrayList<com.google.inject.Module>();
        guiceModuleSet.add(masterBind);
        if (guiceModules != null)
            for (com.google.inject.Module mod : guiceModules)
                guiceModuleSet.add(mod);
        return Guice.createInjector(guiceModuleSet.toArray(new com.google.inject.Module[guiceModuleSet.size()]));
    }
    @Override
    public synchronized void start() {
        if (this.running == true)
            return;
        /*����Guice���󣬲�������ģ���init�¼�*/
        if (this.guice == null) {
            Hasor.info("send init sign...");
            ((PhaseEventManager) this.getEventManager()).popPhaseEvent(PhaseEvent_Init, (InitContext) this);//���ͽ׶��¼�
            this.guice = this.createInjector(null);
            Hasor.assertIsNotNull(this.guice, "can not be create Injector.");
        }
        /*������ɳ�ʼ���ź�*/
        this.running = true;
        Hasor.info("send start sign.");
        ((PhaseEventManager) this.getEventManager()).popPhaseEvent(PhaseEvent_Start, (AppContext) this);//���ͽ׶��¼�
        HasorModule[] hasorModules = this.getModules();
        if (hasorModules != null) {
            for (HasorModule mod : hasorModules) {
                if (mod == null)
                    continue;
                Hasor.info("start Event on : %s", mod.getClass());
                this.onStart(mod);
            }
        }
        Hasor.info("hasor started!");
    }
    @Override
    public synchronized void stop() {
        if (this.running == false)
            return;
        /*������ɳ�ʼ���ź�*/
        this.running = false;
        Hasor.info("send start sign.");
        ((PhaseEventManager) this.getEventManager()).popPhaseEvent(PhaseEvent_Stop, (AppContext) this);//���ͽ׶��¼�
        HasorModule[] hasorModules = this.getModules();
        if (hasorModules != null) {
            for (HasorModule mod : hasorModules) {
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
        this.stop();
        Hasor.info("send destroy sign.");
        ((PhaseEventManager) this.getEventManager()).popPhaseEvent(PhaseEvent_Destroy, (AppContext) this);//���ͽ׶��¼�
        HasorModule[] hasorModules = this.getModules();
        if (hasorModules != null) {
            for (HasorModule mod : hasorModules) {
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
    protected void onTimer() {/**/}
    /**ģ��� init �������ڵ��á�*/
    protected void onInit(HasorModule forModule, Binder binder) {
        ApiBinder apiBinder = this.newApiBinder(forModule, binder);
        forModule.init(apiBinder);
        if (apiBinder instanceof com.google.inject.Module)
            binder.install((com.google.inject.Module) apiBinder);
    }
    /**����ģ�������ź�*/
    protected void onStart(HasorModule forModule) {
        forModule.start(this);
    }
    /**����ģ��ֹͣ�ź�*/
    protected void onStop(HasorModule forModule) {
        forModule.stop(this);
    }
    /**ģ��� destroy �������ڵ��á�*/
    protected void onDestroy(HasorModule forModule) {
        forModule.destroy(this);
    }
    // 
}