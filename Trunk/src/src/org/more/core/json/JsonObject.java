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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.more.util.attribute.IAttribute;
/**
 * ������Object���͵�json��ʽ��ת��
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class JsonObject extends JsonMixed {
    @Override
    public Object toObject(String str) {
        StringBuffer sb = new StringBuffer(str);
        if (sb.charAt(0) == '{') {} else
            throw new JsonException("����һ����Ч��JSON��ʽObjectֵ��");
        //
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        Map<Object, Object> map = new HashMap<Object, Object>();
        //ѭ���������е�������
        while (true) {
            //�����������һ������֮�󷵻ص�ֵ����null���߿��ַ�����һ����������һ�����������ѭ����
            if (sb.toString().equals("") == true || sb == null)
                break;
            //��ȡ��һ�����ԡ�
            String readStr = this.readJSONString(sb.toString());
            //����ȡ�����Դ�context_data��ɾ��
            if (sb.length() == readStr.length())
                sb = sb.delete(0, readStr.length());
            else
                sb = sb.delete(0, readStr.length() + 1);
            //����key-Value
            int firstIndex = readStr.indexOf(':');
            String key = readStr.substring(0, firstIndex);
            String value = readStr.substring(firstIndex + 1);
            //��������ַ������ݵ����ͽ��д���
            map.put(key, this.passJsonObject(value));
        }
        return map;
    }
    @Override
    public String toString(Object bean) {
        StringBuffer json = new StringBuffer("{");
        if (bean instanceof IAttribute) {
            IAttribute att = (IAttribute) bean;
            String[] ns = att.getAttributeNames();
            for (int i = 0; i < ns.length; i++)
                this.appendObject(json, ns[i], att.getAttribute(ns[i]));
        } else if (bean instanceof Map) {
            Map map = (Map) bean;
            Set<?> ns = map.keySet();
            for (Object key : ns)
                this.appendObject(json, key, map.get(key));
        } else {
            Class<?> type = bean.getClass();
            Field[] fs = type.getFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                try {
                    int access = f.getModifiers();
                    if ((access | Modifier.PUBLIC) == access)
                        this.appendObject(json, f.getName(), f.get(i)); //ֱ�ӷ����ֶ�
                    else {
                        //ת������ĸ��д
                        StringBuffer sb = new StringBuffer(f.getName());
                        char firstChar = sb.charAt(0);
                        sb.delete(0, 1);
                        sb.insert(0, (char) ((firstChar >= 97) ? firstChar - 32 : firstChar));
                        sb.insert(0, "get");
                        //ͨ��get/set����
                        Method m = type.getMethod(sb.toString());
                        this.appendObject(json, f.getName(), m.invoke(bean));
                    }
                } catch (Exception e) {}
            }
        }
        /*-----*/
        json.append("}");
        return json.toString();
    }
    private void appendObject(StringBuffer json, Object key, Object var) {
        json.append(this.passJsonString(key));
        json.append(":");
        json.append(this.passJsonString(var));
    }
}