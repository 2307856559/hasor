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
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.UUID;
import org.more.FormatException;
import org.more.TypeException;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassReader;
import org.more.core.asm.ClassVisitor;
import org.more.core.asm.ClassWriter;
import org.more.core.asm.Opcodes;
import org.more.log.ILog;
import org.more.log.LogFactory;
/**
 * �ֽ������ɹ��ߣ��ù��߿��������������ϸ��ӽӿ�ʵ�֣�ʹ��ClassEngine�����Զ�������ṩAOP��֧�֡�ClassEngine�ṩ�����ֹ���ģʽ��<br/>
 * <b>�̳з�ʽ</b>--�̳з�ʽʵ�����࣬��������ģʽ�±���Ҫ���������ͺ��ж������ɵ������Ǽ̳�ԭ����ʵ�ֵģ�
 * ���и��ӷ�����д�������С�ԭʼ���е����з���������д������super��ʽ���ø��ࡣ˽�з�����������д���롣
 * ˽�з�����������AOP���ܡ��ڼ̳�ģʽ�±��������빫����������AOP���ܡ�<br/>
 * <b>����ʽ</b>--����ʽʵ�����࣬��������ģʽ�¿��������еĶ����ϸ��ӽӿ�ʵ�ֶ�����Ҫ���´�������ͬʱ���ɵ��¶���
 * ���ƻ�ԭ�ж�������ʵ�ַ�ʽ����һ����̬����ʽʵ�֡�ע���������ɷ�ʽ��ȡ������ԭʼ���еĹ��췽����
 * ȡ����֮��������һ��һ�������Ĺ��췽�����ò������;��ǻ������͡����з������ö�ʹ�����ע������Ͷ�����á�
 * ͬʱ�������ɷ�ʽ��˽�з�����������д���롣<br/>
 * �ڴ���ģʽ��ֻ�й�����������AOP���ܣ�˽�з������ܱ����ķ��������Ȩ�����ⲻ�ܲ���AOP��<br/>
 * <b>AOP����</b>--ClassEngine�����AOP�����ǿ��������Ƿ����õġ��������AOP��ع������ֽ���������ʱ���˾����˵�һ�νӿڸ��Ӳ���֮��
 * ����Ҫ�����ڶ���AOP���Լ��롣���б��෽���������Լ̳еķ���������д������AOP���Ի����������ֽ������ͬʱҲ�Ȳ�ʹ��AOP���Ե�����Ч��Ҫ��Щ��
 * <br/>Date : 2009-10-15
 * @author ������
 */
