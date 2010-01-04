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
import org.more.NoDefinitionException;
/**
 * ���ฺ�𴴽����ҷ���{@link ActionInvoke action���ö���}��action���ö��������{@link ActionFilter action������}
 * Ҳ������Ŀ��action����ͬʱҲ������Ҳ��ҷ���action�Ĺ��������󣬴�������л�ȡaction���Ե�Ҫ��Ҳͨ���ýӿ�ʵ�֡�
 * <br/>Date : 2009-11-28
 * @author ������
 */
public interface ActionContext {
    /**
     * ���ָ����action�����ַ����Ƿ���Ա�֧�֣������֧���򷵻�true���򷵻�false��
     * @param actionName Ҫ���ҵ�action����
     * @return ���ָ����action�����ַ����Ƿ���Ա�֧�֣������֧���򷵻�true���򷵻�false��
     */
    public boolean containsAction(String actionName);
    /**
     * ���Ҳ��ҷ���invokeString�ַ�������ʾ���Ǹ�action���÷�װ��findAction����������װ����action��������
     * ����޷����һ��߲���action����Ŀ��ʧ���������NoDefinitionException�쳣��
     * @param actionName Ҫ���õ�action����
     * @param invoke Ҫ���õ�action��������
     * @return ���ز��ҵ���action���ö���
     * @throws NoDefinitionException �������δ�����쳣��
     */
    public ActionInvoke findAction(String actionName, String invoke) throws NoDefinitionException;
    /**
     * ����ActionInvoke���ö����˽�й����������û��װ��˽�й�������ԭ�����ص��ö���
     * @param actionName Ҫװ���action���������á�
     * @param invokeObject �ȴ�װ��������ĵ��ö���
     * @return ����װ���ĵ��ö���
     */
    public ActionInvoke configPrivateFilter(String actionName, ActionInvoke invokeObject);
    /**
     * ����ActionInvoke���ö���Ĺ��й����������û��װ��˽�й�������ԭ�����ص��ö���
     * @param actionName Ҫװ���action���������á�
     * @param invokeObject �ȴ�װ��������ĵ��ö���
     * @return ����װ���ĵ��ö���
     */
    public ActionInvoke configPublicFilter(String actionName, ActionInvoke invokeObject);
    /** ��ȡ�Ѿ������Action�����ϡ�*/
    public String[] getActionNames();
    /** ��ȡһ��ָ����Action���ͣ�����Ϊaction����*/
    public Class<?> getActionType(String actionName);
}