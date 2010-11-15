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
import org.more.core.classcode.EngineToos;
import org.more.hypha.beans.AbstractPropertyDefine;
import org.more.hypha.beans.TypeParser;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.define.Simple_ValueMetaData;
import org.more.util.StringConvert;
import org.more.util.attribute.IAttribute;
/**
 * {@link QPP_Value}������boolean,byte,short,int,long,float,double,char,string
 * ֮����κ����ͣ������װ�������ڴ����롣���ȡ����value������Ҳ���ᴦ��
 * @version 2010-11-11
 * @author ������ (zyc@byshell.org)
 */
public class QPP_Value implements TypeParser {
    public ValueMetaData parser(IAttribute attribute, AbstractPropertyDefine property) {
        String value = (String) attribute.getAttribute("value");
        if (value == null)
            return null;
        //2.������boolean,byte,short,int,long,float,double,char,string֮����κ����͡�
        Class<?> propType = property.getClassType();
        if (propType != null)
            if (EngineToos.isBaseType(propType) == true || propType == String.class) {} else
                return null;
        else
            propType = Simple_ValueMetaData.DefaultValueType;
        //2.����
        Object var = StringConvert.changeType(value, propType);
        Simple_ValueMetaData newMEDATA = new Simple_ValueMetaData();
        newMEDATA.setValue(var);
        newMEDATA.setValueMetaType(Simple_ValueMetaData.getPropertyType(propType));
        return newMEDATA;
    }
}