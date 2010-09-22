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
package org.more.beans.resource.namespace;
import java.util.HashMap;
import java.util.Map;
import org.more.beans.define.Relation_ValueMetaData;
import org.more.util.attribute.StackDecorator;
/**
 * ���ڽ���ref��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Ref extends TagBeans_AbstractValueMetaDataDefine {
    /**����{@link Relation_ValueMetaData}����*/
    protected Object createDefine(StackDecorator context) {
        return new Relation_ValueMetaData();
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        refBean, refScope
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.refBean, "refBean");
        propertys.put(PropertyKey.refScope, "refScope");
        return propertys;
    }
}