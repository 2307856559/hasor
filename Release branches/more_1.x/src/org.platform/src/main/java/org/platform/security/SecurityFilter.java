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
import static org.platform.PlatformConfigEnum.Security_LoginFormData_AccountField;
import static org.platform.PlatformConfigEnum.Security_LoginFormData_PasswordField;
import static org.platform.PlatformConfigEnum.Security_LoginURL;
import static org.platform.PlatformConfigEnum.Security_LogoutURL;
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
import org.more.core.global.Global;
import org.platform.Assert;
import org.platform.Platform;
import org.platform.context.AppContext;
import org.platform.context.SettingListener;
import org.platform.startup.RuntimeListener;
/**
 * Ȩ��ϵͳURL������֧�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
class SecurityFilter implements Filter {
    private String          accountField  = null; //�ʺ��ֶ�
    private String          passwordField = null; //�����ֶ�
    private String          loginURL      = null; //�����ַ
    private String          logoutURL     = null; //�ǳ���ַ
    //
    private AppContext      appContext    = null;
    private SecurityService secService    = null;
    //
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Platform.info("init SecurityFilter...");
        ServletContext servletContext = filterConfig.getServletContext();
        this.appContext = (AppContext) servletContext.getAttribute(RuntimeListener.AppContextName);
        Assert.isNotNull(this.appContext, "AppContext is null.");
        //
        this.secService = this.appContext.getBean(SecurityService.class);
        //
        /*�������ļ��ı�ʱ�����ѽ���֪ͨ*/
        SettingListener settingListener = new SettingListener() {
            @Override
            public void reLoadConfig(Global oldConfig, Global newConfig) {
                accountField = newConfig.getString(Security_LoginFormData_AccountField);
                passwordField = newConfig.getString(Security_LoginFormData_PasswordField);
                loginURL = newConfig.getString(Security_LoginURL);
                logoutURL = newConfig.getString(Security_LogoutURL);
            }
        };
        this.appContext.getInitContext().getConfig().addSettingsListener(settingListener);
        /*ȡ������*/
        Global currentSetting = this.appContext.getSettings();
        settingListener.reLoadConfig(currentSetting, currentSetting);
    }
    @Override
    public void destroy() {}
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String reqPath = httpRequest.getRequestURI();
        reqPath = reqPath.substring(httpRequest.getContextPath().length());
        AuthSession authSession = this.secService.getAuthSession(httpRequest, httpResponse, true);//��Ȼ����authSession
        SecurityDispatcher dispatcher = this.secService.getDispatcher(reqPath);/*��ȡ����ת����*/
        //
        //
        if (reqPath.endsWith(loginURL) == true) {
            //1.����ƥ��
            String account = httpRequest.getParameter(accountField);
            String password = httpRequest.getParameter(passwordField);
            Platform.info("Security -> doLogin acc=" + account + " , pwd=" + password);
            authSession.doLogin(account, password);/*����Ự*/
            //
            dispatcher.forwardIndex(httpRequest, httpResponse);//��ת������ɹ�֮��ĵ�ַ
            return;
        } else if (reqPath.endsWith(logoutURL) == true) {
            //2.�ǳ�ƥ��
            Platform.info("Security -> doLogout. user=" + authSession.getUserObject());
            authSession.doLogout();/*�˳��Ự*/
            dispatcher.forwardLogout(httpRequest, httpResponse);//��ת���˳�֮��ĵ�ַ
            return;
        }
        //3.����Ȩ���ж�
        URLPermission rule = this.secService.getURLPermission(reqPath);
        if (rule.testPermission(authSession) == false) {
            /*û��Ȩ�ޣ�ִ����ת*/
            dispatcher.forwardError(request, response);//��ת�������쳣�ĵ�ַ
            return;
        }
        //4.�߱�Ȩ�޼�������
        chain.doFilter(httpRequest, httpResponse);
    }
}