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
package org.more.beans.core.factory;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.more.beans.BeanFactory;
import org.more.beans.core.propparser.MainPropertyParser;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanInterface;
import org.more.beans.info.BeanProp;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.CreateTypeEnum;
import org.more.core.classcode.AOPInvokeFilter;
import org.more.core.classcode.ClassEngine;
import org.more.core.classcode.MethodDelegate;
import org.more.core.classcode.ClassEngine.BuilderMode;
/**
 * ʹ�ù�����ʽ����һ��Bean�������ַ�ʽ��Ҫָ���������Լ�����������ز�����Factory��ʽ��aop�������ص��ķ�����classcode���ߵ�Propxt��ʽ��ͬ��
 * <br/><br/>�÷�ʽ��Ҫbeans����{@link CreateTypeEnum}����ΪFactory�������ṩ���󴴽�����ʱ�������Ĺ��������Լ�����������
 * ���bean������aop���߸��ӽӿ�ʵ���򹤳�bean���صĶ����������ϵͳ����һ�������������࣬�����Ծ�̬����ʽ�ڴ�������ʵ��aop�Լ����ӽӿ�ʵ�֡�
 * ��ʱaop�������ص��ķ�����classcode���ߵ�Propxt��ʽ��ͬ��˽�кͱ������������ܵ�aopӰ�죬�����new��ʽ������ܵ�Ӱ�죩��
 * Date : 2009-11-12
 * @author ������
 */
public class FactoryCreateEngine extends CreateEngine {
    //========================================================================================Field
    /** ���Ի�����󣬻����������� */
    private String             catchFactoryMethodName   = "$more_CreateEngine_Factory";
    /** ����������Ի�����󣬻����������� */
    private String             catchFactoryObjectPropxy = "$more_CreateEngine_Factory_PropxyConstructor";
    /**���Խ�����*/
    private MainPropertyParser propParser               = null;
    //==================================================================================Constructor
    /**����һ��FactoryCreateEngine���󣬴���ʱ����ָ�����Խ�������*/
    public FactoryCreateEngine(MainPropertyParser propParser) {
        if (propParser == null)
            throw new NullPointerException("����ָ��propParser��������FactoryCreateEngineʹ��������Խ������������ԡ�");
        this.propParser = propParser;
    }
    //==========================================================================================Job
    /**���Ҳ���ȡ������������*/
    private Object[] findMethodParamObject(BeanDefinition definition, Object[] params, BeanFactory context) throws Exception {
        BeanProperty[] fmParams = definition.getFactoryMethodParams();
        Object[] fmParamTypes = new Object[fmParams.length];
        for (int i = 0; i < fmParams.length; i++) {
            BeanProperty beanP = fmParams[i];
            BeanProp propValue = beanP.getValue();
            fmParamTypes[i] = propParser.parser(null, params, propValue, beanP, definition);
        }
        return fmParamTypes;
    }
    /**�����������̵��ù�����ķ����������󣬲��ҽ�������bean������о�̬������ʵ��AOP�͸��ӽӿڷ������ܡ�*/
    @Override
    public Object newInstance(BeanDefinition definition, Object[] params, BeanFactory context) throws Exception {
        Method factoryMethod;
        String refBean = definition.getFactoryRefBean();
        //һ����ȡ����������
        if (definition.containsKey(catchFactoryMethodName) == false) {
            String refBeanMethod = definition.getFactoryMethodName();
            //׼���������������б�
            BeanProperty[] refBeanMethodParam = definition.getFactoryMethodParams();
            Class<?>[] refBeanMethodTypes = null;
            if (refBeanMethodParam != null) {
                refBeanMethodTypes = new Class[refBeanMethodParam.length];
                for (int i = 0; i < refBeanMethodParam.length; i++) {
                    BeanProperty beanP = refBeanMethodParam[i];
                    refBeanMethodTypes[i] = this.propParser.parserType(null, params, beanP.getValue(), beanP, definition);
                }
            }
            //��ȡ��������
            Class<?> factoryType = context.getBeanType(refBean);
            factoryMethod = factoryType.getMethod(refBeanMethod, refBeanMethodTypes);
            definition.put(catchFactoryMethodName, factoryMethod);
        } else
            factoryMethod = (Method) definition.get(catchFactoryMethodName);
        //����ִ�й����������ã�����Ŀ�����
        Object newObject;
        Object[] invokeMethodParams = this.findMethodParamObject(definition, params, context);
        if (definition.isFactoryIsStaticMethod() == true)
            newObject = factoryMethod.invoke(null, invokeMethodParams);//��̬��������
        else
            newObject = factoryMethod.invoke(context.getBean(refBean, params), invokeMethodParams); //���󹤳�����
        //���������Ƿ�ͨ��AOP������������ģʽ�²�֧��Super����AOP
        String[] aopFilters = definition.getAopFilterRefBean();
        BeanInterface[] implsFilters = definition.getImplImplInterface();
        if (aopFilters == null && implsFilters == null)
            return newObject; //û�д���Ҫ��
        else {
            //������󻺴�
            Constructor<?> propxyConstructor = null;
            if (definition.containsKey(catchFactoryObjectPropxy) == false) {
                Class<?> superClass = newObject.getClass();
                ClassEngine engine = new ClassEngine(context.getBeanClassLoader());
                engine.setSuperClass(superClass);
                engine.setMode(BuilderMode.Propxy);
                this.configurationImpl_AOP(engine, definition, params, context);//����engine
                propxyConstructor = engine.toClass().getConstructor(superClass);
                definition.put(catchFactoryObjectPropxy, propxyConstructor);
            } else
                propxyConstructor = (Constructor<?>) definition.get(catchFactoryObjectPropxy);
            //
            Object propxy = propxyConstructor.newInstance(newObject);
            return this.configurationBean(propxy.getClass().getClassLoader(), propxy, definition, params, context);
        }
    }
    /** ����ClassEngine���Ľӿ�ʵ���Լ�AOP�� */
    private void configurationImpl_AOP(ClassEngine engine, BeanDefinition definition, Object[] params, BeanFactory context) throws ClassNotFoundException, IOException {
        ClassLoader loader = context.getBeanClassLoader();
        {
            //---------------------------------------------------------------Impl
            BeanInterface[] implS = definition.getImplImplInterface();
            if (implS != null) {
                for (int i = 0; i < implS.length; i++) {
                    BeanInterface beanI = implS[i];
                    Class<?> typeClass = null;
                    String type = beanI.getPropType();
                    //��ȡ���ӵ����ͣ��öδ������֧�����÷�ʽ���������ӿ�bean��
                    if (type != null)
                        typeClass = loader.loadClass(beanI.getPropType());
                    else
                        typeClass = context.getBeanType(beanI.getTypeRefBean());
                    //���ӽӿ�ʵ��
                    engine.appendImpl(typeClass, (MethodDelegate) context.getBean(beanI.getImplDelegateRefBean(), params));
                }
            }
            //
        }
        {
            //---------------------------------------------------------------AOP
            String[] aopFilters = definition.getAopFilterRefBean();
            if (aopFilters != null) {
                AOPInvokeFilter[] filters = new AOPInvokeFilter[aopFilters.length];
                for (int i = 0; i < aopFilters.length; i++)
                    filters[i] = (AOPInvokeFilter) context.getBean(aopFilters[i], params);
                engine.setCallBacks(filters);
            }
        }
    }
}