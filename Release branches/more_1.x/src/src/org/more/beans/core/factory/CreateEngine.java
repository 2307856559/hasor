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
import java.util.HashMap;
import org.more.beans.BeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanInterface;
import org.more.core.classcode.AOPInvokeFilter;
import org.more.core.classcode.ClassEngine;
import org.more.core.classcode.MethodDelegate;
/**
 * CreateEngine������ֱ�ʵ����{@link org.more.beans.info.CreateTypeEnum}ö���ж���Ĵ�����ʽ��
 * @version 2009-11-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class CreateEngine {
    /***/
    public abstract Object newInstance(BeanDefinition definition, Object[] params, BeanFactory context) throws Exception;
    /** ����{@link BeanDefinition}��ȡ�����Class�������������AOP���߸��ӽӿڡ��򷵻ر���֮���Class���� */
    protected Class<?> getClassType(BeanDefinition definition, Object[] params, BeanFactory context) throws Exception {
        ClassLoader loader = context.getBeanClassLoader();
        ClassEngine engine = new ClassEngine(loader);
        engine.setSuperClass(loader.loadClass(definition.getPropType()));
        engine.setEnableAOP(true);
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
                        typeClass = context.getBeanType(beanI.getRefType());
                    //���ӽӿ�ʵ��
                    engine.appendImpl(typeClass, (MethodDelegate) context.getBean(beanI.getDelegateRefBean(), params));
                }
            }
        }
        {
            //---------------------------------------------------------------AOP
            String[] aopFilters = definition.getAopFiltersRefBean();
            if (aopFilters != null) {
                AOPInvokeFilter[] filters = new AOPInvokeFilter[aopFilters.length];
                for (int i = 0; i < aopFilters.length; i++)
                    filters[i] = (AOPInvokeFilter) context.getBean(aopFilters[i], params);
                engine.setCallBacks(filters);
            }
        }
        return engine.toClass();
    }
    /** ���������Bean�������{@link ClassEngine}֧��������beanʱ�ṩ�´������Ĵ������� */
    protected Object configurationBean(ClassLoader loader, Object newObject, BeanDefinition definition, Object[] params, BeanFactory context) throws Exception {
        if (loader instanceof ClassEngine == false)
            return newObject;
        //=====================================================================
        BeanInterface[] implS = definition.getImplImplInterface();
        String[] aopFilters = definition.getAopFiltersRefBean();
        HashMap<Class<?>, MethodDelegate> replaceDelegateMap = null;
        AOPInvokeFilter[] filters = null;
        //
        //һ���������ӽӿڴ���
        if (implS != null) {
            replaceDelegateMap = new HashMap<Class<?>, MethodDelegate>();
            for (int i = 0; i < implS.length; i++) {
                BeanInterface beanI = implS[i];
                MethodDelegate delegate = (MethodDelegate) context.getBean(beanI.getDelegateRefBean(), params);
                Class<?> typeClass = null;
                String type = beanI.getPropType();
                if (type != null)
                    typeClass = loader.loadClass(beanI.getPropType());
                else
                    typeClass = context.getBeanType(beanI.getRefType());
                replaceDelegateMap.put(typeClass, delegate);
            }
        }
        //�����������ӽӿڴ���
        if (aopFilters != null) {
            filters = new AOPInvokeFilter[aopFilters.length];
            for (int i = 0; i < aopFilters.length; i++)
                filters[i] = (AOPInvokeFilter) context.getBean(aopFilters[i], params);
        }
        //����װ��
        ClassEngine loaderClassEngine = (ClassEngine) loader;
        return loaderClassEngine.configuration(newObject, replaceDelegateMap, filters); //AOP�����
    }
}