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
package org.more.hypha.beans.assembler;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.more.ClassFormatException;
import org.more.DoesSupportException;
import org.more.RepeateException;
import org.more.core.ognl.OgnlContext;
import org.more.hypha.AbstractExpandPointManager;
import org.more.hypha.ApplicationContext;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.AbstractMethodDefine;
import org.more.hypha.beans.AbstractPropertyDefine;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.ValueMetaDataParser;
import org.more.util.attribute.IAttribute;
/**
* �����ְ���Ǹ���{@link AbstractBeanDefine}ת�������ͻ���Beanʵ�����
* @version 2011-1-13
* @author ������ (zyc@byshell.org)
*/
public class BeanEngine {
    //0.����
    private Map<String, AbstractBeanBuilder<AbstractBeanDefine>> beanBuilderMap     = new HashMap<String, AbstractBeanBuilder<AbstractBeanDefine>>();
    private IAttribute                                           flashContext       = null;
    private ApplicationContext                                   applicationContext = null;
    private AbstractExpandPointManager                           epManager          = null;
    //1.���ʹ������ȡ
    private EngineClassLoader                                    classLoader        = null;
    //2.���󴴽�
    private Map<String, Object>                                  singleBeanCache    = new Hashtable<String, Object>();
    private RootValueMetaDataParser                              rootMetaDataParser = new RootValueMetaDataParser();
    //3.װ��
    //
    //----------------------------------------------------------------------------------------------------------
    public BeanEngine(ApplicationContext applicationContext, IAttribute flashContext) {
        this.flashContext = flashContext;
        this.applicationContext = applicationContext;
        this.init(flashContext);
    };
    //----------------------------------------------------------------------------------------------------------
    class EngineClassLoader extends ClassLoader {
        public EngineClassLoader(ClassLoader parent) {
            super(parent);
        };
        public Class<?> loadClass(byte[] beanBytes, AbstractBeanDefine define) throws ClassFormatException {
            //���������Ҫװ�ص�����JVM�Ͳ�����ñ��ص�������ȥ���������Ƿ���ڡ�
            return this.defineClass(beanBytes, 0, beanBytes.length);
        };
    };
    //----------------------------------------------------------------------------------------------------------
    /**��{@link BeanEngine}�ĳ�ʼ��������*/
    protected void init(IAttribute flashContext) {
        this.singleBeanCache.clear();
        this.classLoader = new EngineClassLoader(this.applicationContext.getBeanClassLoader());//ʹ��EngineClassLoaderװ�ع�����ֽ���
    };
    /**
     * ע��һ��bean�������ͣ�ʹ֮���Ա��������������ظ�ע��ͬһ��bean���ͽ�������{@link RepeateException}�����쳣��
     * @param beanType ע���bean�������͡�
     * @param builder Ҫע���bean������������
     */
    public void regeditBeanBuilder(String beanType, AbstractBeanBuilder<AbstractBeanDefine> builder) throws RepeateException {
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
        this.singleBeanCache.clear();
    };
    /**��ȡһ��int��int��ʾ��{@link BeanEngine}�������Ѿ������˵ĵ���������Ŀ��*/
    public int getCacheBeanCount() {
        return this.singleBeanCache.size();
    };
    /**
     * ע��һ��{@link ValueMetaData}Ԫ��Ϣ��������ʹ֮���Ա����Խ���������������ظ�ע��ͬһ��Ԫ��Ϣ��������������{@link RepeateException}�����쳣��
     * @param metaDataType Ҫע���Ԫ��Ϣ���ͣ���ֵ���Դ�{@link ValueMetaData#getMetaDataType}�ӿڷ�����á�
     * @param parser ������
     */
    public void regeditMetaDataParser(String metaDataType, ValueMetaDataParser<ValueMetaData> parser) throws RepeateException {
        this.rootMetaDataParser.addParser(metaDataType, parser);
    };
    /**���һ�����Խ�������ע�ᣬ��ֵ���Դ�{@link ValueMetaData#getMetaDataType}�ӿڷ�����á�*/
    public void unRegeditMetaDataParser(String metaDataType) {
        this.rootMetaDataParser.removeParser(metaDataType);
    };
    //----------------------------------------------------------------------------------------------------------
    /**
     * ��{@link AbstractBeanDefine}��������������װ�س�ΪClass���Ͷ����ڼ��һ������{@link ClassByteExpandPoint}��{@link ClassTypeExpandPoint}������չ�㡣
     * @param define Ҫװ�����͵�bean���塣
     * @return ����װ�ص�bean���͡�
     * @throws DoesSupportException ���{@link BeanEngine}����һ����֧�ֵ�Bean������������������쳣��
     * @throws IOException �ڽ��ֽ���ת����Class���ʱ����������ⳣ����������
     * @throws ClassFormatException ��ִ����չ��{@link ClassTypeExpandPoint}������ܳɹ�װ���ֽ������������쳣��
     * @throws ClassNotFoundException �������װ�ع��̽���û�еõ�Class����������������쳣��
     */
    public synchronized Class<?> builderType(AbstractBeanDefine define) throws DoesSupportException, IOException, ClassFormatException, ClassNotFoundException {
        String defineType = define.getBeanType();
        String defineID = define.getID();
        String defineLogStr = defineID + "[" + defineType + "]";//logǰ׺
        //1.
        AbstractBeanBuilder<AbstractBeanDefine> builder = this.beanBuilderMap.get(defineType);
        if (builder == null || builder.canBuilder() == false)
            throw new DoesSupportException(defineLogStr + "����Bean����һ��hypha��֧�ֵ�Bean���ͻ��߸����͵�Bean��֧��Builder��");
        //--------------------------------------------------------------------------------------------------------------׼���׶�
        //2.ͨ��Builderװ��class
        byte[] beanBytes = builder.loadBeanBytes(define);
        beanBytes = (byte[]) this.epManager.exePoint(ClassByteExpandPoint.class, define, new Object[] { beanBytes, define, this.applicationContext });
        Class<?> beanType = (Class<?>) this.epManager.exePoint(ClassTypeExpandPoint.class, define, new Object[] { beanBytes, define, this.applicationContext });
        //3.
        if (beanType == null)
            beanType = this.classLoader.loadClass(beanBytes, define);
        if (beanType == null)
            throw new ClassNotFoundException(defineLogStr + "����Bean�����޷���װ�ػ�װ��ʧ�ܡ�");
        return beanType;
    };
    /**
     * �������洴������ȥ����bean���롣
     * @param define Ҫװ�����͵�bean���塣
     * @param params ����beanʱ���ݵĲ�����
     * @return ���ش�����bean����
     */
    public synchronized Object builderBean(AbstractBeanDefine define, Object[] params) throws Throwable {
        String defineID = define.getID();
        //--------------------------------------------------------------------------------------------------------------��鵥̬
        //0.��̬
        if (this.singleBeanCache.containsKey(defineID) == true)
            return this.singleBeanCache.get(defineID);
        //--------------------------------------------------------------------------------------------------------------
        AbstractBeanBuilder<AbstractBeanDefine> builder = this.beanBuilderMap.get(define.getBeanType());
        Class<?> beanType = this.builderType(define);
        //1.����
        Object obj = this.epManager.exePoint(BeforeCreateExpandPoint.class, define, new Object[] { beanType, params, define, this.applicationContext }); //Ԥ����Bean
        if (obj == null)
            if (builder.ifDefaultBeanCreateMode() == false)
                //------------------------������Ĭ�ϲ��ԣ�
                obj = builder.createBean(define, params);
            else {
                //------------------------��ʹ��Ĭ�ϲ��ԣ�
                // create begin
                AbstractMethodDefine factory = define.factoryMethod();
                Collection<? extends AbstractPropertyDefine> initParam = null;
                if (factory != null) {
                    //1.������ʽ
                    initParam = factory.getParams();
                    Class<?>[] types = transform_toType(initParam, params);
                    Object[] objects = transform_toObject(initParam, params);
                    String factoryBeanID = factory.getForBeanDefine().getID();
                    if (factory.isStatic() == true) {
                        Class<?> factoryType = this.applicationContext.getBeanType(factoryBeanID);
                        Method factoryMethod = factoryType.getMethod(factory.getCodeName(), types);
                        obj = factoryMethod.invoke(null, objects);
                    } else {
                        Object factoryObject = this.applicationContext.getBean(factoryBeanID, params);/*params�����ᱻ˳�ƴ��빤��bean�С�*/
                        Method factoryMethod = factoryObject.getClass().getMethod(factory.getCodeName(), types);
                        obj = factoryMethod.invoke(factoryObject, objects);
                    }
                } else {
                    //2.���췽��
                    initParam = define.getInitParams();
                    Class<?>[] types = transform_toType(initParam, params);
                    Object[] objects = transform_toObject(initParam, params);
                    //
                    Constructor<?> constructor = beanType.getConstructor(types);
                    obj = constructor.newInstance(objects);
                }
                // create end//
            }
        //2.����ע��
        OgnlContext ognl = new OgnlContext();
        ognl.put("this", obj);
        for (AbstractPropertyDefine propDefine : define.getPropertys()) {
            //TODO ELע��
        }
        //--------------------------------------------------------------------------------------------------------------��ʼ���׶�
        //3.��̬����&����ƥ��
        obj = this.cast(beanType, obj);
        if (define.isSingleton() == true)
            this.singleBeanCache.put(defineID, obj);
        //4.�������ٷ���
        {
            //TODO  ProxyFinalizeClassEngine ce = new ProxyFinalizeClassEngine(this);
            //TODO  ce.setBuilderMode(BuilderMode.Propxy);
            //TODO  ce.setSuperClass(objType);
            //TODO  obj = ce.newInstance(obj);
        }
        //5.ִ����չ��
        obj = this.epManager.exePoint(AfterCreateExpandPoint.class, define, new Object[] { obj, params, define, this.applicationContext });//Beanװ��
        //6.ִ����������init����
        {
            Class<?> objType = obj.getClass();
            String initMethodName = define.getInitMethod();
            if (initMethodName != null) {
                Method m = objType.getMethod(initMethodName, Object[].class);
                m.invoke(obj, params);
            }
        }
        return obj;
    };
    /**�����������Ƿ�ƥ�䶨�����ͣ����û��ָ��beanType������ֱ�ӷ��ء�*/
    private Object cast(Class<?> beanType, Object obj) throws ClassCastException {
        if (beanType != null)
            return beanType.cast(obj);
        return obj;
    };
    /*��һ������ת�������͡�*/
    private Class<?>[] transform_toType(Collection<? extends AbstractPropertyDefine> pds, Object[] params) {
        //this.rootMetaDataParser.parser(data, rootParser, context);
        return null;//TODO
    };
    /*��һ������ת���ɶ���*/
    private Object[] transform_toObject(Collection<? extends AbstractPropertyDefine> pds, Object[] params) {
        return null;//TODO
    };
    private Collection<? extends ValueMetaData> transform(Collection<? extends AbstractPropertyDefine> pds) {
        ArrayList<ValueMetaData> vms = new ArrayList<ValueMetaData>(pds.size());
        for (AbstractPropertyDefine pd : pds)
            vms.add(pd.getMetaData());
        return vms;
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