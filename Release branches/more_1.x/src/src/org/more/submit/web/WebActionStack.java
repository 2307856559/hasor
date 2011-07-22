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
package org.more.submit.web;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import org.more.submit.ActionStack;
import org.more.submit.impl.DefaultActionStack;
import org.more.submit.web.scope.CookieScope;
/**
 * �ṩ��Web���Ե�ActionStack���󣬸��໹�ṩ����������������֧�֡���������������WebScopeEnum�ӿڡ�
 * @version : 2010-7-27
 * @author ������(zyc@byshell.org)
 */
public class WebActionStack extends DefaultActionStack {
    private static final long serialVersionUID = 5001483997344333143L;
    private WebSubmitService  submitContext    = null;
    //==================================================================================Constructor
    public WebActionStack(URI uri, ActionStack parent, WebSubmitService submitContext) {
        super(uri, parent, submitContext);
        this.submitContext = submitContext;
    };
    //==========================================================================================������Ե�get/set����
    /**��ȡPageContext����*/
    public PageContext getPageContext() {
        return WebHelper.getPageContext();
    };
    /**��ȡHttpServletRequest����*/
    public HttpServletRequest getHttpRequest() {
        return WebHelper.getHttpRequest();
    };
    /**��ȡHttpServletResponse����*/
    public HttpServletResponse getHttpResponse() {
        return WebHelper.getHttpResponse();
    };
    /**��ȡHttpSession����*/
    public HttpSession getHttpSession() {
        return WebHelper.getHttpSession();
    };
    /**��ȡServletContext����*/
    public ServletContext getServletContext() {
        return WebHelper.getServletContext();
    };
    public WebSubmitService getSubmitService() {
        return this.submitContext;
    };
    //==========================================================================================request��ѯ����ר�÷���
    /**��ȡrequest������������в������ơ�*/
    @SuppressWarnings("unchecked")
    public String[] getRequestParamNames() {
        Set<String> keys = this.getHttpRequest().getParameterMap().keySet();
        String[] ns = new String[keys.size()];
        keys.toArray(ns);
        return ns;
    };
    /**����request��������в���ָ�����ԡ�*/
    public String[] getRequestParams(String attName) {
        return this.getHttpRequest().getParameterValues(attName);
    };
    /**����request��������в���ָ�����ԡ�*/
    public String getRequestParam(String attName) {
        return this.getHttpRequest().getParameter(attName);
    };
    /**�����ת����*/
    public void forward(String url) throws ServletException, IOException {
        HttpServletRequest request = WebHelper.getHttpRequest();
        HttpServletResponse response = WebHelper.getHttpResponse();
        request.getRequestDispatcher(url).forward(request, response);
    };
    /**�����ض��������*/
    public void redirect(String url) throws IOException {
        HttpServletResponse response = WebHelper.getHttpResponse();
        response.sendRedirect(url);
    };
    /**���ʹ���*/
    public void error(int error, String message) throws IOException {
        HttpServletResponse response = WebHelper.getHttpResponse();
        response.sendError(error, message);
    };
    /**���ʹ���*/
    public void error(int error) throws IOException {
        HttpServletResponse response = WebHelper.getHttpResponse();
        response.sendError(error);
    };
    //==========================================================================================Cookie�������µĿ��ٲ�����
    /**��cookie���������һ��cookie����*/
    public void setCookieAttribute(String name, String value, int age) {
        CookieScope cs = (CookieScope) this.getSubmitService().getScope(CookieScope.Name);
        cs.setCookieAttribute(name, value, age);
    };
    /**��cookie���������һ��cookie����*/
    public void setCookieAttribute(Cookie cookie) {
        CookieScope cs = (CookieScope) this.getSubmitService().getScope(CookieScope.Name);
        cs.setCookieAttribute(cookie);
    };
    /**��ȡһ��cookie����*/
    public Cookie getCookieAttribute(String name) {
        CookieScope cs = (CookieScope) this.getSubmitService().getScope(CookieScope.Name);
        return cs.getCookieAttribute(name);
    };
};