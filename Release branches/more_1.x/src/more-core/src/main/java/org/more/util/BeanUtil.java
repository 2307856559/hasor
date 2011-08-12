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
package org.more.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
/**
 * 
 * @version : 2011-6-3
 * @author ������ (zyc@byshell.org)
 */
public abstract class BeanUtil {
    private static final Log log = LogFactory.getLog(BeanUtil.class);
    /**��ȡ����ģ��͸�����Է��ʵ������ֶΡ�*/
    public static Field[] getFields(Class<?> type) {
        if (type == null) {
            log.error("getFields an error , type is null, please check it.");
            return null;
        }
        ArrayList<Field> fList = new ArrayList<Field>();
        for (Field field : type.getDeclaredFields())
            fList.add(field);
        for (Field field : type.getFields())
            if (fList.contains(field) == false)
                fList.add(field);
        Field[] fs = new Field[fList.size()];
        fList.toArray(fs);
        return fs;
    }
    /**��ȡ����ģ��͸�����Է��ʵ����з�����*/
    public static Method[] getMethods(Class<?> type) {
        if (type == null) {
            log.error("getMethods an error , type is null, please check it.");
            return null;
        }
        ArrayList<Method> mList = new ArrayList<Method>();
        for (Method method : type.getDeclaredMethods())
            mList.add(method);
        for (Method method : type.getMethods())
            if (mList.contains(method) == false)
                mList.add(method);
        Method[] ms = new Method[mList.size()];
        mList.toArray(ms);
        return ms;
    }
    /**����ĳ�����ƵĹ����ֶΣ��÷���������һ��������*/
    public static Field findField(String fieldName, Class<?> type) {
        if (fieldName == null || type == null) {
            log.error("fieldName an error , fieldName or type is null, please check it.");
            return null;
        }
        for (Field f : type.getFields())
            if (f.getName().equals(fieldName) == true) {
                log.debug("find method {%0}.", f);
                return f;
            }
        log.debug("{%0} method not exist at {%1}.", fieldName, type);
        return null;
    }
    /**����ĳ�����Ƶķ������÷���������һ��������*/
    public static Method findMethod(String methodName, Class<?> type) {
        if (methodName == null || type == null) {
            log.error("findMethod an error , methodName or type is null, please check it.");
            return null;
        }
        for (Method m : type.getMethods())
            if (m.getName().equals(methodName) == true)
                if (m.getParameterTypes().length == 1) {
                    log.debug("find method {%0}.", m);
                    return m;
                }
        log.debug("{%0} method not exist at {%1}.", methodName, type);
        return null;
    }
    /**ִ������ע�룬����ע��int,short,long,�Ȼ�������֮��÷�����֧��ע��ö�����͡�����ֵ��ʾִ���Ƿ�ɹ���ע�⣺�÷���������������ͽ��г�������ת����*/
    public static boolean writeProperty(Object object, String attName, Object value) {
        if (object == null || attName == null) {
            log.error("putAttribute an error, object or attName is null, please check it.");
            return false;
        }
        //1.���ҷ���
        String methodName = "set" + StringUtil.toUpperCase(attName);
        Class<?> defineType = object.getClass();
        Method writeMethod = findMethod(methodName, defineType);
        if (writeMethod == null) {
            log.debug("can`t invoke {%0} , this method not exist on {%1}", methodName, defineType);
            return false;
        }
        //2.ִ������ת��
        Class<?> toType = writeMethod.getParameterTypes()[0];
        Object attValueObject = StringConvertUtil.changeType(value, toType);
        //3.ִ������ע��
        try {
            writeMethod.invoke(object, attValueObject);
            return true;
        } catch (Exception e) {
            return false;
        }
    };
    /**ִ���ֶ�ע�룬����ע��int,short,long,�Ȼ�������֮��÷�����֧��ע��ö�����͡�ע�⣺�÷���������������ͽ��г�������ת����*/
    public static boolean writeField(Object object, String fieldName, Object value) {
        if (object == null || fieldName == null) {
            log.error("putAttribute an error, object or fieldName is null, please check it.");
            return false;
        }
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Field writeField = findField(fieldName, defineType);
        if (writeField == null) {
            log.debug("can`t invoke {%0} , this field not exist on {%1}", fieldName, defineType);
            return false;
        }
        //2.ִ������ת��
        Class<?> toType = writeField.getType();
        Object attValueObject = StringConvertUtil.changeType(value, toType);
        //3.ִ������ע��
        try {
            writeField.set(object, attValueObject);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**ִ��ע�룬�÷������Ȼ���ͼִ�����Է���ע�롣���ʧ����ִ���ֶ�ע�롣ע�⣺�÷���������������ͽ��г�������ת����*/
    public static boolean writePropertyOrField(Object object, String attName, Object value) {
        if (writeField(object, attName, value) == false)
            if (writeProperty(object, attName, value) == false)
                return false;
        return true;
    }
    /**ִ�����Զ�ȡ��*/
    public static Object readProperty(Object object, String attName) {
        if (object == null || attName == null) {
            log.error("gutAttribute an error, object or attName is null, please check it.");
            return false;
        }
        //1.���ҷ���
        String methodName = "get" + StringUtil.toUpperCase(attName);
        Class<?> defineType = object.getClass();
        Method readMethod = findMethod(methodName, defineType);
        if (readMethod == null) {
            log.debug("can`t invoke {%0} , this method not exist on {%1}", methodName, defineType);
            return null;
        }
        //2.ִ�����Զ�ȡ
        try {
            return readMethod.invoke(object);
        } catch (Exception e) {
            return null;
        }
    };
    /**ִ���ֶζ�ȡ��*/
    public static Object readField(Object object, String fieldName) {
        if (object == null || fieldName == null) {
            log.error("gutAttribute an error, object or fieldName is null, please check it.");
            return null;
        }
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Field readField = findField(fieldName, defineType);
        if (readField == null) {
            log.debug("can`t invoke {%0} , this field not exist on {%1}", fieldName, defineType);
            return null;
        }
        //2.ִ���ֶζ�ȡ
        try {
            return readField.get(object);
        } catch (Exception e) {
            return null;
        }
    }
    /**ִ��ע�룬�÷������Ȼ���ͼִ�����Է���ע�롣���ʧ����ִ���ֶ�ע�롣ע�⣺�÷���������������ͽ��г�������ת����*/
    public static Object readPropertyOrField(Object object, String attName) {
        Object value = readField(object, attName);
        if (value == null)
            value = readProperty(object, attName);
        return value;
    };
};