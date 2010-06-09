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
import org.more.workflow.state.AbstractStateHolder;
/**
 * ������FormBean�ӿڵ�һ������ʵ�֡�
 * Date : 2010-5-21
 * @author ������
 */
class Form extends AbstractObject implements FormBean {
    //========================================================================================Field
    private FormBean formBean = null;
    //==================================================================================Constructor
    public Form(String objectID, FormBean formBean, AbstractStateHolder objectStateHolder) {
        super(objectID, objectStateHolder);
        this.formBean = formBean;
    };
    //==========================================================================================Job
    /** ��ȡForm��������������Ǹ������ʵ���ࡣ*/
    public FormBean getFormBean() {
        return this.formBean;
    };
};