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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.more.core.classcode.EngineToos;
import org.more.core.classcode.RootClassLoader;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
/**
 * 
 * @version : 2011-6-3
 * @author ������ (zyc@byshell.org)
 */
public abstract class BeanUtil {
    private static final Log log = LogFactory.getLog(BeanUtil.class);
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
    /*----------------------------------------------------------------------------------------*/
    /**��ȡ�ඨ����ֶκͼ̳и����ж�����ֶ��Լ�����ĸ��ࣨ�������¶���ͬ���ֶ�Ҳ�ᱻ���أ���*/
    public static List<Field> findALLFields(Class<?> target) {
        if (target == null) {
            log.error("findALLFields an error , target is null, please check it.");
            return null;
        }
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
    /**��ȡ�ඨ����ֶκͼ̳и����ж�����ֶ��Լ�����ĸ��ࣨ�������¶���ͬ���ֶ�Ҳ���ᱻ���أ���*/
    public static List<Field> findALLFieldsNoRepeat(Class<?> target) {
        if (target == null) {
            log.error("findALLFieldsNoRepeat an error , target is null, please check it.");
            return null;
        }
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
        if (target == null) {
            log.error("findALLMethods an error , target is null, please check it.");
            return null;
        }
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
    /**��ȡ�ඨ��ķ����ͼ̳и����ж���ķ����Լ�����ĸ��ࣨ�������д����Ҳ���ᱻ���أ���*/
    public static List<Method> findALLMethodsNoRepeat(Class<?> target) {
        if (target == null) {
            log.error("findALLMethodsNoRepeat an error , target is null, please check it.");
            return null;
        }
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
                name = StringUtil.toLowerCase(name);
                if (mnames.contains(name) == false)
                    mnames.add(name);
            }
        }
        return mnames;
    }
    /**��ȡһ�����ԵĶ�ȡ������*/
    public static Method getReadMethod(String property, Class<?> target) {
        if (property == null || target == null) {
            log.error("getReadMethod an error , property or target is null, please check it.");
            return null;
        }
        String methodName_1 = "get" + StringUtil.toUpperCase(property);
        String methodName_2 = "is" + StringUtil.toUpperCase(property);
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
        log.debug("{%0} method not exist at {%1}.", property, target);
        return null;
    }
    /**��ȡһ�����Ե�д�뷽����*/
    public static Method getWriteMethod(String property, Class<?> target) {
        if (property == null || target == null) {
            log.error("getWriteMethod an error , property or target is null, please check it.");
            return null;
        }
        String methodName = "set" + StringUtil.toUpperCase(property);
        for (Method m : target.getMethods())
            if (m.getName().equals(methodName) == true)
                if (m.getParameterTypes().length == 1)
                    return m;
        log.debug("{%0} method not exist at {%1}.", methodName, target);
        return null;
    }
    /**����һ���ɲ������ֶΡ�*/
    public static Field getField(String fieldName, Class<?> type) {
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
    /**����һ���ɲ������ֶ��б�*/
    public static List<Field> getFields(Class<?> type) {
        ArrayList<Field> fList = new ArrayList<Field>();
        for (Field field : type.getFields())
            if (fList.contains(field) == false)
                fList.add(field);
        return fList;
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
    /**����һ���ɲ����ķ����б�*/
    public static List<Method> getMethods(Class<?> type) {
        ArrayList<Method> mList = new ArrayList<Method>();
        for (Method method : type.getMethods())
            if (mList.contains(method) == false)
                mList.add(method);
        return mList;
    }
    /*----------------------------------------------------------------------------------------*/
    /**ִ������ע�룬����ע��int,short,long,�Ȼ�������֮��÷�����֧��ע��ö�����͡�����ֵ��ʾִ���Ƿ�ɹ���ע�⣺�÷���������������ͽ��г�������ת����*/
    public static boolean writeProperty(Object object, String attName, Object value) {
        if (object == null || attName == null) {
            log.error("putAttribute an error, object or attName is null, please check it.");
            return false;
        }
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Method writeMethod = getWriteMethod(attName, defineType);
        if (writeMethod == null) {
            log.debug("can`t invoke {%0} , this method not exist on {%1}", attName, defineType);
            return false;
        }
        //2.ִ������ת��
        Class<?> toType = writeMethod.getParameterTypes()[0];
        Object defaultValue = EngineToos.getDefaultValue(toType);
        Object attValueObject = StringConvertUtil.changeType(value, toType, defaultValue);
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
        Field writeField = getField(fieldName, defineType);
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
        if (writeProperty(object, attName, value) == false)
            if (writeField(object, attName, value) == false)
                return false;
        return true;
    }
    /**ִ�����Զ�ȡ��*/
    public static Object readProperty(Object object, String attName) {
        if (object == null || attName == null) {
            log.error("readAttribute an error, object or attName is null, please check it.");
            return false;
        }
        //1.���ҷ���
        Class<?> defineType = object.getClass();
        Method readMethod = getReadMethod(attName, defineType);
        if (readMethod == null) {
            log.debug("can`t invoke {%0} , this method not exist on {%1}", attName, defineType);
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
        Field readField = getField(fieldName, defineType);
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
        Object value = readProperty(object, attName);
        if (value == null)
            value = readField(object, attName);
        return value;
    };
};