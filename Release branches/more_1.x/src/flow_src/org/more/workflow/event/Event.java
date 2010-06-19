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
import java.io.Serializable;
import java.util.Date;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * �¼�����workflowϵͳ�е������¼�������ʵ�ָýӿڣ���������Ծ����¼���������Щ��
 * ��ʾ���¼����¼��׶ζ�����Я�����ԣ�����֮������Բ��ụ��Ӱ�졣
 * Date : 2010-5-24
 * @author ������
 */
public abstract class Event implements Serializable, IAttribute {
    //========================================================================================Field
    private static final long  serialVersionUID = 8851774714640209392L;
    /**  */
    private final String       eventID;                                //�¼�ID
    private String             eventMessage;                           //�¼���Ϣ
    private final Object       target;                                 //�¼�Դ����
    private final AttBase      attMap           = new AttBase();
    private final Date         atTime           = new Date();          //����ʱ��
    private final EventPhase[] eventPhase;
    //==================================================================================Constructor
    /**
     * ����Event����
     * @param eventID �¼�ID
     * @param target �¼�Դ����
     */
    public Event(String eventID, Object target) {
        this(eventID, target, null);
    };
    /**
     * ����Event����
     * @param eventID �¼�ID
     * @param target �¼�Դ����
     * @param eventMessage �¼���Ϣ
     */
    public Event(String eventID, Object target, String eventMessage) {
        this.eventID = eventID;
        this.target = target;
        this.eventMessage = eventMessage;
        EventPhase[] tempPhase = this.createEventPhase();
        if (tempPhase == null)
            this.eventPhase = new EventPhase[] { new ProcessEventPhase() };
        else
            this.eventPhase = tempPhase;
        for (EventPhase phase : this.eventPhase)
            phase.setEvent(this);
    };
    //=========================================================================================Type
    /**��ʼ���׶��¼���*/
    protected class InitEventPhase extends EventPhase implements InitPhase {
        public InitEventPhase() {
            super(InitPhase.class);
        };
    };
    /**�¼�����֮ǰ�׶ζ���*/
    protected class BeforeEventPhase extends EventPhase implements BeforePhase {
        public BeforeEventPhase() {
            super(BeforePhase.class);
        };
    };
    /**�¼�����ʱ�׶ζ���*/
    protected class ProcessEventPhase extends EventPhase implements ProcessPhase {
        public ProcessEventPhase() {
            super(ProcessPhase.class);
        };
    };
    /**�¼�����֮��׶ζ���*/
    protected class AfterEventPhase extends EventPhase implements AfterPhase {
        public AfterEventPhase() {
            super(AfterPhase.class);
        };
    };
    /**���ٽ׶Ρ�*/
    protected class DestroyEventPhase extends EventPhase implements DestroyPhase {
        public DestroyEventPhase() {
            super(DestroyPhase.class);
        };
    };
    //==========================================================================================Job
    /**
     * �̳�Event��������Ҫ����������Eventӵ����Щ�׶Ρ�
     * �������ʵ�ָ÷����ķ���ֵ��null��Eventʹ��Event.ProcessEventPhase������ΪĬ��ֵ
     */
    protected abstract EventPhase[] createEventPhase();
    /**��ȡ��ǰ�¼����߱����¼��׶Ρ�*/
    public EventPhase[] getEventPhase() {
        return this.eventPhase;
    };
    /** ��ȡ�¼�ID�� */
    public String getEventID() {
        return this.eventID;
    };
    /** ��ȡ�¼���������Ϣ�� */
    public String getEventMessage() {
        return this.eventMessage;
    };
    /** ��ȡ�¼������Ķ��� */
    public Object getTarget() {
        return this.target;
    };
    /** ��ȡ�¼�����ʱ�䡣 */
    public Date getAtTime() {
        return (Date) this.atTime.clone();
    };
    public void clearAttribute() {
        this.attMap.clearAttribute();
    };
    @Override
    public boolean contains(String name) {
        return this.attMap.contains(name);
    };
    @Override
    public Object getAttribute(String name) {
        return this.attMap.getAttribute(name);
    };
    @Override
    public String[] getAttributeNames() {
        return this.attMap.getAttributeNames();
    };
    @Override
    public void removeAttribute(String name) {
        this.attMap.removeAttribute(name);
    };
    @Override
    public void setAttribute(String name, Object value) {
        this.attMap.setAttribute(name, value);
    };
};