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
/**
 * ��������д������ͨ���ýӿڿ��Խ�һ��ֵд�뵽bean�С�
 * {@link #getTargetClass()}�������ظö�ȡ����֧�ֵ����͡�
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public interface PropertyWrite<T> {
    /**
     * ��ȡĿ������ֵ���÷���Ӧ��������ʵ�֡�
     * @param propertyName Ҫд�������
     * @param target Ҫд���Ŀ��bean
     * @param newValue д�����ֵ
     * @return ����Ŀ������ֵ���÷���Ӧ��������ʵ�֡�
     */
    public boolean writeProperty(String propertyName, T target, Object newValue) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    /**
     * ���Ը����Զ�ȡ���Ƿ�֧��д���������֧�ַ���true���򷵻�false��
     * @param propertyName Ҫд�������
     * @param target Ҫд���Ŀ��bean
     * @param newValue д�����ֵ
     * @return ���ز��Ը����Զ�д���Ƿ�֧�ֶ����������֧�ַ���true���򷵻�false��
     */
    public boolean canWrite(String propertyName, T target, Object newValue);
    /** ������Զ�ȡ����֧�ֵ�bean���͡�*/
    public Class<?> getTargetClass();
}