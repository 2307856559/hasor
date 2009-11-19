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
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * BeanProperty�����ڱ�ʾ���Ե�bean���塣�ڸ����ж���һЩ�������ͣ���Щ�������͵Ķ����������Ż����ܡ����ǻ������͵�
 * ��װ��������int�İ�װ����Integer�����ڻ������ͷ��롣����java�����İ˸���������֮��BeanProperty�����
 * java.lang.String��Array(һά����)��List�ӿڡ�Map�ӿڡ�Set�ӿ����˶��塣BeanProperty��Ϊ���Զ����������propType���ԡ�
 * propType��һЩԤ�����ֵ��鿴BeanProperty�ľ�̬�ֶΡ�
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
    /**��������Array��*/
    public static final String TS_Array         = "Array";
    /**��������List��*/
    public static final String TS_List          = "List";
    /**��������Map��*/
    public static final String TS_Map           = "Map";
    /**��������Set��*/
    public static final String TS_Set           = "Set";
    /**  */
    private static final long  serialVersionUID = -3492072515778133870L;
    private String             name             = null;                 //�����������ڹ��췽���������ø�ֵ��Ч��
    private BeanProp           value            = null;                 //����ֵ��
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
    public BeanProp getValue() {
        return value;
    }
    /**��������ֵ��*/
    public void setValue(BeanProp value) {
        this.value = value;
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
        else if (propType.equals("java.lang.String") == true)
            super.setPropType(BeanProperty.TS_String);
        else if (propType.equals("Array") == true)
            super.setPropType(BeanProperty.TS_Array);
        else if (propType.equals("List") == true)
            super.setPropType(BeanProperty.TS_List);
        else if (propType.equals("Map") == true)
            super.setPropType(BeanProperty.TS_Map);
        else if (propType.equals("Set") == true)
            super.setPropType(BeanProperty.TS_Set);
        else
            super.setPropType(propType);
    }
    /** ����CreateEngine����������ʹ�õ����͡� */
    public static Class<?> toClass(Prop prop, ClassLoader loader) throws ClassNotFoundException {
        String propType = prop.getPropType();
        if (propType == BeanProperty.TS_Integer)
            return int.class;
        else if (propType == BeanProperty.TS_Byte)
            return byte.class;
        else if (propType == BeanProperty.TS_Char)
            return char.class;
        else if (propType == BeanProperty.TS_Double)
            return double.class;
        else if (propType == BeanProperty.TS_Float)
            return float.class;
        else if (propType == BeanProperty.TS_Long)
            return long.class;
        else if (propType == BeanProperty.TS_Short)
            return short.class;
        else if (propType == BeanProperty.TS_Boolean)
            return boolean.class;
        else if (propType == BeanProperty.TS_String)
            return String.class;
        else if (propType == BeanProperty.TS_Array) {
            Class<?> element;
            if (prop instanceof BeanProperty)
                element = BeanProperty.toClass(((BeanProperty) prop).getValue(), loader);
            else
                element = BeanProperty.toClass(prop, loader);
            return Array.newInstance(element, 1).getClass();
        } else if (propType == BeanProperty.TS_List)
            return List.class;
        else if (propType == BeanProperty.TS_Map)
            return Map.class;
        else if (propType == BeanProperty.TS_Set)
            return Set.class;
        else
            return loader.loadClass(propType);
    }
}