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
package org.more.hypha.beans.assembler;
import java.io.IOException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.ApplicationContext;
/**
* �����������ԶԲ�ͬ���͵�{@link AbstractBeanDefine}�ֿ������ֽ���װ�غͶ��󴴽�֮��ĵ�һ�γ�ʼ��������ejb��Ϊ{@link AbstractBeanDefine}������ϵͳ�С�
* ����������ʵ�ִ�����Զ�̣����Ա��ػ�ͨ��ĳ�ַ�ʽ��Զ��ebj����һ��������������Զ�̶�����ô����������Ĵ�����Ҫͨ��hypha��ioc���ƽ��У���
* @version 2010-12-23
* @author ������ (zyc@byshell.org)
*/
public abstract class AbstractBeanBuilder<T extends AbstractBeanDefine> {
    /**Ӧ��������*/
    private ApplicationContext applicationContext = null;
    /**�Ƿ���Ա�װ�س������Ĭ�Ϸ���ֵfalse��*/
    public boolean canBuilder() {
        return false;
    };
    /**
     * �÷���ȷ��{@link AbstractBeanBuilder}���Ƿ�ʹ��Ĭ��bean����ģʽ����Ĭ��ģʽ��ʹ��ioc�����ַ�ʽ(���������췽��)����bean��
     * ����÷�����ֵΪfalse��ͨ��{@link AbstractBeanBuilder#createBean(AbstractBeanDefine, Object[])}����������bean��
     * <br/>Ĭ�Ϸ���ֵfalse��
     */
    public boolean ifDefaultBeanCreateMode() {
        return true;
    };
    /**����{@link ApplicationContext}�ӿڶ���*/
    void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    /**��ȡӦ��������*/
    protected ApplicationContext getApplicationContext() {
        return this.applicationContext;
    };
    /**
     * װ��BeanDefine���壬����ȡ��Class������ֽ������ݡ���ִ�и÷���֮ǰ{@link BeanEngine}��
     * ��ͨ��{@link #canBuilder()}�����ж��Ƿ�֧��{@link #loadBeanBytes(AbstractBeanDefine)}װ���ֽ��롣
     */
    public abstract byte[] loadBeanBytes(T define) throws IOException;
    /**��ifDefaultBeanCreateMode��������ֵΪfalseʱ��ͨ��{@link BeanEngine}�����ͨ���÷�������bean��*/
    public abstract Object createBean(T define, Object[] params) throws Throwable;
};