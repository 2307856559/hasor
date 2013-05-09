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
package org.platform.binder.support;
import static org.platform.binder.support.ManagedServletPipeline.REQUEST_DISPATCHER_REQUEST;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.more.util.Iterators;
import org.platform.context.AppContext;
import org.platform.context.ViewContext;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
/**
 * 
 * @version : 2013-4-11
 * @author ������ (zyc@byshell.org)
 */
class ServletDefinition extends AbstractServletModuleBinding implements Provider<ServletDefinition> {
    private Key<? extends HttpServlet> servletKey      = null; /*HttpServlet������п��ܰ������Key��*/
    private HttpServlet                servletInstance = null;
    private UriPatternMatcher          patternMatcher  = null;
    //
    public ServletDefinition(String pattern, Key<? extends HttpServlet> servletKey, UriPatternMatcher uriPatternMatcher, Map<String, String> initParams, HttpServlet servletInstance) {
        super(initParams, pattern, uriPatternMatcher);
        this.servletKey = servletKey;
        this.servletInstance = servletInstance;
        this.patternMatcher = uriPatternMatcher;
    }
    @Override
    public ServletDefinition get() {
        return this;
    }
    protected HttpServlet getTarget(Injector injector) {
        if (this.servletInstance == null)
            this.servletInstance = injector.getInstance(this.servletKey);
        return this.servletInstance;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(ServletDefinition.class)//
                .append("pattern", getPattern())//
                .append("initParams", getInitParams())//
                .append("uriPatternType", getUriPatternType())//
                .toString();
    }
    /*--------------------------------------------------------------------------------------------------------*/
    /**/
    public void init(final AppContext appContext) throws ServletException {
        HttpServlet servlet = this.getTarget(appContext.getGuice());
        if (servlet == null)
            return;
        final Map<String, String> initParams = this.getInitParams();
        //
        servlet.init(new ServletConfig() {
            public String getServletName() {
                return servletKey.toString();
            }
            public ServletContext getServletContext() {
                return appContext.getInitContext().getServletContext();
            }
            public String getInitParameter(String s) {
                return initParams.get(s);
            }
            public Enumeration<String> getInitParameterNames() {
                return Iterators.asEnumeration(initParams.keySet().iterator());
            }
        });
    }
    /**/
    public boolean service(ViewContext viewContext, ServletRequest request, ServletResponse response) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        boolean serve = this.matchesUri(path);
        // 
        if (serve)
            doService(viewContext, request, response);
        return serve;
    }
    /**/
    private void doService(ViewContext viewContext, final ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
            private String  path;
            private boolean pathComputed     = false;
            //must use a boolean on the memo field, because null is a legal value (TODO no, it's not)
            private boolean pathInfoComputed = false;
            private String  pathInfo;
            @Override
            public String getPathInfo() {
                if (!isPathInfoComputed()) {
                    int servletPathLength = getServletPath().length();
                    pathInfo = getRequestURI().substring(getContextPath().length()).replaceAll("[/]{2,}", "/");
                    pathInfo = pathInfo.length() > servletPathLength ? pathInfo.substring(servletPathLength) : null;
                    // Corner case: when servlet path and request path match exactly (without trailing '/'),
                    // then pathinfo is null
                    if ("".equals(pathInfo) && servletPathLength != 0) {
                        pathInfo = null;
                    }
                    pathInfoComputed = true;
                }
                return pathInfo;
            }
            // NOTE(dhanji): These two are a bit of a hack to help ensure that request dipatcher-sent
            // requests don't use the same path info that was memoized for the original request.
            private boolean isPathInfoComputed() {
                return pathInfoComputed && !(null != servletRequest.getAttribute(REQUEST_DISPATCHER_REQUEST));
            }
            private boolean isPathComputed() {
                return pathComputed && !(null != servletRequest.getAttribute(REQUEST_DISPATCHER_REQUEST));
            }
            @Override
            public String getServletPath() {
                return computePath();
            }
            @Override
            public String getPathTranslated() {
                final String info = getPathInfo();
                return (null == info) ? null : getRealPath(info);
            }
            // Memoizer pattern.
            private String computePath() {
                if (!isPathComputed()) {
                    String servletPath = super.getServletPath();
                    path = patternMatcher.extractPath(servletPath);
                    pathComputed = true;
                    if (null == path) {
                        path = servletPath;
                    }
                }
                return path;
            }
        };
        //
        HttpServlet servlet = this.getTarget(viewContext.getGuice());
        if (servlet == null)
            return;
        servlet.service(request, servletResponse);
    }
    /**/
    public void destroy(AppContext appContext) {
        HttpServlet servlet = this.getTarget(appContext.getGuice());
        if (servlet == null)
            return;
        servlet.destroy();
    }
}