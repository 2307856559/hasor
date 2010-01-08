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
package org.more.submit.support.web;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javax.servlet.http.HttpSession;
import org.more.core.copybean.CopyBeanUtil;
import org.more.submit.CasingBuild;
import org.more.submit.Config;
/**
 * submit3.0�齨��Web���ֵ�֧�֣������Ѿ�ʵ����Filter�ӿڲ��Ҽ̳���HttpServlet�ࡣ
 * ��web֧�ֵ�����ֻ��һ������buildClass����ʾ�������ľ������͡�action������ʾ�����Э����
 * ����action����Բ�������Ĭ����action��<br/>
 * SubmitRoot�ᷴ�����ʽ�������������������ݽ���ʽaction://test.tesy?aaa=aaa
 * @version 2009-6-29
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class SubmitRoot extends HttpServlet implements Filter {
    //========================================================================================Field
    private static final long serialVersionUID = -9157250446565992949L;
    private WebSubmitContext  submitContext;                           //action��������
    private String            actionName       = "action";             //servlet��ű���ԵĲ�������filter���ڽ���action��Э��ǰ׺
    //==========================================================================================Job
    /** ִ�е��� */
    private Object doAction(String exp, ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            //��ȡSession������SessionSynchronize������HttpSession��Session֮����š�
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession(true);
            //ִ�е���
            Object obj = this.submitContext.doAction(exp, new SessionSynchronize(session), this.getParams(request), httpRequest, httpResponse, null);
            return obj;
        } catch (Throwable e) {
            if (e instanceof ServletException)
                throw (ServletException) e;
            else if (e instanceof IOException)
                throw (IOException) e;
            else
                throw new ServletException(e);
        }
    }
    private Map getParams(ServletRequest request) {
        Map params = new HashMap();
        CopyBeanUtil.newInstance().copy(request, params);
        return params;
    }
    private void init(Config config) throws ServletException {
        try {
            // 1.�������Э����
            Object tem_actionName = config.getInitParameter("action");
            if (tem_actionName != null && tem_actionName.equals("") == false)
                this.actionName = tem_actionName.toString();
            // 2.��ʼ��WebSubmitContext
            ServletContext sc = (ServletContext) config.getContext();
            this.submitContext = (WebSubmitContext) sc.getAttribute("org.more.web.submit.ROOT");
            if (this.submitContext == null) {
                WebCasingDirector director = new WebCasingDirector(config, sc);//����Web������
                Object buildClassString = config.getInitParameter("buildClass");
                if (buildClassString == null)
                    buildClassString = "org.more.submit.casing.more.WebMoreBuilder";
                CasingBuild build = (CasingBuild) Class.forName(buildClassString.toString()).newInstance();
                director.build(build);//ͨ��CasingDirector����manager
                this.submitContext = (WebSubmitContext) director.getResult();
                sc.setAttribute("org.more.web.submit.ROOT", this.submitContext);
                sc.setAttribute("org.more.web.submit.ROOT.Action", this.actionName);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    /*-----------------------------------------------------------------*/
    /** ��������ʼ���������÷�������init(InitParameter param) */
    @Override
    public void init(final FilterConfig config) throws ServletException {
        this.init(new FilterSubmitConfig(config));
    }
    /** Servlet��ʼ���������÷�������init(InitParameter param) */
    @Override
    public void init() throws ServletException {
        final ServletConfig config = this.getServletConfig();
        this.init(new ServletSubmitConfig(config));
    }
    /*-----------------------------------------------------------------*/
    /** ������ȹ����� */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String headInfo = ((HttpServletRequest) request).getRequestURL().toString();
        Pattern p = Pattern.compile("/" + this.actionName + ":(//){0,1}(.*)(\\?.*){0,}");// /post:(//){0,1}(.*)(\?.*){0,}
        Matcher m = p.matcher(headInfo);
        //
        if (m.find()) {
            String exp = m.group(2);//��ȡ��������еĵ��ñ����
            this.doAction(exp, request, response); //ִ�е���
        } else
            //����Ҳ����������Ի��߸�������post://Э����������������
            chain.doFilter(request, response);
    }
    /** �������Servlet */
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        String exp = request.getParameter(this.actionName);//��ȡ��������еĵ��ñ����
        this.doAction(exp, request, response); //ִ�е���
    }
}