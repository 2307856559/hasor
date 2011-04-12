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
import java.io.IOException;
import java.util.List;
import org.more.ClassFormatException;
import org.more.DoesSupportException;
import org.more.NoDefinitionException;
import org.more.util.attribute.IAttribute;
/**
 * ����ӿ���More��Bean�����ĺ��Ľӿڣ�{@link ApplicationContext}���ض��ӿ�ʵ�����������ĳЩ���Ρ�<br/><br/>
 * ��{@link ApplicationContext}������Bean����Ψһ��һ�����ơ��ù���������һ�����������һ������ʵ��(ԭ�����ģʽ)���򵥸�
 * ����ʵ��(Singleton���ģʽ����ʵ�����ڵ�ǰ�����е�һ����̬���������������͵�ʵ��ȡ����bean�����á�<br/><br/>
 * ���ڹ����е�beanͨ���Ǵ�����XML�ļ��С������ų�bean����Դ��DBMS����LDAP���ⶼȡ����{@link ApplicationContext}��bean����Դ�ӿ�������ṩ���ݵġ�
 * @version 2011-2-25
 * @author ������ (zyc@byshell.org)
 */
public interface ApplicationContext extends IAttribute {
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
     * @throws NoDefinitionException ���Ҫ��ȡ��bean���岻�����������{@link NoDefinitionException}�쳣��
     */
    public AbstractBeanDefine getBeanDefinition(String id) throws NoDefinitionException;
    /**
     * ��ȡ{@link ApplicationContext}��ʹ�õ�Bean������Դ������Դ��������ṩ�й�Bean������Ϣ��
     * @return ����{@link ApplicationContext}��ʹ�õ�Bean������Դ������Դ��������ṩ�й�Bean������Ϣ��
     */
    public DefineResource getBeanResource();
    /**
    * ��ȡһ����װ������org.more.hypha�е���װ�ؾ���ͨ�������װ��������װ�صġ�
    * @return ����һ����װ������org.more.hypha�е���װ�ؾ���ͨ�������װ��������װ�صġ�
    */
    public ClassLoader getBeanClassLoader();
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
     * @param objects �ڻ�ȡbeanʵ��ʱ���ܻᴫ�ݵĲ�����Ϣ��
     * @return ���ػ��߷��ش�������ʵ����
     */
    public Object getBean(String id, Object... objects) throws Throwable;
    /**
     * ����Bean���ƻ�ȡ��bean���ͣ��÷�����������bean���������õ�bean���͡�
     * ��ôgetBeanType�������������ɵ��������Ͷ���
     * @param id Ҫ��ȡ��Bean id��
     * @return ����Ҫ��ȡ��bean���Ͷ��������ͼ��ȡ�����ڵ�bean�����򷵻� null��
     */
    public Class<?> getBeanType(String id) throws DoesSupportException, IOException, ClassFormatException, ClassNotFoundException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊԭ��ģʽ���������Ŀ��bean�������򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isPrototype(String id) throws NoDefinitionException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ��̬ģʽ���������Ŀ��bean�������򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isSingleton(String id) throws NoDefinitionException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ����ģʽ���������Ŀ��bean�������򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isFactory(String id) throws NoDefinitionException;
    /**
     * ����ָ��id��bean�����Ƿ����ת����ΪtargetType������ʾ�����͡���������򷵻�true�����򷵻�false��
     * @param id Ҫ���Ե�Bean id��
     * @param targetType Ҫ���Ե���������
     * @return ���ز��Խ�������ָ���������Ǳ����Ե�bean�ĸ����򷵻�true,���򷵻�false��
     */
    public boolean isTypeMatch(String id, Class<?> targetType) throws Throwable;
    /**��ʼ��{@link ApplicationContext}�ӿڡ� */
    public void init() throws Throwable;
    /**����{@link ApplicationContext}�ӿڡ�*/
    public void destroy() throws Throwable;
    /**��ȡӦ�õ������Ļ�������*/
    public Object getContext();
    /**��ȡ�¼���������ͨ���ù��������Է����¼����¼��ļ���Ҳ��ͨ������ӿڶ�����ɵġ�*/
    public AbstractEventManager getEventManager();
    /**��ȡ��չ���������ͨ����չ����������Լ�����ע����߽��ע����չ�㡣�й���չ��Ĺ�����μ�{@link ExpandPoint}*/
    public AbstractExpandPointManager getExpandPointManager();
    /**��ȡELִ������*/
    public ELContext getELContext();
}