public class ClassEngine extends ClassLoader implements Opcodes {
    //=================================================================================Builder Type
    /** ClassEngine����������ģʽ�� */
    public enum BuilderMode {
        /** 
         * �̳з�ʽʵ�����࣬��������ģʽ�±���Ҫ���������ͺ��ж������ɵ������Ǽ̳�ԭ����ʵ�ֵģ�
         * ���и��ӷ�����д�������С�ԭʼ���е����з���������д������super��ʽ���ø��ࡣ˽�з�����������д���롣
         * ˽�з�����������AOP���ܡ��ڼ̳�ģʽ�±��������빫����������AOP���ܡ�
         */
        Super,
        /** 
         * ����ʽʵ�����࣬��������ģʽ�¿��������еĶ����ϸ��ӽӿ�ʵ�ֶ�����Ҫ���´�������ͬʱ���ɵ��¶���
         * ���ƻ�ԭ�ж�������ʵ�ַ�ʽ����һ����̬����ʽʵ�֡�ע���������ɷ�ʽ��ȡ������ԭʼ���еĹ��췽����
         * ȡ����֮��������һ��һ�������Ĺ��췽�����ò������;��ǻ������͡����з������ö�ʹ�����ע������Ͷ�����á�
         * ͬʱ�������ɷ�ʽ��˽�з�����������д���롣<br/>
         * �ڴ���ģʽ��ֻ�й�����������AOP���ܣ�˽�з������ܱ����ķ��������Ȩ�����ⲻ�ܲ���AOP��
         */
        Propxy
    }
    //================================================================================Builder Const
    static final String                             BuilderClassPrefix    = "$More_";                                      //�������������׺��
    static final String                             DefaultPackageName    = "org.more.core.classcode.test";                //Ĭ������������
    static final String                             ObjectDelegateMapName = "$More_DelegateMap";                           //��������ί��ӳ�������
    static final String                             DelegateMapUUIDPrefix = "$More_DelegatePrefix_";                       //�����ί��ӳ���е�KEYǰ׺
    static final String                             PropxyModeObjectName  = "$More_PropxyObject";                          //����ʽʵ��ʱ������������е�����
    static final String                             AOPFilterChainName    = "$More_AOPFilterChain";                        //AOP��������������
    static final String                             AOPMethodNamePrefix   = "$More_";                                      //AOP������ԭʼ������ǰ׺
    //========================================================================================Field
    /** ���ɵ��������� */
    private String                                  className             = null;
    /** ���ɵ��������� */
    private Class<?>                                classType             = null;
    /** ���ɵ�������ֽ��� */
    private byte[]                                  classByte             = null;
    /** ������Ļ������� */
    private Class<?>                                superClass            = Object.class;
    /** ���ɵ�����������ʵ�ֵĽӿ� */
    private LinkedHashMap<Class<?>, MethodDelegate> impls                 = new LinkedHashMap<Class<?>, MethodDelegate>(0);
    /** ����ģʽ��Ĭ���Ǽ̳з�ʽ */
    private ClassEngine.BuilderMode                 mode                  = ClassEngine.BuilderMode.Super;
    /** �Ƿ�����������AOP��װ�� */
    private boolean                                 enableAOP             = true;
    /** ���ɵ���������β���ĵ�����ʶ�� */
    private static long                             builderClassNumber    = 0;
    /** ���������־����־�ӿڡ� */
    private static ILog                             log                   = LogFactory.getLog("org_more_core_classcode");
    /**ʵ��AOP�ķ������ù�������ϡ�*/
    private ImplAOPFilterChain                      invokeFilterChain     = null;
    //
    /**���澭��AOP����֮��ķ����ʹ�������*/
    LinkedHashMap<String, AOPMethods>               aopMethods            = new LinkedHashMap<String, AOPMethods>(0);
    //==================================================================================Constructor
    /** ����һ��ClassEngine���Ͷ���Ĭ�����ɵ�����Object�����࣬ʹ�õ��ǵ�ǰ�̵߳�ClassLoader��װ�ض�����Ϊ��װ������ */
    public ClassEngine() {
        this(ClassEngine.DefaultPackageName + ".Object" + ClassEngine.getPrefix(), Object.class, Thread.currentThread().getContextClassLoader());
    }
    /** ʹ��ָ����������һ��ClassEngine���Ͷ������ָ���������ǿ������Object��Ϊ���࣬ʹ�õ��ǵ�ǰ�̵߳�ClassLoader��װ�ض�����Ϊ��װ������ */
    public ClassEngine(String className, Class<?> superClass) {
        this(className, superClass, Thread.currentThread().getContextClassLoader());
    }
    /** ����һ��ClassEngine���Ͷ���Ĭ�����ɵ�����Object�����࣬ͬʱָ����װ������ */
    public ClassEngine(ClassLoader parentLoader) {
        this(ClassEngine.DefaultPackageName + ".Object" + ClassEngine.getPrefix(), Object.class, parentLoader);
    }
    /** ʹ��ָ����������һ��ClassEngine���Ͷ������ָ���������ǿ������Object��Ϊ���࣬ͬʱָ����װ������ */
    public ClassEngine(String className, Class<?> superClass, ClassLoader parentLoader) {
        super(parentLoader);
        if (className == null || className.equals("") || superClass == null)
            throw new TypeException("����ָ��className��superClass����������className����Ϊ���ַ�����");
        this.setNewClass(className, superClass);
    }
    //======================================================================================Get/Set
    /** ��ȡ���������ĺ�׺��ţ�ÿһ�ε��ø÷������ط���һ���µĺ�׺���� */
    private static synchronized String getPrefix() {
        String prefix = ClassEngine.BuilderClassPrefix + ClassEngine.builderClassNumber;
        ClassEngine.builderClassNumber++;//ȫ����������1
        return prefix;
    }
    /** ������������⸸�����Ϣ�� */
    private void setNewClass(String newClassName, Class<?> newClass) {
        //1.��ʽ��ȷ�ж�
        if (EngineToos.checkClassName(newClassName) == false)
            throw new FormatException("����[" + newClassName + "]����һ����ʽ���õ�������");
        if (newClass.isEnum() == true || newClass.isAnnotation() == true || newClass.isInterface() == true || //
                EngineToos.checkIn(newClass.getModifiers(), Modifier.ABSTRACT) == true || EngineToos.checkIn(newClass.getModifiers(), Modifier.FINAL) == true)
            throw new FormatException("����[" + newClass + "]����һ����֧�ֵ��ࡣע����಻��������������[enum��abstract��interface��final��annotation].");
        //2.���Ϊ����ģʽ��Ҫ�����б������һ���������޲ι��캯����
        if (this.mode == BuilderMode.Propxy) {
            try {
                newClass.getConstructor();
            } catch (Exception e) {
                throw new FormatException("ʹ�ô���ģʽ�����࣬�������ӵ��һ���޲εĹ��캯����");
            }
        }
        //2.ȷ������
        this.superClass = newClass;
        this.className = newClassName;
        log.debug("builderClass the superClass=" + this.superClass + " ,className=" + this.className);
        //3.��ʼ���ֽ�����Ϣ
        if (this.classByte != null) {
            this.resetByte();
            this.impls.clear();
        }
    }
    /**
     * ��ȡ���ɵ��������޶������������֡�
     * @return �������ɵ��������޶������������֡�
     */
    public String getSimpleName() {
        return EngineToos.splitSimpleName(this.className);
    }
    /**
     * ��ȡ�����������������
     * @return ���������������������
     */
    public String getClassName() {
        return this.className;
    }
    /**
     * ��ȡ������ĳ���(����)
     * @return ����������ĳ���(����)
     */
    public Class<?> getSuperClass() {
        return this.superClass;
    }
    /**
     * ����������Ļ������͡�ÿ�θı�������Ͷ��ᵼ����ո���ʵ�ֽӿ��б�ͬʱ������ɵ��ֽ������ݡ�
     * @param type ������Ļ�������
     */
    public void setSuperClass(Class<?> type) {
        this.setNewClass(this.className, type);
    }
    /**
     * �������������������õ�������Ϊnull���µ������ǻ�����β������$More_&lt;n&gt;n��һ���Զ���š�
     * ÿ�θı�������Ͷ��ᵼ����ո���ʵ�ֽӿ��б�ͬʱ������ɵ��ֽ������ݡ�
     * @param className Ҫ���õ�������
     */
    public void setClassName(String className) {
        this.setNewClass(className, this.superClass);
    }
    /**
     * ��ȡ���ɵ���������ʵ�ֵĽӿڼ��ϡ�appendImpl�������Ը���һ���µĽӿ�ʵ�֡�
     * @return �������ɵ���������ʵ�ֵĽӿڼ��ϡ�appendImpl�������Ը���һ���µĽӿ�ʵ�֡�
     */
    public Class<?>[] getAppendImpls() {
        Class<?>[] cl = new Class<?>[this.impls.size()];
        this.impls.keySet().toArray(cl);
        return cl;
    }
    /**
     * ��ȡ��ǰ���������ģʽ������ģʽ��ClassEngine.BuilderModeö�پ�����Ĭ������ģʽ��ClassEngine.BuilderMode.Super
     * @return ���ص�ǰ���������ģʽ������ģʽ��ClassEngine.BuilderModeö�پ�����
     */
    public ClassEngine.BuilderMode getMode() {
        return mode;
    }
    /**
     * ���õ�ǰ���������ģʽ������ģʽ��ClassEngine.BuilderModeö�ٶ��塣Ĭ�ϵ�����ģʽ��ClassEngine.BuilderMode.Super
     * ����������µ�����ģʽ�������ClassEngine���ֽ����ʼ�������������˳�ʼ�������ֽ�����Ҫ�������ɡ�
     * @param mode ���õ�������ģʽ��
     */
    public void setMode(ClassEngine.BuilderMode mode) {
        if (this.mode == mode)
            return;
        this.mode = mode;
        this.setNewClass(this.className, this.superClass);
    }
    /**
     * ��ȡ���浱ǰ�Ƿ���������ʱ��AOP���Լ��롣����AOP���Ի����Ӷ�����ֽ�������Ż��û��AOP���Ե���������Ч��Ҫ�ס� Ĭ��������AOP���Եġ�
     * @return �������浱ǰ�Ƿ���������ʱ��AOP���ԡ�
     */
    public boolean isEnableAOP() {
        return enableAOP;
    }
    /**
     * �������浱ǰ�Ƿ���������ʱ��AOP���Լ��롣����AOP���Ի����Ӷ�����ֽ�������Ż��û��AOP���Ե���������Ч��Ҫ�ס� Ĭ��������AOP���Եġ�
     * @param enableAOP true��ʾ����AOP����(Ĭ��)��false��ʾ��ʹ��AOP���ԡ�
     */
    public void setEnableAOP(boolean enableAOP) {
        this.enableAOP = enableAOP;
        this.setNewClass(this.className, this.superClass);
    }
    //==========================================================================================Job
    /**
     * �����и���һ���ӿ�ʵ�֡��ýӿ��е����з�����ͨ��ί�ж��������������ӵĽӿ����з��������ķ�����ͻʱ��
     * appendImpl�ᶪ����ӽӿڵĳ�ͻ�����������෽�����������൱�ڻ���ķ���ʵ���˽ӿڵķ�����
     * ���������һ��ǩ���ķ���ʱClassEngineֻ�ᱣ�����һ�ε�ע�ᡣ������ķ�����������ʱ�ᱣ����ע�����Ϣ��
     * ����ظ����ͬһ���ӿ���ýӿڽ����������һ����ӡ�
     * ע�⣺�����ͼ���һ���ǽӿ�������������쳣��
     * @param appendInterface Ҫ���ӵĽӿڡ�
     * @param delegate ���ӽӿڵķ�������ί�С�
     */
    public void appendImpl(Class<?> appendInterface, MethodDelegate delegate) {
        //1.�����ж�
        if (appendInterface.isInterface() == false || delegate == null)
            throw new FormatException("����appendInterface����һ����Ч�Ľӿڣ����߲���delegateΪ�ա�");
        //2.���Ըýӿ��Ƿ��Ѿ��õ�ʵ��
        try {
            this.superClass.asSubclass(appendInterface);
            return;
        } catch (Exception e) {}
        //3.����ظ�
        if (this.impls.containsKey(appendInterface) == true)
            this.impls.remove(appendInterface);
        //4.���ӽӿ�ʵ��
        this.setNewClass(this.className, this.superClass);
        this.impls.put(appendInterface, delegate);
    }
    /**
     * ����AOP�ص���������������һ�����������ϡ���ִ�й���������ʱ������������˳�����ִ��4321��
     * ����˵Խ��������ǰ�˵Ķ����ڹ��������е�λ�þ�Խ�����ڹ�����ִ����Ϸ����Ƿ�ִ��˳����1234��
     * @param filters Ҫ���õĹ����������顣
     */
    public void setCallBacks(AOPInvokeFilter[] filters) {
        if (filters == null)
            return;
        ImplAOPFilterChain filterChain = new ImplAOPFilterChain(null, null);
        for (AOPInvokeFilter thisFilter : filters)
            filterChain = new ImplAOPFilterChain(thisFilter, filterChain);
        this.invokeFilterChain = filterChain;
    }
    //==================================================================================newInstance
    /** ������ɵ��ֽ������ݡ� */
    public void resetByte() {
        this.classByte = null;
        this.classType = null;;
    }
    /**
     * ��ȡ�Ѿ����ɵ��ֽ������ݣ�����໹û�о���������÷������������ɲ��������ɷ�����builderClass();
     * @return �������ɵ��ֽ������ݡ�
     * @throws IOException �ڵ���builderClassʱ�����쳣��ͨ������IO�쳣���޷���ȡ�������ͻ��߸��ӽӿ����͡�
     * @throws ClassNotFoundException �ڵ���builderClassʱ�����쳣��ͨ���������쳣�����ɵ����ʽ���ִ�������޷�װ�����ࡣ
     */
    public byte[] toBytes() throws IOException, ClassNotFoundException {
        if (this.classByte == null)
            this.builderClass();
        return this.classByte;
    };
    /**
     * ��ȡ�Ѿ����ɵ����������໹û�о���������÷������������ɲ��������ɷ�����builderClass();
     * @return �������ɵ��ֽ������ݡ�
     * @throws IOException �ڵ���builderClassʱ�����쳣��ͨ������IO�쳣���޷���ȡ�������ͻ��߸��ӽӿ����͡�
     * @throws ClassNotFoundException �ڵ���builderClassʱ�����쳣��ͨ���������쳣�����ɵ����ʽ���ִ�������޷�װ�����ࡣ
     */
    public Class<?> toClass() throws IOException, ClassNotFoundException {
        if (this.classType == null)
            this.builderClass();
        return this.classType;
    };
    /**
     * ʹ��Ĭ�Ϲ��췽�������µ��������װ������¶�������������û����������������ɲ��������⵱ClassEngine�����ڴ���ģʽʱ
     * ����ָ������superClassObject���������ڼ̳�ģʽʱ�ò����������ԡ�
     * @return ���ش�������װ����������
     */
    public Object newInstance(Object superClassObject) throws Exception {
        //1.��������
        log.debug("newInstance this class!");
        Object obj = null;
        if (this.mode == ClassEngine.BuilderMode.Super)
            //Super
            obj = this.toClass().newInstance();
        else
            obj = this.toClass().getConstructor(this.superClass).newInstance(superClassObject);
        return this.configuration(obj);
    }
    /**
     * ��ĳ�������ɵ������ִ��װ�䡣ClassEngine���ɵ������п��ܴ��ڸ��ӽӿڡ���Щ���ӵĽӿ�ί�����µ������ǲ����ڵġ�
     * �����Ҫװ�䣬���򵱵�����Щ���ӽӿڷ���ʱ�������ָ���쳣��configuration����װ������¶���ķ�����ע�⣺���ɵ������Ӧ��ʹ��
     * ��Ӧ��ClassEngine����װ�䡣װ����̾��ǽ�ί�ж���ע�뵽��������еĹ��̡����⻹��Ҫע��������ClassEngine�ڴ������һ�������
     * ֮���޸��˻������Ϣ�����������µ��������ô��һ���������Զ������ִ��װ����̡�
     * @param newAsmObject Ҫװ��������
     * @return ����װ��֮��������
     * @throws Exception �����װ���ڼ䷢���쳣��
     */
    public Object configuration(Object newAsmObject) throws Exception {
        //1.���Ŀ��Ҫװ��Ķ������ڵ�ǰ�������ɵ��������ȡ��װ����̲��ҷ���null;
        if (newAsmObject.getClass() != this.classType)
            return null;
        //3.ִ��װ����̣�׼��ע������
        Hashtable<String, Method> map = new Hashtable<String, Method>();
        for (Class<?> type : this.impls.keySet()) {
            Method m = new Method();
            m.uuid = ClassEngine.DelegateMapUUIDPrefix + UUID.randomUUID().toString().replace("-", "");
            m.delegate = this.impls.get(type);
            map.put(type.getName(), m);
        }
        //4.ִ�д���ע��
        java.lang.reflect.Method m1 = newAsmObject.getClass().getMethod("set" + ClassEngine.ObjectDelegateMapName, Hashtable.class);
        m1.invoke(newAsmObject, map);
        //5.װ��AOP��
        if (this.enableAOP == true && this.invokeFilterChain != null) {
            java.lang.reflect.Method m2 = this.classType.getMethod("set" + ClassEngine.AOPFilterChainName, ImplAOPFilterChain.class);
            m2.invoke(newAsmObject, this.invokeFilterChain);
        }
        log.debug("configure object [" + this.className + "]");
        return newAsmObject;
    }
    //=================================================================================BuilderClass
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.equals(this.className) == true)
            return this.defineClass(name, this.classByte, 0, this.classByte.length);
        else
            return super.findClass(name);
    }
    /**
     * ��������ֽ���
     * @throws IOException ͨ������IO�쳣���޷���ȡ�������ͻ��߸��ӽӿ����͡�
     * @throws ClassNotFoundException ͨ���������쳣�����ɵ����ʽ���ִ�������޷�װ�����ࡣ
     */
    public synchronized void builderClass() throws IOException, ClassNotFoundException {
        //------------------------------һ��ȷ���Ƿ���������ֽ��� 
        if (this.classByte != null) {
            //����Ѿ�����������ֽ����򷵻����ɵ��ֽ���
            log.debug("Byte-code has been generated! return this method");
            return;
        } else
            log.debug("builderClass!");
        //------------------------------����׼�������ֽ�����������
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        InputStream inStream = EngineToos.getClassInputStream(this.superClass);//��ȡ������
        ClassReader reader = new ClassReader(inStream);//����ClassReader
        log.debug("ready builderClass stream=" + inStream + ", reader=" + reader + ", writer=" + writer);
        ClassVisitor acceptVisitor = this.acceptClass(writer);
        if (acceptVisitor == null) {
            log.debug("ready [Error] method acceptClass return null!");
            acceptVisitor = new ClassAdapter(writer);
        }
        //------------------------------���������ɵ�����󸽼�AOP����
        if (this.enableAOP == true && this.invokeFilterChain != null)
            acceptVisitor = new AOPClassAdapter(acceptVisitor, this);
        log.debug("ready [OK]!");
        //------------------------------�ġ�ʹ�ô����������з���
        BuilderClassAdapter builderAdapter = new BuilderClassAdapter(this, acceptVisitor, this.superClass, this.impls);
        //------------------------------�塢����ClassReader�������ԭʼ�࣬�������¶���
        ClassAdapter ca = new ClassAdapter(builderAdapter);
        reader.accept(ca, ClassReader.SKIP_DEBUG);
        log.debug("generated Class [OK]! get ByteCode");
        //------------------------------����ȡ�����ɵ��ֽ���
        this.classByte = writer.toByteArray();
        this.classType = this.loadClass(this.className);
        //
        for (java.lang.reflect.Method m : this.classType.getDeclaredMethods()) {
            String desc = EngineToos.toAsmType(m.getParameterTypes());
            String returnDesc = EngineToos.toAsmType(m.getReturnType());
            String fullName = m.getName() + "(" + desc + ")" + returnDesc;
            if (this.aopMethods.containsKey(fullName) == true) {
                java.lang.reflect.Method m1 = EngineToos.getMethod(this.classType, this.AOPMethodNamePrefix + m.getName(), m.getParameterTypes());
                this.aopMethods.put(fullName, new AOPMethods(m1, m));
            }
        }
    }
    /** ����������յ�ClassAdapter������ClassAdapter���Ը����ɵĿ����ֽ���ע����������д�÷���ʱһ��Ҫʹ��classWriter��Ϊ���յ��ֽ����������*/
    protected ClassAdapter acceptClass(final ClassWriter classWriter) {
        return null;
    };
    /** �ڲ����Է���*/
    boolean ignoreMethod(String fullDesc) {
        String[] ignoreMethod = new String[2];
        ignoreMethod[0] = "set" + ClassEngine.ObjectDelegateMapName + "(Ljava/util/Hashtable;)V";
        ignoreMethod[1] = "set" + ClassEngine.AOPFilterChainName + "(Lorg/more/core/classcode/ImplAOPFilterChain;)V";
        for (String n : ignoreMethod)
            if (n.equals(fullDesc) == true)
                return false;
        //���췽��
        if (fullDesc.indexOf("<init>") != -1)
            return false;
        return true;
    };
    /** ����������ֵ���������Ƿ���AOP������*/
    protected boolean acceptMethod(java.lang.reflect.Method method) {
        return true;
    };
}