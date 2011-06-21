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
package org.more.hypha.commons.logic;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.commons.xml.Tag_Abstract;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.log.ILog;
import org.more.log.LogFactory;
/**
 * �ó����������ĳ��{@link AbstractBeanDefine}���͵��ض�֧�֡��ýӿڵĹ����Ǹ��𴴽�ĳ�����͵�Bean��
 * @version : 2011-5-12
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractBeanBuilder<T extends AbstractBeanDefine> {
    private static final ILog          log                = LogFactory.getLog(Tag_Abstract.class);
    private AbstractApplicationContext applicationContext = null;
    /*------------------------------------------------------------------------------*/
    /**���õ�ǰ{@link AbstractBeanBuilder}ʹ�õ�{@link AbstractApplicationContext}��������*/
    public void setApplicationContext(AbstractApplicationContext applicationContext) {
        log.debug("set ApplicationContext {%0}", applicationContext);
        this.applicationContext = applicationContext;
    };
    /**��ȡ{@link AbstractApplicationContext}*/
    protected AbstractApplicationContext getApplicationContext() {
        return this.applicationContext;
    };
    /*------------------------------------------------------------------------------*/
    /**װ��bean��������ͣ�*/
    public abstract Class<?> loadType(T define, Object[] params) throws Throwable;
    /**����Bean����classType�����ǿ��ܾ����ӹ�֮���Ben������loadType������һ����*/
    public abstract <O> O createBean(Class<?> classType, T define, Object[] params) throws Throwable;
};