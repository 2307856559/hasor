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
 * ����Ȩ��ϵͳ�е��û��Ự���û��Ự�б������û�����֮���Ȩ�����ݡ�
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public interface AuthSession {
    /**��½�����ʺţ��÷����Ὣrecover��������Ϊfalse��*/
    public void doLoginGuest();
    /**��ָ�����û�������뵽Ȩ��ϵͳ���÷����Ὣrecover��������Ϊfalse��*/
    public void doLogin(UserInfo user) throws SecurityException;
    /**��ָ�����û�Code��½ϵͳ�����֧�֣����÷����Ὣrecover��������Ϊfalse��*/
    public void doLoginCode(String userCode) throws SecurityException;
    /**��ָ�����û��ʺ�����ϵͳ���÷����Ὣrecover��������Ϊfalse��*/
    public void doLogin(String account, String password) throws SecurityException;
    /**ִ���˳����÷����Ὣrecover��������Ϊfalse��*/
    public void doLogout() throws SecurityException;
    /**�رջỰ��*/
    public void close();
    //
    //
    //
    /**��ȡ�ỰID��*/
    public String getSessionID();
    /**��ȡ������û��������δ��¼ϵͳ���������������ʺ���᷵�������ʺš�*/
    public UserInfo getUserObject();
    /**��Ự���һ����ʱȨ�ޡ�*/
    public void addPermission(Permission permission);
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public void removeTempPermission(Permission permission);
    /**��ȡ�Ự�а���������Ȩ����Ϣ��*/
    public Permission[] getPermissions();
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public boolean hasPermission(Permission permission);
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public boolean hasPermission(String permissionCode);
    /**�Ƿ��Ѿ�����*/
    public boolean isLogin();
    /**�ж��Ƿ�Ϊ�����ʺš������ʺ���һ���û���ݣ�ͨ��������ʾ����Ҫ����ϵͳʱʹ�õ��û���
     * �û�ʹ�������ʺŵ���ϵͳ��Ȼ�Ѿ����뵫����ݲ������ŵ��붯����Ϊ�����û���*/
    public boolean isGuest();
    /**��ȡsession����ʱ��*/
    public long getCreatedTime();
    /**��ȡһ��ֵ����ֵ������session�Ƿ�֧�ִ�Cookie�лָ��Ự��*/
    public boolean supportCookieRecover();
    /**����true��ʾ֧�ֻỰ��Cookie�л�ָ���½��ÿ�����µ�½֮���ֵ���ᱻ����Ϊfalse��*/
    public void setSupportCookieRecover(boolean recover);
}