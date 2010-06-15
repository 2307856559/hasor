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
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.workflow.event.AbstractHolderListener;
import org.more.workflow.state.AbstractStateHolder;
import org.more.workflow.state.StateCache;
import org.more.workflow.state.StateHolder;
/**
 * ����������ͣ������Ͷ���������ϵͳ�ĳ�����࣬��Ϊģ�Ͷ���Ļ��������ṩ��{@link StateHolder}�ӿ�
 * ��{@link MetadataHolder}�ӿڵ�ʵ�֡�������໹�ṩ��{@link IAttribute}�ӿڵ�ʵ�֡�ĳЩ����Ϊ�˼���POJO��
 * ����һ�������࣬������POJO��ʵ��һ�������ӿڡ�
 * Date : 2010-5-16
 * @author ������
 */
public abstract class AbstractObject extends AbstractHolderListener implements IAttribute, StateHolder, MetadataHolder, StateCache {
    private String              objectID     = null;         //����ID
    private AbstractStateHolder stateHolder  = null;         //״̬�����ӿ�
    private final AttBase       attributeMap = new AttBase(); //���ڱ����������Լ��ϡ�
    /**
    * ����AbstractObject����
    * @param objectID ����ID��
    * @param objectStateHolder ״̬�����ӿڡ�
    */
    protected AbstractObject(String objectID, AbstractStateHolder stateHolder) {
        if (objectID == null || stateHolder == null)
            throw new NullPointerException("����objectID���߲���objectStateHolderΪnull��");
        this.objectID = objectID;
        this.stateHolder = stateHolder;
    };
    /**��ȡ����ID��*/
    public String getID() {
        return this.objectID;
    };
    public AbstractStateHolder getStateHolder() {
        return this.stateHolder;
    };
    @Override
    public ObjectMetadata getMetadata() {
        return this.stateHolder.getMetadata();
    };
    @Override
    public void clearAttribute() {
        this.attributeMap.clearAttribute();
    };
    @Override
    public boolean contains(String name) {
        return this.attributeMap.contains(name);
    };
    @Override
    public Object getAttribute(String name) {
        return this.attributeMap.getAttribute(name);
    };
    @Override
    public String[] getAttributeNames() {
        return this.attributeMap.getAttributeNames();
    };
    @Override
    public void removeAttribute(String name) {
        this.attributeMap.removeAttribute(name);
    };
    @Override
    public void setAttribute(String name, Object value) {
        this.attributeMap.setAttribute(name, value);
    };
};