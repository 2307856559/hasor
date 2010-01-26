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
 * �����װbean�������й�List�������Ե���Ϣ���½��ļ��϶������û��ָ��������Ĭ����:java.util.List����
 * propType���Թ���
 * 1.���PropList������propType������ֱ�ӷ��أ����򷵻�propContext��propType���ԡ�
 * 2.���propContextҲû������propType�����򷵻�java.util.List��
 * @version 2009-11-18
 * @author ������ (zyc@byshell.org)
 */
public class PropList extends BeanProp {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -194590250590692070L;
    private BeanProp[]        listElements     = null;                //����Ԫ�ء�
    //==================================================================================Constructor
    /**����һ��java.util.ArrayList���͵Ŀռ��ϡ�*/
    public PropList() {
        this.listElements = new BeanProp[0];
        this.setPropType("java.util.List");
    }
    /**����һ��java.util.ArrayList���͵ļ��ϣ�����Ԫ���ɲ���elements������*/
    public PropList(BeanProp[] elements) {
        if (elements == null)
            this.listElements = new BeanProp[0];
        else
            this.listElements = elements;
        this.setPropType("java.util.List");
    }
    /**����һ��PropList���϶�����󣬼���Ԫ���ɲ���elements����������������listType������*/
    public PropList(BeanProp[] elements, String arrayType) {
        if (elements == null)
            this.listElements = new BeanProp[0];
        else
            this.listElements = elements;
        this.setPropType(arrayType);
    }
    //==========================================================================================Job
    /**��ȡ����Ԫ�ء�*/
    public BeanProp[] getListElements() {
        return listElements;
    }
    /**���ü���Ԫ�ء�*/
    public void setListElements(BeanProp[] listElements) {
        this.listElements = listElements;
    }
}