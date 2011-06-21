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
 * VariableBeanDefine�����ڶ���һ��ֵ��Ϊbean����bean���������������ͺ��ַ������͡�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class VariableBeanDefine extends AbstractBaseBeanDefine {
    private VariableType type  = null; //ֵ����
    private String       value = null; //ֵ
    /**���ء�VariableBean����*/
    public String getBeanType() {
        return "VariableBean";
    }
    /**��ȡֵ���͡�*/
    public VariableType getType() {
        return this.type;
    }
    /**����ֵ���͡�*/
    public void setType(VariableType type) {
        this.type = type;
    }
    /**��ȡֵ*/
    public String getValue() {
        return this.value;
    }
    /**����ֵ*/
    public void setValue(String value) {
        this.value = value;
    }
    public void setUseTemplate(TemplateBeanDefine useTemplate) {}
    //------------------------------------------------------------------
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
    };
    /**���ַ�������ת��Ϊ{@link VariableType}ö�١�*/
    public static VariableType getVariableType(String type) {
        return (VariableType) StringConvertUtil.parseEnum(type, VariableType.class);
    };
    /**����ö�ٻ�ȡ���������Class��*/
    public static Class<?> getType(VariableType typeEnum) {
        if (typeEnum == null)
            return null;
        else if (typeEnum == VariableType.Boolean)
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
        else
            return null;
    }
}