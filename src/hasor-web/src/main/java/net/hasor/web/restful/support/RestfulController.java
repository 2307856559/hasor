/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.web.restful.support;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import net.hasor.core.AppContext;
import net.hasor.web.restful.Path;
import net.hasor.web.restful.RestfulService;
import org.more.util.BeanUtils;
import org.more.util.exception.ExceptionUtils;
import com.google.inject.Inject;
/**
 * action���ܵ���ڡ�
 * @version : 2013-5-11
 * @author ������ (zyc@hasor.net)
 */
@Singleton
class RestfulController implements Filter {
    @Inject
    private AppContext      appContext  = null;
    private RestfulInvoke[] invokeArray = null;
    //
    public void init(FilterConfig filterConfig) throws ServletException {
        Set<Class<?>> controllerSet = this.appContext.getClassSet(RestfulService.class);
        if (controllerSet == null)
            return;
        //1.ע�����
        ArrayList<RestfulInvoke> restfulList = new ArrayList<RestfulInvoke>();
        for (Class<?> controllerType : controllerSet) {
            List<Method> actionMethods = BeanUtils.getMethods(controllerType);
            for (Method targetMethod : actionMethods) {
                if (targetMethod.getAnnotation(Path.class) == null)
                    continue;
                restfulList.add(new RestfulInvoke(this.appContext, targetMethod));
            }
        }
        Collections.sort(restfulList, new Comparator<RestfulInvoke>() {
            public int compare(RestfulInvoke o1, RestfulInvoke o2) {
                return o1.getRestfulMapping().compareToIgnoreCase(o2.getRestfulMapping());
            }
        });
        this.invokeArray = restfulList.toArray(new RestfulInvoke[restfulList.size()]);
    }
    public void destroy() {}
    //
    //
    //
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String actionPath = request.getRequestURI().substring(request.getContextPath().length());
        //1.��ȡ ActionInvoke
        RestfulInvoke define = this.getRestfulInvoke(request.getMethod(), actionPath);
        if (define == null) {
            chain.doFilter(request, resp);
            return;
        }
        //3.ִ�е���
        this.doInvoke(define, request, resp);
    }
    private RestfulInvoke getRestfulInvoke(String httpMethod, String requestPath) {
        for (RestfulInvoke restAction : this.invokeArray) {
            if (requestPath.matches(restAction.getRestfulMappingMatches()) == true) {
                if (restAction.matchingMethod(httpMethod))
                    return restAction;
            }
        }
        return null;
    }
    private void doInvoke(RestfulInvoke define, ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        try {
            define.invoke((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        } catch (Throwable e) {
            //1.���Բ��쳣
            Throwable target = ExceptionUtils.getCause(e);
            //            if (target instanceof UnhandledException)
            //                target = target.getCause();
            //            if (target instanceof InvocationTargetException)
            //                target = ((InvocationTargetException) target).getTargetException();
            //2.�ж��쳣����
            if (target instanceof ServletException)
                throw (ServletException) target;
            if (target instanceof IOException)
                throw (IOException) target;
            throw new ServletException(target);
        }
    }
    //
    //
    //
    /** Ϊת���ṩ֧�� */
    public RequestDispatcher getRequestDispatcher(final String newRequestUri, final HttpServletRequest request) {
        // TODO ��Ҫ�����������Ƿ����Servlet�淶����request���������Ҳ��Ҫ��飩
        //1.��������ַ���
        final RestfulInvoke define = getRestfulInvoke(request.getMethod(), newRequestUri);
        if (define == null)
            return null;
        //
        return new RequestDispatcher() {
            public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                servletRequest.setAttribute(REQUEST_DISPATCHER_REQUEST, Boolean.TRUE);
                /*ִ��servlet*/
                try {
                    doInvoke(define, servletRequest, servletResponse);
                } finally {
                    servletRequest.removeAttribute(REQUEST_DISPATCHER_REQUEST);
                }
            }
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
                    doInvoke(define, servletRequest, servletResponse);
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
        public String getRequestURI() {
            return newRequestUri;
        }
    }
}