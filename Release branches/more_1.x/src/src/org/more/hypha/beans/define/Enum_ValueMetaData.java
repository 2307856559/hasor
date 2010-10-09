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
package org.more.hypha.beans.define;
import org.more.hypha.beans.ValueMetaData.PropertyMetaTypeEnum;
import org.more.util.StringConvert;
/**
 * ��ʾһ�����ı����ݶΣ�ͨ��ʹ��CDATA��������Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#Enum}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class Enum_ValueMetaData extends AbstractValueMetaData {
    private String   enumValue = null; //ö�ٱ������ַ�����ʽ
    private Class<?> enumType  = null; //ö������
    /**�÷������᷵��{@link PropertyMetaTypeEnum#Enum}��*/
    public PropertyMetaTypeEnum getPropertyType() {
        return PropertyMetaTypeEnum.Enum;
    }
    /**ֱ�ӷ��ؽ���֮���ö�٣����û������enumType���������쳣��*/
    public Enum<?> getEnum() {
        return (Enum<?>) StringConvert.changeType(enumValue, enumType);
    }
    /**ʹ��һ��ö�����ͷ����������ö�������е�ö��ֵ��*/
    public Enum<?> getEnum(Class<?> enumType) {
        return (Enum<?>) StringConvert.changeType(enumValue, enumType);
    }
    /**ʹ��һ��ö�����ͷ����������ö�������е�ö��ֵ��*/
    public Enum<?> getEnum(Class<?> enumType, Enum<?> defaultValue) {
        return (Enum<?>) StringConvert.changeType(enumValue, enumType, defaultValue);
    }
    /**��ȡö�ٱ������ַ�����ʽ��*/
    public String getEnumValue() {
        return this.enumValue;
    }
    /**����ö�ٱ������ַ�����ʽ��*/
    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }
    /**��ȡö������*/
    public Class<?> getEnumType() {
        return this.enumType;
    }
    /**����ö������*/
    public void setEnumType(Class<?> enumType) {
        this.enumType = enumType;
    }
}