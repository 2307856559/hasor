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
package org.more.webui.event;
import org.more.core.error.InitializationException;
/**
 * �¼���һ��֪ͨ���ƣ�ʹ���¼����ܿ����������̵�ִ�С�����ȴ����ͨ���¼���֪�ڲ��Ĺ���״̬��
 * �ýӿڱ�ʾ����һ��{@link EventManager}���Ա�ʶ������¼���
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public class Event {
    private String eventType = null;
    public Event(String eventType) {
        this.eventType = eventType;
    }
    public String getEventType() {
        return eventType;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return this.toString().equals(obj.toString());
    }
    @Override
    public String toString() {
        return "Event " + eventType;
    }
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    //----------------------------------------
    /**��ȡָ�������¼������������Ϊ����ֱ�ӷ��ؿ�ֵ���¼�������hypha����ȫ��Ψһ�ģ���������Ŀ����Ϊ�˼���new��������*/
    public static Event getEvent(String eventType) throws InitializationException {
        if (eventType == null)
            return null;
        return new Event(eventType);
    }
};