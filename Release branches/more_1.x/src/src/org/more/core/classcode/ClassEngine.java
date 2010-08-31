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
package org.more.core.classcode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.more.FormatException;
import org.more.StateException;
import org.more.core.classcode.objects.DefaultAopStrategy;
import org.more.core.classcode.objects.DefaultClassNameStrategy;
import org.more.core.classcode.objects.DefaultDelegateStrategy;
import org.more.core.classcode.objects.DefaultMethodStrategy;
import org.more.core.classcode.objects.DefaultPropertyStrategy;
/**
 * �ֽ������ɹ��ߣ��ù��߿��������������ϸ��ӽӿ�ʵ�֣�ʹ��ClassEngine�����Զ�������ṩAOP��֧�֣�����ClassEngine�ṩ�����ֹ���ģʽ��<br/>
 * <br/><b>�̳з�ʽ</b>--�̳з�ʽʵ�����࣬��������ģʽ�±���Ҫ���������ͺ��ж������ɵ������Ǽ̳�ԭ����ʵ�ֵģ�
 * ���и��ӷ�����д�������С�ԭʼ���е����з���������д������super��ʽ���ø��ࡣ˽�з�����������д���롣
 * ˽�з�����������AOP���ܡ��ڼ̳�ģʽ�±��������빫����������AOP���ܡ�<br/>
 * <br/><b>����ʽ</b>--����ʽʵ�����࣬��������ģʽ�¿��������еĶ����ϸ��ӽӿ�ʵ�ֶ�����Ҫ���´�������ͬʱ���ɵ��¶���
 * ���ƻ�ԭ�ж�������ʵ�ַ�ʽ����һ����̬����ʽʵ�֡�ע���������ɷ�ʽ��ȡ������ԭʼ���еĹ��췽����
 * ȡ����֮��������һ��һ�������Ĺ��췽�����ò������;��ǻ������͡����з������ö�ʹ�����ע������Ͷ�����á�
 * ͬʱ�������ɷ�ʽ��˽�з�����������д���롣<br/>
 * �ڴ���ģʽ��ֻ�й�����������AOP���ܣ�˽�з������ܱ����ķ��������Ȩ�����ⲻ�ܲ���AOP��<br/>
 * <br/><b>AOP����</b>--ClassEngine�����AOP�����ǿ��������Ƿ����õġ��������AOP��ع������ֽ���������ʱ���˾����˵�һ�νӿڸ��Ӳ���֮��
 * ����Ҫ�����ڶ���AOP���Լ��롣���б��෽���������Լ̳еķ���������д������AOP���Ի����������ֽ������ͬʱҲ�Ȳ�ʹ��AOP���Ե�����Ч��Ҫ��Щ��
 * @version 2009-10-15
 * @author ������ (zyc@byshell.org)
 */
