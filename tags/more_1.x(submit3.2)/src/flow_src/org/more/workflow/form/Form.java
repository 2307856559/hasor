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
import org.more.workflow.metadata.AbstractObject;
/**
 * ������{@link FormBean}�ӿڵ�һ��ʵ�֣���Ҫ���ڴ���{@link FormBean}����
 * �����ṩԪ��Ϣ��{@link FormStateHolder}�İ󶨡�
 * Date : 2010-5-21
 * @author ������
 */
public class Form extends AbstractObject implements FormBean {
    //========================================================================================Field
    private FormBean formBean = null;
    //==================================================================================Constructor
    protected Form(String objectID, FormBean formBean, FormStateHolder objectStateHolder) {
        super(objectID, objectStateHolder);
        if (formBean == null)
            throw new NullPointerException("��������FormBean�����쳣�����ܴ���һ����FormBean���õĴ���");
        this.formBean = formBean;
    };
    //==========================================================================================Job
    /** ��ȡForm��������������Ǹ������ʵ���ࡣ*/
    public FormBean getTargetBean() {
        return this.formBean;
    }
};