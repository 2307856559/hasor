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
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.PropVarValue;
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
 * ��Ҳ����˵��fact��ʽ�»��ioc��ʽ���������ڴ����ġ����ɵ�ע������������{@link BeanDefinition}�������С�
 * ֻ��{@link BeanDefinition}���󱻻��������������Ч�ʣ�����fact��Ч�ʿ���ԶԶ����ioc��
 * <br/>Date : 2009-11-7
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
     * ���ע��������������ö�����������context��getBean���á���factע��ģʽ�´�����ֻ�����ɡ�
     */
    @Override
    public Object ioc(final Object object, final Object[] params, final BeanDefinition definition, final ResourceBeanFactory context) throws Exception {
        FactIoc fact = null;
        if (definition.containsKey(this.factCatchName) == true)
            //��ȡ����ע������������
            fact = (FactIoc) definition.get(this.factCatchName);
        else {
            //����ClassEngine����
            /*
             * �������д������ڽ��������AOP�������ҪFact��ʽִ��Ioc���޷���ȡiocbean��Class�������⡣
             * ��������ClassEngineʹ�õĸ���װ������context.getBeanClassLoader()���ֱ�ӻ�ȡ����Ҫioc
             * ��bean��ClassLoader���൱�ڻ�ȡ����context.getBeanClassLoader()��
             * ���ǵĹ�ϵ�����µ�:
             * systemClassLoader
             *   context.getBeanClassLoader()
             *     ClassEngine
             * ���������д���more.beans �ṩ��Fact��ʽע���µ�AOP��impl����֧�֡�
             */
            ClassLoader beanLoader = object.getClass().getClassLoader();
            if (beanLoader instanceof ClassEngine == false)
                beanLoader = context.getBeanClassLoader();
            //
            ClassEngine engine = new ClassEngine(beanLoader) {
                protected ClassAdapter acceptClass(ClassWriter classWriter) {
                    return new FactClassAdapter(classWriter, object.getClass().getName(), definition);
                }
            };
            //�������������Ϣ
            engine.setSuperClass(FactIocObject.class);//���ڲ�֧��rt.jar�е��������Ҫһ����Object����
            engine.setMode(BuilderMode.Super);
            engine.forceBuilderClass();
            fact = (FactIoc) engine.newInstance(null);
            //�����������ɶ���
            definition.setAttribute(this.factCatchName, fact);
        }
        /*ִ��factע��*/
        fact.ioc(object, params, definition, context);
        return object;
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
        String desc = EngineToos.toAsmType(new Class<?>[] { Object.class, Object[].class, BeanDefinition.class, ResourceBeanFactory.class });
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "ioc", "(" + desc + ")V", null, new String[] { "java/lang/Throwable" });
        //����ע�뷽��
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, this.className);
        mv.visitVarInsn(ASTORE, 5);
        BeanProperty[] bps = this.definition.getPropertys();
        for (int i = 0; i < bps.length; i++) {
            mv.visitVarInsn(ALOAD, 5);
            //String propTypeByASM = null;
            BeanProperty prop = bps[i];
            //
            String propType = prop.getPropType();
            if (propType == BeanProperty.TS_Integer) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                mv.visitIntInsn(BIPUSH, StringConvert.parseInt(var.getValue()));
                this.invokeMethod(mv, prop, "I");
            } else if (propType == BeanProperty.TS_Byte) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                mv.visitIntInsn(BIPUSH, StringConvert.parseByte(var.getValue()));
                this.invokeMethod(mv, prop, "B");
            } else if (propType == BeanProperty.TS_Char) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                if (var.getValue() == null)
                    mv.visitIntInsn(BIPUSH, 0);
                else
                    mv.visitIntInsn(BIPUSH, var.getValue().charAt(0));
                this.invokeMethod(mv, prop, "C");
            } else if (propType == BeanProperty.TS_Double) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                mv.visitLdcInsn(StringConvert.parseDouble(var.getValue()));
                this.invokeMethod(mv, prop, "D");
            } else if (propType == BeanProperty.TS_Float) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                mv.visitLdcInsn(StringConvert.parseFloat(var.getValue()));
                this.invokeMethod(mv, prop, "F");
            } else if (propType == BeanProperty.TS_Long) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                mv.visitLdcInsn(StringConvert.parseLong(var.getValue()));
                this.invokeMethod(mv, prop, "J");
            } else if (propType == BeanProperty.TS_Short) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                mv.visitIntInsn(BIPUSH, StringConvert.parseShort(var.getValue()));
                this.invokeMethod(mv, prop, "S");
            } else if (propType == BeanProperty.TS_Boolean) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                boolean bool = StringConvert.parseBoolean(var.getValue());
                mv.visitInsn((bool == true) ? ICONST_1 : ICONST_0);
                this.invokeMethod(mv, prop, "Z");
            } else if (propType == BeanProperty.TS_String) {
                PropVarValue var = (PropVarValue) prop.getRefValue();
                mv.visitLdcInsn(var.getValue());
                this.invokeMethod(mv, prop, "Ljava/lang/String;");
            } else {
                /*in          Object[]��BeanDefinition��ResourceBeanFactory */
                /*out Object��Object[]��BeanDefinition��ResourceBeanFactory��BeanProperty*/
                StringBuffer sb = new StringBuffer(prop.getName());
                char firstChar = sb.charAt(0);
                sb.delete(0, 1);
                firstChar = (char) ((firstChar >= 97) ? firstChar - 32 : firstChar);
                sb.insert(0, firstChar);
                sb.insert(0, "set");
                mv.visitLdcInsn(sb.toString());
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ALOAD, 4);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitMethodInsn(INVOKEVIRTUAL, "org/more/beans/info/BeanDefinition", "getPropertys", "()[Lorg/more/beans/info/BeanProperty;");
                mv.visitIntInsn(BIPUSH, i);
                mv.visitInsn(AALOAD);
                String descStr = EngineToos.toAsmType(new Class<?>[] { String.class, Object.class, Object[].class, BeanDefinition.class, ResourceBeanFactory.class, BeanProperty.class });
                mv.visitMethodInsn(INVOKESTATIC, "org/more/beans/core/TypeParser", "passerType", "(" + descStr + ")Ljava/lang/Object;");
                mv.visitInsn(POP);
            }
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(5, 4);
        mv.visitEnd();
        super.visitEnd();
    }
    private void invokeMethod(MethodVisitor mv, BeanProperty prop, String propTypeByASM) {
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
}