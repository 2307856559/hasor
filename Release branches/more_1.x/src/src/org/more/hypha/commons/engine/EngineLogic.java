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
package org.more.hypha.commons.engine;
import java.util.HashMap;
import java.util.Map;
import org.more.ClassFormatException;
import org.more.DoesSupportException;
import org.more.RepeateException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ValueMetaData;
import org.more.hypha.commons.engine.ioc.IocEngine;
import org.more.hypha.context.AbstractApplicationContext;
/**
 * ���ฺ��hypha������bean�������̣���һ�������ࡣ
 * @version : 2011-5-12
 * @author ������ (zyc@byshell.org)
 */
public class EngineLogic {
    private Map<String, AbstractBeanBuilder<AbstractBeanDefine>> builderMap         = null;
    private RootValueMetaDataParser                              rootParser         = null;
    private AbstractApplicationContext                           applicationContext = null;
    //
    private Map<String, IocEngine>                               engineMap          = null;
    private EngineClassLoader                                    classLoader        = null;
    //----------------------------------------------------------------------------------------------------------
    private class EngineClassLoader extends ClassLoader {
        public EngineClassLoader(AbstractApplicationContext applicationContext) {
            super(applicationContext.getBeanClassLoader());
        };
        public Class<?> loadClass(byte[] beanBytes, AbstractBeanDefine define) throws ClassFormatException {
            //���������Ҫװ�ص�����JVM�Ͳ�����ñ��ص�������ȥ���������Ƿ���ڡ�
            return this.defineClass(beanBytes, 0, beanBytes.length);
        };
    };
    /**��ʼ��*/
    public void init(AbstractApplicationContext applicationContext) throws Throwable {
        this.applicationContext = applicationContext;
        this.classLoader = new EngineClassLoader(this.applicationContext);//ʹ��EngineClassLoaderװ�ع�����ֽ���
        this.builderMap = new HashMap<String, AbstractBeanBuilder<AbstractBeanDefine>>();
        this.rootParser = new RootValueMetaDataParser() {};
    }
    /**��ȡ{@link AbstractApplicationContext}��������Bean����������*/
    protected IocEngine getEngine(String key) throws Throwable {
        return this.engineMap.get(key);
    };
    /**���һ��beanע�����棬ע���ظ�ע�Ὣ�ᵼ���滻��*/
    public void addIocEngine(String key, IocEngine engine) throws Throwable {
        engine.init(this.applicationContext, this.rootParser);
        this.engineMap.put(key, engine);
    };
    /*
     * 
     * 
     */
    //----------------------------------------------------------------------------------------------------------
    /**ע��{@link ValueMetaDataParser}�����ע��Ľ����������ظ��������{@link RepeateException}�쳣��*/
    public void regeditValueMetaDataParser(String metaDataType, ValueMetaDataParser<ValueMetaData> parser) {
        this.rootParser.addParser(metaDataType, parser);
    };
    /**���ע��{@link ValueMetaDataParser}�����Ҫ�Ƴ��Ľ��������������Ҳ�����׳��쳣��*/
    public void unRegeditValueMetaDataParser(String metaDataType) {
        this.rootParser.removeParser(metaDataType);
    };
    /**
     * ע��һ��bean�������ͣ�ʹ֮���Ա��������������ظ�ע��ͬһ��bean���ͽ�������{@link RepeateException}�����쳣��
     * @param beanType ע���bean�������͡�
     * @param builder Ҫע���bean������������
     */
    public void regeditBeanBuilder(String beanType, AbstractBeanBuilder<AbstractBeanDefine> builder) throws RepeateException {
        if (this.builderMap.containsKey(beanType) == false)
            this.builderMap.put(beanType, builder);
        else
            throw new RepeateException("�����ظ�ע��[" + beanType + "]���͵�BeanBuilder��");
    };
    /**���ָ������bean�Ľ���֧�֣�����Ҫ�Ӵ�ע���bean�����Ƿ���ڸ÷������ᱻ��ȷִ�С�*/
    public void unRegeditBeanBuilder(String beanType) {
        if (this.builderMap.containsKey(beanType) == true)
            this.builderMap.remove(beanType);
    };
    /*
     * 
     * 
     */
    //----------------------------------------------------------------------------------------------------------
    /**
     * ��{@link AbstractBeanDefine}��������������װ�س�ΪClass���Ͷ����ڼ����������{@link ClassBytePoint}��{@link ClassTypePoint}������չ�㡣
     * �������bean����{@link AbstractBeanBuilder}������ֻ��ִ��{@link ClassTypePoint}��չ�㡣
     * �������bean����{@link AbstractBeanBuilderEx}������ֻ��ִ��������չ�㡣
     * @param define Ҫװ�����͵�bean���塣
     * @param params getBeanʱ����Ĳ�����
     */
    public Class<?> builderType(AbstractBeanDefine define, Object[] params) {
        String defineType = define.getBeanType();
        String defineID = define.getID();
        String defineLogStr = defineID + "[" + defineType + "]";//logǰ׺
        //1.
        AbstractBeanBuilder<AbstractBeanDefine> builder = this.builderMap.get(defineType);
        if (builder == null)
            throw new DoesSupportException(defineLogStr + "����Bean����һ��hypha��֧�ֵ�Bean���ͻ��߸����͵�Bean��֧��Builder��");
        if (builder instanceof AbstractBeanBuilderEx == true)
            return this.doBuilderExForType((AbstractBeanBuilderEx<AbstractBeanDefine>) builder, define, params);
        else
            return this.doBuilderForType(builder, define, params);
    }
    /**ִ��{@link AbstractBeanBuilder}���������̡�*/
    private Class<?> doBuilderForType(AbstractBeanBuilder<AbstractBeanDefine> builder, AbstractBeanDefine define, Object[] params) {
        Class<?> beanType = builder.loadType(define, params);
        //ִ��ClassTypePoint��չ�㡣
        beanType = (Class<?>) this.applicationContext.getExpandPointManager().exePointOnSequence(ClassTypePoint.class,//
                new Object[] { beanType, define, this.applicationContext });//Param
        return beanType;
    };
    /**ִ��{@link AbstractBeanBuilderEx}���������̡�*/
    private Class<?> doBuilderExForType(AbstractBeanBuilderEx<AbstractBeanDefine> builderEx, AbstractBeanDefine define, Object[] params) {
        byte[] beanBytes = builderEx.loadBytes(define, params);
        //ִ��ClassBytePoint��չ��
        beanBytes = (byte[]) this.applicationContext.getExpandPointManager().exePointOnSequence(ClassBytePoint.class, //
                new Object[] { beanBytes, define, this.applicationContext });//Param
        Class<?> beanType = null;
        //ִ��ClassTypePoint��չ�㡣
        beanType = (Class<?>) this.applicationContext.getExpandPointManager().exePointOnSequence(ClassTypePoint.class,//
                new Object[] { beanType, define, this.applicationContext });//Param
        //
        if (beanType != null)
            return beanType;
        return this.classLoader.loadClass(beanBytes, define);
    };
    //----------------------------------------------------------------------------------------------------------
    /*
     * 
     * 
     */
    public <T> T builderBean(AbstractBeanDefine define, Object[] params) {
        return null;
    };
}
//class ProxyFinalizeClassEngine extends ClassEngine {
//private final ProxyFinalizeClassBuilder builder = new ProxyFinalizeClassBuilder();
//public ProxyFinalizeClassEngine(BeanEngine beanEngine) throws ClassNotFoundException {
//  super(false);
//};
//protected ClassBuilder createBuilder(BuilderMode builderMode) {
//  return this.builder;
//};
//public Object newInstance(Object propxyBean) throws FormatException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
//  Object obj = super.newInstance(propxyBean);
//  //this.toClass().getMethod("", parameterTypes);
//  return obj;
//};
//};
//class ProxyFinalizeClassBuilder extends ClassBuilder {
//protected ClassAdapter acceptClass(ClassWriter classVisitor) {
//  return new ClassAdapter(classVisitor) {
//      public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//          if (name.equals("finalize()V") == true)
//              System.out.println();
//          return super.visitMethod(access, name, desc, signature, exceptions);
//      }
//  };
//}
//};