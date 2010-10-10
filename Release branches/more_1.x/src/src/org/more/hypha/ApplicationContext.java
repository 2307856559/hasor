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
package org.more.hypha;
import java.util.List;
import org.more.NoDefinitionException;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.util.attribute.IAttribute;
/**
 * ����ӿ���More��Bean�����ĸ��ӿڣ�BeanFactory���ӽӿڿ��������ض�Ŀ�Ľ���ʵ�֡�<br/><br/>
 * ��BeanFactory������Bean����Ψһ��һ�����ơ��ù���������һ�����������һ������ʵ��(ԭ�����ģʽ)���򵥸�
 * ����ʵ��(Singleton���ģʽ����ʵ�����ڵ�ǰ�����е�һ����̬���������������͵�ʵ��ȡ����bean�����á�<br/><br/>
 * ���ڹ����е�beanͨ���Ǵ�����XML�ļ��С������ų�bean����Դ��DBMS����LDAP���ⶼȡ����BeanFactory��bean����Դ���ṩ�ߡ�
 * @version 2009-11-3
 * @author ������ (zyc@byshell.org)
 */
public interface ApplicationContext extends IAttribute {
    /**
     * ��ȡ{@link ApplicationContext}�п���������������bean�������Ƽ��ϣ������ȡ�����κ���������Ҫ����һ���ռ��ϡ�
     * @return ���ػ�ȡ��������bean�������Ƽ��ϡ�
     */
    public List<String> getBeanDefinitionNames();
    /**
     * ��ȡbean�Ķ��壬��Bean�Ķ�����ֻ�������ڵ�ǰbean��Ԫ��Ϣ��
     * �����ǰbean������ע����Ҫ��������bean���ȡ����bean�Ķ�����Ҫ���µ���getBeanDefinition�������л�ȡ��
     * @param name Ҫ��ȡbean�����bean���ơ�
     * @return ����bean���壬�����ȡ����ָ����bean�����򷵻�null��
     */
    public AbstractBeanDefine getBeanDefinition(String name) throws NoDefinitionException;
    /**
     * ��ȡBeanFactory��ʹ�õ�Bean������Դ������Դ��������ṩ�й�Bean������Ϣ��
     * @return ����BeanFactory��ʹ�õ�Bean������Դ������Դ��������ṩ�й�Bean������Ϣ��
     */
    public DefineResource getBeanResource();
    /**
    * ��ȡһ����װ������more.beans�е���װ�ؾ���ͨ�������װ��������װ�صġ�
    * @return ����һ����װ������more.beans�е���װ�ؾ���ͨ�������װ��������װ�صġ�
    */
    public ClassLoader getBeanClassLoader();
    /**
     * ����Ƿ����ĳ�����Ƶ�Bean��
     * @param name Ҫ��ȡ��Bean���ơ�
     * @return ���ؼ������������ڷ���true���򷵻�false��
     */
    public boolean containsBean(String name);
    /**
     * ��ȡĳ��Bean��ʵ�����󣬸�ʵ��������ʱ����������þ����䴴����ԭ��ģʽ���ǵ�̬ģʽ�� 
     * ������bean�����Ե�����ע��������ע��Ҳ���ڴ���ʱ���С�
     * @param name Ҫ��ȡ��beanʵ�����ơ�
     * @param objects �ڻ�ȡbeanʵ��ʱ���ܻᴫ�ݵĲ�����Ϣ��
     * @return ���ػ��߷��ش�������ʵ����
     */
    public Object getBean(String name, Object... objects) throws NoDefinitionException;
    /**
     * ����Bean���ƻ�ȡ��bean���ͣ��÷�����������bean���������õ�bean���͡�
     * ��ôgetBeanType�������������ɵ��������Ͷ���
     * @param name Ҫ��ȡ��Bean���ơ�
     * @return ����Ҫ��ȡ��bean���Ͷ��������ͼ��ȡ�����ڵ�bean�����򷵻� null��
     */
    public Class<?> getBeanType(String name) throws NoDefinitionException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊԭ��ģʽ���������Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isPrototype(String name) throws NoDefinitionException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ��̬ģʽ���������Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isSingleton(String name) throws NoDefinitionException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ����ģʽ���������Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isFactory(String name) throws NoDefinitionException;
    /**
     * ����ĳ��Bean�������Ƿ�Ϊָ�����ͣ������������͡����Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @param targetType Ҫ���Ե���������
     * @return ���ز��Խ������������Ե�Ŀ�����Ϳ���ת��Ϊָ�����Ͷ����򷵻�true,���򷵻�false��
     */
    public boolean isTypeMatch(String name, Class<?> targetType) throws NoDefinitionException;
    /**��ʼ�� */
    public void init() throws Exception;
    /**����*/
    public void destroy() throws Exception;
}