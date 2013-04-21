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
package org.platform.startup;
import static org.platform.PlatformConfigEnum.Platform_LoadPackages;
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
import org.more.util.ClassUtil;
import org.platform.Assert;
import org.platform.Platform;
import org.platform.binder.AbstractApiBinder;
import org.platform.binder.ApiBinder;
import org.platform.binder.ApiBinderModule;
import org.platform.binder.SessionListenerPipeline;
import org.platform.context.AbstractAppContext;
import org.platform.context.AbstractInitContext;
import org.platform.context.AppContext;
import org.platform.context.ContextListener;
import org.platform.context.InitContext;
import org.platform.context.InitListener;
import org.platform.context.setting.AbstractConfig;
import org.platform.context.setting.Config;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
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
    private List<ContextListener>   initListener            = new ArrayList<ContextListener>();
    private Config                  settings                = null;
    private Injector                guice                   = null;
    private AppContext              appContext              = null;
    private InitContext             initContext             = null;
    private SessionListenerPipeline sessionListenerPipeline = null;
    /*----------------------------------------------------------------------------------------------------*/
    protected final Config getSettings() {
        return this.settings;
    }
    protected final Injector getGuice() {
        return this.guice;
    }
    protected final AppContext getAppContext() {
        return this.appContext;
    }
    protected final InitContext getInitContext() {
        return this.initContext;
    }
    /*----------------------------------------------------------------------------------------------------*/
    //
    /**����Guice���󣬵�Ҫ��systemModule���뵽Module�У������ʼ�����̻�ʧ�ܡ�*/
    protected Injector createInjector(Module systemModule) {
        return Guice.createInjector(systemModule);
    }
    //
    /**��ȡ���������ͼ��ϣ������������������б����InitListenerע������ͣ����Ҹ�����ʵ����{@link ContextListener}�ӿڡ�*/
    protected List<Class<? extends ContextListener>> searchListenerClasses() {
        //1.ɨ��classpath��
        String spanPackages = this.settings.getSettings().getString(Platform_LoadPackages);
        String[] spanPackage = spanPackages.split(",");
        Platform.info("loadPackages : " + Platform.logString(spanPackage));
        Set<Class<?>> initHookSet = ClassUtil.getClassSet(spanPackage, InitListener.class);
        //2.����δʵ��ContextListener�ӿڵı�ע
        List<Class<? extends ContextListener>> initHookList = new ArrayList<Class<? extends ContextListener>>();
        for (Class<?> cls : initHookSet) {
            if (ContextListener.class.isAssignableFrom(cls) == false) {
                Platform.warning("not implemented ContextListener ��" + Platform.logString(cls));
            } else {
                initHookList.add((Class<? extends ContextListener>) cls);
            }
        }
        //������������
        Collections.sort(initHookList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                InitListener o1Anno = o1.getAnnotation(InitListener.class);
                InitListener o2Anno = o2.getAnnotation(InitListener.class);
                int o1AnnoIndex = o1Anno.startIndex();
                int o2AnnoIndex = o2Anno.startIndex();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        return initHookList;
    }
    //
    /**����{@link ContextListener}�ӿڶ���*/
    protected ContextListener createInitListenerClasse(Class<?> listenerClass) {
        try {
            return (ContextListener) listenerClass.newInstance();
        } catch (Exception e) {
            Platform.error("create " + Platform.logString(listenerClass) + " an error.", e);
            return null;
        }
    }
    //
    /**����{@link Config}�ӿڶ���*/
    protected Config createSettings(ServletContext servletContext) {
        return new AbstractConfig(servletContext) {
            @Override
            protected List<String> loadNameSpaceDefinition() {
                return null;
            }
        };
    }
    //
    /**����{@link InitContext}�ӿڶ���*/
    protected InitContext createInitContext() {
        return new AbstractInitContext(this.settings) {};
    }
    //
    /**����{@link AppContext}�ӿڶ���*/
    protected AppContext createAppContext() {
        return new AbstractAppContext(this.guice) {};
    }
    /*----------------------------------------------------------------------------------------------------*/
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //1.����settings��
        Platform.info("createSettings...");
        this.settings = this.createSettings(servletContextEvent.getServletContext());
        //2.ɨ������ContextListener��
        List<Class<? extends ContextListener>> initHookList = this.searchListenerClasses();
        Platform.info("find ContextListener : " + Platform.logString(initHookList));
        //3.��ʼ��ִ�й��ӡ�
        Platform.info("create ContextListener...");
        for (Class<?> listenerClass : initHookList) {
            ContextListener listenerObject = this.createInitListenerClasse(listenerClass);
            if (listenerObject != null)
                this.initListener.add(listenerObject);
        }
        //4.׼��PlatformContextEvent��sysModule����
        this.initContext = this.createInitContext();
        final ListenerApiBinder apiBinder = new ListenerApiBinder(initContext);
        final Module systemModule = new ApiBinderModule(this.initListener) {
            @Override
            protected ApiBinder getApiBinder(Binder guiceBinder) {
                apiBinder.setGuiceBinder(guiceBinder);/*���ڸ�apiBinder����guiceBinder��*/
                return apiBinder;
            }
            @Override
            public void configure(Binder binder) {
                super.configure(binder);
                binder.bind(AppContext.class).toProvider(new Provider<AppContext>() {
                    @Override
                    public AppContext get() {
                        return getAppContext();
                    }
                });
            }
        };
        //5.����Guice��init @InitContextע���ࡣ
        Platform.info("initialize ...");
        this.guice = this.createInjector(systemModule);
        Assert.isNotNull(this.guice, "can not be create Injector.");
        Platform.info("init modules finish.");
        //6.����AppContext
        Platform.info("createAppContext...");
        this.appContext = this.createAppContext();
        //7.��ȡSessionListenerPipeline
        Platform.info("SessionListenerPipeline createInstance...");
        this.sessionListenerPipeline = this.guice.getInstance(SessionListenerPipeline.class);
        this.sessionListenerPipeline.init(this.appContext);
        //8.������ɳ�ʼ���ź�
        Platform.info("send Initialized sign.");
        for (ContextListener listener : this.initListener) {
            if (listener == null)
                continue;
            listener.initialized(this.appContext);
        }
        //9.����ServletContext������
        Platform.info("ServletContext Attribut : " + AppContextName + " -->> " + Platform.logString(this.appContext));
        servletContextEvent.getServletContext().setAttribute(AppContextName, this.appContext);
        Platform.info("platform started!");
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        final List<ContextListener> listenerList = this.initListener;
        for (ContextListener listener : listenerList)
            listener.destroy();
    }
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        this.sessionListenerPipeline.sessionCreated(se);
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        this.sessionListenerPipeline.sessionDestroyed(se);
    }
    private static class ListenerApiBinder extends AbstractApiBinder {
        private Binder guiceBinder = null;
        public ListenerApiBinder(InitContext initContext) {
            super(initContext);
        }
        @Override
        public Binder getGuiceBinder() {
            return this.guiceBinder;
        }
        public void setGuiceBinder(Binder guiceBinder) {
            this.guiceBinder = guiceBinder;
        }
    }
}