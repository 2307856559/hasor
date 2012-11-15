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
package org.more.hypha.define;
import org.more.util.StringConvertUtil;
/**
 * ��ʾһ�������������ݣ���Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#SimpleType}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class Simple_ValueMetaData extends ValueMetaData {
    /**����⵽value��ֵ������û�ж���typeʱ��ֵ���Ͳ��õ�Ĭ���������͡�*/
    public static final PropertyType DefaultValueType = PropertyType.String;
    //---------------------------------------------------------------------------------------���߷���
    /**�����ַ�����������ȡ��{@link PropertyType}ö�ٶ�Ӧ�����͡�*/
    public static PropertyType getPropertyType(String typeString) {
        if (typeString == null)
            return PropertyType.Null;
        if (typeString.equals("java.lang.String") == true)
            return PropertyType.String;
        PropertyType type = (PropertyType) StringConvertUtil.parseEnum(typeString, PropertyType.class);
        return type == null ? PropertyType.Null : type;
    };
    /**����ö�ٻ�ȡ���������Class��*/
    public static Class<?> getPropertyType(PropertyType type) {
        if (type == null)
            throw new NullPointerException("����type������Ϊ�ա�");
        if (type == PropertyType.Null)
            return null;
        if (type == PropertyType.Boolean)
            return boolean.class;
        else if (type == PropertyType.Byte)
            return byte.class;
        else if (type == PropertyType.Short)
            return short.class;
        else if (type == PropertyType.Int)
            return int.class;
        else if (type == PropertyType.Long)
            return long.class;
        else if (type == PropertyType.Float)
            return float.class;
        else if (type == PropertyType.Double)
            return double.class;
        else if (type == PropertyType.Char)
            return char.class;
        else if (type == PropertyType.String)
            return String.class;
        return null;//��Զ���������δ���
    }
    /**�������ͻ�ȡ��ö�����ͣ���Ŀ�������޷�ת����PropertyType�����Ļ�������ö��ʱ����null��*/
    public static PropertyType getPropertyType(Class<?> type) {
        if (type == null)
            return PropertyType.Null;
        if (type == boolean.class || type == Boolean.class)
            return PropertyType.Boolean;
        else if (type == byte.class || type == Byte.class)
            return PropertyType.Byte;
        else if (type == short.class || type == Short.class)
            return PropertyType.Short;
        else if (type == int.class || type == Integer.class)
            return PropertyType.Int;
        else if (type == long.class || type == Long.class)
            return PropertyType.Long;
        else if (type == float.class || type == Float.class)
            return PropertyType.Float;
        else if (type == double.class || type == Double.class)
            return PropertyType.Double;
        else if (type == char.class || type == Character.class)
            return PropertyType.Char;
        else if (type == String.class)
            return PropertyType.String;
        else
            //ת��Ĭ��ֵ
            return null;
    }
    //---------------------------------------------------------------------------------------
    private PropertyType valueMetaType = PropertyType.Null; //ֵ����
    private Object       value         = null;             //ֵ
    /**�÷������᷵��{@link PropertyMetaTypeEnum#SimpleType}��*/
    public String getMetaDataType() {
        return PropertyMetaTypeEnum.SimpleType;
    }
    /**��ȡһ��ö�����ö��ֵ�����˵�ǰValueMetaData��ͼ�������������͡�*/
    public PropertyType getValueMetaType() {
        return this.valueMetaType;
    }
    /**����һ��ö�����ö��ֵ�����˵�ǰValueMetaData��ͼ�������������͡�*/
    public void setValueMetaType(PropertyType valueMetaType) {
        this.valueMetaType = valueMetaType;
    }
    /**��ȡֵ*/
    public Object getValue() {
        return value;
    }
    /**д��ֵ*/
    public void setValue(Object value) {
        this.value = value;
    }
}