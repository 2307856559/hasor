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
import java.util.ArrayList;
import java.util.List;
/**
 * ��ʾһ���������͵ĳ��������������������Ա����ļ������͡�
 * @version 2010-9-18
 * @author ������ (zyc@byshell.org)
 */
public abstract class Collection_ValueMetaData<T extends ValueMetaData> extends ValueMetaData {
    /*���϶������ͣ�������������ֵ������collectionValueTypeһ�¡�*/
    private String  collectionType = null;
    /*����*/
    private List<T> values         = new ArrayList<T>();
    /*------------------------------------------------------------------*/
    /**��ȡ���϶������ͣ�������������ֵ������collectionValueTypeһ�¡�*/
    public String getCollectionType() {
        return this.collectionType;
    }
    /**���ü��϶������ͣ�������������ֵ������collectionValueTypeһ�¡�*/
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
    /**��ȡ���Լ��ϡ�*/
    public List<T> getValues() {
        return this.values;
    };
    /**�������Լ��ϡ�*/
    public void setValues(List<T> values) {
        this.values = values;
    };
}