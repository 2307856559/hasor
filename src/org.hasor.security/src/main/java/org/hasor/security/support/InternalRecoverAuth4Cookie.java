/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.hasor.security.support;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hasor.Hasor;
import org.hasor.security.AuthSession;
import org.hasor.security.SecurityContext;
import org.hasor.security.SecurityException;
import org.hasor.security.support.CookieDataUtil.CookieUserData;
import org.more.util.StringUtils;
import com.google.inject.Inject;
/**
 * 
 * @version : 2013-4-25
 * @author ������ (zyc@byshell.org)
 */
class InternalRecoverAuth4Cookie {
    protected SecuritySettings settings = null;
    @Inject
    public InternalRecoverAuth4Cookie(SecuritySettings settings) {
        this.settings = settings;
    };
    /**�ָ�Ȩ��*/
    public AuthSession[] recoverCookie(SecurityContext secContext, HttpServletRequest request, HttpServletResponse response) throws SecurityException {
        //1.�ָ��Ự
        boolean recoverMark = this.recoverAuthSession4HttpSession(secContext, request.getSession(true));
        if (recoverMark == false)
            recoverMark = this.recoverAuthSession4Cookie(secContext, request);
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
                Hasor.warning("%s", e);
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
            Hasor.warning("recover cookieUser failure! userCode=%s", userCode);
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
        String cookieValue = null;
        Cookie[] cookieArray = httpRequest.getCookies();
        if (cookieArray != null)
            for (Cookie cookie : cookieArray) {
                //ƥ��cookie����
                if (cookie.getName().endsWith(this.settings.getCookieName()) == false)
                    continue;
                cookieValue = cookie.getValue();
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
            Hasor.debug("parseJson to CookieDataUtil error! %s decode. cookieValue=%s", this.settings.getCookieEncryptionEncodeType(), cookieValue);
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
        String authSessionIDs = (String) httpSession.getAttribute(AuthSession.HttpSessionAuthSessionSetName);
        if (StringUtils.isBlank(authSessionIDs) == true)
            return false;
        String[] authSessionIDSet = authSessionIDs.split(",");
        boolean returnData = false;
        for (String authSessionID : authSessionIDSet) {
            try {
                if (secContext.activateAuthSession(authSessionID) == true) {
                    Hasor.debug("authSession : %s activate!", authSessionID);
                    returnData = true;
                }
            } catch (SecurityException e) {
                Hasor.warning("%s activate an error.%s", authSessionID, e);
            }
        }
        return returnData;
    }
}