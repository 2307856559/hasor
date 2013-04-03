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
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.more.util.ClassUtil;
import org.platform.Assert;
import org.platform.api.context.AppContext;
import org.platform.api.context.ContextConfig;
import org.platform.api.context.ContextEvent;
import org.platform.api.context.ContextListener;
import org.platform.api.context.InitContext;
import org.platform.runtime.Platform;
import org.platform.runtime.PlatformFactoryFinder;
import org.platform.runtime.context.AppContextFactory;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
/**
 * 
 * @version : 2013-3-25
 * @author ������ (zyc@byshell.org)
 */
public class PlatformListener implements ServletContextListener {
    protected final static String GuiceName          = Guice.class.getName();
    protected static final String ContextFactoryName = AppContextFactory.class.getName();
    private ContextConfig         contextConfig      = null;
    private List<ContextListener> initListener       = new ArrayList<ContextListener>();
    //
    //
    //
    /**��������Զ���Injector���󣬵�Ҫ��systemModule���뵽Module�С�*/
    protected Injector getInjector(Module systemModule) {
        return Guice.createInjector(systemModule);
    }
    /**��������Զ���Injector���󣬵�Ҫ��systemModule���뵽Module�С�*/
    protected AppContextFactory getContextFactory(ContextConfig config) {
        return PlatformFactoryFinder.getAppContextFactory(this.contextConfig);
    }
    /**��ʼ*/
    protected void initContext(ContextConfig config) {
        //1.ȷ��ɨ�跶Χ��
        String[] spanPackage = new String[] { "org.*" };
        Platform.info("loadPackages : " + Platform.logString(spanPackage));
        //2.ɨ������init���ӡ�
        Set<Class<?>> initHookSet = ClassUtil.getClassSet(spanPackage, InitContext.class);
        //3.�Թ��ӽ�������
        ArrayList<Class<?>> initHookList = new ArrayList<Class<?>>(initHookSet);
        Collections.sort(initHookList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                InitContext o1Anno = o1.getAnnotation(InitContext.class);
                InitContext o2Anno = o2.getAnnotation(InitContext.class);
                int o1AnnoIndex = o1Anno.startIndex();
                int o2AnnoIndex = o2Anno.startIndex();
                return (o1AnnoIndex < o2AnnoIndex ? -1 : (o1AnnoIndex == o2AnnoIndex ? 0 : 1));
            }
        });
        Platform.info("find ContextListener : " + Platform.logString(initHookList));
        Platform.info("create ContextListener...");
        //4.��ʼ��ִ�й��ӡ�
        for (Class<?> listenerClass : initHookList) {
            if (ContextListener.class.isAssignableFrom(listenerClass) == false) {
                Platform.warning("not implemented ContextListener ��" + Platform.logString(listenerClass));
                continue;
            }
            try {
                ContextListener listenerObject = (ContextListener) listenerClass.newInstance();
                this.initListener.add(listenerObject);
            } catch (Exception e) {
                Platform.error("create " + Platform.logString(listenerClass) + " error : " + Platform.logString(e));
            }
        }
        //5.׼��AppContext��sysModule����
        AppContextFactory factory = this.getContextFactory(this.contextConfig);
        Assert.isNotNull(factory, "can not be create AppContextFactory.");
        final AppContext appContext = factory.getAppContext(this.contextConfig.getServletContext());
        final List<ContextListener> listenerList = this.initListener;
        Module sysModule = new Module() {
            @Override
            public void configure(Binder binder) {
                ContextEvent event = new ContextEvent(appContext, binder) {};
                for (ContextListener listener : listenerList) {
                    if (listener == null)
                        continue;
                    Platform.info("send ContextEvent to : " + Platform.logString(listener.getClass()));
                    listener.onContextInitialized(event);
                }
            }
        };
        //6.����Guice��init @InitContextע���ࡣ
        Platform.info("create guice and begin start...");
        Injector guice = this.getInjector(sysModule);
        Assert.isNotNull(guice, "can not be create Injector.");
        //7.����ExecuteCycle����
        //
        //8.����ServletContext������
        Platform.info("ServletContext Attribut : " + ContextFactoryName + " -->> " + Platform.logString(factory));
        Platform.info("ServletContext Attribut : " + GuiceName + " -->> " + Platform.logString(guice));
        this.contextConfig.getServletContext().setAttribute(ContextFactoryName, factory);
        this.contextConfig.getServletContext().setAttribute(GuiceName, guice);
    }
    /**����*/
    protected void destroyContext(ContextConfig config) {
        final List<ContextListener> listenerList = this.initListener;
        for (ContextListener listener : listenerList)
            listener.onContextDestroyed();
    }
    //
    //
    //
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //1.����initConfig
        this.contextConfig = new PlatformContextConfig(servletContextEvent.getServletContext());
        //2.��ʼ����
        this.initContext(this.contextConfig);
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        this.destroyContext(this.contextConfig);
    }
}