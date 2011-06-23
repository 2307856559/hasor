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
package org.more.hypha.commons.logic;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassWriter;
import org.more.core.asm.MethodVisitor;
import org.more.core.asm.Opcodes;
import org.more.core.classcode.BuilderMode;
import org.more.core.classcode.ClassBuilder;
import org.more.core.classcode.ClassEngine;
import org.more.core.classcode.ClassNameStrategy;
import org.more.core.classcode.RootClassLoader;
import org.more.core.error.LostException;
import org.more.core.error.RepeateException;
import org.more.core.error.SupportException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.AbstractMethodDefine;
import org.more.hypha.AbstractPropertyDefine;
import org.more.hypha.ValueMetaData;
import org.more.hypha.beans.define.AbstractBaseBeanDefine;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.log.ILog;
import org.more.log.LogFactory;
import org.more.util.PropxyObject;
/**
* ���ฺ��hypha������bean�������̣���һ���ǳ���Ҫ�ĺ����ࡣ
* @version : 2011-5-12
* @author ������ (zyc@byshell.org)
*/
public class EngineLogic {
    private static ILog                                          log                = LogFactory.getLog(EngineLogic.class);
    private Map<String, AbstractBeanBuilder<AbstractBeanDefine>> builderMap         = null;
    private RootValueMetaDataParser                              rootParser         = null;
    private AbstractApplicationContext                           applicationContext = null;
    //
    private Map<String, IocEngine>                               engineMap          = null;
    private EngineClassLoader                                    classLoader        = null;
    /*------------------------------------------------------------------------------*/
    private class EngineClassLoader extends ClassLoader {
        public EngineClassLoader(AbstractApplicationContext applicationContext) {
            super(applicationContext.getBeanClassLoader());
        };
        public Class<?> loadClass(ClassData classData, AbstractBeanDefine define) {
            if (classData == null)
                throw new NullPointerException("loadClass error ClassData is null.");
            if (classData.bytes == null || classData.className == null)
                throw new NullPointerException("loadClass error beanBytes or className is null.");
            //���������Ҫװ�ص�����JVM�Ͳ�����ñ��ص�������ȥ���������Ƿ���ڡ�
            byte[] b = classData.bytes;
            return this.defineClass(classData.className, b, 0, b.length);
        }
    };
    /*------------------------------------------------------------------------------*/
    /**��ʼ������������Ϊ�ա�*/
    public void init(AbstractApplicationContext applicationContext) {
        if (applicationContext != null)
            log.info("init EngineLogic, applicationContext = {%0}", applicationContext);
        else {
            log.error("init EngineLogic, applicationContext is null.");
            throw new NullPointerException("applicationContext param is null.");
        }
        //
        this.applicationContext = applicationContext;
        this.builderMap = new HashMap<String, AbstractBeanBuilder<AbstractBeanDefine>>();
        this.engineMap = new HashMap<String, IocEngine>();
        //
        log.debug("init EngineLogic, applicationContext = {%0}", applicationContext);
        this.rootParser = new RootValueMetaDataParser() {};
        this.classLoader = new EngineClassLoader(this.applicationContext);//ʹ��EngineClassLoaderװ�ع�����ֽ���
    }
    /**��ȡ{@link AbstractApplicationContext}��������Bean����������*/
    protected IocEngine getEngine(String key) {
        if (key != null) {
            IocEngine ioc = this.engineMap.get(key);
            log.debug("getEngine key = {%0} , return Engine {%1}.", key, ioc);
            return ioc;
        }
        log.info("can`t getEngine key is null.");
        return null;
    };
    /**���һ��beanע�����棬ע���ظ�ע�Ὣ�ᵼ���滻��*/
    public void addIocEngine(String key, IocEngine engine) {
        if (key == null || engine == null) {
            log.warning("add IocEngine an error , key or IocEngine is null.");
            return;
        }
        engine.init(this.applicationContext, this.rootParser);
        if (this.engineMap.containsKey(key) == true)
            log.info("add IocEngine {%0} is exist,use new engine Repeate it OK!", key);
        else
            log.info("add IocEngine {%0} OK!", key);
        this.engineMap.put(key, engine);
    };
    /**ע��{@link ValueMetaDataParser}�����ע��Ľ����������ظ��������{@link RepeateException}�쳣��*/
    public void regeditValueMetaDataParser(String metaDataType, ValueMetaDataParser<ValueMetaData> parser) {
        log.debug("regedit ValueMetaDataParser, metaDataType = {%0}, ValueMetaDataParser = {%1}", metaDataType, parser);
        this.rootParser.addParser(metaDataType, parser);
    };
    /**���ע��{@link ValueMetaDataParser}�����Ҫ�Ƴ��Ľ��������������Ҳ�����׳��쳣��*/
    public void unRegeditValueMetaDataParser(String metaDataType) {
        log.debug("unRegedit ValueMetaDataParser, metaDataType = {%0}", metaDataType);
        this.rootParser.removeParser(metaDataType);
    };
    /**��ȡԪ��Ϣ����������*/
    public ValueMetaDataParser<ValueMetaData> getRootParser() {
        return this.rootParser;
    }
    /**
     * ע��һ��bean�������ͣ�ʹ֮���Ա�����������ظ�ע��ᵼ�¸��ǡ�
     * @param beanType ע���bean�������͡�
     * @param builder Ҫע���bean������������
     */
    public void regeditBeanBuilder(String beanType, AbstractBeanBuilder<AbstractBeanDefine> builder) {
        if (beanType == null || builder == null) {
            log.warning("regedit bean Builder an error , beanType or AbstractBeanBuilder is null.");
            return;
        }
        if (this.engineMap.containsKey(beanType) == true)
            log.info("regedit bean Builder {%0} is exist,use new engine Repeate it OK!", beanType);
        else
            log.info("regedit bean Builder {%0} OK!", beanType);
        builder.setApplicationContext(this.applicationContext);
        this.builderMap.put(beanType, builder);
    };
    /**���ָ������bean�Ľ���֧�֣�����Ҫ�Ӵ�ע���bean�����Ƿ���ڸ÷������ᱻ��ȷִ�С�*/
    public void unRegeditBeanBuilder(String beanType) {
        if (this.builderMap.containsKey(beanType) == true) {
            log.info("unRegedit bean Builder {%0} OK!", beanType);
            this.builderMap.remove(beanType);
        }
    };
    /*------------------------------------------------------------------------------*/
    /**
     * ��{@link AbstractBaseBeanDefine}��������������װ�س�ΪClass���Ͷ����ڼ����������{@link ClassBytePoint}��{@link ClassTypePoint}������չ�㡣
     * �������bean����{@link AbstractBeanBuilder}������ֻ��ִ��{@link ClassTypePoint}��չ�㡣
     * �������bean����{@link AbstractBeanBuilderEx}������ֻ��ִ��������չ�㡣
     * @param define Ҫװ�����͵�bean���塣
     * @param params getBeanʱ����Ĳ�����
     */
    public Class<?> builderType(AbstractBeanDefine define, Object[] params) throws Throwable {
        if (define == null) {
            log.error("builderType an error param AbstractBeanDefine is null.");
            throw new NullPointerException("builderType an error param AbstractBeanDefine is null.");
        }
        String defineType = define.getBeanType();
        String defineID = define.getID();
        String defineLogStr = defineID + "[" + defineType + "]";//logǰ׺
        log.debug("builder bean Type defineID is {%0} ...", defineID);
        //1.
        AbstractBeanBuilder<AbstractBeanDefine> builder = this.builderMap.get(defineType);
        if (builder == null) {
            log.error("bean {%0} Type {%1} is doesn`t support!", defineID, defineType);
            throw new SupportException(defineLogStr + "����Bean����һ��hypha��֧�ֵ�Bean���ͻ��߸����͵�Bean��֧��Builder��");
        }
        //2.
        if (builder instanceof AbstractBeanBuilderEx == true)
            return this.doBuilderExForType((AbstractBeanBuilderEx<AbstractBeanDefine>) builder, define, params);
        else
            return this.doBuilderForType(builder, define, params);
    }
    /**ִ��{@link AbstractBeanBuilder}���������̡�*/
    private Class<?> doBuilderForType(AbstractBeanBuilder<AbstractBeanDefine> builder, AbstractBeanDefine define, Object[] params) throws Throwable {
        String defineID = define.getID();
        log.info("defineID {%0} loadType By Builder...", defineID);
        Class<?> beanType = builder.loadType(define, params);
        //ִ��ClassTypePoint��չ�㡣
        beanType = (Class<?>) this.applicationContext.getExpandPointManager().exePointOnSequence(ClassTypePoint.class, beanType,//
                define, this.applicationContext);//Param
        log.debug("defineID {%0} loadType OK! type = {%1}", defineID, beanType);
        return beanType;
    };
    /**ִ��{@link AbstractBeanBuilderEx}���������̡�*/
    private Class<?> doBuilderExForType(AbstractBeanBuilderEx<AbstractBeanDefine> builderEx, AbstractBeanDefine define, Object[] params) throws Throwable {
        String defineID = define.getID();
        log.info("defineID {%0} loadBytes By BuilderEx...", defineID);
        ClassData classData = builderEx.loadBytes(define, params);
        //ִ��ClassBytePoint��չ��
        classData = (ClassData) this.applicationContext.getExpandPointManager().exePointOnSequence(ClassBytePoint.class, classData, //
                define, this.applicationContext);//Param
        log.debug("defineID {%0} loadBytes OK! beanBytes = {%1}", defineID, classData);
        Class<?> beanType = builderEx.loadType(classData, define, params);//װ�����͡�
        //ִ��ClassTypePoint��չ�㡣
        beanType = (Class<?>) this.applicationContext.getExpandPointManager().exePointOnSequence(ClassTypePoint.class, beanType,//
                define, this.applicationContext);//Param
        if (beanType == null) {
            log.debug("loading {%0} bytes transform to Type!", defineID);
            beanType = this.classLoader.loadClass(classData, define);
        }
        log.debug("defineID {%0} loadType OK! type = {%1}", defineID, beanType);
        return beanType;
    };
    /*------------------------------------------------------------------------------*/
    public <T> T builderBean(AbstractBeanDefine define, Object[] params) throws Throwable {
        if (define == null) {
            log.error("builderBean an error param AbstractBeanDefine is null.");
            throw new NullPointerException("builderBean an error param AbstractBeanDefine is null.");
        }
        if (define.isAbstract() == true) {
            log.error("builderBean an error define is Abstract.");
            throw new SupportException("builderBean an error define is Abstract.");
        }
        String defineType = define.getBeanType();
        String defineID = define.getID();
        log.debug("builder bean Object defineID is {%0} ...", defineID);
        //1.
        AbstractBeanBuilder<AbstractBeanDefine> builder = this.builderMap.get(defineType);
        if (builder == null) {
            log.error("bean {%0} Type {%1} is doesn`t support!", defineID, defineType);
            throw new SupportException("bean " + defineID + " Type " + defineType + " is doesn`t support!");
        }
        //2.����bean
        Object obj = this.applicationContext.getExpandPointManager().exePointOnReturn(BeforeCreatePoint.class, null,//Ԥ����Bean
                define, params, this.applicationContext);
        log.debug("beforeCreate defineID = {%0}, return = {%1}.", defineID, obj);
        if (obj == null) {
            AbstractMethodDefine factory = define.factoryMethod();
            if (factory != null) {
                String factoryBeanID = define.factoryBean().getID();
                log.debug("use factoryBean create {%0}, factoryBeanID = {%1}...", defineID, factoryBeanID);
                Collection<? extends AbstractPropertyDefine> initParamDefine = factory.getParams();
                Object[] initParam_objects = transform_toObjects(null, initParamDefine, params);//null��ʱ��û�н�������
                if (factory.isStatic() == true) {
                    //��̬������������
                    log.debug("create by static ,function is static....");
                    Class<?> factoryType = this.applicationContext.getBeanType(factoryBeanID);
                    PropxyObject op = this.findMethodByC(factoryType, initParam_objects);
                    obj = op.invokeMethod(factory.getCodeName());
                    log.debug("create by static {%0}, defineID = {%1}, return = {%2}.", factory.getCodeName(), defineID, obj);
                } else {
                    //������������
                    log.debug("create by factory ....");
                    Object factoryObject = this.applicationContext.getBean(factoryBeanID, params);/*params�����ᱻ˳�ƴ��빤��bean�С�*/
                    PropxyObject op = this.findMethodByO(factoryObject, initParam_objects);
                    obj = op.invokeMethod(factory.getCodeName());
                    log.debug("create by factory {%0}, defineID = {%1}, return = {%2}.", factory.getCodeName(), defineID, obj);
                }
            } else {
                //ʹ��builder������
                log.debug("use builder create {%0}...", defineID);
                Class<?> classType = this.applicationContext.getBeanType(define.getID(), params);
                obj = builder.createBean(classType, define, params);
                log.debug("use builder create {%0}, return .", defineID, obj);
            }
        }
        //3.����ע��
        String iocEngineName = define.getIocEngine();
        if (iocEngineName == null) {
            log.error("{%0} ioc engine name is null.", defineID);
            throw new SupportException(defineID + " ioc engine name is null.");
        }
        IocEngine iocEngine = this.getEngine(iocEngineName);
        if (iocEngine == null) {
            log.error("ioc Engine lost name is {%0}.", iocEngineName);
            throw new LostException("ioc Engine lost name is " + iocEngineName + ".");
        }
        log.debug("ioc defineID = {%0} ,engine name is {%1}.", defineID, iocEngineName);
        this.ioc(iocEngine, obj, define, params);
        //--------------------------------------------------------------------------------------------------------------��ʼ���׶�
        //4.�������ٷ���
        if (define.getDestroyMethod() != null)
            obj = this.propxyFinalize(define, obj);
        //5.ִ����չ��
        obj = this.applicationContext.getExpandPointManager().exePointOnSequence(AfterCreatePoint.class, obj,//Beanװ��
                params, define, this.applicationContext);
        //6.ִ����������init����
        String initMethodName = define.getInitMethod();
        if (initMethodName != null)
            try {
                Method m = obj.getClass().getMethod(initMethodName);
                log.info("{%0} invoke init Method...", defineID, m);
                m.invoke(obj);
            } catch (Exception e) {
                log.error("{%0} invoke init Method an error, {%1} method not found.", defineID, initMethodName);
                throw e;
            }
        return (T) obj;
    };
    private void ioc(IocEngine iocEngine, Object obj, AbstractBeanDefine define, Object[] params) throws Throwable {
        AbstractBaseBeanDefine template = define.getUseTemplate();
        if (template != null)
            this.ioc(iocEngine, obj, template, params);
        iocEngine.ioc(obj, define, params);
    }
    private PropxyObject findMethodByC(Class<?> parentClass, Object[] params) {
        PropxyObject po = new PropxyObject(parentClass);
        for (Object o : params)
            po.put(o);
        return po;
    }
    private PropxyObject findMethodByO(Object parent, Object[] params) {
        PropxyObject po = new PropxyObject(parent);
        for (Object o : params)
            po.put(o);
        return po;
    }
    /*��һ������ת���ɶ���*/
    private Object[] transform_toObjects(Object object, Collection<? extends AbstractPropertyDefine> pds, Object[] params) throws Throwable {
        if (pds == null)
            return new Object[0];
        //
        int size = pds.size();
        int index = 0;
        Object[] res = new Object[size];
        for (AbstractPropertyDefine apd : pds) {
            res[index] = this.rootParser.parser(object, apd.getMetaData(), null/*�ò�����Ч*/, this.applicationContext);
            index++;
        }
        return res;
    };
    /*�������ٷ���*/
    private RootClassLoader propxyRootLoader = null;
    private Object propxyFinalize(AbstractBeanDefine define, Object object) throws ClassNotFoundException, IOException {
        //ʹ��ͳһ��ClassLoader����װ�����ɵ� �������ٷ����ࡣ
        if (this.propxyRootLoader == null)
            this.propxyRootLoader = new RootClassLoader(this.applicationContext.getBeanClassLoader());
        //
        AbstractBaseBeanDefine attDefine = null;
        ProxyFinalizeClassEngine proxyEngine = null;//�������ٷ�����������
        //1.ȷ��ProxyFinalizeClassEngine���Ͷ���
        if (define instanceof AbstractBaseBeanDefine == true)
            attDefine = (AbstractBaseBeanDefine) define;
        if (attDefine != null) //ȡ�ÿ��ܻ�������ٷ�����������
            proxyEngine = (ProxyFinalizeClassEngine) attDefine.getAttribute("ProxyFinalizeClassEngine");
        if (proxyEngine == null)
            proxyEngine = new ProxyFinalizeClassEngine(define, object, this.propxyRootLoader);
        if (attDefine != null) //���»�������ٷ�����������
            attDefine.setAttribute("ProxyFinalizeClassEngine", proxyEngine);
        //        FileOutputStream fos = new FileOutputStream(proxyEngine.getSimpleName() + ".class");
        //        fos.write(proxyEngine.builderClass().toBytes());
        //        fos.flush();
        //        fos.close();
        //2.���ɴ������
        return proxyEngine.newInstance(object);
    };
}
/**����������*/
class ProxyClassNameStrategy implements ClassNameStrategy {
    private static long         generateID  = 0;
    private static final String ClassPrefix = "_PF$"; //�������������׺��
    public void initStrategy(ClassEngine classEngine) {};
    public void reset() {};
    public synchronized String generateName(Class<?> superClass) {
        String cn = superClass.getName();
        generateID++;
        return cn + ClassPrefix + generateID;
    }
}
/**�����������*/
class ProxyFinalizeClassEngine extends ClassEngine {
    private ProxyFinalizeClassBuilder builder      = null;
    private String                    finalizeName = null;
    public ProxyFinalizeClassEngine(AbstractBeanDefine define, Object obj, RootClassLoader classLoader) throws ClassNotFoundException {
        super(false);
        this.setRootClassLoader(classLoader);
        this.setBuilderMode(BuilderMode.Propxy);
        this.setClassNameStrategy(new ProxyClassNameStrategy());
        this.setSuperClass(obj.getClass());
        this.generateName();
        this.finalizeName = define.getDestroyMethod();
        this.builder = new ProxyFinalizeClassBuilder(this.finalizeName);
    }
    protected ClassBuilder createBuilder(BuilderMode builderMode) {
        return this.builder;
    };
};
/**����finalize��������Ҫ�߼����滻���е�finalize���������һ���µ�finalize��������ִ�е��á�*/
class ProxyFinalizeClassBuilder extends ClassBuilder {
    private String finalizeName = null;
    public ProxyFinalizeClassBuilder(String finalizeName) {
        this.finalizeName = finalizeName;
    }
    protected void init(ClassEngine classEngine) {}
    protected ClassAdapter acceptClass(ClassWriter classVisitor) {
        final String _finalizeName = this.finalizeName;//���ٷ�����
        return new ClassAdapter(classVisitor) {
            private boolean _isFindFinalize  = false; //���������Ƿ����finalize������
            private String  _propxyClassName = null; //����������
            /*�������*/
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                this._propxyClassName = name;
                super.visit(version, access, name, signature, superName, interfaces);
            }
            /*ȷ���Ƿ��Ѿ������finalize()V*/
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                String fullname = name + desc;
                if (fullname.equals("finalize()V") == true) {
                    _isFindFinalize = true;//�ҵ����е�finalize������
                    return super.visitMethod(access, "$_" + name, desc, signature, exceptions);
                } else
                    return super.visitMethod(access, name, desc, signature, exceptions);
            }
            /*���finalize()V*/
            public void visitEnd() {
                //���finalize������
                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC, "finalize", "()V", null, null);
                mv.visitCode();
                //
                mv.visitVarInsn(Opcodes.ALOAD, 0);//this.$propxyObject.destroyMethod()
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _propxyClassName, _finalizeName, "()V");
                //
                if (_isFindFinalize == true) {
                    mv.visitVarInsn(Opcodes.ALOAD, 0);//this.$_finalize()
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _propxyClassName, "$_finalize", "()V");
                }
                //
                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
                super.visitEnd();
            }
        };
    }
};