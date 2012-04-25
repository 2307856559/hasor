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
package org.more.web;
import java.io.IOException;
import java.util.Enumeration;
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
import org.more.util.config.Config;
/**
 * {@link AbstractServletFilter}�������ṩ{@link HttpServlet}��{@link Filter}����ʵ�ֵĹ����ࡣ
 * @version : 2011-9-17
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractServletFilter extends HttpServlet implements Filter {
    private static final long serialVersionUID = -5364737038579415096L;
    private String            contextPath      = null;
    private ServletContext    servletContext   = null;
    /*-----------------------------------------------------------------*/
    protected abstract void init(Config<ServletContext> config) throws ServletException;
    /*-----------------------------------------------------------------*/
    /** ��������ʼ���������÷�������init(InitParameter param) */
    public final void init(final FilterConfig config) throws ServletException {
        this.servletContext = config.getServletContext();
        this.contextPath = this.servletContext.getContextPath();
        this.init(new FilterSubmitConfig(config));
    };
    /** Servlet��ʼ���������÷�������init(InitParameter param) */
    public final void init() throws ServletException {
        final ServletConfig config = this.getServletConfig();
        this.servletContext = config.getServletContext();
        this.contextPath = this.servletContext.getContextPath();
        this.init(new ServletSubmitConfig(config));
    };
    /*-----------------------------------------------------------------*/
    /** ������ȹ�������*/
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    };
    /**��ȡ����·����*/
    protected String getRequestPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        requestURI = requestURI.substring(this.contextPath.length());
        return requestURI;
    };
    /**��ȡ������Դ·��(web)��*/
    public String getContextPath() {
        return this.contextPath;
    };
    /**��ȡ������Դ·�����������·����*/
    public String getAbsoluteContextPath() {
        return this.getServletContext().getRealPath(this.contextPath);
    };
    public ServletContext getServletContext() {
        return this.servletContext;
    };
};
class FilterSubmitConfig implements Config<ServletContext> {
    private FilterConfig config = null;
    public FilterSubmitConfig(FilterConfig config) {
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
class ServletSubmitConfig implements Config<ServletContext> {
    private ServletConfig config = null;
    public ServletSubmitConfig(ServletConfig config) {
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