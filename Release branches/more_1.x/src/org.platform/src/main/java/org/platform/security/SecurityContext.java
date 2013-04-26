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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.platform.Assert;
import org.platform.context.AppContext;
/**
 * ��ȫ��֤ϵͳ����
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class SecurityContext {
    private InternalDispatcherManager             dispatcherManager        = null;
    private InternalUriPatternMatcherManager      uriPatternMatcherManager = null;
    private InternalCodeDigestManager             codeDigestManager        = null;
    private UriPatternMatcher                     defaultRules             = null;
    private InternalSecurityQueryBuilder          securityQueryBuilder     = null; //������SecurityQuer�ӿ���ط�����
    private volatile Map<String, AuthSession>     authSessionMap           = null;
    private ThreadLocal<Map<String, AuthSession>> currentAuthSessionMap    = null;
    //
    public void initSecurity(AppContext appContext) {
        this.dispatcherManager = new InternalDispatcherManager();
        this.uriPatternMatcherManager = new InternalUriPatternMatcherManager();
        this.codeDigestManager = new InternalCodeDigestManager();
        //
        SecuritySettings setting = appContext.getBean(SecuritySettings.class);
        this.defaultRules = setting.getRulesDefault();
        //
        this.securityQueryBuilder = new InternalSecurityQueryBuilder(appContext);
        //
        this.dispatcherManager.initManager(appContext);
        this.uriPatternMatcherManager.initManager(appContext);
        this.codeDigestManager.initManager(appContext);
        //
        this.authSessionMap = new HashMap<String, AuthSession>();
        this.currentAuthSessionMap = new ThreadLocal<Map<String, AuthSession>>();
    }
    public void destroySecurity(AppContext appContext) {
        this.dispatcherManager.destroyManager(appContext);
        this.uriPatternMatcherManager.destroyManager(appContext);
        this.codeDigestManager.destroyManager(appContext);
        this.authSessionMap.clear();
        this.currentAuthSessionMap = new ThreadLocal<Map<String, AuthSession>>();
    }
    /**����һ��Ȩ�޻Ự��*/
    public AuthSession createAuthSession() {
        AuthSession newAuthSession = this.newAuthSession();
        Assert.isNotNull(newAuthSession);
        String sessionID = newAuthSession.getSessionID();
        this.authSessionMap.put(sessionID, newAuthSession);
        this.activateAuthSession(sessionID);
        return newAuthSession;
    };
    /**�����»Ự�Ĵ�������������»Ự�����͡�*/
    protected abstract AuthSession newAuthSession();
    /**����Ự�Ĺر�����������»Ự�����͡�*/
    protected abstract void closeAuthSession(AuthSession authSession);
    /**ͨ��AuthSessionID��ȡȨ�޻Ự��*/
    public AuthSession getAuthSession(String authSessionID) {
        return this.authSessionMap.get(authSessionID);
    };
    /**�ж�Ȩ��ϵͳ���Ƿ����ָ��ID��Ȩ�޻Ự������з���true.*/
    public boolean hasAuthSession(String authSessionID) {
        return this.authSessionMap.containsKey(authSessionID);
    };
    /**��������ʾ�ĻỰ�����ǰ�̡߳�*/
    public synchronized boolean activateAuthSession(String authSessionID) {
        Map<String, AuthSession> curSessionMap = this.currentAuthSessionMap.get();
        if (curSessionMap == null) {
            curSessionMap = new HashMap<String, AuthSession>();
            this.currentAuthSessionMap.set(curSessionMap);
        }
        if (this.hasAuthSession(authSessionID) == true && curSessionMap.containsKey(authSessionID) == false) {
            AuthSession authSession = this.getAuthSession(authSessionID);
            curSessionMap.put(authSessionID, authSession);
            return true;
        }
        return false;
    };
    /**�ӵ�ǰ�߳��л�ĻỰ��ȥ��ĳ���Ự��*/
    public synchronized boolean inactivationAuthSession(String sessionID) {
        Map<String, AuthSession> curSessionMap = this.currentAuthSessionMap.get();
        if (curSessionMap == null)
            return false;
        //
        boolean returnData = false;
        if (curSessionMap.containsKey(sessionID) == true) {
            curSessionMap.remove(sessionID);
            returnData = true;
        }
        //
        if (curSessionMap.size() == 0)
            this.currentAuthSessionMap.remove();
        //
        return returnData;
    };
    /**��ȡ��ǰ�̰߳󶨵�Ȩ�޻Ự���ϡ�����ֵ������Ϊ�ա�*/
    public AuthSession[] getCurrentAuthSession() {
        Map<String, AuthSession> curSessionMap = this.currentAuthSessionMap.get();
        if (curSessionMap == null || curSessionMap.size() == 0)
            return new AuthSession[0];
        else {
            Collection<AuthSession> curAuthSessionSet = curSessionMap.values();
            return curAuthSessionSet.toArray(new AuthSession[curAuthSessionSet.size()]);
        }
    };
    /**��ȡ���빤��*/
    public CodeDigest getCodeDigest(String name) throws SecurityException {
        CodeDigest digest = this.codeDigestManager.getCodeDigest(name);
        if (digest == null)
            throw new SecurityException("CodeDigest :" + name + " is Undefined.");
        return digest;
    };
    /**����uri��ȡ�����ж�Ȩ�޵Ĺ��ܽӿڡ�*/
    public UriPatternMatcher getUriMatcher(String requestPath) {
        UriPatternMatcher matcher = this.uriPatternMatcherManager.getUriMatcher(requestPath);
        return (matcher == null) ? this.defaultRules : matcher;
    }
    /**����uri��ȡ��������ת�����ࡣ*/
    public SecurityDispatcher getDispatcher(String requestPath) throws ServletException {
        SecurityDispatcher dispatcher = this.dispatcherManager.getDispatcher(requestPath);
        if (dispatcher == null)
            throw new ServletException("no match SecurityDispatcher to " + requestPath + "");
        return dispatcher;
    };
    /**��Permissionע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(Permission permission) {
        return this.securityQueryBuilder.getSecurityCondition(permission);
    }
    /**��Stringע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(String permissionCode) {
        return this.securityQueryBuilder.getSecurityCondition(permissionCode);
    }
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery() {
        return this.securityQueryBuilder.newSecurityQuery();
    };
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(Permission permission) {
        return this.securityQueryBuilder.newSecurityQuery(permission);
    }
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(String permissionCode) {
        return this.securityQueryBuilder.newSecurityQuery(permissionCode);
    }
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(SecurityNode testNode) {
        return this.securityQueryBuilder.newSecurityQuery(testNode);
    }
}