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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * �����ṩ�˻�ȡ�뵱ǰ�߳̽��а󶨵�{@link AuthSession}��
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityContext {
    /**��ȡ�򴴽�һ��{@link AuthSession}�ӿڡ�*/
    public AuthSession getAuthSession(HttpServletRequest request, HttpServletResponse response, boolean created);
    /**��ȡ�򴴽�һ��{@link AuthSession}�ӿڡ�*/
    public AuthSession getAuthSession(String authSessionID, boolean created);
    /**��ȡ�򴴽�һ��{@link AuthSession}�ӿڡ�*/
    public AuthSession getAuthSession(HttpSession session, boolean created);
    /**��ȡ��ǰ��Ȩ�޻Ự��*/
    public AuthSession getCurrentAuthSession();
    /**����uri��ȡ�����ж�Ȩ�޵Ĺ��ܽӿڡ�*/
    public UriPatternMatcher getUriMatcher(String requestPath);
    /**����uri��ȡ��������ת�����ࡣ*/
    public SecurityDispatcher getDispatcher(String requestPath);
    //
    /**��Permissionע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(Permission permission);
    /**��Stringע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(String permissionCode);
    //
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery();
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(Permission permission);
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(String permissionCode);
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(SecurityNode testNode);
}