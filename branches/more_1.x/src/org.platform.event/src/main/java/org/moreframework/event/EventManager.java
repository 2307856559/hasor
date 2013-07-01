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
package org.moreframework.event;
/**
 * �¼����������ýӿڵ�Ŀ����Ϊ�˴����¼���
 * @version : 2013-5-6
 * @author ������ (zyc@byshell.org)
 */
public interface EventManager {
    /**EventManager����������*/
    public static final String OnStart   = "EventManager_OnStart";
    /**EventManager��������*/
    public static final String OnDestroy = "EventManager_OnDestroy";
    //
    //
    /**���һ�������¼����¼���������*/
    public void addEventListener(String eventType, Listener listener);
    /**ɾ��ĳ����������ע�ᡣ*/
    public void removeAllEventListener(String eventType);
    /**ɾ��ĳ����������ע�ᡣ*/
    public void removeEventListener(String eventType, Listener listener);
    /**��ȡĳ���ض����͵��¼����������ϡ�*/
    public Listener[] getEventListener(String eventType);
    /**��ȡ�����¼����������͡�*/
    public String[] getEventTypes();
    //
    /**ͬ����ʽ�׳��¼���*/
    public void throwEvent(String eventType, Object... objects);
    /**�첽��ʽ�׳��¼���*/
    public void asynThrowEvent(String eventType, Object... objects);
}