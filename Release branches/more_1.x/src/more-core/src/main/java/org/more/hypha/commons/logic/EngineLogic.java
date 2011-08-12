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
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
import org.more.hypha.PointCallBack;
import org.more.hypha.ValueMetaData;
import org.more.hypha.context.AbstractApplicationContext;
/**
* ���ฺ��hypha������bean�������̣���һ���ǳ���Ҫ�ĺ����ࡣ
* @version : 2011-5-12
* @author ������ (zyc@byshell.org)
*/
public class EngineLogic {
    private static Log                                           log                = LogFactory.getLog(EngineLogic.class);
    private Map<String, AbstractBeanBuilder<AbstractBeanDefine>> builderMap         = null;
    private RootValueMetaDataParser                              rootParser         = null;
    private AbstractApplicationContext                           applicationContext = null;
    //
    private Map<String, IocEngine>                               engineMap          = null;
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
    };
    /**��ȡ{@link AbstractApplicationContext}��������Bean����������*/
    protected IocEngine getEngine(String key) {
        if (key != null) {
            IocEngine ioc = this.engineMap.get(key);
            log.debug("getEngine key = {%0} , return Engine {%1}.", key, ioc);
            return ioc;
        }
        log.warning("can`t getEngine key is null.");
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
            log.debug("add IocEngine {%0} OK!", key);
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
    };
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
            log.debug("regedit bean Builder {%0} OK!", beanType);
        builder.setApplicationContext(this.applicationContext);
        this.builderMap.put(beanType, builder);
    };
    /**���ָ������bean�Ľ���֧�֣�����Ҫ�Ӵ�ע���bean�����Ƿ���ڸ÷������ᱻ��ȷִ�С�*/
    public void unRegeditBeanBuilder(String beanType) {
        if (this.builderMap.containsKey(beanType) == true) {
            log.debug("unRegedit bean Builder {%0} OK!", beanType);
            this.builderMap.remove(beanType);
        }
    };
    /*------------------------------------------------------------------------------*/
    /**
     * ִ��bean��ص�{@link AbstractBeanBuilder}����ִ������class�Ķ�����
     * @param define Ҫװ�����͵�bean���塣
     * @param params getBeanʱ����Ĳ�����
     */
    public Class<?> loadType(final AbstractBeanDefine define, final Object[] params) throws Throwable {
        if (define == null) {
            log.error("builderType an error param AbstractBeanDefine is null.");
            throw new NullPointerException("builderType an error param AbstractBeanDefine is null.");
        }
        String defineType = define.getBeanType();
        String defineID = define.getID();
        log.debug("builder bean Type defineID is {%0} ...", defineID);
        final AbstractBeanBuilder<AbstractBeanDefine> builder = this.builderMap.get(defineType);
        /*����ʹ����չ��ķ�ʽ����ǿװ������ */
        return this.applicationContext.getExpandPointManager().exePoint(//ִ��LoadClassPoint��չ��
                LoadClassPoint.class, new PointCallBack() {
                    public Object call(ApplicationContext applicationContext, Object[] params) throws Throwable {
                        return builder.loadType(define, params);
                    }
                }, define, params);
    };
    /*------------------------------------------------------------------------------*/
    public <T> T loadBean(final AbstractBeanDefine define, final Object[] params) throws Throwable {
        if (define == null) {
            log.error("loadBean an error param AbstractBeanDefine is null.");
            throw new NullPointerException("loadBean an error param AbstractBeanDefine is null.");
        }
        if (define.isAbstract() == true) {
            log.error("loadBean an error define is Abstract.");
            throw new SupportException("loadBean an error define is Abstract.");
        }
        final String defineType = define.getBeanType();
        final String defineID = define.getID();
        log.debug("loadBean Object defineID is {%0} ...", defineID);
        //1.
        final AbstractBeanBuilder<AbstractBeanDefine> builder = this.builderMap.get(defineType);
        if (builder == null) {
            log.error("loadBean {%0} Type {%1} is doesn`t support!", defineID, defineType);
            throw new SupportException("loadBean " + defineID + " Type " + defineType + " is doesn`t support!");
        }
        //2.����bean
        log.debug("beforeCreate defineID = {%0}.", defineID);
        Object obj = this.applicationContext.getExpandPointManager().exePoint(//ִ��CreateBeanPoint��չ��
                CreateBeanPoint.class, new PointCallBack() {
                    public Object call(ApplicationContext applicationContext, Object[] params) throws Throwable {
                        return builder.loadBean(define, params);
                    }
                }, define, params);
        log.debug("AfterCreate defineID = {%0}.", defineID);
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
        iocEngine.ioc(obj, define, params);
        //--------------------------------------------------------------------------------------------------------------��ʼ���׶�
        //4.�������ٷ���
        if (define.getDestroyMethod() != null)
            obj = this.propxyFinalize(define, obj);
        //5.ִ����չ��
        final Object _temp = obj;//������չ���п��Է��ʵ��������
        obj = this.applicationContext.getExpandPointManager().exePoint(//ִ��CreatedBeanPoint��չ��
                CreatedBeanPoint.class, new PointCallBack() {
                    public Object call(ApplicationContext applicationContext, Object[] params) throws Throwable {
                        return _temp;
                    }
                }, obj, define, params);
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
    /*�������ٷ���*/
    private RootClassLoader propxyRootLoader = null;
    private Object propxyFinalize(AbstractBeanDefine define, Object object) throws ClassNotFoundException, IOException {
        //ʹ��ͳһ��ClassLoader����װ�����ɵ� �������ٷ����ࡣ
        if (this.propxyRootLoader == null)
            this.propxyRootLoader = new RootClassLoader(this.applicationContext.getClassLoader());
        //
        AbstractBeanDefine attDefine = null;
        ProxyFinalizeClassEngine proxyEngine = null;//�������ٷ�����������
        //1.ȷ��ProxyFinalizeClassEngine���Ͷ���
        if (define instanceof AbstractBeanDefine == true)
            attDefine = (AbstractBeanDefine) define;
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
};
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
    };
};
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
    };
    protected ClassBuilder createBuilder(BuilderMode builderMode) {
        return this.builder;
    };
};
/**����finalize��������Ҫ�߼����滻���е�finalize���������һ���µ�finalize��������ִ�е��á�*/
class ProxyFinalizeClassBuilder extends ClassBuilder {
    private String finalizeName = null;
    public ProxyFinalizeClassBuilder(String finalizeName) {
        this.finalizeName = finalizeName;
    };
    protected void init(ClassEngine classEngine) {};
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
    };
};