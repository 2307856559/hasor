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
import org.more.CastException;
/**
 * more���л��ͷ����л������࣬ͨ��ʹ�ø��ྲ̬��������ʵ�ֽ��������������л�Ϊmore�ַ�����
 * Ҳ����ͨ�������toObject�������ַ��������л�Ϊ����
 * Date : 2009-7-8
 * @author ������
 */
public class MoreSerialization {
    /**
     * ���������л����������л�֮���more�ַ�����
     * @param object Ҫ���л��Ķ���
     * @return �������л�֮���more�ַ�����
     * @throws CastException �����л������з����쳣��
     */
    public static String toString(Object object) throws CastException {
        BaseType type = BaseType.findType(object);
        if (type == null)
            throw new CastException(object.getClass() + "����һ����֧�ֵ����͡�More�޷����л�������͵Ķ���");
        else
            return type.toString(object);
    }
    /**
     * ��more���л��ַ��������л�Ϊ���󡣷��ط����л�֮��Ķ���
     * @param data �������л��ַ���
     * @return ���ط����л�֮��Ķ���
     * @throws CastException �ڷ����л������з����쳣��
     */
    public static Object toObject(String data) throws CastException {
        BaseType type = BaseType.findType(data);
        if (type == null) {
            String shortString = (data.length() > 20) ? data.substring(0, 20) + "..." : data;
            throw new CastException("����More��û��ƥ��Ĵ�����������More�޷������л�����ַ���[" + shortString + "]");
        } else
            return type.toObject(data);
    }
}
