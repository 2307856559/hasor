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
package org.more.core.json;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.more.core.copybean.CopyBeanUtil;
/**
 * ����stringBorder�����ھ����ַ������л�ʱʹ�õ����š�'������˫���š�"��(Ĭ��ֵ)��
 * JsonUtil�����л��ַ�������ʱ֧����String��Character��CharSequence��Reader��Щ���͡�
 * ��Ŀǰ�汾ΪֹJsonUtil��֧�ֿ�ѧ��������ʾ�����֡�
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class JsonUtil {
    private char stringBorder = 34; //34:", 39:'
    /**����JsonUtil�����ַ������л�ʹ��˫���Ż�����*/
    public JsonUtil() {};
    /**����JsonUtil�����ַ������л�ʹ�ò���������ֻ�е������˵����š�'������˫���š�"��ʱ�����Ч��������������ַ����ʹ��Ĭ���ַ���*/
    public JsonUtil(char stringBorder) {
        this.setStringBorder(stringBorder);
    };
    /**����JsonUtil�����ַ������л�ʹ�ò���������ֻ�е������˵����š�'������˫���š�"��ʱ�����Ч��������������ַ����ʹ��Ĭ���ַ���*/
    public JsonUtil(int stringBorder) {
        this.setStringBorder((char) stringBorder);
    };
    /**����JsonUtil�����ַ������л�ʹ�ò���������ֻ�е������˵����š�'������˫���š�"��ʱ�����Ч��������������ַ����ʹ��Ĭ���ַ���*/
    public JsonUtil(String stringBorder) {
        if (stringBorder != null)
            this.setStringBorder(stringBorder.charAt(0));
    };
    /**��ȡ�����������л�Ϊjson����ʱ�ַ�������������ʽ���Ե����Ż���˫���š�(Ĭ����˫����)  */
    public char getStringBorder() {
        return this.stringBorder;
    };
    /**���õ����������л�Ϊjson����ʱ�ַ�������������ʽ���Ե����Ż���˫���ţ�ֻ�е������˵����š�'������˫���š�"��ʱ�����Ч��������������ַ����������á�*/
    public void setStringBorder(char stringBorder) {
        if (stringBorder == 34 || stringBorder == 39)
            this.stringBorder = stringBorder;
    };
    /**��json����ת��Ϊָ�������ͽṹ��*/
    public Object toObject(String str, Class<?> superType) throws Throwable {
        Object obj = superType.newInstance();
        Map<?, ?> data = this.toMap(str);
        return CopyBeanUtil.newInstance().copy(data, obj, "value");
    }
    /**��json����ת��Ϊmap��ʽ�����json���ݽ�����ʾһ���������json Object���������json���󽫱���װ�����ص�map������key�ǿ��ַ���*/
    public Map<String, ?> toMap(String str) {
        String readStr = str.trim();
        if (readStr == null || readStr.equals(""))
            return null;
        /*----------------------------*/
        Object obj = this.passJsonString(readStr);
        if (obj instanceof Map == false) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("", obj);
            obj = map;
        }
        return (HashMap<String, Object>) obj;
    }
    /**����������ת��Ϊmap��ʽ���÷������Ƚ�����ת��ΪjsonȻ����ת����map*/
    public Map<String, ?> toMap(Object value) {
        String readStr = this.toString(value);
        return this.toMap(readStr);
    }
    /**������ת��Ϊjson��ʽ���ݣ�ע����������г��ֵݹ��������������ջ����쳣��*/
    public String toString(Object object) {
        //��������ַ������ݵ����ͽ��д���
        if (object == null)
            return "null";
        else if (object instanceof Boolean)
            return (((Boolean) object) == true) ? "true" : "false";
        else if (object instanceof String || object instanceof Character || object instanceof CharSequence)
            return new JsonString(this).toString(object);
        else if (object instanceof Collection || object.getClass().isArray() == true)
            return new JsonArray(this).toString(object);
        else if (object instanceof Number)
            return new JsonNumber(this).toString(object);
        else
            return new JsonObject(this).toString(object);
    }
    /**����һ��json����Ϊ����*/
    private Object passJsonString(String readStr) {
        if (readStr.equals("null"))
            return null;
        else if (readStr.equals("true"))
            return true;
        else if (readStr.equals("false"))
            return false;
        else if (readStr.charAt(0) == 34 || readStr.charAt(0) == 39)
            return new JsonString(this).toObject(readStr);
        else if (readStr.charAt(0) == '[')
            return new JsonArray(this).toObject(readStr);
        else if (readStr.charAt(0) == '{')
            return new JsonObject(this).toObject(readStr);
        else
            return new JsonNumber(this).toObject(readStr);
    }
}