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
package org.more.submit.support;
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

import org.more.core.copybean.CopyBeanUtil;
import org.more.submit.ActionManager;
import org.more.submit.CasingBuild;
import org.more.submit.CasingDirector;
import org.more.submit.Config;
/**
 * submit2.0�齨��Web���ֵ�֧�֣������Ѿ�ʵ����Filter�ӿڲ��Ҽ̳���HttpServlet�ࡣ
 * ��web֧�ֵ�����ֻ��һ������buildClass����ʾ�������ľ������͡�action������ʾ�����Э����
 * ����action����Բ�������Ĭ����action��<br/>
 * TopFilter�ᷴ�����ʽ�������������������ݽ���ʽaction://test.tesy?aaa=aaa
 * Date : 2009-6-29
 * @author ������
 */
@SuppressWarnings("unchecked")
public class TopFilter extends HttpServlet implements Filter {
    /**  */
    private static final long serialVersionUID = -9157250446565992949L;
    private ActionManager     actionManager    = null;                 //action��������
    private String            actionName       = "action";             //servlet��ű���ԵĲ�������filter���ڽ���action��Э��ǰ׺
    //==================================================================
    /** ִ�е��� */
    private Object doAction(String exp, ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            //����̻߳�������
            this.actionManager.addThreadContextParams("request", request);
            this.actionManager.addThreadContextParams("response", response);
            //ִ�е���
            Object obj = this.actionManager.doAction(exp, this.getParams(request));
            this.actionManager.clearThreadContextParams();//����̻߳�������
            return obj;
        } catch (Exception e) {
            this.actionManager.clearThreadContextParams();//����̻߳�������
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
            String tem_actionName = config.getInitParameter("action");
            if (tem_actionName != null && tem_actionName.equals("") == false)
                this.actionName = tem_actionName;
            // 2.��ʼ��ActionManager
            ServletContext sc = (ServletContext) config.getContext();
            ActionManager am = (ActionManager) sc.getAttribute("org.more.web.submit.ROOT");
            if (am == null) {
                CasingDirector director = new CasingDirector(config);//����������
                CasingBuild build = (CasingBuild) Class.forName(config.getInitParameter("buildClass")).newInstance();
                director.buildManager(build);//ͨ��CasingDirector����manager
                this.actionManager = director.getResult();
                sc.setAttribute("org.more.web.submit.ROOT", this.actionManager);
                sc.setAttribute("org.more.web.submit.ROOT.Action", this.actionName);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    // ============================================================================
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
    // ============================================================================
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
