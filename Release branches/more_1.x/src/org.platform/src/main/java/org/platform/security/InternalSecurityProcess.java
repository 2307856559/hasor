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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.more.util.StringUtil;
import org.platform.Platform;
import org.platform.context.AppContext;
import org.platform.security.CookieDataUtil.CookieUserData;
import com.google.inject.Inject;
/**
 * Ȩ�޴������,��������롢�ǳ��ĺ����߼���
 * @version : 2013-4-25
 * @author ������ (zyc@byshell.org)
 */
class InternalSecurityProcess implements SecurityProcess {
    private static final String HttpSessionAuthSessionSetName = AuthSession.class.getName();
    @Inject
    private SecuritySettings    settings                      = null;
    @Inject
    private SecurityContext     secService                    = null;
    @Inject
    private AppContext          appContext                    = null;
    //
    //
    /**д��Ȩ��Cookie��*/
    private void writeAuthSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SecurityException {
        if (this.settings.isCookieEncryptionEnable() == false)
            return;
        AuthSession[] authSessions = this.secService.getCurrentAuthSession();
        if (authSessions == null)
            return;
        //1.д��HttpSession
        StringBuilder authSessionIDs = new StringBuilder("");
        for (AuthSession authSession : authSessions)
            authSessionIDs.append(authSession.getSessionID() + ",");
        httpRequest.getSession(true).setAttribute(HttpSessionAuthSessionSetName, authSessionIDs.toString());
        //
        //2.д��Cookie����
        CookieDataUtil cookieData = CookieDataUtil.create();
        if (authSessions != null)
            for (AuthSession authSession : authSessions) {
                CookieUserData cookieUserData = new CookieUserData();
                cookieUserData.setCreatedTime(authSession.getCreatedTime());//Session����ʱ��
                cookieUserData.setUserCode(authSession.getUserObject().getUserCode());//�û�Code
                cookieUserData.setAuthSessionID(authSession.getSessionID());//AuthSessionID
                cookieUserData.setAppStartTime(this.appContext.getAppStartTime());
                cookieData.addCookieUserData(cookieUserData);
            }
        //2.����Cookie
        String cookieValue = CookieDataUtil.parseString(cookieData);
        if (this.settings.isCookieEncryptionEnable() == true) {
            CodeDigest digest = this.secService.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
            try {
                cookieValue = digest.encode(cookieValue, this.settings.getCookieEncryptionKey());
            } catch (Throwable e) {
                Platform.warning(this.settings.getCookieEncryptionEncodeType() + " encode cookieValue error. cookieValue=" + cookieValue);
                return;
            }
        }
        Cookie cookie = new Cookie(this.settings.getCookieName(), cookieValue);
        cookie.setMaxAge(this.settings.getCookieTimeout());
        String cookiePath = this.settings.getCookiePath();
        String cookieDomain = this.settings.getCookieDomain();
        if (StringUtil.isBlank(cookiePath) == false)
            cookie.setPath(cookiePath);
        if (StringUtil.isBlank(cookieDomain) == false)
            cookie.setDomain(cookieDomain);
        //3.д����Ӧ��
        httpResponse.addCookie(cookie);
    }
    //
    /**ͨ��userCode�ָ�AuthSession*/
    private void recoverUserByCode(String userCode) {
        AuthSession newAuthSession = this.secService.createAuthSession();
        try {
            newAuthSession.doLoginCode(userCode);
        } catch (SecurityException e) {
            newAuthSession.close();
            Platform.warning("recover cookieUser failure! userCode=" + userCode);
        }
    }
    //
    /**�ָ�Ȩ��*/
    public void recoverAuthSession(HttpServletRequest httpRequest) throws SecurityException {
        this.recoverAuthSession4HttpSession(httpRequest.getSession(true));
        this.recoverAuthSession4Cookie(httpRequest);
    }
    //
    /**�ָ�Cookie�еĵ�½�ʺš�*/
    private void recoverAuthSession4Cookie(HttpServletRequest httpRequest) throws SecurityException {
        //1.���Cookie
        if (this.settings.isCookieEnable() == false)
            return;
        //2.����cookie��value
        Cookie[] cookieArray = httpRequest.getCookies();
        String cookieValue = null;
        for (Cookie cookie : cookieArray) {
            //ƥ��cookie����
            if (cookie.getName().endsWith(this.settings.getCookieName()) == false)
                continue;
            cookieValue = cookie.getValue();
            if (this.settings.isCookieEncryptionEnable() == true) {
                CodeDigest digest = this.secService.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
                try {
                    cookieValue = digest.decode(cookieValue, this.settings.getCookieEncryptionKey());
                } catch (Throwable e) {
                    Platform.warning(this.settings.getCookieEncryptionEncodeType() + " decode cookieValue error. cookieValue=" + cookieValue);
                    return;/*����ʧ����ζ�ź���Ļָ������������õ���Ч�������return.*/
                }
            }
            break;
        }
        //3.��ȡcookie���ݻָ�Ȩ�޻Ự
        CookieDataUtil cookieData = CookieDataUtil.parseJson(cookieValue);
        CookieUserData[] infos = cookieData.getCookieUserDatas();
        if (infos == null)
            return;
        //4.�ָ�Cookie�ﱣ��ĻỰ
        for (CookieUserData info : infos) {
            if (info.isRecover() == false)
                continue;
            if (this.settings.isLoseCookieOnStart() == true)
                if (this.appContext.getAppStartTime() != info.getAppStartTime())
                    continue;
            String authSessionID = info.getAuthSessionID();
            if (this.secService.hasAuthSession(authSessionID) == true)
                /*�����֤ϵͳ�л�����authSessionID��ʾ�ĻỰ�ͼ�����*/
                this.secService.activateAuthSession(authSessionID);
            else
                /*��userCode�ָ���һ���µĻỰ*/
                this.recoverUserByCode(info.getUserCode());
        }
    }
    //
    /**�ָ�Cookie�еĵ�½�ʺš�*/
    private void recoverAuthSession4HttpSession(HttpSession httpSession) {
        String authSessionIDs = (String) httpSession.getAttribute(HttpSessionAuthSessionSetName);
        if (StringUtil.isBlank(authSessionIDs) == true)
            return;
        String[] authSessionIDSet = authSessionIDs.split(",");
        for (String authSessionID : authSessionIDSet) {
            if (this.secService.hasAuthSession(authSessionID) == true)
                this.secService.activateAuthSession(authSessionID);
        }
    }
    //
    /**�����������*/
    public void processLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SecurityException {
        //1.��õ��������Ϣ
        String account = httpRequest.getParameter(this.settings.getAccountField());
        String password = httpRequest.getParameter(this.settings.getPasswordField());
        //3.ִ�е���
        AuthSession authSession = this.secService.createAuthSession();
        try {
            authSession.doLogin(account, password);/*�����»Ự*/
            Platform.info("login OK. acc=" + account + " , at SessionID=" + authSession.getSessionID());
            this.writeAuthSession(httpRequest, httpResponse);
        } catch (SecurityException e) {
            authSession.close();
            Platform.info("login failure! acc=" + account + " , msg=" + e.getMessage());
            throw e;
        }
    }
    //
    /**����ǳ�����*/
    public void processLogout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SecurityException {
        AuthSession[] authSessions = this.secService.getCurrentAuthSession();
        if (authSessions == null)
            return;
        //
        for (AuthSession authSession : authSessions) {
            /*�������ѵ���ĻỰȫ���ǳ�*/
            if (authSession.isLogin() == false)
                continue;
            String acc = authSession.getUserObject().getAccount();
            try {
                authSession.doLogout();/*�˳��Ự*/
                Platform.info("logout OK. acc=" + acc + " , at SessionID=" + authSession.getSessionID());
            } catch (SecurityException e) {
                Platform.info("logout failure! acc=" + acc + " , at SessionID=" + authSession.getSessionID());
                throw e;
            }
        }
        //
        this.writeAuthSession(httpRequest, httpResponse);
    }
    //
    /**����Ҫ�������Դ�Ƿ����Ȩ�޷��ʣ����Ȩ�޼��ʧ�ܻ��׳�PermissionException�쳣��*/
    public void processTestFilter(String reqPath) throws PermissionException {
        UriPatternMatcher uriMatcher = this.secService.getUriMatcher(reqPath);
        AuthSession[] authSessions = this.secService.getCurrentAuthSession();
        if (uriMatcher.testPermission(authSessions) == false)
            throw new PermissionException(reqPath);
    }
}