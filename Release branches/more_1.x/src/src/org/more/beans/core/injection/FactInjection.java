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
package org.more.beans.core.injection;
import java.util.ArrayList;
import java.util.Collections;
import org.more.beans.BeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProperty;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassVisitor;
import org.more.core.asm.ClassWriter;
import org.more.core.asm.MethodVisitor;
import org.more.core.asm.Opcodes;
import org.more.core.classcode.ClassEngine;
import org.more.core.classcode.EngineToos;
import org.more.core.classcode.ClassEngine.BuilderMode;
import org.more.util.StringConvert;
/**
 * ��Fact��ʽ���״�����װ����ʱmore.beans������һ������ע���࣬����ʹ���������ע�������ע�롣
 * �������ע����Ĵ�������ȫ��more.classcode�������ɣ����ɵ������ʹ����ԭʼ�ķ�ʽ��bean����get/set��
 * Fact��ʽ�Ƚ�Ioc��ʽʡ���˷���ע��Ĺ��̣�Fact����ֱ�ӵ��÷�����������ע�룬�Ӷ����������ٶȡ���������
 * fact��ʽ�������ٶ���ԭʼget/set�����ٶ��൱�ӽ���100��ν��л�����������ע���ٶ�ֻ���15�������
 * ��1000���ע�������get/set������312�����fact������843���룬ioc��ʽ����Ҫ����18.3�롣
 * �����֤����Fact��ʽ�»��кܺõ�����ע������Ч�ʣ�����FactҲ���ÿ��Ҫ��Fact��bean����һ��ע������
 * ��Ҳ����˵��fact��ʽ�»��ioc��ʽ���������ڴ����ġ����ɵ�ע������������BeanDefinition�������С�
 * ֻ��BeanDefinition���󱻻��������������Ч�ʣ�����fact��Ч�ʿ���ԶԶ����ioc��
 * Date : 2009-11-7
 * @author ������
 */
