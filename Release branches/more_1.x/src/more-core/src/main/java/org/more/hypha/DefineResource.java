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
import org.more.util.attribute.IAttribute;
/**
 * ����ӿ���more.hypha�齨�Ļ����ӿ�֮һ���ýӿ������ṩ{@link AbstractBeanDefine}��������ȡ���ܡ�
 * �ӿ�ʵ�������bean��������ʲô��ʽ���ڣ�DBMS��LDAP��XML��Щ���������ṩ��ʽ�����ýӿ�ֻ���¼�֧�֡�
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public interface DefineResource extends IAttribute {
    /**��ȡһ��״̬��״̬�����Ƿ��Ѿ�׼���á�*/
    public boolean isReady();
    /**��ȡ��Դ����*/
    public String getSourceName();
    /**
     * ��ȡbean�Ķ��壬��Bean�Ķ�����ֻ�������ڵ�ǰbean��Ԫ��Ϣ��
     * @param id Ҫ��ȡbean�����id��
     * @return ����bean���壬�����ȡ����ָ����bean�����򷵻�null��
     */
    public AbstractBeanDefine getBeanDefine(String id);
    /**
     * ���һ��bean���塣ʹ�ø÷�����ӵĶ�����{@link DefineResource}�ӿ�����ʱ�ᶪʧ��
     * @param define Ҫ��ӵ�bean���塣
     * @throws NullPointerException �������Ϊ�����������쳣��
     * @throws RepeateBeanException �����ͼ���һ��ͬid��Bean�������������쳣��
     */
    public void addBeanDefine(AbstractBeanDefine define) throws NullPointerException, RepeateBeanException;
    /**
     * ����Ƿ����ĳ�����Ƶ�Bean���壬�������Ŀ��bean�����򷵻�true�����򷵻�false��
     * @param id Ҫ����Bean����id��
     * @return ���ؼ������������ڷ���true���򷵻�false��
     */
    public boolean containsBeanDefine(String id);
    /**
     * ��ȡ{@link DefineResource}�п���������������bean�������Ƽ��ϣ�
     * �����ȡ�����κ���������Ҫ����һ���ռ��ϡ����صļ�����һ��ֻ�����ϡ�
     * @return ���ػ�ȡ��������bean�������Ƽ��ϡ�
     */
    public List<String> getBeanDefinitionIDs();
    /**
     * ����ĳ����Bean�Ƿ�Ϊԭ��ģʽ������ԭ��ģʽ��ָbean�������ڹ�����ʽ����Ҳû�����õ�̬���ԡ�
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     * @throws NoDefineBeanException ���Ҫ���Ե�Ŀ��bean���������������쳣��
     */
    public boolean isPrototype(String id) throws NoDefineBeanException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ��̬ģʽ���������Ŀ��bean�������������{@link NoDefineBeanException}�쳣��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     * @throws NoDefineBeanException ���Ҫ���Ե�Ŀ��bean���������������쳣��
     */
    public boolean isSingleton(String id) throws NoDefineBeanException;
    /**
     * ����ĳ����Bean�Ƿ�Ϊ����ģʽ���������Ŀ��bean�������������{@link NoDefineBeanException}�쳣��
     * @param id Ҫ���Ե�Bean id��
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     * @throws NoDefineBeanException ���Ҫ���Ե�Ŀ��bean���������������쳣��
     */
    public boolean isFactory(String id) throws NoDefineBeanException;
    /**�������װ�ص�Bean�������*/
    public void clearDefine();
    /**��ȡ�¼���������ͨ���ù��������Է����¼����¼��ļ���Ҳ��ͨ������ӿڶ�����ɵġ�*/
    public EventManager getEventManager();
};