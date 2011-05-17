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
package org.more.hypha.commons.engine;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
/**
 * �ýӿ������ĳ��{@link AbstractBeanDefine}���͵��ض�֧�֡��ýӿڵĹ����Ǹ��𴴽�ĳ�����͵�Bean��
 * @version : 2011-5-12
 * @author ������ (zyc@byshell.org)
 */
public interface AbstractBeanBuilder<T extends AbstractBeanDefine> {
    /**���õ�ǰ{@link AbstractBeanBuilder}ʹ�õ�{@link ApplicationContext}��������*/
    public void setApplicationContext(ApplicationContext applicationContext);
    /**װ��bean��������͡�*/
    public Class<?> loadType(AbstractBeanDefine define, Object[] params);
    /**����Bean����*/
    public <O> O createBean(AbstractBeanDefine define, Object[] params);
};