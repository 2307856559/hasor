/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
 * 入口Filter，同一个应用程序只能实例化一个 RuntimeFilter 对象。
 * @version : 2013-3-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class RuntimeFilter implements Filter {
    private WebAppContext  appContext       = null;
    private FilterPipeline filterPipeline   = null;
    private String         requestEncoding  = null;
    private String         responseEncoding = null;
    //
    //
    /**初始化过滤器，初始化会同时初始化FilterPipeline*/
    public synchronized void init(FilterConfig filterConfig) throws ServletException {
        if (appContext == null) {
            ServletContext servletContext = filterConfig.getServletContext();
            appContext = (WebAppContext) servletContext.getAttribute(RuntimeListener.AppContextName);
            Hasor.assertIsNotNull(appContext, "AppContext is null.");
            this.filterPipeline = appContext.getInstance(FilterPipeline.class);
        }
        /*获取请求响应编码*/
        this.requestEncoding = appContext.getSettings().getString("hasor-web.requestEncoding");
        this.responseEncoding = appContext.getSettings().getString("hasor-web.responseEncoding");
        /*1.初始化执行周期管理器。*/
        this.filterPipeline.initPipeline(appContext);
        Hasor.logInfo("PlatformFilter started.");
    }
    //
    /** 销毁 */
    public void destroy() {
        Hasor.logInfo("executeCycle destroyCycle.");
        if (this.filterPipeline != null)
            this.filterPipeline.destroyPipeline(appContext);
    }
    //
    /** 处理request，响应response */
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
            //执行.
            this.beforeRequest(appContext, httpReq, httpRes);
            this.processFilterPipeline(httpReq, httpRes, chain);
        } finally {
            this.afterResponse(appContext, httpReq, httpRes);
        }
    }
    //
    /**执行FilterPipeline*/
    private void processFilterPipeline(HttpServletRequest httpReq, HttpServletResponse httpRes, FilterChain chain) throws IOException, ServletException {
        this.filterPipeline.dispatch(httpReq, httpRes, chain);
    }
    //
    /**获取{@link AppContext}接口。*/
    protected final AppContext getAppContext() {
        return RuntimeListener.getLocalAppContext();
    }
    //
    /**在filter请求处理之前，该方法负责通知HttpRequestProvider、HttpResponseProvider、HttpSessionProvider更新对象。*/
    protected void beforeRequest(AppContext appContext, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        LocalRequest.set(httpReq);
        LocalResponse.set(httpRes);
    }
    //
    /**在filter请求处理之后，该方法负责通知HttpRequestProvider、HttpResponseProvider、HttpSessionProvider重置对象。*/
    protected void afterResponse(AppContext appContext, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        LocalRequest.remove();
        LocalResponse.remove();
    }
    //
    //
    private static ThreadLocal<HttpServletRequest>  LocalRequest  = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> LocalResponse = new ThreadLocal<HttpServletResponse>();
    //
    /**获取{@link HttpServletRequest}*/
    public static HttpServletRequest getLocalRequest() {
        return LocalRequest.get();
    }
    //
    /**获取{@link HttpServletResponse}*/
    public static HttpServletResponse getLocalResponse() {
        return LocalResponse.get();
    }
    //
    /**获取{@link ServletContext}*/
    public static ServletContext getLocalServletContext() {
        return RuntimeListener.getLocalServletContext();
    }
    //
    /**获取{@link AppContext}*/
    public static AppContext getLocalAppContext() {
        return RuntimeListener.getLocalAppContext();
    }
}