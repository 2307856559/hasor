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
package org.more.core.copybean.rw;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.more.core.copybean.PropertyReader;
import org.more.core.copybean.PropertyWrite;
import org.more.util.BeanUtil;
/**
 * Object��д����ʹ�øö�д������ʵ�ִӶ����ж�ȡһ�����Ի���д��һ�����ԡ�
 * �������Զ�дʱֻ֧�ֱ�׼get/set����������Boolean����(��װ����)ֻ֧��get/set��
 * �����boolean����(��������)��ֻ֧��is,setis���ȼ���ֶκ��ⷽ����
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class ObjectRW implements PropertyReader<Object>, PropertyWrite<Object> {
    public List<String> getPropertyNames(Object target) {
        return BeanUtil.getPropertysAndFields(target.getClass());
    };
    public boolean canWrite(String propertyName, Object target, Object newValue) {
        Field field = BeanUtil.getField(propertyName, target.getClass());
        if (field != null)
            return true;
        Method method = BeanUtil.getWriteMethod(propertyName, target.getClass());
        if (method != null)
            return true;
        return false;
    };
    public boolean writeProperty(String propertyName, Object target, Object newValue) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Field field = BeanUtil.getField(propertyName, target.getClass());
        if (field != null) {
            field.set(target, newValue);
            return true;
        }
        Method method = BeanUtil.getWriteMethod(propertyName, target.getClass());
        if (method != null) {
            method.invoke(target, newValue);
            return true;
        }
        return false;
    };
    public boolean canReader(String propertyName, Object target) {
        Field field = BeanUtil.getField(propertyName, target.getClass());
        if (field != null)
            return true;
        Method method = BeanUtil.getReadMethod(propertyName, target.getClass());
        if (method != null)
            return true;
        return false;
    };
    public Object readProperty(String propertyName, Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Field field = BeanUtil.getField(propertyName, target.getClass());
        if (field != null)
            return field.get(target);
        Method method = BeanUtil.getReadMethod(propertyName, target.getClass());
        if (method != null)
            return method.invoke(target);
        return null;
    };
    public Class<?> getTargetClass() {
        return Object.class;
    };
};