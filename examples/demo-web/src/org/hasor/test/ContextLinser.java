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
package org.hasor.test;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hasor.servlet.anno.WebContextListener;
@WebContextListener
public class ContextLinser implements ServletContextListener {
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("contextDestroyed.");
    }
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("contextInitialized.");
        
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("org");
            while(urls.hasMoreElements())
                System.out.println(urls.nextElement());
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("----------------------------------------------");
        //
        try {
            Enumeration<URL> urls = findAllClassPath("org");
            while(urls.hasMoreElements())
                System.out.println(urls.nextElement());
        } catch (Exception e) {
            // TODO: handle exception
        }

        
    }
    /**��ȡ����ClassPath��Ŀ*/
    public static Enumeration<URL> findAllClassPath(String name) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = null;
        if (loader instanceof URLClassLoader == false)
            urls = loader.getResources(name);
        else {
            URLClassLoader urlLoader = (URLClassLoader) loader;
            /*
             * Jetty ʹ��getResources��Tomcat ʹ��findResources
             * ��Jetty��WebappsClassLoaderֻʵ����û����дfindResources
             * ��Tomcat��WebappsClassLoaderֻʵ����û����дgetResources
             * 
             * TODO : �ô��߼�Ϊ�������ж�findResources�����Ƿ���д���������д��������������getResources
             */
            try {
                Class<?> loaderType = urlLoader.getClass();
                Method m = loaderType.getMethod("findResources", String.class);
                if (m.getDeclaringClass() == loaderType)
                    urls = urlLoader.findResources(name);
                else
                    urls = urlLoader.getResources(name);
            } catch (Exception e) {
                urls = urlLoader.findResources(name);//Default
            }
        }
        return urls;
    }
}