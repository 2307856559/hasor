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
package org.platform.security;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Ȩ��ϵͳURL������֧�֡�
 * @version : 2013-4-9  
 * @author ������ (zyc@byshell.org)
 */
public interface SecurityDispatcher {
    /**��ת������ɹ����ҳ�档*/
    public void forwardIndex(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    /**��ת���ǳ�֮���ҳ�档*/
    public void forwardLogout(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    /**Ȩ���ж�ģʽ�µ���ת��������ת��ʽ�������ļ����塣*/
    public void forwardError(ServletRequest request, ServletResponse response);
    /**��ת�����õ�ҳ�档*/
    public void forward(String id, ServletRequest request, ServletResponse response);
}