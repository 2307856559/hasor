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
import java.util.ArrayList;
import java.util.List;
import org.more.beans.ValueMetaData;
import org.more.beans.ValueMetaData.PropertyMetaTypeEnum;
/**
 * ��ʾһ��{@link List}���͵�ֵԪ��Ϣ��������Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#ListCollection}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class List_ValueMetaData extends Collection_ValueMetaData {
    private ArrayList<ValueMetaData> valueData = new ArrayList<ValueMetaData>(); //����
    /**�÷������᷵��{@link PropertyMetaTypeEnum#ListCollection}��*/
    public PropertyMetaTypeEnum getPropertyType() {
        return PropertyMetaTypeEnum.ListCollection;
    }
    /**��List��ʽ���ؼ����е����ݡ�*/
    public List<ValueMetaData> getCollectionValue() {
        return this.valueData;
    }
    /**���һ��Ԫ�ء�*/
    public void addObject(ValueMetaData value) {
        this.valueData.add(value);
    };
    /**ɾ��һ��Ԫ�ء�*/
    public void removeObject(ValueMetaData value) {
        this.valueData.remove(value);
    };
    /**��ȡ���ϵ�ǰ����������������*/
    public int size() {
        return this.valueData.size();
    };
}