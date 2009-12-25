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
 * ��Ҫ����ʵ�ֵĽӿڼ������bean�������á�
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class BeanInterface extends Prop {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -1660901774837550451L;
    private String            refType          = null;                 //�ӿ���������Bean����������typeRefBean��������Բ�����type���ԡ�
    private String            delegateRefBean  = null;                 //���ӽӿ�ʵ��ʱ�ӿڴ���ί��bean����
    //==========================================================================================Job
    /**��ȡ�ӿ���������Bean����������typeRefBean��������Բ�����type���ԡ�*/
    public String getRefType() {
        return refType;
    }
    /**���ýӿ���������Bean����������typeRefBean��������Բ�����type���ԡ�*/
    public void setRefType(String refType) {
        this.refType = refType;
    }
    /**��ȡ���ӽӿ�ʵ��ʱ�ӿڴ���ί��bean����*/
    public String getDelegateRefBean() {
        return delegateRefBean;
    }
    /**���ø��ӽӿ�ʵ��ʱ�ӿڴ���ί��bean����*/
    public void setDelegateRefBean(String delegateRefBean) {
        this.delegateRefBean = delegateRefBean;
    }
}