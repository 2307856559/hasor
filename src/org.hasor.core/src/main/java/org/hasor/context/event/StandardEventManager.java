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
package org.hasor.context.event;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.hasor.Hasor;
import org.hasor.context.EventManager;
import org.hasor.context.HasorEventListener;
import org.hasor.context.HasorSettingListener;
import org.hasor.context.Settings;
import org.more.util.ArrayUtils;
import org.more.util.StringUtils;
/**
 * ��׼�¼��������ӿڵ�ʵ����
 * @version : 2013-5-6
 * @author ������ (zyc@byshell.org)
 */
public class StandardEventManager implements EventManager {
    private Settings                          settings        = null;
    private ScheduledExecutorService          executorService = null;
    private Map<String, HasorEventListener[]> listenerMap     = new HashMap<String, HasorEventListener[]>();
    private ReadWriteLock                     listenerRWLock  = new ReentrantReadWriteLock();
    //
    public StandardEventManager(Settings settings) {
        this.settings = settings;
        this.executorService = Executors.newScheduledThreadPool(1);
        settings.addSettingsListener(new HasorSettingListener() {
            @Override
            public void onLoadConfig(Settings newConfig) {
                update();
            }
        });
        this.update();
    }
    /**��ȡSetting�ӿڶ���*/
    public Settings getSettings() {
        return this.settings;
    }
    private void update() {
        int eventThreadPoolSize = this.getSettings().getInteger("framework.eventThreadPoolSize", 20);
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executorService;
        threadPool.setCorePoolSize(eventThreadPoolSize);
    }
    /**��ȡִ���¼�ʹ�õ�ScheduledExecutorService�ӿڶ���*/
    protected ScheduledExecutorService getExecutorService() {
        return this.executorService;
    }
    @Override
    public void addEventListener(String eventType, HasorEventListener hasorEventListener) {
        this.listenerRWLock.writeLock().lock();//����(д)
        //
        Hasor.assertIsNotNull(hasorEventListener, "add EventListener object is null.");
        HasorEventListener[] eventListenerArray = this.listenerMap.get(eventType);
        if (eventListenerArray == null) {
            eventListenerArray = new HasorEventListener[] { hasorEventListener };
            this.listenerMap.put(eventType, eventListenerArray);
        } else {
            if (ArrayUtils.contains(eventListenerArray, hasorEventListener) == false) {
                eventListenerArray = ArrayUtils.addToArray(eventListenerArray, hasorEventListener);
                this.listenerMap.put(eventType, eventListenerArray);
            }
        }
        //
        this.listenerRWLock.writeLock().unlock();//����(д)
    }
    @Override
    public void removeAllEventListener(String eventType) {
        this.listenerRWLock.writeLock().lock();//����(д)
        //
        this.listenerMap.remove(eventType);
        //
        this.listenerRWLock.writeLock().unlock();//����(д)
    }
    @Override
    public void removeEventListener(String eventType, HasorEventListener hasorEventListener) {
        this.listenerRWLock.writeLock().lock();//����(д)
        //
        Hasor.assertIsNotNull(eventType, "remove eventType is null.");
        Hasor.assertIsNotNull(hasorEventListener, "remove EventListener object is null.");
        HasorEventListener[] eventListenerArray = this.listenerMap.get(eventType);
        if (!ArrayUtils.isBlank(eventListenerArray)) {
            eventListenerArray = ArrayUtils.removeInArray(eventListenerArray, hasorEventListener);
            this.listenerMap.put(eventType, eventListenerArray);
        }
        //
        this.listenerRWLock.writeLock().unlock();//����(д)
    }
    private static final HasorEventListener[] EmptyEventListener = new HasorEventListener[0];
    @Override
    public HasorEventListener[] getEventListener(String eventType) {
        this.listenerRWLock.readLock().lock();//����(��)
        //
        HasorEventListener[] eventListenerArray = this.listenerMap.get(eventType);
        if (eventListenerArray != null) {
            HasorEventListener[] array = new HasorEventListener[eventListenerArray.length];
            System.arraycopy(eventListenerArray, 0, array, 0, eventListenerArray.length);
            eventListenerArray = array;
        } else
            eventListenerArray = EmptyEventListener;
        //
        this.listenerRWLock.readLock().unlock();//����(��)
        return eventListenerArray;
    }
    @Override
    public String[] getEventTypes() {
        this.listenerRWLock.readLock().lock();//����(��)
        //
        Set<String> eventTypes = this.listenerMap.keySet();
        String[] eventTypeNames = eventTypes.toArray(new String[eventTypes.size()]);
        //
        this.listenerRWLock.readLock().unlock();//����(��)
        return eventTypeNames;
    }
    @Override
    public void doSyncEvent(String eventType, Object... objects) {
        if (StringUtils.isBlank(eventType) == true)
            return;
        this.listenerRWLock.readLock().lock();//����(��)
        HasorEventListener[] eventListenerArray = this.listenerMap.get(eventType);
        this.listenerRWLock.readLock().unlock();//����(��)
        //
        if (eventListenerArray != null) {
            for (HasorEventListener event : eventListenerArray)
                event.onEvent(eventType, objects);
        }
    }
    @Override
    public void doAsynEvent(final String eventType, final Object... objects) {
        if (StringUtils.isBlank(eventType) == true)
            return;
        this.listenerRWLock.readLock().lock();//����(��)
        final HasorEventListener[] eventListenerArray = this.listenerMap.get(eventType);
        this.listenerRWLock.readLock().unlock();//����(��)
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                if (eventListenerArray != null) {
                    for (HasorEventListener event : eventListenerArray)
                        event.onEvent(eventType, objects);
                }
            }
        });
    }
    @Override
    public synchronized void clean() {
        this.executorService.shutdownNow();
        this.executorService = Executors.newScheduledThreadPool(1);
        this.update();
    }
}