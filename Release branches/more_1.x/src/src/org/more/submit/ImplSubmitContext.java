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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.more.FormatException;
import org.more.NoDefinitionException;
import org.more.util.attribute.AttBase;
/**
 * SubmitContext�ӿڵ�ʵ���ࡣImplSubmitContext��createActionStack�������Ըı�Action�����ľ������͡�
 * <br/>Date : 2009-12-1
 * @author ������
 */
public class ImplSubmitContext extends AttBase implements SubmitContext {
    //========================================================================================Field
    private static final long serialVersionUID = 3966376070855006285L;
    private ActionContext     actionContext    = null;
    //==================================================================================Constructor
    /**����һ��Submit3.0���л�����*/
    public ImplSubmitContext(ActionContext actionContext) {
        if (actionContext == null)
            throw new NullPointerException("ActionContext���Ͳ�������Ϊ�ա�");
        this.actionContext = actionContext;
    }
    //==========================================================================================Job
    /** ��ȡ�Ѿ������Action�����ϡ�*/
    public String[] getActionNames() {
        return actionContext.getActionNames();
    };
    /** ��ȡһ��ָ����Action���ͣ�����Ϊaction����*/
    public Class<?> getActionType(String actionName) {
        return actionContext.getActionType(actionName);
    };
    /**�÷������𴴽����ҳ�ʼ��ActionStack����SubmitContext���������ͨ����д�÷�������չActionStack�Ĺ��ܡ�*/
    protected ActionStack createActionStack(ActionStack parent, Session session, ImplSubmitContext context) {
        ActionStack as = new ActionStack(parent, session, context);
        as.init();
        return as;
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
    public Object doAction(String invokeString, Session session, Map<String, ?> params) throws Throwable {
        //һ����������ȡ��ջ����
        ActionStack stack = this.parseInvokeString(invokeString, this.createActionStack(null, session, this));
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
        Object res = invoke.invoke(stack);
        return this.shellCallBack(stack, res);
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
        ActionStack newStack = this.parseInvokeString(invokeString, this.createActionStack(stack, stack.getSession(), this));
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
        Object res = invoke.invoke(newStack);
        return this.shellCallBack(newStack, res);
    };
    /**���������ַ������ҽ�����֮�������ע�뵽ActionStack�����У���󷵻ء�*/
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
    /** ָ������Action����֮��Ľű��������� */
    private Object shellCallBack(ActionStack stack, Object results) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String scriptIn = "/META-INF/submit_scripts/" + stack.getResultsScript() + ".js";
        InputStream in = ImplSubmitContext.class.getResourceAsStream(scriptIn);
        if (stack.getResultsScript() == null)
            return results;
        if (in == null)
            throw new ScriptException("�Ҳ����ű���Դ[" + scriptIn + "]");
        // 
        engine.eval(new InputStreamReader(in));
        Invocable inv = (Invocable) engine;
        return inv.invokeFunction("callBack", stack, results, stack.getResultsScriptParams());
    }
}