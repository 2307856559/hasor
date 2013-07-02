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
package org.moreframework.security.support.process;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.moreframework.MoreFramework;
import org.moreframework.security.AuthSession;
import org.moreframework.security.LogoutProcess;
import org.moreframework.security.SecurityContext;
import org.moreframework.security.SecurityDispatcher;
import org.moreframework.security.SecurityException;
import org.moreframework.security.SecurityForward;
/**
 * {@link LogoutProcess}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-5-8
 * @author ������ (zyc@byshell.org)
 */
public class DefaultLogoutProcess extends AbstractProcess implements LogoutProcess {
    /**����ǳ�����*/
    @Override
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
                MoreFramework.info("logout OK. userCode=%s , at SessionID= %s", userCode, authSession.getSessionID());
                return dispatcher.forwardLogout();
            } catch (SecurityException e) {
                MoreFramework.info("logout failure! userCode=%s , at SessionID= %s", userCode, authSession.getSessionID());
                return dispatcher.forwardFailure(e);
            }
        }
        return dispatcher.forwardLogout();
    }
}