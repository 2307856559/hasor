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
package org.platform.event;
import org.platform.event.Event;
import org.platform.event.EventListener;
/**
 * �¼����������ýӿڵ�Ŀ����Ϊ�˴���{@link Event}�����¼���
 * @version : 2013-5-6
 * @author ������ (zyc@byshell.org)
 */
public interface EventManager {
    /**���һ�������¼����¼���������*/
    public void addEventListener(String eventType, EventListener listener);
    /**���һ�������¼����¼���������*/
    public void addEventListener(Event eventType, EventListener listener);
    /**ɾ��ĳ����������ע�ᡣ*/
    public void removeEventListener(EventListener listener);
    /**ͬ����ʽ�׳��¼���*/
    public void throwEvent(String eventType);
    /**ͬ����ʽ�׳��¼���*/
    public void throwEvent(Event eventType);
    /**�첽��ʽ�׳��¼���*/
    public void asynThrowEvent(String eventType);
    /**�첽��ʽ�׳��¼���*/
    public void asynThrowEvent(Event eventType);
}