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
package org.more.hypha.annotation.assembler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import org.more.NoDefinitionException;
import org.more.NotFoundException;
import org.more.core.classcode.EngineToos;
import org.more.hypha.DefineResource;
import org.more.hypha.annotation.AnnotationDefineResourcePlugin;
import org.more.hypha.annotation.Bean;
import org.more.hypha.annotation.KeepWatchParser;
import org.more.hypha.annotation.MetaData;
import org.more.hypha.annotation.Param;
import org.more.hypha.annotation.Property;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.define.AbstractPropertyDefine;
import org.more.hypha.beans.define.ClassBeanDefine;
import org.more.hypha.beans.define.ConstructorDefine;
import org.more.hypha.beans.define.EL_ValueMetaData;
import org.more.hypha.beans.define.MethodDefine;
import org.more.hypha.beans.define.ParamDefine;
import org.more.hypha.beans.define.PropertyDefine;
import org.more.hypha.beans.define.PropertyType;
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.util.StringConvert;
import org.more.util.attribute.IAttribute;
/**
 * �������ڽ���Bean��ע��ʹ���ΪClassBeanDefine�������
 * @version 2010-10-14
 * @author ������ (zyc@byshell.org)
 */
public class Watch_Bean implements KeepWatchParser {
    public void process(Class<?> beanType, DefineResource resource, AnnotationDefineResourcePlugin plugin) {
        Bean bean = beanType.getAnnotation(Bean.class);
        ClassBeanDefine define = new ClassBeanDefine();
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
        // Boolean
        define.setBoolSingleton(bean.singleton());
        define.setBoolLazyInit(bean.lazyInit());
        define.setBoolAbstract(Modifier.isAbstract(beanType.getModifiers()));
        define.setBoolInterface(Modifier.isInterface(beanType.getModifiers()));
        // Factory
        var = bean.factoryName();
        if (var.equals("") == false)
            define.setFactoryName(var);
        var = bean.factoryMethod();
        if (var.equals("") == false)
            define.setFactoryMethod(var);
        // Desc
        var = bean.description();
        if (var.equals("") == false)
            define.setDescription(var);
        // useTemplate
        var = bean.useTemplate();
        if (var.equals("") == false) {
            if (resource.containsBeanDefine(var) == true)
                define.setUseTemplate(var);
            else
                throw new NoDefinitionException("û���ҵ�idΪ[" + var + "]��Bean������Ϊģ�塣");
        }
        // MetaData
        this.addMetaData(define, bean.metaData());
        // Source
        define.setSource(beanType);
        //-----------------------------------------------------------------------------------------------------���췽��
        Constructor<?>[] cs = beanType.getConstructors();
        for (Constructor<?> c : cs) {
            org.more.hypha.annotation.Constructor annoC = c.getAnnotation(org.more.hypha.annotation.Constructor.class);
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
                cDefine.setClassType(cpt);
                //3)���������
                cDefine = (ConstructorDefine) getPropertyDefine(cpa, define, cDefine, resource);
                define.addInitParam(cDefine);
            }
            break;
        }
        //-----------------------------------------------------------------------------------------------------����
        ArrayList<Method> methods = EngineToos.findAllMethod(beanType);
        for (Method m : methods) {
            org.more.hypha.annotation.Method ma = m.getAnnotation(org.more.hypha.annotation.Method.class);
            if (ma == null)
                continue;
            //1.����
            MethodDefine mDefine = new MethodDefine();
            mDefine.setCodeName(m.getName());
            mDefine.setName(ma.name());
            this.addMetaData(mDefine, ma.metaData());
            //2.����
            Class<?>[] mparamType = m.getParameterTypes();
            Annotation[][] mparamAnno = m.getParameterAnnotations();
            int length = mparamType.length;
            /*-----------------------------------------*/
            LocalVariableAttribute varAtt = this.getLocalVariableAttribute(m);
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
                pDefine.setName(varAtt.variableName(i + 1));
                pDefine.setClassType(cpt);
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
            pDefine.setClassType(f.getType());
            pDefine.setDescription(fa.desc());
            pDefine.setName(f.getName());
            this.addMetaData(pDefine, fa.metaData());
            //
            pDefine = (PropertyDefine) getPropertyDefine(fa, define, pDefine, resource);
            define.addProperty(pDefine);
        }
        resource.addBeanDefine(define);
    }
    private AbstractPropertyDefine getPropertyDefine(Property anno, ClassBeanDefine define, AbstractPropertyDefine propDefine, DefineResource resource) {
        Class<?> cpt = propDefine.getClassType();
        //3)����Paramע��
        ValueMetaData valueMetaData = null;
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
                    throw new NotFoundException(define.getID() + "������ע��Param�ڼ䷢���޷���[" + cpt + "]��Ϊ�������ʹ���");
                Simple_ValueMetaData temp = new Simple_ValueMetaData();
                temp.setValue(StringConvert.changeType(txtVar, cpt));
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
    private AbstractPropertyDefine getPropertyDefine(Param anno, ClassBeanDefine define, AbstractPropertyDefine propDefine, DefineResource resource) {
        Class<?> cpt = propDefine.getClassType();
        //3)����Paramע��
        ValueMetaData valueMetaData = null;
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
                    throw new NotFoundException(define.getID() + "������ע��Param�ڼ䷢���޷���[" + cpt + "]��Ϊ�������ʹ���");
                Simple_ValueMetaData temp = new Simple_ValueMetaData();
                temp.setValue(StringConvert.changeType(txtVar, cpt));
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
    private ValueMetaData getDefaultValueMetaData(Class<?> cpt) {
        //û���ע��
        Simple_ValueMetaData simpleMetaData = new Simple_ValueMetaData();
        Object defaultValue = null;//
        PropertyType enumType = null;//
        if (cpt.isPrimitive() == true) {
            //�����������
            defaultValue = EngineToos.getDefaultValue(cpt);//
            enumType = Simple_ValueMetaData.getPropertyType(cpt);//
        } else {
            //��ֵ
            enumType = PropertyType.Null;
            defaultValue = null;
        }
        //�������ͺ�Ĭ��ֵ
        simpleMetaData.setValueMetaType(enumType);
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
    //
    private static ClassPool classPool = ClassPool.getDefault();
    private LocalVariableAttribute getLocalVariableAttribute(Method m) {
        /*-----------------------------------------*/
        try {
            StringBuffer s = new StringBuffer("(");
            s.append(EngineToos.toAsmType(m.getParameterTypes()));
            s.append(")");
            s.append(EngineToos.toAsmType(m.getReturnType()));
            CtMethod cmethod = classPool.getCtClass(m.getDeclaringClass().getName()).getMethod(m.getName(), s.toString());
            CodeAttribute codeAtt = cmethod.getMethodInfo().getCodeAttribute();
            return (LocalVariableAttribute) codeAtt.getAttribute(LocalVariableAttribute.tag);
        } catch (javassist.NotFoundException e) {}
        return null;
        /*-----------------------------------------*/
    }
}