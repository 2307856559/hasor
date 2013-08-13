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
package org.hasor.mvc.controller.support;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hasor.Hasor;
import org.hasor.context.AppContext;
import org.hasor.context.EventManager;
import org.hasor.mvc.controller.ActionDefine;
import org.hasor.mvc.controller.ActionInvoke;
import org.hasor.mvc.controller.Var;
import org.hasor.servlet.context.provider.HttpProvider;
import org.more.convert.ConverterUtils;
import org.more.util.BeanUtils;
import org.more.util.StringUtils;
/**
 * 
 * @version : 2013-6-5
 * @author ������ (zyc@byshell.org)
 */
class ActionInvokeImpl implements ActionInvoke {
    private ActionDefine    actionDefine = null;
    private AppContext      appContext   = null;
    private Object          targetObject = null;
    private ServletRequest  request      = null;
    private ServletResponse response     = null;
    //
    public ActionInvokeImpl(ActionDefine actionDefine, Object targetObject, ServletRequest request, ServletResponse response) {
        this.actionDefine = actionDefine;
        this.targetObject = targetObject;
        this.request = request;
        this.response = response;
        this.appContext = this.getActionDefine().getAppContext();
    }
    //
    /**��ȡActionDefine*/
    public ActionDefine getActionDefine() {
        return this.actionDefine;
    }
    //
    /**��ȡAppContext*/
    public AppContext getAppContext() {
        return this.appContext;
    }
    //
    /**��ȡ���õ�Ŀ�����*/
    public Object getTargetObject() {
        return targetObject;
    }
    //
    /**ִ�е���*/
    public Object invoke() throws ServletException {
        HashMap<String, Object> overwriteHttpParams = new HashMap<String, Object>();
        overwriteHttpParams.putAll(request.getParameterMap());
        return this.invoke(overwriteHttpParams);
    }
    //
    /**ִ�е���*/
    public Object invoke(Map<String, Object> overwriteHttpParams) throws ServletException {
        try {
            Method targetMethod = this.getActionDefine().getTargetMethod();
            Class<?>[] targetParamClass = targetMethod.getParameterTypes();
            Annotation[][] targetParamAnno = targetMethod.getParameterAnnotations();
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
                Object paramObject = null;
                if (StringUtils.isBlank(paramName) == false)
                    paramObject = (overwriteHttpParams != null) ? overwriteHttpParams.get(paramName) : request.getParameterValues(paramName);
                /**�������*/
                if (paramObject == null)
                    paramObject = getSpecialParamObject(paramClass);
                /**�����������*/
                if (paramObject != null) {
                    try {
                        paramObject = processParamObject(paramObject, paramClass);
                    } catch (Exception e) {
                        /*�ô��벻�ᱻִ�У�StringConvertUtils���෽����������֮����Զ�ʹ��Ĭ��ֵ���*/
                        paramObject = BeanUtils.getDefaultValue(paramClass);
                        Hasor.error("the action request parameter %s Convert Type error %s", paramName, e);
                    }
                }
                paramsArray.add(paramObject);
            }
            Object[] invokeParams = paramsArray.toArray();
            //
            String mimeType = this.getActionDefine().getMimeType();
            if (!StringUtils.isBlank(mimeType))
                response.setContentType(mimeType);
            //
            return this.call(targetMethod, this.getTargetObject(), invokeParams);
        } catch (InvocationTargetException e) {
            throw new ServletException(e.getCause());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    //
    /*ִ�е���*/
    private Object call(Method targetMethod, Object targetObject, Object[] invokeParams) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        HttpProvider httpProvider = HttpProvider.getProvider();
        HttpServletRequest oriRequest = httpProvider.getRequest();
        HttpServletResponse oriResponse = httpProvider.getResponse();
        {
            httpProvider.update((HttpServletRequest) this.request, (HttpServletResponse) this.response);
        }
        EventManager eventManager = this.getAppContext().getEventManager();
        eventManager.doSyncEvent(ActionDefineImpl.Event_BeforeInvoke, this, invokeParams);/*�����¼�*/
        Object returnData = targetMethod.invoke(this.getTargetObject(), invokeParams);
        eventManager.doSyncEvent(ActionDefineImpl.Event_AfterInvoke, this, invokeParams, returnData); /*�����¼�*/
        {
            httpProvider.update(oriRequest, oriResponse);
        }
        return returnData;
    }
    /*�����������Ͳ���*/
    private Object getSpecialParamObject(Class<?> paramClass) {
        if (paramClass.isEnum() || paramClass.isArray() || paramClass.isPrimitive() || paramClass == String.class)
            return null;/*���ԣ��������͡��ַ�������*/
        //
        try {
            return this.getActionDefine().getAppContext().getInstance(paramClass);
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
            returnData = ConverterUtils.convert(targetType, targetValue, BeanUtils.getDefaultValue(targetType));
        }
        return returnData;
    }
}