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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.platform.context.AppContext;
import org.platform.security.AuthSession;
import org.platform.security.Digest;
import org.platform.security.SecurityAccess;
import org.platform.security.SecurityAuth;
import org.platform.security.Permission;
import org.platform.security.RoleIdentity;
import org.platform.security.SecurityContext;
import org.platform.security.SecurityDispatcher;
import org.platform.security.SecurityException;
import org.platform.security.SecurityNode;
import org.platform.security.SecurityQuery;
import org.platform.security.UriPatternMatcher;
import org.platform.security.UserInfo;
import org.platform.security.support.DefaultSecurityQuery.CheckPermission;
/**
 * ��ȫ��֤ϵͳ��������ֻ��Ҫ���SessionData�洢�Ϳ����ˡ�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractSecurityContext implements SecurityContext {
    private AppContext                       appContext               = null;
    private InternalCodeDigestManager        codeDigestManager        = null;
    private ThreadLocal<List<AuthSession>>   currentAuthSessionList   = null;
    private UriPatternMatcher                defaultRules             = null;
    private InternalDispatcherManager        dispatcherManager        = null;
    private ManagedSecurityAccessManager     securityAccessManager    = null;
    private ManagedSecurityAuthManager       securityAuthManager      = null;
    private SecuritySettings                 settings                 = null;
    private InternalUriPatternMatcherManager uriPatternMatcherManager = null;
    @Override
    public synchronized boolean activateAuthSession(AuthSession activateAuthSession) throws SecurityException {
        if (activateAuthSession == null)
            return false;
        //
        List<AuthSession> curSessionList = this.currentAuthSessionList.get();
        if (curSessionList == null) {
            curSessionList = new ArrayList<AuthSession>();
            this.currentAuthSessionList.set(curSessionList);
        }
        //
        String activateAuthSessionID = activateAuthSession.getSessionID();
        for (AuthSession authSession : curSessionList) {
            if (authSession.getSessionID().equals(activateAuthSessionID) == true)
                return false;
        }
        //
        curSessionList.add(activateAuthSession);
        this.throwEvent(SecurityEventDefine.AuthSession_Activate, activateAuthSession);/*�׳��¼�*/
        //
        return true;
    }
    @Override
    public synchronized boolean activateAuthSession(String authSessionID) throws SecurityException {
        AuthSession authSession = this.getAuthSession(authSessionID);/*�÷�����������סauthSessionID�Ķ���*/
        if (authSession != null)
            return this.activateAuthSession(authSession);
        return false;
    }
    @Override
    public synchronized AuthSession createAuthSession() throws SecurityException {
        AbstractAuthSession newAuthSession = this.newAuthSession(UUID.randomUUID().toString());
        newAuthSession.loadSessionData(new SessionData());
        this.activateAuthSession(newAuthSession);
        return newAuthSession;
    }
    @Override
    public synchronized void destroySecurity(AppContext appContext) {
        this.dispatcherManager.destroyManager(appContext);
        this.uriPatternMatcherManager.destroyManager(appContext);
        this.codeDigestManager.destroyManager(appContext);
        this.securityAuthManager.destroyManager(appContext);
        this.securityAccessManager.destroyManager(appContext);
        //
        this.currentAuthSessionList = new ThreadLocal<List<AuthSession>>();
    };
    @Override
    public AuthSession[] findCurrentAuthSession(RoleIdentity userIdentity) {
        AuthSession[] authSessionArray = getCurrentAuthSession();
        if (userIdentity == null)
            return authSessionArray;
        ArrayList<AuthSession> finds = new ArrayList<AuthSession>();
        for (AuthSession authSession : authSessionArray) {
            UserInfo userInfo = authSession.getUserObject();
            if (userInfo == null)
                continue;
            if (userInfo.getIdentity().equals(userIdentity) == true)
                finds.add(authSession);
        }
        return finds.toArray(new AuthSession[finds.size()]);
    };
    //
    public String getRequestURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        return requestURI.substring(contextPath.length());
    }
    @Override
    public AppContext getAppContext() {
        return appContext;
    };
    @Override
    public AuthSession getAuthSession(String authSessionID) throws SecurityException {
        SessionData sessionData = this.getSessionData(authSessionID);
        if (sessionData != null) {
            AbstractAuthSession newAuthSession = this.newAuthSession(authSessionID);
            newAuthSession.loadSessionData(sessionData);
            return newAuthSession;
        }
        return null;
    }
    @Override
    public Digest getCodeDigest(String name) throws SecurityException {
        Digest digest = this.codeDigestManager.getCodeDigest(name);
        if (digest == null)
            throw new SecurityException("CodeDigest :" + name + " is Undefined.");
        return digest;
    };
    @Override
    public AuthSession[] getCurrentAuthSession() {
        List<AuthSession> curSessionMap = this.currentAuthSessionList.get();
        if (curSessionMap == null || curSessionMap.size() == 0)
            return new AuthSession[0];
        else {
            return curSessionMap.toArray(new AuthSession[curSessionMap.size()]);
        }
    }
    @Override
    public AuthSession getCurrentBlankAuthSession() {
        AuthSession[] authList = getCurrentAuthSession();
        if (authList != null)
            for (AuthSession auth : authList)
                if (auth.isBlank() == true)
                    return auth;
        return null;
    };
    @Override
    public AuthSession getCurrentGuestAuthSession() {
        AuthSession[] authList = getCurrentAuthSession();
        if (authList != null)
            for (AuthSession auth : authList)
                if (auth.isGuest() == true)
                    return auth;
        return null;
    };
    @Override
    public SecurityDispatcher getDispatcher(String requestPath) throws ServletException {
        return this.dispatcherManager.getDispatcher(requestPath);
    }
    /**��ȡ{@link SecurityAccess}�ӿڶ�����������ڷ���null��*/
    protected SecurityAccess getSecurityAccess(String authName) {
        return securityAccessManager.getSecurityAccess(authName, this.appContext);
    }
    /**��ȡ{@link SecurityAuth}�ӿڶ�����������ڷ���null��*/
    protected SecurityAuth getSecurityAuth(String authName) throws SecurityException {
        return securityAuthManager.getSecurityAuth(authName, this.appContext);
    };
    @Override
    public SecurityNode getSecurityCondition(Permission permission) {
        return new CheckPermission(permission);
    }
    @Override
    public SecurityNode getSecurityCondition(String permissionCode) {
        return new CheckPermission(new Permission(permissionCode));
    };
    /**ʹ��SessionData�����ݴ���AuthSession��*/
    protected abstract SessionData getSessionData(String sessionDataID);
    @Override
    public UriPatternMatcher getUriMatcher(String requestPath) {
        UriPatternMatcher matcher = this.uriPatternMatcherManager.getUriMatcher(requestPath);
        return (matcher == null) ? this.defaultRules : matcher;
    }
    @Override
    public synchronized boolean inactivationAuthSession(AuthSession authSession) {
        return this.inactivationAuthSession(authSession.getSessionID());
    }
    @Override
    public synchronized boolean inactivationAuthSession(String sessionID) {
        List<AuthSession> curSessionList = this.currentAuthSessionList.get();
        if (curSessionList == null)
            return false;
        //
        AuthSession removeAuthSession = null;
        for (AuthSession authSession : curSessionList) {
            if (authSession.getSessionID().equals(sessionID) == true)
                removeAuthSession = authSession;
        }
        //
        if (removeAuthSession != null) {
            curSessionList.remove(removeAuthSession);
            if (curSessionList.size() == 0)
                this.currentAuthSessionList.remove();
            this.throwEvent(SecurityEventDefine.AuthSession_Inactivation, removeAuthSession);/*�׳��¼�*/
            return true;
        }
        //
        return false;
    }
    //
    @Override
    public synchronized void initSecurity(AppContext appContext) {
        this.appContext = appContext;
        //
        this.dispatcherManager = new InternalDispatcherManager();
        this.uriPatternMatcherManager = new InternalUriPatternMatcherManager();
        this.codeDigestManager = new InternalCodeDigestManager();
        this.securityAuthManager = new ManagedSecurityAuthManager();
        this.securityAccessManager = new ManagedSecurityAccessManager();
        //
        this.settings = appContext.getInstance(SecuritySettings.class);
        this.defaultRules = settings.getRulesDefault();
        //
        this.dispatcherManager.initManager(appContext);
        this.uriPatternMatcherManager.initManager(appContext);
        this.codeDigestManager.initManager(appContext);
        this.securityAuthManager.initManager(appContext);
        this.securityAccessManager.initManager(appContext);
        //
        this.currentAuthSessionList = new ThreadLocal<List<AuthSession>>();
    };
    /**�½�AuthSession*/
    protected AbstractAuthSession newAuthSession(String authSessionID) throws SecurityException {
        AbstractAuthSession newAuthSession = new AbstractAuthSession(authSessionID, this) {};
        this.throwEvent(SecurityEventDefine.AuthSession_New, newAuthSession);/*�׳��¼�*/
        return newAuthSession;
    }
    @Override
    public SecurityQuery newSecurityQuery() {
        return this.appContext.getGuice().getInstance(SecurityQuery.class);
    }
    @Override
    public SecurityQuery newSecurityQuery(Permission permission) {
        return this.newSecurityQuery().and(permission);
    }
    @Override
    public SecurityQuery newSecurityQuery(SecurityNode testNode) {
        return this.newSecurityQuery().andCustomer(testNode);
    }
    @Override
    public SecurityQuery newSecurityQuery(String permissionCode) {
        return this.newSecurityQuery().and(permissionCode);
    }
    //
    /**ͬ����ʽ�׳��¼���*/
    protected abstract void throwEvent(String eventType, Object... objects);
    //
    /**ʹ��SessionData�����ݴ���AuthSession��*/
    protected abstract void removeSessionData(String sessionDataID);
    /**����ˢ�»������ݵĹ���ʱ��*/
    protected abstract void updateSessionData(String sessionID);
    /**����SessionData*/
    protected abstract void updateSessionData(String sessionDataID, SessionData newSessionData);
}