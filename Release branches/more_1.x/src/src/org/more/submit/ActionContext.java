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
import java.util.Iterator;
import org.more.core.error.ExistException;
/**
 * �ýӿڸ��𴴽����ҷ���{@link ActionInvoke}���󡣳���֮��action���������ڹ���Ҳ�ɸýӿ��ṩ��
 * ��Ŀǰ�汾�ݲ�֧���������ڹ���
 * @version 2010-7-27
 * @author ������ (zyc@byshell.org)
 */
public interface ActionContext {
    /**����ĳ��Action�����Ƿ���ڣ�������Ҫ���Ե�action��������������ڷ���true���򷵻�false��*/
    public boolean containsAction(String actionName);
    /**
     * ���Ҳ��ҷ���Ŀ��Action��������޷����һ��߲���action����Ŀ��ʧ���������{@link ExistException}�쳣��
     * <br/>�÷�������������������л��ڡ�
     * @param actionName action��������
     * @param invoke action�����������
     * @return ���ز��ҵ���action���ö���
     * @throws ExistException ���Ҫ��ȡ��action�����ڻ����Ҳ�������������쳣��
     */
    public ActionInvoke findAction(String actionName, String invoke) throws ExistException;
    /** ��ȡһ��ָ����Action���ͣ�����Ϊaction��������*/
    public Class<?> getActionType(String actionName);
    /** ͨ��������������Ե�������Action��������*/
    public Iterator<String> getActionNameIterator();
    /**������������action����ȡָ����Action��*/
    public Object getActionProperty(String actionName, String property);
};