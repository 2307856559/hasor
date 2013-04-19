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
/**
 * ����Ȩ��ϵͳ�е��û��Ự���û��Ự�б������û���½֮���Ȩ�����ݡ�
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public interface AuthSession {
    /**��ȡ�ỰID*/
    public String getSessionID();
    /**��ȡ��½���û��������δ��¼ϵͳ���������������ʺ���᷵�������ʺš�*/
    public UserInfo getUserObject();
    /**��ָ�����û������½��Ȩ��ϵͳ��*/
    public boolean doLogin(UserInfo user) throws SecurityException;
    /**��ָ�����û��ʺ�����ϵͳ��*/
    public boolean doLogin(String account, String password) throws SecurityException;
    /**ִ���˳���*/
    public boolean doLogout() throws SecurityException;
    /**��Ự���һ����ʱȨ�ޡ�*/
    public void addPermission(Permission permission);
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public void removeTempPermission(Permission permission);
    /**��ȡ�Ự�а���������Ȩ����Ϣ��*/
    public Permission[] getPermissions();
    /**�Ƿ��Ѿ���½*/
    public boolean isLogin();
    /**�ж��Ƿ�Ϊ�����ʺţ��������������ʺţ�������δ��½ϵͳ������¸�ֵ����Ϊtrue��*/
    public boolean isGuest();
    /**�رջỰ*/
    public void close();
}