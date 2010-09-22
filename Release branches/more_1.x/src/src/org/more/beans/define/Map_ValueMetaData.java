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
import java.util.HashMap;
import java.util.Map;
import org.more.beans.ValueMetaData;
import org.more.beans.ValueMetaData.PropertyMetaTypeEnum;
/**
 * ��ʾһ��{@link Map}���͵�ֵԪ��Ϣ��������Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#MapCollection}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class Map_ValueMetaData extends Collection_ValueMetaData {
    private HashMap<ValueMetaData, ValueMetaData> valueData = new HashMap<ValueMetaData, ValueMetaData>(); //����
    /**�÷������᷵��{@link PropertyMetaTypeEnum#MapCollection}��*/
    public PropertyMetaTypeEnum getPropertyType() {
        return PropertyMetaTypeEnum.MapCollection;
    }
    /**��Map��ʽ���ؼ����е����ݡ�*/
    public Map<ValueMetaData, ValueMetaData> getCollectionValue() {
        return this.valueData;
    }
    /**���һ��Ԫ�ء�*/
    public void addObject(ValueMetaData key, ValueMetaData value) {
        this.valueData.put(key, value);
    };
    /**ɾ��һ��Ԫ�ء�*/
    public void removeObject(ValueMetaData key) {
        this.valueData.remove(key);
    };
    /**��ȡ���ϵ�ǰ����������������*/
    public int size() {
        return this.valueData.size();
    };
}