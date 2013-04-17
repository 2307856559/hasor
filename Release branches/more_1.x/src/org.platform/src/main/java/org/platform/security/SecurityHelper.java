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
/**
 * �����ṩ�˻�ȡ�뵱ǰ�߳̽��а󶨵�{@link AuthSession}��������ͨ������initHelper��clearHelper������̬�����Թ���󶨶���
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class SecurityHelper {
    private static final ThreadLocal<AuthSession> currentSession = new ThreadLocal<AuthSession>();
    //
    /**�ж�{@link SecurityHelper}���Ƿ����ʹ�á�*/
    public static boolean canUse() {
        AuthSession authSession = getAuthSession();
        if (authSession == null)
            return false;
        else
            return true;
    }
    /**��ȡ��ǰ�߳��µ�{@link AuthSession}*/
    public static AuthSession getAuthSession() {
        return currentSession.get();
    }
    /**�÷�����runtime������������������ֱ�ӵ��ã����ø÷����ᵼ�»�����{@link HttpServletRequest}��{@link HttpServletResponse}������ҡ�*/
    protected synchronized static void initHelper(AuthSession authSession) {
        clearHelper();
        currentSession.set(authSession);
    }
    /**���WebHelper���뵱ǰ�̹߳�����{@link AuthSession}����*/
    protected synchronized static void clearHelper() {
        if (currentSession.get() != null)
            currentSession.remove();
    }
}