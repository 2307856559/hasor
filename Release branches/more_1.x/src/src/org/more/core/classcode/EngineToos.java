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
package org.more.core.classcode;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import org.more.FormatException;
import org.more.core.asm.Opcodes;
import org.more.core.asm.Type;
/**
 * �����ֽ���ʱ��ʹ�õĹ����࣬����д{@link ClassEngine}����ط���ʱ������ϴ��ࡣ
 * Date : 2009-10-16
 * @author ������
 */
public class EngineToos implements Opcodes {
    /** �������л�ȡĳ���������������ඨ��ķ�������(����˽�з���)����������з������Լ��̳еķ������ҡ� */
    public static java.lang.reflect.Method getMethod(Class<?> atClass, String name, Class<?>... types) {
        try {
            return atClass.getDeclaredMethod(name, types);
        } catch (Exception e) {
            try {
                return atClass.getMethod(name, types);
            } catch (Exception e1) {
                return null;
            }
        }
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
        //System.out.println("OK");
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
            throw new RuntimeException("��֧�ֵ�����װ������");//
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
            throw new RuntimeException("��֧�ֵ�����װ������");//
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
    /**��ĳһ������תΪasm��ʽ�ı����� int תΪ I��StringתΪ Ljava/lang/String��*/
    public static String toAsmType(Class<?>[] classType) {
        String returnString = "";
        for (Class<?> c : classType)
            returnString += EngineToos.toAsmType(c);;
        return returnString;
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
    /***/
    public static String toClassType(String asmType) {
        if (asmType.charAt(0) == 'L')
            return asmType.substring(1, asmType.length() - 1);
        else
            return asmType;
    }
    //=======================================================================================================================
    /**��ȡһ��������ֽ���Ķ�ȡ����*/
    public static InputStream getClassInputStream(Class<?> type) {
        ClassLoader cl = type.getClassLoader();
        if (cl == null)
            throw new RuntimeException("��ǰ�汾�޷�װ�� rt.jar�е��ࡣ");
        else
            return cl.getResourceAsStream(type.getName().replace(".", "/") + ".class");
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
    /**��ȡ�������޶����İ������֣�������ʽ��asm��ʽ��*/
    public static String splitPackageNameByASM(String fullName) {
        if (fullName.lastIndexOf("/") > 0)
            return fullName.substring(0, fullName.lastIndexOf("/"));
        else
            return fullName;
    }
    /**��ȡ�������޶������������֣�������ʽ��asm��ʽ��*/
    public static String splitSimpleNameByASM(String fullName) {
        String[] ns = fullName.split("/");
        return ns[ns.length - 1];
    }
    /**������ת��Ϊasm������*/
    public static String replaceClassName(String className) {
        return className.replace(".", "/");
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
            throw new RuntimeException("��֧�ֵ�����װ������");//
        }
    }
    /**ͨ��λ�������check�Ƿ���data�*/
    public static boolean checkIn(int data, int check) {
        int or = data | check;
        return or == data;
    }
}