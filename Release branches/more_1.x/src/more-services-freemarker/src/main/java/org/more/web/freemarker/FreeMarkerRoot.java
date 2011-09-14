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
package org.more.web.freemarker;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.more.hypha.ApplicationContext;
import org.more.services.freemarker.FreemarkerService;
import org.more.util.config.Config;
import org.more.web.hypha.ContextLoaderListener;
/**
 * freemarker �齨��Web���ֵ�֧�֣������Ѿ�ʵ����Filter�ӿڲ��Ҽ̳���HttpServlet�ࡣ
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org)
 */
public class FreeMarkerRoot extends HttpServlet implements Filter {
    /*-----------------------------------------------------------------*/
    private static final long serialVersionUID  = -9157250446565992949L;
    private FreemarkerService freemarkerService = null;
    private ServletContext    servletContext    = null;
    private String            contextPath       = null;
    /*-----------------------------------------------------------------*/
    protected void init(Config<ServletContext> config) throws ServletException {
        try {
            this.servletContext = config.getContext();
            this.contextPath = this.servletContext.getContextPath();
            ApplicationContext context = (ApplicationContext) this.servletContext.getAttribute(ContextLoaderListener.ContextName);
            this.freemarkerService = context.getService(FreemarkerService.class);
            {
                //���ò���
                Enumeration<String> enums = config.getInitParameterNames();
                while (enums.hasMoreElements()) {
                    String name = enums.nextElement();
                    this.freemarkerService.setAttribute(name, config.getInitParameter(name));
                }
            }
            this.servletContext.setAttribute("org.more.freemarker.ROOT", this.freemarkerService);
        } catch (Throwable e) {
            e.printStackTrace();
            if (e instanceof ServletException)
                throw (ServletException) e;
            else
                throw new ServletException(e.getLocalizedMessage(), e);
        }
    };
    /*-----------------------------------------------------------------*/
    /** ��������ʼ���������÷�������init(InitParameter param) */
    public final void init(final FilterConfig config) throws ServletException {
        this.init(new FilterFreemarkerConfig(config));
    };
    /** Servlet��ʼ���������÷�������init(InitParameter param) */
    public final void init() throws ServletException {
        final ServletConfig config = this.getServletConfig();
        this.init(new ServletFreemarkerConfig(config));
    };
    /*-----------------------------------------------------------------*/
    /**��ȡһ��RootMap�����Map���ڴ��һЩ���󣬵�ģ�屻ִ��ʱ��Map�е������ڽ���ģ��ʱ���Ա�ģ����ʵ���*/
    protected Map<String, Object> getRootMap(HttpServletRequest req, HttpServletResponse res) {
        Map<String, String[]> paramMap = req.getParameterMap();
        HashMap<String, Object> reqParams = new HashMap<String, Object>();
        for (String key : paramMap.keySet()) {
            String[] param = paramMap.get(key);
            if (param == null || param.length == 0)
                reqParams.put(key, null);
            else if (param.length == 1)
                reqParams.put(key, param[0]);
            else
                reqParams.put(key, param);
        }
        //
        HashMap<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("req", req);
        rootMap.put("request", req);
        rootMap.put("res", res);
        rootMap.put("response", res);
        rootMap.put("reqMap", reqParams);
        rootMap.put("session", req.getSession(true));
        rootMap.put("context", req.getServletContext());
        //
        rootMap.put("service", this.freemarkerService);
        rootMap.put("more", this.freemarkerService.getContext());
        return rootMap;
    }
    /** ������ȹ����� */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //
        String templateName = this.getRequestPath(req);
        if (this.freemarkerService.containsTemplate(templateName) == true) {
            Map<String, Object> params = this.getRootMap(req, res);
            this.freemarkerService.getProcess().process(templateName, params, response.getWriter());
        } else
            chain.doFilter(req, res);
    };
    /** �������Servlet */
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //
        String templateName = this.getRequestPath(req);
        Map<String, Object> params = this.getRootMap(req, res);
        if (this.freemarkerService.containsTemplate(templateName) == true)
            this.freemarkerService.getProcess().process(templateName, params, response.getWriter());
        else
            this.freemarkerService.getProcess().process("404", params, response.getWriter());
    };
    /**��ȡ{@link FreemarkerService}����*/
    protected FreemarkerService getFreemarkerService() {
        return this.freemarkerService;
    };
    /**��ȡ����·��*/
    protected String getRequestPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        requestURI = requestURI.substring(this.contextPath.length());
        return requestURI;
    };
};
class FilterFreemarkerConfig implements Config<ServletContext> {
    private FilterConfig config = null;
    public FilterFreemarkerConfig(FilterConfig config) {
        this.config = config;
    };
    public ServletContext getContext() {
        return this.config.getServletContext();
    };
    public String getInitParameter(String name) {
        return this.config.getInitParameter(name);
    };
    public Enumeration<String> getInitParameterNames() {
        return this.config.getInitParameterNames();
    };
}
class ServletFreemarkerConfig implements Config<ServletContext> {
    private ServletConfig config = null;
    public ServletFreemarkerConfig(ServletConfig config) {
        this.config = config;
    };
    public ServletContext getContext() {
        return this.config.getServletContext();
    };
    public String getInitParameter(String name) {
        return this.config.getInitParameter(name);
    };
    public Enumeration<String> getInitParameterNames() {
        return this.config.getInitParameterNames();
    };
};