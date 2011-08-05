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
package org.more.hypha.anno.assembler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import org.more.core.classcode.EngineToos;
import org.more.core.error.LostException;
import org.more.core.error.TypeException;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.AbstractMethodDefine;
import org.more.hypha.DefineResource;
import org.more.hypha.anno.KeepWatchParser;
import org.more.hypha.anno.define.Bean;
import org.more.hypha.anno.define.MetaData;
import org.more.hypha.anno.define.Param;
import org.more.hypha.anno.define.Property;
import org.more.hypha.beans.define.AbstractBaseBeanDefine;
import org.more.hypha.beans.define.AbstractPropertyDefine;
import org.more.hypha.beans.define.AbstractValueMetaData;
import org.more.hypha.beans.define.ClassPathBeanDefine;
import org.more.hypha.beans.define.ConstructorDefine;
import org.more.hypha.beans.define.EL_ValueMetaData;
import org.more.hypha.beans.define.MethodDefine;
import org.more.hypha.beans.define.ParamDefine;
import org.more.hypha.beans.define.PropertyDefine;
import org.more.hypha.beans.define.PropertyType;
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.util.StringConvertUtil;
import org.more.util.attribute.IAttribute;
/**
 * �������ڽ���Bean��ע��ʹ���ΪClassBeanDefine�������ȡ�����಻��Ӱ�쵽anno�ķ����ṩ��ֻ�����ᵼ���޷�����bean��ע��������
 * @version 2010-10-14
 * @author ������ (zyc@byshell.org)
 */
