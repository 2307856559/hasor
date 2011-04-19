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
package org.more.hypha.xml.support.beans;
import java.util.HashMap;
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.hypha.define.beans.Date_ValueMetaData;
import org.more.hypha.xml.context.XmlDefineResource;
/**
 * ���ڽ���date��ǩ
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Date extends TagBeans_AbstractValueMetaDataDefine<Date_ValueMetaData> {
    /**����{@link TagBeans_Date}����*/
    public TagBeans_Date(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link Date_ValueMetaData}����*/
    protected Date_ValueMetaData createDefine(XmlStackDecorator context) {
        return new Date_ValueMetaData();
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        dateString, formatString
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.dateString, "date");
        propertys.put(PropertyKey.formatString, "format");
        return propertys;
    }
}