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
package org.platform.context;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.more.global.Global;
import com.google.inject.Injector;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public abstract class ViewContext {
    private AppContext appContext = null;
    private String     requestURI = null;
    //
    //
    protected ViewContext(AppContext appContext) {
        this.appContext = appContext;
    }
    /**��ȡ{@link AppContext}����*/
    public AppContext getAppContext() {
        return this.appContext;
    }
    /**��ȡ{@link InitContext}����*/
    public InitContext getInitContext() {
        return this.getAppContext().getInitContext();
    }
    /**��ȡ{@link Global}����*/
    public Global getSettings() {
        return this.getInitContext().getConfig().getSettings();
    }
    /**��ȡ{@link Injector}Guice����*/
    public Injector getGuice() {
        return this.getAppContext().getGuice();
    };
    /**��ȡ�������Դ��ַ��*/
    public String getRequestURI() {
        if (this.requestURI == null) {
            String requestURI = this.getHttpRequest().getRequestURI();
            String contextPath = this.getHttpRequest().getContextPath();
            this.requestURI = requestURI.substring(contextPath.length());
        }
        return requestURI;
    }
    /**ȡ��{@link HttpServletRequest}���Ͷ���*/
    public abstract HttpServletRequest getHttpRequest();
    /**ȡ��{@link HttpServletResponse}���Ͷ���*/
    public abstract HttpServletResponse getHttpResponse();
    /**ȡ��{@link HttpSession}���Ͷ���*/
    public HttpSession getHttpSession(boolean create) {
        return this.getHttpRequest().getSession(create);
    }
    //
    private static ThreadLocal<ViewContext> currentViewContext = new ThreadLocal<ViewContext>();
    /**��ȡ��ǰ�߳��������{@link ViewContext}*/
    public static ViewContext currentViewContext() {
        return currentViewContext.get();
    }
    /**���õ�ǰ�߳��������{@link ViewContext}*/
    protected static void setViewContext(ViewContext viewContext) {
        if (currentViewContext.get() != null)
            currentViewContext.remove();
        currentViewContext.set(viewContext);
    }
    /**��յ�ǰ�߳��������{@link ViewContext}*/
    protected static void cleanViewContext() {
        if (currentViewContext.get() != null)
            currentViewContext.remove();
    }
    /*----------------------------------------------------------------------*/
    /**
     * ����·���㷨��
     * @param number ����
     * @param size ÿ��Ŀ¼�¿���ӵ�е���Ŀ¼���ļ���Ŀ��
     */
    public String genPath(long number, int size) {
        StringBuffer buffer = new StringBuffer();
        long b = size;
        long c = number;
        do {
            long m = number % b;
            buffer.append(m + File.separator);
            c = number / b;
            number = c;
        } while (c > 0);
        return buffer.reverse().toString();
    }
}