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
package org.more.submit.web;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.more.submit.SubmitContext;
/**
 * submit�ĺ��Ľӿڣ��κ�action�ĵ��ö���ͨ������ӿڽ��еġ�
 * @version 2009-12-1
 * @author ������ (zyc@byshell.org)
 */
public interface WebSubmitContext extends SubmitContext {
    public static final String Default_Protocol = "action";
    public boolean isActionRequest(HttpServletRequest request);
    /**��ȡServletContext����*/
    public ServletContext getServletContext();
    /**��ȡ��ʹ��request����actionʱ����ʹ�õ�Э��ǰ׺��*/
    public String getProtocol();
    /**���õ�ʹ��request����actionʱ����ʹ�õ�Э��ǰ׺��������õ�ֵΪ�ջ�����һ�����ַ��������ý���ʹ��Default_ProtocolĬ��ֵ���档*/
    public void setProtocol(String protocol);
    /**ִ�е���action�Ĵ�����̣����ҷ���ִ�н����*/
    public Object doAction(HttpServletRequest request, HttpServletResponse response) throws Throwable;
    /**ִ�е���action�Ĵ�����̣����ҷ���ִ�н����*/
    public Object doAction(String actionInvoke, HttpServletRequest request, HttpServletResponse response) throws Throwable;
    /**ִ�е���action�Ĵ�����̣����ҷ���ִ�н����*/
    public Object doAction(String actionInvoke, HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Throwable;
    /**ִ�е���action�Ĵ�����̣����ҷ���ִ�н����*/
    public Object doAction(PageContext pageContext) throws Throwable;
    /**ִ�е���action�Ĵ�����̣����ҷ���ִ�н����*/
    public Object doAction(PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws Throwable;
    /**ִ�е���action�Ĵ�����̣����ҷ���ִ�н����*/
    public Object doAction(String actionInvoke, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws Throwable;
    /**ִ�е���action�Ĵ�����̣����ҷ���ִ�н����*/
    public Object doAction(String actionInvoke, PageContext pageContext, HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Throwable;
};