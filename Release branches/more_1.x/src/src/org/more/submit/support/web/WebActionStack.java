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
package org.more.submit.support.web;
import java.util.HashMap;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.more.StateException;
import org.more.submit.ActionStack;
import org.more.submit.ImplSubmitContext;
import org.more.submit.Session;
import org.more.submit.support.web.scope.CookieScope;
import org.more.submit.support.web.scope.HttpSessionScope;
import org.more.submit.support.web.scope.JspPageScope;
import org.more.submit.support.web.scope.RequestScope;
import org.more.submit.support.web.scope.ServletContextScope;
import org.more.util.attribute.IAttribute;
/**
 * �ṩ��Web���Ե�ActionStack���󣬸��໹�ṩ����������������֧�֡�
 * @version 2009-12-28
 * @author ������ (zyc@byshell.org)
 */
public class WebActionStack extends ActionStack implements WebScopeEnum {
    private static final long           serialVersionUID = 5001483997344333143L;
    //========================================================================================Field
    private HttpServletRequest          httpRequest;
    private HttpServletResponse         httpResponse;
    private PageContext                 httpPageContext;
    private HashMap<String, IAttribute> attributeForScopeForMap;                //��ǰ��������������ӿڡ�
    //==================================================================================Constructor
    public WebActionStack(ActionStack parent, Session session, ImplSubmitContext context) {
        super(parent, session, context);
    }
    //==========================================================================================ע�����ԡ���ʼ��
    void setRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
    void setResponse(HttpServletResponse httpResponse) {
        this.httpResponse = httpResponse;
    }
    void setPageContext(PageContext httpPageContext) {
        this.httpPageContext = httpPageContext;
    }
    //==========================================================================================������Ե�get/set����
    /**��ȡServletContext����*/
    public ServletContext getServletContext() {
        WebSubmitContext webContext = (WebSubmitContext) this.getContext();
        return webContext.getServletContext();
    }
    /**��ȡHttpServletRequest����*/
    public HttpServletRequest getRequest() {
        return httpRequest;
    }
    /**��ȡHttpServletResponse����*/
    public HttpServletResponse getResponse() {
        return httpResponse;
    }
    /**��ȡPageContext����*/
    public PageContext getPageContext() {
        return httpPageContext;
    }
    @Override
    protected IAttribute getScopeAttribute(String scope) {
        if (this.attributeForScopeForMap == null)
            this.attributeForScopeForMap = new HashMap<String, IAttribute>();
        IAttribute att = this.attributeForScopeForMap.get(scope);
        if (att != null)
            return att;
        if (Scope_JspPage.equals(scope) == true)
            if (this.httpPageContext != null)
                att = new JspPageScope(this.httpPageContext);//ҳ��������
            else
                throw new StateException("��ǰ״̬��֧��JspPage������");
        else if (Scope_HttpRequest.equals(scope) == true)
            att = new RequestScope(this.httpRequest);//Request��Χ
        else if (Scope_HttpSession.equals(scope) == true)
            att = new HttpSessionScope(this.httpRequest.getSession(true));//HttpSession��Χ
        else if (Scope_Cookie.equals(scope) == true)
            att = new CookieScope(this.httpRequest, this.httpResponse);//Cookie��Χ
        else if (Scope_ServletContext.equals(scope) == true)
            att = new ServletContextScope(this.httpRequest.getSession().getServletContext());//ServletContext��Χ
        else
            att = super.getScopeAttribute(scope);
        this.attributeForScopeForMap.put(scope, att);
        return att;
    }
    //==========================================================================================request��ѯ����ר�÷���
    /** ����stack->jspPage->request->session->httpSession->servletContext->context->cookie���˳�����β������ԣ���stack�в���ʱ��������stack���в��ҡ�*/
    public Object getParam(String key) {
        Object obj = this.getByStackTree(key);
        if (obj == null && this.httpPageContext != null)
            obj = this.getScopeAttribute(Scope_JspPage).getAttribute(key);
        if (obj == null && this.httpRequest != null)
            obj = this.getScopeAttribute(Scope_HttpRequest).getAttribute(key);
        if (obj == null && this.moreSessionScope != null)
            obj = this.getScopeAttribute(Scope_Session).getAttribute(key);
        if (obj == null && this.httpRequest != null)
            obj = this.getScopeAttribute(Scope_HttpSession).getAttribute(key);
        if (obj == null && this.getServletContext() != null)
            obj = this.getScopeAttribute(Scope_ServletContext).getAttribute(key);
        if (obj == null && this.moreContextScope != null)
            obj = this.getScopeAttribute(Scope_Context).getAttribute(key);
        if (obj == null && this.httpRequest != null && this.httpResponse != null)
            obj = this.getScopeAttribute(Scope_Cookie).getAttribute(key);
        return obj;
    };
    //==========================================================================================request��ѯ����ר�÷���
    /**��ȡrequest������������в������ơ�*/
    @SuppressWarnings("unchecked")
    public String[] getRequestParamNames() {
        Set keys = this.httpRequest.getParameterMap().keySet();
        String[] ns = new String[keys.size()];
        keys.toArray(ns);
        return ns;
    }
    /**����request��������в���ָ�����ԡ�*/
    public String[] getRequestParams(String attName) {
        return this.httpRequest.getParameterValues(attName);
    }
    /**����request��������в���ָ�����ԡ�*/
    public String getRequestParam(String attName) {
        return this.httpRequest.getParameter(attName);
    }
    //==========================================================================================Cookie�������µĿ��ٲ�����
    /**�����ǰ��������Cookie�������StateException�쳣��*/
    public void setCookieAttribute(String name, String value, int age) throws StateException {
        if (this.getScope().equals(Scope_Cookie) == false)
            throw new StateException("��ǰ��������Cookie��");
        ((CookieScope) this.getCurrentScopeAtt()).setCookieAttribute(name, value, age);
    };
    /**�����ǰ��������Cookie�������StateException�쳣��*/
    public void setCookieAttribute(Cookie cookie) throws StateException {
        if (this.getScope().equals(Scope_Cookie) == false)
            throw new StateException("��ǰ��������Cookie��");
        ((CookieScope) this.getCurrentScopeAtt()).setCookieAttribute(cookie);
    };
    /**�����ǰ��������Cookie�������StateException�쳣��*/
    public Cookie getCookieAttribute(String name) throws StateException {
        if (this.getScope().equals(Scope_Cookie) == false)
            throw new StateException("��ǰ��������Cookie��");
        return ((CookieScope) this.getCurrentScopeAtt()).getCookieAttribute(name);
    };
}