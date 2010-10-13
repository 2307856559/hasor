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
import java.net.URI;
import java.util.List;
import org.more.NoDefinitionException;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.util.attribute.IAttribute;
/**
 * ����ӿ���more.beans�齨�Ļ����ӿ�֮һ���ýӿ������ṩ{@link AbstractBeanDefine}��������ȡ���ܡ�
 * �ӿ�ʵ�������bean��������ʲô��ʽ���ڣ�DBMS��LDAP��XML��Щ���������ṩ��ʽ����
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public interface DefineResource extends DefineResourcePluginSet {
    ///**���ݵ�ǰ������Ϣ����һ��{@link ApplicationContext}����ӿڡ�*/
    //public ApplicationContext buildApplication();
    /**��ȡ{@link DefineResource}�����Է��ʽӿڡ�*/
    public IAttribute getAttribute();
    /**��ȡ�¼���������*/
    public EventManager getEventManager();
    /**��ȡ��Դ����*/
    public String getSourceName();
    /**��ȡ��Դ��URI������ʽ�������Դ��֧�ָñ�����ʽ�򷵻�null��*/
    public URI getSourceURI();
    /**
     * ��ȡbean�Ķ��壬��Bean�Ķ�����ֻ�������ڵ�ǰbean��Ԫ��Ϣ��
     * @param name Ҫ��ȡbean�����bean���ơ�
     * @return ����bean���壬�����ȡ����ָ����bean�����򷵻�null��
     */
    public AbstractBeanDefine getBeanDefine(String name) throws NoDefinitionException;
    /**
     * ����Ƿ����ĳ�����Ƶ�Bean���壬�������Ŀ��bean�����򷵻�true�����򷵻�false��
     * @param name Ҫ����Bean�������ơ�
     * @return ���ؼ������������ڷ���true���򷵻�false��
     */
    public boolean containsBeanDefine(String name);
    /**
     * ��ȡ{@link DefineResource}�п���������������bean�������Ƽ��ϣ������ȡ�����κ���������Ҫ����һ���ռ��ϡ�
     * @return ���ػ�ȡ��������bean�������Ƽ��ϡ�
     */
    public List<String> getBeanDefineNames();
    /**
     * ����ĳ����Bean�Ƿ�Ϊԭ��ģʽ���������Ŀ��bean�������������{@link NoDefinitionException}�쳣��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isPrototype(String name) throws NoDefinitionException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ��̬ģʽ���������Ŀ��bean�������������{@link NoDefinitionException}�쳣��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isSingleton(String name) throws NoDefinitionException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ����ģʽ���������Ŀ��bean�������������{@link NoDefinitionException}�쳣��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isFactory(String name) throws NoDefinitionException;
    /**��ʼ��{@link DefineResource}�ӿڣ�����ظ�����init��������һЩ�����쳣��*/
    public void init() throws Exception;
    /**����BeanResource�ӿڡ�*/
    public void destroy() throws Exception;
    /** ����Ѿ���ʼ����ִ��������ִ�г�ʼ����*/
    public void reload() throws Exception;
};