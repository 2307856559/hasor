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
import java.util.Iterator;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.more.core.error.MoreStateException;
import org.more.util.attribute.AttBase;
/**
 * ������submit�ĺ����࣬����ʵ����SubmitContext�ӿڵ���ֻ��һ������ʵ�֡�
 * ���������ͨ��ʵ�������Է�����֧��console��web���������������������Ҳ��������ʵ�֡�
 * ����֮����໹�ṩ����invokeString�ַ����Ĺ��ܣ�ͬʱ���ṩ��returnScript��֧�֡�
 * @version : 2010-7-26
 * @author ������(zyc@byshell.org)
 */
public abstract class AbstractSubmitContextImpl extends AttBase implements SubmitContext {
    /**  */
    private static final long serialVersionUID = -7237602016830547578L;
    private ActionContext     actionContext    = null;
    private SessionManager    sessionManager   = null;
    public Object doAction(String invokeString) throws Throwable {
        Session session = sessionManager.createSession();
        return this.doAction(invokeString, session, null);
    }
    public Object doAction(String invokeString, Map<String, ?> params) throws Throwable {
        Session session = sessionManager.createSession();
        return this.doAction(invokeString, session, params);
    }
    public Object doAction(String invokeString, String sessionID, Map<String, ?> params) throws Throwable {
        Session session = sessionManager.getSession(sessionID);
        return this.doAction(invokeString, session, params);
    }
    public Object doAction(String invokeString, Session session, Map<String, ?> params) throws Throwable {
        if (this.sessionManager.isBelong(session) == false)
            throw new MoreStateException("session " + session.getSessionID() + " �����ڵ�ǰsessionManager�����session");
        String[] ss = Util.splitInvokeString(invokeString);
        ActionStack as = this.createStack(ss[0], ss[1], null, session);
        ActionInvoke ai = this.actionContext.findAction(ss[0], ss[1]);
        return this.invokeAction(ai, as, params);
    }
    public Object doActionOnStack(String invokeString, ActionStack stack, Map<String, ?> params) throws Throwable {
        Session session = stack.getSession();
        if (this.sessionManager.isBelong(session) == false)
            throw new MoreStateException("session " + session.getSessionID() + " �����ڵ�ǰsessionManager�����session");
        String[] ss = Util.splitInvokeString(invokeString);
        ActionStack as = this.createStack(ss[0], ss[1], stack, session);
        ActionInvoke ai = this.actionContext.findAction(ss[0], ss[1]);
        return this.invokeAction(ai, as, params);
    }
    /**ִ�е��á�*/
    protected Object invokeAction(ActionInvoke action, ActionStack stack, Map<String, ?> params) throws Throwable {
        stack.putALL(params);
        return this.callBack(stack, action.invoke(stack));
    }
    /**����һ������ʹ�õ�ActionStack�����������ͨ����д�÷�������չActionStack����*/
    protected abstract ActionStack createStack(String actionName, String actionMethod, ActionStack parent, Session session);
    public ActionContext getActionContext() {
        return this.actionContext;
    }
    protected void setActionContext(ActionContext actionContext) {
        this.actionContext = actionContext;
    }
    public Iterator<String> getActionInvokeStringIterator() {
        return new InvokeStringIterator(this.actionContext);
    }
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }
    public void setSessionManager(SessionManager sessionManager) {
        if (sessionManager != null)
            this.sessionManager = sessionManager;
        else
            throw new NullPointerException("SessionManager���Ͳ���Ϊ�ա�");
    }
    //------------------------------------------------------------------------------------------
    /** ָ������Action����֮��Ľű��������� */
    private Object callBack(ActionStack stack, Object results) throws Exception {
        String scriptEngine = stack.getResultsScriptEngine();
        String resultsScript = stack.getResultsScript();
        //һ�����û�����ûص��ű���ֱ�ӷ��ؽ��
        if (scriptEngine == null || resultsScript == null)
            return results;
        //���������ű�ִ�л���
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(scriptEngine);
        if (engine == null)
            throw new ScriptException("�޷������ű�����[" + scriptEngine + "]");
        //������ȡ�ű��ļ�������
        String scriptIn = "/META-INF/resource/submit/submit_scripts/" + stack.getResultsScript() + ".js";
        InputStream in = SubmitContextImpl.class.getResourceAsStream(scriptIn);
        if (in == null)
            throw new ScriptException("�Ҳ����ű���Դ[" + scriptIn + "]");
        //�ġ�ִ�нű�����callBack�����һ�ȡ����ֵ����
        engine.eval(new InputStreamReader(in));
        Invocable inv = (Invocable) engine;
        return inv.invokeFunction("callBack", stack, results, stack.getResultsScriptParams());
    }
}