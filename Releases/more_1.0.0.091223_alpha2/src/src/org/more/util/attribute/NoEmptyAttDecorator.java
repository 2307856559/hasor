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
/**
 * ����װ��������װ������Ŀ������������ֵ���벻��Ϊ�ա�
 * Date : 2009-5-1
 * @author ������
 */
public class NoEmptyAttDecorator extends AbstractAttDecorator {
    //==================================================================================Constructor
    /**
     * ��������װ��������װ������Ŀ������������ֵ���벻��Ϊ�ա�
     * @param source Ҫװ�ε�Ŀ�����Զ���
     * @throws NullPointerException �����ͼ����һ����ֵ��װ�������������쳣��
     */
    public NoEmptyAttDecorator(IAttribute source) throws NullPointerException {
        super(source);
    }
    //==========================================================================================Job
    /**
     * �������ԣ��÷���װ����ԭ���������÷�������������������ֵ���벻��Ϊ�ա�
     * @param name Ҫ�������������
     * @param value Ҫ���������ֵ��
     * @throws NullPointerException �������쳣��ʾ��ͼ����һ��������ֵ�����Լ����С�
     */
    @Override
    public void setAttribute(String name, Object value) throws NullPointerException {
        if (value == null)
            throw new NullPointerException("�����������ֵ��");
        else
            super.setAttribute(name, value);
    }
}