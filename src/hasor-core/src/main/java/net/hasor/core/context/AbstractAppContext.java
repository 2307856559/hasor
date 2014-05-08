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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.Environment;
import net.hasor.core.EventCallBackHook;
import net.hasor.core.EventListener;
import net.hasor.core.Hasor;
import net.hasor.core.Module;
import net.hasor.core.ModuleInfo;
import net.hasor.core.RegisterInfo;
import net.hasor.core.Settings;
import net.hasor.core.binder.AbstractBinder;
import net.hasor.core.binder.BeanInfo;
import net.hasor.core.binder.TypeRegister;
import net.hasor.core.module.ModuleProxy;
import org.more.UndefinedException;
import org.more.util.ArrayUtils;
/**
 * ������ AbstractAppContext �� {@link AppContext} �ӿڵĻ���ʵ�֡�
 * <p>����װ�˴���ϸ�ڴ��룬���Է����ͨ���������������ص�������֧�֡�<p>
 * 
 * ��ʾ��initContext ���������� AbstractAppContext ����ڷ�����
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public abstract class AbstractAppContext implements AppContext, RegisterScope {
    //
    /*---------------------------------------------------------------------------------------Bean*/
    public <T> Class<T> getBeanType(String name) {
        BeanInfo info = this.findBindingBean(name, BeanInfo.class);
        return info == null ? null : (Class<T>) info.getType();
    }
    public String getBeanName(Class<?> targetClass) {
        Hasor.assertIsNotNull(targetClass, "targetClass is null.");
        List<BeanInfo> infoArray = this.findBindingBean(BeanInfo.class);
        if (infoArray == null || infoArray.isEmpty())
            return null;
        /*����*/
        for (BeanInfo info : infoArray) {
            if (info.getType().equals(targetClass))
                return info.getName();
        }
        return null;
    }
    public String[] getBeanNames() {
        List<BeanInfo> infoArray = this.findBindingBean(BeanInfo.class);
        if (infoArray == null || infoArray.isEmpty())
            return ArrayUtils.EMPTY_STRING_ARRAY;
        /*����*/
        List<String> names = new ArrayList<String>();
        for (BeanInfo info : infoArray) {
            names.add(info.getName());
            String[] aliasNames = info.getAliasName();
            if (aliasNames != null)
                for (String aliasName : aliasNames)
                    names.add(aliasName);
        }
        return names.toArray(new String[names.size()]);
    }
    public <T> T getInstance(String name) {
        BeanInfo info = this.findBindingBean(name, BeanInfo.class);
        if (info == null)
            return null;
        Class<?> targetType = info.getType();
        return (T) this.getInstance(targetType);
    }
    /**����Bean��*/
    public abstract <T> T getInstance(Class<T> oriType);
    /**����Bean��*/
    public abstract <T> T getBean(String name);
    //
    /*------------------------------------------------------------------------------------Binding*/
    public <T> T findBindingBean(String withName, Class<T> bindingType) {
        return findBindingBean(withName, bindingType, this);
    }
    public <T> Provider<T> findBindingProvider(String withName, Class<T> bindingType) {
        return findBindingProvider(withName, bindingType, this);
    }
    public <T> List<T> findBindingBean(Class<T> bindingType) {
        return findBindingBean(bindingType, this);
    }
    public <T> List<Provider<T>> findBindingProvider(Class<T> bindingType) {
        return findBindingProvider(bindingType, this);
    }
    /**��RegisterScope��Χ�ڲ��Ұ󶨡�*/
    protected final <T> T findBindingBean(String withName, Class<T> bindingType, RegisterScope scope) {
        Provider<? extends T> targetProvider = this.findBindingProvider(withName, bindingType, scope);
        return targetProvider != null ? targetProvider.get() : null;
    }
    /**��RegisterScope��Χ�ڲ��Ұ󶨡�*/
    protected final <T> Provider<T> findBindingProvider(String withName, Class<T> bindingType, RegisterScope scope) {
        Hasor.assertIsNotNull(withName, "withName is null.");
        Hasor.assertIsNotNull(bindingType, "bindingType is null.");
        Hasor.assertIsNotNull(scope, "scope is null.");
        //
        List<RegisterInfo<T>> targetRegisterList = this.findBindingRegisterInfo(bindingType, scope);
        /*�ҵ��Ǹ�RegisterInfo*/
        for (RegisterInfo<T> info : targetRegisterList) {
            String bindName = info.getBindName();
            boolean nameTest = withName.equals(bindName);
            if (nameTest)
                return info.getProvider();
        }
        return null;
    }
    /**��RegisterScope��Χ�ڲ��Ұ󶨡�*/
    protected final <T> List<T> findBindingBean(Class<T> bindingType, RegisterScope scope) {
        List<T> targetList = new ArrayList<T>();
        List<Provider<T>> targetProviderList = this.findBindingProvider(bindingType, scope);
        /*��Provider<T>�б�ת��ΪList<T>*/
        for (Provider<T> pro : targetProviderList) {
            T target = pro.get();
            targetList.add(target);
        }
        return targetList;
    }
    /**��RegisterScope��Χ�ڲ��Ұ󶨡�*/
    protected final <T> List<Provider<T>> findBindingProvider(Class<T> bindingType, RegisterScope scope) {
        List<RegisterInfo<T>> targetInfoList = this.findBindingRegisterInfo(bindingType, scope);
        /*��RegisterInfo<T>�б�ת��ΪProvider<T>�б�*/
        List<Provider<T>> targetList = new ArrayList<Provider<T>>();
        for (RegisterInfo<T> info : targetInfoList) {
            Provider<T> target = info.getProvider();
            targetList.add(target);
        }
        return targetList;
    }
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵ�RegisterInfo���Ӹô���ȡ��RegisterInfo��Ȼ���� public�ġ�*/
    protected final <T> List<RegisterInfo<T>> findBindingRegisterInfo(Class<T> bindingType, RegisterScope scope) {
        Hasor.assertIsNotNull(bindingType, "bindingType is null.");
        Hasor.assertIsNotNull(scope, "scope is null.");
        //
        List<RegisterInfo<T>> arrayList = new ArrayList<RegisterInfo<T>>();
        findBindingRegisterInfo(bindingType, scope, arrayList);
        return arrayList;
    };
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵ�RegisterInfo���Ӹô���ȡ��RegisterInfo��Ȼ���� public�ġ�*/
    private final <T> void findBindingRegisterInfo(Class<T> bindingType, RegisterScope scope, List<RegisterInfo<T>> toList) {
        Hasor.assertIsNotNull(bindingType, "bindingType is null.");
        Hasor.assertIsNotNull(scope, "scope is null.");
        /*����Scope����*/
        Iterator<RegisterInfo<?>> iterator = scope.getRegisterIterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                RegisterInfo<?> info = iterator.next();
                if (info.getRegisterType() == bindingType)
                    toList.add((RegisterInfo<T>) info);
            }
        }
        /*����Scope����*/
        RegisterScope parentScope = scope.getParentScope();
        if (parentScope != null)
            this.findBindingRegisterInfo(bindingType, parentScope, toList);
    };
    public RegisterScope getParentScope() {
        return this.getParent();
    }
    //
    /*--------------------------------------------------------------------------------------Event*/
    public void pushListener(String eventType, EventListener eventListener) {
        this.getEnvironment().pushListener(eventType, eventListener);
    }
    public void addListener(String eventType, EventListener eventListener) {
        this.getEnvironment().addListener(eventType, eventListener);
    }
    public void removeListener(String eventType, EventListener eventListener) {
        this.getEnvironment().removeListener(eventType, eventListener);
    }
    public void fireSyncEvent(String eventType, Object... objects) {
        this.getEnvironment().fireSyncEvent(eventType, objects);
    }
    public void fireSyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.getEnvironment().fireSyncEvent(eventType, callBack, objects);
    }
    public void fireAsyncEvent(String eventType, Object... objects) {
        this.getEnvironment().fireAsyncEvent(eventType, objects);
    }
    public void fireAsyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.getEnvironment().fireAsyncEvent(eventType, callBack, objects);
    }
    //
    /*------------------------------------------------------------------------------------Context*/
    private AbstractAppContext parent;
    private Object             context;
    /**��ȡ������*/
    public AbstractAppContext getParent() {
        return this.parent;
    }
    /**��ȡ������*/
    public Object getContext() {
        return this.context;
    }
    /**����������*/
    public void setContext(Object context) {
        this.context = context;
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
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> findClass(Class<?> featureType) {
        return this.getEnvironment().findClass(featureType);
    }
    //
    /*-------------------------------------------------------------------------------------Module*/
    private List<ModuleProxy> tempModuleSet;
    /**�������߻�����ڴ������ModuleInfo�ļ��϶���*/
    private List<ModuleProxy> getModuleList() {
        if (this.tempModuleSet == null)
            this.tempModuleSet = new ArrayList<ModuleProxy>();
        return tempModuleSet;
    }
    /**���ģ�飬��������Ѿ���ʼ����ô������{@link IllegalStateException}�쳣��*/
    public synchronized ModuleInfo addModule(Module hasorModule) {
        if (this.isReady())
            throw new IllegalStateException("context is inited.");
        /*��ֹ�ظ����*/
        for (ModuleProxy info : this.getModuleList())
            if (info.getTarget() == hasorModule)
                return info;
        /*���ģ��*/
        ModuleProxy propxy = new ContextModulePropxy(hasorModule, this);
        List<ModuleProxy> propxyList = this.getModuleList();
        if (propxyList.contains(propxy) == false)
            propxyList.add(propxy);
        return propxy;
    }
    /**ɾ��ģ�飬��������Ѿ���ʼ����ô������{@link IllegalStateException}�쳣��*/
    public synchronized boolean removeModule(Module hasorModule) {
        if (this.isReady())
            throw new IllegalStateException("context is inited.");
        ModuleProxy targetInfo = null;
        for (ModuleProxy info : this.getModuleList())
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
        if (!this.isReady())
            throw new IllegalStateException("context is not ready.");
        List<ModuleInfo> moduleList = this.findBindingBean(ModuleInfo.class);
        ModuleInfo[] infoArray = new ModuleInfo[moduleList.size()];
        for (int i = 0; i < moduleList.size(); i++)
            infoArray[i] = moduleList.get(i);
        return infoArray;
    }
    /**λ�������� ModulePropxy �������ʵ��*/
    private class ContextModulePropxy extends ModuleProxy {
        public ContextModulePropxy(Module targetModule, AbstractAppContext appContext) {
            super(targetModule, appContext);
        }
        protected ModuleProxy getInfo(Class<? extends Module> targetModule, AppContext appContext) {
            List<ModuleProxy> modulePropxyList = ((AbstractAppContext) appContext).getModuleList();
            for (ModuleProxy moduleProxy : modulePropxyList)
                if (targetModule == moduleProxy.getTarget().getClass())
                    return moduleProxy;
            throw new UndefinedException(targetModule.getName() + " module is Undefined!");
        }
    }
    /**��ʼ�����̣�ע�⣺apiBinder ����ֻ���� init �׶���ʹ�á�*/
    protected void doInit(ApiBinder apiBinder) throws Throwable {
        if (this.tempModuleSet != null) {
            for (ModuleProxy propxy : tempModuleSet) {
                apiBinder.bindingType(ModuleInfo.class).toInstance(propxy);
                //apiBinder.bindingType(ModuleProxy.class).toInstance(propxy);
            }
        }
        this.doBind(apiBinder);
    };
    /**��������г�ʼ������֮����ã������� Guice ��һЩԤ�ȶ�������͡�*/
    protected void doBind(ApiBinder apiBinder) {
        final AbstractAppContext appContet = this;
        /*��Environment�����Provider*/
        apiBinder.bindingType(Environment.class).toProvider(new Provider<Environment>() {
            public Environment get() {
                return appContet.getEnvironment();
            }
        });
        /*��Settings�����Provider*/
        apiBinder.bindingType(Settings.class).toProvider(new Provider<Settings>() {
            public Settings get() {
                return appContet.getSettings();
            }
        });
        /*��AppContext�����Provider*/
        apiBinder.bindingType(AppContext.class).toProvider(new Provider<AppContext>() {
            public AppContext get() {
                return appContet;
            }
        });
    }
    //
    /*--------------------------------------------------------------------------------------Utils*/
    /**ע��һ������*/
    protected abstract <T> TypeRegister<T> registerType(Class<T> type);
    /**Ϊģ�鴴��ApiBinder*/
    protected ApiBinder newApiBinder(final ModuleProxy forModule) {
        return new AbstractBinder(this.getEnvironment()) {
            public ModuleSettings configModule() {
                return forModule;
            }
            protected <T> TypeRegister<T> registerType(Class<T> type) {
                return AbstractAppContext.this.registerType(type);
            }
        };
    }
    /**��ӡģ��״̬*/
    protected static void printModState(AbstractAppContext appContext) {
        ModuleInfo[] modArray = appContext.getModules();
        StringBuilder sb = new StringBuilder("");
        int size = String.valueOf(modArray.length).length();
        for (int i = 0; i < modArray.length; i++) {
            ModuleInfo info = modArray[i];
            sb.append(String.format("%0" + size + "d", i));
            sb.append('.');
            sb.append("-->[");
            //Running:������(start)��Failed:׼��ʧ�ܡ�Stopped:ֹͣ(stop)
            sb.append(!info.isReady() ? "Failed " : info.isStart() ? "Running" : "Stopped");
            sb.append("] ");
            sb.append(info.getDisplayName());
            sb.append(" (");
            sb.append(info.getDescription());
            sb.append(")\n");
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        Hasor.logInfo("Modules State List:\n%s", sb);
    }
    //    /**ʹ�÷�Ӧ�Ѷ�ģ�����ѭ����������*/
    //    private List<ModuleProxy> doReactor() {
    //        List<ModuleInfo> readOnlyModules = new ArrayList<ModuleInfo>();
    //        for (ModuleProxy amp : this.getModuleList())
    //            readOnlyModules.add(amp);
    //        ModuleReactor reactor = new ModuleReactor(readOnlyModules);//������Ӧ��
    //        List<ModuleInfo> result = reactor.process();
    //        List<ModuleProxy> propxyList = new ArrayList<ModuleProxy>();
    //        for (ModuleInfo info : result)
    //            propxyList.add((ModuleProxy) info);
    //        return propxyList;
    //    }
    //    
    //    
    //    
    //    
    //    
    //    
    //    
    //    
    //    //-----------------------------------------------------------------------------------------Life
    //    private boolean isStart;
    //    /**�ж������Ƿ�������״̬*/
    //    public boolean isStart() {
    //        return this.isStart;
    //    }
    //    /**��ʾAppContext�Ƿ�׼���á�*/
    //    public boolean isReady() {
    //        return this.getGuice() != null;
    //    }
    //    /**��ʼ����������ע������ֻ�ܱ���ʼ��һ�Ρ��÷����ڴ��� Guice �����л����� doInitialize �����ĵ��á�*/
    //    protected void initContext() {
    //        if (this.injector != null)
    //            return;
    //        /*1.��������guice*/
    //        Hasor.logInfo("createInjector...");
    //        this.injector = this.createInjector(new com.google.inject.Module[] { new RootInitializeModule(this) });
    //        Hasor.assertIsNotNull(this.injector, "can not be create Injector.");
    //        /*2.ʹ�÷�Ӧ�Ѷ�ģ�����ѭ����������*/
    //        this.doReactor();
    //        /*3.���init*/
    //        Hasor.logInfo("the init is completed!");
    //    }
    //    /**������������ģ�鷢�������źţ�����������״̬��ΪStart�����÷����᳢��init����ģ�飩*/
    //    public synchronized void start() {
    //        if (this.isStart() == true)
    //            return;
    //        /*1.��ʼ��*/
    //        this.initContext();
    //        /*2.����*/
    //        this.doStart();
    //    }
    //    //--------------------------------------------------------------------------------------Process
    //    /**ִ�� Initialize ���̡�*/
    //    protected void doInitialize(final Binder guiceBinder) {
    //        Hasor.logInfo("send init sign...");
    //        List<ModuleProxy> modulePropxyList = this.getModuleList();
    //        /*����ģ��init��������*/
    //        for (ModuleProxy forModule : modulePropxyList) {
    //            AbstractBinderContext apiBinder = this.newApiBinder(forModule, guiceBinder);
    //            forModule.init(apiBinder);//������������ 
    //            apiBinder.configure(guiceBinder);
    //        }
    //        this.doBind(guiceBinder);
    //        /*�����¼�*/
    //        AbstractBinderContext apiBinder = new AbstractBinderContext(this.getEnvironment()) {
    //            public ModuleSettings configModule() {
    //                return null;
    //            }
    //            public Binder getGuiceBinder() {
    //                return guiceBinder;
    //            }
    //        };
    //        this.getEventManager().doSync(ContextEvent_Initialized, apiBinder);
    //        Hasor.logInfo("init modules finish.");
    //    }
    //    /**������������ģ�鷢�������źţ�����������״̬��ΪStart�����÷����᳢��init����ģ�飩*/
    //    protected void doStart() {
    //        Hasor.logInfo("send start sign.");
    //        /*1.ִ��Aware֪ͨ*/
    //        List<AppContextAware> awareList = this.findBindingBean(AppContextAware.class);
    //        if (awareList != null) {
    //            for (AppContextAware weak : awareList)
    //                weak.setAppContext(this);
    //        }
    //        /*2.��һ����ģ��*/
    //        List<ModuleProxy> modulePropxyList = this.getModuleList();
    //        for (ModuleProxy mod : modulePropxyList)
    //            mod.start(this);
    //        this.isStart = true;
    //        /*3.���������¼�*/
    //        this.getEnvironment().getEventManager().doSync(ContextEvent_Started, this);
    //        /*4.��ӡģ��״̬*/
    //        printModState(this);
    //        Hasor.logInfo("hasor started!");
    //    }
}