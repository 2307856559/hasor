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
package org.hasor.context;
/**
 * �ṩ�¼���������ע���ͬ���¼��첽�¼��Ĵ���������
 * @version : 2013-5-6
 * @author ������ (zyc@hasor.net)
 */
public interface EventManager {
    /**pushPhaseEvent����ע���ʱ����������յ�һ���¼�֮��ᱻ�Զ�ɾ����*/
    public void pushEventListener(String eventType, HasorEventListener hasorEventListener);
    /**���һ�������¼����¼���������*/
    public void addEventListener(String eventType, HasorEventListener hasorEventListener);
    /**ɾ��ĳ����������ע�ᡣ*/
    public void removeEventListener(String eventType, HasorEventListener hasorEventListener);
    //
    /**ͬ����ʽ�׳��¼�������������ʱ�Ѿ�ȫ����������¼��ַ���<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ�÷������̵��쳣�������ַ��¼������̵����쳣����һ������ķ�ʽ���֡�*/
    public void doSyncEventIgnoreThrow(String eventType, Object... objects);
    /**ͬ����ʽ�׳��¼�������������ʱ�Ѿ�ȫ����������¼��ַ���<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ���ж��¼��ַ��׳��������쳣��*/
    public void doSyncEvent(String eventType, Object... objects) throws Throwable;
    /**�첽��ʽ�׳��¼���asynEvent�����ĵ��ò��������ʱ��ʼִ���¼�������һ�����¼�������������<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ�÷������̵��쳣�������ַ��¼������̵����쳣����һ������ķ�ʽ���֡�*/
    public void doAsynEventIgnoreThrow(String eventType, Object... objects);
    /**�첽��ʽ�׳��¼���asynEvent�����ĵ��ò��������ʱ��ʼִ���¼�������һ�����¼�������������<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ���ж��¼��ַ�����������ִ��Ȩ�����쳣����ӿڡ�*/
    public void doAsynEvent(String eventType, AsyncCallBackHook callBack, Object... objects);
    //
    /**���δ��ɵ��¼��ȴ�ִ�ж���*/
    public void clean();
}