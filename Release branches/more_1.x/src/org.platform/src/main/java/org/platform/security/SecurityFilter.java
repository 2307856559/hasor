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
package org.platform.security;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.platform.Assert;
import org.platform.Platform;
import org.platform.context.AppContext;
import org.platform.context.ViewContext;
import org.platform.startup.RuntimeListener;
/**
 * Ȩ��ϵͳURL������֧�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
class SecurityFilter implements Filter {
    private AppContext       appContext      = null;
    private SecuritySettings settings        = null;
    private SecurityContext  secService      = null;
    private SecurityProcess  securityProcess = null;
    //
    /**��ʼ��*/
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Platform.info("init SecurityFilter...");
        ServletContext servletContext = filterConfig.getServletContext();
        this.appContext = (AppContext) servletContext.getAttribute(RuntimeListener.AppContextName);
        Assert.isNotNull(this.appContext, "AppContext is null.");
        //
        this.settings = this.appContext.getInstance(SecuritySettings.class);
        this.secService = this.appContext.getInstance(SecurityContext.class);
        this.securityProcess = this.appContext.getInstance(SecurityProcess.class);
    }
    //
    /**����*/
    @Override
    public void destroy() {}
    //
    /***/
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*����״̬*/
        if (this.settings.isEnableURL() == false) {
            chain.doFilter(request, response);
            return;
        }
        /*ִ�д���*/
        this.doSecurityFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        /*�ۻ��̵߳�AuthSession������ˢ������*/
        AuthSession[] authSessions = secService.getCurrentAuthSession();
        for (AuthSession authSession : authSessions) {
            secService.inactivationAuthSession(authSession.getSessionID()); /*�ۻ�AuthSession*/
            authSession.refreshCacheTime();/*ˢ�»����е�����*/
        }
    }
    //
    /***/
    public void doSecurityFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException {
        //1.�ָ��Ự
        try {
            this.securityProcess.recoverAuthSession(httpRequest, httpResponse);
        } catch (SecurityException e) {
            Platform.error("recover AuthSession failure!\n%s", e);
        }
        //2.������
        String reqPath = httpRequest.getRequestURI();
        reqPath = reqPath.substring(httpRequest.getContextPath().length());
        if (reqPath.endsWith(this.settings.getLoginURL()) == true) {
            this.processLogin(reqPath, httpRequest, httpResponse);
            return;
        }
        if (reqPath.endsWith(this.settings.getLogoutURL()) == true) {
            this.processLogout(reqPath, httpRequest, httpResponse);
            return;
        }
        //3.��������
        try {
            this.securityProcess.processTestFilter(reqPath);
            chain.doFilter(httpRequest, httpResponse);
        } catch (PermissionException e) {
            Platform.debug("testPermission failure! uri=%s\n%s", reqPath, e);/*û��Ȩ��*/
            SecurityDispatcher dispatcher = this.secService.getDispatcher(reqPath);
            if (dispatcher != null) {
                dispatcher.forwardFailure(ViewContext.currentViewContext(), e);
            } else {
                e.printStackTrace(httpResponse.getWriter());
            }
        }
    }
    //
    /***/
    private void processLogin(String reqPath, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        /*A.����*/
        SecurityDispatcher dispatcher = this.secService.getDispatcher(reqPath);
        try {
            this.securityProcess.processLogin(httpRequest, httpResponse);
            dispatcher.forwardIndex(ViewContext.currentViewContext());//��ת�����ַ
        } catch (SecurityException e) {
            dispatcher.forwardFailure(ViewContext.currentViewContext(), e);//��ת����ǳ�ʧ�ܵ�ַ
        }
    };
    //
    /***/
    private void processLogout(String reqPath, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        /*B.�ǳ�*/
        SecurityDispatcher dispatcher = this.secService.getDispatcher(reqPath);
        try {
            this.securityProcess.processLogout(httpRequest, httpResponse);
            dispatcher.forwardLogout(ViewContext.currentViewContext());//��ת�ǳ���ַ
        } catch (SecurityException e) {
            dispatcher.forwardFailure(ViewContext.currentViewContext(), e);//��ת����ǳ�ʧ�ܵ�ַ
        }
    };
}