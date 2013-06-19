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
package org.more.hypha.el;
/**
 * ���԰��������ͨ��������Զ����Խ��ж�д������
 * Date : 2011-4-11
 * @author ������ (zyc@byshell.org)
 */
public interface PropertyBinding {
    /**��������EL�����һ�ȡ����֮�������ֵ��*/
    public Object getValue() throws ELException;
    /**
     * ��������EL����һ���µ�ֵ�滻ԭ������ֵ���÷������������۳ɹ���񶼽�
     * ����ֻ���������������д��ɹ���isReadOnly��������Ϊfalse�����򷵻�true��
     */
    public void setValue(Object value) throws ELException;
    /**��ȡ���ڱ�ʾ���Ե�EL���ʽ��*/
    public String getPropertyEL();
};