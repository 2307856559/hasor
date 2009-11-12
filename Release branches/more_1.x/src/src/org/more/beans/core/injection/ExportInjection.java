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
package org.more.beans.core.injection;
import org.more.beans.BeanFactory;
import org.more.beans.core.Injection;
import org.more.beans.info.BeanDefinition;
/**
 * ʹ��Export��ʽ����ʹ������Ա��������ע����̣���Export��ʽ��more.beans������������κ�ע�������
 * more.bean��ί��ExportInjectionProperty�ӿڽ���ע�����󡣾��������Ե�ע�������ʵ��
 * ExportInjectionProperty�ӿڡ�����ⲿע�봦�����Ϊ����Export������ע������
 * <br/>Export��ʽע�����ʹ������Ա��������ע����̴Ӷ��ṩ���߼�������ע��ҵ���߼�������aop��ͬ��
 * aopרע�ڶԷ������������̣���ioc��רע������ע�롣Export���ṩһ�ָ��������ɵĸ߼�ע�뷽ʽ��<br/>
 * Date : 2009-11-7
 * @author ������
 */
public class ExportInjection implements Injection {
    //========================================================================================Field
    /** ʹ�õ��ⲿ����ע����� */
    private ExportInjectionProperty injectionProperty = null;
    //==================================================================================Constructor
    /** ����һ��ExportInjection���Ͷ��� */
    public ExportInjection(ExportInjectionProperty injectionProperty) {
        this.injectionProperty = injectionProperty;
    }
    //==========================================================================================Job
    /** ִ��ExportInjectionProperty�ӿڷ�������ע�����ԡ� */
    @Override
    public void ioc(Object object, Object[] params, BeanDefinition definition, BeanFactory context) {
        if (this.injectionProperty != null)
            this.injectionProperty.injectionProperty(object, params, definition, context);
    }
}
