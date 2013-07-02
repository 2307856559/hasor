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
package org.moreframework.context;
import org.moreframework.binder.BeanInfo;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public interface BeanContext {
    /**ͨ������ȡBean�����͡�*/
    public <T> Class<T> getBeanType(String name);
    /**�������Ŀ�����͵�Bean�򷵻�Bean�����ơ�*/
    public String getBeanName(Class<?> targetClass);
    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    public String[] getBeanNames();
    /**��ȡbean��Ϣ��*/
    public BeanInfo getBeanInfo(String name);
    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    public <T> T getBean(String name);
}