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
package org.test.more.hypha.spring;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
public class TestAuthorityInterceptor implements MethodInterceptor {
    // invoke�������ص��õĽ��   
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //        String methodName = invocation.getMethod().getName();
        //        if (user.getUsername().equals("unRegistedUser")) {
        //            System.out.println("��������δע���û�,û��Ȩ�޻ظ�,ɾ������!");
        //            return null;
        //        }
        //        if ((user.getUsername().equals("user")) && (methodName.equals("deleteTopic"))) {
        //            System.out.println("��������ע���û�,û��Ȩ��ɾ������");
        //            return null;
        //        }
        //        // proceed()���������ӵ��������������������,���������е�ÿ����������ִ�и÷���,���������ķ���ֵ   
        System.out.println("a");
        return invocation.proceed();
    }
}
