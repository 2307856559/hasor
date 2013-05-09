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
import javax.servlet.ServletException;
import org.platform.context.AppContext;
public interface SecurityContext {
    /**��������ʾ�ĻỰ�����ǰ�̡߳�*/
    public boolean activateAuthSession(AuthSession activateAuthSession) throws SecurityException;
    /**��������ʾ�ĻỰ�����ǰ�̡߳�*/
    public boolean activateAuthSession(String authSessionID) throws SecurityException;
    /**����һ��Ȩ�޻Ự������Ϊʹ�õ���Ȩϵͳ*/
    public AuthSession createAuthSession() throws SecurityException;
    /**���ٷ���*/
    public void destroySecurity(AppContext appContext);
    /**�����û�������ʹӵ�ǰ�̻߳Ự�б��в��һỰ���ϡ�������Ϊ�ջ᷵�ص�ǰ�߳������еĻỰ����*/
    public AuthSession[] findCurrentAuthSession(RoleIdentity userIdentity);
    //
    public AppContext getAppContext();
    /**ͨ��AuthSessionID��ȡȨ�޻Ự�������ڷ��ؿա�*/
    public AuthSession getAuthSession(String authSessionID) throws SecurityException;
    /**��ȡ���빤��*/
    public Digest getCodeDigest(String name) throws SecurityException;
    /**��ȡ��ǰ�̰߳󶨵�Ȩ�޻Ự���ϡ�����ֵ������Ϊ�ա�*/
    public AuthSession[] getCurrentAuthSession();
    /**��ȡ�����ΪBlank�ĻỰ�������û�����δ��¼�ĻỰ��*/
    public AuthSession getCurrentBlankAuthSession();
    /**��ȡ��ǰ�����û���������ڵĻ���*/
    public AuthSession getCurrentGuestAuthSession();
    /**����uri��ȡ��������ת�����ࡣ*/
    public SecurityDispatcher getDispatcher(String requestPath) throws ServletException;
    /**��Permissionע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(Permission permission);
    /**��Stringע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(String permissionCode);
    /**����uri��ȡ�����ж�Ȩ�޵Ĺ��ܽӿڡ�*/
    public UriPatternMatcher getUriMatcher(String requestPath);
    /**�ӵ�ǰ�߳��л�ĻỰ��ȥ��ĳ���Ự��*/
    public boolean inactivationAuthSession(AuthSession authSession);
    /**�ӵ�ǰ�߳��л�ĻỰ��ȥ��ĳ���Ự��*/
    public boolean inactivationAuthSession(String sessionID);
    //
    /**��ʼ������*/
    public void initSecurity(AppContext appContext);
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery();
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(Permission permission);
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(SecurityNode testNode);
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(String permissionCode);
}