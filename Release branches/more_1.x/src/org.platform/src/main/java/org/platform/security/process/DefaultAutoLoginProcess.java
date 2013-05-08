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
package org.platform.security.process;
import static org.platform.security.AuthSession.HttpSessionAuthSessionSetName;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.more.util.StringUtil;
import org.platform.Platform;
import org.platform.context.ViewContext;
import org.platform.security.AuthSession;
import org.platform.security.AutoLoginProcess;
import org.platform.security.Digest;
import org.platform.security.SecurityContext;
import org.platform.security.SecurityException;
import org.platform.security.process.CookieDataUtil.CookieUserData;
/**
 * {@link AutoLoginProcess}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-4-25
 * @author ������ (zyc@byshell.org)
 */
public class DefaultAutoLoginProcess extends AbstractProcess implements AutoLoginProcess {
    /**д��Ự���ݡ�*/
    /**д��Ự���ݡ�*/
    public void writeCookie(SecurityContext secContext, AuthSession[] authSessions, ViewContext viewContext) throws SecurityException {
        //
        if (this.settings.isCookieEncryptionEnable() == false)
            return;
        //2.д��Cookie����
        CookieDataUtil cookieData = CookieDataUtil.create();
        if (authSessions != null)
            for (AuthSession authSession : authSessions) {
                if (authSession.isLogin() == false)
                    continue;
                CookieUserData cookieUserData = new CookieUserData();
                cookieUserData.setUserCode(authSession.getUserObject().getUserCode());//�û�Code
                cookieUserData.setAuthSystem(authSession.getAuthSystem());//�û���Դ
                cookieUserData.setAppStartTime(secContext.getAppContext().getAppStartTime());
                cookieData.addCookieUserData(cookieUserData);
            }
        //2.����Cookie
        String cookieValue = CookieDataUtil.parseString(cookieData);
        if (this.settings.isCookieEncryptionEnable() == true) {
            Digest digest = secContext.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
            try {
                cookieValue = digest.encrypt(cookieValue, this.settings.getCookieEncryptionKey());
            } catch (Throwable e) {
                Platform.warning("%s encode cookieValue error. cookieValue=%s", this.settings.getCookieEncryptionEncodeType(), cookieValue);
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
        viewContext.getHttpResponse().addCookie(cookie);
    }
    /**�ָ�Ȩ��*/
    public AuthSession[] recoverCookie(SecurityContext secContext, ViewContext viewContext) throws SecurityException {
        //1.�ָ��Ự
        boolean recoverMark = this.recoverAuthSession4HttpSession(secContext, viewContext.getHttpSession(true));
        if (recoverMark == false)
            recoverMark = this.recoverAuthSession4Cookie(secContext, viewContext.getHttpRequest());
        if (recoverMark == true)
            return secContext.getCurrentAuthSession();
        //2.���������˻�
        if (this.settings.isGuestEnable() == true) {
            try {
                AuthSession targetAuthSession = secContext.getCurrentBlankAuthSession();
                if (targetAuthSession == null)
                    targetAuthSession = secContext.createAuthSession();
                String guestAccount = this.settings.getGuestAccount();
                String guestPassword = this.settings.getGuestPassword();
                String guestAuthSystem = this.settings.getGuestAuthSystem();
                targetAuthSession.doLogin(guestAuthSystem, guestAccount, guestPassword);/*��½�����ʺ�*/
            } catch (Exception e) {
                Platform.warning("%s", e);
            }
        }
        return secContext.getCurrentAuthSession();
    }
    private void recoverUserByCode(SecurityContext secContext, String authSystem, String userCode) throws SecurityException {
        /**ͨ��userCode�������µ�½�ķ�ʽ�ָ�AuthSession*/
        AuthSession newAuthSession = null;
        try {
            newAuthSession = secContext.getCurrentBlankAuthSession();
            if (newAuthSession == null)
                newAuthSession = secContext.createAuthSession();
            //
            newAuthSession.doLoginCode(authSystem, userCode);
        } catch (SecurityException e) {
            Platform.warning("recover cookieUser failure! userCode=%s", userCode);
            if (newAuthSession != null)
                newAuthSession.close();
        }
    }
    private boolean recoverAuthSession4Cookie(SecurityContext secContext, HttpServletRequest httpRequest) throws SecurityException {
        /**�ָ�Cookie�еĵ�½�ʺ�,�÷����ᵼ�µ���writeHttpSession������*/
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
                Digest digest = secContext.getCodeDigest(this.settings.getCookieEncryptionEncodeType());
                try {
                    cookieValue = digest.decrypt(cookieValue, this.settings.getCookieEncryptionKey());
                } catch (Throwable e) {
                    Platform.warning("%s decode cookieValue error. cookieValue=%s", this.settings.getCookieEncryptionEncodeType(), cookieValue);
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
            Platform.debug("parseJson to CookieDataUtil error! %s decode. cookieValue=%s", this.settings.getCookieEncryptionEncodeType(), cookieValue);
            return false;
        }
        boolean returnData = false;
        //4.�ָ�Cookie�ﱣ��ĻỰ
        for (CookieUserData info : infos) {
            if (this.settings.isLoseCookieOnStart() == true)
                if (secContext.getAppContext().getAppStartTime() != info.getAppStartTime())
                    continue;
            /*��userCode�ָ���һ���µĻỰ*/
            this.recoverUserByCode(secContext, info.getAuthSystem(), info.getUserCode());
            returnData = true;
        }
        return returnData;
    }
    private boolean recoverAuthSession4HttpSession(SecurityContext secContext, HttpSession httpSession) {
        /**�ָ�HttpSession�еĵ�½�ʺš�*/
        String authSessionIDs = (String) httpSession.getAttribute(HttpSessionAuthSessionSetName);
        if (StringUtil.isBlank(authSessionIDs) == true)
            return false;
        String[] authSessionIDSet = authSessionIDs.split(",");
        boolean returnData = false;
        for (String authSessionID : authSessionIDSet) {
            try {
                if (secContext.activateAuthSession(authSessionID) == true) {
                    Platform.debug("authSession : %s activate!", authSessionID);
                    returnData = true;
                }
            } catch (SecurityException e) {
                Platform.warning("%s activate an error.%s", authSessionID, e);
            }
        }
        return returnData;
    }
}