public class FactInjection implements Injection {
    //========================================================================================Field
    /** ���Ի�����󣬻����������� */
    private String factCatchName = "$more_Injection_fact";
    //==========================================================================================Job
    /**
     * ȡ�û����factע��������������棬��������ڻ����򴴽�������沢�����ɴ����ࡣ
     * ���ݴ��������洴��һ�������������ִ�д��������Ĵ���ע�뷽����ע�뷽����ʹ����ԭʼ��get/set��ʽ����ע�롣
     * ���ע��������������ö�����������context��getBean���á������෽����Ϊ$org_more_beans_core_injection_FactIoc_iocMethod
     * ��˸��ӵķ�������Ϊ��ȷ����������в�������������������factע��ģʽ�´�����ֻ�����ɡ�
     */
    @Override
    public void ioc(final Object object, final Object[] params, final BeanDefinition definition, final BeanFactory context) throws Throwable {
        FactIoc fact = null;
        if (definition.containsKey(this.factCatchName) == true)
            //��ȡ����ע������������
            fact = (FactIoc) definition.get(this.factCatchName);
        else {
            //����ClassEngine����
            ClassEngine engine = new ClassEngine(context.getBeanClassLoader()) {
                protected ClassAdapter acceptClass(ClassWriter classWriter) {
                    return new FactClassAdapter(classWriter, object.getClass().getName(), definition);
                }
            };
            //�������������Ϣ
            engine.setSuperClass(FactIocObject.class);//���ڲ�֧��rt.jar�е��������Ҫһ����Object����
            engine.setMode(BuilderMode.Super);
            engine.setEnableAOP(false);
            fact = (FactIoc) engine.newInstance(null);
            //�����������ɶ���
            definition.setAttribute(this.factCatchName, fact);
        }
        /*ִ��factע��*/
        fact.ioc(object, params, context, definition);
    }
}
/** �����дioc������ʵ��fact��ʽע�롣*/
class FactClassAdapter extends ClassAdapter implements Opcodes {
    //========================================================================================Field
    private BeanDefinition definition = null; //Bean
    private String         className  = null; //ע��������
    //==================================================================================Constructor
    public FactClassAdapter(ClassVisitor cv, String objectClassName, BeanDefinition definition) {
        super(cv);
        this.className = EngineToos.replaceClassName(objectClassName);
        this.definition = definition;
    }
    //==========================================================================================Job
    /** ���ӽӿ�ʵ�� */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ArrayList<String> al = new ArrayList<String>(0);
        Collections.addAll(al, interfaces);
        al.add(EngineToos.replaceClassName(FactIoc.class.getName()));
        String[] ins = new String[al.size()];
        al.toArray(ins);
        super.visit(version, access, name, signature, superName, ins);
    }
    @Override
    public void visitEnd() {
        String desc = EngineToos.toAsmType(new Class<?>[] { Object.class, Object[].class, BeanFactory.class, BeanDefinition.class });
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "ioc", "(" + desc + ")V", null, new String[] { "java/lang/Throwable" });
        //����ע�뷽��
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, this.className);
        mv.visitVarInsn(ASTORE, 5);
        BeanProperty[] bps = this.definition.getPropertys();
        for (int i = 0; i < bps.length; i++) {
            mv.visitVarInsn(ALOAD, 5);
            String propTypeByASM = null;
            BeanProperty prop = bps[i];
            //
            String propType = prop.getPropType();
            if (propType == BeanProperty.TS_Integer) {
                mv.visitIntInsn(BIPUSH, StringConvert.parseInt(prop.getValue()));
                propTypeByASM = "I";
            } else if (propType == BeanProperty.TS_Byte) {
                mv.visitIntInsn(BIPUSH, StringConvert.parseByte(prop.getValue()));
                propTypeByASM = "B";
            } else if (propType == BeanProperty.TS_Char) {
                if (prop.getValue() == null)
                    mv.visitIntInsn(BIPUSH, 0);
                else
                    mv.visitIntInsn(BIPUSH, prop.getValue().charAt(0));
                propTypeByASM = "C";
            } else if (propType == BeanProperty.TS_Double) {
                mv.visitLdcInsn(StringConvert.parseDouble(prop.getValue()));
                propTypeByASM = "D";
            } else if (propType == BeanProperty.TS_Float) {
                mv.visitLdcInsn(StringConvert.parseFloat(prop.getValue()));
                propTypeByASM = "F";
            } else if (propType == BeanProperty.TS_Long) {
                mv.visitLdcInsn(StringConvert.parseLong(prop.getValue()));
                propTypeByASM = "J";
            } else if (propType == BeanProperty.TS_Short) {
                mv.visitIntInsn(BIPUSH, StringConvert.parseShort(prop.getValue()));
                propTypeByASM = "S";
            } else if (propType == BeanProperty.TS_Boolean) {
                boolean bool = StringConvert.parseBoolean(prop.getValue());
                mv.visitInsn((bool == true) ? ICONST_1 : ICONST_0);
                propTypeByASM = "Z";
            } else if (propType == BeanProperty.TS_String) {
                mv.visitLdcInsn(prop.getValue());
                propTypeByASM = "Ljava/lang/String;";
            } else if (propType == BeanProperty.TS_Array) {
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitMethodInsn(INVOKEVIRTUAL, "org/more/beans/info/BeanDefinition", "getPropertys", "()[Lorg/more/beans/info/BeanProperty;");
                mv.visitIntInsn(BIPUSH, i);
                mv.visitInsn(AALOAD);
                mv.visitVarInsn(ALOAD, 3);
                String descStr = EngineToos.toAsmType(new Class<?>[] { Object.class, Object[].class, BeanProperty.class, BeanFactory.class });
                mv.visitMethodInsn(INVOKESTATIC, "org/more/beans/core/injection/TypeParser", "passerArray", "(" + descStr + ")Ljava/lang/Object;");
                mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Object;");
                propTypeByASM = "[Ljava/lang/Object;";
            } else if (propType == BeanProperty.TS_List) {
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitMethodInsn(INVOKEVIRTUAL, "org/more/beans/info/BeanDefinition", "getPropertys", "()[Lorg/more/beans/info/BeanProperty;");
                mv.visitIntInsn(BIPUSH, i);
                mv.visitInsn(AALOAD);
                mv.visitVarInsn(ALOAD, 3);
                String descStr = EngineToos.toAsmType(new Class<?>[] { Object.class, Object[].class, BeanProperty.class, BeanFactory.class });
                mv.visitMethodInsn(INVOKESTATIC, "org/more/beans/core/injection/TypeParser", "passerList", "(" + descStr + ")Ljava/util/List;");
                propTypeByASM = "Ljava/util/List;";
            } else if (propType == BeanProperty.TS_Map) {
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitMethodInsn(INVOKEVIRTUAL, "org/more/beans/info/BeanDefinition", "getPropertys", "()[Lorg/more/beans/info/BeanProperty;");
                mv.visitIntInsn(BIPUSH, i);
                mv.visitInsn(AALOAD);
                mv.visitVarInsn(ALOAD, 3);
                String descStr = EngineToos.toAsmType(new Class<?>[] { Object.class, Object[].class, BeanProperty.class, BeanFactory.class });
                mv.visitMethodInsn(INVOKESTATIC, "org/more/beans/core/injection/TypeParser", "passerMap", "(" + descStr + ")Ljava/util/Map;");
                propTypeByASM = "Ljava/util/Map;";
            } else if (propType == BeanProperty.TS_Set) {
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitMethodInsn(INVOKEVIRTUAL, "org/more/beans/info/BeanDefinition", "getPropertys", "()[Lorg/more/beans/info/BeanProperty;");
                mv.visitIntInsn(BIPUSH, i);
                mv.visitInsn(AALOAD);
                mv.visitVarInsn(ALOAD, 3);
                String descStr = EngineToos.toAsmType(new Class<?>[] { Object.class, Object[].class, BeanProperty.class, BeanFactory.class });
                mv.visitMethodInsn(INVOKESTATIC, "org/more/beans/core/injection/TypeParser", "passerSet", "(" + descStr + ")Ljava/util/Set;");
                propTypeByASM = "Ljava/util/Set;";
            } else if (prop.getRefBean() != null) {
                //refBean
                mv.visitVarInsn(ALOAD, 3);
                mv.visitLdcInsn(prop.getRefBean());
                mv.visitVarInsn(ALOAD, 2);
                String descStr = EngineToos.toAsmType(new Class<?>[] { String.class, Object[].class });
                mv.visitMethodInsn(INVOKEINTERFACE, "org/more/beans/BeanFactory", "getBean", "(" + descStr + ")Ljava/lang/Object;");
                String propTypeASM = prop.getPropType().replace(".", "/");
                mv.visitTypeInsn(CHECKCAST, propTypeASM);
                propTypeByASM = "L" + propTypeASM + ";";
            } else {
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitMethodInsn(INVOKEVIRTUAL, "org/more/beans/info/BeanDefinition", "getPropertys", "()[Lorg/more/beans/info/BeanProperty;");
                mv.visitIntInsn(BIPUSH, i);
                mv.visitInsn(AALOAD);
                mv.visitVarInsn(ALOAD, 3);
                String descStr = EngineToos.toAsmType(new Class<?>[] { Object.class, Object[].class, BeanProperty.class, BeanFactory.class });
                mv.visitMethodInsn(INVOKESTATIC, "org/more/beans/core/injection/TypeParser", "passerType", "(" + descStr + ")Ljava/lang/Object;");
                String propTypeASM = prop.getPropType().replace(".", "/");
                propTypeByASM = "L" + propTypeASM + ";";
                mv.visitTypeInsn(CHECKCAST, propTypeASM);
            }
            //ת������ĸ��д
            StringBuffer sb = new StringBuffer(prop.getName());
            char firstChar = sb.charAt(0);
            sb.delete(0, 1);
            firstChar = (char) ((firstChar >= 97) ? firstChar - 32 : firstChar);
            sb.insert(0, firstChar);
            sb.insert(0, "set");
            mv.visitMethodInsn(INVOKEVIRTUAL, this.className, sb.toString(), "(" + propTypeByASM + ")V");
            //this.setMeyhodName(...);
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(5, 4);
        mv.visitEnd();
        super.visitEnd();
    }
}