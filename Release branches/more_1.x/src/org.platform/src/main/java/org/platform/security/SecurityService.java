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
import static org.platform.PlatformConfigEnum.Security_LoginFormData_AccountField;
import static org.platform.PlatformConfigEnum.Security_LoginFormData_PasswordField;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.platform.Platform;
/**
 * �����ṩ�˻�ȡ�뵱ǰ�߳̽��а󶨵�{@link AuthSession}��������ͨ������initHelper��clearHelper������̬�����Թ���󶨶���
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityService {
    /**��ȡ�򴴽�һ��{@link AuthSession}�ӿڡ�*/
    public abstract AuthSession getAuthSession(HttpServletRequest request, HttpServletResponse response, boolean created);
    /*
     * 
     * 
     *                 private String          accountField  = null;
                private String          passwordField = null;
                
                accountField = newConfig.getString(Security_LoginFormData_AccountField);
                passwordField = newConfig.getString(Security_LoginFormData_PasswordField);
            String account = httpRequest.getParameter(accountField);
            String password = httpRequest.getParameter(passwordField);

     * */s
    /**��ȡ�򴴽�һ��{@link AuthSession}�ӿڡ�*/
    public abstract AuthSession getAuthSession(String authSessionID, boolean created);
    /**��ȡ�򴴽�һ��{@link AuthSession}�ӿڡ�*/
    public abstract AuthSession getAuthSession(HttpSession session, boolean created);
    /***/
    public abstract AuthSession getCurrentAuthSession();
    /***/
    public abstract PowerTest createPowerTest();
    /***/
    public abstract URLPermission getURLPermission(String reqPath);
    public abstract SecurityDispatcher getDispatcher(String reqPath);
    //
    //  
    public interface PowerTest {
        /*-*/
        public PowerTest and(Power powerAnno);
        public PowerTest or(Power permission);
        public PowerTest not(Power powerCode);
        /*-*/
        public PowerTest and(Permission powerAnno);
        public PowerTest or(Permission permission);
        public PowerTest not(Permission powerCode);
        /*-*/
        public PowerTest and(String powerAnno);
        public PowerTest or(String permission);
        public PowerTest not(String powerCode);
        /*-----------------------------------------*/
        public void width(Runnable run);
        public boolean test();
    }
}