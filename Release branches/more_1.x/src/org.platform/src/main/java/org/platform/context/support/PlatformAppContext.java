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
package org.platform.context.support;
import static org.platform.PlatformConfig.Platform_LoadPackages;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.more.util.ClassUtil;
import org.platform.Assert;
import org.platform.Platform;
import org.platform.binder.support.ApiBinderModule;
import org.platform.context.AppContext;
import org.platform.context.BeanContext;
import org.platform.context.ContextListener;
import org.platform.context.InitListener;
import org.platform.context.Settings;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
/**
 * {@link AppContext}�ӿڵĳ���ʵ���ࡣ
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public class PlatformAppContext extends AbstractAppContext {
    private Injector guice    = null;
    private Object   context  = null;
    private Settings settings = null;
    //
    public PlatformAppContext(Object context) {
        this.context = context;
    }
    public Injector getGuice() {
        return this.guice;
    }
    @Override
    public Object getContext() {
        return this.context;
    }
    @Override
    public Settings getSettings() {
        if (this.settings == null)
            this.settings = new PlatformSettings();
        return this.settings;
    }
    /**����*/
    public synchronized void start() {
        //1.settings��
        final Settings settings = this.getSettings();
        final Object contet = this.getContext();
        final AppContext appContet = this;
        //2.����ApiBinderModule��
        final ApiBinderModule systemModule = new ApiBinderModule(settings, contet) {
            @Override
            public void configure(Binder binder) {
                super.configure(binder);
                /*��BeanContext�����Provider*/
                binder.bind(BeanContext.class).toProvider(new Provider<BeanContext>() {
                    @Override
                    public BeanContext get() {
                        return appContet;
                    }
                });
                /*��AppContext�����Provider*/
                binder.bind(AppContext.class).toProvider(new Provider<AppContext>() {
                    @Override
                    public AppContext get() {
                        return appContet;
                    }
                });
            }
        };
        //3.ɨ������ContextListener��
        List<Class<? extends ContextListener>> initHookList = this.searchListenerClasses();
        Platform.info("find ContextListener : " + Platform.logString(initHookList));
        Platform.info("create ContextListener...");
        for (Class<?> listenerClass : initHookList) {
            ContextListener listenerObject = this.createInitListenerClasse(listenerClass);
            if (listenerObject != null)
                settings.addContextListener(listenerObject);
        }
        //4.����Guice��init ע���ࡣ
        Platform.info("initialize ...");
        this.guice = this.createInjector(systemModule);
        Assert.isNotNull(this.guice, "can not be create Injector.");
        Platform.info("init modules finish.");
        //4.������ɳ�ʼ���ź�
        Platform.info("send Initialized sign.");
        final ContextListener[] listenerList = this.getSettings().getContextListeners();
        if (listenerList != null) {
            for (ContextListener listener : listenerList) {
                if (listener == null)
                    continue;
                listener.initialized(this);
            }
        }
        Platform.info("platform started!");
    }
    /**���ٷ�����*/
    public synchronized void destroyed() {
        final ContextListener[] listenerList = this.getSettings().getContextListeners();
        if (listenerList != null) {
            for (ContextListener listener : listenerList)
                listener.destroy(this);
        }
    }
    /**ͨ��guice����{@link Injector}*/
    protected Injector createInjector(Module systemModule) {
        return Guice.createInjector(systemModule);
    }
    //
    /**��ȡ���������ͼ��ϣ������������������б����InitListenerע������ͣ����Ҹ�����ʵ����{@link ContextListener}�ӿڡ�*/
    protected List<Class<? extends ContextListener>> searchListenerClasses() {
        //1.ɨ��classpath��
        String spanPackages = this.getSettings().getString(Platform_LoadPackages);
        String[] spanPackage = spanPackages.split(",");
        Platform.info("loadPackages : " + Platform.logString(spanPackage));
        Set<Class<?>> initHookSet = ClassUtil.getClassSet(spanPackage, InitListener.class);
        //2.����δʵ��ContextListener�ӿڵı�ע
        List<Class<? extends ContextListener>> initHookList = new ArrayList<Class<? extends ContextListener>>();
        for (Class<?> cls : initHookSet) {
            if (ContextListener.class.isAssignableFrom(cls) == false) {
                Platform.warning("not implemented ContextListener :%s", cls);
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
            Platform.error("create %s an error!%s", listenerClass, e);
            return null;
        }
    }
}