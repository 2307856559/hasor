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
 * �����װbean�������й��������Ե���Ϣ���½�������������û��ָ������������Ĭ����:java.lang.Object����
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class PropArray extends BeanProp {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -194590250590692070L; //
    private int               length           = 0;                   //���鳤�ȡ�
    private BeanProp[]        arrayElements    = null;                //����Ԫ�ض��塣
    //==================================================================================Constructor
    /**ʹ��Ĭ��java.lang.Object���ʹ���һ�������㳤�ȵĶ���*/
    public PropArray() {
        this.arrayElements = new BeanProp[0];
        this.length = 0;
        this.setPropType("java.lang.Object");
    }
    /**����һ��������󣬸ö����Ԫ�������ɲ���elements������ͬʱ����������java.lang.Object��*/
    public PropArray(BeanProp[] elements) {
        if (elements == null) {
            this.arrayElements = new BeanProp[0];
            this.length = 0;
        } else {
            this.arrayElements = elements;
            this.length = elements.length;
        }
        this.setPropType("java.lang.Object");
    }
    /**����һ��������󣬸ö����Ԫ�������ɲ���elements������ͬʱ���������ɲ���arrayTypeָ����*/
    public PropArray(BeanProp[] elements, String arrayType) {
        if (elements == null) {
            this.arrayElements = new BeanProp[0];
            this.length = 0;
        } else {
            this.arrayElements = elements;
            this.length = elements.length;
        }
        this.setPropType(arrayType);
    }
    //==========================================================================================Job
    /**��ȡ���鳤��*/
    public int getLength() {
        return length;
    }
    /**�������鳤��*/
    public void setLength(int length) {
        this.length = length;
    }
    /**��ȡ����Ԫ�ض��塣*/
    public BeanProp[] getArrayElements() {
        return arrayElements;
    }
    /**��������Ԫ�ض��塣*/
    public void setArrayElements(BeanProp[] arrayElements) {
        this.arrayElements = arrayElements;
    }
}