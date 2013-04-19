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
package org.more.classcode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import org.more.asm.Opcodes;
import org.more.asm.Type;
import org.more.core.error.FormatException;
/**
 * �����ֽ���ʱ��ʹ�õĹ����࣬����д{@link ClassEngine}����ط���ʱ������ϴ��ࡣ
 * @version 2009-10-16
 * @author ������ (zyc@byshell.org)
 */
public class EngineToos implements Opcodes {
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
    /**�������ͻ�ȡ��Returnָ�*/
    public static int getReturn(String asmType) {
        char t = asmType.charAt(0);
        switch (t) {
        case 'B':
            return IRETURN;//Byte
        case 'C':
            return IRETURN;//Char
        case 'D':
            return DRETURN;//Double
        case 'F':
            return FRETURN;//Float
        case 'I':
            return IRETURN;//Integer
        case 'J':
            return LRETURN;//Long
        case 'L':
            return ARETURN;//Ref
        case 'S':
            return IRETURN;//Short
        case 'Z':
            return IRETURN;//Boolean
        case '[':
            return ARETURN;//Array
        case 'V':
            return RETURN;//Void
        default:
            throw new UnsupportedOperationException("��֧�ֵ�����װ������");//
        }
    }
    /**�������ͻ�ȡ��Loadָ�*/
    public static int getLoad(String asmType) {
        char t = asmType.charAt(0);
        switch (t) {
        case 'B':
            return ILOAD;//Byte
        case 'C':
            return ILOAD;//Char
        case 'D':
            return DLOAD;//Double
        case 'F':
            return FLOAD;//Float
        case 'I':
            return ILOAD;//Integer
        case 'J':
            return LLOAD;//Long
        case 'L':
            return ALOAD;//Ref
        case 'S':
            return ILOAD;//Short
        case 'Z':
            return ILOAD;//Boolean
        case '[':
            return ALOAD;//Array
        default:
            throw new UnsupportedOperationException("��֧�ֵ�����װ������");//
        }
    }
    /**����asm���ͻ�ȡ��ASTOREָ�*/
    public static int getAstore(String asmType) {
        char t = asmType.charAt(0);
        switch (t) {
        case 'B':
            return IASTORE;//Byte
        case 'C':
            return IASTORE;//Char
        case 'D':
            return DASTORE;//Double
        case 'F':
            return FASTORE;//Float
        case 'I':
            return IASTORE;//Integer
        case 'J':
            return LASTORE;//Long
        case 'L':
            return AASTORE;//Ref
        case 'S':
            return IASTORE;//Short
        case 'Z':
            return IASTORE;//Boolean
        case '[':
            return AASTORE;//Array
        default:
            throw new UnsupportedOperationException("��֧�ֵ�����װ������");//
        }
    }
    //=======================================================================================================================
    /**��ĳһ������תΪasm��ʽ�ı����� int תΪ I��StringתΪ Ljava/lang/String��*/
    public static String toAsmType(Class<?> classType) {
        if (classType == int.class)
            return "I";
        else if (classType == byte.class)
            return "B";
        else if (classType == char.class)
            return "C";
        else if (classType == double.class)
            return "D";
        else if (classType == float.class)
            return "F";
        else if (classType == long.class)
            return "J";
        else if (classType == short.class)
            return "S";
        else if (classType == boolean.class)
            return "Z";
        else if (classType == void.class)
            return "V";
        else if (classType.isArray() == true)
            return "[" + EngineToos.toAsmType(classType.getComponentType());
        else
            return "L" + Type.getInternalName(classType) + ";";
    }
    /**��ĳһ������תΪasm��ʽ�ı����� int,int תΪ II��String,intתΪ Ljava/lang/String;I��*/
    public static String toAsmType(Class<?>[] classType) {
        String returnString = "";
        for (Class<?> c : classType)
            returnString += EngineToos.toAsmType(c);;
        return returnString;
    }
    /**ʹ��ָ����ClassLoader��һ��asm����ת��ΪClass����*/
    public static Class<?> toJavaType(String asmClassType, ClassLoader loader) {
        if (asmClassType.equals("I") == true)
            return int.class;
        else if (asmClassType.equals("B") == true)
            return byte.class;
        else if (asmClassType.equals("C") == true)
            return char.class;
        else if (asmClassType.equals("D") == true)
            return double.class;
        else if (asmClassType.equals("F") == true)
            return float.class;
        else if (asmClassType.equals("J") == true)
            return long.class;
        else if (asmClassType.equals("S") == true)
            return short.class;
        else if (asmClassType.equals("Z") == true)
            return boolean.class;
        else if (asmClassType.equals("V") == true)
            return void.class;
        else if (asmClassType.charAt(0) == '[') {
            int length = 0;
            while (true) {
                if (asmClassType.charAt(length) != '[')
                    break;
                length++;
            }
            String arrayType = asmClassType.substring(length, asmClassType.length());
            arrayType = arrayType.replace("/", ".");
            Class<?> returnType = toJavaType(arrayType, loader);
            for (int i = 0; i < length; i++) {
                Object obj = Array.newInstance(returnType, length);
                returnType = obj.getClass();
            }
            return returnType;
        } else {
            String cs = asmTypeToType(asmClassType).replace("/", ".");
            try {
                return loader.loadClass(cs);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(cs + "�಻�ܱ�װ��," + e.getMessage());
            }
        }
    }
    /**ʹ��ָ����ClassLoader��һ��asm����ת��Ϊһ��Class����*/
    public static Class<?>[] toJavaType(String[] asmClassType, ClassLoader loader) {
        Class<?>[] types = new Class<?>[asmClassType.length];
        for (int i = 0; i < asmClassType.length; i++)
            types[i] = toJavaType(asmClassType[i], loader);
        return types;
    }
    /**��һ�����в���ĳ��������*/
    public static Method findMethod(Class<?> atClass, String name, Class<?>[] paramType) {
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
    /**����һ����Ķ�����������а������ඨ���˽�з����͸����пɼ��ķ�����*/
    public static ArrayList<Method> findAllMethod(Class<?> atClass) {
        ArrayList<Method> al = new ArrayList<Method>();
        Method[] m1 = atClass.getDeclaredMethods();
        Collections.addAll(al, m1);
        for (Method m : atClass.getMethods())
            if (al.contains(m) == false)
                al.add(m);
        return al;
    }
    /**����һ����Ķ���ֶΣ����а������ඨ���˽���ֶκ͸����пɼ����ֶΡ�*/
    public static ArrayList<Field> findAllField(Class<?> atClass) {
        ArrayList<Field> al = new ArrayList<Field>();
        Field[] m1 = atClass.getDeclaredFields();
        Collections.addAll(al, m1);
        for (Field f : atClass.getFields())
            if (al.contains(f) == false)
                al.add(f);
        return al;
    }
    /**��һ��Ljava/lang/Object;��ʽ���ַ���ת��Ϊjava/lang/Object��ʽ��*/
    public static String asmTypeToType(String asmType) {
        if (asmType.charAt(0) == 'L')
            return asmType.substring(1, asmType.length() - 1);
        else
            return asmType;
    }
    //=======================================================================================================================
    public static String methodToAsmMethod(Method method) {
        StringBuffer str = new StringBuffer();
        str.append(method.getName());
        str.append("(");
        str.append(toAsmType(method.getParameterTypes()));
        str.append(")");
        Class<?> returnType = method.getReturnType();
        if (returnType == void.class)
            str.append("V");
        else
            str.append(toAsmType(returnType));
        return str.toString();
    }
    /**��ȡһ��������ֽ���Ķ�ȡ����*/
    public static InputStream getClassInputStream(Class<?> type) {
        ClassLoader loader = type.getClassLoader();
        if (loader instanceof RootClassLoader) {
            byte[] data = ((RootClassLoader) loader).toBytes(type);
            return new ByteArrayInputStream(data);
        }
        String classResourceName = type.getName().replace(".", "/") + ".class";
        if (loader != null)
            return loader.getResourceAsStream(classResourceName);
        else
            return ClassLoader.getSystemResourceAsStream(classResourceName);
    };
    /**�ж�ĳ�����Ƿ�Ϊһ��lang�����ࡣ*/
    public static boolean isLangClass(Class<?> type) {
        return type.getName().startsWith("java.lang.");
    };
    /**ת������ĸ��д*/
    public static String toUpperCase(String value) {
        StringBuffer sb = new StringBuffer(value);
        char firstChar = sb.charAt(0);
        sb.delete(0, 1);
        sb.insert(0, (char) ((firstChar >= 97) ? firstChar - 32 : firstChar));
        return sb.toString();
    }
    /** ��IIIILjava/lang/Integer;F��ʽ��ASM���ͱ����ֽ�Ϊ���顣�����ַ���IIIILjava/lang/Integer;F[[[ILjava/lang.Boolean; */
    public static String[] splitAsmType(String asmTypes) {
        class AsmTypeRead {
            StringReader sread = null;
            public AsmTypeRead(String sr) {
                this.sread = new StringReader(sr);
            }
            /** ��ȡ����һ���ֺ�Ϊֹ���߽���Ϊֹ��*/
            private String readToSemicolon() throws IOException {
                String res = "";
                while (true) {
                    int strInt = sread.read();
                    if (strInt == -1)
                        return res;
                    else if ((char) strInt == ';')
                        return res + ';';
                    else
                        res += (char) strInt;
                }
            }
            /** ��ȡһ������ */
            private String readType() throws IOException {
                int strInt = sread.read();
                if (strInt == -1)
                    return "";
                switch ((char) strInt) {
                case '['://array
                    return '[' + this.readType();
                case 'L'://Object
                    return 'L' + this.readToSemicolon();
                default:
                    return String.valueOf((char) strInt);
                }
            }
            /** ��ȡ�������� */
            public String[] readTypes() throws IOException {
                ArrayList<String> ss = new ArrayList<String>(0);
                while (true) {
                    String s = this.readType();
                    if (s.equals("") == true)
                        break;
                    else
                        ss.add(s);
                }
                String[] res = new String[ss.size()];
                ss.toArray(res);
                return res;
            }
        }
        try {
            return new AsmTypeRead(asmTypes).readTypes();//     IIIILjava/lang/Integer;F[[[Ljava/util/Date;
        } catch (Exception e) {
            throw new FormatException("���Ϸ���ASM����desc��");
        }
    }
    /**��ȡ�������޶������������֡�*/
    public static String splitSimpleName(String fullName) {
        String[] ns = fullName.split("\\.");
        return ns[ns.length - 1];
    }
    /**��ȡ�������޶����İ������֡�*/
    public static String splitPackageName(String fullName) {
        if (fullName.lastIndexOf(".") > 0)
            return fullName.substring(0, fullName.lastIndexOf("."));
        else
            return fullName;
    }
    /**������ת��Ϊasm������*/
    public static String replaceClassName(String className) {
        return className.replace(".", "/");
    }
    /**ͨ��λ�������check�Ƿ���data�*/
    public static boolean checkIn(int data, int check) {
        int or = data | check;
        return or == data;
    };
    /**��ȡָ�����͵�Ĭ��ֵ��*/
    public static Object getDefaultValue(Class<?> returnType) {
        if (returnType == null)
            return null;
        else if (returnType == int.class)
            return 0;
        else if (returnType == byte.class)
            return 0;
        else if (returnType == char.class)
            return ' ';
        else if (returnType == double.class)
            return 0d;
        else if (returnType == float.class)
            return 0f;
        else if (returnType == long.class)
            return 0l;
        else if (returnType == short.class)
            return 0;
        else if (returnType == boolean.class)
            return false;
        else if (returnType == void.class)
            return null;
        else if (returnType.isArray() == true)
            return null;
        else
            return null;
    };
    /**��������Ƿ�Ϊһ���������ͻ����װ���ͣ��������Ͱ�����boolean, byte, char, short, int, long, float, �� double*/
    public static boolean isBaseType(Class<?> type) {
        /*�ж��Ƿ�Ϊ��������*/
        if (type.isPrimitive() == true)
            return true;
        /*�жϸ��ְ�װ����*/
        if (type == Boolean.class)
            return true;
        if (type == Byte.class)
            return true;
        if (type == Character.class)
            return true;
        if (type == Short.class)
            return true;
        if (type == Integer.class)
            return true;
        if (type == Long.class)
            return true;
        if (type == Float.class)
            return true;
        if (type == Double.class)
            return true;
        return false;
    }
}