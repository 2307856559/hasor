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
package org.more.submit;
/**
 * Action�����ӿڣ��ýӿڵ�ʵ����Ӧ�������action����Ĵ������Լ�����action�����filterװ�ء�������submit�л���һ��action������
 * @version 2009-6-25
 * @author ������ (zyc@byshell.org)
 */
public interface ActionFactory {
    /**
     * ���Ҳ�����ָ�����Ƶ�Action���󣬵��Ҳ���ָ�����Ƶ�Action�����򷵻�null��
     * @param name Ҫ���ҵ�action��
     * @return �����ҵ���Action��������Ҳ���ָ�����Ƶ�Action�����򷵻�null��
     */
    public Object findAction(String name);
    /**
     * ���Ҳ�����ָ��action��ĳһ�����ԣ����Ҳ���ʱ�򷵻�null��
     * @param name Ҫ���ҵ�action��
     * @param propName Ҫ���ҵ�������
     * @return ���ز��Ҳ�����ָ��action��ĳһ�����ԣ����Ҳ���ʱ�򷵻�null��
     */
    public Object findActionProp(String name, String propName);
    /**
     * ���Ҳ�����ָ�����Ƶ�Action���󣬵��Ҳ���ָ�����Ƶ�Action�����򷵻�flase�����򷵻�true��
     * @param name Ҫ���ҵ�action��
     * @return ���ز���Action�Ľ��������Ҳ���ָ�����Ƶ�Action���󷵻�false���򷵻�true��
     */
    public boolean containsAction(String name);
    /**
     * ����ָ����action����ȡ���action�ϵĹ����������ϣ����������Ѿ����չ���������˳����д������
     * @param actionName action��
     * @return ���ظ���ָ����action����ȡ���action�ϵĹ����������ϣ����������Ѿ����չ���������˳����д������
     */
    public String[] getActionFilterNames(String actionName);
    /**
     * ��ȡFactory������Action�����Ƽ��ϡ�
     * @return ����Factory������Action�����Ƽ��ϡ�
     */
    public String[] getActionNames();
    /**
     * ��ȡBean��Class���͡�
     * @param name Ҫ��ȡ��Bean����
     * @return ����Bean��Class���͡�
     */
    public Class<?> getType(String name);
    /**
     * ����Bean�Ƿ�Ϊprototype�����Ŀ��Action��prototype�򷵻�true���򷵻�false��
     * @param name Ҫ���Ե�Bean��
     * @return ���ز��Խ������Bean�Ƿ�Ϊprototype�����Ŀ��Action��prototype�򷵻�true���򷵻�false��
     */
    public boolean isPrototype(String name);
    /**
     * ����Bean�Ƿ�Ϊsingleton�����Ŀ��Action��singleton�򷵻�true���򷵻�false��
     * @param name Ҫ���Ե�Bean��
     * @return ���ز��Խ������Bean�Ƿ�Ϊsingleton�����Ŀ��Action��singleton�򷵻�true���򷵻�false��
     */
    public boolean isSingleton(String name);
}