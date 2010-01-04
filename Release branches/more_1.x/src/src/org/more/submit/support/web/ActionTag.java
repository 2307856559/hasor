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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.more.core.copybean.CopyBeanUtil;
/**
 * ҳ��Ԥ�����ǩ��ע�⣺���ҳ��Ԥ������ʹ����jsp����ҳ���ڰ���ҳ��Ҳʹ����ҳ��Ԥ�����ǩ�������������⡣
 * <br/>Date : 2009-5-11
 * @author ������
 */
public class ActionTag extends BodyTagSupport {
    //========================================================================================Field
    private static final long   serialVersionUID = 5847549188323147281L;
    /** Ҫ���õ�Ŀ����ͷ�������test.testMethod */
    private String              process;
    /** ActionTag��ִ����action֮��action���ؽ����ŵ�ĳ�������е����ơ� */
    private String              result           = "result";
    /** ActionTag��ִ����action֮��action���ؽ����ŵ��ĸ�������Ĭ����request��ѡ����page,request,session,context */
    private String              scope            = "request";
    /**Action�Ļ�������*/
    private Map<String, Object> params;
    //==========================================================================================Job
    public void addParam(String key, Object value) {
        params.put(key, value);
    }
    @Override
    public int doStartTag() throws JspException {
        this.params = new HashMap<String, Object>();
        CopyBeanUtil.newInstance().copy(this.pageContext.getRequest(), params);
        return Tag.EVAL_BODY_INCLUDE;
    }
    /** ��ñ�ǩ��������� */
    public JspWriter getOut() {
        return this.pageContext.getOut();
    }
    @Override
    @SuppressWarnings("unchecked")
    public int doEndTag() throws JspException {
        Object resultObject = null;
        //��ȡ�����Լ���Ӧ����
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
        WebSubmitContext am = (WebSubmitContext) this.pageContext.getServletContext().getAttribute("org.more.web.submit.ROOT");
        //���û�������������
        try {
            //��Ӳ���
            Map map = new HashMap<String, Object>();
            for (String key : this.params.keySet())
                map.put(key, this.params.get(key));
            map.put("tag", this);
            //���� 
            resultObject = am.doAction(this.process, new SessionSynchronize(request.getSession(true)), map, request, response, this.pageContext);
        } catch (Throwable e) {
            if (e instanceof JspException == true)
                throw (JspException) e;
            else if (e.getCause() instanceof JspException)
                throw (JspException) e.getCause();
            else
                throw new JspException(e);
        }
        //ִ�н������
        if (this.result == null || this.result.equals("")) {} else {
            if (this.scope.equals("page") == true)
                this.pageContext.setAttribute(this.result, resultObject);
            else if (this.scope.equals("request") == true)
                request.setAttribute(this.result, resultObject);
            else if (this.scope.equals("session") == true)
                request.getSession().setAttribute(this.result, resultObject);
            else if (this.scope.equals("context") == true)
                request.getSession().getServletContext().setAttribute(this.result, resultObject);
            else
                throw new JspException("�޷���ִ�н���������õ�����ȷ�����У��Ϸ�������page,request,session,context");
        }
        return Tag.EVAL_PAGE;
    }
    public String getProcess() {
        return this.process;
    }
    public void setProcess(String process) {
        this.process = process;
    }
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}