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
import org.more.hypha.define.BeanDefine;
/**
* ����ӿ���Bean�����ĺ��Ľӿڣ�{@link ApplicationContext}���ض��ӿ�ʵ�����������ĳЩ���Ρ�<br/><br/>
* ��{@link ApplicationContext}������Bean����Ψһ��һ�����ơ��ù���������һ�����������һ������ʵ��(ԭ�����ģʽ)���򵥸�
* ����ʵ��(Singleton���ģʽ����ʵ�����ڵ�ǰ�����е�һ����̬���������������͵�ʵ��ȡ����bean�����á�<br/><br/>
* ���ڹ����е�beanͨ���Ǵ�����XML�ļ��С������ų�bean����Դ��DBMS����LDAP���ⶼȡ����{@link ApplicationContext}��bean����Դ�ӿ�������ṩ���ݵġ�
* @version 2011-2-25
* @author ������ (zyc@byshell.org)
*/
public interface ApplicationContext {
    /**��ȡӦ��������IDֵ��*/
    public String getID();
    /**
     * ��ȡ{@link ApplicationContext}�п���������������bean�������Ƽ��ϣ������ȡ�����κ���������Ҫ����һ���ռ��ϡ�
     * @return ���ػ�ȡ��������bean�������Ƽ��ϡ�
     */
    public List<String> getBeanDefinitionIDs();
    /**
     * ��ȡbean�Ķ��壬��Bean�Ķ�����ֻ�������ڵ�ǰbean��Ԫ��Ϣ��
     * �����ǰbean������ע����Ҫ��������bean���ȡ����bean�Ķ�����Ҫ���µ���getBeanDefinition�������л�ȡ��
     * @param id Ҫ��ȡbean�����bean���ơ�
     * @return ����bean���壬�����ȡ����ָ����bean�����򷵻�null��
     */
    public BeanDefine getBeanDefinition(String id);
    /**
    * ��ȡһ����װ������org.more.hypha�е���װ�ؾ���ͨ�������װ��������װ�صġ�
    * @return ����һ����װ������org.more.hypha�е���װ�ؾ���ͨ�������װ��������װ�صġ�
    */
    public ClassLoader getClassLoader();
    /**
     * ����Ƿ����ĳ�����Ƶ�Bean��
     * @param id Ҫ��ȡ��Bean id��
     * @return ���ؼ������������ڷ���true���򷵻�false��
     */
    public boolean containsBean(String id);
    /**
     * ��ȡĳ��Bean��ʵ�����󣬸�ʵ��������ʱ����������þ����䴴����ԭ��ģʽ���ǵ�̬ģʽ�� 
     * ������bean�����Ե�����ע��������ע��Ҳ���ڴ���ʱ���С�
     * @param id Ҫ��ȡ��beanʵ��id��
     * @return ���ػ��߷��ش�������ʵ����
     */
    public <T> T getBean(String id);
    /**
     * ��ȡĳ��Bean��ʵ�����󣬸�ʵ��������ʱ����������þ����䴴����ԭ��ģʽ���ǵ�̬ģʽ�� 
     * ������bean�����Ե�����ע��������ע��Ҳ���ڴ���ʱ���С�
     * @param id Ҫ��ȡ��beanʵ��id��
     * @param objects �ڻ�ȡbeanʵ��ʱ���ܻᴫ�ݵĲ�����Ϣ��
     * @return ���ػ��߷��ش�������ʵ����
     */
    public <T> T getBean(String id, Object... objects);
    /**
     * ����Bean���ƻ�ȡ��bean���ͣ��÷�����������bean���������õ�bean���͡�
     * ��ôgetBeanType�������������ɵ��������Ͷ���
     * @param id Ҫ��ȡ��Bean id��
     * @param objects �ڻ�ȡbean����ʱ���ܻᴫ�ݵĲ�����Ϣ��
     * @return ����Ҫ��ȡ��bean���Ͷ��������ͼ��ȡ�����ڵ�bean�����򷵻� null��
     */
    public Class<?> getBeanType(String id, Object... objects);
    /**
     * ����ĳ����Bean�Ƿ�Ϊԭ��ģʽ���������Ŀ��bean�������򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     * @throws NoDefineBeanException ���Ҫ���Ե�Ŀ��bean���������������쳣��
     */
    public boolean isPrototype(String id) throws NoDefineBeanException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ��̬ģʽ���������Ŀ��bean�������򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     * @throws NoDefineBeanException ���Ҫ���Ե�Ŀ��bean���������������쳣��
     */
    public boolean isSingleton(String id) throws NoDefineBeanException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ����ģʽ���������Ŀ��bean�������򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     * @throws NoDefineBeanException ���Ҫ���Ե�Ŀ��bean���������������쳣��
     */
    public boolean isFactory(String id) throws NoDefineBeanException;
    /**
     * ����ָ��id��bean�����Ƿ����ת����ΪtargetType������ʾ�����͡���������򷵻�true�����򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @param targetType Ҫ���Ե���������
     * @return ���ز��Խ�������ָ���������Ǳ����Ե�bean�ĸ����򷵻�true,���򷵻�false��
     */
    public boolean isTypeMatch(String id, Class<?> targetType);
    /**��ʼ��{@link ApplicationContext}�ӿڣ��÷���һ��Ҫ��destroy֮ǰִ�С� */
    public void init();
    /**����{@link ApplicationContext}�ӿڡ�*/
    public void destroy();
    /*------------------------------------------------------------------------------*/
    /**��ȡӦ�õ������Ļ�������*/
    public Object getContextObject();
    /**����Ӧ�õ������Ļ�������*/
    public void setContextObject(Object contextObject);
};