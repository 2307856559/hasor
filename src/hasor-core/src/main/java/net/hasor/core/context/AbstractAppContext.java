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
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.hasor.core.AppContext;
import net.hasor.core.AppContextAware;
import net.hasor.core.Environment;
import net.hasor.core.EventManager;
import net.hasor.core.Hasor;
import net.hasor.core.Module;
import net.hasor.core.ModuleInfo;
import net.hasor.core.Settings;
import net.hasor.core.binder.AbstractApiBinder;
import net.hasor.core.binder.BeanMetaData;
import net.hasor.core.module.ModulePropxy;
import net.hasor.core.module.ModuleReactor;
import org.more.UndefinedException;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
/**
 * ������ AbstractAppContext �� {@link AppContext} �ӿڵĻ���ʵ�֡�
 * <p>����װ�˴���ϸ�ڴ��룬���Է����ͨ���������������ص�������֧�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public abstract class AbstractAppContext implements AppContext {
    //
    //-----------------------------------------------------------------------------------------Bean
    private Map<String, BeanMetaData> beanInfoMap;
    private void collectBeanInfos() {
        this.beanInfoMap = new HashMap<String, BeanMetaData>();
        List<Provider<BeanMetaData>> beanInfoProviderArray = this.findProviderByType(BeanMetaData.class);
        if (beanInfoProviderArray == null)
            return;
        for (Provider<BeanMetaData> entry : beanInfoProviderArray) {
            BeanMetaData beanMetaData = entry.get();
            this.beanInfoMap.put(beanMetaData.getName(), beanMetaData);
        }
    }
    /**ͨ������ȡBean�����͡�*/
    public <T> Class<T> getBeanType(String name) {
        Hasor.assertIsNotNull(name, "bean name is null.");
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        BeanMetaData info = this.beanInfoMap.get(name);
        if (info != null)
            return (Class<T>) info.getBeanType();
        throw null;
    }
    /**�������Ŀ�����͵�Bean�򷵻�Bean�����ơ�*/
    public String getBeanName(Class<?> targetClass) {
        Hasor.assertIsNotNull(targetClass, "targetClass is null.");
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        for (Entry<String, BeanMetaData> ent : this.beanInfoMap.entrySet()) {
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
    /**ͨ�����ƴ���beanʵ����ʹ��guice�������ȡ��bean�������������{@link UndefinedException}�����쳣��*/
    public <T> T getBean(String name) {
        BeanMetaData beanMetaData = this.getBeanInfo(name);
        if (beanMetaData == null)
            throw new UndefinedException("bean ��" + name + "�� is undefined.");
        return (T) this.getGuice().getInstance(beanMetaData.getBeanType());
    };
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getInstance(Class<T> beanType) {
        return this.getGuice().getInstance(beanType);
    }
    /**��ȡ Bean �������ӿڡ�*/
    public BeanMetaData getBeanInfo(String name) {
        if (this.beanInfoMap == null)
            this.collectBeanInfos();
        return this.beanInfoMap.get(name);
    }
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> List<T> findBeanByType(Class<T> bindingType) {
        ArrayList<T> providerList = new ArrayList<T>();
        TypeLiteral<T> BindingType_DEFS = TypeLiteral.get(bindingType);
        for (Binding<T> entry : this.getGuice().findBindingsByType(BindingType_DEFS)) {
            Provider<T> bindingTypeProvider = entry.getProvider();
            providerList.add(bindingTypeProvider.get());
        }
        //
        if (providerList.isEmpty())
            return null;
        return providerList;
    }
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> List<Provider<T>> findProviderByType(Class<T> bindingType) {
        ArrayList<Provider<T>> providerList = new ArrayList<Provider<T>>();
        TypeLiteral<T> BindingType_DEFS = TypeLiteral.get(bindingType);
        for (Binding<T> entry : this.getGuice().findBindingsByType(BindingType_DEFS)) {
            Provider<T> bindingTypeProvider = entry.getProvider();
            providerList.add(bindingTypeProvider);
        }
        //
        if (providerList.isEmpty())
            return null;
        return providerList;
    }
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> T findBeanByType(String withName, Class<T> bindingType) {
        Provider<T> provider = findProviderByType(withName, bindingType);
        if (provider == null)
            return null;
        return provider.get();
    }
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> Provider<T> findProviderByType(String withName, Class<T> bindingType) {
        TypeLiteral<T> BindingType_DEFS = TypeLiteral.get(bindingType);
        Named named = Names.named(withName);
        //
        for (Binding<T> entry : this.getGuice().findBindingsByType(BindingType_DEFS)) {
            Provider<T> bindingTypeProvider = entry.getProvider();
            Annotation nameAnno = entry.getKey().getAnnotation();
            if (!named.equals(nameAnno))/*�μ� Names.named��*/
                continue;
            return bindingTypeProvider;
        }
        return null;
    }
    //
    //--------------------------------------------------------------------------------------Context
    private Injector injector = null;
    private Object   context;
    /**���Guice������*/
    public Injector getGuice() {
        return this.injector;
    }
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
    private Environment environment;
    /**��ȡ�����ӿڡ�*/
    public Environment getEnvironment() {
        if (this.environment == null)
            this.environment = this.createEnvironment();
        return this.environment;
    }
    /**������������*/
    protected abstract Environment createEnvironment();
    /**��ȡ�¼������ӿڡ�*/
    public EventManager getEventManager() {
        return this.getEnvironment().getEventManager();
    }
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType) {
        return this.getEnvironment().getClassSet(featureType);
    }
    //---------------------------------------------------------------------------------------Module
    private List<ModulePropxy> moduleSet;
    /**�������߻�����ڴ������ModuleInfo�ļ��϶���*/
    protected List<ModulePropxy> getModuleList() {
        if (this.moduleSet == null)
            this.moduleSet = new ArrayList<ModulePropxy>();
        return moduleSet;
    }
    /**���ģ�飬��������Ѿ���ʼ����ô������{@link IllegalStateException}�쳣��*/
    public synchronized ModuleInfo addModule(Module hasorModule) {
        if (this.isReady())
            throw new IllegalStateException("context is inited.");
        /*��ֹ�ظ����*/
        for (ModulePropxy info : this.getModuleList())
            if (info.getTarget() == hasorModule)
                return info;
        /*���ģ��*/
        ModulePropxy propxy = new ContextModulePropxy(hasorModule, this);
        List<ModulePropxy> propxyList = this.getModuleList();
        if (propxyList.contains(propxy) == false)
            propxyList.add(propxy);
        return propxy;
    }
    /**ɾ��ģ�飬��������Ѿ���ʼ����ô������{@link IllegalStateException}�쳣��*/
    public synchronized boolean removeModule(Module hasorModule) {
        if (this.isReady())
            throw new IllegalStateException("context is inited.");
        ModulePropxy targetInfo = null;
        for (ModulePropxy info : this.getModuleList())
            if (info.getTarget() == hasorModule) {
                targetInfo = info;
                break;
            }
        if (targetInfo != null) {
            this.getModuleList().remove(targetInfo);
            return true;
        }
        return false;
    }
    /**�������ģ��*/
    public ModuleInfo[] getModules() {
        List<ModulePropxy> haosrModuleList = this.getModuleList();
        ModuleInfo[] infoArray = new ModuleInfo[haosrModuleList.size()];
        for (int i = 0; i < haosrModuleList.size(); i++)
            infoArray[i] = haosrModuleList.get(i);
        return infoArray;
    }
    //----------------------------------------------------------------------------------------Utils
    private boolean isStart;
    /**�ж������Ƿ�������״̬*/
    public boolean isStart() {
        return this.isStart;
    }
    /**Ϊģ�鴴��ApiBinder*/
    protected AbstractApiBinder newApiBinder(final ModulePropxy forModule, Binder binder) {
        return new AbstractApiBinder(binder, this.getEnvironment(), forModule) {
            public DependencySettings dependency() {
                return forModule;
            }
        };
    }
    /**ͨ��guice����{@link Injector}���÷������ʹ����ģ��init��������*/
    protected Injector createInjector(com.google.inject.Module[] guiceModules) {
        return Guice.createInjector(guiceModules);
    }
    /**��ӡģ��״̬*/
    protected static void printModState(AbstractAppContext appContext) {
        List<ModulePropxy> modList = appContext.getModuleList();
        StringBuilder sb = new StringBuilder("");
        int size = String.valueOf(modList.size() - 1).length();
        for (int i = 0; i < modList.size(); i++) {
            ModuleInfo info = modList.get(i);
            sb.append(String.format("%0" + size + "d", i));
            sb.append('.');
            sb.append("-->[");
            //Running:������(start)��Failed:׼��ʧ�ܡ�Stopped:ֹͣ(stop)
            sb.append(!info.isReady() ? "Failed " : info.isStart() ? "Running" : "Stopped");
            sb.append("] ");
            sb.append(info.getDisplayName());
            sb.append(" (");
            sb.append(info.getTarget().getClass());
            sb.append(")\n");
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        Hasor.logInfo("Modules State List:\n%s", sb);
    }
    //-----------------------------------------------------------------------------------------Life
    /**��ʼ����������ע������ֻ�ܱ���ʼ��һ�Ρ��÷����ڴ��� Guice �����л����� doInitialize �����ĵ��á�*/
    protected void initContext() {
        if (this.injector != null)
            return;
        /*1.��������guice*/
        Hasor.logInfo("createInjector...");
        this.injector = this.createInjector(new com.google.inject.Module[] { new RootInitializeModule(this) });
        Hasor.assertIsNotNull(this.injector, "can not be create Injector.");
        /*2.ʹ�÷�Ӧ�Ѷ�ģ�����ѭ����������*/
        this.doReactor();
        /*3.���init*/
        Hasor.logInfo("the init is completed!");
    }
    /**������������ģ�鷢�������źţ�����������״̬��ΪStart�����÷����᳢��init����ģ�飩*/
    public synchronized void start() {
        if (this.isStart() == true)
            return;
        /*1.��ʼ��*/
        this.initContext();
        /*2.����*/
        this.doStart();
    }
    /**ֹͣ��������ģ�鷢��ֹͣ�źţ�����������״̬��ΪStop��*/
    public synchronized void stop() {
        if (this.isStart() == false)
            return;
        doStop();
    }
    /**���³�ʼ��������������ǿ������ģ�鶼���³�ʼ��(Init)������(Start)*/
    public synchronized void reboot() {
        this.stop();
        this.getEnvironment().release();
        this.environment = null;
        this.injector = null;
        this.beanInfoMap = null;
        this.moduleSet.clear();
        this.start();
    }
    /**����ִֹͣ�У��������startָ�*/
    public synchronized void restart() {
        this.stop();
        this.start();
    }
    //--------------------------------------------------------------------------------------Process
    /**ִ�� Initialize ���̡�*/
    protected void doInitialize(Binder binder) {
        Hasor.logInfo("send init sign...");
        this.getEventManager().doSyncEventIgnoreThrow(ContextEvent_Initialize, getEnvironment(), binder);
        //
        List<ModulePropxy> modulePropxyList = this.getModuleList();
        /*����ģ��init��������*/
        for (ModulePropxy forModule : modulePropxyList) {
            AbstractApiBinder apiBinder = this.newApiBinder(forModule, binder);
            forModule.init(apiBinder);//������������ 
            apiBinder.configure(binder);
        }
        this.doBind(binder);
        //
        this.getEventManager().doSyncEventIgnoreThrow(ContextEvent_Initialized, getEnvironment(), binder);
        Hasor.logInfo("init modules finish.");
    }
    /**ʹ�÷�Ӧ�Ѷ�ģ�����ѭ����������*/
    private void doReactor() {
        List<ModuleInfo> readOnlyModules = new ArrayList<ModuleInfo>();
        for (ModulePropxy amp : this.getModuleList())
            readOnlyModules.add(amp);
        ModuleReactor reactor = new ModuleReactor(readOnlyModules);//������Ӧ��
        List<ModuleInfo> result = reactor.process();
        List<ModulePropxy> propxyList = this.getModuleList();
        propxyList.clear();
        for (ModuleInfo info : result)
            propxyList.add((ModulePropxy) info);
    }
    /**������������ģ�鷢�������źţ�����������״̬��ΪStart�����÷����᳢��init����ģ�飩*/
    protected void doStart() {
        Hasor.logInfo("send start sign.");
        /*1.ִ��Aware֪ͨ*/
        List<AppContextAware> awareList = this.findBeanByType(AppContextAware.class);
        if (awareList == null)
            return;
        for (AppContextAware weak : awareList)
            weak.setAppContext(this);
        /*2.���������¼�*/
        this.getEnvironment().getEventManager().doSyncEventIgnoreThrow(ContextEvent_Start, this);
        List<ModulePropxy> modulePropxyList = this.getModuleList();
        for (ModulePropxy mod : modulePropxyList)
            mod.start(this);
        this.isStart = true;
        /*3.��ӡģ��״̬*/
        printModState(this);
        Hasor.logInfo("hasor started!");
    }
    /**ִ�� Stop ���̡�*/
    protected void doStop() {
        /*1.����ֹͣ�¼�*/
        Hasor.logInfo("send stop sign.");
        List<ModulePropxy> modulePropxyList = this.getModuleList();
        for (ModulePropxy mod : modulePropxyList)
            mod.stop(this);
        this.getEnvironment().getEventManager().doSyncEventIgnoreThrow(ContextEvent_Stoped, this);
        this.isStart = false;
        /*2.��ӡģ��״̬*/
        printModState(this);
        Hasor.logInfo("hasor stoped!");
    }
    /**��������г�ʼ������֮����ã������� Guice ��һЩԤ�ȶ�������͡�*/
    protected void doBind(Binder binder) {
        final AbstractAppContext appContet = this;
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
    /**������ Init �׶ε�����ࡣ*/
    private class RootInitializeModule implements com.google.inject.Module {
        private AbstractAppContext appContet;
        public RootInitializeModule(AbstractAppContext appContet) {
            this.appContet = appContet;
        }
        public void configure(Binder binder) {
            this.appContet.doInitialize(binder);
        }
    }
    /**λ�������� ModulePropxy �������ʵ��*/
    private class ContextModulePropxy extends ModulePropxy {
        public ContextModulePropxy(Module targetModule, AbstractAppContext appContext) {
            super(targetModule, appContext);
        }
        protected ModulePropxy getInfo(Class<? extends Module> targetModule, AppContext appContext) {
            List<ModulePropxy> modulePropxyList = ((AbstractAppContext) appContext).getModuleList();
            for (ModulePropxy modulePropxy : modulePropxyList)
                if (targetModule == modulePropxy.getTarget().getClass())
                    return modulePropxy;
            throw new UndefinedException(targetModule.getName() + " module is Undefined!");
        }
    }
}