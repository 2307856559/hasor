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
package net.hasor.core.event;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.hasor.core.Environment;
import net.hasor.core.EventCallBackHook;
import net.hasor.core.EventListener;
import net.hasor.core.Hasor;
import net.hasor.core.Settings;
import org.more.util.ArrayUtils;
import org.more.util.StringUtils;
/**
 * ��׼�¼��������ӿڵ�ʵ����
 * @version : 2013-5-6
 * @author ������ (zyc@hasor.net)
 */
public class StandardEventManager implements EventManager {
    private static final EmptyEventCallBackHook    EmptyAsyncCallBack = new EmptyEventCallBackHook();
    //
    private Settings                               settings           = null;
    private ScheduledExecutorService               executorService    = null;
    private Map<String, EventListener[]>           listenerMap        = new HashMap<String, EventListener[]>();
    private ReadWriteLock                          listenerRWLock     = new ReentrantReadWriteLock();
    private Map<String, LinkedList<EventListener>> onceListenerMap    = new HashMap<String, LinkedList<EventListener>>();
    private Lock                                   onceListenerLock   = new ReentrantLock();
    //
    public StandardEventManager(Environment env) {
        env = Hasor.assertIsNotNull(env, "Environment type parameter is empty!");
        this.settings = env.getSettings();
        this.executorService = Executors.newScheduledThreadPool(1);
        this.updateSettings();
    }
    private void updateSettings() {
        //����ThreadPoolExecutor
        int eventThreadPoolSize = this.getSettings().getInteger("hasor.eventThreadPoolSize", 20);
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executorService;
        threadPool.setCorePoolSize(eventThreadPoolSize);
        threadPool.setMaximumPoolSize(eventThreadPoolSize);
    }
    /**��ȡSetting�ӿڶ���*/
    public Settings getSettings() {
        return this.settings;
    }
    /**��ȡִ���¼�ʹ�õ�ScheduledExecutorService�ӿڶ���*/
    protected ScheduledExecutorService getExecutorService() {
        return this.executorService;
    }
    //
    public void pushListener(String eventType, EventListener eventListener) {
        if (StringUtils.isBlank(eventType) || eventListener == null)
            return;
        this.onceListenerLock.lock();//����
        LinkedList<EventListener> eventList = this.onceListenerMap.get(eventType);
        if (eventList == null) {
            eventList = new LinkedList<EventListener>();
            this.onceListenerMap.put(eventType, eventList);
        }
        if (eventList.contains(eventListener) == false)
            eventList.push(eventListener);
        this.onceListenerLock.unlock();//����
    }
    public void addListener(String eventType, EventListener eventListener) {
        this.listenerRWLock.writeLock().lock();//����(д)
        //
        Hasor.assertIsNotNull(eventListener, "add EventListener object is null.");
        EventListener[] eventListenerArray = this.listenerMap.get(eventType);
        if (eventListenerArray == null) {
            eventListenerArray = new EventListener[] { eventListener };
            this.listenerMap.put(eventType, eventListenerArray);
        } else {
            if (ArrayUtils.contains(eventListenerArray, eventListener) == false) {
                eventListenerArray = (EventListener[]) ArrayUtils.add(eventListenerArray, eventListener);
                this.listenerMap.put(eventType, eventListenerArray);
            }
        }
        //
        this.listenerRWLock.writeLock().unlock();//����(д)
    }
    public void removeListener(String eventType, EventListener eventListener) {
        this.listenerRWLock.writeLock().lock();//����(д)
        //
        Hasor.assertIsNotNull(eventType, "remove eventType is null.");
        Hasor.assertIsNotNull(eventListener, "remove EventListener object is null.");
        EventListener[] eventListenerArray = this.listenerMap.get(eventType);
        if (!ArrayUtils.isEmpty(eventListenerArray)) {
            int index = ArrayUtils.indexOf(eventListenerArray, eventListener);
            eventListenerArray = (EventListener[]) ((index == ArrayUtils.INDEX_NOT_FOUND) ? eventListenerArray : ArrayUtils.remove(eventListenerArray, index));
            this.listenerMap.put(eventType, eventListenerArray);
        }
        //
        this.listenerRWLock.writeLock().unlock();//����(д)
    }
    //
    public final void fireSyncEvent(String eventType, Object... objects) {
        this.fireSyncEvent(eventType, null, objects);
    }
    public final void fireSyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.fireEvent(eventType, true, callBack, objects);
    }
    public final void fireAsyncEvent(String eventType, Object... objects) {
        this.fireAsyncEvent(eventType, null, objects);
    }
    public final void fireAsyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.fireEvent(eventType, false, callBack, objects);
    }
    private final void fireEvent(String eventType, boolean sync, EventCallBackHook callBack, Object... objects) {
        EventObject event = createEvent(eventType, sync);
        event.setCallBack(callBack);
        event.addParams(objects);
        this.fireEvent(event);
    }
    /**�����¼�����*/
    protected EventObject createEvent(String eventType, boolean sync) {
        return new EventObject(eventType, sync);
    };
    /**�����¼�*/
    protected void fireEvent(final EventObject event) {
        if (event.isSync()) {
            //ͬ����
            executeEvent(event);
        } else {
            //�첽��
            this.executorService.submit(new Runnable() {
                public void run() {
                    executeEvent(event);
                }
            });
        }
    };
    /**�����¼�*/
    private void executeEvent(EventObject eventObj) {
        String eventType = eventObj.getEventType();
        Object[] objects = eventObj.getParams();
        EventCallBackHook callBack = eventObj.getCallBack();
        callBack = (callBack != null ? callBack : EmptyAsyncCallBack);
        if (StringUtils.isBlank(eventType) == true)
            return;
        //
        //1.��������.
        this.listenerRWLock.readLock().lock();//����(��)
        EventListener[] eventListenerArray = this.listenerMap.get(eventType);
        this.listenerRWLock.readLock().unlock();//����(��)
        if (eventListenerArray != null) {
            for (EventListener listener : eventListenerArray) {
                try {
                    listener.onEvent(eventType, objects);
                } catch (Throwable e) {
                    callBack.handleException(eventType, objects, e);
                } finally {
                    callBack.handleComplete(eventType, objects);
                }
            }
        }
        //
        //2.����Once����.
        this.onceListenerLock.lock();//����
        LinkedList<EventListener> eventList = this.onceListenerMap.get(eventType);
        if (eventList != null) {
            EventListener listener = null;
            while ((listener = eventList.pollLast()) != null) {
                try {
                    listener.onEvent(eventType, objects);
                } catch (Throwable e) {
                    callBack.handleException(eventType, objects, e);
                } finally {
                    callBack.handleComplete(eventType, objects);
                }
            }
        }
        this.onceListenerLock.unlock();//����
    };
    //
    public void release() {
        this.onceListenerLock.lock();//����
        this.onceListenerMap.clear();
        this.onceListenerLock.unlock();//����
        //
        this.executorService.shutdownNow();
        this.executorService = Executors.newScheduledThreadPool(1);
        this.updateSettings();
        //
        this.listenerRWLock.writeLock().lock();//����
        this.listenerMap.clear();
        this.listenerRWLock.writeLock().unlock();//����
    }
}