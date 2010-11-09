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
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.util.StringConvert;
import org.more.util.attribute.IAttribute;
/**
 * Ĭ������ֵ��������Ĭ������������String
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class QPP_Value implements TypeParser {
    /**��ͼ������Ϊ{@link Simple_ValueMetaData}�������ʧ�ܷ���null��*/
    public ValueMetaData parser(String value, IAttribute attribute, AbstractPropertyDefine property) {
        //1.����Ƿ���Խ���
        if (value == null)
            return null;
        //2.���н���
        Class<?> propType = property.getClassType();
        if (propType == null)
            //����⵽value��ֵ������û�ж���typeʱ��ֵ���Ͳ��õ�Ĭ���������͡�
            propType = Simple_ValueMetaData.DefaultValueType;
        Object var = StringConvert.changeType(value, propType);
        Simple_ValueMetaData newMEDATA = new Simple_ValueMetaData();
        newMEDATA.setValue(var);
        newMEDATA.setValueMetaType(Simple_ValueMetaData.getPropertyType(propType));
        return newMEDATA;
    }
}