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
package net.test.project.common.aop;
import javax.servlet.http.HttpServletRequest;
import net.hasor.core.Hasor;
import net.hasor.plugins.aop.GlobalAop;
import net.hasor.plugins.controller.interceptor.ControllerInterceptor;
import net.hasor.plugins.controller.interceptor.ControllerInvocation;
/**
 * ȫ��Aop�������������� Controller ���ã������  Action ������־��¼ 
 * @version : 2013-12-23
 * @author ������(zyc@hasor.net)
 */
@GlobalAop("*net.test.project.*")
public class TestActionInterceptor extends ControllerInterceptor {
    /* 
     * 1.@GlobalAop ע��������������Ϊȫ����������������������������з���
     * 2.ControllerInterceptor ���͵���������ֻ������ Controller �� Action ������
     */
    public Object invoke(ControllerInvocation invocation) throws Throwable {
        try {
            HttpServletRequest reqest = invocation.getRequest();
            Hasor.logInfo("���� Action :%s.", reqest.getRequestURI());
            return invocation.proceed();
        } catch (Exception e) {
            throw e;
        }
    }
}