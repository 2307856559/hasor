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
 * �����װbean�������й�Set�������Ե���Ϣ���½��ļ��϶������û��ָ��������Ĭ����:java.util.Set���͡�
 * propType���Թ���
 * 1.���PropSet������propType������ֱ�ӷ��أ����򷵻�propContext��propType���ԡ�
 * 2.���propContextҲû������propType�����򷵻�java.util.Set��
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class PropSet extends BeanProp {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -194590250590692070L;
    private BeanProp[]        setElements      = null;                //����Ԫ�ء�
    //==================================================================================Constructor
    /**����һ��PropSet���϶������*/
    public PropSet() {
        this.setElements = new BeanProp[0];
        this.setPropType("java.util.Set");
    }
    /**����һ��PropSet���϶�����󣬼���Ԫ���ɲ���elements������*/
    public PropSet(BeanProp[] elements) {
        if (elements == null)
            this.setElements = new BeanProp[0];
        else
            this.setElements = elements;
        this.setPropType("java.util.Set");
    }
    /**����һ��PropSet���϶�����󣬼���Ԫ���ɲ���elements����������������setType������*/
    public PropSet(BeanProp[] elements, String setType) {
        if (elements == null)
            this.setElements = new BeanProp[0];
        else
            this.setElements = elements;
        this.setPropType(setType);
    }
    //==========================================================================================Job
    /**��ȡ����Ԫ�ء�*/
    public BeanProp[] getSetElements() {
        return setElements;
    }
    /**���ü���Ԫ�ء�*/
    public void setSetElements(BeanProp[] setElements) {
        this.setElements = setElements;
    }
}