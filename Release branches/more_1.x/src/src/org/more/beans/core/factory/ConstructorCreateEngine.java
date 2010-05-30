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
import org.more.beans.core.propparser.MainPropertyParser;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanInterface;
import org.more.beans.info.BeanProperty;
import org.more.core.classcode.ClassEngine;
/**
 * new��ʽ�ǳ����ִ�й��췽���������������beanû�����ù��췽����ϵͳ�����Class��newInstance()��������������������˹��췽������ôϵͳ���Զ�
 * Ѱ����ع��췽������ִ���乹�췽����ע�⣺Ĭ�ϲ����εĹ��췽�����Բ����ã������״��ҵ������͹��췽��֮����Щ��Ϣ�ᱻ������{@link BeanDefinition}�����С�<br/>
 * �й�AOP���߸��ӽӿ�ʵ�֡����New��ʽ��������������AOP���߽ӿ�ʵ�������ܻ����½��������������10��~100�����ͬClass������ϵĲ��Խ������������
 * ��������н��ܡ���AOP���߸��ӽӿ��������µ��������classcode���ߵ�Super��ʽ��ͬ��˽�кͱ������������ܵ�aopӰ�죬�����new��ʽ������ܵ�Ӱ�죩��
 * @version 2009-11-14
 * @author ������ (zyc@byshell.org)
 */
public class ConstructorCreateEngine extends CreateEngine {
    //========================================================================================Field
    /** ���Ի�����󣬻����������� */
    private String             catchDataName = "$more_CreateEngine_Constructor";
    /**���Խ�����*/
    private MainPropertyParser propParser    = null;
    //==================================================================================Constructor
    /**����һ��ConstructorCreateEngine���󣬴���ʱ����ָ�����Խ�������*/
    public ConstructorCreateEngine(MainPropertyParser propParser) {
        if (propParser == null)
            throw new NullPointerException("����ָ��propParser��������ConstructorCreateEngineʹ��������Խ������������ԡ�");
        this.propParser = propParser;
    }
    //==========================================================================================Job
    /**���ҹ��췽������*/
    private Object[] findConstructorObject(BeanDefinition definition, Object[] params, BeanFactory context) throws Exception {
        BeanProperty[] beanConParams = definition.getConstructorParams();
        Object[] classConParams = new Object[beanConParams.length];
        for (int i = 0; i < beanConParams.length; i++) {
            BeanProperty beanP = beanConParams[i];
            if (beanP == null)
                continue;//���Կյ�
            classConParams[i] = this.propParser.parser(null, params, beanP.getRefValue(), beanP, definition);
        }
        return classConParams;
    }
    /**
     * ���ҹ��췽�������û�����ù��췽��������null��
     * ������õĹ��췽�����������õ�����bean��ᵼ��context.getBeanType(beanCP.getRefBean())��
     */
    private Constructor<?> findConstructor(Class<?> type, BeanDefinition definition, Object[] params, BeanFactory context) throws Exception {
        BeanProperty[] beanConParams = definition.getConstructorParams();
        if (beanConParams == null)
            return null;
        //
        Class<?>[] classConParams = new Class<?>[beanConParams.length];
        for (int i = 0; i < beanConParams.length; i++) {
            BeanProperty beanCP = beanConParams[i];
            classConParams[i] = this.propParser.parserType(null, params, beanCP.getRefValue(), beanCP, definition);;
        }
        return type.getConstructor(classConParams);
    }
    /**
     * ����Ŀ����Ĺ��췽��ʵ��������������Ŀ��������AOP���߸��ӽӿ�ʵ�֣��������ᱻ{@link ClassEngine}��д��
     * Ȼ�����������õĹ��췽����ʼ����������ڷ��������ݶ��󴴽�ʱʹ�õ���װ�������;����Ƿ����{@link ClassEngine}������������¶���
     */
    @Override
    public Object newInstance(BeanDefinition definition, Object[] params, BeanFactory context) throws Exception {
        ConstructorCreateEngineData createEngineData;
        BeanProperty[] bc = definition.getConstructorParams();
        //һ������׼��&����
        if (definition.containsKey(catchDataName) == false) {
            ClassLoader contextLoader = context.getBeanClassLoader();
            createEngineData = new ConstructorCreateEngineData();
            //����֮ǰ�Ļ�����Ϣ
            String[] aopFilters = definition.getAopFiltersRefBean();
            BeanInterface[] implsFilters = definition.getImplImplInterface();
            //ȷ������
            if (aopFilters == null && implsFilters == null)
                createEngineData.type = contextLoader.loadClass(definition.getPropType()); //û��AOPҪ��
            else {
                createEngineData.type = this.getClassType(definition, params, context);//��ҪAOP
                createEngineData.loaderClassEngine = (ClassEngine) createEngineData.type.getClassLoader();
            }
            //ȷ�����췽��
            if (bc != null)
                createEngineData.c = this.findConstructor(createEngineData.type, definition, params, context);
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