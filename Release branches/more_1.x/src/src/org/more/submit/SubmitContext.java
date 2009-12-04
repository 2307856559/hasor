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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.FormatException;
import org.more.NoDefinitionException;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * submit3.0�ĺ��Ľӿڣ��κ�action�ĵ��ö���ͨ������ӿڽ��еġ�
 * <br/>Date : 2009-12-1
 * @author ������
 */
public class SubmitContext implements IAttribute {
    //========================================================================================Field
    private static final long serialVersionUID = 3966376070855006285L;
    private ActionContext     actionContext    = null;
    private IAttribute        contextAtt       = new AttBase();
    //==================================================================================Constructor
    SubmitContext(ActionContext actionContext) {
        this.actionContext = actionContext;
    }
    //==========================================================================================Job
    /**��ȡSubmitContext���ڱ������Ե����Ա�������*/
    public IAttribute getContextAtt() {
        return contextAtt;
    }
    /**����SubmitContext���ڱ������Ե����Ա�������������ò���Ϊ���������NullPointerException�쳣��*/
    public void setContextAtt(IAttribute contextAtt) {
        if (contextAtt == null)
            throw new NullPointerException("SubmitContext�����ܿ����Ա�������");
        this.contextAtt = contextAtt;
    }
    /** ��ȡ�Ѿ������Action�����ϡ�*/
    public String[] getActionNames() {
        return actionContext.getActionNames();
    };
    /** ��ȡһ��ָ����Action���ͣ�����Ϊaction����*/
    public Class<?> getActionType(String actionName) {
        return actionContext.getActionType(actionName);
    };
    /**
     * ִ�е���action�Ĵ�����̣����action�����˹�������װ���������֮����ִ�С�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doAction(String invokeString) throws Throwable {
        return this.doAction(invokeString, null, new HashMap<String, Object>());
    };
    /**
     * ִ�е���action�Ĵ�����̣����action�����˹�������װ���������֮����ִ�У��÷����ṩ��һ����action���ݲ�����֧�֡�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @param session action����ʱʹ�õĻỰ���Ự����һ�����ݻ�������
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doAction(String invokeString, Session session, Object... params) throws Throwable {
        HashMap<String, Object> vars = new HashMap<String, Object>();
        for (Integer i = 0; i < params.length; i++)
            vars.put(i.toString(), params[i]);
        return this.doAction(invokeString, session, vars);
    };
    /**
     * ִ�е���action�Ĵ�����̣����action�����˹�������װ���������֮����ִ�У��÷����ṩ��һ����action���ݲ�����֧�֡�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @param session action����ʱʹ�õĻỰ���Ự����һ�����ݻ�������
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doAction(String invokeString, Session session, Map<String, ?> params) throws Throwable {
        //һ����������ȡ��ջ����
        ActionStack stack = this.parseInvokeString(invokeString, new ActionStack(null, session, this));
        String actionName = stack.getActionName();
        if (this.actionContext.containsAction(actionName) == false)
            throw new NoDefinitionException("�Ҳ���action[" + actionName + "]�Ķ��塣");
        //
        if (params != null)
            for (String key : params.keySet())
                stack.setAttribute(key, params.get(key));
        //������ȡ���ö���
        ActionInvoke invoke = this.actionContext.findAction(actionName, stack.getActionMethod());
        //��������˽�й�����
        invoke = this.actionContext.configPrivateFilter(actionName, invoke);
        //�������ù��й�����
        invoke = this.actionContext.configPublicFilter(actionName, invoke);
        //�ġ�ִ�е���
        return invoke.invoke(stack);
    };
    /**
     * ����action�����õ�action�Ǵ�����event�ģ�������˴��ж�ջ�ͻỰ��Ϣ��ע�⣺�÷���ֻ�е���actionִ���ڼ���òŻᷢ�����á�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @param event ���нӵĶ�ջ����
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doActionOnStack(String invokeString, ActionStack stack, Object... params) throws Throwable {
        HashMap<String, Object> vars = new HashMap<String, Object>();
        for (Integer i = 0; i < params.length; i++)
            vars.put(i.toString(), params[i]);
        return this.doActionOnStack(invokeString, stack, vars);
    };
    /**
     * ����action�����õ�action�Ǵ�����event�ģ�������˴��ж�ջ�ͻỰ��Ϣ��ע�⣺�÷���ֻ�е���actionִ���ڼ���òŻᷢ�����á�
     * @param invokeString ����action��ʹ�õĵ����ַ�����
     * @param event ���нӵĶ�ջ����
     * @param params ����actionĿ������action���ݵĲ������ò����ڶ�ջ�б��档
     * @return ���ش�������
     * @throws Throwable �����ִ��action�ڼ䷢���쳣��
     */
    public Object doActionOnStack(String invokeString, ActionStack stack, Map<String, ?> params) throws Throwable {
        if (stack == null)
            throw new NullPointerException("����stack����Ϊ�ա�");
        //һ����������ȡ��ջ����
        ActionStack newStack = this.parseInvokeString(invokeString, new ActionStack(stack, stack.getSession(), this));
        String actionName = newStack.getActionName();
        if (this.actionContext.containsAction(actionName) == false)
            throw new NoDefinitionException("�Ҳ���action[" + actionName + "]�Ķ��塣");
        //
        if (params != null)
            for (String key : params.keySet())
                newStack.setAttribute(key, params.get(key));
        //������ȡ���ö���
        ActionInvoke invoke = this.actionContext.findAction(actionName, newStack.getActionMethod());
        //��������˽�й�����
        invoke = this.actionContext.configPrivateFilter(actionName, invoke);
        //�ġ�ִ�е���
        return invoke.invoke(newStack);
    };
    /***/
    private ActionStack parseInvokeString(String invokeString, ActionStack stack) throws FormatException {
        String regex = "(.*)\\.(.*)";
        if (Pattern.matches(regex, invokeString) == false)
            throw new FormatException("invokeString ��ʽ����");;
        //����action���ñ��ʽ�����һ�ø������ֲ���
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(invokeString);
        m.find();
        String actionName = m.group(1);
        String actionMethod = m.group(2);
        //
        stack.setInvokeString(invokeString);
        stack.setActionName(actionName);
        stack.setActionMethod(actionMethod);
        return stack;
    }
    //==========================================================================================Att
    @Override
    public void clearAttribute() {
        this.contextAtt.clearAttribute();
    }
    @Override
    public boolean contains(String name) {
        return this.contextAtt.contains(name);
    }
    @Override
    public Object getAttribute(String name) {
        return this.contextAtt.getAttribute(name);
    }
    @Override
    public String[] getAttributeNames() {
        return this.contextAtt.getAttributeNames();
    }
    @Override
    public void removeAttribute(String name) {
        this.contextAtt.removeAttribute(name);
    }
    @Override
    public void setAttribute(String name, Object value) {
        this.contextAtt.setAttribute(name, value);
    }
}