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
package org.hasor.security.support;
/**
 * Ȩ�޻Ự����
 * @version : 2013-4-27
 * @author ������ (zyc@byshell.org)
 */
public class SessionData implements Cloneable {
    private String   userCode      = null;                      //�û���ʶ��
    private String   authSystem    = null;                      //��ȨϵͳID
    private String[] permissionSet = null;                      //�Ự��Ȩ��
    private long     loginTime     = 0;                         //��½ʱ��
    private long     lastTime      = System.currentTimeMillis(); //������ʱ��
    /*--------------------------------------------------*/
    @Override
    public SessionData clone() throws CloneNotSupportedException {
        SessionData newData = new SessionData();
        newData.userCode = newData.userCode;
        newData.authSystem = newData.authSystem;
        newData.permissionSet = newData.permissionSet.clone();
        newData.loginTime = newData.loginTime;
        newData.lastTime = newData.lastTime;
        return newData;
    }
    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public String getAuthSystem() {
        return authSystem;
    }
    public void setAuthSystem(String authSystem) {
        this.authSystem = authSystem;
    }
    public String[] getPermissionSet() {
        return permissionSet;
    }
    public void setPermissionSet(String[] permissionSet) {
        this.permissionSet = permissionSet;
    }
    public long getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
    public long getLastTime() {
        return lastTime;
    }
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}