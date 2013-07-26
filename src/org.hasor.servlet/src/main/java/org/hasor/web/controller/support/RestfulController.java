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
package org.hasor.web.controller.support;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.hasor.context.AppContext;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
/**
 * action���ܵ���ڡ�
 * @version : 2013-5-11
 * @author ������ (zyc@byshell.org)
 */
//@WebFilter(value = "*", sort = Integer.MIN_VALUE + 2)
class RestfulController implements Filter {
    @Inject
    private AppContext      appContext    = null;
    private ActionManager   actionManager = null;
    private ActionInvoke2[] invokeArray   = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.actionManager = appContext.getInstance(ActionManager.class);
        //
        ArrayList<ActionInvoke2> restfulList = new ArrayList<ActionInvoke2>();
        TypeLiteral<ActionInvoke2> REST_DEFS = TypeLiteral.get(ActionInvoke2.class);
        for (Binding<ActionInvoke2> entry : appContext.getGuice().findBindingsByType(REST_DEFS))
            restfulList.add(entry.getProvider().get());
        Collections.sort(restfulList, new Comparator<ActionInvoke2>() {
            @Override
            public int compare(ActionInvoke2 o1, ActionInvoke2 o2) {
                return o1.getRestfulMapping().compareToIgnoreCase(o2.getRestfulMapping());
            }
        });
        this.invokeArray = restfulList.toArray(new ActionInvoke2[restfulList.size()]);
    }
    @Override
    public void destroy() {}
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String actionPath = request.getRequestURI().substring(request.getContextPath().length());
        //1.��ȡ ActionInvoke
        ActionInvoke2 invoke = this.getActionInvoke(actionPath);
        if (invoke == null) {
            chain.doFilter(request, resp);
            return;
        }
        //3.ִ�е���
        try {
            Map<String, Object> overwriteHttpParams = this.getParams(request, actionPath, invoke);
            Object result = invoke.invoke(request, resp, overwriteHttpParams);
            this.actionManager.processResult(invoke.getMethod(), result, request, resp);
        } catch (ServletException e) {
            if (e.getCause() instanceof IOException)
                throw (IOException) e.getCause();
            else
                throw e;
        }
    }
    private Map<String, Object> getParams(ServletRequest request, String actionPath, ActionInvoke2 invoke) {
        String matchVar = invoke.getRestfulMappingMatches();
        String matchKey = "(?:\\{(\\w+)\\}){1,}";//  (?:\{(\w+)\}){1,}
        Matcher keyM = Pattern.compile(matchKey).matcher(invoke.getRestfulMapping());
        Matcher varM = Pattern.compile(matchVar).matcher(actionPath);
        ArrayList<String> keyArray = new ArrayList<String>();
        ArrayList<String> varArray = new ArrayList<String>();
        while (keyM.find())
            keyArray.add(keyM.group(1));
        varM.find();
        for (int i = 1; i <= varM.groupCount(); i++)
            varArray.add(varM.group(i));
        //
        Map<String, List<String>> uriParams = new HashMap<String, List<String>>();
        for (int i = 0; i < keyArray.size(); i++) {
            String k = keyArray.get(i);
            String v = varArray.get(i);
            List<String> pArray = uriParams.get(k);
            pArray = pArray == null ? new ArrayList<String>() : pArray;
            if (pArray.contains(v) == false)
                pArray.add(v);
            uriParams.put(k, pArray);
        }
        HashMap<String, Object> overwriteHttpParams = new HashMap<String, Object>();
        overwriteHttpParams.putAll(request.getParameterMap());
        for (Entry<String, List<String>> ent : uriParams.entrySet()) {
            String k = ent.getKey();
            List<String> v = ent.getValue();
            overwriteHttpParams.put(k, v.toArray(new String[v.size()]));
        }
        return overwriteHttpParams;
    }
    private ActionInvoke2 getActionInvoke(String requestPath) {
        for (ActionInvoke2 restAction : this.invokeArray)
            if (requestPath.matches(restAction.getRestfulMappingMatches()) == true)
                return restAction;
        return null;
    }
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    /** Ϊת���ṩ֧�� */
    public RequestDispatcher getRequestDispatcher(final String newRequestUri, final HttpServletRequest request) {
        // TODO ��Ҫ�����������Ƿ����Servlet�淶����request���������Ҳ��Ҫ��飩
        //1.��������ַ���
        final ActionInvoke2 invoke = getActionInvoke(newRequestUri);
        if (invoke == null)
            return null;
        else
            return new RequestDispatcher() {
                @Override
                public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                    servletRequest.setAttribute(REQUEST_DISPATCHER_REQUEST, Boolean.TRUE);
                    /*ִ��servlet*/
                    try {
                        Map<String, Object> overwriteHttpParams = getParams(request, newRequestUri, invoke);
                        invoke.invoke(servletRequest, servletResponse, overwriteHttpParams);
                    } finally {
                        servletRequest.removeAttribute(REQUEST_DISPATCHER_REQUEST);
                    }
                }
                @Override
                public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                    if (servletResponse.isCommitted() == true)
                        throw new ServletException("Response has been committed--you can only call forward before committing the response (hint: don't flush buffers)");
                    /*��ջ���*/
                    servletResponse.resetBuffer();
                    ServletRequest requestToProcess;
                    if (servletRequest instanceof HttpServletRequest) {
                        requestToProcess = new RequestDispatcherRequestWrapper(servletRequest, newRequestUri);
                    } else {
                        //�������֮�²���ִ����δ��롣
                        requestToProcess = servletRequest;
                    }
                    /*ִ��ת��*/
                    servletRequest.setAttribute(REQUEST_DISPATCHER_REQUEST, Boolean.TRUE);
                    try {
                        Map<String, Object> overwriteHttpParams = getParams(request, newRequestUri, invoke);
                        invoke.invoke(requestToProcess, servletResponse, overwriteHttpParams);
                    } finally {
                        servletRequest.removeAttribute(REQUEST_DISPATCHER_REQUEST);
                    }
                }
            };
    }
    /** ʹ��RequestDispatcherRequestWrapper�ദ��request.getRequestURI�����ķ���ֵ*/
    public static final String REQUEST_DISPATCHER_REQUEST = "javax.servlet.forward.servlet_path";
    private static class RequestDispatcherRequestWrapper extends HttpServletRequestWrapper {
        private final String newRequestUri;
        public RequestDispatcherRequestWrapper(ServletRequest servletRequest, String newRequestUri) {
            super((HttpServletRequest) servletRequest);
            this.newRequestUri = newRequestUri;
        }
        @Override
        public String getRequestURI() {
            return newRequestUri;
        }
    }
}