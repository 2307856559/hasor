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
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.web.binder.FilterPipeline;
import org.more.util.ContextClassLoaderLocal;
import com.google.inject.Inject;
import com.google.inject.Singleton;
/**
 * ���Filter
 * @version : 2013-3-25
 * @author ������ (zyc@hasor.net)
 */
@Singleton
public class RuntimeFilter implements Filter {
    @Inject
    private AppContext     appContext       = null;
    @Inject
    private FilterPipeline filterPipeline   = null;
    private String         requestEncoding  = null;
    private String         responseEncoding = null;
    //
    //
    /**��ʼ������������ʼ����ͬʱ��ʼ��FilterPipeline*/
    public void init(FilterConfig filterConfig) throws ServletException {
        if (this.appContext == null) {
            ServletContext servletContext = filterConfig.getServletContext();
            this.appContext = (AppContext) servletContext.getAttribute(RuntimeListener.AppContextName);
            Hasor.assertIsNotNull(this.appContext, "AppContext is null.");
            this.filterPipeline = this.appContext.getInstance(FilterPipeline.class);
        }
        //
        LocalServletContext.set(filterConfig.getServletContext());
        LocalAppContext.set(this.appContext);
        /*��ȡ������Ӧ����*/
        this.requestEncoding = this.appContext.getSettings().getString("hasor-web.encoding.requestEncoding");
        this.responseEncoding = this.appContext.getSettings().getString("hasor-web.encoding.responseEncoding");
        /*1.��ʼ��ִ�����ڹ�������*/
        this.filterPipeline.initPipeline(this.appContext);
        Hasor.info("PlatformFilter started.");
    }
    //
    /** ���� */
    public void destroy() {
        Hasor.info("executeCycle destroyCycle.");
        if (this.filterPipeline != null)
            this.filterPipeline.destroyPipeline(this.appContext);
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
        Hasor.debug("at http(%s/%s) request : %s", this.requestEncoding, this.responseEncoding, httpReq.getRequestURI());
        //
        try {
            //ִ��.
            this.beforeRequest(appContext, httpReq, httpRes);
            this.processFilterPipeline(httpReq, httpRes, chain);
        } catch (IOException e) {
            Hasor.warning("execFilterPipeline IOException %s.", e);
            throw e;
        } catch (ServletException e) {
            Hasor.warning("execFilterPipeline ServletException %s.", e.getCause());
            throw e;
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
        return this.appContext;
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
    //
    private static ContextClassLoaderLocal          LocalServletContext = new ContextClassLoaderLocal();
    private static ContextClassLoaderLocal          LocalAppContext     = new ContextClassLoaderLocal();
    private static ThreadLocal<HttpServletRequest>  LocalRequest        = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> LocalResponse       = new ThreadLocal<HttpServletResponse>();
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
        return (ServletContext) LocalServletContext.get();
    }
    //
    /**��ȡ{@link AppContext}*/
    public static AppContext getLocalAppContext() {
        return (AppContext) LocalAppContext.get();
    }
}