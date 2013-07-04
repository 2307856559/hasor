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
package org.hasor.startup;
import static org.hasor.HasorFramework.Platform_LoadPackages;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.hasor.HasorFramework;
import org.hasor.binder.SessionListenerPipeline;
import org.hasor.context.AppContext;
import org.hasor.context.PlatformListener;
import org.hasor.context.core.AbstractAppContext;
import org.hasor.context.core.WebPlatformAppContext;
import org.more.util.ClassUtils;
/**
 * ����ʵ���������������¶�����<br/>
 * <pre>
 * 1.SpanClasses -> 2.add internal -> 3. Decide Listener -> 4.Create InitHook ->
 * 4.Create Event & InitContext -> 5.Create Guice -> 6.do ContextListener ->
 * </pre>
 * @version : 2013-3-25
 * @author ������ (zyc@byshell.org)
 */
public class RuntimeListener implements ServletContextListener, HttpSessionListener {
    public static final String      AppContextName          = AppContext.class.getName();
    private AbstractAppContext      appContext              = null;
    private SessionListenerPipeline sessionListenerPipeline = null;
    /*----------------------------------------------------------------------------------------------------*/
    /***/
    protected AbstractAppContext createAppContext(ServletContext sc) {
        return new WebPlatformAppContext(sc);
    }
    //
    /**��ȡ���������ͼ��ϣ������������������б����InitListenerע������ͣ����Ҹ�����ʵ����{@link PlatformListener}�ӿڡ�*/
    protected void loadPlatformListener(AbstractAppContext appContext) {
        //1.ɨ��classpath��
        String spanPackages = appContext.getSettings().getString(Platform_LoadPackages);
        String[] spanPackage = spanPackages.split(",");
        HasorFramework.info("loadPackages : " + HasorFramework.logString(spanPackage));
        Set<Class<?>> initHookSet = ClassUtils.getClassSet(spanPackage, PlatformExt.class);
        //2.����δʵ��ContextListener�ӿڵı�ע
        List<Class<? extends PlatformListener>> initHookList = new ArrayList<Class<? extends PlatformListener>>();
        for (Class<?> cls : initHookSet) {
            if (PlatformListener.class.isAssignableFrom(cls) == false) {
                HasorFramework.warning("not implemented ContextListener :%s", cls);
            } else {
                initHookList.add((Class<? extends PlatformListener>) cls);
            }
        }
        //3.������������
        Collections.sort(initHookList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                PlatformExt o1Anno = o1.getAnnotation(PlatformExt.class);
                PlatformExt o2Anno = o2.getAnnotation(PlatformExt.class);
                int o1AnnoIndex = o1Anno.startIndex();
                int o2AnnoIndex = o2Anno.startIndex();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        HasorFramework.info("find ContextListener : " + HasorFramework.logString(initHookList));
        //4.ɨ������ContextListener��
        HasorFramework.info("create ContextListener...");
        for (Class<?> listenerClass : initHookList) {
            PlatformListener listenerObject = this.createInitListenerClasse(listenerClass);
            if (listenerObject != null)
                appContext.addContextListener(listenerObject);
        }
    }
    /**����{@link PlatformListener}�ӿڶ���*/
    protected PlatformListener createInitListenerClasse(Class<?> listenerClass) {
        try {
            return (PlatformListener) listenerClass.newInstance();
        } catch (Exception e) {
            HasorFramework.error("create %s an error!%s", listenerClass, e);
            return null;
        }
    }
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //1.����AppContext
        this.appContext = this.createAppContext(servletContextEvent.getServletContext());
        this.loadPlatformListener(appContext);
        appContext.start();
        //2.��ȡSessionListenerPipeline
        this.sessionListenerPipeline = this.appContext.getInstance(SessionListenerPipeline.class);
        this.sessionListenerPipeline.init(this.appContext);
        HasorFramework.info("sessionListenerPipeline created.");
        //3.����ServletContext������
        HasorFramework.info("ServletContext Attribut : " + AppContextName + " -->> " + HasorFramework.logString(this.appContext));
        servletContextEvent.getServletContext().setAttribute(AppContextName, this.appContext);
        HasorFramework.info("platform started!");
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        this.appContext.destroyed();
    }
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        this.sessionListenerPipeline.sessionCreated(se);
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        this.sessionListenerPipeline.sessionDestroyed(se);
    }
}