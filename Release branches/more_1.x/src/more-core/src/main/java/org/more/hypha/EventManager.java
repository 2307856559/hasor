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
package org.more.hypha;
import org.more.hypha.Event.Sequence;
/**
 * �¼����������ýӿڵ�Ŀ����Ϊ�˴���{@link Event}�����¼����ýӿ��ṩ����һ���Ƚ��ȳ��Ķ��еķ�ʽ�����¼���
 * �¼������ǵ�ѹ�����ʱ�͵õ�������Ҫ���Եĵ��õ����¼������¼�����Ҳ����ͨ��doEvent������ȷ��������¼���
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public interface EventManager {
    /**����Ĭ���쳣����������ִ�м���������ʱ�������׳����쳣���иö�����д���*/
    public void setDefaultEventExceptionHandler(EventExceptionHandler<Event> handler);
    /**��ȡĬ�ϵ��쳣����������ִ�м���������ʱ�������׳����쳣���иö�����д���*/
    public EventExceptionHandler<Event> getDefaultEventExceptionHandler();
    /*------------------------------------------------------------------------------*/
    /**���һ�������¼����¼���������*/
    public void addEventListener(Event eventType, EventListener<?> listener);
    /**��������ڶ����еȴ�������¼���*/
    public void clearEvent();
    /**��ն�����ָ�����͵��¼���*/
    public void clearEvent(Event eventType);
    /**ɾ�������е�ĳһ����ִ�е��¼���*/
    public boolean removeEvent(Sequence sequence);
    /*------------------------------------------------------------------------------*/
    /**
     * ���¼�ѹ�뵽�����еȴ�ִ�С�
     * @param eventType Ҫѹ����¼����͡�
     * @param objects ���¼�Я���Ĳ�����Ϣ
     * @return ���ظ��¼��ڶ����е�λ�á�
     */
    public Sequence pushEvent(Event eventType, Object... objects);
    /**�������������¼��������ִ���¼��ڼ䷢���쳣��ʹ��Ĭ�ϵ��쳣�����������¼��쳣��*/
    public void popEvent();
    /**����ĳ���ض����͵��¼��������ִ���¼��ڼ䷢���쳣��ʹ��Ĭ�ϵ��쳣�����������¼��쳣��*/
    public void popEvent(Event eventType);
    /**�����ض�˳��λ�õ��¼��������ִ���¼��ڼ䷢���쳣��ʹ��Ĭ�ϵ��쳣�����������¼��쳣��*/
    public void popEvent(Sequence sequence);
    /**�ƹ��¼�����ֱ��֪ͨ�¼���������������¼��������ִ���¼��ڼ䷢���쳣��ʹ��Ĭ�ϵ��쳣�����������¼��쳣��*/
    public void doEvent(Event eventType, Object... objects);
    /*------------------------------------------------------------------------------*/
    /**
     * ���¼�ѹ�뵽�����еȴ�ִ�У�����ʹ��ָ����
     * @param eventType Ҫѹ����¼����͡�
     * @param handler ʹ�õ��쳣��������
     * @param objects ���¼�Я���Ĳ�����Ϣ
     * @return ���ظ��¼��ڶ����е�λ�á�
     */
    public Sequence pushEvent(Event eventType, EventExceptionHandler<Event> handler, Object... objects);
    /**�������������¼���{@link EventExceptionHandler}���Ͳ���ָ���������ִ���¼��ڼ䷢���쳣���쳣��������
     * ����ò���ָ��Ϊ����ʹ��Ĭ�ϵ��쳣��������*/
    public void popEvent(EventExceptionHandler<Event> handler);
    /**����ĳ���ض����͵��¼���{@link EventExceptionHandler}���Ͳ���ָ���������ִ���¼��ڼ䷢���쳣���쳣��������
     * ����ò���ָ��Ϊ����ʹ��Ĭ�ϵ��쳣��������*/
    public void popEvent(Event eventType, EventExceptionHandler<Event> handler);
    /**�����ض�˳��λ�õ��¼���{@link EventExceptionHandler}���Ͳ���ָ���������ִ���¼��ڼ䷢���쳣���쳣��������
     * ����ò���ָ��Ϊ����ʹ��Ĭ�ϵ��쳣��������*/
    public void popEvent(Sequence sequence, EventExceptionHandler<Event> handler);
    /**�ƹ��¼�����ֱ��֪ͨ�¼���������������¼���{@link EventExceptionHandler}����
     * ����ָ���������ִ���¼��ڼ䷢���쳣���쳣�����������ò���ָ��Ϊ����ʹ��Ĭ�ϵ��쳣��������*/
    public void doEvent(Event eventType, EventExceptionHandler<Event> handler, Object... objects);
}