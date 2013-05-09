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
package org.platform.event.support;
import java.util.Set;
import org.more.util.ArrayUtil;
import org.more.util.StringUtil;
import org.platform.Platform;
import org.platform.binder.ApiBinder;
import org.platform.context.AppContext;
import org.platform.context.InitListener;
import org.platform.context.support.AbstractModuleListener;
import org.platform.event.EventListener;
import org.platform.event.EventManager;
import org.platform.event.Listener;
/**
 * �¼�����
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@InitListener(displayName = "EventModuleServiceListener", description = "org.platform.event���������֧�֡�", startIndex = -100)
public class EventModuleServiceListener extends AbstractModuleListener {
    private EventManager eventManager = null;
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        event.getGuiceBinder().bind(EventManager.class).to(DefaultEventManager.class).asEagerSingleton();
    }
    //
    /*װ��Listener*/
    protected void loadListener(AppContext appContext) {
        Platform.info("begin loadListener...");
        //1.��ȡ
        Set<Class<?>> listenerSet = appContext.getInitContext().getClassSet(Listener.class);
        for (Class<?> cls : listenerSet) {
            if (EventListener.class.isAssignableFrom(cls) == false) {
                Platform.warning("loadListener : not implemented EventListener of type %s.", cls);
            } else {
                try {
                    Listener annoListener = cls.getAnnotation(Listener.class);
                    EventListener eventListener = (EventListener) appContext.getInstance(cls);
                    String[] vals = annoListener.value();
                    if (ArrayUtil.isBlank(vals)) {
                        Platform.warning("missing eventType at listener %s.", new Object[] { vals });
                        continue;
                    }
                    for (String eventType : vals) {
                        if (StringUtil.isBlank(eventType) == true)
                            continue;
                        Platform.info("listener %s is listening on %s.", cls, eventType);
                        this.eventManager.addEventListener(eventType, eventListener);
                    }
                } catch (Exception e) {
                    Platform.warning("addEventListener error.%s", e);
                }
            }
        }
    }
    @Override
    public void initialized(AppContext appContext) {
        this.eventManager = appContext.getInstance(EventManager.class);
        if (this.eventManager instanceof ManagerLife)
            ((ManagerLife) this.eventManager).initLife(appContext);
        this.loadListener(appContext);
        this.eventManager.throwEvent(EventManager.OnStart);
        Platform.info("EventManager is started.");
    }
    @Override
    public void destroy(AppContext appContext) {
        this.eventManager.throwEvent(EventManager.OnDestroy);
        if (this.eventManager instanceof ManagerLife)
            ((ManagerLife) this.eventManager).destroyLife(appContext);
        Platform.info("EventManager is destroy.");
    }
}