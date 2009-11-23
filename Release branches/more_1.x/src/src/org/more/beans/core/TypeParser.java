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
package org.more.beans.core;
import java.lang.reflect.Method;

import org.more.beans.core.propparser.MainPropertyParser;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProperty;
/**
 * ���Խ���������bean��fact��ʽִ��ע��ʱ��ע����̿��ܻ�ִ�и��෽����������Ա������������ࡣ
 * Date : 2009-11-8
 * @author ������
 */
public class TypeParser {
    /**����һ����������Ϊ����*/
    public static Object passerType(String methodName, Object object, Object[] getBeanParam, BeanDefinition definition, ResourceBeanFactory context, BeanProperty prop) throws Throwable {
        MainPropertyParser parser = context.getPropParser();
        Class<?> type = parser.parserType(object, getBeanParam, prop.getRefValue(), prop, definition);
        Method method = object.getClass().getMethod(methodName, type);
        Object value = parser.parser(object, getBeanParam, prop.getRefValue(), prop, definition);
        return method.invoke(object, value);
    }
}