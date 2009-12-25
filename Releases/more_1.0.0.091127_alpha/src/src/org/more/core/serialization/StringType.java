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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.CastException;
import org.more.util.Base64;
/**
 * �ַ������ͣ���more�����л��齨��֧�ֵ��������ַ������ַ���һ�����Ͷ����ַ�����ֻ���ַ������Ȳ�һ�¶��ѡ�
 * �ַ���������more���л��齨��ԭʼ�������ǡ�[More String]����more���Խ����¶���ֱ�����л�Ϊ�ַ�����
 * String��Character��StringBuffer��StringBuilder����more���л��ַ���ʱ�Ὣ�ַ�������base64����Ȼ���װ��
 * base64�ַ����������Ƿ��������ַ��ƻ����л���ʽ���ȷ�˵�س����У��Ʊ���Ͷ����Լ����ŵȳ������š�
 * Date : 2009-7-7
 * @author ������
 */
public final class StringType extends BaseType {
    StringType() {}
    @Override
    protected String getShortOriginalName() {
        return "String";
    }
    @Override
    public boolean testString(String string) {
        // ^S|".*"$
        return Pattern.matches("^S\\|\".*\"$", string);
    }
    @Override
    public boolean testObject(Object object) {
        if (object == null)
            //null
            return false;
        else if (object instanceof Character)
            //string|char
            return true;
        else if (object instanceof CharSequence)
            //string|char buffer,string
            return true;
        else
            return false;
    }
    @Override
    public Object toObject(String string) throws CastException {
        if (this.testString(string) == false)
            throw new CastException("�޷������л��ַ����������ݣ��������л����ݲ������������л����ݲ����ַ������͡�");
        Pattern p = Pattern.compile("^S\\|\"(.*)\"$");
        Matcher m = p.matcher(string);
        m.find();
        return Base64.base64Decode(m.group(1));
    }
    @Override
    public String toString(Object object) throws CastException {
        if (this.testObject(object) == false)
            throw new CastException("�޷����л�Ŀ�������ȷ�����л��Ķ�������������֮һ��String��Character��StringBuffer��StringBuilder�����ұ�֤���ݲ���Ϊnull��");
        else
            return "S|\"" + Base64.base64Encode(object.toString()) + "\"";
    }
}
