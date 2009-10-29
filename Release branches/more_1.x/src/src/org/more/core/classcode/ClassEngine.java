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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.UUID;
import org.more.FormatException;
import org.more.InvokeException;
import org.more.TypeException;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassReader;
import org.more.core.asm.ClassVisitor;
import org.more.core.asm.ClassWriter;
import org.more.core.asm.Opcodes;
import org.more.log.ILog;
import org.more.log.LogFactory;
/**
 * More��Ŀ��ClassCode���ߺ����ࡣʹ�øù��߿��Ը���һ������������һ���µ��࣬���ҿ�������
 * ������AOP������֮��ù��߻�����ʹһ����������ʱ����һ���ӿ�ʵ�ֲ��ҿ�������������Щ�ӿڷ�����
 * �����õķ�����ͨ�����ӽӿ�ʱ���ݵ�ί����������ҵ���߼���
 * Date : 2009-10-15
 * @author ������
 */
public abstract class ClassEngine extends ClassLoader implements Opcodes {
    //================================================================================Builder Const
    static final String                             BuilderClassPrefix    = "$More_";                                      //�������������׺��
    static final String                             ObjectDelegateMapName = "$delegateMap";                                //��������ί��ӳ�������
    static final String                             DelegateMapUUIDPrefix = "$MoreDelegate_";                              //�����ί��ӳ���е�KEYǰ׺
    static final String                             DefaultPackageName    = "org.more.core.classcode.test";                //Ĭ������������
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
    //    /** ί�з���ӳ�� */
    //    private Hashtable<String, Method>      delegateMethodMap     = new Hashtable<String, Method>();             //ί�з���ӳ�䡣 
    /** ���ɵ���������β���ĵ�����ʶ�� */
    private static long                             builderClassNumber    = 0;
    /** ���������־����־�ӿڡ� */
    private static ILog                             log                   = LogFactory.getLog("org_more_core_classcode");
    //    //AOP�������
    //    private MethodInvokeFilter             methodFilter      = null;                                        //ʵ��AOP�ķ������ù�����
    //==================================================================================Constructor
    /** ����һ��ClassEngine���Ͷ���Ĭ�����ɵ�����Object�����ࡣ */
    public ClassEngine() {
        this(ClassEngine.DefaultPackageName + ".Object" + ClassEngine.getPrefix(), Object.class, Thread.currentThread().getContextClassLoader());
    }
    /** ʹ��ָ����������һ��ClassEngine���Ͷ������ָ���������ǿ������Object��Ϊ���ࡣ */
    public ClassEngine(String className, Class<?> superClass) {
        this(className, superClass, Thread.currentThread().getContextClassLoader());
    }
    /** ����һ��ClassEngine���Ͷ���Ĭ�����ɵ�����Object�����ࡣ */
    public ClassEngine(ClassLoader parentLoader) {
        this(ClassEngine.DefaultPackageName + ".Object" + ClassEngine.getPrefix(), Object.class, parentLoader);
    }
    /** ʹ��ָ����������һ��ClassEngine���Ͷ������ָ���������ǿ������Object��Ϊ���ࡣ */
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
    //==========================================================================================Job
    /**
     * �����и���һ���ӿ�ʵ�֡��ýӿ��е����з�����ͨ��ί�ж��������������ӵĽӿ����з��������ķ�����ͻʱ��
     * appendImpl�ᶪ����ӽӿڵĳ�ͻ�����������෽�����������൱�ڻ���ķ���ʵ���˽ӿڵķ�����
     * ���������һ��ǩ���ķ���ʱClassEngineֻ�ᱣ�����һ�ε�ע�ᡣ������ķ�����������ʱ�ᱣ����ע�����Ϣ��
     * ����ظ����ͬһ���ӿ���ýӿڽ����������һ����ӡ�
     * ע�⣺�����ͼ���һ���ǽӿ�������������쳣��
     * @param interfaceName Ҫ���ӵĽӿڡ�
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
     * ���ɲ��Ҵ�����������ͬʱװ���¶���Ĵ����������������û����������������ɲ�����
     * @return ���ش�������װ����������
     * @throws InvokeException ����toClassʱ�����쳣��
     */
    public Object newInstance() throws Exception {
        //1.��������
        log.debug("newInstance this class!");
        Object obj = this.toClass().newInstance();
        Hashtable<String, Method> map = new Hashtable<String, Method>();
        //2.׼��ע������
        for (Class<?> type : this.impls.keySet()) {
            Method m = new Method();
            m.uuid = ClassEngine.DelegateMapUUIDPrefix + UUID.randomUUID().toString().replace("-", "");
            m.delegate = this.impls.get(type);
            map.put(type.getName(), m);
        }
        //3.ִ��ע��
        Field field = obj.getClass().getField(ClassEngine.ObjectDelegateMapName);
        field.set(obj, map);
        String className = obj.getClass().getName();
        log.debug("configure object [" + className + "]" + obj);
        return obj;
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
        log.debug("ready [OK]!");
        //------------------------------����ʹ�ô����������з���
        BuilderClassAdapter builderAdapter = new BuilderClassAdapter(this, acceptVisitor, this.superClass, this.impls);
        //------------------------------�ġ�����ClassReader�������ԭʼ��
        ClassAdapter ca = new ClassAdapter(builderAdapter);
        reader.accept(ca, ClassReader.SKIP_DEBUG);
        //------------------------------�塢ȡ�����ɵ��ֽ���
        log.debug("generated Class [OK]! get ByteCode");
        this.classByte = writer.toByteArray();
        //------------------------------���������ɵ�����󸽼�AOP����
        //
        //------------------------------�ߡ�����Class����
        //this.classByte = this.classByte;
        this.classType = this.loadClass(this.className);
    }
    /** ����������յ�ClassAdapter������ClassAdapter���Խ��յ����ӽӿ���ص�visit�� */
    protected abstract ClassAdapter acceptClass(final ClassWriter classWriter);
}