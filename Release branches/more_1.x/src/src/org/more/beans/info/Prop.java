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
import org.more.util.attribute.AttBase;
/**
 * ����more.beans��������ĺ����࣬Prop���ڱ�ʾһ��ӵ�о������͵�bean������Դ��
 * �ڸ����ж���һЩ�������ͣ���Щ�������͵Ķ����������Ż����ܡ����ǻ������͵�
 * ��װ��������int�İ�װ����Integer�����ڻ������ͷ��롣����java�����İ˸���������֮��BeanProperty�����java.lang.String���˶��塣
 * BeanProperty��Ϊ���Զ����������propType���ԣ�propType��һЩԤ�����ֵ��鿴BeanProperty�ľ�̬�ֶΡ�
 * <br/>Date : 2009-11-18
 * @author ������
 */
public abstract class Prop extends AttBase {
    //========================================================================================Field
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
    private static final long  serialVersionUID = -3350281122432032642L;
    private String             id               = null;                 //Ψһ��Bean ID
    private String             propType         = null;                 //������Ӧ�ÿ���ͨ��BeanFactory.getBeanClassLoader().loadClass()��ȡ
    //==========================================================================================Job
    /**��ȡΨһ��Bean ID��*/
    public String getId() {
        return id;
    }
    /**����Ψһ��Bean ID��*/
    public void setId(String id) {
        this.id = id;
    }
    /**��ȡ���Ե����ͣ��������ã���������������TS_�������塣*/
    public String getPropType() {
        return propType;
    }
    /**�������Ե����ͣ��������ã���������������TS_�������塣*/
    public void setPropType(String propType) {
        if (propType.equals("int") == true)
            this.propType = Prop.TS_Integer;
        else if (propType.equals("byte") == true)
            this.propType = Prop.TS_Byte;
        else if (propType.equals("char") == true)
            this.propType = Prop.TS_Char;
        else if (propType.equals("double") == true)
            this.propType = Prop.TS_Double;
        else if (propType.equals("float") == true)
            this.propType = Prop.TS_Float;
        else if (propType.equals("long") == true)
            this.propType = Prop.TS_Long;
        else if (propType.equals("short") == true)
            this.propType = Prop.TS_Short;
        else if (propType.equals("boolean") == true)
            this.propType = Prop.TS_Boolean;
        else if (propType.equals("String") == true)
            this.propType = Prop.TS_String;
        else
            this.propType = propType;
    }
    /***/
    public static Class<?> getType(String propType, ClassLoader loader) throws Exception {
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
        else
            return loader.loadClass(propType);
    }
}