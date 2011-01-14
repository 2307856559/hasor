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
package org.more.hypha.beans.assembler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.more.hypha.Event;
import org.more.hypha.EventListener;
import org.more.hypha.EventManager;
/**
 * ������{@link EventManager}�ӿڵ�һ������ʵ�֡�
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractEventManager implements EventManager {
    private LinkedList<Event>                                    eventQueue = new LinkedList<Event>();
    private HashMap<Class<? extends Event>, List<EventListener>> listener   = new HashMap<Class<? extends Event>, List<EventListener>>();
    //
    /**���һ�������¼����¼���������*/
    public void addEventListener(Class<? extends Event> eventType, EventListener listener) {
        List<EventListener> listeners = this.listener.get(eventType);
        if (listeners == null) {
            listeners = new ArrayList<EventListener>();
            this.listener.put(eventType, listeners);
        }
        listeners.add(listener);
    }
    /**���¼�ѹ�뵽�����еȴ�ִ�С�*/
    public void pushEvent(Event event) {
        this.eventQueue.add(event);
    }
    /**��������ڶ����еȴ�������¼���*/
    public synchronized void clearEvent() {
        this.eventQueue.clear();
    }
    /**��ն�����ָ�����͵��¼���*/
    public synchronized void clearEvent(Class<? extends Event> eventType) {
        LinkedList<Event> eList = new LinkedList<Event>();
        for (Event e : this.eventQueue)
            if (eventType.isAssignableFrom(eventType) == true)
                eList.add(e);
        this.eventQueue.removeAll(eList);
    }
    /**�������������¼����ڵ������������μ���ÿ���¼����¼������������һЩ�¼�û����Ӧ���¼���������ô��Щ�µĴ��������ԡ�*/
    public synchronized void popEvent() {
        for (Event e : eventQueue)
            this.doEvent(e);
        this.eventQueue.clear();
    }
    /**����ĳ���ض����͵��¼����ڵ������������μ���ÿ���¼����¼��������������Щ�¼�û����Ӧ���¼���������ô��Щ�µĴ��������ԡ�*/
    public synchronized void popEvent(Class<? extends Event> eventType) {
        LinkedList<Event> eList = new LinkedList<Event>();
        for (Event e : this.eventQueue)
            if (eventType.isAssignableFrom(eventType) == true)
                eList.add(e);
        this.eventQueue.removeAll(eList);
        for (Event e : eList)
            this.doEvent(e);
    }
    /**�ƹ��¼�����ֱ��֪ͨ�¼���������������¼���*/
    public void doEvent(Event event) {
        for (Class<?> eventType : this.listener.keySet())
            if (eventType.isAssignableFrom(event.getClass()) == true) {
                List<EventListener> listener = this.listener.get(eventType);
                for (EventListener el : listener)
                    el.onEvent(event);
            }
    }
}