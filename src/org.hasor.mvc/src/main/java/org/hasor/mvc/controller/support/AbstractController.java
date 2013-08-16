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
package org.hasor.mvc.controller.support;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @version : 2013-8-14
 * @author ������ (zyc@byshell.org)
 */
public class AbstractController {
    private HttpServletRequest  request  = null;
    private HttpServletResponse response = null;
    //
    public void initController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    /**��ȡ{@link HttpServletRequest}*/
    protected HttpServletRequest getRequest() {
        return request;
    }
    /**��ȡ{@link HttpServletResponse}*/
    protected HttpServletResponse getResponse() {
        return response;
    }
    /**����{@link HttpServletRequest}����*/
    protected void putAtt(String attKey, Object attValue) {
        this.getRequest().setAttribute(attKey, attValue);
    }
    /**����{@link HttpServletResponse}Header����*/
    protected void setHeader(String key, String value) {
        this.getResponse().setHeader(key, value);
    }
    /**����{@link HttpServletResponse}Header����*/
    protected void addHeader(String key, String value) {
        this.getResponse().addHeader(key, value);
    }
}