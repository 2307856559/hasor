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
package net.hasor.web.controller;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hasor.core.AppContext;
import org.more.UnhandledException;
import org.more.convert.ConverterUtils;
import org.more.util.BeanUtils;
import org.more.util.exception.ExceptionUtils;
/**
 * �̰߳�ȫ
 * @version : 2013-6-5
 * @author ������ (zyc@hasor.net)
 */
public class ControllerInvoke {
    private Class<?>                        targetClass;
    private Method                          targetMethod;
    private AppContext                      appContext;
    private ThreadLocal<AbstractController> localObject;
    //
    public ControllerInvoke(Method targetMethod, AppContext appContext) {
        this.targetMethod = targetMethod;
        this.targetMethod.setAccessible(false);
        this.targetClass = targetMethod.getDeclaringClass();
        this.appContext = appContext;
        this.localObject = new ThreadLocal<AbstractController>();//ʹ��ThreadLocal ȷ��ÿ���߳���ִ�� Action �������ڼ䲻�ᴴ����� Controller
    }
    public AbstractController getTargetObject() {
        AbstractController targetObject = this.localObject.get();
        if (targetObject != null)
            return targetObject;
        targetObject = (AbstractController) this.appContext.getInstance(this.targetClass);
        this.localObject.set(targetObject);
        return targetObject;
    }
    public Object invoke(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        AbstractController targetObject = this.getTargetObject();
        try {
            Object[] paramArrays = this.getParams(servletRequest, servletResponse);
            targetObject.initController(servletRequest, servletResponse);
            return this.targetMethod.invoke(targetObject, paramArrays);
        } catch (Throwable e) {
            //�����쳣
            Throwable target = ExceptionUtils.getCause(e);
            target = (target == null) ? e : target;
            throw new UnhandledException(target);
        } finally {
            targetObject.resetController();
            this.localObject.remove();
        }
    }
    /***/
    private Object[] getParams(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Class<?>[] targetParamClass = this.targetMethod.getParameterTypes();
        Annotation[][] targetParamAnno = this.targetMethod.getParameterAnnotations();
        targetParamClass = (targetParamClass == null) ? new Class<?>[0] : targetParamClass;
        targetParamAnno = (targetParamAnno == null) ? new Annotation[0][0] : targetParamAnno;
        ArrayList<Object> paramsArray = new ArrayList<Object>();
        /*׼������*/
        for (int i = 0; i < targetParamClass.length; i++) {
            Class<?> paramClass = targetParamClass[i];
            Object paramObject = this.getIvnokeParams(paramClass, targetParamAnno[i]);//��ȡ����
            /*��ȡ���Ĳ�����Ҫ��һ������ת�����Է�ֹmethod.invokeʱ�����쳣��*/
            if (paramObject == null)
                paramObject = BeanUtils.getDefaultValue(paramClass);
            else
                paramObject = ConverterUtils.convert(paramClass, paramObject);
            paramsArray.add(paramObject);
        }
        Object[] invokeParams = paramsArray.toArray();
        return invokeParams;
    }
    /**��ò�����*/
    private Object getIvnokeParams(Class<?> paramClass, Annotation[] paramAnno) {
        //        for (Annotation pAnno : paramAnno) {
        //            if (pAnno instanceof AttributeParam)
        //                return this.getAttributeParam(paramClass, (AttributeParam) pAnno);
        //            else if (pAnno instanceof CookieParam)
        //                return this.getCookieParam(paramClass, (CookieParam) pAnno);
        //            else if (pAnno instanceof HeaderParam)
        //                return this.getHeaderParam(paramClass, (HeaderParam) pAnno);
        //            else if (pAnno instanceof QueryParam)
        //                return this.getQueryParam(paramClass, (QueryParam) pAnno);
        //            else if (pAnno instanceof PathParam)
        //                return this.getPathParam(paramClass, (PathParam) pAnno);
        //            else if (pAnno instanceof InjectParam)
        //                return this.getInjectParam(paramClass, (InjectParam) pAnno);
        //        }
        return BeanUtils.getDefaultValue(paramClass);
    }
}