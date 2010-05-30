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
/**
 * �������Ա�����ʱ���������Ҫ��ȡ���õ���ֵ����ͨ��getNewValue��������ȡ��
 * ������滻��Ҫ���õ���ֵ�����ͨ��setNewValue�������滻��
 * Date : 2010-5-21
 * @author ������
 */
public class SetValueEvent extends Event {
    /**  */
    private static final long serialVersionUID = 5010075302608463391L;
    private Object            newValue;
    public SetValueEvent(Object target, Object newValue) {
        super("SetValueEvent", target);
        this.newValue = newValue;
    }
    @Override
    protected EventPhase[] createEventPhase() {
        return null;
    };
    /**Ҫ��ȡ���õ���ֵ����ͨ��getNewValue��������ȡ��*/
    public Object getNewValue() {
        return newValue;
    };
    /**�滻��Ҫ���õ���ֵ�����ͨ��setNewValue�������滻��*/
    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    };
};