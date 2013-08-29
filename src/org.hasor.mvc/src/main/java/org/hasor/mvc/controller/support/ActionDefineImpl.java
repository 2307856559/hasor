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
package org.hasor.mvc.controller.support;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hasor.context.AppContext;
import org.hasor.mvc.controller.ActionDefine;
import org.more.util.StringUtils;
/**
 * 
 * @version : 2013-8-12
 * @author ������ (zyc@hasor.net)
 */
class ActionDefineImpl implements ActionDefine {
    private Method     targetMethod;
    private Object     targetObject;
    private String[]   httpMethod;
    private String     mimeType;
    private String     restfulMapping;
    private String     restfulMappingMatches;
    private AppContext appContext;
    //
    public ActionDefineImpl(Method targetMethod, String[] httpMethod, String mimeType, String restfulMapping, Object targetObject) {
        this.targetMethod = targetMethod;
        this.httpMethod = httpMethod;
        this.mimeType = mimeType;
        this.restfulMapping = restfulMapping;
        this.targetObject = targetObject;
    }
    //
    public boolean matchingMethod(String httpMethod) {
        for (String m : this.getHttpMethod())
            if (StringUtils.equalsIgnoreCase(httpMethod, m))
                return true;
            else if (StringUtils.equalsIgnoreCase(m, "ANY"))
                return true;
        return false;
    }
    //
    /**��ȡAction���Խ��յķ���*/
    public String[] getHttpMethod() {
        return this.httpMethod;
    }
    //
    /**��ȡĿ�귽����*/
    public Method getTargetMethod() {
        return this.targetMethod;
    }
    //
    /**��ȡӳ���ַ���*/
    public String getRestfulMapping() {
        return this.restfulMapping;
    }
    //
    /**��ȡӳ���ַ�������ƥ��ı��ʽ�ַ���*/
    public String getRestfulMappingMatches() {
        if (this.restfulMappingMatches == null) {
            String mapping = this.getRestfulMapping();
            this.restfulMappingMatches = mapping.replaceAll("\\{\\w{1,}\\}", "([^/]{1,})");
        }
        return this.restfulMappingMatches;
    }
    //
    /**��ȡ��Ӧ����*/
    public String getMimeType() {
        return mimeType;
    }
    //
    /**��ȡAppContext*/
    public AppContext getAppContext() {
        return appContext;
    }
    //
    /**��ʼ��*/
    public void initInvoke(AppContext appContext) {
        this.appContext = appContext;
    }
    /**����һ��ActionInvoke*/
    public ActionInvokeImpl createInvoke(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException {
        Object target = this.targetObject;
        Method targetMethod = this.getTargetMethod();
        //
        if (target == null) {
            Class<?> targetClass = targetMethod.getDeclaringClass();
            String beanName = this.getAppContext().getBeanName(targetClass);
            if (StringUtils.isBlank(beanName) == false)
                target = this.getAppContext().getBean(beanName);
            else
                target = this.getAppContext().getInstance(targetClass);
        }
        //
        if (target == null)
            throw new ServletException("create invokeObject on " + targetMethod.toString() + " return null.");
        return new ActionInvokeImpl(this, target, (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
    }
    public boolean isRESTful() {
        return !StringUtils.isBlank(restfulMapping);
    }
}