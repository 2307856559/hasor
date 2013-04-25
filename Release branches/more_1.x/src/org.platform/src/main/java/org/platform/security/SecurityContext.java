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
import java.util.Map;
import javax.servlet.ServletException;
import org.platform.context.AppContext;
/**
 * �����ṩ�˻�ȡ�뵱ǰ�߳̽��а󶨵�{@link AuthSession}��
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class SecurityContext {
    private InternalDispatcherManager        dispatcherManager        = null;
    private InternalUriPatternMatcherManager uriPatternMatcherManager = null;
    private UriPatternMatcher                defaultRules             = null;
    private Map<String, CodeDigest>          codeDigestMap            = null;
    private InternalSecurityQueryBuilder     securityQueryBuilder     = null; //������SecurityQuer�ӿ���ط�����
    //
    public void initSecurity(AppContext appContext) {
        this.securityQueryBuilder = new InternalSecurityQueryBuilder(appContext);
        this.dispatcherManager = new InternalDispatcherManager();
        this.uriPatternMatcherManager = new InternalUriPatternMatcherManager();
        //
        this.dispatcherManager.initManager(appContext);
        this.uriPatternMatcherManager.initManager(appContext);
        //
        SecuritySettings setting = appContext.getBean(SecuritySettings.class);
        this.defaultRules = setting.getRulesDefault();
    }
    public void destroySecurity(AppContext appContext) {
        this.codeDigestMap.clear();
    }
    /**����һ��Ȩ�޻Ự��*/
    public abstract AuthSession createAuthSession();
    /**ͨ��AuthSessionID��ȡȨ�޻Ự��*/
    public AuthSession getAuthSession(String authSessionID);
    /**�ж�Ȩ��ϵͳ���Ƿ����ָ��ID��Ȩ�޻Ự������з���true.*/
    public boolean hasAuthSession(String authSessionID);
    /**����authSessionID������ʾ��Ȩ�޻Ự���������Ȩ�޻Ự�����ڵ�ǰ�̻߳Ự�С�*/
    public void activateAuthSession(String authSessionID);
    /***/
    public void inactivationAuthSession(String sessionID);
    /**��ȡ��ǰ�̰߳󶨵�Ȩ�޻Ự���ϡ�*/
    public AuthSession[] getCurrentAuthSession();
    /**��ȡ���빤��*/
    public CodeDigest getCodeDigest(String name) throws SecurityException {
        if (this.codeDigestMap.containsKey(name) == true)
            return this.codeDigestMap.get(name);
        throw new SecurityException("CodeDigest :" + name + " is Undefined.");
    };
    //
    //
    //
    //
    //
    //
    //
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