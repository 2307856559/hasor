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
 * null��more���л��齨�Ļ�������֮һ����NullType����Ǵ���ֵΪ�յ�����ת����
 * �����κ�����ֻҪֵΪ����Ϳ���ʹ�ø����ͽ����������л��ͷ����л���
 * �����͵�ԭʼ�������ǡ�[More Void]����
 * Date : 2009-7-7
 * @author ������
 */
public final class NullType extends BaseType {
    NullType() {}
    @Override
    protected String getShortOriginalName() {
        return "Void";
    }
    @Override
    public boolean testString(String string) {
        // ^V|void$
        return Pattern.matches("^V\\|void$", string);
    }
    @Override
    public boolean testObject(Object object) {
        //��������ǿ�ֵ�򷵻�true���򷵻�false
        return (object == null) ? true : false;
    }
    @Override
    public Object toObject(String string) throws CastException {
        //���������Ƿ���ȷ
        if (this.testString(string) == false)
            throw new CastException("�޷���ת���ݣ�ԭʼ���ݸ�ʽ������߲��������޷������л��ַ���Ϊvoid���͡�");
        return null;
    }
    @Override
    public String toString(Object object) throws CastException {
        //���������Ƿ���ȷ
        if (this.testObject(object) == false)
            throw new CastException("����ת���쳣�����ܽ��ǿյ�����ת���ɿյ����͡�");
        return "V|void";
    }
}
