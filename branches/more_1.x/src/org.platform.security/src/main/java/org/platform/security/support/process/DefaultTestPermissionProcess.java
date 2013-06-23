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
package org.platform.security.support.process;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.platform.security.AuthSession;
import org.platform.security.SecurityContext;
import org.platform.security.TestPermissionProcess;
import org.platform.security.UriPatternMatcher;
/**
 * {@link TestPermissionProcess}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-5-8
 * @author ������ (zyc@byshell.org)
 */
public class DefaultTestPermissionProcess extends AbstractProcess implements TestPermissionProcess {
    /**����Ҫ�������Դ�Ƿ����Ȩ�޷��ʣ����Ȩ�޼��ʧ�ܻ��׳�PermissionException�쳣��*/
    @Override
    public boolean testURL(SecurityContext secContext, AuthSession[] authSessions, HttpServletRequest request, HttpServletResponse response) {
        String reqPath = request.getRequestURI().substring(request.getContextPath().length());
        UriPatternMatcher uriMatcher = secContext.getUriMatcher(reqPath);
        return uriMatcher.testPermission(authSessions);
    }
}