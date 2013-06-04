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
package org.platform.action.support;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.more.util.BeanUtils;
import org.more.util.StringConvertUtils;
import org.more.util.StringUtils;
import org.platform.Platform;
import org.platform.action.Var;
import org.platform.action.faces.ActionInvoke;
import org.platform.action.faces.RestfulActionInvoke;
import org.platform.context.AppContext;
/**
 * 
 * @version : 2013-5-11
 * @author ������ (zyc@byshell.org)
 */
abstract class InternalActionInvoke implements RestfulActionInvoke, ActionInvoke {
    private String[]   httpMethod     = null;
    private String     actionName     = null;
    private String     restfulMapping = null;
    private AppContext appContext     = null;
    private Object     target         = null;
    public InternalActionInvoke(String actionName) {
        this.actionName = actionName;
    }
    //
    public String[] getHttpMethod() {
        return this.httpMethod;
    }
    public String getActionName() {
        return actionName;
    }
    public String getRestfulMapping() {
        return restfulMapping;
    }
    protected AppContext getAppContext() {
        return appContext;
    }
    protected Object getTarget() {
        return this.target;
    }
    protected void setTarget(Object target) {
        this.target = target;
    }
    protected void setActionMethod(String[] httpMethod) {
        this.httpMethod = httpMethod;
    }
    public void setRestfulMapping(String restfulMapping) {
        this.restfulMapping = restfulMapping;
    }
    /**��ʼ��ActionInvoke*/
    public void destroyInvoke(AppContext appContext) {
        this.appContext = appContext;
    };
    /**����ActionInvoke*/
    public void initInvoke(AppContext appContext) {
        this.appContext = appContext;
    };
    @Override
    public abstract Object invoke(HttpServletRequest request, HttpServletResponse response, Map<String, Object> overwriteHttpParams) throws ServletException;
    /*-------------------------------------------------------------------------------------------------------------------*/
    /**ActionInvoke*/
    public static class InternalInvokeActionInvoke extends InternalActionInvoke {
        public InternalInvokeActionInvoke(String actionName, ActionInvoke targetInvoke) {
            super(actionName);
            this.setTarget(targetInvoke);
        }
        @Override
        public Object invoke(HttpServletRequest request, HttpServletResponse response, Map<String, Object> overwriteHttpParams) throws ServletException {
            return ((ActionInvoke) this.getTarget()).invoke(request, response, overwriteHttpParams);
        }
    }
    /**Method*/
    public static class InternalMethodActionInvoke extends InternalActionInvoke {
        private Method targetMethod = null;
        public InternalMethodActionInvoke(Method targetMethod) {
            super(targetMethod.getName());
            this.targetMethod = targetMethod;
        }
        @Override
        public Object invoke(HttpServletRequest request, HttpServletResponse response, Map<String, Object> overwriteHttpParams) throws ServletException {
            Object targetObject = this.getTarget();
            if (targetObject == null) {
                AppContext appContext = this.getAppContext();
                Class<?> targetClass = this.targetMethod.getDeclaringClass();
                String beanName = appContext.getBeanName(targetClass);
                if (StringUtils.isBlank(beanName) == false)
                    targetObject = appContext.getBean(beanName);
                else
                    targetObject = appContext.getInstance(targetClass);
            }
            //
            if (targetObject == null)
                throw new ServletException("create invokeObject on " + targetMethod.toString() + " return null.");
            //
            try {
                Class<?>[] targetParamClass = this.targetMethod.getParameterTypes();
                Annotation[][] targetParamAnno = this.targetMethod.getParameterAnnotations();
                targetParamClass = (targetParamClass == null) ? new Class<?>[0] : targetParamClass;
                targetParamAnno = (targetParamAnno == null) ? new Annotation[0][0] : targetParamAnno;
                ArrayList<Object> paramsArray = new ArrayList<Object>();
                /*׼������*/
                for (int i = 0; i < targetParamClass.length; i++) {
                    Class<?> paramClass = targetParamClass[i];
                    Annotation[] paramAnno = targetParamAnno[i];
                    paramAnno = (paramAnno == null) ? new Annotation[0] : paramAnno;
                    String paramName = null;
                    for (Annotation pAnno : paramAnno) {
                        if (pAnno instanceof Var)
                            paramName = ((Var) pAnno).value();
                    }
                    /**��ͨ����*/
                    Object paramObject = (overwriteHttpParams != null) ? overwriteHttpParams.get(paramName) : request.getParameterValues(paramName);
                    /**�������*/
                    if (paramObject == null)
                        paramObject = getSpecialParamObject(request, response, paramClass);
                    /**�����������*/
                    if (paramObject != null) {
                        try {
                            paramObject = processParamObject(paramObject, paramClass);
                        } catch (Exception e) {
                            /*�ô��벻�ᱻִ�У�StringConvertUtils���෽����������֮����Զ�ʹ��Ĭ��ֵ���*/
                            paramObject = BeanUtils.getDefaultValue(paramClass);
                            Platform.error("the action request parameter %s Convert Type error %s", paramName, e);
                        }
                    }
                    paramsArray.add(paramObject);
                }
                return this.targetMethod.invoke(targetObject, paramsArray.toArray());
            } catch (InvocationTargetException e) {
                throw new ServletException(e.getCause());
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        /*�����������Ͳ���*/
        private Object getSpecialParamObject(HttpServletRequest request, HttpServletResponse response, Class<?> paramClass) {
            if (paramClass.isEnum() || paramClass.isArray() || paramClass.isPrimitive() || paramClass == String.class)
                return null;/*���ԣ��������͡��ַ�������*/
            //
            if (paramClass.isAssignableFrom(HttpServletRequest.class) || paramClass.isAssignableFrom(ServletRequest.class))
                return request;
            if (paramClass.isAssignableFrom(HttpServletResponse.class) || paramClass.isAssignableFrom(ServletResponse.class))
                return response;
            if (paramClass.isAssignableFrom(HttpSession.class))
                return request.getSession(true);
            if (paramClass.isAssignableFrom(ServletContext.class))
                return request.getServletContext();
            try {
                return this.getAppContext().getInstance(paramClass);
            } catch (Exception e) {
                return null;
            }
        }
        /*�����������ת��*/
        private Object processParamObject(Object targetValue, Class<?> targetType) {
            Object returnData = null;
            if (targetType.isArray() == true) {
                //��������
                Class<?> targetClass = targetType.getComponentType();
                if (targetValue instanceof Object[]) {
                    Object[] arr = (Object[]) targetValue;
                    returnData = Array.newInstance(targetClass, arr.length);
                    for (int i = 0; i < arr.length; i++)
                        Array.set(returnData, i, processParamObject(arr[i], targetClass));
                } else {
                    returnData = Array.newInstance(targetClass, 1);
                    Array.set(returnData, 0, targetValue);
                }
            } else {
                //����ֵ
                if (targetValue instanceof Object[]) {
                    Object[] arrayParamObject = (Object[]) targetValue;
                    targetValue = arrayParamObject.length == 0 ? null : arrayParamObject[0];
                }
                returnData = StringConvertUtils.changeType(targetValue, targetType, BeanUtils.getDefaultValue(targetType));
            }
            return returnData;
        }
    }
}