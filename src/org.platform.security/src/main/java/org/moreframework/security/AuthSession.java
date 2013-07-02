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
package org.moreframework.security;
/**
 * ����Ȩ��ϵͳ�е��û��Ự���û��Ự�б������û�����֮���Ȩ�����ݡ�
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public interface AuthSession {
    public static final String HttpSessionAuthSessionSetName = AuthSession.class.getName();
    /**��ȡ�ỰID��*/
    public abstract String getSessionID();
    /**��ȡ������û��������δ��¼ϵͳ���������������ʺ���᷵�������ʺš�*/
    public abstract UserInfo getUserObject();
    /**��ȡ��½�Ựʱʹ�õľ���Ȩ��ϵͳ��*/
    public abstract String getAuthSystem();
    /**��Ự���һ����ʱȨ�ޡ�*/
    public abstract void addPermission(Permission permission) throws SecurityException;
    /**��Ự���һ����ʱȨ�ޡ�*/
    public abstract void addPermission(String permissionCode) throws SecurityException;
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public abstract void removeTempPermission(Permission permission) throws SecurityException;
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public abstract void removeTempPermission(String permissionCode) throws SecurityException;
    /**��ȡ�Ự�а���������Ȩ����Ϣ��*/
    public abstract Permission[] getPermissionObjects();
    /**��ȡ�Ự�а���������Ȩ����Ϣ��*/
    public abstract String[] getPermissions();
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public abstract boolean hasPermission(Permission permission);
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public abstract boolean hasPermission(String permissionCode);
    /**�жϻỰ�Ƿ�ر�*/
    public abstract boolean isClose();
    /**�Ƿ��Ѿ����롣*/
    public abstract boolean isLogin();
    /**�ж��Ƿ�Ϊ�����ʺš������ʺ���һ���û���ݣ�ͨ��������ʾ����Ҫ����ϵͳʱʹ�õ��û���
     * �û�ʹ�������ʺŵ���ϵͳ��Ȼ�Ѿ����뵫����ݲ������ŵ��붯����Ϊ�����û���*/
    public abstract boolean isGuest();
    /**�Ƿ�Ϊ�հ�״̬���µ�Session���˳�֮���.������δ�رյġ�*/
    public abstract boolean isBlank();
    /**��ȡsession����ʱ��*/
    public abstract long getLoginTime() throws SecurityException;
    /**���������е�Ȩ�����ݣ�����������Ȩ���ݡ�*/
    public abstract void reloadPermission() throws SecurityException;
    /**��ָ�����û�������뵽Ȩ��ϵͳ�������½ʧ�ܻ��׳�SecurityException�����쳣��*/
    public abstract void doLogin(String authSystem, UserInfo user) throws SecurityException;
    /**��ָ�����û�userCode���뵽Ȩ��ϵͳ�������½ʧ�ܻ��׳�SecurityException�����쳣��*/
    public abstract void doLoginCode(String authSystem, String userCode) throws SecurityException;
    /**��ָ�����û��ʺ�����ϵͳ��*/
    public abstract void doLogin(String authSystem, String account, String password) throws SecurityException;
    /**ִ���˳���*/
    public abstract void doLogout() throws SecurityException;
    /**�رջỰ���˳��Ự�����Ҵӵ�ǰ�߳���ע������*/
    public abstract void close() throws SecurityException;
    /**ˢ��Ȩ�������ڻ����е�ʱ��,(δ��¼\�����ʺ�\�Ѿ��ر�)����ǰ���������������һ��ʱ�������򻺴������¡�*/
    public abstract void refreshCacheTime();
}