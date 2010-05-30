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
package org.more.workflow.state;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.workflow.context.ELContext;
import org.more.workflow.context.RunContext;
import org.more.workflow.event.AbstractHolderListener;
import org.more.workflow.event.object.LoadStateEvent;
import org.more.workflow.event.object.SaveStateEvent;
import org.more.workflow.metadata.AbstractMetadata;
import org.more.workflow.metadata.MetadataHolder;
import org.more.workflow.metadata.ModeUpdataHolder;
/**
 * �����״̬�����ӿڣ������������չ���ֲ�������ģ���ṩ{@link ModeUpdataHolder}�ӿ�ʵ�֣��ö����ʵ��
 * ��ͨ����ȡ��{@link AbstractMetadata}����Ȼ���ڸö����ϵ�����updataMode������ʵ�֡��������⻹�ṩ��
 * {@link MetadataHolder} �ӿڵ�ʵ�֡�
 * Date : 2010-5-16
 * @author ������
 */
public abstract class AbstractStateHolder extends AbstractHolderListener implements IAttribute, ModeUpdataHolder, MetadataHolder {
    //========================================================================================Field
    private AbstractMetadata metadataObject;              //Ԫ��Ϣ����
    private final AttBase    attributeMap = new AttBase(); //���ڱ����¼������Զ���
    //==================================================================================Constructor
    /***/
    protected AbstractStateHolder(AbstractMetadata metadataObject) {
        this.metadataObject = metadataObject;
    };
    //==========================================================================================Job
    @Override
    public AbstractMetadata getMetadata() {
        return this.metadataObject;
    };
    @Override
    public void updataMode(Object mode, ELContext elContext) throws Throwable {
        this.metadataObject.updataMode(mode, elContext);
    };
    /**����ʱ�־û������У�װ��ģ��״̬����������д�÷���ʱ��һ��Ҫ���ø������Ӧ������AbstractStateHolder�ฺ���¼���������*/
    public void loadState(Object mode, RunContext runContext) {
        LoadStateEvent event = new LoadStateEvent(mode, runContext.getApplication().getFlashSession());
        this.event(event.getEventPhase()[0]);
    };
    /**����ʱ�־û������У�����ģ��״̬����������д�÷���ʱ��һ��Ҫ���ø������Ӧ������AbstractStateHolder�ฺ���¼���������*/
    public void saveState(Object mode, RunContext runContext) {
        SaveStateEvent event = new SaveStateEvent(mode, runContext.getApplication().getFlashSession());
        this.event(event.getEventPhase()[0]);
    };
    //==========================================================================================Job
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