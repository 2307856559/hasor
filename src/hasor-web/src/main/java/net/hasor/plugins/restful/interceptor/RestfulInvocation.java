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
package net.hasor.plugins.restful.interceptor;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hasor.core.AppContext;
import net.hasor.plugins.restful.support.RestfulInvoke;
import org.aopalliance.intercept.MethodInvocation;
/***
 * 
 * @version : 2013-9-26
 * @author ������(zyc@hasor.net)
 */
public final class RestfulInvocation {
    private RestfulInvoke    restfulService;
    private MethodInvocation invocation;
    //
    public RestfulInvocation(RestfulInvoke restfulService, MethodInvocation invocation) {
        this.restfulService = restfulService;
        this.invocation = invocation;
    }
    /**��ȡ{@link HttpServletRequest}����*/
    public HttpServletRequest getRequest() {
        return this.restfulService.getRequest();
    };
    /**��ȡ{@link HttpServletResponse}����*/
    public HttpServletResponse getResponse() {
        return this.restfulService.getResponse();
    };
    /**��ȡ{@link AppContext}����*/
    public AppContext getAppContext() {
        return this.restfulService.getDefine().getAppContext();
    };
    /**��ȡActionInvoke*/
    public RestfulInvoke getService() {
        return restfulService;
    };
    /**��ȡActionInvoke*/
    public Method getControllerMethod() {
        return this.invocation.getMethod();
    };
    /**����Ŀ��*/
    public Object proceed() throws Throwable {
        return invocation.proceed();
    };
    /**ʹ��ָ���� req res ����*/
    public Object proceed(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return this.proceed();
    };
}