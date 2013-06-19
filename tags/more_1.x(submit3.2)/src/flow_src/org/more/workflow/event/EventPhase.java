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
import org.more.util.attribute.IAttribute;
/**
 * ��ʾһ���¼��������׶Σ��������ͨ������׶ζ����ڲ����¼���������������ݹ���
 * Date : 2010-5-24
 * @author ������
 */
public abstract class EventPhase {
    //========================================================================================Field
    private final Class<? extends PhaseMark> markType; //�׶α��
    private Event                            event;
    //==================================================================================Constructor
    protected EventPhase(Class<? extends PhaseMark> markType) {
        this.markType = markType;
    };
    //==========================================================================================Job
    /**��ȡ�¼�����׶ε����͡�*/
    public Class<? extends PhaseMark> getEventPhaseType() {
        return this.markType;
    };
    void setEvent(Event event) {
        this.event = event;
    };
    /**��ȡ�ý׶��������¼���*/
    public Event getEvent() {
        return event;
    };
    /**��ȡ�ý׶���ʹ�õ�IAttribute����*/
    public IAttribute getAttribute() {
        return this.event;
    };
};