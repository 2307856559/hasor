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
package org.more.hypha.beans.assembler.factory;
import org.more.hypha.ApplicationContext;
import org.more.hypha.beans.AbstractBeanDefine;
/**
* 
* @version 2010-12-23
* @author ������ (zyc@byshell.org)
*/
public abstract class BeanBuilder {
    /**Ӧ��������*/
    private ApplicationContext context = null;
    public BeanBuilder(ApplicationContext context) {
        this.context = context;
    };
    /**�Ƿ�װ�ػ����е��ֽ���*/
    public boolean canCache() {
        return false;
    };
    /**�Ƿ���Ա�װ�س������*/
    public boolean canbuilder() {
        return false;
    };
    /**��ȡӦ��������*/
    protected ApplicationContext getApplicationContext() {
        return this.context;
    };
    /**װ��BeanDefine���壬����ȡ��Class������ֽ������ݡ�*/
    public abstract byte[] loadBeanBytes(AbstractBeanDefine define);
    /**��ԭʼ�´�����Bean����һ�γ�ʼbuilder��*/
    public abstract Object builderBean(Object obj, AbstractBeanDefine define);
}