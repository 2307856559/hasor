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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.more.DoesSupportException;
import org.more.submit.web.WebSubmitContext;
/**
 * SubmitContext�ӿ�װ���������������һ�����κθ��ӹ��ܵĿ�װ����������ͨ�������������дĳЩ�����������书�ܵ�Ŀ�ġ�
 * @version : 2010-7-26
 * @author ������(zyc@byshell.org)
 */
public abstract class SubmitContextDecorator implements SubmitContext, WebSubmitContext {
    /**  */
    private static final long  serialVersionUID = -546605355053294936L;
    protected SubmitContext    submitContext    = null;
    protected WebSubmitContext webSubmitContext = null;
    /**������װ�����ɹ���ʼ������Ҫ����true���򷵻�false��submit������������ľ����Ƿ������װ������*/
    public boolean initDecorator(SubmitContext submitContext) {
        this.submitContext = submitContext;
        if (submitContext instanceof WebSubmitContext)
            this.webSubmitContext = (WebSubmitContext) submitContext;
        return true;
    };
    //===================================================================================
    @Override
    public Object doAction(String invokeString, Map<String, ?> params) throws Throwable {
        return this.submitContext.doAction(invokeString, params);
    };
    @Override
    public Object doAction(String invokeString, Session session, Map<String, ?> params) throws Throwable {
        return this.submitContext.doAction(invokeString, session, params);
    };
    @Override
    public Object doAction(String invokeString, String sessionID, Map<String, ?> params) throws Throwable {
        return this.submitContext.doAction(invokeString, sessionID, params);
    };
    @Override
    public Object doAction(String invokeString) throws Throwable {
        return this.submitContext.doAction(invokeString);
    };
    @Override
    public Object doActionOnStack(String invokeString, ActionStack stack, Map<String, ?> params) throws Throwable {
        return this.submitContext.doActionOnStack(invokeString, stack, params);
    };
    @Override
    public ActionContext getActionContext() {
        return this.submitContext.getActionContext();
    };
    @Override
    public Iterator<String> getActionInvokeStringIterator() {
        return this.submitContext.getActionInvokeStringIterator();
    };
    @Override
    public SessionManager getSessionManager() {
        return this.submitContext.getSessionManager();
    };
    @Override
    public boolean isWebContext() {
        return this.submitContext.isWebContext();
    };
    @Override
    public void setSessionManager(SessionManager sessionManager) {
        this.submitContext.setSessionManager(sessionManager);
    };
    //===================================================================================
    @Override
    public Object doAction(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.doAction(request, response);
    };
    @Override
    public Object doAction(PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.doAction(pageContext, request, response);
    };
    @Override
    public Object doAction(PageContext pageContext) throws Throwable {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.doAction(pageContext);
    };
    @Override
    public Object doAction(String actionInvoke, HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Throwable {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.doAction(actionInvoke, request, response, params);
    };
    @Override
    public Object doAction(String actionInvoke, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.doAction(actionInvoke, request, response);
    };
    @Override
    public Object doAction(String actionInvoke, PageContext pageContext, HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Throwable {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.doAction(actionInvoke, pageContext, request, response, params);
    };
    @Override
    public Object doAction(String actionInvoke, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.doAction(actionInvoke, pageContext, request, response);
    };
    @Override
    public String getProtocol() {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.getProtocol();
    };
    @Override
    public ServletContext getServletContext() {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.getServletContext();
    };
    @Override
    public boolean isActionRequest(HttpServletRequest request) {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        return this.webSubmitContext.isActionRequest(request);
    };
    @Override
    public void setProtocol(String protocol) {
        if (this.webSubmitContext == null)
            throw new DoesSupportException("��װ�ε�submitContext����һ��webSubmitContext��");
        this.webSubmitContext.setProtocol(protocol);
    };
    //===================================================================================
    @Override
    public void clearAttribute() {
        this.submitContext.clearAttribute();
    };
    @Override
    public boolean contains(String name) {
        return this.submitContext.contains(name);
    };
    @Override
    public Object getAttribute(String name) {
        return this.submitContext.getAttribute(name);
    };
    @Override
    public String[] getAttributeNames() {
        return this.submitContext.getAttributeNames();
    };
    @Override
    public void removeAttribute(String name) {
        this.submitContext.removeAttribute(name);
    };
    @Override
    public void setAttribute(String name, Object value) {
        this.submitContext.setAttribute(name, value);
    };
};