public class ClassEngine extends ClassLoader {
    //Engine������Ϣ��Ĭ����Ϣ
    public static final String               DefaultSuperClass        = "java.lang.Object";            //����
    public static final BuilderMode          DefaultBuilderMode       = BuilderMode.Super;             //Ĭ������ģʽ
    public static final ClassNameStrategy    DefaultClassNameStrategy = new DefaultClassNameStrategy(); //��������
    public static final DelegateStrategy     DefaultDelegateStrategy  = new DefaultDelegateStrategy(); //ί�в���
    public static final AopStrategy          DefaultAopStrategy       = new DefaultAopStrategy();      //AOP����
    public static final MethodStrategy       DefaultMethodStrategy    = new DefaultMethodStrategy();   //�������ԣ����𷽷��Ĺ���
    public static final PropertyStrategy     DefaultPropertyStrategy  = new DefaultPropertyStrategy(); //���Բ��ԡ�
    private boolean                          debug                    = false;                         //����ģʽ�������������ģʽ��ֻ�����ֽ��벻װ����
    //
    static {}
    //
    //������Ϣ
    private ClassNameStrategy                classNameStrategy        = DefaultClassNameStrategy;      //�������ԣ��������������Ĺ���
    private DelegateStrategy                 delegateStrategy         = DefaultDelegateStrategy;       //ί�в��ԣ�����ί�нӿ�ʵ�ֵĹ���
    private AopStrategy                      aopStrategy              = DefaultAopStrategy;            //AOP���ԣ�����Aop�����Ĺ���
    private PropertyStrategy                 propertyStrategy         = DefaultPropertyStrategy;       //���Բ��ԡ�
    private MethodStrategy                   methodStrategy           = DefaultMethodStrategy;         //�������ԣ����𷽷��Ĺ���
    //������Ϣ
    private String                           className                = null;                          //��������,�ɹ��췽����ʼ����
    private Class<?>                         superClass               = null;                          //��������,�ɹ��췽����ʼ����
    private BuilderMode                      builderMode              = DefaultBuilderMode;            //����ģʽ
    private Map<Class<?>, MethodDelegate>    addDelegateMap           = null;                          //ί�б�
    private Map<String, Class<?>>            addPropertyMap           = null;                          //�����Ա�
    private Map<String, PropertyDelegate<?>> addPropertyDelMap        = null;                          //��ί�����Ա�
    //����������Ϣ��
    private ArrayList<AopInvokeFilter>       aopFilters               = null;                          //aop��������
    private ArrayList<AopBeforeListener>     aopBeforeListeners       = null;                          //��ʼ���ã���Ϣ������
    private ArrayList<AopReturningListener>  aopReturningListeners    = null;                          //���÷��أ���Ϣ������
    private ArrayList<AopThrowingListener>   aopThrowingListeners     = null;                          //�׳��쳣����Ϣ������
    //���ɵ�
    private Class<?>                         newClass                 = null;                          //����
    private byte[]                           newClassBytes            = null;                          //������ֽ��롣
    private ClassConfiguration               configuration            = null;
    //==================================================================================Constructor
    public ClassEngine(boolean debug) throws ClassNotFoundException {
        this();
        this.debug = debug;
    }
    /** ����һ��ClassEngine���Ͷ���Ĭ�����ɵ�����Object�����࣬ʹ�õ��ǵ�ǰ�̵߳�ClassLoader��װ�ض�����Ϊ��װ������ */
    public ClassEngine() throws ClassNotFoundException {
        this(ClassLoader.getSystemClassLoader().loadClass(DefaultSuperClass));
    }
    /** ʹ��ָ����������һ��ClassEngine���Ͷ������ָ���������ǿ������Object��Ϊ���࣬ʹ�õ��ǵ�ǰ�̵߳�ClassLoader��װ�ض�����Ϊ��װ������ */
    public ClassEngine(String className) throws ClassNotFoundException {
        this(className, DefaultSuperClass, ClassLoader.getSystemClassLoader());
    }
    /** ʹ��ָ����������һ��ClassEngine���Ͷ������ָ���������ǿ������Object��Ϊ���࣬ʹ�õ��ǵ�ǰ�̵߳�ClassLoader��װ�ض�����Ϊ��װ������ */
    public ClassEngine(String className, String superClass, ClassLoader parentLoader) throws ClassNotFoundException {
        this(className, parentLoader.loadClass(superClass));
    }
    public ClassEngine(Class<?> superClass) {
        this(null, superClass);
    }
    /** ʹ��ָ����������һ��ClassEngine���Ͷ������ָ���������ǿ������Object��Ϊ���࣬ʹ�õ��ǵ�ǰ�̵߳�ClassLoader��װ�ض�����Ϊ��װ������ */
    public ClassEngine(String className, Class<?> superClass) {
        super(Thread.currentThread().getContextClassLoader());
        if (className == null || className.equals("") == true) {
            String packageName = this.classNameStrategy.generatePackageName();
            String simpleName = this.classNameStrategy.generateSimpleName();
            this.className = packageName + "." + simpleName;
        } else
            this.className = className;
        this.superClass = superClass;
    }
    //======================================================================================Get/Set
    /**��ȡ���������ɲ��ԡ�*/
    public ClassNameStrategy getClassNameStrategy() {
        return this.classNameStrategy;
    }
    /**�����������ɲ��ԣ��������Ϊ����ʹ��Ĭ���������ɲ��ԡ�*/
    public void setClassNameStrategy(ClassNameStrategy classNameStrategy) {
        if (classNameStrategy == null)
            this.classNameStrategy = DefaultClassNameStrategy;
        else
            this.classNameStrategy = classNameStrategy;
    }
    /**��ȡ��������ɲ��ԡ�*/
    public DelegateStrategy getDelegateStrategy() {
        return this.delegateStrategy;
    }
    /**���ô������ɲ��ԣ��������Ϊ����ʹ��Ĭ�ϴ������ɲ��ԡ�*/
    public void setDelegateStrategy(DelegateStrategy delegateStrategy) {
        if (delegateStrategy == null)
            this.delegateStrategy = DefaultDelegateStrategy;
        else
            this.delegateStrategy = delegateStrategy;
    }
    /**��ȡAop���ɲ��ԡ�*/
    public AopStrategy getAopStrategy() {
        return this.aopStrategy;
    }
    /**����aop���ɲ��ԣ��������Ϊ����ʹ��Ĭ��aop���ɲ��ԡ�*/
    public void setAopStrategy(AopStrategy aopStrategy) {
        if (aopStrategy == null)
            this.aopStrategy = DefaultAopStrategy;
        else
            this.aopStrategy = aopStrategy;
    }
    /**��ȡ�������ɲ��ԡ�*/
    public PropertyStrategy getPropertyStrategy() {
        return propertyStrategy;
    }
    /**�����������ɲ��ԣ��������Ϊ����ʹ��Ĭ���������ɲ��ԡ�*/
    public void setPropertyStrategy(PropertyStrategy propertyStrategy) {
        if (propertyStrategy == null)
            this.propertyStrategy = DefaultPropertyStrategy;
        else
            this.propertyStrategy = propertyStrategy;
    }
    /**��ȡMethod���ɲ��ԡ�*/
    public MethodStrategy getMethodStrategy() {
        return methodStrategy;
    }
    /**����Method���ɲ��ԣ��������Ϊ����ʹ��Ĭ��Method���ɲ��ԡ�*/
    public void setMethodStrategy(MethodStrategy methodStrategy) {
        if (methodStrategy == null)
            this.methodStrategy = DefaultMethodStrategy;
        else
            this.methodStrategy = methodStrategy;
    }
    /**��ȡ�������ɷ�ʽ��Ĭ�ϵ����ɷ�ʽ{@link ClassEngine#DefaultBuilderMode Super}��*/
    public BuilderMode getBuilderMode() {
        return this.builderMode;
    }
    /**�����������ɷ�ʽ���������Ϊ����ʹ��Ĭ�����ɷ�ʽ{@link ClassEngine#DefaultBuilderMode Super}��*/
    public void setBuilderMode(BuilderMode builderMode) {
        if (builderMode == null)
            this.builderMode = DefaultBuilderMode;
        else
            this.builderMode = builderMode;
    }
    /** ��ȡ���ɵ��������޶������������֡�*/
    public String getSimpleName() {
        return EngineToos.splitSimpleName(this.className);
    }
    /** ��ȡ�����������������*/
    public String getClassName() {
        return this.className;
    }
    public void setClassName(String className, String packageName) {
        String _className = (className == null || className.equals("")) ? this.classNameStrategy.generateSimpleName() : className;
        String _packageName = (packageName == null || packageName.equals("")) ? this.classNameStrategy.generatePackageName() : packageName;
        this.className = _packageName + "." + _className;
    }
    public void generateName() {
        this.setClassName(null, null);
    }
    /** ��ȡ������ĳ���(����)��*/
    public Class<?> getSuperClass() {
        return this.superClass;
    }
    /**����������Ļ������͡�*/
    public void setSuperClass(Class<?> superClass) {
        if (superClass == null)
            throw new NullPointerException("����Ϊ�ա�");
        this.superClass = superClass;
    }
    /**����������Ļ������ͣ�ÿ�θı�������Ͷ��ᵼ����ո���ʵ�ֽӿ��б�ͬʱ������ɵ��ֽ������ݡ�*/
    public void setSuperClass(String superClass, ClassLoader parentLoader) throws ClassNotFoundException {
        this.setSuperClass(parentLoader.loadClass(superClass));
    }
    /**
     * �����и���һ���ӿ�ʵ�֣��ýӿ��е����з�����ͨ��ί�ж��������������ӵĽӿ����з��������ķ�����ͻʱ��
     * appendImpl�ᶪ����ӽӿڵĳ�ͻ�����������෽�����������൱�ڻ���ķ���ʵ���˽ӿڵķ�����
     * ���������һ��ǩ���ķ���ʱClassEngineֻ�ᱣ�����һ�ε�ע�ᡣ������ķ�����������ʱ�ᱣ����ע�����Ϣ��
     * ����ظ����ͬһ���ӿ���ýӿڽ����������һ����ӡ�
     * ע�⣺�����ͼ���һ���ǽӿ�������������쳣��
     * @param appendInterface Ҫ���ӵĽӿڡ�
     * @param delegate ���ӽӿڵķ�������ί�С�
     */
    public void addDelegate(Class<?> appendInterface, MethodDelegate delegate) {
        //1.�����ж�
        if (appendInterface.isInterface() == false || delegate == null)
            throw new FormatException("����appendInterface����һ����Ч�Ľӿڣ����߲���delegateΪ�ա�");
        //2.���Ըýӿ��Ƿ��Ѿ��õ�ʵ��
        try {
            this.superClass.asSubclass(appendInterface);
            return;
        } catch (Exception e) {}
        //3.����ظ�,���ӽӿ�ʵ��
        if (this.addDelegateMap == null)
            this.addDelegateMap = new LinkedHashMap<Class<?>, MethodDelegate>();
        if (this.addDelegateMap.containsKey(appendInterface) == false)
            this.addDelegateMap.put(appendInterface, delegate);
    }
    /**���һ��AOP���������ù����������ظ���ӡ�*/
    public void addAopFilter(AopInvokeFilter filter) {
        if (filter == null)
            return;
        if (this.aopFilters == null)
            this.aopFilters = new ArrayList<AopInvokeFilter>();
        this.aopFilters.add(filter);
    };
    /**���һ��AOP���������ü����������ظ���ӡ�*/
    public void addListener(AopBeforeListener listener) {
        if (listener == null)
            return;
        if (this.aopBeforeListeners == null)
            this.aopBeforeListeners = new ArrayList<AopBeforeListener>();
        this.aopBeforeListeners.add(listener);
    };
    /**���һ��AOP���������ü����������ظ���ӡ�*/
    public void addListener(AopReturningListener listener) {
        if (listener == null)
            return;
        if (this.aopReturningListeners == null)
            this.aopReturningListeners = new ArrayList<AopReturningListener>();
        this.aopReturningListeners.add(listener);
    };
    /**���һ��AOP���������ü����������ظ���ӡ�*/
    public void addListener(AopThrowingListener listener) {
        if (listener == null)
            return;
        if (this.aopThrowingListeners == null)
            this.aopThrowingListeners = new ArrayList<AopThrowingListener>();
        this.aopThrowingListeners.add(listener);
    };
    /**���һ�����ԡ�*/
    public void addProperty(String name, Class<?> type) {
        if (name == null || name.equals("") || type == null)
            throw new NullPointerException("����name��typeΪ�ա�");
        if (this.addPropertyMap == null)
            this.addPropertyMap = new LinkedHashMap<String, Class<?>>();
        this.addPropertyMap.put(name, type);
    };
    /**���һ�����ԡ�*/
    public void addProperty(String name, PropertyDelegate<?> delegate) {
        if (name == null || name.equals("") || delegate == null)
            throw new NullPointerException("����name��delegateΪ�ա�");
        if (this.addPropertyDelMap == null)
            this.addPropertyDelMap = new LinkedHashMap<String, PropertyDelegate<?>>();
        this.addPropertyDelMap.put(name, delegate);
    };
    /**��ȡ������ӵ�����������*/
    public String[] getAppendPropertys() {
        String[] simpleProp = this.getAppendSimplePropertys();
        String[] delegateProp = this.getAppendDelegatePropertys();
        if (simpleProp == null && delegateProp == null)
            return null;
        //
        if (simpleProp == null)
            simpleProp = new String[0];
        if (delegateProp == null)
            delegateProp = new String[0];
        //
        String[] all = new String[simpleProp.length + delegateProp.length];
        for (int i = 0; i < simpleProp.length; i++)
            all[i] = simpleProp[i];
        int index = simpleProp.length;
        for (int i = 0; i < simpleProp.length; i++)
            all[index + i] = simpleProp[i];
        return all;
    }
    /**��ȡ������ӵļ�������*/
    public String[] getAppendSimplePropertys() {
        if (this.addPropertyMap == null || this.addPropertyMap.size() == 0)
            return null;
        String[] strs = new String[this.addPropertyMap.size()];
        this.addPropertyMap.keySet().toArray(strs);
        return strs;
    }
    /**��ȡ������ӵĴ���������*/
    public String[] getAppendDelegatePropertys() {
        if (this.addPropertyDelMap == null || this.addPropertyDelMap.size() == 0)
            return null;
        String[] strs = new String[this.addPropertyDelMap.size()];
        this.addPropertyDelMap.keySet().toArray(strs);
        return strs;
    }
    /**����Ҫʵ�ֵĴ���ӿڻ�ȡ�����ʵ�ֶ���*/
    public MethodDelegate getDelegate(Class<?> impl) {
        if (this.addDelegateMap == null)
            return null;
        return this.addDelegateMap.get(impl);
    }
    /**������������ȡ���������͡�*/
    public Class<?> getSimplePropertyType(String name) {
        if (this.addPropertyMap == null)
            return null;
        return this.addPropertyMap.get(name);
    }
    /**���ݴ�������������ȡ�����Դ����ࡣ*/
    public PropertyDelegate<?> getDelegateProperty(String name) {
        if (this.addPropertyDelMap == null)
            return null;
        return this.addPropertyDelMap.get(name);
    }
    /**
     * ��ȡ���ɵ���������ʵ�ֵĽӿڼ��ϣ�appendImpl�������Ը���һ���µĽӿ�ʵ�֡�
     * @return �������ɵ���������ʵ�ֵĽӿڼ��ϡ�appendImpl�������Ը���һ���µĽӿ�ʵ�֡�
     */
    public Class<?>[] getDelegates() {
        if (this.addDelegateMap == null || this.addDelegateMap.size() == 0)
            return null;
        Class<?>[] cl = new Class<?>[this.addDelegateMap.size()];
        this.addDelegateMap.keySet().toArray(cl);
        return cl;
    }
    /**��ȡ��Aop���������ϡ�*/
    public AopInvokeFilter[] getAopFilters() {
        if (this.aopFilters == null || this.aopFilters.size() == 0)
            return null;
        AopInvokeFilter[] aops = new AopInvokeFilter[this.aopFilters.size()];
        this.aopFilters.toArray(aops);
        return aops;
    }
    /**��ȡbefore�¼���������*/
    public AopBeforeListener[] getAopBeforeListeners() {
        if (this.aopBeforeListeners == null || this.aopBeforeListeners.size() == 0)
            return null;
        AopBeforeListener[] listeners = new AopBeforeListener[this.aopBeforeListeners.size()];
        this.aopBeforeListeners.toArray(listeners);
        return listeners;
    }
    /**��ȡreturning�¼���������*/
    public AopReturningListener[] getAopReturningListeners() {
        if (this.aopReturningListeners == null || this.aopReturningListeners.size() == 0)
            return null;
        AopReturningListener[] listeners = new AopReturningListener[this.aopReturningListeners.size()];
        this.aopReturningListeners.toArray(listeners);
        return listeners;
    }
    /**��ȡthrowing�¼���������*/
    public AopThrowingListener[] getAopThrowingListeners() {
        if (this.aopThrowingListeners == null || this.aopThrowingListeners.size() == 0)
            return null;
        AopThrowingListener[] listeners = new AopThrowingListener[this.aopThrowingListeners.size()];
        this.aopThrowingListeners.toArray(listeners);
        return listeners;
    }
    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    //=======================================================================================Method
    public void reset() {
        this.newClass = null;
        this.newClassBytes = null;
        this.addDelegateMap = null; //ί�б�
        this.addPropertyMap = null; //�����Ա�
        this.addPropertyDelMap = null; //��ί�����Ա�
        this.aopFilters = null; //aop��������
        this.aopBeforeListeners = null; //��ʼ���ã���Ϣ������
        this.aopReturningListeners = null; //���÷��أ���Ϣ������
        this.aopThrowingListeners = null; //�׳��쳣����Ϣ������
    };
    /**ȡ������״̬���ٴε�������ʱ��������class�������̡�*/
    public void resetBuilder() {
        this.newClass = null;
        this.newClassBytes = null;
    };
    /**��ȡ�Ѿ����ɵ������*/
    public Class<?> toClass() {
        return this.newClass;
    };
    /**��ȡ�Ѿ����ɵ��ֽ�������*/
    public byte[] toBytes() {
        return this.newClassBytes;
    };
    /**�������ɹ����������ࡣ*/
    public ClassEngine builderClass() throws ClassNotFoundException, IOException, FormatException {
        if (newClassBytes != null)
            return this;
        //��ʼ������
        this.classNameStrategy.initStrategy(this);
        this.delegateStrategy.initStrategy(this);
        this.aopStrategy.initStrategy(this);
        this.propertyStrategy.initStrategy(this);
        this.methodStrategy.initStrategy(this);
        //
        if (EngineToos.checkClassName(this.className) == false)
            throw new FormatException("���������ʱ�����������ͨ����");
        if (className == null || className.equals("") == true) {
            String packageName = this.classNameStrategy.generatePackageName();
            String simpleName = this.classNameStrategy.generateSimpleName();
            this.className = packageName + simpleName;
        }
        //
        ClassBuilder cb = this.createBuilder(this.builderMode);
        cb.initBuilder(this);
        this.configuration = cb.builderClass();
        if (this.configuration == null)
            throw new StateException("builderClassʧ�ܡ�");
        this.newClassBytes = cb.getClassBytes();
        if (this.debug == false)
            this.newClass = this.loadClass(this.className);//TODO
        //���ò���
        this.classNameStrategy.reset();
        this.delegateStrategy.reset();
        this.aopStrategy.reset();
        this.propertyStrategy.reset();
        this.methodStrategy.reset();
        //
        return this;
    }
    //======================================================================================Builder
    protected ClassBuilder createBuilder(BuilderMode builderMode) {
        return new ClassBuilder();
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.equals(this.className) == true)
            return this.defineClass(name, this.newClassBytes, 0, this.newClassBytes.length);
        else
            return super.findClass(name);
    }
    //==========================================================================================New
    public Object newInstance(Object propxyBean) throws FormatException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
        this.builderClass();
        Object obj = null;
        if (this.builderMode == BuilderMode.Super)
            obj = this.newClass.newInstance();
        else
            obj = this.newClass.getConstructor(this.superClass).newInstance(propxyBean);
        return this.configuration.configBean(obj);
    };
}