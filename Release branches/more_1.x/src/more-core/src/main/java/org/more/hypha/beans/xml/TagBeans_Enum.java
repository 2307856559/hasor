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
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.hypha.define.AbstractPropertyDefine;
import org.more.hypha.define.Enum_ValueMetaData;
/**
 * ���ڽ���enum��ǩ���ñ�ǩʹ�õ�ö�����������Զ�������һ�¡�
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Enum extends TagBeans_AbstractValueMetaDataDefine<Enum_ValueMetaData> {
    /**����{@link TagBeans_Enum}����*/
    public TagBeans_Enum(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link Enum_ValueMetaData}����*/
    protected Enum_ValueMetaData createDefine(XmlStackDecorator<Object> context) {
        return new Enum_ValueMetaData();
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        enumValue, enumType
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.enumValue, "enum");
        propertys.put(PropertyKey.enumType, "type");
        return propertys;
    }
    /**��������*/
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        Enum_ValueMetaData metaData = this.getDefine(context);
        if (metaData.getEnumType() == null) {
            AbstractPropertyDefine pdefine = (AbstractPropertyDefine) context.getAttribute(TagBeans_AbstractPropertyDefine.PropertyDefine);
            metaData.setEnumType(pdefine.getClassType());
        }
    }
}