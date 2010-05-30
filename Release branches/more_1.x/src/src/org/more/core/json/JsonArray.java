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
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
/**
 * ������Array���ͻ���Collection���͵�json��ʽ��ת��
 * @version 2010-1-7
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class JsonArray extends JsonMixed {
    protected JsonArray(JsonUtil currentContext) {
        super(currentContext);
    };
    @Override
    public Object toObject(String str) {
        StringBuffer sb = new StringBuffer(str);
        if (sb.charAt(0) == '[') {} else
            throw new JsonException("����һ����Ч��JSON��ʽarrayֵ��");
        //
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        List<Object> al = new LinkedList<Object>();
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
            //��������ַ������ݵ����ͽ��д���
            al.add(this.passJsonObject(readStr));
        }
        return al.toArray();
    }
    @Override
    public String toString(Object bean) {
        StringBuffer json = new StringBuffer('[');
        if (bean instanceof Collection == true) {
            //Collection����
            Collection coll = (Collection) bean;
            for (Object obj : coll)
                this.appendObject(json, obj);
        } else if (bean.getClass().isArray() == true) {
            //Array����
            int length = Array.getLength(bean);
            for (int i = 0; i < length; i++)
                this.appendObject(json, Array.get(bean, i));
        } else
            throw new JsonException("JsonArray���ܽ�һ����Collection���ͻ����������Ͷ���ת��ΪJSON��ʽarrayֵ��");
        /*-----*/
        int index = json.length() - 1;
        if (json.charAt(index) == ',')
            json.deleteCharAt(index);
        json.append(']');
        return json.toString();
    }
    private void appendObject(StringBuffer json, Object var) {
        json.append(this.passJsonString(var));
        json.append(',');
    }
}