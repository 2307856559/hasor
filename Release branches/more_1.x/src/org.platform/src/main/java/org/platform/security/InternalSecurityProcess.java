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
import static org.platform.security.AuthSession.HttpSessionAuthSessionSetName;
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
    @Inject
    private SecuritySettings settings   = null;
    @Inject
    private SecurityContext  secService = null;
    @Inject
    private AppContext       appContext = null;
    //
    private void writeHttpSession(HttpServletRequest httpRequest) {
        AuthSession[] authSessions = this.secService.getCurrentAuthSession();
        //1.д��HttpSession
        StringBuilder authSessionIDs = new StringBuilder("");
        for (AuthSession authSession : authSessions)
            authSessionIDs.append(authSession.getSessionID() + ",");
        httpRequest.getSession(true).setAttribute(HttpSessionAuthSessionSetName, authSessionIDs.toString());
    }
    //
    /**д��Ȩ��Cookie��*/
    public void writeAuthSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SecurityException {
        this.writeHttpSession(httpRequest);
        //
        if (this.settings.isCookieEncryptionEnable() == false)
            return;
        //2.д��Cookie����
        CookieDataUtil cookieData = CookieDataUtil.create();
        AuthSession[] authSessions = this.secService.getCurrentAuthSession();
        if (authSessions != null)
            for (AuthSession authSession : authSessions) {
                if (authSession.isLogin() == false)
                    continue;
                CookieUserData cookieUserData = new CookieUserData();
                cookieUserData.setUserCode(authSession.getUserObject().getUserCode());//�û�Code
                cookieUserData.setAuthSystem(authSession.getAuthSystem());//�û���Դ
                cookieUserData.setAppStartTime(this.appContext.getAppStartTime());
                cookieData.addCookieUserData(cookieUserData);
            }
        //2.����Cookie
        String cookieValue = CookieDataUtil.parseString(cookieData);
        if (this.settings.isCookieEncryptionEnable() == true) {
            Digest digest = this.secService.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
            try {
                cookieValue = digest.encrypt(cookieValue, this.settings.getCookieEncryptionKey());
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
    /**ͨ��userCode�������µ�½�ķ�ʽ�ָ�AuthSession*/
    private void recoverUserByCode(String authSystem, String userCode) throws SecurityException {
        AuthSession newAuthSession = null;
        try {
            newAuthSession = this.secService.getCurrentBlankAuthSession();
            if (newAuthSession == null)
                newAuthSession = this.secService.createAuthSession();
            //
            newAuthSession.doLoginCode(authSystem, userCode);
        } catch (SecurityException e) {
            Platform.warning("recover cookieUser failure! userCode=" + userCode);
            if (newAuthSession != null)
                newAuthSession.close();
        }
    }
    //
    /**�ָ�Cookie�еĵ�½�ʺ�,�÷����ᵼ�µ���writeHttpSession������*/
    private boolean recoverAuthSession4Cookie(HttpServletRequest httpRequest) throws SecurityException {
        //1.���Cookie
        if (this.settings.isCookieEnable() == false)
            return false;
        //2.����cookie��value
        Cookie[] cookieArray = httpRequest.getCookies();
        String cookieValue = null;
        for (Cookie cookie : cookieArray) {
            //ƥ��cookie����
            if (cookie.getName().endsWith(this.settings.getCookieName()) == false)
                continue;
            cookieValue = cookie.getValue();
            if (this.settings.isCookieEncryptionEnable() == true) {
                Digest digest = this.secService.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
                try {
                    cookieValue = digest.decrypt(cookieValue, this.settings.getCookieEncryptionKey());
                } catch (Throwable e) {
                    Platform.warning(this.settings.getCookieEncryptionEncodeType() + " decode cookieValue error. cookieValue=" + cookieValue);
                    return false;/*����ʧ����ζ�ź���Ļָ������������õ���Ч�������return.*/
                }
            }
            break;
        }
        //3.��ȡcookie���ݻָ�Ȩ�޻Ự
        CookieUserData[] infos = null;
        try {
            CookieDataUtil cookieData = CookieDataUtil.parseJson(cookieValue);
            infos = cookieData.getCookieUserDatas();
            if (infos == null)
                return false;
        } catch (Exception e) {
            Platform.debug("parseJson to CookieDataUtil error! " + this.settings.getCookieEncryptionEncodeType() + " decode . cookieValue=" + cookieValue);
            return false;
        }
        boolean returnData = false;
        //4.�ָ�Cookie�ﱣ��ĻỰ
        for (CookieUserData info : infos) {
            if (this.settings.isLoseCookieOnStart() == true)
                if (this.appContext.getAppStartTime() != info.getAppStartTime())
                    continue;
            /*��userCode�ָ���һ���µĻỰ*/
            this.recoverUserByCode(info.getAuthSystem(), info.getUserCode());
            returnData = true;
        }
        this.writeHttpSession(httpRequest);
        return returnData;
    }
    //
    /**�ָ�HttpSession�еĵ�½�ʺš�*/
    private boolean recoverAuthSession4HttpSession(HttpSession httpSession) {
        String authSessionIDs = (String) httpSession.getAttribute(HttpSessionAuthSessionSetName);
        if (StringUtil.isBlank(authSessionIDs) == true)
            return false;
        String[] authSessionIDSet = authSessionIDs.split(",");
        boolean returnData = false;
        for (String authSessionID : authSessionIDSet) {
            try {
                if (this.secService.activateAuthSession(authSessionID) == true) {
                    Platform.debug("authSession : " + authSessionID + " activate!");
                    returnData = true;
                }
            } catch (SecurityException e) {
                Platform.info(authSessionID + " activate an error. " + Platform.logString(e));
            }
        }
        return returnData;
    }
    //
    /**�ָ�Ȩ��*/
    public void recoverAuthSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SecurityException {
        //1.�ָ��Ự
        boolean recoverMark = this.recoverAuthSession4HttpSession(httpRequest.getSession(true));
        if (recoverMark == false)
            recoverMark = this.recoverAuthSession4Cookie(httpRequest);
        if (recoverMark == true)
            return;
        //2.���������˻�
        if (this.settings.isGuestEnable() == true) {
            try {
                AuthSession targetAuthSession = this.secService.getCurrentBlankAuthSession();
                if (targetAuthSession == null)
                    targetAuthSession = this.secService.createAuthSession();
                String guestAccount = this.settings.getGuestAccount();
                String guestPassword = this.settings.getGuestPassword();
                String guestAuthSystem = this.settings.getGuestAuthSystem();
                targetAuthSession.doLogin(guestAuthSystem, guestAccount, guestPassword);/*��½�����ʺ�*/
            } catch (Exception e) {
                Platform.warning(Platform.logString(e));
            }
        }
    }
    //
    /**�����������*/
    public void processLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SecurityException {
        //1.��õ��������Ϣ
        String account = httpRequest.getParameter(this.settings.getAccountField());
        String password = httpRequest.getParameter(this.settings.getPasswordField());
        String formAuth = httpRequest.getParameter(this.settings.getAuthField());
        //3.ִ�е���
        AuthSession authSession = this.secService.getCurrentBlankAuthSession();
        if (authSession == null)
            authSession = this.secService.createAuthSession();
        try {
            authSession.doLogin(formAuth, account, password);/*�����»Ự*/
            Platform.info("login OK. acc=" + account + " , at SessionID=" + authSession.getSessionID());
            this.writeAuthSession(httpRequest, httpResponse);
        } catch (SecurityException e) {
            Platform.warning("login failure! acc=" + account + " , msg=" + e.getMessage());
            authSession.close();
            throw e;
        }
    }
    //
    /**����ǳ�����*/
    public void processLogout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SecurityException {
        AuthSession[] authSessions = this.secService.getCurrentAuthSession();
        for (AuthSession authSession : authSessions) {
            /*�������ѵ���ĻỰȫ���ǳ�*/
            if (authSession.isLogin() == false)
                continue;
            String userCode = authSession.getUserObject().getUserCode();
            try {
                authSession.doLogout();/*�˳��Ự*/
                Platform.info("logout OK. userCode=" + userCode + " , at SessionID=" + authSession.getSessionID());
            } catch (SecurityException e) {
                Platform.info("logout failure! userCode=" + userCode + " , at SessionID=" + authSession.getSessionID());
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