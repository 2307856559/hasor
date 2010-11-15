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
package org.more.hypha.beans.support;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.hypha.DefineResource;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.define.Array_ValueMetaData;
import org.more.hypha.beans.define.PropertyType;
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.util.StringConvert;
/**
 * ���ڽ���array��ǩ
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Array extends TagBeans_AbstractCollection<Array_ValueMetaData> {
    /**����{@link TagBeans_Array}����*/
    public TagBeans_Array(DefineResource configuration) {
        super(configuration);
    }
    /**����{@link Array_ValueMetaData}����*/
    protected Array_ValueMetaData createDefine() {
        return new Array_ValueMetaData();
    }
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {
        Array_ValueMetaData array = this.getDefine(context);
        Class<?> collectionType = array.getCollectionType();
        for (ValueMetaData var : array.getCollectionValue())
            //����һ�¼�����Simple_ValueMetaData�������ݵ�valueֵ���͡�
            if (var instanceof Simple_ValueMetaData == true) {
                Simple_ValueMetaData simple = (Simple_ValueMetaData) var;
                PropertyType propertyType = Simple_ValueMetaData.getPropertyType(collectionType);
                simple.setValueMetaType(propertyType);
                if (propertyType != PropertyType.Null)
                    simple.setValue(StringConvert.changeType(simple.getValue(), collectionType));
                else
                    simple.setValue(null);
            }
        super.endElement(context, xpath, event);
    }
    /**����Ĭ�ϼ�����������*/
    protected Class<?> getDefaultCollectionType() {
        return Object.class;
    }
}