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
import org.more.beans.ValueMetaData;
import org.more.beans.define.AbstractPropertyDefine;
import org.more.core.xml.stream.EndElementEvent;
import org.more.util.attribute.StackDecorator;
/**
 * �����������Ԫ��Ϣ��ǩ�Ļ��ࡣ
 * @version 2010-9-19
 * @author ������ (zyc@byshell.org)
 */
public abstract class TagBeans_AbstractValueMetaDataDefine extends TagBeans_AbstractDefine {
    /**����ֵԪ��Ϣ.*/
    private static final String ValueMetaDataDefine = "$more_ValueMetaDataDefine";
    /**���ԵĶ�������*/
    protected String getDefineName() {
        return ValueMetaDataDefine;
    }
    /**���𽫽���������ValueMetaData���õ�������*/
    public void endElement(StackDecorator context, String xpath, EndElementEvent event) {
        ValueMetaData metaData = (ValueMetaData) context.getAttribute(ValueMetaDataDefine);
        AbstractPropertyDefine pdefine = (AbstractPropertyDefine) context.getAttribute(TagBeans_AbstractPropertyDefine.PropertyDefine);
        if (metaData != null)
            pdefine.setValueMetaData(metaData);
        super.endElement(context, xpath, event);
    };
}