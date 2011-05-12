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
package org.more.util.attribute;
import java.util.Map;
/**
 * ���Է��ʽӿڣ��ýӿڵĹ������ṩһ�鳣�õ��������ö�ȡ������
 * @version 2009-4-28
 * @author ������ (zyc@byshell.org)
 */
public interface IAttribute {
    /**
     * ����ĳ�������Ƿ���ڡ�
     * @param name �����Ե�������
     * @return ����ĳ�������Ƿ���ڡ�
     */
    public boolean contains(String name);
    /**
     * �������ԣ���������Ѿ��������滻ԭ�����ԡ�
     * @param name Ҫ�������������
     * @param value Ҫ���������ֵ��
     */
    public void setAttribute(String name, Object value);
    /**
     * �����Լ����л��һ�����ԣ������ͼ��õ����Բ����ڷ���null
     * @param name Ҫ��õ���������
     * @return ��������ֵ��������������򷵻�null��
     */
    public Object getAttribute(String name);
    /**
     * ���������Լ�����ɾ��ָ�����ԡ�
     * @param name Ҫɾ�����������ơ�
     */
    public void removeAttribute(String name);
    /** @return �����������Ե����Ƽ��� */
    public String[] getAttributeNames();
    /** ����������ԡ� */
    public void clearAttribute();
    /**��{@link IAttribute}ת��ΪMap��ʽ��*/
    public Map<String, Object> toMap();
}