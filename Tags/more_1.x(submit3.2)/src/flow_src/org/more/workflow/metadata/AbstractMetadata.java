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
package org.more.workflow.metadata;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.more.workflow.event.EventListener;
import org.more.workflow.event.ListenerHolder;
/**
 * ����������Ԫ��Ϣ���͵Ļ��࣬�κ�Ԫ��Ϣ�඼��Ҫ�̳и��ࡣAbstractMetadata�������������ࡣ
 * {@link ObjectMetadata}������Ϊ������ģ�͵�Ԫ��Ϣ����{@link PropertyMetadata}��Ϊ������ģ�������Ե�Ԫ��Ϣ��
 * Date : 2010-6-15
 * @author ������
 */
public abstract class AbstractMetadata implements ListenerHolder {
    private String                    metadataID = null;                          //Ԫ��Ϣ����ID
    private final List<EventListener> listeners  = new ArrayList<EventListener>(); //������¼�����������
    /**����һ��Ԫ��Ϣ���󣬲���������Ԫ��Ϣ��ID�����id����ͨ��getMetadataID������ȡ��*/
    public AbstractMetadata(String metadataID) {
        if (metadataID == null)
            throw new NullPointerException("��ָ��metadataID����ֵ��");
        this.metadataID = metadataID;
    };
    /**��ȡԪ��Ϣ����ID����id���ڴ���AbstractMetadata����ʱָ���ģ����Ҳ����޸ġ�*/
    public String getMetadataID() {
        return this.metadataID;
    };
    @Override
    public Iterator<EventListener> getListeners() {
        return this.listeners.iterator();
    };
    /**����һ���¼���������*/
    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    };
    /**ɾ��һ���¼���������*/
    public void removeListener(EventListener listener) {
        if (this.listeners.contains(listener) == true)
            this.listeners.remove(listener);
    };
};