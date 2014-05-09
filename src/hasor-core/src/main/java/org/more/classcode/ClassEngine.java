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
package org.more.classcode;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.more.asm.ClassVisitor;
import org.more.asm.ClassWriter;
import org.more.classcode.objects.DefaultAopStrategy;
import org.more.classcode.objects.DefaultClassNameStrategy;
import org.more.classcode.objects.DefaultDelegateStrategy;
import org.more.classcode.objects.DefaultMethodStrategy;
import org.more.classcode.objects.DefaultPropertyStrategy;
/**
* classcode v2.0���档������������debugģʽ����debugģʽ��{@link ClassEngine#builderClass()}������װ�����ɵ����� ʱ�����׳���
* ���û��ָ����װ�������ʹ��Thread.currentThread().getContextClassLoader()�������ص���װ������װ���ࡣ
* @version 2010-9-5
* @author ������ (zyc@hasor.net)
*/
public class ClassEngine {
    /**Ĭ�ϳ���java.lang.Object��*/
    public static final Class<?>             DefaultSuperClass        = java.lang.Object.class;
    /**Ĭ������ģʽ{@link BuilderMode#Super}��*/
    public static final BuilderMode          DefaultBuilderMode       = BuilderMode.Super;
    /**Ĭ����������{@link DefaultClassNameStrategy}��*/
    public static final ClassNameStrategy    DefaultClassNameStrategy = new DefaultClassNameStrategy();
    /**Ĭ��ί�в���{@link DefaultDelegateStrategy}*/
    public static final DelegateStrategy     DefaultDelegateStrategy  = new DefaultDelegateStrategy();
    /**Ĭ�ϵ�Aop����{@link DefaultAopStrategy}��*/
    public static final AopStrategy          DefaultAopStrategy       = new DefaultAopStrategy();
    /**Ĭ�Ϸ�������{@link DefaultMethodStrategy}�����𷽷��Ĺ���*/
    public static final MethodStrategy       DefaultMethodStrategy    = new DefaultMethodStrategy();
    /**Ĭ�����Բ���{@link DefaultPropertyStrategy}��*/
    public static final PropertyStrategy     DefaultPropertyStrategy  = new DefaultPropertyStrategy();
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
    private CreatedConfiguration             configuration            = null;
    private RootClassLoader                  rootClassLoader          = null;                          //��������������װ���������װ������
    //==================================================================================Constructor
    /** ����һ��ClassEngine���Ͷ���Ĭ�����ɵ�����Object�����࣬ */
    public ClassEngine() throws ClassNotFoundException {
        this(null, DefaultSuperClass, null);
    };
    /** ����һ��ClassEngine���Ͷ���Ĭ�����ɵ�����Object�����࣬ */
    public ClassEngine(String className) throws ClassNotFoundException {
        this(className, DefaultSuperClass, null);
    };
    /**
     * ����һ��ClassEngine���Ͷ��󣬸ù������ָ������������������Ļ�������װ������<br/>
     * ��װ�ص��趨����ѭ���¹������parentLoader����Ϊ�����ʹ�õ�ǰ�̵߳���װ������Ϊ������װ�����ĸ���װ������
     * ���ָ������{@link RootClassLoader}����װ������������ֱ��ʹ�ø���װ������Ϊ�������װ���������ָ������һ��
     * {@link ClassLoader}���Ͳ������������װ������ʹ�������װ������Ϊ�丸��װ������     * @param className ������������������Ϊ����ʹ��Ĭ�����ɲ������ɡ�
     * @param superClass ���������ַ���������������ʹ��parentLoader��������װ����װ�ء�
     * @param parentLoader ClassEngine��װ������
     */
    public ClassEngine(String className, String superClass, ClassLoader parentLoader) throws ClassNotFoundException {
        this(className, parentLoader.loadClass(superClass), parentLoader);
    };
    /** ����һ��ClassEngine���Ͷ��󣬲���ָ����������ĸ������͡�*/
    public ClassEngine(Class<?> superClass) {
        this(null, superClass, null);
    };
    /** ����һ��ClassEngine���Ͷ��󣬲���ָ����������ĸ������͡�*/
    public ClassEngine(Class<?> superClass, ClassLoader parentLoader) {
        this(null, superClass, parentLoader);
    };
    /**
     * ����һ��ClassEngine���Ͷ��󣬸ù������ָ������������������Ļ�������װ������<br/>
     * ��װ�ص��趨����ѭ���¹������parentLoader����Ϊ�����ʹ�õ�ǰ�̵߳���װ������Ϊ������װ�����ĸ���װ������
     * ���ָ������{@link RootClassLoader}����װ������������ֱ��ʹ�ø���װ������Ϊ�������װ���������ָ������һ��
     * {@link ClassLoader}���Ͳ������������װ������ʹ�������װ������Ϊ�丸��װ������
     * @param className ������������������Ϊ����ʹ��Ĭ�����ɲ������ɡ�
     * @param superClass �������͡�
     * @param parentLoader ����װ�������ĸ���װ������
     */
    public ClassEngine(String className, Class<?> superClass, ClassLoader parentLoader) {
        //1.����className
        if (className == null || className.equals("") == true)
            this.className = this.classNameStrategy.generateName(superClass);
        else
            this.className = className;
        //2.����superClass
        if (superClass != null)
            this.superClass = superClass;
        else
            this.superClass = DefaultSuperClass;
        //3.����parentLoader
        if (parentLoader == null)
            this.rootClassLoader = new RootClassLoader(Thread.currentThread().getContextClassLoader());
        else if (parentLoader instanceof RootClassLoader)
            this.rootClassLoader = (RootClassLoader) parentLoader;
        else
            this.rootClassLoader = new RootClassLoader(parentLoader);
    };
    //======================================================================================private
    /**�������������(�ֽ�����ʽ)��*/
    final String getAsmClassName() {
        return EngineToos.replaceClassName(this.getClassName());
    }
    /**���ظ��������(�ֽ�����ʽ)��*/
    final String getAsmSuperClassName() {
        return EngineToos.replaceClassName(this.getSuperClass().getName());
    };
    //======================================================================================Get/Set
    /**��ȡ���������ɲ��ԡ�*/
    public ClassNameStrategy getClassNameStrategy() {
        return this.classNameStrategy;
    };
    /**�����������ɲ��ԣ��������Ϊ����ʹ��Ĭ���������ɲ��ԡ�*/
    public void setClassNameStrategy(ClassNameStrategy classNameStrategy) {
        if (classNameStrategy == null)
            this.classNameStrategy = DefaultClassNameStrategy;
        else
            this.classNameStrategy = classNameStrategy;
    };
    /**��ȡ��������ɲ��ԡ�*/
    public DelegateStrategy getDelegateStrategy() {
        return this.delegateStrategy;
    };
    /**���ô������ɲ��ԣ��������Ϊ����ʹ��Ĭ�ϴ������ɲ��ԡ�*/
    public void setDelegateStrategy(DelegateStrategy delegateStrategy) {
        if (delegateStrategy == null)
            this.delegateStrategy = DefaultDelegateStrategy;
        else
            this.delegateStrategy = delegateStrategy;
    };
    /**��ȡAop���ɲ��ԡ�*/
    public AopStrategy getAopStrategy() {
        return this.aopStrategy;
    };
    /**����aop���ɲ��ԣ��������Ϊ����ʹ��Ĭ��aop���ɲ��ԡ�*/
    public void setAopStrategy(AopStrategy aopStrategy) {
        if (aopStrategy == null)
            this.aopStrategy = DefaultAopStrategy;
        else
            this.aopStrategy = aopStrategy;
    };
    /**��ȡ�������ɲ��ԡ�*/
    public PropertyStrategy getPropertyStrategy() {
        return propertyStrategy;
    };
    /**�����������ɲ��ԣ��������Ϊ����ʹ��Ĭ���������ɲ��ԡ�*/
    public void setPropertyStrategy(PropertyStrategy propertyStrategy) {
        if (propertyStrategy == null)
            this.propertyStrategy = DefaultPropertyStrategy;
        else
            this.propertyStrategy = propertyStrategy;
    };
    /**��ȡMethod���ɲ��ԡ�*/
    public MethodStrategy getMethodStrategy() {
        return methodStrategy;
    };
    /**����Method���ɲ��ԣ��������Ϊ����ʹ��Ĭ��Method���ɲ��ԡ�*/
    public void setMethodStrategy(MethodStrategy methodStrategy) {
        if (methodStrategy == null)
            this.methodStrategy = DefaultMethodStrategy;
        else
            this.methodStrategy = methodStrategy;
    };
    /**��ȡ�������ɷ�ʽ��Ĭ�ϵ����ɷ�ʽ{@link ClassEngine#DefaultBuilderMode Super}��*/
    public BuilderMode getBuilderMode() {
        return this.builderMode;
    };
    /**�����������ɷ�ʽ���������Ϊ����ʹ��Ĭ�����ɷ�ʽ{@link ClassEngine#DefaultBuilderMode Super}��*/
    public void setBuilderMode(BuilderMode builderMode) {
        if (builderMode == null)
            this.builderMode = DefaultBuilderMode;
        else
            this.builderMode = builderMode;
    };
    /** ��ȡ���ɵ��������޶������������֡�*/
    public String getSimpleName() {
        return EngineToos.splitSimpleName(this.className);
    };
    /** ��ȡ�����������������*/
    public String getClassName() {
        return this.className;
    };
    /**��������������������������������Ϊnull�����������������ɲ��Է������ɵİ���������Ҳͬ��*/
    public void setClassName(String className) {
        if (className == null || className.equals("") == true)
            this.className = this.classNameStrategy.generateName(this.superClass);
        else
            this.className = className;
    };
    /**�÷����ǵ����������ɲ�������һ�������Լ���������ԭ�����ͨ�����ÿ������Ϳհ�����ʵ�֡�����ͨ������setClassName������������null����ɡ�*/
    public void generateName() {
        this.setClassName(null);
    };
    /** ��ȡ������ĳ���(����)��*/
    public Class<?> getSuperClass() {
        return this.superClass;
    };
    /**����������Ļ������͡�*/
    public void setSuperClass(Class<?> superClass) {
        if (superClass == null)
            throw new NullPointerException("����Ϊ�ա�");
        this.superClass = superClass;
    };
    /**����������Ļ������ͣ�ÿ�θı�������Ͷ��ᵼ����ո���ʵ�ֽӿ��б�ͬʱ������ɵ��ֽ������ݡ�*/
    public void setSuperClass(String superClass, ClassLoader parentLoader) throws ClassNotFoundException {
        this.setSuperClass(parentLoader.loadClass(superClass));
    };
    /**��ȡ��ǰ��������ʹ�õĸ���װ������*/
    public RootClassLoader getRootClassLoader() {
        return this.rootClassLoader;
    };
    /**
     * �����������һ��ί�нӿ�ʵ�֣���ί�нӿ��е����з�����ͨ��ί�ж�����������ί�нӿ����з��������ķ�����ͻʱ��
     * �����ɵ�ί�з�����ᶪ��ί�нӿ��еķ���ȥ�������෽��������java��Ҳ���൱��ʵ�֣����Ǹ���Ҫ���Ǳ����˻��ࡣ
     * ����ظ����ͬһ���ӿ���ýӿڽ����������һ����ӡ�ע�⣺�����ͼ���һ���ǽӿ�������������쳣��
     * @param appendInterface Ҫ���ӵĽӿڡ�
     * @param delegate ί�нӿڵķ�������ί�С�
     */
    public void addDelegate(Class<?> appendInterface, MethodDelegate delegate) {
        //1.�����ж�
        if (appendInterface.isInterface() == false || delegate == null)
            throw new FormatException("ί�в���һ����Ч�Ľӿ����ͣ�����MethodDelegate���Ͳ���Ϊ�ա�");
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
    };
    /**���һ��AOP���������ù����������ظ���ӡ�*/
    public void addAopFilter(AopInvokeFilter filter) {
        if (filter == null)
            return;
        if (this.aopFilters == null)
            this.aopFilters = new ArrayList<AopInvokeFilter>();
        this.aopFilters.add(filter);
    };
    /**���һ��{@link AopBeforeListener}���������ü����������ظ���ӡ�*/
    public void addListener(AopBeforeListener listener) {
        if (listener == null)
            return;
        if (this.aopBeforeListeners == null)
            this.aopBeforeListeners = new ArrayList<AopBeforeListener>();
        this.aopBeforeListeners.add(listener);
    };
    /**���һ��{@link AopReturningListener}���������ü����������ظ���ӡ�*/
    public void addListener(AopReturningListener listener) {
        if (listener == null)
            return;
        if (this.aopReturningListeners == null)
            this.aopReturningListeners = new ArrayList<AopReturningListener>();
        this.aopReturningListeners.add(listener);
    };
    /**���һ��{@link AopThrowingListener}���������ü����������ظ���ӡ�*/
    public void addListener(AopThrowingListener listener) {
        if (listener == null)
            return;
        if (this.aopThrowingListeners == null)
            this.aopThrowingListeners = new ArrayList<AopThrowingListener>();
        this.aopThrowingListeners.add(listener);
    };
    /**�������ɵ��������һ�������ֶΣ������������Բ���������get/set������*/
    public void addProperty(String name, Class<?> type) {
        if (name == null || name.equals("") || type == null)
            throw new NullPointerException("����name��typeΪ�ա�");
        if (this.addPropertyMap == null)
            this.addPropertyMap = new LinkedHashMap<String, Class<?>>();
        this.addPropertyMap.put(name, type);
    };
    /**�������ɵ��������һ��ί�����ԣ������������Բ���������get/set������*/
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
    };
    /**��ȡ������ӵļ�������*/
    public String[] getAppendSimplePropertys() {
        if (this.addPropertyMap == null || this.addPropertyMap.size() == 0)
            return null;
        String[] strs = new String[this.addPropertyMap.size()];
        this.addPropertyMap.keySet().toArray(strs);
        return strs;
    };
    /**��ȡ������ӵĴ���������*/
    public String[] getAppendDelegatePropertys() {
        if (this.addPropertyDelMap == null || this.addPropertyDelMap.size() == 0)
            return null;
        String[] strs = new String[this.addPropertyDelMap.size()];
        this.addPropertyDelMap.keySet().toArray(strs);
        return strs;
    };
    /**����Ҫʵ�ֵĴ���ӿڻ�ȡ�����ʵ�ֶ���*/
    public MethodDelegate getDelegate(Class<?> impl) {
        if (this.addDelegateMap == null)
            return null;
        return this.addDelegateMap.get(impl);
    };
    /**������������ȡ���������͡�*/
    public Class<?> getSimplePropertyType(String name) {
        if (this.addPropertyMap == null)
            return null;
        return this.addPropertyMap.get(name);
    };
    /**���ݴ�������������ȡ�����Դ����ࡣ*/
    public PropertyDelegate<?> getDelegateProperty(String name) {
        if (this.addPropertyDelMap == null)
            return null;
        return this.addPropertyDelMap.get(name);
    };
    /** ��ȡ���ɵ���������ӵ�����ί�нӿ����顣*/
    public Class<?>[] getDelegates() {
        if (this.addDelegateMap == null || this.addDelegateMap.size() == 0)
            return null;
        Class<?>[] cl = new Class<?>[this.addDelegateMap.size()];
        this.addDelegateMap.keySet().toArray(cl);
        return cl;
    };
    /**��ȡ��Aop���������ϡ�*/
    public AopInvokeFilter[] getAopFilters() {
        if (this.aopFilters == null || this.aopFilters.size() == 0)
            return null;
        AopInvokeFilter[] aops = new AopInvokeFilter[this.aopFilters.size()];
        this.aopFilters.toArray(aops);
        return aops;
    };
    /**��ȡbefore�����������*/
    public AopBeforeListener[] getAopBeforeListeners() {
        if (this.aopBeforeListeners == null || this.aopBeforeListeners.size() == 0)
            return null;
        AopBeforeListener[] listeners = new AopBeforeListener[this.aopBeforeListeners.size()];
        this.aopBeforeListeners.toArray(listeners);
        return listeners;
    };
    /**��ȡreturning�����������*/
    public AopReturningListener[] getAopReturningListeners() {
        if (this.aopReturningListeners == null || this.aopReturningListeners.size() == 0)
            return null;
        AopReturningListener[] listeners = new AopReturningListener[this.aopReturningListeners.size()];
        this.aopReturningListeners.toArray(listeners);
        return listeners;
    };
    /**��ȡthrowing�����������*/
    public AopThrowingListener[] getAopThrowingListeners() {
        if (this.aopThrowingListeners == null || this.aopThrowingListeners.size() == 0)
            return null;
        AopThrowingListener[] listeners = new AopThrowingListener[this.aopThrowingListeners.size()];
        this.aopThrowingListeners.toArray(listeners);
        return listeners;
    };
    //=======================================================================================Method
    /**
     * ��ȫ���ã������÷���������������ɵ���ͬʱҲ�������ӵ�ί�нӿ��Լ������ԡ�<br/>
     * �����������װ�����÷���Ҳ�����������ϵ�ע�ᡣ
     */
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
        this.rootClassLoader.unRegeditEngine(this);//
    };
    /**�������aop���á�*/
    public void resetAop() {
        this.aopFilters = null; //aop��������
        this.aopBeforeListeners = null; //��ʼ���ã���Ϣ������
        this.aopReturningListeners = null; //���÷��أ���Ϣ������
        this.aopThrowingListeners = null; //�׳��쳣����Ϣ������ 
    };
    /**
     * ��������״̬���ٴε�������ʱ��������class�������̣��÷�������Ӱ�쵽�Ѿ�ע���aop�������Ե���Ϣ��
     * ���Ǹ÷�������������װ���ϵ�ע�����������ڴ���װ�����ࡣ
     */
    public void resetBuilder() {
        this.newClass = null;
        this.newClassBytes = null;
        this.rootClassLoader.unRegeditEngine(this);//
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
    public ClassEngine builderClass() throws ClassNotFoundException, IOException {
        if (newClassBytes != null)
            return this;
        //1.��ʼ������
        this.classNameStrategy.initStrategy(this);
        this.delegateStrategy.initStrategy(this);
        this.aopStrategy.initStrategy(this);
        this.propertyStrategy.initStrategy(this);
        this.methodStrategy.initStrategy(this);
        this.rootClassLoader.regeditEngine(this);//ע����װ��
        //2.
        if (EngineToos.checkClassName(this.className) == false)
            throw new FormatException("���������ʱ�����������ͨ����");
        if (className == null || className.equals("") == true)
            this.className = this.classNameStrategy.generateName(this.superClass);
        //3.
        ClassBuilder cb = this.createBuilder(this.builderMode);
        cb.initBuilder(this);
        this.configuration = cb.builderClass();
        if (this.configuration == null)
            throw new FormatException("builderClassʧ�ܡ�");
        this.newClassBytes = cb.getClassBytes();
        //5.
        this.newClass = this.rootClassLoader.loadClass(this.className);
        return this;
    };
    //======================================================================================Builder
    /**�������ͨ����д�÷���������һ���µ�ClassBuilder������ClassBuilder�����п�����Ա����ʹ��classcode��չ���ܣ�ͬʱҲ����ʹ��asm�������չ��*/
    protected ClassBuilder createBuilder(BuilderMode builderMode) {
        /*��ʵ��*/
        return new ClassBuilder() {
            protected ClassVisitor acceptClass(ClassWriter classVisitor) {
                return null;
            }
            protected void init(ClassEngine classEngine) {}
        };
    };
    //==========================================================================================New
    /**װ�ز��Ҵ�����������һ��ʵ�������������Propxyģʽ�µģ���Ҫָ������ĸ������͡������Super���null���ɡ�*/
    public Object newInstance(Object propxyBean) throws ClassNotFoundException, IOException {
        this.builderClass();
        Object obj = null;
        try {
            if (this.builderMode == BuilderMode.Super)
                obj = this.newClass.newInstance();
            else
                obj = this.newClass.getConstructor(this.superClass).newInstance(propxyBean);
        } catch (Exception e) {
            throw new InitializationException("��ʼ����������[" + this.newClass.getName() + "]", e.getCause());
        }
        return this.configuration.configBean(obj);
    };
    /**����bean��ִ��aopע��Ȳ������ò��������κ�ClassEngine�����Ķ���*/
    public Object configBean(Object bean) {
        if (bean == null)
            throw new NullPointerException("��������Ϊ��!");
        ClassLoader loader = bean.getClass().getClassLoader();
        if (loader instanceof RootClassLoader == false)
            return bean;
        RootClassLoader rootLoader = (RootClassLoader) loader;
        ClassEngine engine = rootLoader.getRegeditEngine(bean.getClass().getName());
        return engine.configuration.configBean(bean);
    };
    /**�жϸ�bean�Ƿ��Ѿ��������á�*/
    public boolean isConfig(Object bean) {
        if (bean == null)
            throw new NullPointerException("��������Ϊ��!");
        ClassLoader loader = bean.getClass().getClassLoader();
        if (loader instanceof RootClassLoader == false)
            throw new TypeException("��������ʾ��bean����һ����Ч������bean��");
        //
        try {
            Method method_1 = bean.getClass().getMethod("get" + BuilderClassAdapter.ConfigMarkName);
            Boolean res = (Boolean) method_1.invoke(bean);
            if (res == null)
                return false;
            return res;
        } catch (Exception e) {
            throw new InvokeException("��ִ�е����ڼ䷢���쳣��");
        }
    };
}