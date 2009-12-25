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
import org.more.beans.info.BeanDefinition;
/**
 * ����ע��������ӿڣ��ýӿڸ����ĳ��bean���и���ע������Ĵ���
 * <br/>Date : 2009-11-7
 * @author ������
 */
public interface ExportInjectionProperty {
    /**
     * ����beanע�뷽����
     * @param object ������ע���bean
     * @param getBeanParam �ڵ���getbeanʱ���ݵĲ�����
     * @param definition ������ע���bean���塣
     * @param context ����beans���������ġ�
     * @return ����ע����ϵĶ���
     */
    public Object injectionProperty(Object object, Object[] getBeanParam, BeanDefinition definition, BeanFactory context);
}