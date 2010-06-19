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
package org.more.workflow.form;
import org.more.workflow.context.FormFactory;
import org.more.workflow.context.RunContext;
import org.more.workflow.event.object.NewInstanceEvent;
import org.more.workflow.metadata.ObjectMetadata;
import org.more.workflow.state.AbstractStateHolder;
/**
 * ��״̬��������ͨ���ö�����Է���ı��������������໹�ṩ��ɾ�����Ĳ�����
 * Date : 2010-6-12
 * @author ������
 */
public class FormStateHolder extends AbstractStateHolder {
    private FormMetadata formMetadata;
    /**
     *  ����FormStateHolder����������췽����Ҫһ������Ԫ��Ϣ������Ϊ������
     * @param metadataObject ����Ԫ��Ϣ����
     */
    public FormStateHolder(FormMetadata formMetadata) {
        this.formMetadata = formMetadata;
    };
    @Override
    public ObjectMetadata getMetadata() {
        return this.formMetadata;
    };
    /**
     * ����{@link FormBean}��ע��÷���ֻ�ᴴ��һ��FormBean���Ͷ��������ȥ�������Bean�����ԡ�
     * �����Ҫ������Ը�����ִ��updataMode������
     */
    @Override
    public FormBean newInstance(RunContext runContext) throws Throwable {
        FormFactory factory = runContext.getApplication().getFormFactory();
        FormBean obj = factory.createForm(this.formMetadata);
        String beanID = factory.generateID(obj);
        //
        obj = new Form(beanID, obj, this);
        NewInstanceEvent event = new NewInstanceEvent(obj, this);
        this.event(event.getEventPhase()[0]);
        return obj;
    };
    /**���ݱ�ID�ӳ־û�ϵͳ��װ�ر���*/
    public FormBean loadForm(String formID, RunContext runContext) {
        FormFactory factory = runContext.getApplication().getFormFactory();
        FormBean obj = factory.getForm(formID, this.formMetadata);
        //
        obj = new Form(formID, obj, this);
        NewInstanceEvent event = new NewInstanceEvent(obj, this);
        this.event(event.getEventPhase()[0]);
        return obj;
    };
    /**�������Ϣ������ñ������Ѿ����ڳ־û�����÷������ᵼ�¸��²�����*/
    public void saveForm(Form formObject, RunContext runContext) {
        FormBean bean = formObject.getTargetBean();
        String formID = formObject.getID();
        runContext.getApplication().getFormFactory().saveForm(formID, bean);
    };
    /**���ݱ�IDɾ�������÷���ֻ��ɾ����ǰFormMetadata����ı�����*/
    public void deleteFrom(Form formObject, RunContext runContext) {
        String formID = formObject.getID();
        runContext.getApplication().getFormFactory().deleteForm(formID);
    };
};