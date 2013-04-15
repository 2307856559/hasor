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
package org.platform.runtime.startup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.more.util.ClassUtil;
import org.platform.Assert;
import org.platform.api.binder.AbstractApiBinder;
import org.platform.api.binder.ApiBinder;
import org.platform.api.binder.ApiBinderModule;
import org.platform.api.context.Config;
import org.platform.api.context.ContextListener;
import org.platform.api.context.InitContext;
import org.platform.api.context.InitListener;
import org.platform.runtime.Platform;
import org.platform.runtime.config.AppConfig;
import org.platform.runtime.context.AbstractInitContext;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
/**
 * ����ʵ���������������¶�����<br/>
 * <pre>
 * 1.SpanClasses -> 2.add internal -> 3. Decide Listener -> 4.Create InitHook ->
 * 4.Create Event & InitContext -> 5.Create Guice -> 6.do ContextListener ->
 * </pre>
 * @version : 2013-3-25
 * @author ������ (zyc@byshell.org)
 */
public class RuntimeListener implements ServletContextListener {
    protected final static String RuntimeGuice = Guice.class.getName();
    private List<ContextListener> initListener = new ArrayList<ContextListener>();
    /*----------------------------------------------------------------------------------------------------*/
    //
    /**��������Զ���Injector���󣬵�Ҫ��systemModule���뵽Module�У������ʼ�����̻�ʧ�ܡ�*/
    protected Injector getInjector(Module systemModule) {
        return Guice.createInjector(systemModule);
    }
    //
    /**��ȡ���õ�{@link ContextListener}����Щ����������ɾ�����������ͨ���÷����õ�ϵͳ��������Щ���ü�������*/
    protected final List<Class<?>> internalInitListenerClasses() {
        List<Class<?>> internal = new ArrayList<Class<?>>();
        //internal.add(ServicesServiceSupportListener.class);
        //internal.add(SafetyServiceSupportListener.class);
        return internal;
    }
    //
    /**��ȡ���������ͼ��ϣ������������������б����InitListenerע������͡�*/
    protected Set<Class<?>> findInitListenerClasses() {
        String[] spanPackage = new String[] { "org.*", "com.*", "net.*", "cn.*" };
        Platform.info("loadPackages : " + Platform.logString(spanPackage));
        return ClassUtil.getClassSet(spanPackage, InitListener.class);
    }
    //
    /**�÷��������վ�����ʼ����ЩInitListener����ʼ��˳���շ��ؼ��ϵ�˳��*/
    protected List<Class<?>> decideInitListenerClasses(List<Class<?>> sourceList) {
        Collections.sort(sourceList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                InitListener o1Anno = o1.getAnnotation(InitListener.class);
                InitListener o2Anno = o2.getAnnotation(InitListener.class);
                int o1AnnoIndex = o1Anno.startIndex();
                int o2AnnoIndex = o2Anno.startIndex();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        return sourceList;
    }
    //
    /**����{@link ContextListener}���Ͷ���*/
    protected ContextListener createInitListenerClasse(Class<?> listenerClass) {
        try {
            return (ContextListener) listenerClass.newInstance();
        } catch (Exception e) {
            Platform.error("create " + Platform.logString(listenerClass) + " an error.", e);
            return null;
        }
    }
    /*----------------------------------------------------------------------------------------------------*/
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //1.ɨ������ContextListener��
        Set<Class<?>> initHookSet = this.findInitListenerClasses();
        initHookSet = (initHookSet == null) ? new HashSet<Class<?>>() : initHookSet;
        //2.��������ContextListener
        List<Class<?>> initHookList = new ArrayList<Class<?>>(initHookSet);
        List<Class<?>> internalListener = this.internalInitListenerClasses();
        for (Class<?> internal : internalListener)
            if (initHookList.contains(internal) == false)
                initHookList.add(internal);
        //3.��������ContextListener���ϣ�ͬʱ������ʼ��˳��
        initHookList = this.decideInitListenerClasses(initHookList);
        Platform.info("find ContextListener : " + Platform.logString(initHookList));
        Platform.info("create ContextListener...");
        //4.��ʼ��ִ�й��ӡ�
        for (Class<?> listenerClass : initHookList) {
            if (ContextListener.class.isAssignableFrom(listenerClass) == false) {
                Platform.warning("not implemented ContextListener ��" + Platform.logString(listenerClass));
                continue;
            }
            ContextListener listenerObject = this.createInitListenerClasse(listenerClass);
            if (listenerObject != null)
                this.initListener.add(listenerObject);
        }
        //5.׼��PlatformContextEvent��sysModule����
        final Config config = new AppConfig(servletContextEvent.getServletContext());
        final AbstractInitContext initContext = new AbstractInitContext(config) {};
        final ListenerApiBinder apiBinder = new ListenerApiBinder(initContext);
        final Module systemModule = new ApiBinderModule(this.initListener) {
            @Override
            protected ApiBinder getApiBinder(Binder guiceBinder) {
                apiBinder.setGuiceBinder(guiceBinder);/*���ڸ�apiBinder����guiceBinder��*/
                return apiBinder;
            }
        };
        //6.����Guice��init @InitContextע���ࡣ
        Platform.info("initialize ...");
        Injector guice = this.getInjector(systemModule);
        Assert.isNotNull(guice, "can not be create Injector.");
        //7.������ʼ��PlatformBuild����
        //8.������ɳ�ʼ���ź�
        Platform.info("send Initialized sign.");
        for (ContextListener listener : this.initListener) {
            if (listener == null)
                continue;
            listener.initialized();
        }
        Platform.info("initialization finish.");
        //9.����ServletContext������
        Platform.info("ServletContext Attribut : " + RuntimeGuice + " -->> " + Platform.logString(guice));
        config.getServletContext().setAttribute(RuntimeGuice, guice);
        Platform.info("platform started!");
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        final List<ContextListener> listenerList = this.initListener;
        for (ContextListener listener : listenerList)
            listener.destroy();
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