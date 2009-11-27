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
package org.more.beans.info;
/**
 * BeanProperty�����ڱ�ʾ���Ե�bean���塣
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class BeanProperty extends Prop {
    /**  */
    private static final long serialVersionUID = -3492072515778133870L;
    private String            name             = null;                 //�����������ڹ��췽���������ø�ֵ��Ч��
    private BeanProp          refValue         = null;                 //����ֵ��
    //=========================================================================
    /**��ȡ�����������ڹ��췽���������ø�ֵ��Ч��*/
    public String getName() {
        return name;
    }
    /**���������������ڹ��췽���������ø�ֵ��Ч��*/
    public void setName(String name) {
        this.name = name;
    }
    /**��ȡ����ֵ��*/
    public BeanProp getRefValue() {
        return refValue;
    }
    /**��������ֵ��*/
    public void setRefValue(BeanProp refValue) {
        this.refValue = refValue;
    }
}