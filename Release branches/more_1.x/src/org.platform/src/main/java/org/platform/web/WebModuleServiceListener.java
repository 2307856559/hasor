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
package org.platform.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.util.StringUtil;
import org.platform.Platform;
import org.platform.binder.ApiBinder;
import org.platform.context.AbstractModuleListener;
import org.platform.context.InitListener;
/**
 * ֧��Service��ע�⹦�ܡ�
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@InitListener(displayName = "WebModuleServiceListener", description = "org.platform.web���������֧�֡�", startIndex = 0)
public class WebModuleServiceListener extends AbstractModuleListener {
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        //1.����RootFilter
        event.filter("*").through(new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {}
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                WebHelper.initWebHelper((HttpServletRequest) request, (HttpServletResponse) response);
                chain.doFilter(request, response);
                WebHelper.clearWebHelper();
            }
            @Override
            public void destroy() {}
        });
        //2.LoadFilter.
        this.loadFilter(event);
        //2.LoadServlet.
        this.loadServlet(event);
    }
    /*װ��Filter*/
    protected void loadFilter(ApiBinder event) {
        //1.��ȡ
        Set<Class<?>> webFilterSet = event.getClassSet(WebFilter.class);
        List<Class<? extends Filter>> webFilterList = new ArrayList<Class<? extends Filter>>();
        for (Class<?> cls : webFilterSet) {
            if (Filter.class.isAssignableFrom(cls) == false) {
                Platform.warning("not implemented Filter ��" + Platform.logString(cls));
            } else {
                webFilterList.add((Class<? extends Filter>) cls);
            }
        }
        //2.����
        Collections.sort(webFilterList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                WebFilter o1Anno = o1.getAnnotation(WebFilter.class);
                WebFilter o2Anno = o2.getAnnotation(WebFilter.class);
                int o1AnnoIndex = o1Anno.sort();
                int o2AnnoIndex = o2Anno.sort();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.ע��
        for (Class<? extends Filter> filterType : webFilterList) {
            WebFilter filterAnno = filterType.getAnnotation(WebFilter.class);
            Map<String, String> initMap = this.toMap(filterAnno.initParams());
            event.filter(null, filterAnno.value()).through(filterType, initMap);
            event.filter(null, filterAnno.urlPatterns()).through(filterType, initMap);
        }
    }
    /*װ��Servlet*/
    protected void loadServlet(ApiBinder event) {
        //1.��ȡ
        Set<Class<?>> webServletSet = event.getClassSet(WebServlet.class);
        List<Class<? extends HttpServlet>> webServletList = new ArrayList<Class<? extends HttpServlet>>();
        for (Class<?> cls : webServletSet) {
            if (HttpServlet.class.isAssignableFrom(cls) == false) {
                Platform.warning("not implemented HttpServlet ��" + Platform.logString(cls));
            } else {
                webServletList.add((Class<? extends HttpServlet>) cls);
            }
        }
        //2.����
        Collections.sort(webServletList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                WebServlet o1Anno = o1.getAnnotation(WebServlet.class);
                WebServlet o2Anno = o2.getAnnotation(WebServlet.class);
                int o1AnnoIndex = o1Anno.loadOnStartup();
                int o2AnnoIndex = o2Anno.loadOnStartup();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        //3.ע��
        for (Class<? extends HttpServlet> servletType : webServletList) {
            WebServlet servletAnno = servletType.getAnnotation(WebServlet.class);
            Map<String, String> initMap = this.toMap(servletAnno.initParams());
            event.serve(null, servletAnno.value()).with(servletType, initMap);
            event.serve(null, servletAnno.urlPatterns()).with(servletType, initMap);
        }
    }
    /*ת������*/
    protected Map<String, String> toMap(WebInitParam[] initParams) {
        Map<String, String> initMap = new HashMap<String, String>();
        if (initParams != null)
            for (WebInitParam param : initParams)
                if (StringUtil.isBlank(param.name()) == false)
                    initMap.put(param.name(), param.value());
        return initMap;
    }
}