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
 * �¼��������ڱ��һ���ض�ʱ�䷢�������飬������¼�ʱ�����Ϊ����ǰ�������У������
 * Date : 2010-5-16
 * @author ������
 */
public abstract class Event implements Serializable, IAttribute {
    //========================================================================================Field
    private static final long serialVersionUID = 8851774714640209392L;
    /**  */
    private final String      eventID;                                //�¼�ID
    private String            eventMessage;                           //�¼���Ϣ
    private final Object      target;                                 //�¼�Դ����
    private final Date        atTime           = new Date();          //����ʱ��
    private final AttBase     attMap           = new AttBase();       //���ڱ����¼������Զ���
    //==================================================================================Constructor
    /**
     * ����Event����
     * @param eventID �¼�ID
     * @param target �¼�Դ����
     */
    public Event(String eventID, Object target) {
        this.eventID = eventID;
        this.target = target;
        this.eventMessage = null;
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
    };
    //==========================================================================================Job
    /** ��ȡ�¼�ID�� */
    public String getEventID() {
        return eventID;
    };
    /** ��ȡ�¼���������Ϣ�� */
    public String getEventMessage() {
        return eventMessage;
    };
    /** ��ȡ�¼������Ķ��� */
    public Object getTarget() {
        return target;
    };
    /** ��ȡ�¼�����ʱ�䡣 */
    public Date getAtTime() {
        return (Date) atTime.clone();
    }
    //==========================================================================================Job
    @Override
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