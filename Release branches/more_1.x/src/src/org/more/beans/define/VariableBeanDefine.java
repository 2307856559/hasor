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
package org.more.beans.define;
import java.util.Date;
/**
 * VariableBeanDefine�����ڶ���һ��ֵ��Ϊbean��������{@link VariableType}ö���޶������ڸ�����Bean����ģ���ǲ������õġ�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class VariableBeanDefine extends TemplateBeanDefine {
    /**��ö���ж�����{@link VariableBeanDefine}����Ա�ʾ�Ļ������͡�*/
    public enum VariableType {
        /**null���ݡ�*/
        Null,
        /**�������͡�*/
        Boolean,
        /**�ֽ����͡�*/
        Byte,
        /**���������͡�*/
        Short,
        /**�������͡�*/
        Int,
        /**���������͡�*/
        Long,
        /**�����ȸ��������͡�*/
        Float,
        /**˫���ȸ��������͡�*/
        Double,
        /**�ַ����͡�*/
        Char,
        /**�ַ������͡�*/
        String,
        /**ʱ������*/
        Date,
    }
    /**����ö�ٻ�ȡ���������Class��*/
    public static Class<?> getBaseType(VariableType typeEnum) {
        if (typeEnum == VariableType.Boolean)
            return boolean.class;
        else if (typeEnum == VariableType.Byte)
            return byte.class;
        else if (typeEnum == VariableType.Short)
            return short.class;
        else if (typeEnum == VariableType.Int)
            return int.class;
        else if (typeEnum == VariableType.Long)
            return long.class;
        else if (typeEnum == VariableType.Float)
            return float.class;
        else if (typeEnum == VariableType.Double)
            return double.class;
        else if (typeEnum == VariableType.Char)
            return char.class;
        else if (typeEnum == VariableType.String)
            return String.class;
        else if (typeEnum == VariableType.Date)
            return Date.class;
        else
            return null;
    }
    /**����ö�ٻ�ȡ���װ����Class��*/
    public static Class<?> getPackType(VariableType typeEnum) {
        if (typeEnum == VariableType.Boolean)
            return Boolean.class;
        else if (typeEnum == VariableType.Byte)
            return Byte.class;
        else if (typeEnum == VariableType.Short)
            return Short.class;
        else if (typeEnum == VariableType.Int)
            return Integer.class;
        else if (typeEnum == VariableType.Long)
            return Long.class;
        else if (typeEnum == VariableType.Float)
            return Float.class;
        else if (typeEnum == VariableType.Double)
            return Double.class;
        else if (typeEnum == VariableType.Char)
            return Character.class;
        else if (typeEnum == VariableType.String)
            return String.class;
        else if (typeEnum == VariableType.Date)
            return Date.class;
        else
            return null;
    }
    //------------------------------------------------------------------
    private VariableType type   = VariableType.Null; //ֵ����
    private Object       value  = null;             //ֵ
    private String       format = null;             //���ݸ�ʽ��
    /**��ȡֵ���͡�*/
    public VariableType getType() {
        return type;
    }
    /**����ֵ���͡�*/
    public void setType(VariableType type) {
        this.type = type;
    }
    /**��ȡֵ*/
    public Object getValue() {
        return value;
    }
    /**����ֵ*/
    public void setValue(Object value) {
        this.value = value;
    }
    /**��ȡֵ�����ݸ�ʽ��*/
    public String getFormat() {
        return format;
    }
    /**����ֵ�����ݸ�ʽ��*/
    public void setFormat(String format) {
        this.format = format;
    }
    public TemplateBeanDefine getUseTemplate() {
        return null;
    }
    public void setUseTemplate(TemplateBeanDefine useTemplate) {}
}