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
import org.more.beans.AbstractPropertyDefine;
import org.more.beans.ValueMetaData;
import org.more.beans.ValueMetaData.PropertyMetaTypeEnum;
/**
 * ��ʾһ�������������ݣ���Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#SimpleType}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class Simple_ValueMetaData extends ValueMetaData {
    /** ��������{@link AbstractPropertyDefine}��������Ե��������������֣�������Ե������ǱȽϸ��ӵı���Map,List��ôͳͳʹ��Object��*/
    public enum PropertyType {
        /**��ʾ���Ե�ֵ������һ��null��*/
        Null,
        /**��ʾ���Ե�ֵ������һ��boolean���ͣ�����{@link Boolean}���͡�*/
        Boolean,
        /**��ʾ���Ե�ֵ������һ��byte���ͣ�����{@link Byte}���͡�*/
        Byte,
        /**��ʾ���Ե�ֵ������һ��short���ͣ�����{@link Short}���͡�*/
        Short,
        /**��ʾ���Ե�ֵ������һ��int���ͣ�����{@link Integer}���͡�*/
        Integer,
        /**��ʾ���Ե�ֵ������һ��long���ͣ�����{@link Long}���͡�*/
        Long,
        /**��ʾ���Ե�ֵ������һ��float���ͣ�����{@link Float}���͡�*/
        Float,
        /**��ʾ���Ե�ֵ������һ��double���ͣ�����{@link Double}���͡�*/
        Double,
        /**��ʾ���Ե�ֵ������һ��char���ͣ�����{@link Character}���͡�*/
        Char,
        /**��ʾ���Ե�ֵ������һ��string���ͣ�����{@link String}���͡�*/
        String,
    }
    private PropertyType valueMetaType = PropertyType.Null; //ֵ����
    private Object       value         = null;             //ֵ
    /**�÷������᷵��{@link PropertyMetaTypeEnum#SimpleType}��*/
    public PropertyMetaTypeEnum getPropertyType() {
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