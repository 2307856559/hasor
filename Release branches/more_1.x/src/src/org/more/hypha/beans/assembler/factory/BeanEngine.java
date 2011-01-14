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
import java.util.Map;
import org.more.DoesSupportException;
import org.more.InitializationException;
import org.more.InvokeException;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ExpandPointManager;
import org.more.hypha.a.AfterCreateExpandPoint;
import org.more.hypha.a.BeforeCreateExpandPoint;
import org.more.hypha.a.ClassByteExpandPoint;
import org.more.hypha.a.ClassTypeExpandPoint;
import org.more.hypha.a.DecoratorExpandPoint;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.AbstractMethodDefine;
import org.more.hypha.beans.AbstractPropertyDefine;
import org.more.hypha.beans.assembler.ClassCache;
/**
 * �����ְ���Ǹ���{@link AbstractBeanDefine}ת�������ͻ���Beanʵ�����
 * @version 2011-1-13
 * @author ������ (zyc@byshell.org)
 */
public class BeanEngine {
    private Map<String, BeanBuilder> beanBuilderMap = new HashMap<String, BeanBuilder>();
    //----------------------------------------------------------------------------------------------------------
    /**�÷����ķ���ֵ�����Ƿ����������չ���ִ�У�flase��ʾ������(Ĭ��ֵ)��*/
    protected boolean isIgnorePoints() {
        return false;
    };
    /**�÷����ķ���ֵ�������Ƿ����ִ���������ڷ�����false��ʾ������(Ĭ��ֵ)��*/
    protected boolean isIgnoreLifeMethod() {
        return false;
    };
    protected ClassCache getCatch() {
        return null;//TODO
    };
    /**
     * 
     * @param define
     * @param context
     * @param createParams
     * @return
     */
    public synchronized Class<?> builderType(AbstractBeanDefine define, ApplicationContext applicationContext) throws DoesSupportException, InitializationException {
        String defineType = define.getBeanType();
        String defineID = define.getID();
        ExpandPointManager epm = applicationContext.getExpandPointManager();
        //
        BeanBuilder builder = this.beanBuilderMap.get(defineType);
        if (builder == null)
            throw new DoesSupportException("hypha ��֧�ֵ�Bean�������ͣ�" + defineType);
        //1.ȷ��������bean�Ƿ���Ա�װ�س�Ϊjava���͡�
        if (builder.canbuilder() == false)
            throw new DoesSupportException(defineID + "�������Ͷ��岻��ִ��װ�ع��̡�");
        //--------------------------------------------------------------------------------------------------------------׼���׶�
        //2.װ��class
        ClassCache cache = this.getCatch();
        byte[] beanBytes = null;
        if (builder.canCache() == true)
            beanBytes = cache.loadClassCode(defineID);//��ͼ�ӻ�����װ��
        if (beanBytes == null) {
            beanBytes = builder.loadBeanBytes(define);
            if (this.isIgnorePoints() == false)
                //������þ�����������չ����ִ����չ�����
                beanBytes = (byte[]) epm.exePoint(ClassByteExpandPoint.class, new Object[] { beanBytes, define, applicationContext });
        }
        if (beanBytes == null)
            throw new NullPointerException("�����޷���ȡ�ֽ�����Ϣ�������޷�ת��Bean�����Ϊ���͡�");
        //3.װ��Class�࣬���װ�ز���hypha����ǿ��
        Class<?> beanType = builder.loadClass(define, beanBytes);
        if (this.isIgnorePoints() == false)
            //������þ�����������չ����ִ����չ�����
            beanType = (Class<?>) epm.exePoint(ClassTypeExpandPoint.class, new Object[] { beanType, define, applicationContext });
        if (beanType == null)
            throw new NullPointerException("��ʧBean���Ͷ��壬�������չ���Ƿ������������͡�");
        return beanType;
    };
    /**
     * 
     * @param define
     * @param context
     * @param createParams
     * @return
     */
    public Object builderBean(AbstractBeanDefine define, ApplicationContext applicationContext, Object[] params) {
        String defineID = define.getID();
        Class<?> beanType = this.builderType(define, applicationContext);
        ExpandPointManager epm = applicationContext.getExpandPointManager();
        //--------------------------------------------------------------------------------------------------------------�����׶�
        //1.Ԥ����Bean
        Object obj = null;
        if (this.isIgnorePoints() == false)
            obj = epm.exePoint(BeforeCreateExpandPoint.class, new Object[] { beanType, params, define, applicationContext });
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
        //3.ִ�д����ĺ�������
        if (this.isIgnorePoints() == false)
            //������þ�����������չ����ִ����չ�����
            obj = epm.exePoint(AfterCreateExpandPoint.class, new Object[] { obj, params, define, applicationContext });
        if (obj == null)
            throw new InvokeException("����[" + defineID + "]���쳣���������������ߣ�װ�ε���չ�㷵��Ϊ�ա�");
        //--------------------------------------------------------------------------------------------------------------��ʼ���׶�
        //4.װ��
        if (this.isIgnorePoints() == false)
            //������þ�����������չ����ִ����չ�����
            obj = epm.exePoint(DecoratorExpandPoint.class, new Object[] { obj, params, define, applicationContext });
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
        //
        //        ProxyFinalizeClassEngine ce = new ProxyFinalizeClassEngine(this);
        //        ce.setBuilderMode(BuilderMode.Propxy);
        //        ce.setSuperClass(objType);
        //        obj = ce.newInstance(obj);
        //7.�����������Ƿ�ƥ�䶨�����͡�
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