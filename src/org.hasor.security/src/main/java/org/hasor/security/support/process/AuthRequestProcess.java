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
package org.hasor.security.support.process;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hasor.Hasor;
import org.hasor.security.AuthSession;
import org.hasor.security.SecurityContext;
import org.hasor.security.SecurityDispatcher;
import org.hasor.security.SecurityException;
import org.hasor.security.SecurityForward;
/**
 * {@link AuthRequestProcess}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-5-8
 * @author ������ (zyc@byshell.org)
 */
public class AuthRequestProcess extends AbstractProcess {
    /**�����������*/
    public SecurityForward processLogin(SecurityContext secContext, HttpServletRequest request, HttpServletResponse response) throws SecurityException, ServletException, IOException {
        String reqPath = request.getRequestURI().substring(request.getContextPath().length());
        SecurityDispatcher dispatcher = secContext.getDispatcher(reqPath);
        //1.��õ��������Ϣ
        String account = request.getParameter(this.settings.getAccountField());
        String password = request.getParameter(this.settings.getPasswordField());
        String formAuth = request.getParameter(this.settings.getAuthField());
        //3.ִ�е���
        AuthSession authSession = secContext.getCurrentBlankAuthSession();
        if (authSession == null)
            authSession = secContext.createAuthSession();
        try {
            authSession.doLogin(formAuth, account, password);/*�����»Ự*/
            Hasor.info("login OK. acc=%s , at SessionID= %s", account, authSession.getSessionID());
            return dispatcher.forwardIndex();
        } catch (SecurityException e) {
            Hasor.warning("login failure! acc=%s , msg= %s", account, e.getMessage());
            authSession.close();
            return dispatcher.forwardFailure(e);
        }
    }
    /**����ǳ�����*/
    public SecurityForward processLogout(SecurityContext secContext, HttpServletRequest request, HttpServletResponse response) throws SecurityException, ServletException, IOException {
        String reqPath = request.getRequestURI().substring(request.getContextPath().length());
        SecurityDispatcher dispatcher = secContext.getDispatcher(reqPath);
        AuthSession[] authSessions = secContext.getCurrentAuthSession();
        for (AuthSession authSession : authSessions) {
            /*�������ѵ���ĻỰȫ���ǳ�*/
            if (authSession.isLogin() == false)
                continue;
            String userCode = authSession.getUserObject().getUserCode();
            try {
                authSession.doLogout();/*�˳��Ự*/
                Hasor.info("logout OK. userCode=%s , at SessionID= %s", userCode, authSession.getSessionID());
                return dispatcher.forwardLogout();
            } catch (SecurityException e) {
                Hasor.info("logout failure! userCode=%s , at SessionID= %s", userCode, authSession.getSessionID());
                return dispatcher.forwardFailure(e);
            }
        }
        return dispatcher.forwardLogout();
    }
}