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
import java.util.Map;
import org.more.util.attribute.IAttribute;
/**
 * submit�ĺ��Ľӿڣ��κ�action�ĵ��ö���ͨ������ӿڽ��еġ�
 * @version 2009-12-1
 * @author ������ (zyc@byshell.org)
 */
public interface SubmitContext extends IAttribute {
    /** ��ȡ�Ѿ������Action�����ϡ�*/
    public String[] getActionNames();
    /** ��ȡһ��ָ����Action���ͣ�����Ϊaction����*/
    public Class<?> getActionType(String actionName);
    /**
     * ִ�е���action�Ĵ�����̣����action�����˹�������װ���������֮����ִ�С�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doAction(String invokeString) throws Throwable;
    /**
     * ִ�е���action�Ĵ�����̣����action�����˹�������װ���������֮����ִ�У��÷����ṩ��һ����action���ݲ�����֧�֡�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @param session action����ʱʹ�õĻỰ���Ự����һ�����ݻ�������
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doAction(String invokeString, Session session, Map<String, ?> params) throws Throwable;
    /**
     * ����action�����õ�action�Ǵ�����event�ģ�������˴��ж�ջ�ͻỰ��Ϣ��ע�⣺�÷���ֻ�е���actionִ���ڼ���òŻᷢ�����á�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @param event ���нӵĶ�ջ����
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doActionOnStack(String invokeString, ActionStack stack, Map<String, ?> params) throws Throwable;
}