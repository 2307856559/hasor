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
import org.more.util.attribute.IAttribute;
/**
 * �¼��������ӿڣ��ýӿڵ�Ŀ����Ϊ�˴���{@link Event}�����¼����ýӿ��ṩ����һ���Ƚ��ȳ��Ķ��еķ�ʽ�����¼���
 * �¼������ǵ�ѹ�����ʱ�͵õ�������Ҫ���Եĵ��õ����¼������¼�����Ҳ����ͨ��doEvent������ȷ��������¼���
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public interface EventManager {
    /**�����ǣ���־�¼���ѹ���¼�������֮���˳��λ�á�*/
    public static abstract class Sequence {
        /**�����¼�����������λ�á�*/
        public abstract int getIndex();
        /**��ȡ�¼������͡�*/
        public abstract Event getEventType();
        /**��ȡ�¼��Ĳ�����*/
        public abstract Object[] getParams();
    };
    /**�����¼��в����ĳ����ࡣ*/
    public static abstract class Params {};
    /**��ʼ��������������һ��flash��*/
    public void init(IAttribute flash) throws Throwable;
    /**���һ�������¼����¼���������*/
    public void addEventListener(Event eventType, EventListener<?> listener);
    /**
     * ���¼�ѹ�뵽�����еȴ�ִ�С�
     * @param eventType Ҫѹ����¼����͡�
     * @param objects ���¼�Я���Ĳ�����Ϣ
     * @return ���ظ��¼��ڶ����е�λ�á�
     */
    public Sequence pushEvent(Event eventType, Object... objects);
    /**��������ڶ����еȴ�������¼���*/
    public void clearEvent();
    /**��ն�����ָ�����͵��¼���*/
    public void clearEvent(Event eventType);
    /**ɾ�������е�ĳһ����ִ�е��¼���*/
    public boolean removeEvent(Sequence sequence);
    /**�������������¼����ڵ������������μ���ÿ���¼����¼������������һЩ�¼�û����Ӧ���¼���������ô��Щ�µĴ��������ԡ�*/
    public void popEvent();
    /**����ĳ���ض����͵��¼����ڵ������������μ���ÿ���¼����¼��������������Щ�¼�û����Ӧ���¼���������ô��Щ�µĴ��������ԡ�*/
    public void popEvent(Event eventType);
    /**�����ض�˳��λ�õ��¼����ڵ������������μ���ÿ���¼����¼��������������Щ�¼�û����Ӧ���¼���������ô��Щ�µĴ��������ԡ�*/
    public void popEvent(Sequence sequence);
    /**�ƹ��¼�����ֱ��֪ͨ�¼���������������¼���*/
    public void doEvent(Event eventType, Object... objects);
}