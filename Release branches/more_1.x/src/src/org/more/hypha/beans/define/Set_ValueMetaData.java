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
package org.more.hypha.beans.define;
import java.util.HashSet;
import java.util.Set;
import org.more.hypha.beans.PropertyMetaTypeEnum;
/**
 * ��ʾһ��{@link Set}���͵�ֵԪ��Ϣ��������Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#SetCollection}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class Set_ValueMetaData extends Collection_ValueMetaData<AbstractValueMetaData> {
    private HashSet<AbstractValueMetaData> valueData = new HashSet<AbstractValueMetaData>(); //����
    /**�÷������᷵��{@link PropertyMetaTypeEnum#SetCollection}��*/
    public String getPropertyType() {
        return PropertyMetaTypeEnum.SetCollection;
    }
    /**��Set��ʽ���ؼ����е����ݡ�*/
    public Set<AbstractValueMetaData> getCollectionValue() {
        return this.valueData;
    }
    /**���һ��Ԫ�ء�*/
    public void addObject(AbstractValueMetaData value) {
        this.valueData.add(value);
    };
    /**ɾ��һ��Ԫ�ء�*/
    public void removeObject(AbstractValueMetaData value) {
        this.valueData.remove(value);
    };
    /**��ȡ���ϵ�ǰ����������������*/
    public int size() {
        return this.valueData.size();
    };
}