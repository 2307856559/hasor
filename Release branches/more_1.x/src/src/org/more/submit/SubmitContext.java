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
import java.util.Map;
import org.more.util.attribute.IAttribute;
/**
 * submit�ĺ��Ľӿڣ��κ�action�ĵ��ö���ͨ������ӿڽ��еġ��ýӿ��ṩ�˻�ȡ����sessio�������ķ����͵���action�ķ�����
 * @version : 2010-7-27
 * @author ������(zyc@byshell.org)
 */
public interface SubmitContext extends IAttribute {
    /** ��ȡActionContext����*/
    public ActionContext getActionContext();
    /** ͨ��������������Ե�������Action��Դ���õ�������ɨ��ÿ��action����ķ��񷽷���*/
    public Iterator<String> getActionInvokeStringIterator();
    /**�������SubmitContext�Ƿ���һ��֧��web������SubmitContext��*/
    public boolean isWebContext();
    /**��ȡsubmitContext��ʹ�õ�Session��������submit��session��������������ά��session��*/
    public SessionManager getSessionManager();
    /**�滻���е�session������ʹ���û��Զ����һ��session��������*/
    public void setSessionManager(SessionManager sessionManager);
    /**
     * ִ�е���action�Ĵ�����̡�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doAction(String invokeString) throws Throwable;
    /**
    * ִ�е���action�Ĵ�����̣��÷����ṩ��һ����action���ݲ�����֧�֡�
    * @param invokeString ����action��ʹ�õĵ����ַ�����
    * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
    * @return ���ش�������
    * @throws Throwable �����ִ��action�ڼ䷢���쳣��
    */
    public Object doAction(String invokeString, Map<String, ?> params) throws Throwable;
    /**
     * ִ�е���action�Ĵ�����̣��÷����ṩ��һ����action���ݲ�����֧�֡�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @param sessionID action����ʱʹ�õĻỰid���Ự����һ�����ݻ�������
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doAction(String invokeString, String sessionID, Map<String, ?> params) throws Throwable;
    /**
     * ִ�е���action�Ĵ�����̣��÷����ṩ��һ����action���ݲ�����֧�֡�
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
     * @param stack ���нӵĶ�ջ����
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doActionOnStack(String invokeString, ActionStack stack, Map<String, ?> params) throws Throwable;
};