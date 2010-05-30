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
import org.more.workflow.context.RunContext;
import org.more.workflow.event.object.NewInstanceEvent;
import org.more.workflow.metadata.AbstractMetadata;
/**
 * �����Ǳ���һ�����̱���Ԫ��Ϣ����ͨ��������Դ���һ��{@link FormBean}���Ͷ���
 * Date : 2010-5-22
 * @author ������
 */
public class FormMetadata extends AbstractMetadata {
    //========================================================================================Field
    private final Class<? extends FormBean> formType; //FormBean�ľ�������
    //==================================================================================Constructor
    /**
     * ����FormMetadata���Ͷ��󣬲���metadataID������FormMetadata�����Ԫ��ϢID��formType�����˱������͡�
     * ���formType����Ϊ���������{@link NullPointerException}�쳣��
     * @param metadataID Ԫ��ϢID
     * @param formType ��Bean���ͣ�����ò���Ϊ���������{@link NullPointerException}�쳣��
     */
    public FormMetadata(String metadataID, Class<? extends FormBean> formType) {
        super(metadataID);
        if (formType == null)
            throw new NullPointerException("û��ָ����Ԫ��Ϣ��ָ������͡�");
        this.formType = formType;
    };
    //==========================================================================================Job
    /**
     * ����{@link FormBean}��ע��÷���ֻ�ᴴ��һ��FormBean���Ͷ��������ȥ�������Bean�����ԡ�
     * �����Ҫ������Ը�����ִ��updataMode������
     */
    @Override
    public FormBean newInstance(RunContext runContext) throws Throwable {
        NewInstanceEvent event = new NewInstanceEvent(this);
        FormBean obj = this.formType.newInstance();
        this.event(event.getEventPhase()[0]);
        return new Form(null, obj, new FormStateHolder(this));
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
    /**��ȡflowForm�ľ������͡�*/
    public Class<? extends FormBean> getFormType() {
        return this.formType;
    };
};