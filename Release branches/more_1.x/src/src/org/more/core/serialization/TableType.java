/*
 * Copyright 2008-2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.more.core.serialization;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.CastException;
/**
 * �����ͣ���������more���л�ϵͳ����ḻ�Ļ����������͡������͵�ԭʼ�ṹ���Կ�����һ����������ÿһ�����ڵ�����������more���л��������͡�
 * �ɴ˿�����ϳ�������ı������Ӷ��ﵽ�������ݶ����Ŀ�ġ����Ƕ���ʵ�ʵ�����ƽ̨����ȷ�˵ʱ�����ڡ�����Ҫ����ı���д���Ȼ���ڽ������л���
 * ������Ҳ���Դ�ſ�����Ա�Զ����������͡����Ƕ��ڷ����л�ʱ��Ҫ������Ӧ���Զ����������Ͷ�������ⲿ�����ݻ������л���չλ���Ͻ��б��档
 * �����һ��ƽ̨�����л�֮���������һ��ƽ̨�Ϸ����л��Ҳ���ָ�����ͻ����޷�װ��ָ��������ʱmoreϵ�л�ϵͳ���Ա����ʽ���з����л������������ʽ
 * Ϊһ��Map���󡣱����Ͷ����ԭʼ�������ǡ�[More Table]��
 * Date : 2009-7-8
 * @author ������
 */
@SuppressWarnings("unchecked")
public final class TableType extends BaseType {
    TableType() {}
    @Override
    protected String getShortOriginalName() {
        return "Table";
    }
    @Override
    public boolean testString(String string) {
        // ^T|{}(:org.test.User)?$
        return Pattern.matches("^T\\|\\{.*\\}(:.*)?$", string);
    }
    // T|{}:org.UserInfo
    @Override
    public boolean testObject(Object object) {
        return true;
    }
    @Override
    public Object toObject(String string) throws CastException {
        if (this.testString(string) == false)
            throw new CastException("ԭʼ���ݿ����ܵ��ƻ����߲���һ����ṹ����޷������л��ַ���Ϊ�����͡�");
        else {
            Pattern p = Pattern.compile("^T\\|\\{(.*)\\}(:(.*))?$");
            Matcher m = p.matcher(string);
            m.find();
            StringBuffer context_data = new StringBuffer(m.group(1));//��������
            String userType = (m.groupCount() == 3) ? m.group(3) : null;//�������������е��������͡�
            //1.��ͼװ�������������ͣ�����޷�װ�����е�������ʹ��Map���������
            Object object = null;
            try {
                Class<?> type = Class.forName(userType);
                object = type.newInstance();
            } catch (Exception e) {
                object = new HashMap();
            }
            //2.ѭ��������е�������
            while (true) {
                //�����������һ������֮�󷵻ص�ֵ����null���߿��ַ�����һ����������һ�����������ѭ����
                if (context_data.toString().equals("") == true || context_data == null)
                    break;
                //��ȡ��һ�����ԡ�
                String readStr = this.readString(context_data.toString());
                //����ȡ�����Դ�context_data��ɾ��
                if (context_data.length() == readStr.length())
                    context_data = context_data.delete(0, readStr.length());
                else
                    context_data = context_data.delete(0, readStr.length() + 1);
                //����key-Value
                String key = readStr.substring(0, readStr.indexOf("="));
                String value = readStr.substring(key.length() + 1, readStr.length());
                //���ҿ��Դ�������ַ������ݵ����ͽ��д���
                BaseType bt = BaseType.findType(value);
                if (bt == null)
                    bt = this;
                Object obj = bt.toObject(value);
                //����������ӵ������С�
                this.setValue(object, key, obj);
            }
            //4.���ش�����
            return object;
        }
    }
    @Override
    public String toString(Object object) throws CastException {
        if (this.testObject(object) == false)
            throw new CastException("Ŀ������Ѿ��������������ʹ��������д�������Ͳ��ܴ����Ѿ��д��������Դ���Ķ��󣬱����Ϳ��Դ���Map�����������Ͷ������������Ͷ��������û�������������ʹ��������Դ���ġ�");
        else {
            String result = "";
            String type = (object instanceof Map) ? null : object.getClass().getName();
            //���л�����
            Object[] ns = this.getNS(object);
            for (Object n : ns) {
                Object value = this.getValue(object, n.toString());
                BaseType bt = BaseType.findType(value);
                if (bt == null)
                    bt = this;
                result += n + "=" + bt.toString(value) + ",";
            }
            //�������һ������
            if (ns.length > 0)
                result = result.substring(0, result.length() - 1);
            //
            if (type == null)
                //Ĭ�ϱ�ṹ
                return "T|{" + result + "}";
            else
                //�����Զ�����������
                return "T|{" + result + "}:" + type;
        }
    }
    // ====================================================
    private String readString(String str) {
        String returnS = "";
        int depth = 0;
        //��ȡ�����һ������ֵ
        for (int i = 0; i < str.length(); i++) {
            char s_temp = str.charAt(i);
            if (s_temp == ',' && depth == 0)
                return returnS;
            else if (s_temp == '[' || s_temp == '{')
                depth++;
            else if (s_temp == ']' || s_temp == '}')
                depth--;
            returnS += s_temp;
        }
        return returnS;
    }
    //��ȡĿ���������������ϡ�
    @SuppressWarnings("unchecked")
    private Object[] getNS(Object obj) {
        if (obj instanceof Map) {
            Map obj_map = (Map) obj;
            return obj_map.keySet().toArray();
        } else {
            ArrayList<String> al = new ArrayList<String>();
            for (Field f : obj.getClass().getDeclaredFields())
                al.add(f.getName());
            return al.toArray();
        }
    }
    //��Ŀ���ȡĳ������
    @SuppressWarnings( { "unused", "unchecked" })
    private Object getValue(Object object, String ns) {
        if (object instanceof Map) {
            return ((Map) object).get(ns);
        } else
            try {
                PropertyDescriptor pd = new PropertyDescriptor(ns, object.getClass());
                return pd.getReadMethod().invoke(object);
            } catch (Exception e) {
                return null;
            }
    }
    //��Ŀ������ĳ������
    @SuppressWarnings( { "unused", "unchecked" })
    private void setValue(Object object, String name, Object value) {
        if (object instanceof Map) {
            ((Map) object).put(name, value);
        } else
            try {
                PropertyDescriptor pd = new PropertyDescriptor(name, object.getClass());
                pd.getWriteMethod().invoke(object, value);
            } catch (Exception e) {}
    }
}
