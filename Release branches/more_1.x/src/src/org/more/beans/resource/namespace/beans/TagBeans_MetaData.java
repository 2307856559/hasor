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
package org.more.beans.resource.namespace.beans;
import org.more.beans.AbstractBeanDefine;
import org.more.beans.AbstractPropertyDefine;
import org.more.beans.ValueMetaData;
import org.more.beans.resource.XmlConfiguration;
import org.more.beans.resource.namespace.Tag_Abstract;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
/**
 * ���ڽ���meta��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_MetaData extends Tag_Abstract implements XmlElementHook {
    /**����{@link TagBeans_MetaData}����*/
    public TagBeans_MetaData(XmlConfiguration configuration) {
        super(configuration);
    }
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        String key = event.getAttributeValue("key");
        String value = event.getAttributeValue("value");
        //1.����ֵ����
        ValueMetaData meta_Define = (ValueMetaData) context.getAttribute(TagBeans_AbstractValueMetaDataDefine.ValueMetaDataDefine);
        if (meta_Define != null) {
            meta_Define.setAttribute(key, value);
            return;
        }
        //2.����
        AbstractPropertyDefine prop_Define = (AbstractPropertyDefine) context.getAttribute(TagBeans_AbstractPropertyDefine.PropertyDefine);
        if (prop_Define != null) {
            prop_Define.setAttribute(key, value);
            return;
        }
        //3.Bean����
        AbstractBeanDefine bean_Define = (AbstractBeanDefine) context.getAttribute(TagBeans_AbstractBeanDefine.BeanDefine);
        if (bean_Define != null) {
            bean_Define.setAttribute(key, value);
            return;
        }
    }
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {}
}