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
import org.more.util.StringConvertUtil;
/**
 * ��ʾһ�����ı����ݶΣ�ͨ��ʹ��CDATA��������Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#Enum}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class Enum_ValueMetaData extends AbstractValueMetaData {
    private String   enumValue = null; //ö�ٱ������ַ�����ʽ
    private String   enumType  = null; //ö������
    private Class<?> eType     = null;
    /**�÷������᷵��{@link PropertyMetaTypeEnum#Enum}��*/
    public String getMetaDataType() {
        return PropertyMetaTypeEnum.Enum;
    }
    /**ֱ�ӷ��ؽ���֮���ö�٣����û������enumType���������쳣��*/
    public Enum<?> getEnumType(ClassLoader enumLoader) throws ClassNotFoundException {
        Class<?> eType = this.getEnumClass(enumLoader);
        return this.getEnum(eType);
    }
    /**ʹ��һ��ö�����ͷ����������ö�������е�ö��ֵ��*/
    public Enum<?> getEnum(Class<?> enumType) {
        return (Enum<?>) StringConvertUtil.changeType(enumValue, enumType);
    }
    /**ʹ��һ��ö�����ͷ����������ö�������е�ö��ֵ��*/
    public Enum<?> getEnum(Class<?> enumType, Enum<?> defaultValue) {
        return (Enum<?>) StringConvertUtil.changeType(enumValue, enumType, defaultValue);
    }
    /**��ȡװ�ص�ö�����͡�*/
    public Class<?> getEnumClass(ClassLoader enumLoader) throws ClassNotFoundException {
        if (this.eType == null)
            this.eType = enumLoader.loadClass(this.enumType);
        return this.eType;
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
    public String getEnumType() {
        return this.enumType;
    }
    /**����ö������*/
    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }
}