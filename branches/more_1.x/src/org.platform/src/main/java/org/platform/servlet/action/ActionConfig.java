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
package org.platform.servlet.action;
/**
 * 
 * @version : 2013-5-28
 * @author ������ (zyc@byshell.org)
 */
public interface ActionConfig {
    /**�Ƿ�����Action����.*/
    public static final String ActionServlet_Enable       = "httpServlet.actionServlet.enable";
    /**ģʽ��mode:RestOnly��rest��񣩡�ServletOnly������servlet����Both������ͬʱʹ�ã�*/
    public static final String ActionServlet_Mode         = "httpServlet.actionServlet.mode";
    /**action������.*/
    public static final String ActionServlet_Intercept    = "httpServlet.actionServlet.intercept";
    /**�������Եķ��������ŷָ���鷽��������ע�⣺���������õĺ��Ի�Ӧ�õ�����action��.*/
    public static final String ActionServlet_IgnoreMethod = "httpServlet.actionServlet.ignoreMethod";
}