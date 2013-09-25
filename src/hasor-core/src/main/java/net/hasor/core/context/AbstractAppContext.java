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
package net.hasor.core.context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.core.Environment;
import net.hasor.core.EventManager;
import net.hasor.core.Module;
import net.hasor.core.ModuleInfo;
import net.hasor.core.Settings;
import net.hasor.core.binder.ApiBinderModule;
import net.hasor.core.binder.BeanInfo;
import net.hasor.core.module.AbstractModulePropxy;
import net.hasor.core.module.ModuleReactor;
import org.more.UndefinedException;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
/**
 * {@link AppContext}�ӿڵĳ���ʵ���ࡣ
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public abstract class AbstractAppContext implements AppContext {
    private Object context;
    /**��ȡ������*/
    public Object getContext() {
        return this.context;
    }
    /**����������*/
    public void setContext(Object context) {
        this.context = context;
    }
    /**��ȡϵͳ����ʱ��*/
    public long getStartTime() {
        return this.getEnvironment().getStartTime();
    };
    /**��ʾAppContext�Ƿ�׼���á�*/
    public boolean isReady() {
        return this.getGuice() != null;
    }
    /**��ȡӦ�ó������á�*/
    public Settings getSettings() {
        return this.getEnvironment().getSettings();
    };
    /**��ȡ�¼������ӿڡ�*/
    public EventManager getEventManager() {
        return this.getEnvironment().getEventManager();
    }
    //
    //
    //-----------------------------------------------------------------------------------------Bean
    private Map<String, BeanInfo> beanInfoMap;
    private Injector              injector = null;
    /**ͨ������ȡBean�����͡�*/
    public <T> Class<T> getBeanType(String name) {
        Hasor.assertIsNotNull(name, "bean name is null.");
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        BeanInfo info = this.beanInfoMap.get(name);
        if (info != null)
            return (Class<T>) info.getBeanType();
        throw null;
    }
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType) {
        return this.getEnvironment().getClassSet(featureType);
    }
    /**�������Ŀ�����͵�Bean�򷵻�Bean�����ơ�*/
    public String getBeanName(Class<?> targetClass) {
        Hasor.assertIsNotNull(targetClass, "targetClass is null.");
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        for (Entry<String, BeanInfo> ent : this.beanInfoMap.entrySet()) {
            if (ent.getValue().getBeanType() == targetClass)
                return ent.getKey();
        }
        return null;
    }
    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    public String[] getBeanNames() {
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        return this.beanInfoMap.keySet().toArray(new String[this.beanInfoMap.size()]);
    }
    //
    public BeanInfo getBeanInfo(String name) {
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        return this.beanInfoMap.get(name);
    }
    //
    private void collectBeanInfos() {
        this.beanInfoMap = new HashMap<String, BeanInfo>();
        Provider<BeanInfo>[] beanInfoProviderArray = this.getProviderByBindingType(BeanInfo.class);
        if (beanInfoProviderArray == null)
            return;
        for (Provider<BeanInfo> entry : beanInfoProviderArray) {
            BeanInfo beanInfo = entry.get();
            this.beanInfoMap.put(beanInfo.getName(), beanInfo);
        }
    }
    /**ͨ�����ƴ���beanʵ����ʹ��guice�������ȡ��bean�������������{@link UndefinedException}�����쳣��*/
    public <T> T getBean(String name) {
        BeanInfo beanInfo = this.getBeanInfo(name);
        if (beanInfo == null)
            throw new UndefinedException("bean ��" + name + "�� is undefined.");
        return (T) this.getGuice().getInstance(beanInfo.getBeanType());
    };
    /**���Guice������*/
    public Injector getGuice() {
        return this.injector;
    }
    //
    //
    //----------------------------------------------------------------------------------Core Method
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getInstance(Class<T> beanType) {
        return this.getGuice().getInstance(beanType);
    }
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> T[] getInstanceByBindingType(Class<T> bindingType) {
        ArrayList<T> providerList = new ArrayList<T>();
        TypeLiteral<T> BindingType_DEFS = TypeLiteral.get(bindingType);
        for (Binding<T> entry : this.getGuice().findBindingsByType(BindingType_DEFS)) {
            Provider<T> bindingTypeProvider = entry.getProvider();
            providerList.add(bindingTypeProvider.get());
        }
        //
        if (providerList.isEmpty())
            return null;
        return (T[]) providerList.toArray();
    }
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> Provider<T>[] getProviderByBindingType(Class<T> bindingType) {
        ArrayList<Provider<T>> providerList = new ArrayList<Provider<T>>();
        TypeLiteral<T> BindingType_DEFS = TypeLiteral.get(bindingType);
        for (Binding<T> entry : this.getGuice().findBindingsByType(BindingType_DEFS)) {
            Provider<T> bindingTypeProvider = entry.getProvider();
            providerList.add(bindingTypeProvider);
        }
        //
        if (providerList.isEmpty())
            return null;
        return providerList.toArray(new Provider[providerList.size()]);
    }
    //
    //
    //---------------------------------------------------------------------------------------Module
    /**ѡȡ��������ָ��ģ��Ĵ������͡�*/
    protected AbstractModulePropxy generateModulePropxy(Module hasorModule) {
        for (AbstractModulePropxy info : this.getModulePropxyList())
            if (info.getTarget() == hasorModule)
                return info;
        return new ContextModulePropxy(hasorModule, this);
    }
    /**���ģ�飬��������Ѿ���ʼ����ô������{@link IllegalStateException}�쳣��*/
    public synchronized ModuleInfo addModule(Module hasorModule) {
        if (this.isReady())
            throw new IllegalStateException("context is inited.");
        AbstractModulePropxy propxy = this.generateModulePropxy(hasorModule);
        List<AbstractModulePropxy> propxyList = this.getModulePropxyList();
        if (propxyList.contains(propxy) == false)
            propxyList.add(propxy);
        return propxy;
    }
    /**ɾ��ģ�飬��������Ѿ���ʼ����ô������{@link IllegalStateException}�쳣��*/
    public synchronized boolean removeModule(Module hasorModule) {
        if (this.isReady())
            throw new IllegalStateException("context is inited.");
        AbstractModulePropxy targetInfo = null;
        for (AbstractModulePropxy info : this.getModulePropxyList())
            if (info.getTarget() == hasorModule) {
                targetInfo = info;
                break;
            }
        if (targetInfo != null) {
            this.getModulePropxyList().remove(targetInfo);
            return true;
        }
        return false;
    }
    /**�������ģ��*/
    public ModuleInfo[] getModules() {
        List<AbstractModulePropxy> haosrModuleList = this.getModulePropxyList();
        ModuleInfo[] infoArray = new ModuleInfo[haosrModuleList.size()];
        for (int i = 0; i < haosrModuleList.size(); i++)
            infoArray[i] = haosrModuleList.get(i);
        return infoArray;
    }
    //
    private List<AbstractModulePropxy> haosrModuleSet;
    /**�������߻�����ڴ������ModuleInfo�ļ��϶���*/
    protected List<AbstractModulePropxy> getModulePropxyList() {
        if (this.haosrModuleSet == null)
            this.haosrModuleSet = new ArrayList<AbstractModulePropxy>();
        return haosrModuleSet;
    }
    //
    /**Ϊģ�鴴��ApiBinder*/
    protected ApiBinderModule newApiBinder(final AbstractModulePropxy forModule, final Binder binder) {
        return new ApiBinderModule(this.getEnvironment(), forModule) {
            public Binder getGuiceBinder() {
                return binder;
            }
            public DependencySettings dependency() {
                return forModule;
            }
        };
    }
    /**ͨ��guice����{@link Injector}���÷������ʹ����ģ��init��������*/
    protected Injector createInjector(com.google.inject.Module[] guiceModules) {
        ArrayList<com.google.inject.Module> guiceModuleSet = new ArrayList<com.google.inject.Module>();
        guiceModuleSet.add(new MasterModule(this));
        if (guiceModules != null)
            for (com.google.inject.Module mod : guiceModules)
                guiceModuleSet.add(mod);
        return Guice.createInjector(guiceModuleSet.toArray(new com.google.inject.Module[guiceModuleSet.size()]));
    }
    /**��ӡģ��״̬*/
    protected void printModState() {
        List<AbstractModulePropxy> modList = this.getModulePropxyList();
        StringBuilder sb = new StringBuilder("");
        int size = String.valueOf(modList.size() - 1).length();
        for (int i = 0; i < modList.size(); i++) {
            ModuleInfo info = modList.get(i);
            sb.append(String.format("%0" + size + "d", i));
            sb.append('.');
            sb.append("-->[");
            //Running:������(start)��Initial:׼��ʧ�ܡ�Stopped:ֹͣ(stop)
            sb.append(!info.isReady() ? "Initial" : info.isStart() ? "Running" : "Stopped");
            sb.append("] ");
            sb.append(info.getDisplayName());
            sb.append(" (");
            sb.append(info.getTarget().getClass());
            sb.append(")\n");
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        Hasor.info("Modules State List:\n%s", sb);
    }
    //
    //
    //---------------------------------------------------------------------------------------Module
    private boolean     isStart;
    private Environment environment;
    //
    /**��ȡ�����ӿڡ�*/
    public Environment getEnvironment() {
        if (this.environment == null)
            this.environment = this.createEnvironment();
        return this.environment;
    }
    /**������������*/
    protected abstract Environment createEnvironment();
    /**�ж������Ƿ�������״̬*/
    public boolean isStart() {
        return this.isStart;
    }
    /**��ʼ����������ע������ֻ�ܱ���ʼ��һ�Ρ�*/
    protected void initContext() {
        if (this.injector != null)
            return;
        /*����ContextEvent_Init�¼������Ҵ�������guice*/
        Hasor.info("send init sign.");
        this.getEnvironment().getEventManager().doSyncEventIgnoreThrow(ContextEvent_Init, this);
        this.injector = this.createInjector(null);
        Hasor.assertIsNotNull(this.injector, "can not be create Injector.");
        /*ʹ�÷�Ӧ�Ѷ�ģ�����ѭ����������*/
        List<ModuleInfo> readOnlyModules = new ArrayList<ModuleInfo>();
        for (AbstractModulePropxy amp : this.getModulePropxyList())
            readOnlyModules.add(amp);
        //
        ModuleReactor reactor = new ModuleReactor(readOnlyModules);
        List<ModuleInfo> result = reactor.process();
        List<AbstractModulePropxy> propxyList = this.getModulePropxyList();
        propxyList.clear();
        for (ModuleInfo info : result)
            propxyList.add((AbstractModulePropxy) info);
        // 
        Hasor.info("the init is completed!");
    }
    /**������������ģ�鷢�������źţ�����������״̬��ΪStart�����÷����᳢��init����ģ�飩*/
    public synchronized void start() {
        if (this.isStart == true)
            return;
        this.initContext();
        Hasor.info("send start sign.");
        this.getEnvironment().getEventManager().doSyncEventIgnoreThrow(ContextEvent_Start, this);
        List<AbstractModulePropxy> modulePropxyList = this.getModulePropxyList();
        for (AbstractModulePropxy mod : modulePropxyList)
            mod.start(this);
        this.isStart = true;
        /*��ӡģ��״̬*/
        this.printModState();
        Hasor.info("hasor started!");
    }
    /**ֹͣ��������ģ�鷢��ֹͣ�źţ�����������״̬��ΪStop��*/
    public synchronized void stop() {
        if (this.isStart == false)
            return;
        Hasor.info("send stop sign.");
        this.getEnvironment().getEventManager().doSyncEventIgnoreThrow(ContextEvent_Stop, this);
        List<AbstractModulePropxy> modulePropxyList = this.getModulePropxyList();
        for (AbstractModulePropxy mod : modulePropxyList)
            mod.stop(this);
        this.isStart = false;
        /*��ӡģ��״̬*/
        this.printModState();
        Hasor.info("hasor stoped!");
    }
    /**���³�ʼ��������������ǿ������ģ�鶼���³�ʼ��(Init)������(Start)*/
    public synchronized void reboot() {
        this.stop();
        this.getEnvironment().release();
        this.environment = null;
        this.injector = null;
        this.beanInfoMap = null;
        this.haosrModuleSet = null;
        this.start();
    }
    /**����ִֹͣ�У��������startָ�*/
    public synchronized void restart() {
        this.stop();
        this.start();
    }
}
/**���ฺ����ģ����Guice.configure�ڼ�ĳ�ʼ������*/
class MasterModule implements com.google.inject.Module {
    private AbstractAppContext appContet;
    public MasterModule(AbstractAppContext appContet) {
        this.appContet = appContet;
    }
    public void configure(Binder binder) {
        Hasor.info("send init sign...");
        List<AbstractModulePropxy> modulePropxyList = this.appContet.getModulePropxyList();
        /*����ģ��init��������*/
        for (AbstractModulePropxy forModule : modulePropxyList) {
            ApiBinderModule apiBinder = this.appContet.newApiBinder(forModule, binder);
            forModule.init(apiBinder);//������������ 
            apiBinder.configure(binder);
        }
        Hasor.info("init modules finish.");
        ExtBind.doBind(binder, appContet);
    }
}
/***/
class ContextModulePropxy extends AbstractModulePropxy {
    public ContextModulePropxy(Module targetModule, AbstractAppContext appContext) {
        super(targetModule, appContext);
    }
    protected AbstractModulePropxy getInfo(Class<? extends Module> targetModule, AppContext appContext) {
        List<AbstractModulePropxy> modulePropxyList = ((AbstractAppContext) appContext).getModulePropxyList();
        for (AbstractModulePropxy modulePropxy : modulePropxyList)
            if (targetModule == modulePropxy.getTarget().getClass())
                return modulePropxy;
        throw new UndefinedException(targetModule.getName() + " module is Undefined!");
    }
}
/**��*/
class ExtBind {
    public static void doBind(final Binder binder, final AppContext appContet) {
        /*��Environment�����Provider*/
        binder.bind(Environment.class).toProvider(new Provider<Environment>() {
            public Environment get() {
                return appContet.getEnvironment();
            }
        });
        /*��EventManager�����Provider*/
        binder.bind(EventManager.class).toProvider(new Provider<EventManager>() {
            public EventManager get() {
                return appContet.getEnvironment().getEventManager();
            }
        });
        /*��Settings�����Provider*/
        binder.bind(Settings.class).toProvider(new Provider<Settings>() {
            public Settings get() {
                return appContet.getSettings();
            }
        });
        /*��AppContext�����Provider*/
        binder.bind(AppContext.class).toProvider(new Provider<AppContext>() {
            public AppContext get() {
                return appContet;
            }
        });
    }
}