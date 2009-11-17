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
import java.lang.reflect.Constructor;

import org.more.beans.BeanFactory;
import org.more.beans.core.TypeParser;
import org.more.beans.info.BeanConstructor;
import org.more.beans.info.BeanConstructorParam;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanInterface;
import org.more.core.classcode.ClassEngine;
import org.more.util.StringConvert;
/**
 * new��ʽ�ǳ����ִ�й��췽���������������beanû�����ù��췽����ϵͳ�����Class��newInstance()��������������������˹��췽������ôϵͳ���Զ�
 * Ѱ����ع��췽������ִ���乹�췽����ע�⣺Ĭ�ϲ����εĹ��췽�����Բ����ã������״��ҵ������͹��췽��֮����Щ��Ϣ�ᱻ������BeanDefinition�����С�<br/>
 * �й�AOP���߸��ӽӿ�ʵ�֡����New��ʽ��������������AOP���߽ӿ�ʵ�������ܻ����½��������������10��~100�����ͬClass������ϵĲ��Խ������������
 * ��������н��ܡ���AOP���߸��ӽӿ��������µ��������classcode���ߵ�Super��ʽ��ͬ��˽�кͱ������������ܵ�aopӰ�죬�����new��ʽ������ܵ�Ӱ�죩��
 * Date : 2009-11-14
 * @author ������
 */
public class ConstructorCreateEngine extends CreateEngine {
    //========================================================================================Field
    /** ���Ի�����󣬻����������� */
    private String catchDataName = "$more_CreateEngine_Constructor";
    //==========================================================================================Job
    /**���ҹ��췽������*/
    private Object[] findConstructorObject(BeanDefinition definition, Object[] params, BeanFactory context) {
        BeanConstructorParam[] beanConParams = definition.getConstructor().getParamTypes();
        Object[] classConParams = new Object[beanConParams.length];
        for (int i = 0; i < beanConParams.length; i++) {
            BeanConstructorParam beanP = beanConParams[i];
            String propType = beanP.getPropType();
            String propValue = beanP.getValue();
            if (propType == BeanConstructorParam.TS_Boolean)
                classConParams[i] = StringConvert.parseBoolean(propValue);
            else if (propType == BeanConstructorParam.TS_Byte)
                classConParams[i] = StringConvert.parseByte(propValue);
            else if (propType == BeanConstructorParam.TS_Char)
                if (propValue == null)
                    classConParams[i] = (char) 0;
                else
                    classConParams[i] = propValue.charAt(0);
            else if (propType == BeanConstructorParam.TS_Double)
                classConParams[i] = StringConvert.parseDouble(propValue);
            else if (propType == BeanConstructorParam.TS_Float)
                classConParams[i] = StringConvert.parseFloat(propValue);
            else if (propType == BeanConstructorParam.TS_Integer)
                classConParams[i] = StringConvert.parseInt(propValue);
            else if (propType == BeanConstructorParam.TS_Long)
                classConParams[i] = StringConvert.parseLong(propValue);
            else if (propType == BeanConstructorParam.TS_Short)
                classConParams[i] = StringConvert.parseShort(propValue);
            else if (propType == BeanConstructorParam.TS_String)
                classConParams[i] = propValue;
            else if (propType == BeanConstructorParam.TS_List)
                classConParams[i] = TypeParser.passerList(null, params, beanP, context);
            else if (propType == BeanConstructorParam.TS_Set)
                classConParams[i] = TypeParser.passerSet(null, params, beanP, context);
            else if (propType == BeanConstructorParam.TS_Map)
                classConParams[i] = TypeParser.passerMap(null, params, beanP, context);
            else if (propType == BeanConstructorParam.TS_Array)
                classConParams[i] = TypeParser.passerArray(null, params, beanP, context);
            else
                classConParams[i] = TypeParser.passerType(null, params, beanP, context);
        }
        return classConParams;
    }
    /**
     * ���ҹ��췽�������û�����ù��췽��������null��
     * ������õĹ��췽�����������õ�����bean��ᵼ��context.getOriginalBeanType(beanCP.getRefBean())��
     */
    private Constructor<?> findConstructor(Class<?> type, BeanDefinition definition, BeanFactory context) throws Throwable {
        BeanConstructor constructor = definition.getConstructor();
        if (constructor == null)
            return null;
        //
        ClassLoader contextLoader = context.getBeanClassLoader();
        BeanConstructorParam[] beanConParams = constructor.getParamTypes();
        Class<?>[] classConParams = new Class<?>[beanConParams.length];
        for (int i = 0; i < beanConParams.length; i++) {
            BeanConstructorParam beanCP = beanConParams[i];
            String paramType = beanCP.getPropType();
            if (paramType != null)
                classConParams[i] = this.toClass(paramType, contextLoader);
            else
                classConParams[i] = context.getBeanType(beanCP.getRefBean());
        }
        return type.getConstructor(classConParams);
    }
    /**
     * ����Ŀ����Ĺ��췽��ʵ��������������Ŀ��������AOP���߸��ӽӿ�ʵ�֣��������ᱻClassEngine��д��
     * Ȼ�����������õĹ��췽����ʼ����������ڷ��������ݶ��󴴽�ʱʹ�õ���װ�������;����Ƿ����ClassEngine������������¶���
     */
    @Override
    public Object newInstance(BeanDefinition definition, Object[] params, BeanFactory context) throws Throwable {
        ConstructorCreateEngineData createEngineData;
        BeanConstructor bc = definition.getConstructor();
        //һ������׼��&����
        if (definition.containsKey(catchDataName) == false) {
            ClassLoader contextLoader = context.getBeanClassLoader();
            createEngineData = new ConstructorCreateEngineData();
            //����֮ǰ�Ļ�����Ϣ
            String[] aopFilters = definition.getAopFilterRefBean();
            BeanInterface[] implsFilters = definition.getImplImplInterface();
            //ȷ������
            if (aopFilters == null && implsFilters == null)
                createEngineData.type = this.toClass(definition.getType(), contextLoader); //û��AOPҪ��
            else {
                createEngineData.type = this.getClassType(definition, params, context);//��ҪAOP
                createEngineData.loaderClassEngine = (ClassEngine) createEngineData.type.getClassLoader();
            }
            //ȷ�����췽��
            if (bc != null)
                createEngineData.c = this.findConstructor(createEngineData.type, definition, context);
            //����
            definition.put(catchDataName, createEngineData);
        } else
            createEngineData = (ConstructorCreateEngineData) definition.get(this.catchDataName);
        //����ִ�д���
        Object obj = null;
        if (createEngineData.c == null)
            obj = createEngineData.type.newInstance();
        else
            obj = createEngineData.c.newInstance(this.findConstructorObject(definition, params, context));
        //����װ��
        return this.configurationBean(createEngineData.loaderClassEngine, obj, definition, params, context);
    }
}
/**
 * ConstructorCreateEngine��ʽ����Ҫ��������ݡ�
 * Date : 2009-11-14
 * @author ������
 */
class ConstructorCreateEngineData {
    public Class<?>       type              = null;
    public Constructor<?> c                 = null;
    public ClassEngine    loaderClassEngine = null;
}