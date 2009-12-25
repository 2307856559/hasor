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
/**
 * boolean�������ݣ�more���л��齨�Ļ�������֮һ��more�����л�������û�а�װ���͵ĸ��
 * �κ�Ϊ�յ����Ͷ��ǿ����͡���BooleanType���ʾ����һ���ض���������ֵ���ֵ������trueҲ������false��
 * more���л��齨�е�ԭʼ�������ǡ�[More Boolean]����
 * Date : 2009-7-7
 * @author ������
 */
public final class BooleanType extends BaseType {
    BooleanType() {}
    @Override
    protected String getShortOriginalName() {
        return "Boolean";
    }
    @Override
    public boolean testString(String string) {
        // ^B|(true|false)$
        return Pattern.matches("^B\\|(true|false)$", string);
    }
    @Override
    public boolean testObject(Object object) {
        //���������Ƿ�Ϊboolean�����Ҷ�����Ϊnull��
        if (object == null || object instanceof Boolean == false)
            return false;
        else
            return true;
    }
    @Override
    public Object toObject(String string) throws CastException {
        //�������ݸ�ʽ
        if (this.testString(string) == false)
            throw new CastException("�޷���ת���ݣ�ԭʼ���ݸ�ʽ������߲��������޷������л��ַ���ΪBoolean���͡�");
        else if (string.indexOf("true") != -1)
            //���ֵ�а���true�����true
            return true;
        else
            return false;
    }
    @Override
    public String toString(Object object) throws CastException {
        if (this.testObject(object) == false)
            throw new CastException("����ִ��ת����Ŀ���ʽ����һ����Ч��Boolean���Ͷ�����˲���ʹ��BooleanType��������л���");
        else
            return "B|" + object;
    }
}
