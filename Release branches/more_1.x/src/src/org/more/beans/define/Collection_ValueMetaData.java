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
package org.more.beans.define;
import org.more.beans.ValueMetaData;
/**
 * ��ʾһ���������͵ĳ��������������������Ա����ļ������͡�
 * @version 2010-9-18
 * @author ������ (zyc@byshell.org)
 */
public abstract class Collection_ValueMetaData extends ValueMetaData {
    private String collectionValueType = null; //���ϵ���������
    private String collectionType      = null; //���϶������ͣ�������������ֵ������collectionValueTypeһ�¡�
    private int    initSize            = 0;   //��ʾ���ϳ�ʼ����С
    /**��ȡ���ϵ���������*/
    public String getCollectionValueType() {
        return this.collectionValueType;
    }
    /**���ü��ϵ���������*/
    public void setCollectionValueType(String collectionValueType) {
        this.collectionValueType = collectionValueType;
    }
    /**��ȡ���϶������ͣ�������������ֵ������collectionValueTypeһ�¡�*/
    public String getCollectionType() {
        return this.collectionType;
    }
    /**���ü��϶������ͣ�������������ֵ������collectionValueTypeһ�¡�*/
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
    /**��ȡ���ϳ�ʼ����С*/
    public int getInitSize() {
        return this.initSize;
    }
    /**���ü��ϳ�ʼ����С*/
    public void setInitSize(int initSize) {
        this.initSize = initSize;
    }
    /**��ȡ���ϵ�ǰ����������������*/
    public abstract int size();
}