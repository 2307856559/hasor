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
 * ��ʾBeanProperty�������õ�ֵ��һ��������������(��more.beans�г��˰˸�java��������֮�⻹�������ַ�������)��
 * <pre>
 * propType���Թ���
 * 1.���û������propType������ʹ��value������BeanProperty��������Ϊ���á�
 * </pre>
 * @version 2009-11-18
 * @author ������ (zyc@byshell.org)
 */
public class PropVarValue extends BeanProp {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -194590250590692070L;
    private String            value            = null;                //����ֵ
    //==================================================================================Constructor
    /**����һ����ʾ�������ݵ�bean�����������ݣ�Ĭ��ֵ��null��Ĭ������������String��*/
    public PropVarValue() {}
    /**����һ����ʾ�������ݵ�bean�����������ݡ�*/
    public PropVarValue(String value) {
        this.value = value;
    }
    /**����һ����ʾ�������ݵ�bean�����������ݡ�*/
    public PropVarValue(String value, String valueType) {
        this.value = value;
        this.setPropType(valueType);
    }
    //==========================================================================================Job
    /**��ȡ����ֵ��*/
    public String getValue() {
        return value;
    }
    /**��������ֵ��*/
    public void setValue(String value) {
        this.value = value;
    }
}