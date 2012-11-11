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
package org.more.hypha.beans.xml;
import java.util.HashMap;
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ���ڽ���value��ǩ
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Value extends TagBeans_AbstractValueMetaDataDefine<Simple_ValueMetaData> {
    /**����{@link TagBeans_Value}����*/
    public TagBeans_Value(XmlDefineResource configuration) {
        super(configuration);
    }
    protected Simple_ValueMetaData createDefine(XmlStackDecorator<Object> context) {
        return new Simple_ValueMetaData();
    }
    /**�������ԡ�*/
    public enum PropertyKey {
        value, valueMetaType
    };
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.value, "value");
        propertys.put(PropertyKey.valueMetaType, "type");
        return propertys;
    }
    /**��ʼ��ǩ*/
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        //1.���յ�����ֵ
        Simple_ValueMetaData newMEDATA = this.getDefine(context);
        if (newMEDATA.getValueMetaType() == null)
            newMEDATA.setValueMetaType(Simple_ValueMetaData.DefaultValueType);
    }
}