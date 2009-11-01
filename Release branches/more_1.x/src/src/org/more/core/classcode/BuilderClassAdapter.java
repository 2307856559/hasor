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
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.InvokeException;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassReader;
import org.more.core.asm.ClassVisitor;
import org.more.core.asm.ClassWriter;
import org.more.core.asm.FieldVisitor;
import org.more.core.asm.Label;
import org.more.core.asm.MethodVisitor;
import org.more.core.asm.Opcodes;
import org.more.core.asm.Type;
/**
 * ���ฺ���޸�����ֽ��븽�ӽӿ�ʵ�ַ�����
 * ���������
 * visit
 *   1.����ʵ�ֽӿ�
 *   2.�̳л���
 *   3.�޸���������
 * visitMethod
 *   1.�޸ķ�����Ϊ _methodName
 *   2.���������
 *   3.���ӱ��ط�������
 * visitEnd
 *   1.ɨ�踽�ӽӿڷ���
 *   2.������ط��������д��ڸ÷�����������������÷����Ǳ������Ļ���˽�е��׳��쳣
 *   3.�������������
 * Date : 2009-10-22
 * @author ������
 */
class BuilderClassAdapter extends ClassAdapter implements Opcodes {
    //========================================================================================Field
    /** xxxx */
    private ClassEngine                   engine          = null;
    /** �������� */
    private String                        superClassByASM = null;
    /** ��ǰ������ */
    private String                        thisClassByASM  = null;
    /** ���ɵ�������Ҫ���ӵĽӿ�ʵ�� */
    private Map<Class<?>, MethodDelegate> implsMap        = null;
    /** �������Ѿ����ڵķ��� */
    private ArrayList<String>             methodList      = new ArrayList<String>(0);
    private Class<?>                      superClassType;
    //==================================================================================Constructor
    /** ...... */
    public BuilderClassAdapter(ClassEngine engine, ClassVisitor cv, Class<?> superClass, Map<Class<?>, MethodDelegate> implsMap) {
        super(cv);
        this.engine = engine;
        this.superClassByASM = EngineToos.replaceClassName(superClass.getName());
        this.implsMap = implsMap;
        this.superClassType = superClass;
    }
    //==========================================================================================Job
    /** ���ӽӿ�ʵ�� */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //1.����ʵ�ֽӿ�
        //2.�̳л���
        //3.�޸���������/
        ArrayList<String> al = new ArrayList<String>(0);
        //----------һ���Ѿ�ʵ�ֵĽӿ�
        Collections.addAll(al, interfaces);
        //----------�������ӽӿ�ʵ��
        for (Class<?> i : this.implsMap.keySet()) {
            String implType = EngineToos.replaceClassName(i.getName());
            if (al.contains(implType) == false)
                al.add(implType);
        }
        //----------����ת��ListΪArray
        String[] ins = new String[al.size()];
        al.toArray(ins);
        //----------�ġ��̳л��ࡢ�޸���������
        this.thisClassByASM = engine.getClassName().replace(".", "/");
        super.visit(version, access, this.thisClassByASM, signature, name, ins);
    }
    /** ���ø��෽�� */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        //1.��������ڴ���ģʽ�����������˽�з������ܱ����ķ�����
        if (this.engine.getMode() == ClassEngine.BuilderMode.Propxy)
            if (name.equals("<init>") || (access | ACC_PRIVATE) == access || (access | ACC_PROTECTED) == access)
                return null;
        //2.������˽�з�������ԭװ
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if ((access | ACC_PRIVATE) == access)
            return mv;//������˽�з����ĸ�����ã�ά��ԭװ
        //3.׼�������������
        Pattern p = Pattern.compile("\\((.*)\\)(.*)");
        Matcher m = p.matcher(desc);
        m.find();
        String[] asmParams = EngineToos.splitAsmType(m.group(1));//"IIIILjava/lang/Integer;F[[[ILjava/lang.Boolean;"
        String asmReturns = m.group(2);
        int maxLocals = 1;
        //3.�������
        mv.visitCode();
        if (this.engine.getMode() == ClassEngine.BuilderMode.Super) {
            //Super ����Ǽ̳з�ʽ������ʹ��super.invoke���á�
            mv.visitVarInsn(ALOAD, 0);
            for (int i = 0; i < asmParams.length; i++)
                mv.visitVarInsn(EngineToos.getLoad(asmParams[i]), i + 1);
            mv.visitMethodInsn(INVOKESPECIAL, this.superClassByASM, name, desc);
            maxLocals += asmParams.length;
        } else {
            //Propxy ����Ǵ���ʽ��ʹ��this.$propxyObject.invoke��
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, this.thisClassByASM, ClassEngine.PropxyModeObjectName, "L" + this.superClassByASM + ";");
            for (int i = 0; i < asmParams.length; i++)
                mv.visitVarInsn(EngineToos.getLoad(asmParams[i]), i + 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, this.superClassByASM, name, desc);
            maxLocals += (asmParams.length + 1);
        }
        //4.���������õķ���ֵreturn��
        if (asmReturns.equals("V") == true)
            mv.visitInsn(RETURN);
        else
            mv.visitInsn(EngineToos.getReturn(asmReturns));
        //5.�������������ȷ��������ջ����Ϣ��
        mv.visitMaxs(1, maxLocals);
        mv.visitEnd();
        //6.���Ѿ�����ķ�����ӵ����ط������в����ء�
        methodList.add(name + desc);
        return null;
    }
    /** ����ӿڸ��ӷ��� */
    @Override
    public void visitEnd() {
        try {
            {
                //1.��������ֶ�
                FieldVisitor field = super.visitField(ACC_PRIVATE, ClassEngine.ObjectDelegateMapName, "Ljava/util/Hashtable;", null, null);
                field.visitEnd();
                //2.��������ֶε�ע�뷽��,�����������Ǵ����ֶε�����ǰ�����set�����ֶ�����ĸ����Ҫ��д��
                MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "set" + ClassEngine.ObjectDelegateMapName, "(Ljava/util/Hashtable;)V", null, null);
                mv.visitVarInsn(ALOAD, 0);//װ��this
                mv.visitVarInsn(ALOAD, 1);//װ�ز��� 
                mv.visitFieldInsn(PUTFIELD, this.thisClassByASM, ClassEngine.ObjectDelegateMapName, "Ljava/util/Hashtable;");
                mv.visitInsn(RETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            {
                //3.��������ڴ���ģʽ��������������ط�����
                if (this.engine.getMode() == ClassEngine.BuilderMode.Propxy) {
                    //Super ����Ǽ̳з�ʽ��������������ֶΡ�
                    String superClassNyASMType = "L" + this.superClassByASM + ";";
                    FieldVisitor propxy = super.visitField(ACC_PRIVATE, ClassEngine.PropxyModeObjectName, superClassNyASMType, null, null);
                    propxy.visitEnd();
                    MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "(" + superClassNyASMType + ")V", null, null);
                    mv.visitVarInsn(ALOAD, 0);//װ��this
                    mv.visitMethodInsn(INVOKESPECIAL, this.superClassByASM, "<init>", "()V");
                    mv.visitVarInsn(ALOAD, 0);//װ��this
                    mv.visitVarInsn(ALOAD, 1);//װ�ز��� 
                    mv.visitFieldInsn(PUTFIELD, this.thisClassByASM, ClassEngine.PropxyModeObjectName, superClassNyASMType);
                    mv.visitInsn(RETURN);
                    mv.visitMaxs(1, 1);
                    mv.visitEnd();
                }
            }
            {
                //2.���ӽӿ�ʵ��
                for (final Class<?> impl_type : this.implsMap.keySet()) {
                    InputStream inStream = EngineToos.getClassInputStream(impl_type);//��ȡ������
                    ClassReader reader = new ClassReader(inStream);//����ClassReader
                    final BuilderClassAdapter ca = this;
                    //ɨ�踽�ӽӿڷ���
                    reader.accept(new ClassAdapter(new ClassWriter(ClassWriter.COMPUTE_MAXS)) {
                        @Override
                        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                            if (ca.methodList.contains(name + desc) == true)
                                //������ط��������д��ڸ÷�������������
                                return null;
                            ca.methodList.add(name + desc);
                            MethodVisitor mv = ca.cv.visitMethod(ACC_PUBLIC, name, desc, signature, exceptions);
                            BuilderClassAdapter.visitInterfaceMethod(ca.engine, mv, impl_type, name, desc);//�������������
                            return mv;
                        }
                    }, ClassReader.SKIP_DEBUG);
                }
            }
            //3.��д�����еķ���
            {
                for (java.lang.reflect.Method m : this.superClassType.getMethods()) {
                    String returnStr = EngineToos.toAsmType(m.getReturnType());
                    //
                    String desc = "(" + EngineToos.toAsmType(m.getParameterTypes()) + ")" + returnStr;
                    String fullDesc = m.getName() + desc;
                    //����Ƿ����Ѿ����ڵķ���
                    if (this.methodList.contains(fullDesc) == true)
                        continue;
                    String[] exceptions = EngineToos.splitAsmType(EngineToos.toAsmType(m.getExceptionTypes()));
                    //��������
                    for (int i = 0; i < exceptions.length; i++)
                        exceptions[i] = exceptions[i].substring(1, exceptions[i].length() - 1);
                    exceptions = (exceptions.length == 0) ? null : exceptions;
                    //
                    int access = m.getModifiers();
                    if ((access | Modifier.FINAL) == access)
                        continue;//���Գ�����
                    else if ((access | Modifier.PUBLIC) == access)
                        this.visitMethod(ACC_PUBLIC, m.getName(), desc, null, exceptions);
                    else if ((access | Modifier.PROTECTED) == access)
                        this.visitMethod(ACC_PROTECTED, m.getName(), desc, null, exceptions);
                }
            }
            //4.����
            super.visitEnd();
        } catch (Exception e) {
            throw new InvokeException("ִ�и��ӽӿڷ����ڼ䷢���쳣��" + e.getMessage(), e);
        }
    }
    /** ʵ�ֽӿڸ��� */
    public static void visitInterfaceMethod(final ClassEngine engine, final MethodVisitor mv, Class<?> inplType, String name, String desc) {//, final Method method) {
        String replaceClassName = EngineToos.replaceClassName(engine.getClassName());
        Pattern p = Pattern.compile("\\((.*)\\)(.*)");
        Matcher m = p.matcher(desc);
        m.find();
        String[] asmParams = EngineToos.splitAsmType(m.group(1));//"IIIILjava/lang/Integer;F[[[ILjava/lang.Boolean;"
        String asmReturns = m.group(2);
        int paramCount = asmParams.length;
        int localVarSize = paramCount;//�����������С
        int maxStackSize = 0;//��������ջ��С
        //-----------------------------------------------------------------------------------------------------------------------
        mv.visitCode();
        Label try_begin = new Label();
        Label try_end = new Label();
        Label try_catch = new Label();
        mv.visitTryCatchBlock(try_begin, try_end, try_catch, "java/lang/Throwable");
        mv.visitLabel(try_begin);
        //-----------------------------------------------------------------------------------------------------------------------
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, replaceClassName, ClassEngine.ObjectDelegateMapName, "Ljava/util/Hashtable;");
        mv.visitLdcInsn(inplType.getName());
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
        mv.visitTypeInsn(CHECKCAST, "org/more/core/classcode/Method");
        mv.visitVarInsn(ASTORE, paramCount + 1);//0=this 1=param1
        localVarSize++;
        maxStackSize = (maxStackSize < 2) ? 2 : maxStackSize;
        //Method localMethod=this.$delegateMap.get("xxxxxxx");-------------------------------------------------------------------
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, replaceClassName, "getClass", "()Ljava/lang/Class;");
        //this.getClass();
        mv.visitLdcInsn(name);
        mv.visitIntInsn(BIPUSH, paramCount);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
        for (int i = 0; i < paramCount; i++) {
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, i);
            if (asmParams[i].equals("B") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
            else if (asmParams[i].equals("S") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
            else if (asmParams[i].equals("I") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
            else if (asmParams[i].equals("J") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
            else if (asmParams[i].equals("F") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
            else if (asmParams[i].equals("D") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
            else if (asmParams[i].equals("C") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Character", "TYPE", "Ljava/lang/Class;");
            else if (asmParams[i].equals("Z") == true)
                mv.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
            else
                mv.visitLdcInsn(Type.getObjectType(EngineToos.toClassType(asmParams[i])));
            mv.visitInsn(AASTORE);
            maxStackSize = (maxStackSize < 5 + i) ? 5 + i : maxStackSize;
        }
        //new Class[]{....,....}
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
        mv.visitVarInsn(ASTORE, paramCount + 2);
        localVarSize++;
        // Method localMethod1 = getClass().getMethod("xxx", new Class[]{Integer.Type,Data.class......});------------------------
        mv.visitVarInsn(ALOAD, paramCount + 1);
        mv.visitFieldInsn(GETFIELD, "org/more/core/classcode/Method", "delegate", "Lorg/more/core/classcode/MethodDelegate;");
        mv.visitVarInsn(ALOAD, paramCount + 2);//����1
        mv.visitVarInsn(ALOAD, 0); //����3
        //����4
        mv.visitIntInsn(BIPUSH, paramCount);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        for (int i = 0; i < paramCount; i++) {
            String asmType = asmParams[i];
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, i);
            if (asmParams[i].equals("B")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            } else if (asmParams[i].equals("S")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
            } else if (asmParams[i].equals("I")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            } else if (asmParams[i].equals("J")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
            } else if (asmParams[i].equals("F")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            } else if (asmParams[i].equals("D")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            } else if (asmParams[i].equals("C")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            } else if (asmParams[i].equals("Z")) {
                mv.visitVarInsn(EngineToos.getLoad(asmType), i + 1);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
            } else
                mv.visitVarInsn(ALOAD, i + 1);
            mv.visitInsn(AASTORE);
            maxStackSize = (maxStackSize < 8 + i) ? 8 + i : maxStackSize;
        }
        //localMethod1, localMethod.originalMethod, this, new Object[]{xxx,xxx}
        String desc2 = "Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;";
        mv.visitMethodInsn(INVOKEINTERFACE, "org/more/core/classcode/MethodDelegate", "invoke", "(" + desc2 + ")Ljava/lang/Object;");
        mv.visitVarInsn(ASTORE, paramCount + 3);
        localVarSize++;
        //obj = localMethod.delegate.invoke(localMethod1, this, new Object[] { methodCode });--------
        mv.visitVarInsn(ALOAD, paramCount + 3);
        if (asmReturns.equals("B") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
            mv.visitInsn(EngineToos.getReturn("B"));
        } else if (asmReturns.equals("S") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
            mv.visitInsn(EngineToos.getReturn("S"));
        } else if (asmReturns.equals("I") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
            mv.visitInsn(EngineToos.getReturn("I"));
        } else if (asmReturns.equals("J") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
            mv.visitInsn(EngineToos.getReturn("J"));
        } else if (asmReturns.equals("F") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
            mv.visitInsn(EngineToos.getReturn("F"));
        } else if (asmReturns.equals("D") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
            mv.visitInsn(EngineToos.getReturn("D"));
        } else if (asmReturns.equals("C") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
            mv.visitInsn(EngineToos.getReturn("C"));
        } else if (asmReturns.equals("Z") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
            mv.visitInsn(EngineToos.getReturn("Z"));
        } else if (asmReturns.equals("V") == true) {
            mv.visitInsn(RETURN);
        } else {
            mv.visitTypeInsn(CHECKCAST, asmReturns);
            mv.visitInsn(ARETURN);
        }
        mv.visitLabel(try_end);
        //return obj-------------------------------------------------------------------------------------------------------------
        mv.visitLabel(try_catch);
        mv.visitVarInsn(ASTORE, 4);
        mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/Throwable;)V");
        mv.visitInsn(ATHROW);
        /* �����ջ�б� */
        mv.visitMaxs(maxStackSize, localVarSize + 1);
        mv.visitEnd();
    }
}