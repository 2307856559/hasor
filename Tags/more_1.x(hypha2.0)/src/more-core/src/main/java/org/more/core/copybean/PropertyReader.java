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
package org.more.core.copybean;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
/**
 * �������Զ�ȡ����ͨ���ýӿڿ��Խ�bean�ϵ�ĳ�����Զ�ȡ������
 * {@link #getTargetClass()}�������ظö�ȡ����֧�ֵ����͡�
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public interface PropertyReader<T> {
    /**��ȡ��������*/
    public List<String> getPropertyNames(T target);
    /**
     * ��ȡĿ������ֵ���÷���Ӧ��������ʵ�֡�
     * @return ����Ŀ������ֵ���÷���Ӧ��������ʵ�֡�
     */
    public Object readProperty(String propertyName, T target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    /**����һ��boolean����ֵ��ʾ�˸������Ƿ���Ա�����*/
    public boolean canReader(String propertyName, T target);
    /** ������Զ�ȡ����֧�ֵ�bean���͡�*/
    public Class<?> getTargetClass();
}