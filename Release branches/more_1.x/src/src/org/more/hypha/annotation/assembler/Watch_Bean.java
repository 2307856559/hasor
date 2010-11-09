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
import java.lang.reflect.Modifier;
import org.more.NoDefinitionException;
import org.more.hypha.DefineResource;
import org.more.hypha.annotation.AnnotationDefineResourcePlugin;
import org.more.hypha.annotation.Bean;
import org.more.hypha.annotation.KeepWatchParser;
import org.more.hypha.annotation.Param;
import org.more.hypha.beans.define.ClassBeanDefine;
import org.more.hypha.beans.define.ConstructorDefine;
import org.more.hypha.configuration.DefineResourceImpl;
/**
 * �������ڽ���Bean��ע��ʹ���ΪClassBeanDefine�������
 * @version 2010-10-14
 * @author ������ (zyc@byshell.org)
 */
public class Watch_Bean implements KeepWatchParser {
    public void process(Class<?> beanType, DefineResource resource, AnnotationDefineResourcePlugin plugin) {
        DefineResourceImpl resourceImpl = (DefineResourceImpl) resource;
        Bean bean = beanType.getAnnotation(Bean.class);
        ClassBeanDefine define = new ClassBeanDefine();
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
        // Scope
        var = bean.scope();
        if (var.equals("") == false)
            define.setScope(var);
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
            if (resourceImpl.containsBeanDefine(var) == true)
                define.setUseTemplate(var);
            else
                throw new NoDefinitionException("û���ҵ�idΪ[" + var + "]��Bean������Ϊģ�塣");
        }
        // Source
        define.setSource(beanType);
        // ���췽��
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
                //
                //
                //                //3)ע��
                //                var = cpa.desc();
                //                if (var.equals("") == false)
                //                    cDefine.setDescription(var);
                //                //4)����ֵ
                //                if (cpt.isPrimitive()==true){
                //                    
                //                    cpa.value();
                //                    
                //                    //resourceImpl.getTypeManager().parserType(cpa.value(), att, property);
                //                     
                //                    
                //                    
                //                }
                //5)���
                define.addInitParam(cDefine);
            }
            break;
        }
        // ����
        // ����
        //
        // C
        //
        resourceImpl.addBeanDefine(define);
        System.out.println("����Bean:" + beanType);
    }
}