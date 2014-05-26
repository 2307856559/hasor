/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.plugins.secweb;

import net.hasor.security._.AuthSession;

/**
 * ��URI����Ȩ���жϽӿڡ�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class UriPatternMatcher {
    private String requestURI = null;
    //
    protected UriPatternMatcher(String requestURI) {
        this.requestURI = requestURI;
    }
    /**��ȡrequestURI*/
    public String getRequestURI() {
        return requestURI;
    }
    /**��Ȩ�޻Ự�м���Ƿ����Ȩ�ޡ�*/
    public abstract boolean testPermission(AuthSession authSession);
    /**��Ȩ�޻Ự�м���Ƿ����Ȩ�ޡ�*/
    public boolean testPermission(AuthSession[] authSessions) {
        if (authSessions == null)
            return false;
        for (AuthSession authSession : authSessions)
            if (this.testPermission(authSession) == true)
                return true;
        return false;
    }
    public String toString() {
        return this.getClass().getSimpleName() + " at requestURI= " + requestURI;
    }
}