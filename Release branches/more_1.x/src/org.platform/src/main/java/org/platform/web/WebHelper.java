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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * �����ṩ�˻�ȡ�뵱ǰ�߳̽��а󶨵�{@link HttpServletRequest}��{@link HttpServletResponse}��������ͨ������initWebHelper��clearWebHelper������̬�����Թ���󶨶���
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class WebHelper {
    private static final ThreadLocal<HttpServletRequest>  currentRequest  = new ThreadLocal<HttpServletRequest>();
    private static final ThreadLocal<HttpServletResponse> currentResponse = new ThreadLocal<HttpServletResponse>();
    //
    /**�ж�{@link WebHelper}���Ƿ����ʹ�á�*/
    public static boolean canUse() {
        HttpServletRequest request = getHttpRequest();
        if (request == null)
            return false;
        else
            return true;
    }
    /**��ȡ�뵱ǰ�߳��������{@link HttpServletRequest}�ӿڶ���*/
    public static HttpServletRequest getHttpRequest() {
        return currentRequest.get();
    }
    /**��ȡ�뵱ǰ�߳��������{@link HttpServletResponse}�ӿڶ���*/
    public static HttpServletResponse getHttpResponse() {
        return currentResponse.get();
    }
    /**��ȡ�뵱ǰ�߳��������{@link ServletContext}�ӿڶ���*/
    public static ServletContext getServletContext() {
        if (canUse() == true)
            return getHttpRequest().getServletContext();
        return null;
    }
    /**��ȡ�뵱ǰ�߳��������{@link HttpSession}�ӿڶ���*/
    public static HttpSession getHttpSession(boolean create) {
        if (canUse() == true)
            getHttpRequest().getSession(create);
        return null;
    }
    //
    //
    /**�÷�����runtime������������������ֱ�ӵ��ã����ø÷����ᵼ�»�����{@link HttpServletRequest}��{@link HttpServletResponse}������ҡ�*/
    protected synchronized static void initWebHelper(HttpServletRequest reqHttp, HttpServletResponse resHttp) {
        clearWebHelper();
        currentRequest.set(reqHttp);
        currentResponse.set(resHttp);
    }
    /**���WebHelper���뵱ǰ�̹߳�����{@link HttpServletRequest}��{@link HttpServletResponse}����*/
    protected synchronized static void clearWebHelper() {
        if (currentRequest.get() != null)
            currentRequest.remove();
        if (currentResponse.get() != null)
            currentResponse.remove();
    }
}