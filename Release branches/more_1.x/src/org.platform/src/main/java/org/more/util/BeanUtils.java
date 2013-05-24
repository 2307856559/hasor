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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.more.classcode.EngineToos;
import org.more.classcode.RootClassLoader;
import org.more.core.error.InvokeException;
/**
 * 
 * @version : 2011-6-3
 * @author ������ (zyc@byshell.org)
 */
public abstract class BeanUtils {
    /**ͨ��λ�������data�Ƿ���modifiers�*/
    public static boolean checkModifiers(int modifiers, int data) {
        int a = modifiers | data;
        return a == data;
    };
    /**��ȡһ��������ֽ���Ķ�ȡ����*/
    public static InputStream getClassInputStream(Class<?> target) {
        ClassLoader loader = target.getClassLoader();
        if (loader instanceof RootClassLoader) {
            byte[] data = ((RootClassLoader) loader).toBytes(target);
            return new ByteArrayInputStream(data);
        }
        String classResourceName = target.getName().replace(".", "/") + ".class";
        if (loader != null)
            return loader.getResourceAsStream(classResourceName);
        else
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(classResourceName);
    };
    /**��������Ƿ�Ϊһ���������ͻ����װ���ͣ��������Ͱ�����boolean, byte, char, short, int, long, float, �� double*/
    public static boolean isBaseType(Class<?> target) {
        /*�ж��Ƿ�Ϊ��������*/
        if (target.isPrimitive() == true)
            return true;
        /*�жϸ��ְ�װ����*/
        if (target == Boolean.class)
            return true;
        if (target == Byte.class)
            return true;
        if (target == Character.class)
            return true;
        if (target == Short.class)
            return true;
        if (target == Integer.class)
            return true;
        if (target == Long.class)
            return true;
        if (target == Float.class)
            return true;
        if (target == Double.class)
            return true;
        return false;
    }
    /**��ȡָ�����͵�Ĭ��ֵ��*/
    public static Object getDefaultValue(Class<?> returnType) {
        if (returnType == null)
            return null;
        else if (returnType == int.class || returnType == Integer.class)
            return 0;
        else if (returnType == byte.class || returnType == Byte.class)
            return 0;
        else if (returnType == char.class || returnType == Character.class)
            return ' ';
        else if (returnType == double.class || returnType == Double.class)
            return 0d;
        else if (returnType == float.class || returnType == Float.class)
            return 0f;
        else if (returnType == long.class || returnType == Long.class)
            return 0l;
        else if (returnType == short.class || returnType == Short.class)
            return 0;
        else if (returnType == boolean.class || returnType == Boolean.class)
            return false;
        else if (returnType == void.class || returnType == Void.class)
            return null;
        else if (returnType.isArray() == true)
            return null;
        else
            return null;
    };
    /**��������Ƿ�Ϸ���*/
    public static boolean checkClassName(String className) {
        if (className == null || className.equals(""))
            return false;
        String item[] = { "..", "!", "@", "#", "%", "^", "&", "*", "(", ")", "-", "=", "+", "{", "}", ";", ";", "\"", "'", "<", ">", ",", "?", "/", "`", "~", " ", "\\", "|" };
        for (int i = 0; i <= item.length - 1; i++)
            if (className.indexOf(item[i]) >= 0)
                return false;
        if (className.indexOf(".") == 0)
            return false;
        if (className.indexOf(".", className.length()) == className.length())
            return false;
        return true;
    }
    /**�ж�ĳ�����Ƿ�Ϊһ��lang�����ࡣ*/
    public static boolean isLangClass(Class<?> target) {
        return target.getName().startsWith("java.lang.");
    };
    /**��ȡ�������޶������������֡�*/
    public static String getSimpleAtFull(String fullName) {
        String[] ns = fullName.split("\\.");
        return ns[ns.length - 1];
    }
    /**��ȡ�������޶����İ������֡�*/
    public static String getPackageAtFull(String fullName) {
        if (fullName.lastIndexOf(".") > 0)
            return fullName.substring(0, fullName.lastIndexOf("."));
        else
            return fullName;
    }
    /**��ȡ�����ı�ʶ���룬�ڲ������������������¡�*/
    public static String getCodeWithoutClass(Method method) {
        //public void addChild(org.noe.safety.services.SYS_TB_MenuTree)
        StringBuffer str = new StringBuffer(method.toString());
        String declaringClass = method.getDeclaringClass().getName();
        int indexStart = str.indexOf(declaringClass);
        str.delete(indexStart, indexStart + declaringClass.length() + 1);
        return str.toString();
    }
    /**
     * �÷����������Ƿ������ʽ����Ŀ��ķ�����
     * @param target �����õĶ���
     * @param methodName Ҫ���õķ��䷽������
     * @param objects �����б�
     */
    public static Object invokeMethod(Object target, String methodName, Object... objects) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (target == null)
            return null;
        Class<?> targetType = target.getClass();
        Method invokeMethod = null;
        //������÷���
        Method[] ms = targetType.getMethods();
        for (Method m : ms) {
            //1.���ֲ���ȵĺ���
            if (m.getName().equals(methodName) == false)
                continue;
            //2.Ŀ�귽�������б������types�ֶ��д�ŵĸ�����һ���ĺ��ԡ�
            Class<?>[] paramTypes = m.getParameterTypes();
            if (paramTypes.length != objects.length)
                continue;
            //3.����в������Ͳ�һ����Ҳ����---1
            boolean isFind = true;
            for (int i = 0; i < paramTypes.length; i++) {
                Object param_object = objects[i];
                if (param_object == null)
                    continue;
                //
                if (paramTypes[i].isAssignableFrom(param_object.getClass()) == false) {
                    isFind = false;
                    break;
                }
            }
            //5.����в������Ͳ�һ����Ҳ����---2
            if (isFind == false)
                continue;
            //��������ִ�е���
            invokeMethod = m;
        }
        if (invokeMethod == null)
            throw new InvokeException("�޷�����Ŀ�귽��[" + methodName + "]��");
        else
            return invokeMethod.invoke(target, objects);
    }
    /*----------------------------------------------------------------------------------------*/
    /**��ȡ�ඨ����ֶκͼ̳и����ж�����ֶ��Լ�����ĸ��ࣨ�������¶���ͬ���ֶ�Ҳ�ᱻ���뼯�ϣ���*/
    public static List<Field> findALLFields(Class<?> target) {
        if (target == null)
            return null;
        ArrayList<Field> fList = new ArrayList<Field>();
        findALLFields(target, fList);
        return fList;
    }
    private static void findALLFields(Class<?> target, ArrayList<Field> fList) {
        if (target == null)
            return;
        for (Field field : target.getDeclaredFields())
            if (fList.contains(field) == false)
                fList.add(field);
        for (Field field : target.getFields())
            if (fList.contains(field) == false)
                fList.add(field);
        Class<?> superType = target.getDeclaringClass();
        if (superType == null || superType == target)
            return;
        findALLFields(superType, fList);
    }
    /**{@link #findALLFields(Class))}��������ֵ�����ظ��汾��*/
    public static List<Field> findALLFieldsNoRepeat(Class<?> target) {
        if (target == null)
            return null;
        LinkedHashMap<String, Field> fMap = new LinkedHashMap<String, Field>();
        findALLFieldsNoRepeat(target, fMap);
        return new ArrayList<Field>(fMap.values());
    }
    private static void findALLFieldsNoRepeat(Class<?> target, LinkedHashMap<String, Field> fMap) {
        if (target == null)
            return;
        for (Field field : target.getDeclaredFields())
            if (fMap.containsKey(field.getName()) == false)
                fMap.put(field.getName(), field);
        for (Field field : target.getFields())
            if (fMap.containsKey(field.getName()) == false)
                fMap.put(field.getName(), field);
        Class<?> superType = target.getDeclaringClass();
        if (superType == null || superType == target)
            return;
        findALLFieldsNoRepeat(superType, fMap);
    }
    /**��ȡ�ඨ��ķ����ͼ̳и����ж���ķ����Լ�����ĸ��ࣨ�������д����Ҳ�ᱻ���أ���*/
    public static List<Method> findALLMethods(Class<?> target) {
        if (target == null)
            return null;
        ArrayList<Method> mList = new ArrayList<Method>();
        findALLMethods(target, mList);
        return mList;
    }
    private static void findALLMethods(Class<?> target, ArrayList<Method> mList) {
        if (target == null)
            return;
        for (Method method : target.getDeclaredMethods())
            if (mList.contains(method) == false)
                mList.add(method);
        for (Method method : target.getMethods())
            if (mList.contains(method) == false)
                mList.add(method);
        Class<?> superType = target.getDeclaringClass();
        if (superType == null || superType == target)
            return;
        findALLMethods(superType, mList);
    }
    /**{@link #findALLMethods(Class))}��������ֵ�����ظ��汾��*/
    public static List<Method> findALLMethodsNoRepeat(Class<?> target) {
        if (target == null)
            return null;
        LinkedHashMap<String, Method> mMap = new LinkedHashMap<String, Method>();
        findALLMethodsNoRepeat(target, mMap);
        return new ArrayList<Method>(mMap.values());
    }
    private static void findALLMethodsNoRepeat(Class<?> target, LinkedHashMap<String, Method> mMap) {
        if (target == null)
            return;
        for (Method method : target.getDeclaredMethods()) {
            String fullDesc = getCodeWithoutClass(method);
            if (mMap.containsKey(fullDesc) == false)
                mMap.put(fullDesc, method);
        }
        for (Method method : target.getMethods()) {
            String fullDesc = getCodeWithoutClass(method);
            if (mMap.containsKey(fullDesc) == false)
                mMap.put(fullDesc, method);
        }
        Class<?> superType = target.getDeclaringClass();
        if (superType == null || superType == target)
            return;
        findALLMethodsNoRepeat(superType, mMap);
    }
    /*----------------------------------------------------------------------------------------*/
    /**����һ���ɲ������ֶ��б�*/
    public static List<Field> getFields(Class<?> type) {
        ArrayList<Field> fList = new ArrayList<Field>();
        for (Field field : type.getFields())
            if (fList.contains(field) == false)
                fList.add(field);
        return fList;
    }
    /**����һ���ɲ����ķ����б�*/
    public static List<Method> getMethods(Class<?> type) {
        ArrayList<Method> mList = new ArrayList<Method>();
        for (Method method : type.getMethods())
            if (mList.contains(method) == false)
                mList.add(method);
        return mList;
    }
    /**����һ���ɲ������ֶΡ�*/
    public static Field getField(String fieldName, Class<?> type) {
        if (fieldName == null || type == null)
            return null;
        for (Field f : type.getFields())
            if (f.getName().equals(fieldName) == true)
                return f;
        return null;
    }
    /**����һ���ɲ����ķ�����*/
    public static Method getMethod(Class<?> atClass, String name, Class<?>[] paramType) {
        try {
            return atClass.getMethod(name, paramType);
        } catch (Exception e) {
            try {
                return atClass.getDeclaredMethod(name, paramType);
            } catch (Exception e1) {
                return null;
            }
        }
    }
    /**��ȡ���������ϣ��÷�����{@link #getPropertys(Class)}�����������棬ͨ���÷���������ͬʱ���ؿɷ��ʵ��ֶ���Ϊ���ԡ�*/
    public static List<String> getPropertysAndFields(Class<?> target) {
        List<String> mnames = getPropertys(target);
        List<Field> fnames = getFields(target);
        for (Field f : fnames) {
            String fName = f.getName();
            if (mnames.contains(fName) == false)
                mnames.add(fName);
        }
        return mnames;
    }
    /**��ȡ���������ϣ������������Կ�����Щֻ��ֻ�����ԣ���Щ��ֻд���ԡ�Ҳ�ж�д���ԡ�*/
    public static List<String> getPropertys(Class<?> target) {
        List<String> mnames = new ArrayList<String>();
        List<Method> ms = getMethods(target);
        for (Method m : ms) {
            String name = m.getName();
            if (name.startsWith("get") == true || name.startsWith("set") == true)
                name = name.substring(3);
            else if (name.startsWith("is") == true)
                name = name.substring(2);
            else
                continue;
            if (name.equals("") == false) {
                name = StringUtils.toLowerCase(name);
                if (mnames.contains(name) == false)
                    mnames.add(name);
            }
        }
        return mnames;
    }
    /**��ȡһ�����ԵĶ�ȡ������*/
    public static Method getReadMethod(String property, Class<?> target) {
        if (property == null || target == null)
            return null;
        String methodName_1 = "get" + StringUtils.toUpperCase(property);
        String methodName_2 = "is" + StringUtils.toUpperCase(property);
        //
        for (Method m : target.getMethods())
            if (m.getParameterTypes().length == 0) {
                String methodName = m.getName();
                if (methodName.equals(methodName_1) == true)
                    return m;
                /*�Ƿ��ǲ���*/
                if (methodName.equals(methodName_2) == true) {
                    Class<?> t = m.getReturnType();
                    if (t == Boolean.class || t == boolean.class)
                        return m;
                }
            }
        return null;
    }
    /**��ȡһ�����Ե�д�뷽����*/
    public static Method getWriteMethod(String property, Class<?> target) {
        if (property == null || target == null)
            return null;
        String methodName = "set" + StringUtils.toUpperCase(property);
        for (Method m : target.getMethods())
            if (m.getName().equals(methodName) == true)
                if (m.getParameterTypes().length == 1)
                    return m;
        return null;
    }
    /**�����Ƿ����propertyName����ʾ�����ԣ������Ƕ���д����ֻҪ����һ���ͱ�ʾ���ڸ����ԡ�*/
    public static boolean hasProperty(String propertyName, Class<?> target) {
        //get��set����
        if (getReadMethod(propertyName, target) == null)
            if (getWriteMethod(propertyName, target) == null)
                return false;
        return true;
    }
    /**�����Ƿ����fieldName����ʾ���ֶΣ������Ƕ���д����ֻҪ����һ���ͱ�ʾ���ڸ����ԡ�*/
    public static boolean hasField(String propertyName, Class<?> target) {
        if (getField(propertyName, target) == null)
            return false;
        else
            return true;
    }
    /**�����Ƿ����name����ʾ�����ԣ�hasProperty��hasField��һ������Ϊtrue�򷵻�true��*/
    public static boolean hasPropertyOrField(String name, Class<?> target) {
        if (hasProperty(name, target) == false)
            if (hasField(name, target) == false)
                return false;
        return true;
    }
    /**�����Ƿ�֧��readProperty����������true��ʾ���Խ��ж�ȡ������*/
    public static boolean canReadProperty(String propertyName, Class<?> target) {
        Method readMethod = getReadMethod(propertyName, target);
        if (readMethod != null)
            return true;
        else
            return false;
    }
    /**�����Ƿ�֧��readPropertyOrField������*/
    public static boolean canReadPropertyOrField(String propertyName, Class<?> target) {
        if (canReadProperty(propertyName, target) == false)
            if (hasField(propertyName, target) == false)
                return false;
        return true;
    }
    /**�����Ƿ�֧��writeProperty����������true��ʾ���Խ���д�������*/
    public static boolean canWriteProperty(String propertyName, Class<?> target) {
        Method writeMethod = getWriteMethod(propertyName, target);
        if (writeMethod != null)
            return true;
        else
            return false;
    }
    /**�����Ƿ�֧��writePropertyOrField������*/
    public static boolean canWritePropertyOrField(String propertyName, Class<?> target) {
        if (canWriteProperty(propertyName, target) == false)
            if (hasField(propertyName, target) == false)
                return false;
        return true;
    }
    /*----------------------------------------------------------------------------------------*/
    /**ִ������ע�룬����ע��int,short,long,�Ȼ�������֮��÷�����֧��ע��ö�����͡�����ֵ��ʾִ���Ƿ�ɹ���ע�⣺�÷���������������ͽ��г�������ת����*/
    public static boolean writeProperty(Object object, String attName, Object value) {
        if (object == null || attName == null)
            return false;
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Method writeMethod = getWriteMethod(attName, defineType);
        if (writeMethod == null)
            return false;
        //2.ִ������ת��
        Class<?> toType = writeMethod.getParameterTypes()[0];
        Object defaultValue = EngineToos.getDefaultValue(toType);
        Object attValueObject = StringConvertUtils.changeType(value, toType, defaultValue);
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
        if (object == null || fieldName == null)
            return false;
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Field writeField = getField(fieldName, defineType);
        if (writeField == null)
            return false;
        //2.ִ������ת��
        Class<?> toType = writeField.getType();
        Object attValueObject = StringConvertUtils.changeType(value, toType);
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
        Class<?> defineType = object.getClass();
        if (canWriteProperty(attName, defineType) == true)
            return writeProperty(object, attName, value);//֧�ַ���д��
        if (hasField(attName, defineType) == true)
            return writeField(object, attName, value);//֧���ֶ�д��
        return false;
    }
    /**ִ�����Զ�ȡ��*/
    public static Object readProperty(Object object, String attName) {
        if (object == null || attName == null)
            return false;
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Method readMethod = getReadMethod(attName, defineType);
        if (readMethod == null)
            return null;
        //2.ִ�����Զ�ȡ
        try {
            return readMethod.invoke(object);
        } catch (Exception e) {
            return null;
        }
    };
    /**ִ���ֶζ�ȡ��*/
    public static Object readField(Object object, String fieldName) {
        if (object == null || fieldName == null)
            return null;
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Field readField = getField(fieldName, defineType);
        if (readField == null)
            return null;
        //2.ִ���ֶζ�ȡ
        try {
            return readField.get(object);
        } catch (Exception e) {
            return null;
        }
    }
    /**ִ��ע�룬�÷������Ȼ���ͼִ�����Է���ע�롣���ʧ����ִ���ֶ�ע�롣ע�⣺�÷���������������ͽ��г�������ת����*/
    public static Object readPropertyOrField(Object object, String attName) {
        Class<?> defineType = object.getClass();
        if (canReadProperty(attName, defineType) == true)
            return readProperty(object, attName);//֧�ַ�����ȡ
        if (hasField(attName, defineType) == true)
            return readField(object, attName);//֧���ֶζ�ȡ
        return null;
    }
    /**�ж϶����Ƿ�Ϊ�ջ�����һ�����ַ�����*/
    public static boolean isNone(Object targetBean) {
        return (targetBean == null || targetBean.equals("") == true) ? true : false;
    }
    /**�Ƚ�����ֵ�Ƿ���ȣ�ͬʱ������ֵΪ�յ��жϡ��÷���ʹ��equals�����ж�*/
    public static boolean isEq(Object value1, Object value2) {
        if (value1 == null && value2 != null)
            return false;
        if (value1 != null && value2 == null)
            return false;
        if (value1 == null && value2 == null)
            return true;
        return value1.equals(value2);
    }
    /**�Ƚ�����ֵ�Ƿ���ȣ�ͬʱ������ֵΪ�յ��жϡ��÷���ʹ��equals�����ж�*/
    public static boolean isNe(Object value1, Object value2) {
        return !isEq(value1, value2);
    }
};