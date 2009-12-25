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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.CastException;
/**
 * �û��Զ������ͣ���������Ϊ�˽���ض��������л���������ʱ�������;���һ���û��Զ������ͣ�
 * �û��Զ������ͱ���ָ�����͡��û��Զ������Ͷ����ԭʼ�������ǡ�[More User_xxx]��
 * Date : 2009-7-8
 * @author ������
 */
public abstract class UserType extends BaseType {
    @Override
    protected String getShortOriginalName() {
        return "User_" + this.getUserOriginalName();
    }
    @Override
    public boolean testString(String string) {
        // ^U|{}:org.test.User$
        return Pattern.matches("^U\\|\\{.*\\}:" + this.getType() + "$", string);
    }
    @Override
    public Object toObject(String string) throws CastException {
        if (this.testString(string) == false)
            throw new CastException("ԭʼ���ݿ����ܵ��ƻ����߲���һ����ṹ����޷������л��ַ���Ϊ�û��Զ������͡�");
        else {
            Pattern p = Pattern.compile("^U\\|\\{(.*)\\}:(.*)$");
            Matcher m = p.matcher(string);
            m.find();
            String date = m.group(1);
            String type = m.group(2);
            return this.deserialize(date, type);
        }
    }
    @Override
    public String toString(Object object) throws CastException {
        if (this.testObject(object) == false)
            throw new CastException("Ŀ������Ѿ��������������ʹ��������д�������Ͳ��ܴ����Ѿ��д��������Դ���Ķ����û��Զ������ͱ���������ƽ̨���Ѿ�������������͡�");
        else
            return "U|{" + this.serialization(object) + "}:" + this.getType();
    }
    // ====================================================
    /**
     * �÷����Ǳ������ķ������÷����������͵ľ������������÷����ķ���ֵ����getShortOriginalName���á�
     * getShortOriginalName�������Զ��������ԭʼ����
     * @return �������͵ľ�����������
     */
    protected abstract String getUserOriginalName();
    /**
     * ��ȡ��������͵�ƥ���������ԣ����������������л�ʱ���׷�ӵ����л���������Ͳ��֣�Ҳ�����ڷ����л�ʱ��ƥ���������͡�
     * @return ���ض�������ͣ����������������л�ʱ���׷�ӵ����л���������Ͳ��֡�
     */
    protected abstract String getType();
    /**
     * ���л����󡣸÷���ֱ�����л������Ϊ�ַ����Ϳ��Բ��迼�����л�֮����������Զ������������еĸ�ʽ���⡣
     * @param object Ҫ���л��Ķ���
     * @return �������л������
     * @throws CastException ��ִ������ת��ʱ�����쳣��
     */
    protected abstract String serialization(Object object) throws CastException;
    /**
     * ִ�з����л����̡�
     * @param date Ҫ�����л����ַ������ݡ�
     * @param type ������ݵ�ԭʼ�������͡�
     * @return ���ط����л�֮��Ľ����
     * @throws CastException ��ִ������ת��ʱ�����쳣��
     */
    protected abstract Object deserialize(String date, String type) throws CastException;
}
