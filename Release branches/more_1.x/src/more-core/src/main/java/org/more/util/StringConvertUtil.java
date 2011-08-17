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
package org.more.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.core.error.TransformException;
/**
 * �ַ�����������ת��������
 * @version 2009-4-29
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class StringConvertUtil {
    private static final Character DefaultValue_Character = ' ';
    private static final Boolean   DefaultValue_Boolean   = false;
    private static final Byte      DefaultValue_Byte      = 0;
    private static final Short     DefaultValue_Short     = 0;
    private static final Integer   DefaultValue_Integer   = 0;
    private static final Long      DefaultValue_Long      = 0l;
    private static final Float     DefaultValue_Float     = 0f;
    private static final Double    DefaultValue_Double    = 0d;
    /**
    * �����ַ���Ϊ��С���ȵ��������͡����value�������0~255֮�������÷����ط���byte���ͣ������256�򷵻�short���͡�
    * �ڱȷ�˵�ַ���ֵΪ125.6�򷵻�ֵΪfloat��
    * @param value Ҫ����Ϊ���ֵ��ַ���
    * @param defaultValue ������ݴ���ȡ��Ĭ��ֵ��
    * @return ���ؽ������ַ������������ʧ���򷵻�0����Ĭ��ֵ
    */
    public static Number parseNumber(final String value, final Number... defaultValue) {
        try {
            if (value.indexOf(".") != -1)
                try {
                    return Float.parseFloat(value);
                } catch (Exception e) {
                    return Double.parseDouble(value);
                }
            else
                try {
                    return Byte.parseByte(value);
                } catch (Exception e) {
                    try {
                        return Short.parseShort(value);
                    } catch (Exception e1) {
                        try {
                            return Integer.parseInt(value);
                        } catch (Exception e2) {
                            return Long.parseLong(value);
                        }
                    }
                }
        } catch (Exception e) {
            return (defaultValue.length >= 1) ? defaultValue[0] : 0;
        }
    }
    /**
     * ��������ת����ֻ֧�������������ͣ�String��StringBuffer��Integer��Byte��Character��Short��Long��Float��Double��Boolean��Date��
     * ʾ����DataType.changeType("12",Integer.class,-1);����ֵΪ12��DataType.changeType("aa",Integer.class,-1);����ֵΪ-1��
     * ע�⣺�����ָ��ת������Ĭ��������ת����String���͡�����Ĭ��ֵ��null��
     * @param value Ҫת�������ݡ�
     * @param toType Ҫת����Ŀ���������͡�
     * @param defaultValue �ɱ�Ĳ�����һ��������Ҫת�������ͣ� �ڶ���������ת����Ŀ������ʱ���ʧ�ܲ��õ�Ĭ��ֵ��
     * @return ����ת��֮���ֵ��
     */
    public static Object changeType(final Object value, final Class<?> toType, final Object... defaultValue) {
        if (value == null || toType == null)
            return null;
        String valueString = value.toString();
        Object defaultVar = (defaultValue.length >= 1) ? defaultValue[0] : null;
        // -----------����ֱ��ת��
        if (toType.isAssignableFrom(value.getClass()) == true)
            return value;
        // -----------String��ʽ
        else if (String.class == toType)
            return valueString;
        else if (StringBuffer.class == toType)
            return new StringBuffer(valueString);
        else if (Integer.class == toType || int.class == toType)
            return StringConvertUtil.parseInt(valueString, (Integer) defaultVar);
        else if (Byte.class == toType || byte.class == toType)
            return StringConvertUtil.parseByte(valueString, (Byte) defaultVar);
        else if (Character.class == toType || char.class == toType) {
            if (valueString.equals("") == true)
                return DefaultValue_Character;
            return Character.valueOf(valueString.charAt(0));
        } else if (Short.class == toType || short.class == toType)
            return StringConvertUtil.parseShort(valueString, (Short) defaultVar);
        else if (Long.class == toType || long.class == toType)
            return StringConvertUtil.parseLong(valueString, (Long) defaultVar);
        else if (Float.class == toType || float.class == toType)
            return StringConvertUtil.parseFloat(valueString, (Float) defaultVar);
        else if (Double.class == toType || double.class == toType)
            return StringConvertUtil.parseDouble(valueString, (Double) defaultVar);
        else if (Boolean.class == toType || boolean.class == toType)
            return StringConvertUtil.parseBoolean(valueString);
        else if (Date.class.isAssignableFrom(toType) == true) {
            if (value instanceof Date == true)
                return value;
            return StringConvertUtil.parseDate(valueString);
        }
        // -----------����ö��
        else if (Enum.class.isAssignableFrom(toType) == true) {
            Class<Enum<?>> e = (Class<Enum<?>>) toType;
            for (Enum<?> item : e.getEnumConstants()) {
                String enumValue = item.name().toLowerCase();
                if (enumValue.equals(valueString.toLowerCase()) == true)
                    return item;
            }
            return defaultVar;
        } else
            throw new TransformException("from [" + value.getClass() + "] to [" + toType + "]��֧�ֵ�ת�����͡�");
    }
    /**
     * ���ַ���������ת����int�������ݡ�����ַ�����ʽ�Ƿ���Ĭ��ֵΪ0��ʾ����
     * DataType.getInt("aa",0);����0����DataType.getInt("12",0);����12
     * @param value �����ַ�����
     * @param defaultValue ������ݴ���ȡ��Ĭ��ֵ��
     * @return ����int��ת�������
     */
    public static Integer parseInt(final String value, final Integer... defaultValue) {
        try {
            return (value == null || value.equals("") == false) ? Integer.valueOf(value) : defaultValue[0];
        } catch (Exception e) {
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Integer;
        }
    }
    /**
     * ���ַ���������ת����float�������ݡ�����ַ�����ʽ�Ƿ���Ĭ��ֵΪ0.0��ʾ����
     * DataType.getFloat("aa",0);����0����DataType.getFloat("12.8",0);����12.8
     * @param value �����ַ�����
     * @param defaultValue ������ݴ���ȡ��Ĭ��ֵ��
     * @return ����float��ת�������
     */
    public static Float parseFloat(final String value, final Float... defaultValue) {
        try {
            float var = (value == null || value.equals("") == false) ? Float.valueOf(value) : defaultValue[0];
            if (Float.isNaN(var) == true || Float.isInfinite(var) == true)
                return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Float;
            return var;
        } catch (Exception e) {
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Float;
        }
    }
    /**
     * ���ַ���������ת����double�������ݡ�����ַ�����ʽ�Ƿ���Ĭ��ֵΪ0.0��ʾ����
     * DataType.getDouble("aa",0);����0����DataType.getDouble("12.8",0);����12.8
     * @param value �����ַ�����
     * @param defaultValue ������ݴ���ȡ��Ĭ��ֵ��
     * @return ����double��ת�������
     */
    public static Double parseDouble(final String value, final Double... defaultValue) {
        try {
            return (value == null || value.equals("") == false) ? Double.valueOf(value) : defaultValue[0];
        } catch (Exception e) {
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Double;
        }
    }
    /**
     * ���ַ���������ת����boolean�������ݡ�(0,no,N)��ʾfalse��(1,yes,Y)��ʾyes
     * @param value �����ַ�����
     * @return ����boolean��ת�������
     */
    public static Boolean parseBoolean(final String value, final Boolean... defaultValue) {
        if (value == null)
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Boolean; //false;
        else if (value.equals("0") == true || value.equals("no") == true || value.equals("N") == true)
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Boolean; //false;
        else if (value.equals("1") == true || value.equals("yes") == true || value.equals("Y") == true)
            return (defaultValue.length >= 1) ? defaultValue[0] : !StringConvertUtil.DefaultValue_Boolean; //true;
        else
            return Boolean.parseBoolean(value);
    }
    /**
     * ���ַ���������ת����long�������ݡ�����ַ�����ʽ�Ƿ���Ĭ��ֵΪ0��ʾ����
     * DataType.getLong("aa",0);����0����DataType.getLong("123",0);����123
     * @param value �����ַ�����
     * @param defaultValue ������ݴ���ȡ��Ĭ��ֵ��
     * @return ����long��ת�������
     */
    public static Long parseLong(final String value, final Long... defaultValue) {
        try {
            return (value == null || value.equals("") == false) ? Long.valueOf(value) : defaultValue[0];
        } catch (Exception e) {
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Long;
        }
    }
    /**
     * ���ַ���������ת����byte�������ݡ�����ַ�����ʽ�Ƿ���Ĭ��ֵΪ0��ʾ����
     * DataType.getByte("aa",0);����0����DataType.getByte("123",0);����123
     * @param value �����ַ�����
     * @param defaultValue ������ݴ���ȡ��Ĭ��ֵ��
     * @return ����byte��ת�������
     */
    public static Byte parseByte(final String value, final Byte... defaultValue) {
        try {
            return (value == null || value.equals("") == false) ? Byte.valueOf(value) : defaultValue[0];
        } catch (Exception e) {
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Byte;
        }
    }
    /**
     * ���ַ���������ת����short�������ݡ�����ַ�����ʽ�Ƿ���Ĭ��ֵΪ0��ʾ����
     * DataType.getShort("aa",0);����0����DataType.getShort("123",0);����123
     * @param value �����ַ�����
     * @param defaultValue ������ݴ���ȡ��Ĭ��ֵ��
     * @return ����short��ת�������
     */
    public static Short parseShort(final String value, final Short... defaultValue) {
        try {
            return (value == null || value.equals("") == false) ? Short.valueOf(value) : defaultValue[0];
        } catch (Exception e) {
            return (defaultValue.length >= 1) ? defaultValue[0] : StringConvertUtil.DefaultValue_Short;
        }
    }
    /**
     * ���ַ���ת��Ϊ�������͡���ת�������п���ָ���ָ��ת�������Լ���Ӧ���͵�Ĭ��ת��ֵ��
     * ���͵�Ĭ��ת��ֵ��ָ��ԭ��������Ŀ��ת��ʱ�����쳣�����õ�Ĭ��ֵȡ���� �﷨���£�<br/>
     * 1��DataType.getList("a,b,c,3,4,5");Ĭ��ת�������ַ�ʽת���ǽ�ԭʼ���ݰ��ն��ŷָ� ת��������ַ�������<br/>
     * 2��DataType.getList("a;b;c;3;4;5",";");ָ���ָ��ת������������ת������Ĭ��ת��֮��ʹ���õĳ����ת��ʱʹ��
     * �ķָ��ӵ��������Ȩ����<br/>
     * 3��DataType.getList("a;b;c;3;4;5",";",Integer.class);ָ������ת��������ת�����ڵڶ���
     * ת��֮�ϵ�����������ת���ķ��ؼ��Ͻ��ʹ��ָ�����͡�<br/>
     * 4��DataType.getList("a;b;c;3;4;5",";",Integer.class,-1);��Ĭ��ֵ��ָ������ת����
     * �����������������ؽ��Ӧ����-1,-1,3,4,5�������д�ŵ�������Integer��<br/>
     * 5��DataType.getList("a;b;c;3;4;5",";",Integer.class,-1,newArrayList());
     * ��5�ַ�ʽ������ַ�ʽһ������ͬ���ǵ����ַ�ʽ�Ǻ��������ڲ��ᴴ��һ�����϶��󣬶���5�ַ�ʽ ���û��ṩ������϶���
     * 6��DataType.getList("a;b;c;3;4;5",";",Integer.class,-1,newArrayList(),true|false);
     * ��6�ַ�ʽ������ַ�ʽһ���������ֹ�����ʽ������ֵ�����������ڡ����������List������������û����ݵ�List�����г��ֳ�ͻ���Ƿ��滻ԭ������ȡ�����û�
     * true��ʾ�滻��false��ʾ���滻��
     * @param in_value �����ַ�����
     * @param param ��������ϸ�鿴����˵����
     * @return ����ת���ļ��϶���
     */
    public static List parseList(final String in_value, final Object... param) {
        String value = (in_value == null) ? "" : in_value;
        // -------------------
        String split = ",";// Ĭ�Ϸָ����
        Class<?> toType = String.class;// Ĭ��String����
        Object defaultValue = null;// Ĭ��ֵ��null��
        List array = null;
        boolean replay = true;// Ĭ��ֵ��true �滻��
        // -------------------
        if (param.length == 0) {
            // û�в���
            array = new ArrayList<Object>(0);
        } else if (param.length == 1) {
            // һ������
            split = (String) param[0];
            array = new ArrayList<Object>(0);
        } else if (param.length == 2) {
            // ��������
            split = (String) param[0];
            toType = (Class<?>) param[1];
            array = new ArrayList<Object>(0);
        } else if (param.length == 3) {
            // ��������
            split = (String) param[0];
            toType = (Class<?>) param[1];
            defaultValue = param[2];
            array = new ArrayList<Object>(0);
        } else if (param.length == 4) {
            // �ĸ�����
            split = (String) param[0];
            toType = (Class<?>) param[1];
            defaultValue = param[2];
            array = (List<?>) param[3];
        } else {
            // �������
            split = (String) param[0];
            toType = (Class<?>) param[1];
            defaultValue = param[2];
            array = (List<?>) param[3];
            replay = StringConvertUtil.parseBoolean(param[4].toString());
        }
        // -------------------
        String[] temp_split = value.split(split);
        for (String var : temp_split)
            if (array.contains(var) == true)
                if (replay == true) {
                    array.remove(var);
                    array.add(StringConvertUtil.changeType(var, toType, defaultValue));
                } else {}
            else
                array.add(StringConvertUtil.changeType(var, toType, defaultValue));
        return array;
    }
    /**
     * ���ַ���ת��Ϊ�������͡��÷�����parseList��������ͬ���Ǹ÷������ص���List������<br/> List array =
     * parseList(value, param); array.toArray();
     * @param in_value �����ַ�����
     * @param param ��������ϸ�鿴����˵����
     * @return ����ת�����������
     */
    public static Object[] parseArray(final String in_value, final Object... param) {
        String value = (in_value == null) ? "" : in_value;
        // -------------------
        List array = parseList(value, param);
        return array.toArray();
    }
    /**
     * ���ַ���ת��Ϊ�������͡���ת�������п���ָ���ָ��ת�������Լ���Ӧ���͵�Ĭ��ת��ֵ��
     * ���͵�Ĭ��ת��ֵ��ָ��ԭ��������Ŀ��ת��ʱ�����쳣�����õ�Ĭ��ֵȡ���� �﷨���£�<br/>
     * 1��DataType.getMap("key=value;key1=value1;key2=value2;");Ĭ��ת�������ַ�ʽ
     * ת���ǽ�ԭʼ���ݰ��ն��ŷָ�ת��������ַ�������<br/>
     * 2��DataType.getMap("key=value&key1=value1&key2=value2","=&");ָ���ָ��ת����
     * ��������ת������Ĭ��ת��֮��ʹ���õĳ����ת��ʱʹ�õķָ��ӵ��������Ȩ����<br/>
     * 3��DataType.getMap("key=1&key1=2&key2=3","=&",String.class,Integer.class);
     * ָ������ת��������ת�����ڵڶ���ת��֮�ϵ�����������ת���ķ��ؼ��Ͻ��ʹ��ָ�����͡�<br/>
     * 4��DataType.getMap("key=1&key1=a&key2=3","=&",String.class,Integer.class,-1);
     * ��Ĭ��ֵ��ָ������ת���������������������ؽ��Ӧ����key=1,key1=-1,key2=3�������д��
     * ��������String,Integer��ע�⣺�˴���Ĭ��ֵ��key=value��value��Ĭ��ֵ��<br/>
     * 5��DataType.getMap("key=1&key1=a&key2=3","=&",String.class,Integer.class,-1,newHashtable());
     * ��5�ַ�ʽ������ַ�ʽһ������ͬ���ǵ����ַ�ʽ�Ǻ��������ڲ��ᴴ��һ�����϶��󣬶���5�ַ�ʽ ���û��ṩ������϶���
     * ��ʾ�����������Map������������û����ݵ�Map�����г���key��ͻ�����5�ַ�ʽ��ʹ�ý���֮��Ľ���滻ԭ�е�key��value��
     * <br/>Ĭ�ϸú���Ч�����£�DataType.getMap("key=value;key1=value1","=;",String.class,String.class,null,newHashMap<String,String>());
     * 6��DataType.getMap("key=1&key1=a&key2=3","=&",String.class,Integer.class,-1,newHashtable(),true|false);
     * ��6�ַ�ʽ������ַ�ʽһ���������ֹ�����ʽ������ֵ�����������ڡ����������Map������������û����ݵ�Map�����г���key��ͻ���Ƿ��滻ԭ������ȡ�����û�
     * true��ʾ�滻��false��ʾ���滻��<br/>Ĭ�ϸú���Ч�����£�DataType.getMap("key=value;key1=value1","=;",String.class,String.class,null,newHashMap<String,String>(),false);
     * @param in_value �����ַ�����
     * @param param ��������ϸ�鿴����˵����
     * @return ����ת���ļ��϶���
     */
    public static Map parseMap(final String in_value, final Object... param) {
        String value = (in_value == null) ? "" : in_value;
        // -------------------
        String split_key = "=";// Ĭ�Ϸָ��1��
        String split_val = ";";// Ĭ�Ϸָ��2��
        Class<?> toType_key = String.class;// keyĬ��String����
        Class<?> toType_val = String.class;// valĬ��String����
        Object defaultValue = null;// Ĭ��ֵ��null��
        Map array = null;
        boolean replay = true;// Ĭ��ֵ��true �滻��
        // -------------------
        if (param.length == 0) {
            // û�в���
            array = new HashMap<String, String>(0);
        } else if (param.length == 1) {
            // һ������
            String split = (String) param[0];
            if (split.length() == 1)
                split_key = String.valueOf(split.charAt(0));
            else {
                split_key = String.valueOf(split.charAt(0));
                split_val = String.valueOf(split.charAt(1));
            }
            array = new HashMap<String, String>();
        } else if (param.length == 2) {
            // ��������
            String split = (String) param[0];
            if (split.length() == 1)
                split_key = String.valueOf(split.charAt(0));
            else {
                split_key = String.valueOf(split.charAt(0));
                split_val = String.valueOf(split.charAt(1));
            }
            toType_key = (Class<?>) param[1];
            array = new HashMap<String, String>();
        } else if (param.length == 3) {
            // ��������
            String split = (String) param[0];
            if (split.length() == 1)
                split_key = String.valueOf(split.charAt(0));
            else {
                split_key = String.valueOf(split.charAt(0));
                split_val = String.valueOf(split.charAt(1));
            }
            toType_key = (Class<?>) param[1];
            toType_val = (Class<?>) param[2];
            array = new HashMap<String, String>();
        } else if (param.length == 4) {
            // �ĸ�����
            String split = (String) param[0];
            if (split.length() == 1)
                split_key = String.valueOf(split.charAt(0));
            else {
                split_key = String.valueOf(split.charAt(0));
                split_val = String.valueOf(split.charAt(1));
            }
            toType_key = (Class<?>) param[1];
            toType_val = (Class<?>) param[2];
            defaultValue = param[3];
            array = new HashMap<String, String>();
        } else if (param.length == 5) {
            // �������
            String split = (String) param[0];
            if (split.length() == 1)
                split_key = String.valueOf(split.charAt(0));
            else {
                split_key = String.valueOf(split.charAt(0));
                split_val = String.valueOf(split.charAt(1));
            }
            toType_key = (Class<?>) param[1];
            toType_val = (Class<?>) param[2];
            defaultValue = param[3];
            array = (Map) param[4];
        } else {
            // ��������
            String split = (String) param[0];
            if (split.length() == 1)
                split_key = String.valueOf(split.charAt(0));
            else {
                split_key = String.valueOf(split.charAt(0));
                split_val = String.valueOf(split.charAt(1));
            }
            toType_key = (Class<?>) param[1];
            toType_val = (Class<?>) param[2];
            defaultValue = param[3];
            array = (Map) param[4];
            replay = StringConvertUtil.parseBoolean(param[4].toString());
        }
        // -------------------
        String[] temp_split = value.split(split_val);// key=value
        for (String var : temp_split) {
            String[] over_split = var.split(split_key);
            if (over_split.length != 2)
                continue;
            Object ov_key = StringConvertUtil.changeType(over_split[0], toType_key);
            Object ov_var = StringConvertUtil.changeType(over_split[1], toType_val, defaultValue);
            if (array.containsKey(ov_key) == true)
                if (replay == true) {
                    array.remove(ov_key);
                    array.put(ov_key, ov_var);
                } else {}
            else
                array.put(ov_key, ov_var);
        }
        return array;
    }
    /**
     * �˷��������ַ���ת����ʱ�����͡�Ĭ�ϸ�ʽ��yyyy/MM/dd-hh:mm:ss Ĭ��ʱ�䣺ϵͳ��ǰʱ�� ʱ���ʽ��ʾ˵����yyyy:��ʾ��
     * MM����ʾ�� dd ��ʾ�� hh:��ʾʱmm:��ʾ�� ss����ʾ��
     * ʾ����Convert.parseDate("2007/05/05","yyyy/MM/dd");
     * @param value �����ַ�����
     * @param patam ʱ���ʽ����ʽ�ַ�����
     * @return ����Date��ת�������
     * @throws ParseException
     */
    public static Date parseDate(String value, String... patam) {
        String formatString = null;
        Date defaultValue = null;
        // -------------------
        if (patam.length == 0) {
            defaultValue = new Date();
            formatString = "yyyy/MM/dd-hh:mm:ss";
        } else if (patam.length == 1)
            formatString = (patam[0] == null) ? "yyyy/MM/dd-hh:mm:ss" : (String) patam[0];
        // -------------------
        if (value == null || value.equals(""))
            return defaultValue;
        try {
            SimpleDateFormat sf = new SimpleDateFormat(formatString);
            Date res = sf.parse(value);
            return res;
        } catch (Exception e) {
            return defaultValue;
        }
    }
    /**
     * ���ַ���������ת����ָ����Enum�������ݣ����ת��ʧ���򷵻�null��
     * @param value �����ַ�����
     * @param forEnum Ҫת����ö�����͡�
     * @param defaultValue Ĭ��ֵ
     * @return ����enum��ת�������
     */
    public static <T extends Enum> T parseEnum(final String value, final Class<? extends Enum<?>> forEnum, Enum<?>... defaultValue) {
        for (Enum<?> item : forEnum.getEnumConstants()) {
            String enumValue = item.name().toLowerCase();
            if (enumValue.equals(value.toLowerCase()) == true)
                return (T) item;
        }
        return (T) ((defaultValue.length >= 1) ? defaultValue[0] : null);
    }
}