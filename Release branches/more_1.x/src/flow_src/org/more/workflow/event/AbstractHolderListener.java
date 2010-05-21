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
package org.more.workflow.event;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * AbstractHolderListener�ṩ���¼��ַ�ʵ�֡�
 * Date : 2010-5-21
 * @author ������
 */
public abstract class AbstractHolderListener implements ListenerHolder {
    private final List<EventListener> listeners = new ArrayList<EventListener>(); //������¼�����������
    @Override
    public Iterator<EventListener> getListeners() {
        return this.listeners.iterator();
    };
    /**����һ���¼���������*/
    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    };
    /**ɾ��һ���¼���������*/
    public void removeListener(EventListener listener) {
        if (this.listeners.contains(listener) == true)
            this.listeners.remove(listener);
    };
    /**����ǰ�������������¼���������onEvent�׶Ρ�*/
    protected void event(Event event) {
        for (EventListener listener : this.listeners)
            listener.onEvent(event);
    };
};