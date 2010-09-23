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
import java.util.Map;
import org.more.StateException;
import org.more.beans.ValueMetaData;
/**
 * ��ʾһ��{@link Map}���͵�һ��key value��ֵ�Ե�Ԫ��Ϣ������
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class MapEntity_ValueMetaData extends Collection_ValueMetaData<ValueMetaData> {
    private ValueMetaData key   = null; //Key
    private ValueMetaData value = null; //Value
    /**�÷������᷵��null*/
    public PropertyMetaTypeEnum getPropertyType() {
        return null;
    }
    /**����KEY*/
    public void setKeyObject(ValueMetaData key) {
        this.key = key;
    }
    /**����VAR*/
    public void setVarObject(ValueMetaData value) {
        this.value = value;
    }
    /**���һ��ֵ����ǰ�����У��״����Ϊkey���������Ϊvar������������쳣��*/
    public void addObject(ValueMetaData value) {
        if (this.key == null) {
            this.key = value;
            return;
        }
        if (this.value == null) {
            this.value = value;
            return;
        }
        throw new StateException("key��value���Ѿ�����.");
    }
    public int size() {
        if (this.key == null && this.value == null)
            return 0;
        if (this.key != null && this.value != null)
            return 2;
        if (this.key == null || this.value == null)
            return 1;
        return 0;
    }
    /**��ȡkey*/
    public ValueMetaData getKey() {
        return this.key;
    }
    /**����key*/
    public void setKey(ValueMetaData key) {
        this.key = key;
    }
    /**��ȡvalue*/
    public ValueMetaData getValue() {
        return this.value;
    }
    /**����value*/
    public void setValue(ValueMetaData value) {
        this.value = value;
    }
}