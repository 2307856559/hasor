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
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.hypha.beans.define.MethodDefine;
import org.more.hypha.beans.define.ParamDefine;
import org.more.hypha.beans.define.PropertyDefine;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ���ڽ���param��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Param extends TagBeans_AbstractPropertyDefine<ParamDefine> {
    /**����{@link TagBeans_Param}����*/
    public TagBeans_Param(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link PropertyDefine}����*/
    protected ParamDefine createDefine(XmlStackDecorator<Object> context) {
        return new ParamDefine();
    }
    /**�������Ա�ǩ���е�����*/
    public enum PropertyKey {
        index
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        Map<Enum<?>, String> propertys = super.getPropertyMappings();
        propertys.put(PropertyKey.index, "index");
        return propertys;
    }
    /**������ע�ᵽBean�С�*/
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) {
        ParamDefine param = this.getDefine(context);
        MethodDefine define = (MethodDefine) context.getAttribute(TagBeans_Method.MethodDefine);
        define.addParam(param);
        super.endElement(context, xpath, event);
    }
}