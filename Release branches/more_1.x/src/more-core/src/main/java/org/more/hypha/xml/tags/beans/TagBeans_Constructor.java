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
package org.more.hypha.xml.tags.beans;
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.hypha.define.BeanDefine;
import org.more.hypha.define.ConstructorDefine;
import org.more.hypha.xml.XmlDefineResource;
/**
 * ���ڽ���constructor-arg��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Constructor extends TagBeans_AbstractPropertyDefine<ConstructorDefine> {
    /**����{@link TagBeans_Constructor}����*/
    public TagBeans_Constructor(XmlDefineResource configuration) {
        super(configuration);
    };
    /**����{@link ConstructorDefine}����*/
    protected ConstructorDefine createDefine(XmlStackDecorator<Object> context) {
        return new ConstructorDefine();
    };
    /**���幹�췽���������ԡ�*/
    public enum PropertyKey {
        index,
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        Map<Enum<?>, String> propertys = super.getPropertyMappings();
        propertys.put(PropertyKey.index, "index");
        return propertys;
    };
    /**������ע�ᵽBean�С�*/
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) {
        ConstructorDefine property = this.getDefine(context);
        BeanDefine define = (BeanDefine) context.getAttribute(TagBeans_AbstractBeanDefine.BeanDefine);
        if (property.getIndex() == -1)
            property.setIndex(define.getInitParams().size());
        define.addInitParam(property);
        super.endElement(context, xpath, event);
    };
}