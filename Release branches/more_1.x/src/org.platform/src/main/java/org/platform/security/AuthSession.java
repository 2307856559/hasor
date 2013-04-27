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
import java.util.HashSet;
import org.more.util.StringUtil;
import org.platform.Assert;
/**
 * ����Ȩ��ϵͳ�е��û��Ự���û��Ự�б������û�����֮���Ȩ�����ݡ�
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public abstract class AuthSession {
    private SessionData     sessionData     = null;
    private UserInfo        userInfo        = null;
    private SecurityContext securityContext = null;
    //
    public AuthSession(SessionData authSessionData, SecurityContext securityContext) {
        this.sessionData = authSessionData;
        this.securityContext = securityContext;
        Assert.isNotNull(authSessionData);
        Assert.isNotNull(securityContext);
    }
    /**��ȡ�ỰID��*/
    public String getSessionID() {
        return this.sessionData.getSessionID();
    };
    /**��ȡ������û��������δ��¼ϵͳ���������������ʺ���᷵�������ʺš�*/
    public UserInfo getUserObject() {
        if (this.userInfo == null) {
            String userCode = this.sessionData.getUserCode();
            if (StringUtil.isBlank(userCode) == false) {
                String userAuthCode = this.sessionData.getFromAuth();
                ISecurityAuth auth = this.getSecurityContext().getSecurityAuth(userAuthCode);
                this.userInfo = auth.getUserInfo(userCode);
            }
        }
        return this.userInfo;
    };
    /**��Ự���һ����ʱȨ�ޡ�*/
    public void addPermission(Permission permission) {
        this.addPermission(permission.getPermissionCode());
    };
    /**��Ự���һ����ʱȨ�ޡ�*/
    public void addPermission(String permissionCode) {
        String[] pers = this.sessionData.getPermissionSet();
        HashSet<String> persArray = new HashSet<String>();
        for (String per : pers)
            persArray.add(per);
        persArray.add(permissionCode);
        pers = persArray.toArray(new String[persArray.size()]);
        this.sessionData.setPermissionSet(pers);
        this.getSecurityContext().updateSessionData(this.sessionData);
    };
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public void removeTempPermission(Permission permission) {
        this.removeTempPermission(permission.getPermissionCode());
    };
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public void removeTempPermission(String permissionCode) {
        String[] pers = this.sessionData.getPermissionSet();
        HashSet<String> persArray = new HashSet<String>();
        for (String per : pers)
            if (per.equals(permissionCode) == false)
                persArray.add(per);
        pers = persArray.toArray(new String[persArray.size()]);
        this.sessionData.setPermissionSet(pers);
        this.getSecurityContext().updateSessionData(this.sessionData);
    };
    /**��ȡ�Ự�а���������Ȩ����Ϣ��*/
    public String[] getPermissions() {
        String[] pers = this.sessionData.getPermissionSet();
        if (pers == null)
            return new String[0];
        else
            return pers;
    };
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public boolean hasPermission(Permission permission) {
        return this.hasPermission(permission.getPermissionCode());
    };
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public boolean hasPermission(String permissionCode) {
        String[] pers = this.sessionData.getPermissionSet();
        for (String per : pers)
            if (per.equals(permissionCode) == true)
                return true;
        return false;
    };
    /**�Ƿ��Ѿ�����*/
    public boolean isLogin() {
        return this.sessionData.isLoginMark();
    };
    /**�ж��Ƿ�Ϊ�����ʺš������ʺ���һ���û���ݣ�ͨ��������ʾ����Ҫ����ϵͳʱʹ�õ��û���
     * �û�ʹ�������ʺŵ���ϵͳ��Ȼ�Ѿ����뵫����ݲ������ŵ��붯����Ϊ�����û���*/
    public boolean isGuest() {
        if (this.userInfo == null)
            return false;
        return this.userInfo.isGuest();
    };
    /**��ȡsession����ʱ��*/
    public long getCreatedTime() {
        return this.sessionData.getCreatedTime();
    };
    /**��ȡһ��ֵ����ֵ������session�Ƿ�֧�ִ�Cookie�лָ��Ự��*/
    public boolean supportCookieRecover() {
        return this.sessionData.isCookieRecover();
    };
    /**����true��ʾ֧�ֻỰ��Cookie�л�ָ���½��ÿ�����µ�½֮���ֵ���ᱻ����Ϊfalse��*/
    public void setSupportCookieRecover(boolean cookieRecover) {
        this.sessionData.setCookieRecover(cookieRecover);
        this.getSecurityContext().updateSessionData(this.sessionData);
    };
    protected SecurityContext getSecurityContext() {
        return this.securityContext;
    };
    //
    //
    //
    //
    //
    /**��½�����ʺţ��÷����Ὣrecover��������Ϊfalse��*/
    public synchronized void doLoginGuest(){};
    /**��ָ�����û�������뵽Ȩ��ϵͳ���÷����Ὣrecover��������Ϊfalse��*/
    public synchronized void doLogin(UserInfo user) throws SecurityException{};
    /**��ָ�����û�Code��½ϵͳ�����֧�֣����÷����Ὣrecover��������Ϊfalse��*/
    public synchronized void doLoginCode(String userCode) throws SecurityException{};
    /**��ָ�����û��ʺ�����ϵͳ���÷����Ὣrecover��������Ϊfalse��*/
    public synchronized void doLogin(String account, String password) throws SecurityException{};
    /**ִ���˳����÷����Ὣrecover��������Ϊfalse��*/
    public synchronized void doLogout() throws SecurityException{};
    /**�رջỰ��*/
    public synchronized void close(){};
}