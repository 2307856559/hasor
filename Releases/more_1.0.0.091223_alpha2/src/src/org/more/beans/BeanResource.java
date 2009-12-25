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
package org.more.beans;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import org.more.DoesSupportException;
import org.more.beans.info.BeanDefinition;
/**
 * ����ӿ���more.beans�齨�Ļ����ӿ�֮һ���ýӿ������ṩ{@link BeanDefinition}��������ȡ���ܡ�
 * �ӿ�ʵ�������bean��������ʲô��ʽ���ڣ�DBMS��LDAP��XML��Щ���������ṩ��ʽ����
 * �����߿���ʹ��Spring��getMergedBeanDefinition����ʹ��Spring��Ϊbean�����ṩ�ߡ�
 * Date : 2009-11-3
 * @author ������
 */
public interface BeanResource {
    /**��ȡ��Դ����*/
    public String getSourceName();
    /**��ȡ��Դ��URI������ʽ�������Դ��֧�ָñ�����ʽ�򷵻�null��*/
    public URI getSourceURI();
    /**��ȡ��Դ��URL������ʽ�������Դ��֧�ָñ�����ʽ�򷵻�null��*/
    public URL getSourceURL();
    /**��ȡ��Դ��File������ʽ�������Դ��֧�ָñ�����ʽ�򷵻�null��*/
    public File getSourceFile();
    /**
     * ��ȡbean�Ķ��壬��Bean�Ķ�����ֻ�������ڵ�ǰbean��Ԫ��Ϣ��
     * �����ǰbean������ע����Ҫ��������bean���ȡ����bean�Ķ�����Ҫ���µ���getBeanDefinition�������л�ȡ��
     * @param name Ҫ��ȡbean�����bean���ơ�
     * @return ����bean���壬�����ȡ����ָ����bean�����򷵻�null��
     */
    public BeanDefinition getBeanDefinition(String name);
    /**
     * ����Ƿ����ĳ�����Ƶ�Bean���壬�������Ŀ��bean�����򷵻�true�����򷵻�false��
     * @param name Ҫ����Bean�������ơ�
     * @return ���ؼ������������ڷ���true���򷵻�false��
     */
    public boolean containsBeanDefinition(String name);
    /**
     * ��ȡ{@link BeanResource}�п���������������bean�������Ƽ��ϣ������ȡ�����κ���������Ҫ����һ���ռ��ϡ�
     * @return ���ػ�ȡ��������bean�������Ƽ��ϡ�
     */
    public List<String> getBeanDefinitionNames();
    /**
     * ��ȡһ��bean���������Ӽ��ϣ����з��ϵ�bean����Ҫ�����ʼ����bean���塣
     * @return ����һ��bean���������Ӽ��ϣ����з��ϵ�bean����Ҫ�����ʼ����bean���塣
     */
    public List<String> getStrartInitBeanDefinitionNames();
    /**
     * ��ȡ������Դϵͳ�����е�������Ϣ��������������������Դϵͳ������ӿڶ���֧�ָ÷����������DoesSupportException�쳣��
     * @param key ��ȡ������KEY��
     * @return ���ػ�ȡBeanResource��������Ϣ��
     * @throws DoesSupportException �ӿڶ���֧�ָ÷�����
     */
    public Object getAttribute(String key) throws DoesSupportException;
    /**
     * ��ȡ{@link BeanResource}�ӿڶ�Bean��Դ��������
     * @return ��ȡ{@link BeanResource}�ӿڶ�Bean��Դ��������
     */
    public String getResourceDescription();
    /**
     * ��ȡһ��boolean��ֵ��ʾ{@link BeanResource}�����Ƿ��Bean�Ķ�����Ϣ�����档���ʵ�����ṩ�˻��湦���򷵻�true�����򷵻�false��
     * @return ��ȡһ��boolean��ֵ��ʾ{@link BeanResource}�����Ƿ��Bean�Ķ�����Ϣ�����档
     */
    public boolean isCacheBeanMetadata();
    /** 
     * ������л����Bean������Ϣ������ӿ�ʵ�ֶ���֧�ָ÷����������DoesSupportException�쳣��
     * @throws DoesSupportException �ӿڶ���֧�ָ÷�����
     */
    public void clearCache() throws DoesSupportException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊԭ��ģʽ���������Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isPrototype(String name);
    /**
     * ����ĳ����Bean�Ƿ�Ϊ��̬ģʽ���������Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isSingleton(String name);
    /**
     * ����ĳ����Bean�Ƿ�Ϊ����ģʽ���������Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    public boolean isFactory(String name);
}