class Watch_Bean implements KeepWatchParser {
    private static Log log = LogFactory.getLog(Watch_Bean.class);
    public void process(Object target, Annotation annoData, XmlDefineResource resource) {
        Class<?> beanType = (Class<?>) target;
        Bean bean = beanType.getAnnotation(Bean.class);
        ClassPathBeanDefine define = new ClassPathBeanDefine();
        //-----------------------------------------------------------------------------------------------------����Ϣ
        String var = null;
        // ID
        var = bean.id();
        if (var.equals("") == false)
            define.setId(var);
        var = bean.name();
        if (var.equals("") == false)
            define.setName(var);
        else
            define.setName(beanType.getSimpleName());
        var = bean.logicPackage();
        if (var.equals("") == false)
            define.setLogicPackage(var);
        else
            define.setLogicPackage(beanType.getPackage().getName());
        //--����
        if (resource.containsBeanDefine(define.getID()) == true) {
            log.warning("define id is repeate!", define.getID());
            return;
        }
        // Boolean
        define.setBoolSingleton(bean.singleton());
        define.setBoolLazyInit(bean.lazyInit());
        define.setBoolAbstract(Modifier.isAbstract(beanType.getModifiers()));
        // Factory
        var = bean.factoryName();
        if (var != null && var.equals("") == false) {
            if (resource.containsBeanDefine(var) == false)
                throw new LostException("[" + define.getName() + "]�Ҳ��������Ĺ���[" + var + "]Bean����");
            AbstractBaseBeanDefine factoryBean = (AbstractBaseBeanDefine) resource.getBeanDefine(var);
            var = bean.factoryName();
            AbstractMethodDefine methodDefine = factoryBean.getMethod(var);
            factoryBean.setFactoryMethod(methodDefine);
        }
        // Desc
        var = bean.description();
        if (var.equals("") == false)
            define.setDescription(var);
        // useTemplate
        var = bean.useTemplate();
        if (var.equals("") == false) {
            if (resource.containsBeanDefine(var) == true)
                define.setUseTemplate((AbstractBaseBeanDefine) resource.getBeanDefine(var));
            else
                throw new LostException("û���ҵ�idΪ[" + var + "]��Bean������Ϊģ�塣");
        }
        // MetaData
        this.addMetaData(define, bean.metaData());
        // Source
        define.setSource(beanType.getName());
        //-----------------------------------------------------------------------------------------------------���췽��
        Constructor<?>[] cs = beanType.getConstructors();
        for (Constructor<?> c : cs) {
            org.more.hypha.anno.define.Constructor annoC = c.getAnnotation(org.more.hypha.anno.define.Constructor.class);
            if (annoC == null)
                continue;
            //1.���������Ϣ
            Class<?>[] cparamType = c.getParameterTypes();
            Annotation[][] cparamAnno = c.getParameterAnnotations();
            int length = cparamType.length;
            //2.����
            for (int i = 0; i < length; i++) {
                //1)׼��һ������������
                Class<?> cpt = cparamType[i];
                Param cpa = null;
                for (Annotation cpa_temp : cparamAnno[i])
                    if (cpa_temp instanceof Param == true) {
                        cpa = (Param) cpa_temp;
                        break;
                    }
                //2)����ConstructorDefine
                ConstructorDefine cDefine = new ConstructorDefine();
                cDefine.setIndex(i);
                cDefine.setClassType(cpt.getName());
                //3)���������
                cDefine = (ConstructorDefine) getPropertyDefine(cpa, define, cDefine, resource);
                define.addInitParam(cDefine);
            }
            break;
        }
        //-----------------------------------------------------------------------------------------------------����
        ArrayList<Method> methods = EngineToos.findAllMethod(beanType);
        for (Method m : methods) {
            org.more.hypha.anno.define.Method ma = m.getAnnotation(org.more.hypha.anno.define.Method.class);
            if (ma == null)
                continue;
            //1.����
            MethodDefine mDefine = new MethodDefine();
            mDefine.setCodeName(m.getName());
            mDefine.setName(ma.name());
            mDefine.setBoolStatic(EngineToos.checkIn(Modifier.STATIC, m.getModifiers()));
            this.addMetaData(mDefine, ma.metaData());
            //2.����
            Class<?>[] mparamType = m.getParameterTypes();
            Annotation[][] mparamAnno = m.getParameterAnnotations();
            int length = mparamType.length;
            /*-----------------------------------------*/
            //LocalVariableAttribute varAtt = this.getLocalVariableAttribute(m);
            /*-----------------------------------------*/
            for (int i = 0; i < length; i++) {
                //1)׼��һ������������
                Class<?> cpt = mparamType[i];
                Param cpa = null;
                for (Annotation cpa_temp : mparamAnno[i])
                    if (cpa_temp instanceof Param == true) {
                        cpa = (Param) cpa_temp;
                        break;
                    }
                //2)����ParamDefine
                ParamDefine pDefine = new ParamDefine();
                pDefine.setIndex(i);
                pDefine.setClassType(cpt.getName());
                //3)���������
                pDefine = (ParamDefine) getPropertyDefine(cpa, define, pDefine, resource);
                define.addMethod(mDefine);
            }
        }
        //-----------------------------------------------------------------------------------------------------����
        ArrayList<Field> af = EngineToos.findAllField(beanType);
        for (Field f : af) {
            Property fa = f.getAnnotation(Property.class);
            if (fa == null)
                continue;
            PropertyDefine pDefine = new PropertyDefine();
            pDefine.setBoolLazyInit(fa.lazyInit());
            pDefine.setClassType(f.getType().getName());
            pDefine.setDescription(fa.desc());
            pDefine.setName(f.getName());
            this.addMetaData(pDefine, fa.metaData());
            //
            pDefine = (PropertyDefine) getPropertyDefine(fa, define, pDefine, resource);
            define.addProperty(pDefine);
        }
        resource.addBeanDefine(define);
    }
    private AbstractPropertyDefine getPropertyDefine(Property anno, ClassPathBeanDefine define, AbstractPropertyDefine propDefine, DefineResource resource) {
        String cpt = propDefine.getClassType();
        //3)����Paramע��
        AbstractValueMetaData valueMetaData = null;
        if (anno != null) {
            this.addMetaData(propDefine, anno.metaData());
            //1.ע��
            String var = anno.desc();
            if (var.equals("") == false)
                propDefine.setDescription(var);
            //2.����
            String txtVar = anno.value();
            String elVar = anno.el();
            if (txtVar.equals("") == false) {
                //����value
                PropertyType propType = Simple_ValueMetaData.getPropertyType(cpt);
                if (propType == null)
                    throw new TypeException(define.getID() + "������ע��Param�ڼ䷢���޷���[" + cpt + "]��Ϊ�������ʹ���");
                Simple_ValueMetaData temp = new Simple_ValueMetaData();
                Class<?> propClass = Simple_ValueMetaData.getPropertyType(propType);
                temp.setValue(StringConvertUtil.changeType(txtVar, propClass));
                temp.setValueMetaType(propType);
                valueMetaData = temp;
            } else if (elVar.equals("") == false) {
                //����elValue
                EL_ValueMetaData temp = new EL_ValueMetaData();
                temp.setElText(elVar);
                valueMetaData = temp;
            }
        }
        if (valueMetaData == null)
            valueMetaData = this.getDefaultValueMetaData(cpt);
        //5)���
        propDefine.setValueMetaData(valueMetaData);
        return propDefine;
    }
    private AbstractPropertyDefine getPropertyDefine(Param anno, ClassPathBeanDefine define, AbstractPropertyDefine propDefine, DefineResource resource) {
        String cpt = propDefine.getClassType();
        //3)����Paramע��
        AbstractValueMetaData valueMetaData = null;
        if (anno != null) {
            this.addMetaData(propDefine, anno.metaData());
            //1.ע��
            String var = anno.desc();
            if (var.equals("") == false)
                propDefine.setDescription(var);
            //2.����
            String txtVar = anno.value();
            String elVar = anno.el();
            if (txtVar.equals("") == false) {
                //����value
                PropertyType propType = Simple_ValueMetaData.getPropertyType(cpt);
                if (propType == null)
                    throw new TypeException(define.getID() + "������ע��Param�ڼ䷢���޷���[" + cpt + "]��Ϊ�������ʹ���");
                Simple_ValueMetaData temp = new Simple_ValueMetaData();
                Class<?> propClass = Simple_ValueMetaData.getPropertyType(propType);
                temp.setValue(StringConvertUtil.changeType(txtVar, propClass));
                temp.setValueMetaType(propType);
                valueMetaData = temp;
            } else if (elVar.equals("") == false) {
                //����elValue
                EL_ValueMetaData temp = new EL_ValueMetaData();
                temp.setElText(elVar);
                valueMetaData = temp;
            }
        }
        if (valueMetaData == null)
            valueMetaData = this.getDefaultValueMetaData(cpt);
        //5)���
        propDefine.setValueMetaData(valueMetaData);
        return propDefine;
    }
    /**�������ͻ�ȡ������ص�Ĭ��ValueMetaData����*/
    private AbstractValueMetaData getDefaultValueMetaData(String stringType) {
        PropertyType propType = Simple_ValueMetaData.getPropertyType(stringType);
        //û���ע��
        Simple_ValueMetaData simpleMetaData = new Simple_ValueMetaData();
        Object defaultValue = null;//
        if (propType == null)
            defaultValue = null;//
        else {
            Class<?> cpt = Simple_ValueMetaData.getPropertyType(propType);
            defaultValue = EngineToos.getDefaultValue(cpt);
        }
        //�������ͺ�Ĭ��ֵ
        simpleMetaData.setValueMetaType(propType);
        simpleMetaData.setValue(defaultValue);
        return simpleMetaData;
    }
    /**���Ԫ��Ϣ����*/
    private void addMetaData(IAttribute att, MetaData[] data) {
        if (data == null)
            return;
        for (MetaData meta : data)
            att.setAttribute(meta.key(), meta.value());
    }
    //private static ClassPool classPool = ClassPool.getDefault();
    //private LocalVariableAttribute getLocalVariableAttribute(Method m) {
    //    /*-----------------------------------------*/
    //    try {
    //        StringBuffer s = new StringBuffer("(");
    //        s.append(EngineToos.toAsmType(m.getParameterTypes()));
    //        s.append(")");
    //        s.append(EngineToos.toAsmType(m.getReturnType()));
    //        CtMethod cmethod = classPool.getCtClass(m.getDeclaringClass().getName()).getMethod(m.getName(), s.toString());
    //        CodeAttribute codeAtt = cmethod.getMethodInfo().getCodeAttribute();
    //        return (LocalVariableAttribute) codeAtt.getAttribute(LocalVariableAttribute.tag);
    //    } catch (javassist.NotFoundException e) {}
    //    return null;
    //    /*-----------------------------------------*/
    //}
}