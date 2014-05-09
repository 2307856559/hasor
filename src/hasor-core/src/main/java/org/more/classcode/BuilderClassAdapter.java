/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.asm.ClassReader;
import org.more.asm.ClassVisitor;
import org.more.asm.ClassWriter;
import org.more.asm.FieldVisitor;
import org.more.asm.MethodVisitor;
import org.more.asm.Opcodes;
/**
 * ���ฺ���޸�����ֽ��븽�ӽӿ�ʵ�ַ�����
 * ���������
 * visit
 *   1.����ʵ�ֽӿ�
 *   2.�̳л���
 *   3.�޸���������
 * visitMethod
 *   1.�޸ķ�����Ϊ
 *   2.���������
 *   3.���ӱ��ط�������
 * visitEnd
 *   1.���Propxy�Ĺ��췽��
 *   2.���������
 *   3.���ί������
 *   4.���ί�з���
 * @version 2010-8-12
 * @author ������ (zyc@hasor.net)
 */
class BuilderClassAdapter extends ClassVisitor implements Opcodes {
    //1.ClassAdapterʹ�õ������
    private ClassBuilder        classBuilder              = null;
    private ClassEngine         classEngine               = null;
    private String              asmClassName              = null;
    private ArrayList<String>   localMethodList           = null;
    //2.���ڱ�����Ⱦ�ֽ���Ľ��
    private ArrayList<String>   renderMethodList          = null;
    private ArrayList<String>   renderDelegateList        = null;
    private ArrayList<String>   renderDelegatePropxyList  = null;
    //3.�������ڴ���ģʽ�´����ֶε����ơ�
    public final static String  SuperPropxyName           = "$propxyObject";
    //4.�����˴������ԣ����������ֶ����ƺ����͡�
    public final static String  DelegateArrayName         = "$delegateArray";
    private final static String DelegateArrayType         = EngineToos.toAsmType(MethodDelegate[].class);
    public final static String  DelegateMethodArrayName   = "$delegateMethodArray";
    private final static String DelegateMethodArrayType   = EngineToos.toAsmType(Method[].class);
    //5.�����˴������Ե��ֶ����ơ�
    public final static String  PropertyArrayName         = "$propertyArray";
    private final static String PropertyDelegateArrayType = EngineToos.toAsmType(PropertyDelegate[].class);
    //6.�Ƿ����õı���ֶ���
    public final static String  ConfigMarkName            = "$configMark";
    //
    public BuilderClassAdapter(ClassVisitor cv, ClassBuilder classBuilder) {
        super(ASM4, cv);
        this.classBuilder = classBuilder;
        this.classEngine = classBuilder.getClassEngine();
        this.localMethodList = new ArrayList<String>();
        this.renderMethodList = new ArrayList<String>();
        this.renderDelegateList = new ArrayList<String>();
        this.renderDelegatePropxyList = new ArrayList<String>();
        this.asmClassName = this.classBuilder.getClassEngine().getAsmClassName();
    }
    /***/
    public ArrayList<String> getRenderMethodList() {
        return this.renderMethodList;
    }
    /***/
    public ArrayList<String> getRenderDelegateList() {
        return this.renderDelegateList;
    }
    /***/
    public ArrayList<String> getRenderDelegatePropxyList() {
        return this.renderDelegatePropxyList;
    }
    //
    //1.����ʵ�ֽӿ�
    //2.�̳л���
    //3.�޸���������
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //1.���ӽӿ�ʵ��
        if (this.classBuilder.isAddDelegate() == true) {
            this.putSetMethod(DelegateArrayName, DelegateArrayType);
            this.putSetMethod(DelegateMethodArrayName, DelegateMethodArrayType);
            ArrayList<String> al = new ArrayList<String>(interfaces.length + 10);
            Collections.addAll(al, interfaces);//�Ѿ�ʵ�ֵĽӿ�
            Collections.addAll(al, this.classBuilder.getDelegateString());//���ӽӿ�ʵ��
            Collections.addAll(renderDelegateList, this.classBuilder.getDelegateString());//���ӽӿ�ʵ��
            //ת��ListΪArray
            interfaces = new String[al.size()];
            al.toArray(interfaces);
        }
        //2.�̳л���
        superName = name;
        //3.�޸���������
        name = this.asmClassName;
        super.visit(version, ACC_PUBLIC, name, signature, superName, interfaces);
    }
    //
    //����Propxyģʽ��ʱ��ͺ����ֶε������
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (this.classEngine.getBuilderMode() == BuilderMode.Propxy)
            return null;
        return super.visitField(access, name, desc, signature, value);
    }
    //
    //1.�������Բ���
    //2.������з�����Super��Propxy��
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        BuilderMode builderMode = this.classEngine.getBuilderMode();
        String asmSuperClassName = this.classBuilder.getClassEngine().getAsmSuperClassName();
        String fullDesc = name + desc;
        MethodStrategy methodStrategy = this.classEngine.getMethodStrategy();
        //
        //1.�����ض���������
        if ((access | ACC_PRIVATE) == access) //����private������
            return null;
        if ((access | ACC_STATIC) == access)//����static����������
            return null;
        if ((access | ACC_FINAL) == access)//����final����������
            return null;
        if ((access | ACC_NATIVE) == access)//����native����������
            if (fullDesc.equals("hashCode()I") == false && fullDesc.equals("clone()Ljava/lang/Object;") == false)
                return null;
            else
                access = access - ACC_NATIVE;
        if (builderMode == BuilderMode.Propxy) {//����Propxy��
            if (name.equals("<init>") == true)//����Propxy�µ����й��췽����
                return null;
            if ((access | ACC_PROTECTED) == access)//����Propxy�µı���������
                return null;
        }
        //2.׼�������������
        Pattern p = Pattern.compile("\\((.*)\\)(.*)");
        Matcher m = p.matcher(desc);
        m.find();
        String[] asmParams = EngineToos.splitAsmType(m.group(1));//"IIIILjava/lang/Integer;F[[[ILjava/lang.Boolean;"
        String asmReturns = m.group(2);
        //
        //3.ִ�з������Բ���
        Class<?> superClass = this.classEngine.getSuperClass();
        boolean isConstructor = false;
        if (name.equals("<init>") == true)
            isConstructor = true;
        Class<?>[] paramTypes = EngineToos.toJavaType(asmParams, this.classEngine.getRootClassLoader());
        Object method = null;
        if (isConstructor == true)
            try {
                method = superClass.getConstructor(paramTypes);
            } catch (Exception e) {/*����*/}
        else
            method = EngineToos.findMethod(superClass, name, paramTypes);
        if (method != null)
            if (methodStrategy.isIgnore(superClass, method, isConstructor) == true)//��������
                return null;
        //
        //4.�������
        int maxLocals = 1;
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        mv.visitCode();
        if (builderMode == BuilderMode.Super) {
            //Super ����Ǽ̳з�ʽ������ʹ��super.invoke���á�
            mv.visitVarInsn(ALOAD, 0);
            for (int i = 0; i < asmParams.length; i++)
                mv.visitVarInsn(EngineToos.getLoad(asmParams[i]), i + 1);
            mv.visitMethodInsn(INVOKESPECIAL, asmSuperClassName, name, desc);
            maxLocals += asmParams.length;
        } else {
            //Propxy ����Ǵ���ʽ��ʹ��this.$propxyObject.invoke��
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, asmClassName, SuperPropxyName, "L" + asmSuperClassName + ";");
            for (int i = 0; i < asmParams.length; i++)
                mv.visitVarInsn(EngineToos.getLoad(asmParams[i]), i + 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, asmSuperClassName, name, desc);
            maxLocals += (asmParams.length + 1);
        }
        //5.���������õķ���ֵreturn��
        if (asmReturns.equals("V") == true)
            mv.visitInsn(RETURN);
        else
            mv.visitInsn(EngineToos.getReturn(asmReturns));
        //6.�������������ȷ��������ջ����Ϣ��
        mv.visitMaxs(maxLocals + 1, maxLocals + 1);
        mv.visitEnd();
        //7.���Ѿ�����ķ�����ӵ����ط������в����أ���visitInterfaceMethod�����л���Ҫ�����Ϣ��
        localMethodList.add(name + desc);
        return null;
    }
    //
    //1.���Propxy�Ĺ��췽��
    //2.���������
    //3.���ί������
    //4.���ί�з���
    public void visitEnd() {
        //
        //1.���Propxy�Ĺ��췽����
        if (this.classEngine.getBuilderMode() == BuilderMode.Propxy) {
            String asmSuperName = EngineToos.toAsmType(this.classEngine.getSuperClass());
            String asmSuperName2 = EngineToos.asmTypeToType(asmSuperName);
            //
            FieldVisitor fv = super.visitField(ACC_PRIVATE, SuperPropxyName, asmSuperName, null, null);
            fv.visitEnd();
            MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "(" + asmSuperName + ")V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);//װ��this
            mv.visitMethodInsn(INVOKESPECIAL, asmSuperName2, "<init>", "()V");
            mv.visitVarInsn(ALOAD, 0);//װ��this
            mv.visitVarInsn(ALOAD, 1);//װ�ز���
            mv.visitFieldInsn(PUTFIELD, EngineToos.asmTypeToType(asmClassName), SuperPropxyName, asmSuperName);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        //
        //2.�������
        if (this.classBuilder.isAddFields() == true) {
            PropertyStrategy propertyStrategy = this.classEngine.getPropertyStrategy();
            //�����ԡ�
            String[] simpleFields = this.classBuilder.getSimpleFields();
            if (simpleFields != null)
                for (String field : simpleFields) {
                    Class<?> fieldType = this.classEngine.getSimplePropertyType(field);
                    if (propertyStrategy.isIgnore(field, fieldType, false) == true)
                        continue;
                    boolean readOnly = propertyStrategy.isReadOnly(field, fieldType, false);
                    boolean writeOnly = propertyStrategy.isWriteOnly(field, fieldType, false);
                    this.putSimpleProperty(field, fieldType, writeOnly, readOnly);
                }
            //ί�����ԡ�
            String[] delegateFields = this.classBuilder.getDelegateFields();
            if (delegateFields != null) {
                super.visitField(ACC_PRIVATE, PropertyArrayName, PropertyDelegateArrayType, null, null);
                this.putSetMethod(PropertyArrayName, PropertyDelegateArrayType);
                for (int i = 0; i < delegateFields.length; i++) {
                    String field = delegateFields[i];
                    PropertyDelegate<?> fieldDelegate = this.classEngine.getDelegateProperty(field);
                    Class<?> delegateType = fieldDelegate.getType();
                    if (propertyStrategy.isIgnore(field, delegateType, true) == true)
                        continue;
                    this.renderDelegatePropxyList.add(field);
                    boolean readOnly = propertyStrategy.isReadOnly(field, delegateType, false);
                    boolean writeOnly = propertyStrategy.isWriteOnly(field, delegateType, false);
                    this.putDelegateProperty(i, field, fieldDelegate, writeOnly, readOnly);
                }
            }
        }
        //PropertyArrayName
        //3.���ί�з�����
        if (this.classBuilder.isAddDelegate() == true) {
            //
            super.visitField(ACC_PRIVATE, DelegateArrayName, DelegateArrayType, null, null);
            super.visitField(ACC_PRIVATE, DelegateMethodArrayName, DelegateMethodArrayType, null, null);
            //
            Class<?>[] delegateType = this.classBuilder.getDelegateType();
            for (int i = 0; i < delegateType.length; i++) {
                final Class<?> type = delegateType[i];
                final int classIndex = i;
                try {
                    ClassReader reader = new ClassReader(EngineToos.getClassInputStream(type));//����ClassReader
                    final BuilderClassAdapter adapter = this;
                    //ɨ�踽�ӽӿڷ���
                    reader.accept(new ClassVisitor(Opcodes.ASM4, new ClassWriter(ClassWriter.COMPUTE_MAXS)) {
                        private int methodIndex = 0;
                        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                            if (adapter.localMethodList.contains(name + desc) == true)
                                return null;//������ط��������д��ڸ÷�������������
                            String fullDesc = name + desc;
                            MethodVisitor mv = adapter.cv.visitMethod(ACC_PUBLIC, name, desc, signature, exceptions);
                            adapter.visitInterfaceMethod(classIndex, methodIndex, adapter, mv, type, name, desc);//�������������
                            adapter.localMethodList.add(fullDesc);//�����Դ�������
                            adapter.renderMethodList.add(fullDesc);//������Ҫioc��Method���͵ķ�����
                            methodIndex++;
                            return mv;
                        }
                    }, ClassReader.SKIP_DEBUG);
                    //try end
                } catch (Exception e) {
                    throw new InvokeException("��ɨ�����ί��[" + type.getName() + "]ʱ�����쳣��");
                }
            }
            //
        }
        //4.������ñ��
        //����Ƿ񾭹����õı��
        this.putSimpleProperty(ConfigMarkName, Boolean.class, false, false);
        super.visitEnd();
    }
    //
    //���������
    private void putSimpleProperty(String propertyName, Class<?> propertyType, boolean isWriteOnly, boolean isReadOnly) {
        String asmFieldType = EngineToos.toAsmType(propertyType);
        FieldVisitor fv = super.visitField(ACC_PRIVATE, propertyName, asmFieldType, null, null);
        fv.visitEnd();
        if (isWriteOnly == false)
            this.putGetMethod(propertyName, asmFieldType);//get
        if (isReadOnly == false)
            this.putSetMethod(propertyName, asmFieldType);//set
    }
    //
    //���ί������
    private void putDelegateProperty(int index, String propertyName, PropertyDelegate<?> fieldDelegate, boolean isWriteOnly, boolean isReadOnly) {
        String asmDelegateType2 = EngineToos.replaceClassName(PropertyDelegate.class.getName());
        //
        Class<?> javaFieldType = fieldDelegate.getType();
        String asmFieldType = EngineToos.toAsmType(javaFieldType);
        String asmFieldType2 = EngineToos.replaceClassName(javaFieldType.getName());
        if (isWriteOnly == false) {
            //get
            MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "get" + EngineToos.toUpperCase(propertyName), "()" + asmFieldType, null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);//װ��this
            mv.visitFieldInsn(GETFIELD, this.asmClassName, PropertyArrayName, PropertyDelegateArrayType);
            mv.visitIntInsn(BIPUSH, index);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ALOAD, 0);//װ��this
            mv.visitMethodInsn(INVOKEINTERFACE, asmDelegateType2, "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
            mv.visitTypeInsn(CHECKCAST, asmFieldType2);
            mv.visitInsn(EngineToos.getReturn(asmFieldType));
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        if (isReadOnly == false) {
            //set
            MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "set" + EngineToos.toUpperCase(propertyName), "(" + asmFieldType + ")V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);//װ��this
            mv.visitFieldInsn(GETFIELD, this.asmClassName, PropertyArrayName, PropertyDelegateArrayType);
            mv.visitIntInsn(BIPUSH, index);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ALOAD, 0);//װ��this
            mv.visitVarInsn(ALOAD, 1);//װ��param1
            mv.visitTypeInsn(CHECKCAST, asmFieldType2);
            mv.visitMethodInsn(INVOKEINTERFACE, asmDelegateType2, "set", "(Ljava/lang/Object;Ljava/lang/Object;)V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
    }
    //
    //ʵ�ֽӿڸ���
    private void visitInterfaceMethod(int classIndex, int methodIndex, BuilderClassAdapter adapter, MethodVisitor mv, Class<?> type, String name, String desc) {
        Pattern p = Pattern.compile("\\((.*)\\)(.*)");
        Matcher m = p.matcher(desc);
        m.find();
        String[] asmParams = EngineToos.splitAsmType(m.group(1));//"IIIILjava/lang/Integer;F[[[ILjava/lang.Boolean;"
        String asmReturns = EngineToos.asmTypeToType(m.group(2));
        int paramCount = asmParams.length;
        int localVarSize = paramCount;//�����������С
        int maxStackSize = 0;//��������ջ��С
        //-----------------------------------------------------------------------------------------------------------------------
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, this.asmClassName, DelegateArrayName, DelegateArrayType);
        mv.visitIntInsn(BIPUSH, classIndex);
        mv.visitInsn(AALOAD);
        //����1
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, this.asmClassName, DelegateMethodArrayName, DelegateMethodArrayType);
        mv.visitIntInsn(BIPUSH, methodIndex);
        mv.visitInsn(AALOAD);
        //����2
        mv.visitVarInsn(ALOAD, 0);
        //����3
        mv.visitIntInsn(BIPUSH, paramCount);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        for (int i = 0; i < paramCount; i++) {
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, i);
            String asmType = asmParams[i];
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
            maxStackSize = (maxStackSize < 5 + i) ? 5 + i : maxStackSize;
        }
        //����
        String delegateType2 = EngineToos.replaceClassName(MethodDelegate.class.getName());
        mv.visitMethodInsn(INVOKEINTERFACE, delegateType2, "invoke", "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
        //return
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
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
        } else {
            mv.visitTypeInsn(CHECKCAST, asmReturns);
            mv.visitInsn(ARETURN);
        }
        /* �����ջ�б� */
        mv.visitMaxs(maxStackSize, localVarSize + 1);
        mv.visitEnd();
    }
    //
    //����ĳ���ֶε�set����
    private void putSetMethod(String propertyName, String asmFieldType) {
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "set" + EngineToos.toUpperCase(propertyName), "(" + asmFieldType + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);//װ��this
        mv.visitVarInsn(ALOAD, 1);//װ�ز���
        mv.visitFieldInsn(PUTFIELD, this.asmClassName, propertyName, asmFieldType);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
    //
    //����ĳ���ֶε�get����
    private void putGetMethod(String propertyName, String asmFieldType) {
        //get
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "get" + EngineToos.toUpperCase(propertyName), "()" + asmFieldType, null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);//װ��this
        mv.visitFieldInsn(GETFIELD, this.asmClassName, propertyName, asmFieldType);
        mv.visitInsn(EngineToos.getReturn(asmFieldType));
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
}