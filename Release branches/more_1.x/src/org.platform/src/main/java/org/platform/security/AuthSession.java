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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.util.StringUtil;
import org.platform.Assert;
import org.platform.Platform;
import org.platform.clock.Clock;
/**
 * ����Ȩ��ϵͳ�е��û��Ự���û��Ự�б������û�����֮���Ȩ�����ݡ�
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public class AuthSession {
    private String                  sessionID;
    private boolean                 cookieRecover;
    private UserInfo                userInfo;
    private Map<String, Permission> permissionMap;
    private SessionData             authSessionData;
    private SecurityContext         securityContext;
    private boolean                 isClose;
    //
    protected AuthSession(String sessionID, SecurityContext securityContext) {
        Assert.isLegal(!StringUtil.isBlank(sessionID), "sessionID is Undefined!");
        Assert.isNotNull(securityContext, "SecurityContext is Undefined!");
        this.sessionID = sessionID;
        this.securityContext = securityContext;
        this.isClose = false;
        this.permissionMap = new HashMap<String, Permission>();
    }
    /**��ȡSessionData*/
    protected SessionData getSessionData() {
        try {
            return this.authSessionData.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    /**��ȡ�ỰID��*/
    public String getSessionID() {
        return this.sessionID;
    };
    /**��ȡ������û��������δ��¼ϵͳ���������������ʺ���᷵�������ʺš�*/
    public UserInfo getUserObject() {
        try {
            this.checkClose();/*Check*/
            if (this.userInfo == null) {
                String userCode = this.authSessionData.getUserCode();
                if (StringUtil.isBlank(userCode) == false) {
                    String userFromAuth = this.authSessionData.getAuthSystem();
                    ISecurityAuth auth = this.getSecurityContext().getSecurityAuth(userFromAuth);
                    this.userInfo = auth.getUserInfo(userCode);
                }
            }
            return this.userInfo;
        } catch (SecurityException e) {
            Platform.debug(Platform.logString(e));
            return null;
        }
    };
    /**��ȡ��½�Ựʱʹ�õľ���Ȩ��ϵͳ��*/
    public String getAuthSystem() {
        if (getUserObject() != null)
            return this.authSessionData.getAuthSystem();
        return null;
    }
    /**��Ự���һ����ʱȨ�ޡ�*/
    public void addPermission(Permission permission) throws SecurityException {
        this.checkClose();/*Check*/
        this.permissionMap.put(permission.getPermissionCode(), permission);
        this.authSessionData.setLastTime(Clock.getSyncTime());
    };
    /**��Ự���һ����ʱȨ�ޡ�*/
    public void addPermission(String permissionCode) throws SecurityException {
        this.checkClose();/*Check*/
        if (StringUtil.isBlank(permissionCode))
            return;
        this.permissionMap.put(permissionCode, new Permission(permissionCode));
        this.authSessionData.setLastTime(Clock.getSyncTime());
    };
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public void removeTempPermission(Permission permission) throws SecurityException {
        this.checkClose();/*Check*/
        if (permission == null)
            return;
        this.permissionMap.remove(permission.getPermissionCode());
        this.authSessionData.setLastTime(Clock.getSyncTime());
    };
    /**��ʱ�����û��Ự��һ��Ȩ�ޡ�*/
    public void removeTempPermission(String permissionCode) throws SecurityException {
        this.checkClose();/*Check*/
        if (StringUtil.isBlank(permissionCode))
            return;
        this.permissionMap.remove(permissionCode);
        this.authSessionData.setLastTime(Clock.getSyncTime());
    };
    /**��ȡ�Ự�а���������Ȩ����Ϣ��*/
    public Permission[] getPermissionObjects() {
        return this.permissionMap.values().toArray(new Permission[this.permissionMap.size()]);
    };
    /**��ȡ�Ự�а���������Ȩ����Ϣ��*/
    public String[] getPermissions() {
        String[] pers = new String[this.permissionMap.size()];
        int i = 0;
        for (Permission per : this.permissionMap.values()) {
            pers[i] = per.getPermissionCode();
            i++;
        }
        return pers;
    };
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public boolean hasPermission(Permission permission) {
        return this.hasPermission(permission.getPermissionCode());
    };
    /**�жϻỰ���Ƿ����ָ��Ȩ�ޡ�*/
    public boolean hasPermission(String permissionCode) {
        return this.permissionMap.containsKey(permissionCode);
    };
    /**�Ƿ��Ѿ����롣*/
    public boolean isLogin() {
        if (this.isClose())
            return false;
        //
        UserInfo userInfo = this.getUserObject();
        return userInfo != null;
    };
    /**�ж��Ƿ�Ϊ�����ʺš������ʺ���һ���û���ݣ�ͨ��������ʾ����Ҫ����ϵͳʱʹ�õ��û���
     * �û�ʹ�������ʺŵ���ϵͳ��Ȼ�Ѿ����뵫����ݲ������ŵ��붯����Ϊ�����û���*/
    public boolean isGuest() {
        if (this.isClose())
            return false;
        //
        UserInfo userInfo = this.getUserObject();
        if (userInfo == null)
            return false;
        return userInfo.isGuest();
    };
    /**��ȡsession����ʱ��*/
    public long getLoginTime() throws SecurityException {
        if (this.isLogin() == true)
            return this.authSessionData.getLoginTime();
        else
            return -1;
    };
    /**��ȡһ��ֵ����ֵ������session�Ƿ�֧�ִ�Cookie�лָ��Ự��*/
    public boolean supportCookieRecover() {
        return this.cookieRecover;
    };
    /**����true��ʾ֧�ֻỰ��Cookie�л�ָ���½��ÿ�����µ�½֮���ֵ���ᱻ����Ϊfalse��*/
    public void setSupportCookieRecover(boolean cookieRecover) {
        this.cookieRecover = cookieRecover;
    };
    /**��ȡSecurityContext����*/
    protected SecurityContext getSecurityContext() {
        return this.securityContext;
    };
    /**���������е�Ȩ�����ݣ�����������Ȩ���ݡ�*/
    public synchronized void reloadPermission() throws SecurityException {
        this.checkClose();/*Check*/
        //
        ISecurityAccess access = this.getSecurityContext().getSecurityAccess(this.authSessionData.getAuthSystem());
        List<Permission> perList = access.loadPermission(this.getUserObject());
        if (perList != null)
            for (Permission per : perList)
                this.permissionMap.put(per.getPermissionCode(), per);
        this.authSessionData.setLastTime(Clock.getSyncTime());
    }
    /**��ָ�����û�������뵽Ȩ��ϵͳ�������½ʧ�ܻ��׳�SecurityException�����쳣��*/
    public synchronized void doLogin(String authSystem, UserInfo user) throws SecurityException {
        this.checkClose();/*Check*/
        Assert.isNotNull(user);
        this.doLoginCode(authSystem, user.getUserCode());
    };
    /**��ָ�����û�userCode���뵽Ȩ��ϵͳ�������½ʧ�ܻ��׳�SecurityException�����쳣��*/
    public synchronized void doLoginCode(String authSystem, String userCode) throws SecurityException {
        this.checkClose();/*Check*/
        ISecurityAuth authApi = this.getSecurityContext().getSecurityAuth(authSystem);
        if (authApi == null)
            throw new SecurityException("Not register " + authSystem + " ISecurityAuth.");
        UserInfo userInfo = authApi.getUserInfo(userCode);
        if (userInfo != null) {
            this.userInfo = userInfo;
            this.authSessionData.setUserCode(this.userInfo.getUserCode());//�û���ʶ��
            this.authSessionData.setAuthSystem(authSystem);
            this.authSessionData.setLoginTime(Clock.getSyncTime());//��½ʱ��
            this.authSessionData.setLastTime(Clock.getSyncTime());
            this.reloadPermission();/*����Ȩ��*/
            this.refreshCacheTime();
            return;
        }
        throw new SecurityException("unknown user!");
    };
    /**��ָ�����û��ʺ�����ϵͳ��*/
    public synchronized void doLogin(String authSystem, String account, String password) throws SecurityException {
        this.checkClose();/*Check*/
        ISecurityAuth authApi = this.getSecurityContext().getSecurityAuth(authSystem);
        if (authApi == null)
            throw new SecurityException("Not register " + authSystem + " ISecurityAuth.");
        UserInfo userInfo = authApi.getUserInfo(account, password);
        if (userInfo != null) {
            this.userInfo = userInfo;
            this.authSessionData.setUserCode(this.userInfo.getUserCode());//�û���ʶ��
            this.authSessionData.setAuthSystem(authSystem);
            this.authSessionData.setLoginTime(Clock.getSyncTime());//��½ʱ��
            this.authSessionData.setLastTime(Clock.getSyncTime());
            this.reloadPermission();/*����Ȩ��*/
            this.refreshCacheTime();
            return;
        }
        throw new SecurityException("unknown user!");
    };
    /**ִ���˳���*/
    public synchronized void doLogout() throws SecurityException {
        this.checkClose();/*Check*/
        this.cookieRecover = false;
        this.authSessionData = new SessionData();
        this.userInfo = null;
        this.permissionMap.clear();
        this.getSecurityContext().removeSessionData(this.sessionID);
    };
    /**�رջỰ���˳��Ự�����Ҵӵ�ǰ�߳���ע������*/
    public synchronized void close() throws SecurityException {
        this.checkClose();/*Check*/
        this.doLogout();
        this.getSecurityContext().inactivationAuthSession(this);
        this.isClose = true;
    }
    /**�жϻỰ�Ƿ�ر�*/
    public boolean isClose() {
        return this.isClose;
    }
    private void checkClose() throws SecurityException {
        if (isClose())
            throw new SecurityException("AuthSession is closed!");
    }
    /**ˢ��Ȩ�������ڻ����е�ʱ��,(δ��¼\�����ʺ�\�Ѿ��ر�)����ǰ���������������һ��ʱ�������򻺴������¡�*/
    protected synchronized void refreshCacheTime() {
        if (this.isClose() == true || this.isLogin() == false)
            return;
        SessionData cacheData = this.getSecurityContext().getSessionData(this.sessionID);
        if (cacheData == null || cacheData.getLastTime() < this.authSessionData.getLastTime()) {
            String[] proArray = this.permissionMap.keySet().toArray(new String[this.permissionMap.size()]);
            this.authSessionData.setPermissionSet(proArray);
            this.getSecurityContext().updateSessionData(this.sessionID, this.authSessionData);
        } else
            this.getSecurityContext().updateSessionData(this.sessionID);
    }
    /**װ��Ȩ������*/
    protected void loadSessionData(SessionData sessionData) {
        this.permissionMap.clear();
        String[] perArray = sessionData.getPermissionSet();
        if (perArray != null)
            for (String per : perArray)
                this.permissionMap.put(per, new Permission(per));
        if (this.authSessionData == null)
            this.authSessionData = sessionData;
        this.authSessionData.setLastTime(Clock.getSyncTime());
    }
}