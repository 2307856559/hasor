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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.AppContextAware;
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
import net.hasor.core.binder.register.FreeTypeRegister;
import net.hasor.core.builder.BeanBuilder;
import net.hasor.core.module.ModuleProxy;
import net.hasor.core.module.ModuleReactor;
import org.more.UndefinedException;
import org.more.util.ArrayUtils;
import org.more.util.MergeUtils;
import org.more.util.StringUtils;
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
    public String[] getBeanNames(Class<?> targetClass) {
        Hasor.assertIsNotNull(targetClass, "targetClass is null.");
        List<BeanInfo> infoArray = this.findBindingBean(BeanInfo.class);
        if (infoArray == null)
            infoArray = Collections.emptyList();
        /*����*/
        ArrayList<String> nameList = new ArrayList<String>();
        for (int i = 0; i < infoArray.size(); i++) {
            BeanInfo info = infoArray.get(i);
            String[] names = info.getNames();
            for (String nameItem : names)
                nameList.add(nameItem);
        }
        return nameList.toArray(new String[nameList.size()]);
    }
    public String[] getBeanNames() {
        List<BeanInfo> infoArray = this.findBindingBean(BeanInfo.class);
        if (infoArray == null || infoArray.isEmpty())
            return ArrayUtils.EMPTY_STRING_ARRAY;
        /*����*/
        List<String> names = new ArrayList<String>();
        for (BeanInfo info : infoArray) {
            String[] aliasNames = info.getNames();
            for (String aliasName : aliasNames)
                names.add(aliasName);
        }
        return names.toArray(new String[names.size()]);
    }
    public <T> T getInstance(String name) {
        BeanInfo info = this.findBindingBean(name, BeanInfo.class);
        if (info == null)
            return null;
        return (T) this.getInstance(info.getType());
    }
    /**����Bean��*/
    public <T> T getBean(String name) {
        /*1.��Hasor������Bean���嶼�ǰ󶨵�BeanInfo�����ϣ���˲�������Ϊname��BeanInfo����*/
        RegisterInfo<BeanInfo> regInfo = this.findRegisterInfo(name, BeanInfo.class);
        if (regInfo == null)
            return null;
        /*2.�õ�BeanInfo���󣬲�ȡ��Bean������ʵ���ͺ�referID*/
        BeanInfo beanInfo = regInfo.getProvider().get();
        RegisterInfo<?> targetInfo = this.findRegisterInfo(beanInfo.getReferID(), beanInfo.getType());
        return (T) this.getBeanBuilder().getInstance(targetInfo);
    };
    /**����Bean��*/
    public <T> T getInstance(Class<T> oriType) {
        /* 1.����ͬһ��Type���ܻ��ж��ע�ᣬÿ��ע����ܻ�ӳ���˲�ͬ��ʵ�֣�����:
         *     String -> "HelloWord"   > name = "Hi"
         *     String -> "Say goodBy." > name = "By"
         *     String -> "Body .."     > name = null  (������)
         * ��˲����Ǹ�û�����ֵ�Type����������������Type�������������򷵻�null��*/
        RegisterInfo<T> info = this.findRegisterInfo(null, oriType);
        if (info == null)
            info = new FreeTypeRegister<T>(oriType);
        return this.getBeanBuilder().getInstance(info);
    };
    /**��ȡ���ڴ���Bean�����BeanBuilder�ӿ�*/
    protected BeanBuilder getBeanBuilder() {
        return this.getRegisterManager().getBeanBuilder();
    };
    //
    /*------------------------------------------------------------------------------------Binding*/
    public <T> T findBindingBean(String withName, Class<T> bindingType) {
        Hasor.assertIsNotNull(withName, "withName is null.");
        Hasor.assertIsNotNull(bindingType, "bindingType is null.");
        //
        List<RegisterInfo<T>> targetRegisterList = this.findRegisterInfo(bindingType);
        /*�ҵ��Ǹ�RegisterInfo*/
        for (RegisterInfo<T> info : targetRegisterList) {
            String bindName = info.getName();
            boolean nameTest = withName.equals(bindName);
            if (nameTest)
                return info.getProvider().get();
        }
        return null;
    }
    public <T> Provider<T> findBindingProvider(String withName, Class<T> bindingType) {
        Hasor.assertIsNotNull(withName, "withName is null.");
        Hasor.assertIsNotNull(bindingType, "bindingType is null.");
        //F
        List<RegisterInfo<T>> targetRegisterList = this.findRegisterInfo(bindingType);
        /*�ҵ��Ǹ�RegisterInfo*/
        for (RegisterInfo<T> info : targetRegisterList) {
            String bindName = info.getName();
            boolean nameTest = withName.equals(bindName);
            if (nameTest)
                return info.getProvider();
        }
        return null;
    }
    public <T> List<T> findBindingBean(Class<T> bindingType) {
        List<RegisterInfo<T>> targetInfoList = this.findRegisterInfo(bindingType);
        /*��RegisterInfo<T>�б�ת��Ϊ<T>�б�*/
        List<T> targetList = new ArrayList<T>();
        for (RegisterInfo<T> info : targetInfoList) {
            Provider<T> target = info.getProvider();
            targetList.add(target.get());
        }
        return targetList;
    }
    public <T> List<Provider<T>> findBindingProvider(Class<T> bindingType) {
        List<RegisterInfo<T>> targetInfoList = this.findRegisterInfo(bindingType);
        /*��RegisterInfo<T>�б�ת��ΪProvider<T>�б�*/
        List<Provider<T>> targetList = new ArrayList<Provider<T>>();
        for (RegisterInfo<T> info : targetInfoList) {
            Provider<T> target = info.getProvider();
            targetList.add(target);
        }
        return targetList;
    }
    public RegisterScope getParentScope() {
        return this.getParent();
    }
    public final Iterator<RegisterInfo<?>> getRegisterIterator() {
        Iterator<RegisterInfo<?>> registerIterator = this.localRegisterIterator();
        RegisterScope scope = this.getParentScope();
        if (scope != null) {
            Iterator<RegisterInfo<?>> parentIterator = scope.getRegisterIterator();
            registerIterator = MergeUtils.mergeIterator(registerIterator, parentIterator);
        }
        return registerIterator;
    }
    /**����RegisterInfo*/
    public <T> List<RegisterInfo<T>> findRegisterInfo(Class<T> bindType) {
        Iterator<RegisterInfo<T>> infoIterator = this.localRegisterIterator(bindType);
        if (infoIterator == null || infoIterator.hasNext() == false)
            return Collections.emptyList();
        //
        List<RegisterInfo<T>> infoList = new ArrayList<RegisterInfo<T>>();
        while (infoIterator.hasNext())
            infoList.add(infoIterator.next());
        return infoList;
    }
    /**����RegisterInfo*/
    public <T> RegisterInfo<T> findRegisterInfo(String withName, Class<T> bindingType) {
        List<RegisterInfo<T>> infoList = this.findRegisterInfo(bindingType);
        for (RegisterInfo<T> info : infoList) {
            if (StringUtils.equals(withName, info.getName()))
                return info;
        }
        return null;
    }
    /**ע��һ������*/
    protected <T> TypeRegister<T> registerType(Class<T> type) {
        return this.getRegisterManager().registerType(type);
    }
    /**��ע��������б�*/
    protected Iterator<RegisterInfo<?>> localRegisterIterator() {
        return this.getBeanBuilder().getRegisterIterator();
    }
    /**��ע��������б�*/
    protected <T> Iterator<RegisterInfo<T>> localRegisterIterator(Class<T> type) {
        return this.getBeanBuilder().getRegisterIterator(type);
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
    /**��ȡRegisterContext����*/
    protected abstract RegisterManager getRegisterManager();
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
    public final ModuleInfo[] getModules() {
        List<ModuleProxy> moduleList = this.getModuleList();
        ModuleInfo[] infoArray = new ModuleInfo[moduleList.size()];
        for (int i = 0; i < moduleList.size(); i++)
            infoArray[i] = moduleList.get(i);
        return infoArray;
    }
    /**װ��ģ�鶨��*/
    protected void initModule() {
        for (ModuleProxy propxy : this.getModuleList()) {
            ApiBinder apiBinder = this.newApiBinder(propxy);
            apiBinder.bindingType(ModuleInfo.class).nameWith(propxy.getDisplayName()).toInstance(propxy);/*�����ǰ�*/
            propxy.init(apiBinder);
        }
    }
    /**λ�������� ModulePropxy �������ʵ��*/
    protected class ContextModulePropxy extends ModuleProxy {
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
    //
    /*------------------------------------------------------------------------------------Process*/
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
    //        this.getEventManager().doSync(ContextEvent_Initialized, apiBinder);
    //        Hasor.logInfo("init modules finish.");
    //    }
    //
    //
    private boolean isStart = false;
    private boolean isReady = false;
    public boolean isStart() {
        return this.isStart;
    }
    public boolean isReady() {
        return this.isReady;
    }
    public synchronized final void start() {
        if (this.isStart() == true)
            return;
        /*1.Init*/
        if (!this.isReady()) {
            Hasor.logInfo("send init sign.");
            this.doInitialize();
            this.initModule();
            /*2.Bind*/
            AbstractBinder apiBinder = new AbstractBinder(this.getEnvironment()) {
                public ModuleSettings configModule() {
                    return null;
                }
                protected <T> TypeRegister<T> registerType(Class<T> type) {
                    return AbstractAppContext.this.registerType(type);
                }
            };
            this.doBind(apiBinder);
            this.doInitializeCompleted();
            Hasor.logInfo("the init is completed!");
            this.isReady = true;
        }
        //
        //
        //
        /*3.Start*/
        Hasor.logInfo("send start sign.");
        this.doStart();
        /*2.ִ��Aware֪ͨ*/
        List<AppContextAware> awareList = this.findBindingBean(AppContextAware.class);
        if (awareList.isEmpty() == false) {
            for (AppContextAware weak : awareList)
                weak.setAppContext(this);
        }
        /*3.��һ����ģ��*/
        List<ModuleProxy> modulePropxyList = this.doReactor();
        for (ModuleProxy mod : modulePropxyList)
            mod.start(this);
        /*4.���������¼�*/
        this.fireSyncEvent(ContextEvent_Started, this);
        this.doStartCompleted();/*������չ*/
        this.isStart = true;
        /*5.��ӡģ��״̬*/
        printModState(this);
        Hasor.logInfo("hasor started!");
    }
    /**��ʼ�����ʼ������.*/
    protected void doInitialize() {
        RegisterManager regContext = this.getRegisterManager();
        if (regContext instanceof ContextInitializeListener)
            ((ContextInitializeListener) regContext).doInitialize(this);
    }
    /**��ʼ���������.*/
    protected void doInitializeCompleted() {
        RegisterManager regContext = this.getRegisterManager();
        if (regContext instanceof ContextInitializeListener)
            ((ContextInitializeListener) regContext).doInitializeCompleted(this);
    }
    /**��ʼ����������������.*/
    protected void doStart() {
        RegisterManager regContext = this.getRegisterManager();
        if (regContext instanceof ContextStartListener)
            ((ContextStartListener) regContext).doStart(this);
    }
    /**�����������*/
    protected void doStartCompleted() {
        RegisterManager regContext = this.getRegisterManager();
        if (regContext instanceof ContextStartListener)
            ((ContextStartListener) regContext).doStartCompleted(this);
    }
    //
    //
    /*--------------------------------------------------------------------------------------Utils*/
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
    /**��������г�ʼ������֮����ã������� Context ��һЩԤ�ȶ�������͡�*/
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
    /**ʹ�÷�Ӧ�Ѷ�ģ�����ѭ����������*/
    private List<ModuleProxy> doReactor() {
        List<ModuleProxy> readOnlyModules = new ArrayList<ModuleProxy>();
        for (ModuleProxy amp : this.getModuleList())
            readOnlyModules.add(amp);
        ModuleReactor reactor = new ModuleReactor(readOnlyModules);//������Ӧ��
        List<ModuleProxy> result = reactor.process();
        List<ModuleProxy> propxyList = new ArrayList<ModuleProxy>();
        for (ModuleProxy info : result)
            propxyList.add(info);
        return propxyList;
    }
}