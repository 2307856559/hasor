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
import org.more.hypha.beans.AbstractPropertyDefine;
import org.more.hypha.beans.TypeParser;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.define.Enum_ValueMetaData;
import org.more.util.attribute.IAttribute;
/**
 * ö������ֵ��������Ĭ��ֵ��null��
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class QPP_Enum implements TypeParser {
    /**��ͼ������Ϊ{@link Enum_ValueMetaData}�������ʧ�ܷ���null��*/
    public ValueMetaData parser(String value, IAttribute attribute, AbstractPropertyDefine property) {
        //1.����Ƿ���Խ���
        if (value == null)
            value = (String) attribute.getAttribute("enum");
        if (value == null)
            return null;
        //2.���н���
        Class<?> propType = property.getClassType();
        if (propType == null || Enum.class.isAssignableFrom(propType) == false)
            propType = null;
        Enum_ValueMetaData newMEDATA = new Enum_ValueMetaData();
        newMEDATA.setEnumValue(value);
        newMEDATA.setEnumType(propType);
        return newMEDATA;
    }
}