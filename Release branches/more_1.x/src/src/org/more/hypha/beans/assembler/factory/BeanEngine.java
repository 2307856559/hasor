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
package org.more.hypha.beans.assembler.factory;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.more.DoesSupportException;
import org.more.InitializationException;
import org.more.InvokeException;
import org.more.RepeateException;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ExpandPointManager;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.AbstractMethodDefine;
import org.more.hypha.beans.AbstractPropertyDefine;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.ValueMetaDataParser;
import org.more.hypha.beans.assembler.a.AfterCreateExpandPoint;
import org.more.hypha.beans.assembler.a.BeforeCreateExpandPoint;
import org.more.hypha.beans.assembler.a.ClassByteExpandPoint;
import org.more.hypha.beans.assembler.a.ClassTypeExpandPoint;
import org.more.hypha.beans.assembler.a.DecoratorExpandPoint;
import org.more.util.attribute.IAttribute;
/**
 * �����ְ���Ǹ���{@link AbstractBeanDefine}ת�������ͻ���Beanʵ�����
 * @version 2011-1-13
 * @author ������ (zyc@byshell.org)
 */
public class BeanEngine {
    private Map<String, BeanBuilder> beanBuilderMap     = new HashMap<String, BeanBuilder>();
    //
    private IAttribute               flashContext       = null;
    private ApplicationContext       applicationContext = null;
    //
    private Map<String, Object>      beanCache          = new Hashtable<String, Object>();
    private EngineClassLoader        classLoader        = null;
    private RootValueMetaDataParser  rootMetaDataParser = new RootValueMetaDataParser();
    private ByteCodeCache            byteCodeCache      = null;
    //----------------------------------------------------------------------------------------------------------
    public BeanEngine(ApplicationContext applicationContext, IAttribute flashContext) {
        this.flashContext = flashContext;
        this.applicationContext = applicationContext;
        this.classLoader = new EngineClassLoader(applicationContext.getBeanClassLoader());//ʹ��EngineClassLoaderװ�ع�����ֽ���
    };
    //----------------------------------------------------------------------------------------------------------
    class EngineClassLoader extends ClassLoader {
        public EngineClassLoader(ClassLoader parent) {
            super(parent);
        };
        public Class<?> loadClass(byte[] beanBytes, AbstractBeanDefine define) {
            //���������Ҫװ�ص�����JVM�Ͳ�����ñ��ص�������ȥ���������Ƿ���ڡ�
            return this.defineClass(beanBytes, 0, beanBytes.length);
        };
    };
    //----------------------------------------------------------------------------------------------------------
    /**�÷����ķ���ֵ�����Ƿ����������չ���ִ�У�flase��ʾ������(Ĭ��ֵ)��*/
    protected boolean isIgnorePoints() {
        return false;
    };
    /**�÷����ķ���ֵ�������Ƿ����ִ���������ڷ�����false��ʾ������(Ĭ��ֵ)��*/
    protected boolean isIgnoreLifeMethod() {
        return false;
    };
    /**��ȡ�ֽ��뻺�������󣬸û��������Ի���*/
    protected ByteCodeCache getByteCodeCache() {
        return this.byteCodeCache;
    };
    //----------------------------------------------------------------------------------------------------------
    /***/
    public void init() {
        this.beanCache.clear();
        this.byteCodeCache.clearCache();
    };
    /**
     * ע��һ��bean�������ͣ�ʹ֮���Ա��������������ظ�ע��ͬһ��bean���ͽ�������{@link RepeateException}�����쳣��
     * @param beanType ע���bean�������͡�
     * @param builder Ҫע���bean������������
     */
    public void regeditBeanBuilder(String beanType, BeanBuilder builder) throws RepeateException {
        if (this.beanBuilderMap.containsKey(beanType) == false)
            this.beanBuilderMap.put(beanType, builder);
        else
            throw new RepeateException("�����ظ�ע��[" + beanType + "]���͵�BeanBuilder��");
    };
    /**���ָ������bean�Ľ���֧�֣�����Ҫ�Ӵ�ע���bean�����Ƿ���ڸ÷������ᱻ��ȷִ�С�*/
    public void unRegeditBeanBuilder(String beanType) {
        if (this.beanBuilderMap.containsKey(beanType) == true)
            this.beanBuilderMap.remove(beanType);
    };
    /**�����{@link BeanEngine}������������ĵ���Bean����*/
    public void clearBeanCache() {
        this.beanCache.clear();
    };
    /**��ȡһ��int��int��ʾ��{@link BeanEngine}�������Ѿ������˵ĵ���������Ŀ��*/
    public int getCacheBeanCount() {
        return this.beanCache.size();
    };
    /**
     * ע��һ��{@link ValueMetaData}Ԫ��Ϣ��������ʹ֮���Ա����Խ���������������ظ�ע��ͬһ��Ԫ��Ϣ��������������{@link RepeateException}�����쳣��
     * @param metaDataType Ҫע���Ԫ��Ϣ���͡�
     * @param parser ������
     */
    public void regeditMetaDataParser(String metaDataType, ValueMetaDataParser<?> parser) throws RepeateException {
        this.rootMetaDataParser.addParser(metaDataType, parser);
    };
    /**���һ�����Խ�������ע�ᡣ*/
    public void unRegeditMetaDataParser(String metaDataType) {
        this.rootMetaDataParser.removeParser(metaDataType);
    };
    //----------------------------------------------------------------------------------------------------------
    /**
     * ��{@link AbstractBeanDefine}��������������װ�س�ΪClass���Ͷ���
     * �ڼ��һ������{@link ClassByteExpandPoint}��{@link ClassTypeExpandPoint}������չ�㡣
     * @param define Ҫװ�����͵�bean���塣
     * @return ����װ�ص�bean���͡�
     */
    public synchronized Class<?> builderType(AbstractBeanDefine define) throws DoesSupportException, InitializationException {
        String defineType = define.getBeanType();
        String defineID = define.getID();
        ExpandPointManager epm = this.applicationContext.getExpandPointManager();
        //
        BeanBuilder builder = this.beanBuilderMap.get(defineType);
        if (builder == null)
            throw new DoesSupportException("hypha ��֧�ֵ�Bean�������ͣ�" + defineType);
        //1.ȷ��������bean�Ƿ���Ա�װ�س�Ϊjava���͡�
        if (builder.canbuilder() == false)
            throw new DoesSupportException(defineID + "�������Ͷ��岻��ִ��װ�ع��̡�");
        //--------------------------------------------------------------------------------------------------------------׼���׶�
        //2.װ��class
        ByteCodeCache cache = this.getByteCodeCache();
        byte[] beanBytes = null;
        if (builder.canCache() == true)
            beanBytes = cache.loadCode(defineID);//��ͼ�ӻ�����װ��
        if (beanBytes == null) {
            beanBytes = builder.loadBeanBytes(define);
            if (this.isIgnorePoints() == false)
                //������þ�����������չ����ִ����չ�����
                beanBytes = (byte[]) epm.exePoint(ClassByteExpandPoint.class, new Object[] { beanBytes, define, this.applicationContext });
            cache.saveCode(defineID, beanBytes);//�����ֽ�����Ϣ
        }
        if (beanBytes == null)
            throw new NullPointerException("�����޷���ȡ�ֽ�����Ϣ�������޷�ת��Bean�����Ϊ���͡�");
        //3.װ��Class�࣬���װ�ز���hypha����ǿ��
        Class<?> beanType = this.classLoader.loadClass(beanBytes, define);
        if (this.isIgnorePoints() == false)
            //������þ�����������չ����ִ����չ�����
            beanType = (Class<?>) epm.exePoint(ClassTypeExpandPoint.class, new Object[] { beanType, define, this.applicationContext });
        if (beanType == null)
            throw new NullPointerException("��ʧBean���Ͷ��壬�������չ���Ƿ������������͡�");
        return beanType;
    };
    /**
     * �������洴������ȥ����bean���롣
     * @param define
     * @param params
     * @return
     */
    public synchronized Object builderBean(AbstractBeanDefine define, Object[] params) {
        String defineID = define.getID();
        Class<?> beanType = this.builderType(define);
        ExpandPointManager epm = this.applicationContext.getExpandPointManager();
        BeanBuilder builder = this.beanBuilderMap.get(define.getBeanType());
        //--------------------------------------------------------------------------------------------------------------��鵥̬
        //0.��̬
        if (this.beanCache.containsKey(defineID) == true)
            return this.beanCache.get(defineID);
        //--------------------------------------------------------------------------------------------------------------�����׶�
        //1.Ԥ����Bean
        Object obj = null;
        if (this.isIgnorePoints() == false)
            obj = epm.exePoint(BeforeCreateExpandPoint.class, new Object[] { beanType, params, define, this.applicationContext });
        //2.���û��Ԥ�����Ķ�����ִ��ϵͳĬ�ϵĴ�������.
        if (obj == null) {
            AbstractMethodDefine factory = define.factoryMethod();
            Collection<? extends AbstractPropertyDefine> initParam = null;
            if (factory != null) {
                //1.������ʽ
                initParam = factory.getParams();
                //TODO
                //
            } else {
                //2.ƽ����ʽ
                initParam = define.getInitParams();
                //TODO
                //
            }
        }
        obj = builder.builderBean(obj, define);
        //3.ִ�д����ĺ�������
        if (this.isIgnorePoints() == false)
            //������þ�����������չ����ִ����չ�����
            obj = epm.exePoint(AfterCreateExpandPoint.class, new Object[] { obj, params, define, this.applicationContext });
        if (obj == null)
            throw new InvokeException("����[" + defineID + "]���쳣���������������ߣ�װ�ε���չ�㷵��Ϊ�ա�");
        //--------------------------------------------------------------------------------------------------------------��ʼ���׶�
        //4.װ��
        if (this.isIgnorePoints() == false)
            //������þ�����������չ����ִ����չ�����
            obj = epm.exePoint(DecoratorExpandPoint.class, new Object[] { obj, params, define, this.applicationContext });
        //5.ִ�г�ʼ��
        Class<?> objType = obj.getClass();
        String initMethodName = define.getInitMethod();
        if (initMethodName != null)
            try {
                Method m = objType.getMethod(initMethodName, Object[].class);
                m.invoke(obj, params);//ִ�г�ʼ��
            } catch (Exception e) {
                throw new InitializationException(e);
            }
        //6.�������ٷ���
        //ProxyFinalizeClassEngine ce = new ProxyFinalizeClassEngine(this);
        //ce.setBuilderMode(BuilderMode.Propxy);
        //ce.setSuperClass(objType);
        //obj = ce.newInstance(obj);
        //8.��̬����&����ƥ��
        obj = this.cast(beanType, obj);
        if (define.isSingleton() == true)
            this.beanCache.put(defineID, obj);
        return obj;
    };
    /**�����������Ƿ�ƥ�䶨�����ͣ����û��ָ��beanType������ֱ�ӷ��ء�*/
    private Object cast(Class<?> beanType, Object obj) {
        if (beanType != null)
            return beanType.cast(obj);
        return obj;
    };
};
//class ProxyFinalizeClassEngine extends ClassEngine {
//    private final ProxyFinalizeClassBuilder builder = new ProxyFinalizeClassBuilder();
//    public ProxyFinalizeClassEngine(BeanEngine beanEngine) throws ClassNotFoundException {
//        super(false);
//    };
//    protected ClassBuilder createBuilder(BuilderMode builderMode) {
//        return this.builder;
//    };
//    public Object newInstance(Object propxyBean) throws FormatException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
//        Object obj = super.newInstance(propxyBean);
//        //this.toClass().getMethod("", parameterTypes);
//        return obj;
//    };
//};
//class ProxyFinalizeClassBuilder extends ClassBuilder {
//    protected ClassAdapter acceptClass(ClassWriter classVisitor) {
//        return new ClassAdapter(classVisitor) {
//            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//                if (name.equals("finalize()V") == true)
//                    System.out.println();
//                return super.visitMethod(access, name, desc, signature, exceptions);
//            }
//        };
//    }
//};