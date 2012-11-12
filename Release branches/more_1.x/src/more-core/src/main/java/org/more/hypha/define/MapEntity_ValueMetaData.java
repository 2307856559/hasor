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
import java.util.Map;
import org.more.core.error.MoreStateException;
/**
 * ��ʾһ��{@link Map}���͵�һ��key value��ֵ�Ե�Ԫ��Ϣ������
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class MapEntity_ValueMetaData extends Collection_ValueMetaData<AbstractValueMetaData> {
    private AbstractValueMetaData key   = null; //Key
    private AbstractValueMetaData value = null; //Value
    /**�÷������᷵��null*/
    public String getMetaDataType() {
        return null;
    }
    /**����KEY*/
    public void setKeyObject(AbstractValueMetaData key) {
        this.key = key;
    }
    /**����VAR*/
    public void setVarObject(AbstractValueMetaData value) {
        this.value = value;
    }
    /**���һ��ֵ����ǰ�����У��״����Ϊkey���������Ϊvar������������쳣��*/
    public void addObject(AbstractValueMetaData value) {
        if (this.key == null) {
            this.key = value;
            return;
        }
        if (this.value == null) {
            this.value = value;
            return;
        }
        throw new MoreStateException("key��value���Ѿ�����.");
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
    public AbstractValueMetaData getKey() {
        return this.key;
    }
    /**����key*/
    public void setKey(AbstractValueMetaData key) {
        this.key = key;
    }
    /**��ȡvalue*/
    public AbstractValueMetaData getValue() {
        return this.value;
    }
    /**����value*/
    public void setValue(AbstractValueMetaData value) {
        this.value = value;
    }
}