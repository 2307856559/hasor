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
package org.platform.security.support;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.more.util.StringUtils;
import org.platform.Platform;
import org.platform.security.AuthRequestProcess;
import org.platform.security.AuthSession;
import org.platform.security.Digest;
import org.platform.security.PermissionException;
import org.platform.security.SecurityContext;
import org.platform.security.SecurityDispatcher;
import org.platform.security.SecurityException;
import org.platform.security.SecurityForward;
import org.platform.security.TestPermissionProcess;
import org.platform.security.support.CookieDataUtil.CookieUserData;
import com.google.inject.Inject;
import com.google.inject.Singleton;
/**
 * Ȩ��ϵͳURL������֧�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
@Singleton
public class SecurityFilter implements Filter {
    @Inject
    private InternalRecoverAuth4Cookie recoverAuth4Cookie   = null;
    @Inject
    private SecurityContext            secContext           = null;
    @Inject
    private AuthRequestProcess         loginSecurityProcess = null;
    @Inject
    private TestPermissionProcess      permissionProcess    = null;
    /**��ʼ��*/
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Platform.info("SecurityFilter started.");
    }
    /**����*/
    @Override
    public void destroy() {
        Platform.info("SecurityFilter destroy.");
    }
    /**ˢ��HttpSession�е�Ȩ������*/
    private void refreshHttpSession(HttpServletRequest request) {
        AuthSession[] authSessions = this.secContext.getCurrentAuthSession();
        //1.д��HttpSession
        StringBuilder authSessionIDs = new StringBuilder("");
        for (AuthSession authSession : authSessions)
            authSessionIDs.append(authSession.getSessionID() + ",");
        String authStrs = authSessionIDs.toString();
        request.getSession(true).setAttribute(AuthSession.HttpSessionAuthSessionSetName, authStrs);
    }
    /**��Ȩ������д��Cookie*/
    private void writeCookie(HttpServletRequest request, HttpServletResponse response) throws SecurityException {
        SecuritySettings settings = this.secContext.getSettings();
        if (this.secContext.getSettings().isCookieEncryptionEnable() == false)
            return;
        //2.д��Cookie����
        CookieDataUtil cookieData = CookieDataUtil.create();
        AuthSession[] authSessions = this.secContext.getCurrentAuthSession();
        if (authSessions != null)
            for (AuthSession authSession : authSessions) {
                if (authSession.isLogin() == false)
                    continue;
                CookieUserData cookieUserData = new CookieUserData();
                cookieUserData.setUserCode(authSession.getUserObject().getUserCode());//�û�Code
                cookieUserData.setAuthSystem(authSession.getAuthSystem());//�û���Դ
                cookieUserData.setAppStartTime(this.secContext.getAppContext().getAppStartTime());
                cookieData.addCookieUserData(cookieUserData);
            }
        //2.����Cookie
        String cookieValue = CookieDataUtil.parseString(cookieData);
        Cookie cookie = new Cookie(this.secContext.getSettings().getCookieName(), cookieValue);
        cookie.setMaxAge(settings.getCookieTimeout());
        String cookiePath = settings.getCookiePath();
        String cookieDomain = settings.getCookieDomain();
        if (StringUtils.isBlank(cookiePath) == false)
            cookie.setPath(cookiePath);
        if (StringUtils.isBlank(cookieDomain) == false)
            cookie.setDomain(cookieDomain);
        //3.д����Ӧ��
        response.addCookie(cookie);
    }
    /***/
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*����״̬*/
        if (this.secContext.getSettings().isEnableURL() == false) {
            chain.doFilter(request, response);
            return;
        }
        /*ִ�д���*/
        SecRequest secReq = new SecRequest((HttpServletRequest) request, secContext);
        SecResponse secRes = new SecResponse((HttpServletResponse) response, secContext);
        try {
            this.doSecurityFilter(secReq, secRes, chain);
        } catch (IOException e) {
            throw e;
        } catch (ServletException e) {
            throw e;
        }
        /*�ۻ��̵߳�AuthSession������ˢ������*/
        finally {
            AuthSession[] authSessions = secContext.getCurrentAuthSession();
            for (AuthSession authSession : authSessions) {
                secContext.inactivationAuthSession(authSession.getSessionID()); /*�ۻ�AuthSession*/
                authSession.refreshCacheTime();/*ˢ�»����е�����*/
            }
        }
    }
    /***/
    public void doSecurityFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //1.�ָ��Ự
        try {
            this.recoverAuth4Cookie.recoverCookie(secContext, request, response);
            this.refreshHttpSession(request);
        } catch (SecurityException e) {
            Platform.error("recover AuthSession failure!%s", e);
        }
        //2.������
        String reqPath = request.getRequestURI().substring(request.getContextPath().length());
        if (reqPath.endsWith(this.secContext.getSettings().getLoginURL()) == true) {
            /*A.����*/
            SecurityForward forward = this.loginSecurityProcess.processLogin(this.secContext, request, response);
            this.refreshHttpSession(request);
            this.writeCookie(request, response);
            if (forward != null)
                forward.forward(request, response);//��ת��ַ
            return;
        }
        if (reqPath.endsWith(this.secContext.getSettings().getLogoutURL()) == true) {
            /*B.�ǳ�*/
            SecurityForward forward = this.loginSecurityProcess.processLogout(this.secContext, request, response);
            this.refreshHttpSession(request);
            this.writeCookie(request, response);
            if (forward != null)
                forward.forward(request, response);//��ת��ַ
            return;
        }
        //3.��������
        try {
            AuthSession[] authSessions = this.secContext.getCurrentAuthSession();
            if (this.secContext instanceof AbstractSecurityContext) {
                ((AbstractSecurityContext) this.secContext).throwEvent(SecurityEventDefine.TestURLPermission, reqPath, authSessions);/*�׳��¼�*/
            }
            boolean res = this.permissionProcess.testURL(this.secContext, authSessions, request, response);
            if (res == false)
                throw new PermissionException(reqPath);
            chain.doFilter(request, response);
        } catch (PermissionException e) {
            Platform.debug("testPermission failure! uri=%s%s", reqPath, e);/*û��Ȩ��*/
            SecurityDispatcher dispatcher = this.secContext.getDispatcher(reqPath);
            if (dispatcher != null)
                dispatcher.forwardFailure(e).forward(request, response);
            else {
                e.printStackTrace(response.getWriter());
            }
        }
    }
    /*----------------------------------------------------------------------*/
    private static class SecRequest extends HttpServletRequestWrapper {
        private SecurityContext  secContext = null;
        private SecuritySettings settings   = null;
        private Cookie[]         _cookies   = null;
        public SecRequest(HttpServletRequest request, SecurityContext secContext) {
            super(request);
            this.secContext = secContext;
            this.settings = this.secContext.getSettings();
        }
        private boolean includeDigest(Cookie cookie) {
            if (StringUtils.eqUnCaseSensitive("ALL", settings.getCookieEncryptionScope()))
                return true;
            if (StringUtils.eqUnCaseSensitive("Security", settings.getCookieEncryptionScope()))
                return StringUtils.eqUnCaseBlankSensitive(cookie.getDomain(), settings.getCookieDomain()) && //
                        StringUtils.eqUnCaseBlankSensitive(cookie.getName(), settings.getCookieName()) && //
                        (StringUtils.eqUnCaseBlankSensitive(cookie.getPath(), settings.getCookiePath()));
            return false;
        }
        @Override
        public Cookie[] getCookies() {
            if (_cookies == null) {
                synchronized (this) {
                    if (_cookies == null) {
                        ArrayList<Cookie> $cookies = new ArrayList<Cookie>(Arrays.asList(super.getCookies()));
                        if ($cookies.isEmpty())
                            return null;
                        //
                        for (int i = 0; i < $cookies.size(); i++) {
                            if (this.settings.isCookieEncryptionEnable() == true) {
                                if (this.includeDigest($cookies.get(i)) == false) {
                                    continue;
                                }
                                //
                                String cookieValue = $cookies.get(i).getValue();
                                try {
                                    Digest digest = this.secContext.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
                                    cookieValue = digest.decrypt(cookieValue, this.settings.getCookieEncryptionKey());
                                    $cookies.get(i).setValue(cookieValue);
                                } catch (Throwable e) {
                                    Platform.warning("%s decode cookieValue error. cookieValue=%s", settings.getCookieEncryptionEncodeType(), cookieValue);
                                    $cookies.remove(i);
                                }
                            }
                        }
                        this._cookies = $cookies.toArray(new Cookie[$cookies.size()]);
                    }
                }
            }
            return this._cookies;
        }
    }
    private static class SecResponse extends HttpServletResponseWrapper {
        private SecurityContext  secContext = null;
        private SecuritySettings settings   = null;
        public SecResponse(HttpServletResponse response, SecurityContext secContext) {
            super(response);
            this.secContext = secContext;
            this.settings = this.secContext.getSettings();
        }
        private boolean includeDigest(Cookie cookie) {
            if (StringUtils.eqUnCaseSensitive("ALL", settings.getCookieEncryptionScope()))
                return true;
            if (StringUtils.eqUnCaseSensitive("Security", settings.getCookieEncryptionScope()))
                return StringUtils.eqUnCaseBlankSensitive(cookie.getDomain(), settings.getCookieDomain()) && //
                        StringUtils.eqUnCaseBlankSensitive(cookie.getName(), settings.getCookieName()) && //
                        StringUtils.eqUnCaseBlankSensitive(cookie.getPath(), settings.getCookiePath());
            return false;
        }
        @Override
        public void addCookie(Cookie cookie) {
            if (this.settings.isCookieEncryptionEnable() == true && this.includeDigest(cookie) == true) {
                String cookieValue = cookie.getValue();
                try {
                    Digest digest = this.secContext.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
                    cookieValue = digest.encrypt(cookieValue, this.settings.getCookieEncryptionKey());
                    cookie.setValue(cookieValue);
                } catch (Throwable e) {
                    Platform.warning("%s encode cookieValue error. cookieValue=%s", this.settings.getCookieEncryptionEncodeType(), cookieValue);
                }
            }
            super.addCookie(cookie);
        }
    }
}