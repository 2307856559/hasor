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
import org.more.hypha.beans.define.Relation_ValueMetaData;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ���ڽ���ref��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Ref extends TagBeans_AbstractValueMetaDataDefine<Relation_ValueMetaData> {
    /**����{@link TagBeans_Ref}����*/
    public TagBeans_Ref(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link Relation_ValueMetaData}����*/
    protected Relation_ValueMetaData createDefine(XmlStackDecorator<Object> context) {
        return new Relation_ValueMetaData();
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        refBean, refPackage
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.refBean, "refBean");
        propertys.put(PropertyKey.refPackage, "refPackage");
        return propertys;
    }
}