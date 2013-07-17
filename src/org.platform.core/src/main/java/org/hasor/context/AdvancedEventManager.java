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
package org.hasor.context;
import org.more.RepeateException;
/**
 * �߼��¼�������
 * @version : 2013-5-6
 * @author ������ (zyc@byshell.org)
 */
public interface AdvancedEventManager extends EventManager {
    /**�׳��׶����¼��������¼��׳�֮��ֻ�еȴ�Hasor��ִ����Ӧ�׶�ʱ�Żᴦ���Ӧ���¼���<br/>
     * �÷�����{@link AdvancedEventManager#addEventListener(String, HasorEventListener)}������ͬ��
     * pushPhaseEvent����ע���ʱ����������յ�һ�ν׶����¼�֮��ᱻ�Զ�ɾ����*/
    public void pushEventListener(String eventType, HasorEventListener hasorEventListener);
    /**���һ����ʱ���������ӵļ�ʱ�������Ѿ���������׳��쳣��*/
    public void addTimer(String timerType, HasorEventListener hasorEventListener) throws RepeateException;
    /**�Ƴ����ܻ��Ѿ����ڵļ�ʱ������,����ʱ������ִ��ʱ�Ὣtimer����ִ�����.*/
    public void removeTimer(String timerType);
    /**�Ƴ����ܻ��Ѿ����ڵļ�ʱ������,����ʱ������ִ��ʱ�᳢��ȡ��timer����ִ�е�����.*/
    public void removeTimerNow(String timerType);
}