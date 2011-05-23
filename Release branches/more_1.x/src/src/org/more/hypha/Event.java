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
import java.util.HashMap;
import org.more.InitializationException;
import org.more.log.ILog;
import org.more.log.LogFactory;
/**
 * �¼���һ��֪ͨ���ƣ�ʹ���¼����ܿ����������̵�ִ�С�����ȴ����ͨ���¼���֪�ڲ��Ĺ���״̬��
 * �ýӿڱ�ʾ����һ��{@link EventManager}���Ա�ʶ������¼���
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public abstract class Event {
    protected static ILog log = LogFactory.getLog(Event.class);
    /**�����ǣ���־�¼���ѹ���¼�������֮���˳��λ�á�*/
    public static abstract class Sequence {
        /**�����¼�����������λ�á�*/
        public abstract int getIndex();
        /**��ȡ�¼������͡�*/
        public abstract Event getEventType();
        /**��ȡ�¼��Ĳ�����*/
        public abstract Object[] getParams();
        /**��ȡ���¼�ʹ�õ��쳣��������*/
        public abstract EventExceptionHandler<Event> getHandler();
    };
    /**�����¼��в����ĳ����ࡣ*/
    public static abstract class Params {};
    //----------------------------------------
    private static HashMap<Class<? extends Event>, Event> eventMap = new HashMap<Class<? extends Event>, Event>();
    /**��ȡָ�������¼������������Ϊ����ֱ�ӷ��ؿ�ֵ���¼�������hypha����ȫ��Ψһ�ģ���������Ŀ����Ϊ�˼���new��������*/
    public static Event getEvent(Class<? extends Event> eventType) throws InitializationException {
        if (eventType == null) {
            log.warning("getEvent an error , eventType is null.", eventType);
            return null;
        }
        Event event = null;
        if (eventMap.containsKey(eventType) == false)
            //��������ע������¼�.
            try {
                log.debug("not found {%0} Event Object.", eventType);
                Event eventObj = eventType.newInstance();
                eventMap.put(eventType, eventObj);
                log.debug("created Event object {%0} and regeidt it.", eventObj);
            } catch (Exception e) {
                log.warning("create Event {%0} error :", eventType, e);
            }
        event = eventMap.get(eventType);
        //����
        log.debug("return {%0} Event Object.", event);
        return event;
    }
    /**���¼�����ת��Ϊ{@link Params}���Ͷ���*/
    public abstract Params toParams(Sequence eventSequence);
};