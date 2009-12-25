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
package org.more.core.serialization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.CastException;
/**
 * �������ͣ���������more���������еĻ�������֮һ����Ϊ����������java����Ҫ�����ֱ�����ʽ��
 * һ�����������ͣ���һ����Collection�ӿڶ���Collection�ӿڶ��������List,Set,Queue����
 * �����л��������Ͷ���ʱArrayType����Զ���more���л�ϵͳ�в��Ҽ�����ӦԪ�ص����ͽ��ж�Ӧ���͵�
 * ���л�����more���л�ϵͳ�м������͵�ԭʼ�������ǡ�[More Array]����
 * Date : 2009-7-7
 * @author ������
 */
public final class ArrayType extends BaseType {
    ArrayType() {}
    @Override
    protected String getShortOriginalName() {
        return "Array";
    }
    @Override
    public boolean testString(String string) {
        // ^A|[.*]$
        return Pattern.matches("^A\\|\\[.*\\](:.*)?$", string);
    }
    @Override
    public boolean testObject(Object object) {
        if (object == null)
            return false;
        else
            return object.getClass().isArray() || Collection.class.isAssignableFrom(object.getClass());
    }
    @Override
    @SuppressWarnings("unchecked")
    public Object toObject(String string) throws CastException {
        if (this.testString(string) == false)
            throw new CastException("�޷�ִ�з����л����ڼ�����ݸ�ʽʱʧ�ܣ��������л��������ݲ��Ǽ������ͻ������л����ݲ�������");
        else {
            Pattern p = Pattern.compile("^A\\|\\[(.*)\\](:(.*))?$");
            Matcher m = p.matcher(string);
            m.find();
            StringBuffer context_data = new StringBuffer(m.group(1));//��������
            String userType = (m.groupCount() == 3) ? m.group(3) : null;//�������������е��������͡�
            //1.��ͼװ�������������ͣ�����޷�װ�����е�������ʹ��Map���������
            Collection<Object> al = null;
            if ("A".equals(userType))
                al = new ArrayList<Object>();
            else {
                try {
                    al = (Collection<Object>) Class.forName(userType).newInstance();
                } catch (Exception e) {
                    al = new ArrayList<Object>();
                }
            }
            //2.ѭ���������е�������
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
                //���ҿ��Դ�������ַ������ݵ����ͽ��д���
                Object obj = super.findType(readStr).toObject(readStr);
                //����������ӵ������С�
                al.add(obj);
            }
            //���ش���Ľ����
            if ("A".equals(userType))
                return al.toArray();
            else
                return al;
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public String toString(Object object) throws CastException {
        if (this.testObject(object) == false)
            throw new CastException("����ִ��ת����Ŀ��������Ͳ���һ����Ч�ļ������ͣ��Ϸ��ļ����������������Collection�ӿڵ�ʵ���ࡣ");
        else {
            String result = "";//���ڴ�����л����
            Collection al = null;//���ڴ����Ҫ���л������ݣ���Щ���ݱ���װ�������н��е�����
            String type = object.getClass().getName();
            if (object.getClass().isArray() == true) {
                //��������������������뼯���еȴ�����
                al = new ArrayList<Object>();
                Object[] objs = (Object[]) object;
                for (Object o : objs)
                    al.add(o);
                type = "A";
            } else
                //����Ǽ�����ֱ��ת��
                al = (Collection) object;
            //������ĩβ�Ķ���
            for (Object obj : al)
                result += BaseType.findType(obj).toString(obj) + ",";
            //ƴ�����л����ݲ��ҷ��ء�
            if (result.length() > 0)
                result = result.substring(0, result.length() - 1);
            return "A|[" + result + "]:" + type;
        }
    }
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
}
