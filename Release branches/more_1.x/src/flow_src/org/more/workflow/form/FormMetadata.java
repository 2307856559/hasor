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
import java.util.HashMap;
import java.util.Map;
import org.more.CastException;
import org.more.RepeateException;
import org.more.workflow.context.ELContext;
import org.more.workflow.el.PropertyBinding;
import org.more.workflow.event.EventType;
import org.more.workflow.event.object.NewInstanceEvent;
import org.more.workflow.metadata.AbstractMetadata;
import org.more.workflow.metadata.ModeUpdataHolder;
/**
 * �����Ǳ���һ�����̱���Ԫ��Ϣ����ͨ��������Դ���һ��{@link FormBean}���Ͷ���
 * Ҳ����ͨ�������������Ԫ��Ϣ������ĳ��{@link FormBean}ģ�͡�
 * ����֮��FormMetadata�����ṩ��ע���Ƴ�����Ԫ��Ϣ�ķ�����ͨ����Щ���������޸�FormMetadataԪ��Ϣ�����е����ԡ�
 * Date : 2010-5-16
 * @author ������
 */
public class FormMetadata extends AbstractMetadata {
    //========================================================================================Field
    private final Class<? extends FormBean>         formType;   //FormBean�ľ�������
    private final Map<String, FormPropertyMetadata> propertyMap; //�������ڸ���Beanģ��ʱʹ�õ�El���Լ���
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
        this.propertyMap = new HashMap<String, FormPropertyMetadata>();
    };
    //==========================================================================================Job
    /**����{@link FormBean}��ÿһ���´�����{@link FormBean}���󶼻�ִ��updataMode������*/
    @Override
    public FormBean newInstance(ELContext elContext) throws Throwable {
        FormBean obj = this.formType.newInstance();
        this.updataMode(obj, elContext);//����ģ�� 
        this.event(new NewInstanceEvent(EventType.NewInstanceEvent, obj));
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
        super.updataMode(mode, elContext);
        //------------
        for (FormPropertyMetadata item : this.propertyMap.values())
            item.updataMode(bean, elContext);
    };
    /**��ȡflowForm�ľ������͡�*/
    public Class<? extends FormBean> getFormType() {
        return this.formType;
    };
    /**
     * ���һ������Ԫ��Ϣ���÷�������ָ����ǰform��һ���������ƣ���ͬʱָ����һ�����ʽ��
     * ������{@link ModeUpdataHolder}�ӿڷ���ʱFormMetadata�����ע��������б��Ԥ����ģ��ִ�и��²�����
     * ע�ⲻ���ظ�ע��ͬһ�����ԣ����������{@link RepeateException}�쳣��
     * @param propertyName Ҫ��ӵ���������
     * @param expressionString �����Զ�Ӧ��{@link PropertyBinding ���ʽ}��
     */
    public void addProperty(String propertyName, String expressionString) {
        if (this.propertyMap.containsKey(propertyName) == true)
            throw new RepeateException("����ע���ظ�������Ԫ��Ϣ�� ");
        this.addProperty(new FormPropertyMetadata(propertyName, expressionString));
    };
    /**
     * ���һ������Ԫ��Ϣ���÷�������ָ����ǰform��һ���������ƣ���ͬʱָ����һ�����ʽ��������
     * {@link ModeUpdataHolder}�ӿڷ���ʱFormMetadata�����ע��������б��Ԥ����ģ��ִ�и��²�����
     * ע�ⲻ���ظ�ע��ͬһ�����ԣ����������{@link RepeateException}�쳣��
     * @param propertyItem Ҫ��ӵ����Զ�������Զ�Ӧ��{@link PropertyBinding ���ʽ}��
     */
    public void addProperty(FormPropertyMetadata propertyItem) {
        if (this.propertyMap.containsKey(propertyItem.getMetadataID()) == true)
            throw new RepeateException("����ע���ظ�������Ԫ��Ϣ�� ");
        this.propertyMap.put(propertyItem.getMetadataID(), propertyItem);
    };
    /**ȡ��һ������Ԫ��Ϣ����ӣ�����ֻ��Ҫ�������������ɣ�����FormItemMetadata����������������metadataID��*/
    public void removeProperty(String propertyName) {
        if (this.propertyMap.containsKey(propertyName) == true)
            this.propertyMap.remove(propertyName);
    };
};