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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.more.InvokeException;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassVisitor;
import org.more.core.asm.Label;
import org.more.core.asm.MethodVisitor;
import org.more.core.asm.Opcodes;
/**
 *����������������ɵ����м���aop��֧�֡�
 * @version 2010-9-2
 * @author ������ (zyc@byshell.org)
 */
class AopClassAdapter extends ClassAdapter implements Opcodes {
    private ClassBuilder       classBuilder        = null;
    private String             asmClassName        = null;
    //
    /**���ɵ�Aop����ǰ׺*/
    public final static String AopMethodPrefix     = "$method_";
    /**���ɵ��ֶ���*/
    public final static String AopMethodArrayName  = "$aopMethods";
    /**���ɵ��ֶ���*/
    public final static String AopFilterChainName  = "$aopfilterChain";
    /**����aop���Եķ����ض�����*/
    private ArrayList<String>  renderAopMethodList = new ArrayList<String>();
    //==================================================================================Constructor
    public AopClassAdapter(ClassVisitor visitor, ClassBuilder classBuilder) {
        super(visitor);
        this.classBuilder = classBuilder;
    }
    /**��ȡ����aop���Եķ������ϡ�*/
    public ArrayList<String> getRenderAopMethodList() {
        return this.renderAopMethodList;
    }
    /**asm.visit�����ڱ���������*/
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.asmClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }
    /**asm.visitMethod������һ��������*/
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        ClassEngine ce = this.classBuilder.getClassEngine();
        AopStrategy aopStrategy = ce.getAopStrategy();//��ȡAop���Զ���
        //
        //1.׼������������ݣ��÷�������ҪĿ���Ǵ�desc�в�ֳ�������ͷ���ֵ��
        Pattern p = Pattern.compile("\\((.*)\\)(.*)");
        Matcher m = p.matcher(desc);
        m.find();
        String[] asmParams = EngineToos.splitAsmType(m.group(1));//"IIIILjava/lang/Integer;F[[[ILjava/lang.Boolean;"
        String asmReturns = m.group(2);
        asmReturns = (asmReturns.charAt(0) == 'L') ? asmReturns.substring(1, asmReturns.length() - 1) : asmReturns;
        //
        //2.���Թ��췽����aop��װ���ῼ�ǹ��췽����
        if (name.equals("<init>") == true)
            return super.visitMethod(access, name, desc, signature, exceptions);
        //
        //3.ִ�з������Բ��ԣ�����aop���Զ������������Եķ����б�
        try {
            Class<?> superClass = ce.getSuperClass();
            Class<?>[] paramTypes = EngineToos.toJavaType(asmParams, ce);
            Method method = EngineToos.findMethod(superClass, name, paramTypes);
            if (aopStrategy.isIgnore(superClass, method) == true)
                return super.visitMethod(access, name, desc, signature, exceptions);//���Է���
        } catch (Exception e) {
            throw new InvokeException(e);
        }
        //
        //4.���Aop��������
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        mv.visitCode();
        String aopMethod = AopMethodPrefix + name + desc;//�ϳ��·�������������
        this.renderAopMethodList.add(aopMethod);
        int index = this.renderAopMethodList.indexOf(aopMethod);//ȷ���·��������������������·�����
        this.visitAOPMethod(index, mv, name, desc);//����·�����
        mv.visitEnd();
        //
        //5.������������Ϸ���
        String newMethodName = AopMethodPrefix + name;
        return super.visitMethod(access, newMethodName, desc, signature, exceptions);
    }
    /**asm.visitEnd�����aop��Ҫ���ض����ԡ�*/
    public void visitEnd() {
        //���FilterChain�����飬�ǽ���Aop�Ĺ���������
        this.putSimpleProperty(AopFilterChainName, AopFilterChain_Start[].class);
        //���Method�����飬Method�������Aop������
        this.putSimpleProperty(AopMethodArrayName, Method[].class);
        super.visitEnd();
    }
    /**��������ԣ�visitEnd�������ã��������ĳһ�����Ե�set���������ֶΡ�*/
    private void putSimpleProperty(String propertyName, Class<?> propertyType) {
        String asmFieldType = EngineToos.toAsmType(propertyType);
        super.visitField(ACC_PRIVATE, propertyName, asmFieldType, null, null);
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "set" + EngineToos.toUpperCase(propertyName), "(" + asmFieldType + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);//װ��this
        mv.visitVarInsn(ALOAD, 1);//װ�ز���
        mv.visitFieldInsn(PUTFIELD, this.asmClassName, propertyName, asmFieldType);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
    /**ʵ��AOP���������������ָ�����ASM3.2��{@link Opcodes}�ӿڶ��塣 */
    public void visitAOPMethod(final int index, final MethodVisitor mv, final String originalMethodName, final String desc) {//, final Method method) {
        //
        //1.׼�������������
        Pattern p = Pattern.compile("\\((.*)\\)(.*)");
        Matcher m = p.matcher(desc);
        m.find();
        String[] asmParams = EngineToos.splitAsmType(m.group(1));//"IIIILjava/lang/Integer;F[[[ILjava/lang.Boolean;"
        String asmReturns = m.group(2);
        int paramCount = asmParams.length;
        int localVarSize = paramCount + 1;//�����������С
        int maxStackSize = 0;//��������ջ��С
        //
        //2.�������
        mv.visitVarInsn(ALOAD, 0);//װ��this
        mv.visitFieldInsn(GETFIELD, this.asmClassName, AopFilterChainName, EngineToos.toAsmType(AopFilterChain_Start[].class));
        Label ifTag = new Label();
        mv.visitJumpInsn(IFNONNULL, ifTag);
        //return this.$method_passObject(param);
        mv.visitVarInsn(ALOAD, 0);
        for (int i = 0; i < paramCount; i++)
            mv.visitVarInsn(ALOAD, i + 1);//װ�ز���
        mv.visitMethodInsn(INVOKEVIRTUAL, this.asmClassName, AopMethodPrefix + originalMethodName, desc);
        mv.visitInsn(EngineToos.getReturn(asmReturns));
        mv.visitLabel(ifTag);
        //else
        mv.visitVarInsn(ALOAD, 0);//װ��this
        mv.visitFieldInsn(GETFIELD, this.asmClassName, AopFilterChainName, EngineToos.toAsmType(AopFilterChain_Start[].class));
        mv.visitIntInsn(BIPUSH, index);
        mv.visitInsn(AALOAD);//װ������ĵ�index��Ԫ��
        //param 1 this
        mv.visitVarInsn(ALOAD, 0);
        //param 2 $aopMethod[6]
        mv.visitVarInsn(ALOAD, 0);//
        mv.visitFieldInsn(GETFIELD, this.asmClassName, AopMethodArrayName, EngineToos.toAsmType(Method[].class));
        mv.visitIntInsn(BIPUSH, index);
        mv.visitInsn(AALOAD);
        //param 3 new Object[] { param }
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
        }
        //chain.doInvokeFilter(this, $aopMethod[6], new Object[] { param })
        mv.visitMethodInsn(INVOKEVIRTUAL, EngineToos.replaceClassName(AopFilterChain_Start.class.getName()), "doInvokeFilter", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;");
        //return (String)a;
        if (asmReturns.equals("B") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
        } else if (asmReturns.equals("S") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
        } else if (asmReturns.equals("I") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
        } else if (asmReturns.equals("J") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
        } else if (asmReturns.equals("F") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
        } else if (asmReturns.equals("D") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
        } else if (asmReturns.equals("C") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
        } else if (asmReturns.equals("Z") == true) {
            mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
        } else if (asmReturns.equals("V") == true) {
            //void
        } else {
            String asmReturnsType = (asmReturns.charAt(0) == 'L') ? asmReturns.substring(1, asmReturns.length() - 1) : asmReturns;
            mv.visitTypeInsn(CHECKCAST, asmReturnsType);
        }
        mv.visitInsn(EngineToos.getReturn(asmReturns));
        /* �����ջ�б� */
        mv.visitMaxs(maxStackSize, localVarSize + 1);
    }
}