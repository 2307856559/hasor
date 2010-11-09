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
import java.util.Map;
import org.more.LostException;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.beans.define.AbstractPropertyDefine;
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.hypha.configuration.DefineResourceImpl;
import org.more.util.StringConvert;
/**
 * ���ڽ���value��ǩ
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Value extends TagBeans_AbstractValueMetaDataDefine<Simple_ValueMetaData> {
    /**����{@link TagBeans_Value}����*/
    public TagBeans_Value(DefineResourceImpl configuration) {
        super(configuration);
    }
    protected Simple_ValueMetaData createDefine() {
        return new Simple_ValueMetaData();
    }
    /**�������ԡ�*/
    public enum PropertyKey {
        value, type
    };
    /**����ģ�����ԡ�*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        return null;
    }
    /**��ʼ��ǩ*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        AbstractPropertyDefine pdefine = (AbstractPropertyDefine) context.getAttribute(TagBeans_AbstractPropertyDefine.PropertyDefine);
        //1.׼������
        String propTypeString = event.getAttributeValue("type");
        String propValueString = event.getAttributeValue("value");
        Class<?> propType = null;
        Object propValue = null;
        //2.����type
        if (propTypeString != null) {
            VariableType typeEnum = (VariableType) StringConvert.changeType(propTypeString, VariableType.class);
            if (typeEnum != null)
                propType = getBaseType(typeEnum);
            else
                try {
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    propType = loader.loadClass(propTypeString);
                    pdefine.setClassType(propType);
                } catch (ClassNotFoundException e) {
                    throw new LostException("ClassNotFoundException,��������[" + propTypeString + "]��ʧ.", e);
                }
        } else
            propType = pdefine.getClassType();
        //
        if (propValueString != null)
            if (propType == null)
                /*����⵽value��ֵ������û�ж���typeʱ��ֵ���Ͳ��õ�Ĭ���������͡�*/
                propType = Simple_ValueMetaData.DefaultValueType;
        //3.ת������ֵ
        propValue = StringConvert.changeType(propValueString, propType);
        //4.��������ֵ
        Simple_ValueMetaData newMEDATA = this.getDefine(context);
        newMEDATA.setValue(propValue);
        newMEDATA.setValueMetaType(Simple_ValueMetaData.getPropertyType(propType));
    }
}