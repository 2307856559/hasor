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
package org.more.hypha.event;
import org.more.hypha.Event;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.DefineResource;
/**
 * ��ʼ��ʼ�������¼������¼���{@link DefineResource}���յ�һ����{@link AbstractBeanDefine}�������ʱ������
 * @version 2010-10-10
 * @author ������ (zyc@byshell.org)
 */
public class AddBeanDefineEvent extends Event {
    private AbstractBeanDefine define = null;
    /**����{@link AddBeanDefineEvent}����*/
    public AddBeanDefineEvent(Object target, AbstractBeanDefine define) {
        super(target);
    }
    /**��ȡ��ӵ���{@link AbstractBeanDefine}���塣*/
    public AbstractBeanDefine getDefine() {
        return define;
    }
}