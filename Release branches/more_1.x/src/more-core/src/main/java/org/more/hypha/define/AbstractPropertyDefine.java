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
package org.more.hypha.define;
/**
 * �ýӿ����ڶ���һ��bean�����е�һ�����Ի������Ϣ��
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractPropertyDefine extends AbstractDefine<AbstractPropertyDefine> {
    private String        classType     = null; //��ִ������ע��ʱ��Ҫִ�е�����ת������
    private String        description   = null; //��������
    private ValueMetaData valueMetaData = null; //ֵ����
    //-------------------------------------------------------------
    /**���ص�ִ������ע��ʱ��Ҫִ�е�����ת�����͡�*/
    public String getClassType() {
        return this.classType;
    };
    /**�������Ե�������Ϣ��*/
    public String getDescription() {
        return this.description;
    };
    /**��ȡ�Ը����Ե�ֵ��Ϣ������*/
    public ValueMetaData getMetaData() {
        return this.valueMetaData;
    };
    //-------------------------------------------------------------
    /**��������ֵ��������Ϣ*/
    public void setValueMetaData(ValueMetaData valueMetaData) {
        valueMetaData.setFor(this);
        this.valueMetaData = valueMetaData;
    }
    /**������������*/
    public void setClassType(String classType) {
        this.classType = classType;
    }
    /**������������*/
    public void setDescription(String description) {
        this.description = description;
    }
}