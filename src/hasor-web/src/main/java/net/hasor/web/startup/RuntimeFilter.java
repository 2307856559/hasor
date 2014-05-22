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
package net.hasor.web.startup;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hasor.core.AppContext;
import net.hasor.core.Hasor;
import net.hasor.web.WebAppContext;
import net.hasor.web.binder.FilterPipeline;
/**
 * ���Filter��ͬһ��Ӧ�ó���ֻ��ʵ����һ�� RuntimeFilter ����
 * @version : 2013-3-25
 * @author ������ (zyc@hasor.net)
 */
public class RuntimeFilter implements Filter {
    private WebAppContext  appContext       = null;
    private FilterPipeline filterPipeline   = null;
    private String         requestEncoding  = null;
    private String         responseEncoding = null;
    //
    //
    /**��ʼ������������ʼ����ͬʱ��ʼ��FilterPipeline*/
    public synchronized void init(FilterConfig filterConfig) throws ServletException {
        if (appContext == null) {
            ServletContext servletContext = filterConfig.getServletContext();
            appContext = (WebAppContext) servletContext.getAttribute(RuntimeListener.AppContextName);
            Hasor.assertIsNotNull(appContext, "AppContext is null.");
            this.filterPipeline = appContext.getInstance(FilterPipeline.class);
        }
        /*��ȡ������Ӧ����*/
        this.requestEncoding = appContext.getSettings().getString("hasor-web.requestEncoding");
        this.responseEncoding = appContext.getSettings().getString("hasor-web.responseEncoding");
        /*1.��ʼ��ִ�����ڹ�������*/
        this.filterPipeline.initPipeline(appContext);
        Hasor.logInfo("PlatformFilter started.");
    }
    //
    /** ���� */
    public void destroy() {
        Hasor.logInfo("executeCycle destroyCycle.");
        if (this.filterPipeline != null)
            this.filterPipeline.destroyPipeline(appContext);
    }
    //
    /** ����request����Ӧresponse */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpServletResponse httpRes = (HttpServletResponse) response;
        if (this.requestEncoding != null)
            httpReq.setCharacterEncoding(this.requestEncoding);
        if (this.requestEncoding != null)
            httpRes.setCharacterEncoding(this.responseEncoding);
        //
        Hasor.logDebug("at http(%s/%s) request : %s", this.requestEncoding, this.responseEncoding, httpReq.getRequestURI());
        //
        try {
            //ִ��.
            this.beforeRequest(appContext, httpReq, httpRes);
            this.processFilterPipeline(httpReq, httpRes, chain);
        } finally {
            this.afterResponse(appContext, httpReq, httpRes);
        }
    }
    //
    /**ִ��FilterPipeline*/
    private void processFilterPipeline(HttpServletRequest httpReq, HttpServletResponse httpRes, FilterChain chain) throws IOException, ServletException {
        this.filterPipeline.dispatch(httpReq, httpRes, chain);
    }
    //
    /**��ȡ{@link AppContext}�ӿڡ�*/
    protected final AppContext getAppContext() {
        return RuntimeListener.getLocalAppContext();
    }
    //
    /**��filter������֮ǰ���÷�������֪ͨHttpRequestProvider��HttpResponseProvider��HttpSessionProvider���¶���*/
    protected void beforeRequest(AppContext appContext, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        LocalRequest.set(httpReq);
        LocalResponse.set(httpRes);
    }
    //
    /**��filter������֮�󣬸÷�������֪ͨHttpRequestProvider��HttpResponseProvider��HttpSessionProvider���ö���*/
    protected void afterResponse(AppContext appContext, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        LocalRequest.remove();
        LocalResponse.remove();
    }
    //
    //
    private static ThreadLocal<HttpServletRequest>  LocalRequest  = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> LocalResponse = new ThreadLocal<HttpServletResponse>();
    //
    /**��ȡ{@link HttpServletRequest}*/
    public static HttpServletRequest getLocalRequest() {
        return LocalRequest.get();
    }
    //
    /**��ȡ{@link HttpServletResponse}*/
    public static HttpServletResponse getLocalResponse() {
        return LocalResponse.get();
    }
    //
    /**��ȡ{@link ServletContext}*/
    public static ServletContext getLocalServletContext() {
        return RuntimeListener.getLocalServletContext();
    }
    //
    /**��ȡ{@link AppContext}*/
    public static AppContext getLocalAppContext() {
        return RuntimeListener.getLocalAppContext();
    }
}