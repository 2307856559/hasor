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
package org.more.beans.info;
/**
 * BeanProperty�����ڱ�ʾ���Ե�bean���塣�ڸ����ж���һЩ�������ͣ���Щ�������͵Ķ����������Ż����ܡ����ǻ������͵�
 * ��װ��������int�İ�װ����Integer�����ڻ������ͷ��롣����java�����İ˸���������֮��BeanProperty�����java.lang.String���˶��塣
 * BeanProperty��Ϊ���Զ����������propType���ԣ�propType��һЩԤ�����ֵ��鿴BeanProperty�ľ�̬�ֶΡ�
 * <br/>Date : 2009-11-18
 * @author ������
 */
public class BeanProperty extends Prop {
    /**��������int��*/
    public static final String TS_Integer       = "int";
    /**��������byte��*/
    public static final String TS_Byte          = "byte";
    /**��������char��*/
    public static final String TS_Char          = "char";
    /**��������double��*/
    public static final String TS_Double        = "double";
    /**��������float��*/
    public static final String TS_Float         = "float";
    /**��������long��*/
    public static final String TS_Long          = "long";
    /**��������short��*/
    public static final String TS_Short         = "short";
    /**��������boolean��*/
    public static final String TS_Boolean       = "boolean";
    /**��������String��*/
    public static final String TS_String        = "String";
    /**  */
    private static final long  serialVersionUID = -3492072515778133870L;
    private String             name             = null;                 //�����������ڹ��췽���������ø�ֵ��Ч��
    private BeanProp           refValue         = null;                 //����ֵ��
    //=========================================================================
    /**��ȡ�����������ڹ��췽���������ø�ֵ��Ч��*/
    public String getName() {
        return name;
    }
    /**���������������ڹ��췽���������ø�ֵ��Ч��*/
    public void setName(String name) {
        this.name = name;
    }
    /**��ȡ����ֵ��*/
    public BeanProp getRefValue() {
        return refValue;
    }
    /**��������ֵ��*/
    public void setRefValue(BeanProp refValue) {
        this.refValue = refValue;
    }
    /**��ȡ���Ե����ͣ��������ã���������������TS_�������塣*/
    public String getPropType() {
        return super.getPropType();
    }
    /**�������Ե����ͣ��������ã���������������TS_�������塣*/
    public void setPropType(String propType) {
        if (propType.equals("int") == true)
            super.setPropType(BeanProperty.TS_Integer);
        else if (propType.equals("byte") == true)
            super.setPropType(BeanProperty.TS_Byte);
        else if (propType.equals("char") == true)
            super.setPropType(BeanProperty.TS_Char);
        else if (propType.equals("double") == true)
            super.setPropType(BeanProperty.TS_Double);
        else if (propType.equals("float") == true)
            super.setPropType(BeanProperty.TS_Float);
        else if (propType.equals("long") == true)
            super.setPropType(BeanProperty.TS_Long);
        else if (propType.equals("short") == true)
            super.setPropType(BeanProperty.TS_Short);
        else if (propType.equals("boolean") == true)
            super.setPropType(BeanProperty.TS_Boolean);
        else if (propType.equals("String") == true)
            super.setPropType(BeanProperty.TS_String);
        else
            super.setPropType(propType);
    }
}