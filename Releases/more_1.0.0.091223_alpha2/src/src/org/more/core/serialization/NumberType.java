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
import java.util.regex.Pattern;
import org.more.CastException;
import org.more.util.StringConvert;
/**
 * �������ͣ���������more���л����͵Ļ�������֮һ����more��������byte����int��long��float�ȶ�ͳһ�����������͡�
 * more��ת����������ʱ�������ȿ��������ֵ�������͡�������֤�ڱ���о����ܵ�ʹ��ϵͳĬ����������ת������ʹ����ʧ���ȵ�ǿ����ת����
 * more���л��齨֧�����¼������ݱ�ʾ��ʽ-12��13��+56.5��.234��+.159��-1.2����23.����������more���л��齨����Ϊ�Ǵ�������ݸ�ʽ��
 * �������͵�ԭʼ�������ǡ�[More Number]��ע��ʹ��testObject��toStringʱ�������봫��Number���͵����ݶ��������ַ������������͡�
 * ���ҵ�ǰ�汾more���л��齨��֧�ֿ�ѧ��������ʾ�����֡�
 * Date : 2009-7-7
 * @author ������
 */
public final class NumberType extends BaseType {
    NumberType() {}
    @Override
    protected String getShortOriginalName() {
        return "Number";
    }
    @Override
    public boolean testString(String string) {
        // ^N\|(\+|-)?\d{0,}(\.\d+){0,}$
        //�涨�ַ�����ʽ
        if (Pattern.matches("^N\\|(\\+|-)?\\d{0,}(\\.\\d+){0,}$", string) == true)
            //������ N| �����������
            if (Pattern.matches("^N\\|.+$", string) == true)
                return true;
        return false;
    }
    @Override
    public boolean testObject(Object object) {
        return (object instanceof Number) ? true : false;
    }
    @Override
    public Object toObject(String string) throws CastException {
        //�������ݸ�ʽ
        if (this.testString(string) == false)
            throw new CastException("�޷������л����֣�ԭʼ���ݿ��ܲ������������ʹ���");
        else
            return StringConvert.parseNumber(string.substring(2), 0);
    }
    @Override
    public String toString(Object object) throws CastException {
        //�������ݸ�ʽ
        if (this.testObject(object) == false)
            throw new CastException("����ִ�����л����̣�Ŀ�������һ����Ч��Number���͡�");
        else
            return "N|" + object;
    }
}
