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
package org.more.workflow.event.object;
import org.more.workflow.event.Event;
import org.more.workflow.event.EventPhase;
import org.more.workflow.metadata.PropertyMetadata;
/**
 * ��ģ�����Ա�����ʱ�����¼������Ϊ�����׶�һ����before��һ����after��
 * Date : 2010-5-21
 * @author ������
 */
public class UpdataPropertyEvnet extends Event {
    /**  */
    private static final long serialVersionUID = 5010075302608463391L;
    private PropertyMetadata  propertyMetadata = null;
    private String            propertyEL       = null;
    private Object            oldValue         = null;
    private Object            newValue         = null;
    /**��ģ�����Ա�����ʱ��*/
    public UpdataPropertyEvnet(Object targetMode, String propertyEL, Object oldValue, Object newValue, PropertyMetadata propertyMetadata) {
        super("UpdataPropertyEvnet", targetMode);
        this.propertyEL = propertyEL;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.propertyMetadata = propertyMetadata;
    };
    @Override
    protected EventPhase[] createEventPhase() {
        return new EventPhase[] { new Event.BeforeEventPhase(), new Event.AfterEventPhase() };
    };
    /**��ȡ����Ԫ��Ϣ����*/
    public PropertyMetadata getPropertyMetadata() {
        return propertyMetadata;
    };
    /**��ȡ���Ե�EL����*/
    public String getPropertyEL() {
        return this.propertyEL;
    };
    /**��ȡԭ������ֵ*/
    public Object getOldValue() {
        return this.oldValue;
    };
    /**��ȡҪ���µ�����ֵ*/
    public Object getNewValue() {
        return this.newValue;
    };
    /**������ֵ������ͨ���÷������ı����ո��µ�������ֵ��*/
    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    };
};