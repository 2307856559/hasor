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
package org.hasor.servlet;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSessionListener;
import org.hasor.context.ApiBinder;
import com.google.inject.Binder;
import com.google.inject.Key;
/**
 * �����Ǵ�����{@link Binder}�����ṩ��ע��Servlet��Filter�ķ�����
 * @version : 2013-4-10
 * @author ������ (zyc@hasor.net)
 */
public interface WebApiBinder extends ApiBinder {
    /**ʹ�ô�ͳ���ʽ������һ��{@link FilterBindingBuilder}��*/
    public FilterBindingBuilder filter(String urlPattern, String... morePatterns);
    /**ʹ��������ʽ������һ��{@link FilterBindingBuilder}��*/
    public FilterBindingBuilder filterRegex(String regex, String... regexes);
    /**ʹ�ô�ͳ���ʽ������һ��{@link ServletBindingBuilder}��*/
    public ServletBindingBuilder serve(String urlPattern, String... morePatterns);
    /**ʹ��������ʽ������һ��{@link ServletBindingBuilder}��*/
    public ServletBindingBuilder serveRegex(String regex, String... regexes);
    /**��һ��Servlet �쳣�������*/
    public ErrorBindingBuilder error(Class<? extends Throwable> error);
    /**ע��һ��Session��������*/
    public SessionListenerBindingBuilder sessionListener();
    /**��������Filter���ο�Guice 3.0�ӿ���ơ�*/
    public static interface FilterBindingBuilder {
        public void through(Class<? extends Filter> filterKey);
        public void through(Key<? extends Filter> filterKey);
        public void through(Filter filter);
        public void through(Class<? extends Filter> filterKey, Map<String, String> initParams);
        public void through(Key<? extends Filter> filterKey, Map<String, String> initParams);
        public void through(Filter filter, Map<String, String> initParams);
    }
    /**��������Servlet���ο�Guice 3.0�ӿ���ơ�*/
    public static interface ServletBindingBuilder {
        public void with(Class<? extends HttpServlet> servletKey);
        public void with(Key<? extends HttpServlet> servletKey);
        public void with(HttpServlet servlet);
        public void with(Class<? extends HttpServlet> servletKey, Map<String, String> initParams);
        public void with(Key<? extends HttpServlet> servletKey, Map<String, String> initParams);
        public void with(HttpServlet servlet, Map<String, String> initParams);
    }
    /**��������Error��*/
    public static interface ErrorBindingBuilder {
        public void bind(Class<? extends WebErrorHook> errorKey);
        public void bind(Key<? extends WebErrorHook> errorKey);
        public void bind(WebErrorHook webErrorHook);
        public void bind(Class<? extends WebErrorHook> errorKey, Map<String, String> initParams);
        public void bind(Key<? extends WebErrorHook> errorKey, Map<String, String> initParams);
        public void bind(WebErrorHook webErrorHook, Map<String, String> initParams);
    }
    /**��������SessionListener��*/
    public static interface SessionListenerBindingBuilder {
        public void bind(Class<? extends HttpSessionListener> listenerKey);
        public void bind(Key<? extends HttpSessionListener> listenerKey);
        public void bind(HttpSessionListener sessionListener);
    }
}