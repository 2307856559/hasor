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
package org.platform.runtime.startup;
import static org.platform.api.RuntimeConfig.LaunchConfigure;
import static org.platform.api.RuntimeConfig.LaunchConfigurePath;
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
import org.platform.Assert;
import org.platform.api.binder.FilterPipeline;
import org.platform.api.context.AppContext;
import org.platform.api.context.ViewContext;
import org.platform.api.setting.ConfigServlet;
import org.platform.runtime.Platform;
import org.platform.runtime.context.AbstractAppContext;
import org.platform.runtime.context.AbstractViewContext;
import com.google.inject.Injector;
/**
 * ���Filter
 * @version : 2013-3-25
 * @author ������ (zyc@byshell.org)
 */
public class RuntimeFilter implements Filter {
    private AppContext     appContext     = null;
    private FilterPipeline filterPipeline = null;
    private ConfigServlet  configServlet  = null;
    //
    //
    /**first visit it or start Filter*/
    public void init(FilterConfig filterConfig) throws ServletException {
        Platform.info("init PlatformFilter...");
        ServletContext servletContext = filterConfig.getServletContext();
        Injector runtimeGuice = (Injector) servletContext.getAttribute(RuntimeListener.RuntimeGuice);
        Assert.isNotNull(runtimeGuice, "RuntimeBuild is null.");
        //
        /*1.����appContext����*/
        this.appContext = new AbstractAppContext(runtimeGuice) {};
        this.filterPipeline = this.appContext.getBean(FilterPipeline.class);
        this.configServlet = this.appContext.getBean(ConfigServlet.class);
        Assert.isNotNull(this.appContext, "AppContext is null.");
        //
        /*2.��ʼ��ִ�����ڹ�������*/
        Platform.info("FilterPipeline init... ");
        this.filterPipeline.initPipeline(this.appContext);
        Platform.info("FilterPipeline started.");
    }
    /** destroy */
    public void destroy() {
        Platform.info("executeCycle destroyCycle.");
        if (this.filterPipeline != null)
            this.filterPipeline.destroyPipeline(this.appContext);
    }
    /** process request and anser response */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpServletResponse httpRes = (HttpServletResponse) response;
        Platform.debug("at http request :" + httpReq.getRequestURI());
        try {
            //ִ��.
            this.beforeRequest(appContext, httpReq, httpRes);
            this.processFilterPipeline(httpReq, httpRes, chain);
        } catch (IOException e) {
            Platform.debug("execFilterPipeline IOException :" + Platform.logString(e));
            throw e;
        } catch (ServletException e) {
            Platform.debug("execFilterPipeline ServletException :" + Platform.logString(e));
            throw e;
        } finally {
            this.afterResponse(appContext, httpReq, httpRes);
        }
    }
    private boolean isStart = false; /*�Ƿ񾭹���ʼ������*/
    private void processFilterPipeline(HttpServletRequest httpReq, HttpServletResponse httpRes, FilterChain chain) throws IOException, ServletException {
        /*1.�Ƿ���ת��ϵͳ���ý��棨�״ε�½ֻ��ʾһ��.��*/
        String launchConfigurePath = this.appContext.getSettings().getString(LaunchConfigurePath, "/setting.config");
        boolean launchConfigure = this.appContext.getSettings().getBoolean(LaunchConfigure, false);
        if (this.isStart == false && launchConfigure == true) {
            String configKey = "key=" + this.configServlet.getLoginKey();
            if (launchConfigurePath.indexOf("?") == -1) {
                launchConfigurePath = launchConfigurePath + "?" + configKey;
            } else {
                launchConfigurePath = launchConfigurePath + "&" + configKey;
            }
            //
            String goURL = httpReq.getContextPath() + "/" + launchConfigurePath;
            httpRes.sendRedirect(goURL.replaceAll("[/]{2,}", "/"));
            this.isStart = true;
            return;
        }
        ViewContext viewContext = new AbstractViewContext(appContext, httpReq, httpRes) {};
        /*2.�Ƿ�Ҫ�����config*/
        if (viewContext.getRequestURI().equals(launchConfigurePath)) {
            this.configServlet.dispatch(viewContext, httpReq, httpRes);
            return;
        }
        /*3.ִ��*/
        this.filterPipeline.dispatch(viewContext, httpReq, httpRes, chain);
    }
    /**��filter������֮ǰ��*/
    protected void beforeRequest(AppContext appContext, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        //WebHelper.initWebHelper(httpReq, httpRes);/*��ʼ��WebHelper*/
    }
    /**��filter������֮��*/
    protected void afterResponse(AppContext appContext, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        //WebHelper.clearWebHelper();/*����WebHelper*/
    }
}