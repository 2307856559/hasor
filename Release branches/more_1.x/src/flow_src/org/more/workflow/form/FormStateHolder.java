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
import org.more.CastException;
import org.more.workflow.context.ELContext;
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
    public Object newInstance(RunContext runContext) throws Throwable {
        FormFactory factory = runContext.getApplication().getFormFactory();
        FormBean obj = factory.getFormBean(runContext, this.formMetadata);
        String beanID = factory.generateID(runContext, obj);
        //
        obj = new Form(beanID, obj, this);
        NewInstanceEvent event = new NewInstanceEvent(obj, this);
        this.event(event.getEventPhase()[0]);
        return obj;
    };
    /**����Bean�����ԣ��÷��������θ���propertyMap�ж�Ӧ�����ԡ� */
    @Override
    public void updataMode(Object mode, ELContext elContext) throws Throwable {
        if (mode instanceof FormBean == false)
            throw new CastException("�޷����·�FormBean���͵�ģ��");
        //------------
        FormBean bean = (FormBean) mode;
        if (mode instanceof Form == true)
            bean = ((Form) mode).getFormBean();
        super.updataMode(bean, elContext);
    };
    /**���ݱ�IDװ�ر���*/
    public Form loadForm(RunContext runContext, String formID) {
        return null;
    };
    /**ˢ�±����󣬸÷���ϵͳ��ӳ־û���ֱ�����������������ݵ�formObject�С�*/
    public void refreshForm(RunContext runContext, Form formObject) {};
    /**�������Ϣ������ñ������Ѿ����ڳ־û�����÷������ᵼ�¸��²�����*/
    public void saveForm(RunContext runContext, Form formObject) {};
    /**���ݱ�IDɾ�������÷���ֻ��ɾ����ǰFormMetadata����ı�����*/
    public void deleteFrom(RunContext runContext, String formID) {}
};