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
import java.util.ArrayList;
import org.more.core.asm.ClassAdapter;
import org.more.core.asm.ClassReader;
import org.more.core.asm.ClassVisitor;
import org.more.core.asm.ClassWriter;
/**
 * ��������������ͨ���̳и�����дacceptClass������ʹ��classcode����asm3.2�ĸ߼����ܡ�<br/>
 * �����ṩ��������չ�������������չ�����ֽ��뷽��Ĺ��ܡ�<br/>
 * <b>��չ��һ</b>��<br/>
 * ��{@link ClassBuilder#initBuilder(ClassEngine)}������ClassEngine����֮���ڽ�������֮ǰ�÷��������
 * {@link ClassBuilder#init(ClassEngine)}��������ʱ��չ����Գ�ʼ���Լ���������ݴ��롣<br/>
 * <b>��չ���</b>��<br/>
 * ��{@link ClassBuilder#builderClass()}������ClassEngine����֮���ڽ�����������{@link ClassConfiguration}
 * ֮ǰ�÷��������{@link ClassBuilder#builder(ClassEngine)}��������ʱ��չ����Թ�������class���߸�дԭ���ֽ��롣
 * ���������ϵĿ����������Ƽ�����������չ��ʽ����Ϊ��������ָ�д�ֽ������չ��ʽ�ǲ������ֽ��빹�����еġ�<br/>
 * <b>��չ����</b>��<br/>
 * {@link ClassBuilder#acceptClass(ClassVisitor)}������һ����Ϊ���ŵ���չ��ʽ���÷�������builderClass()����
 * ����visitor�������ڼ䷢����builderClass�������ֽ���ʱʹ�õ���ASM3.2�ṩ��visitorģʽ���÷����Ĳ���������Ҫ
 * д���visitor��ע��ʹ�ø���չ��ʽ����Ҫ��ϤASM3.2��ܡ�visitor���Ĳ�ι�ϵ�������ģ�<br/>
 * <b>��һ��</b>��ASM Write��<b>�ڶ���</b>���û���չ��<b>������</b>��ClassBuilder��<b>���Ļ�</b>��ASM Read
 * @version 2010-9-3
 * @author ������ (zyc@byshell.org)
 */
public class ClassBuilder {
    /**���ֶλᱻgetClassBytes()����*/
    protected byte[]               newClassBytes         = null; //������ֽ��롣
    private ClassEngine            classEngine           = null; //Class���档
    //property
    private String                 asmClassName          = null; //������ֽ�������
    private String                 asmSuperClassName     = null; //������ֽ�������
    private String[]               delegateString        = null; //ί������By ASM
    private Class<?>[]             delegateType          = null; //ί������By Class
    private String[]               simpleFields          = null; //������
    private String[]               delegateFields        = null; //ί������
    //aop
    private AopInvokeFilter[]      aopFilter             = null; //aop���������󼯺ϡ�
    private AopBeforeListener[]    aopBeforeListeners    = null; //before�����¼���������
    private AopReturningListener[] aopReturningListeners = null; //returning�����¼���������
    private AopThrowingListener[]  aopThrowingListeners  = null; //throwing�����¼���������
    //======================================================================================Get/Set
    /**��ȡʹ�õ�Class���档*/
    public ClassEngine getClassEngine() {
        return this.classEngine;
    }
    /**�������������(�ֽ�����ʽ)��*/
    public String getAsmClassName() {
        return this.asmClassName;
    }
    /**���ظ��������(�ֽ�����ʽ)��*/
    public String getAsmSuperClassName() {
        return this.asmSuperClassName;
    };
    /**�������õ�{@link AopInvokeFilter}���飬�ö�������{@link ClassEngine}�������á�*/
    public AopInvokeFilter[] getAopFilter() {
        return this.aopFilter;
    }
    /**�������õ�{@link AopBeforeListener}���飬�ö�������{@link ClassEngine}�������á�*/
    public AopBeforeListener[] getAopBeforeListeners() {
        return this.aopBeforeListeners;
    }
    /**�������õ�{@link AopReturningListener}���飬�ö�������{@link ClassEngine}�������á�*/
    public AopReturningListener[] getAopReturningListeners() {
        return this.aopReturningListeners;
    }
    /**�������õ�{@link AopThrowingListener}���飬�ö�������{@link ClassEngine}�������á�*/
    public AopThrowingListener[] getAopThrowingListeners() {
        return this.aopThrowingListeners;
    }
    /**����ί�нӿ�������ֽ�����ʽ��*/
    public String[] getDelegateString() {
        return this.delegateString;
    }
    /**����ί�нӿ����顣*/
    public Class<?>[] getDelegateType() {
        return this.delegateType;
    }
    /**���ظ��ӵ����������顣*/
    public String[] getSimpleFields() {
        return this.simpleFields;
    }
    /**���ظ��ӵ�ί�����Ե����������顣*/
    public String[] getDelegateFields() {
        return this.delegateFields;
    }
    //======================================================================================Is
    /** ����һ��boolean����ֵ�������Ƿ����ί�У�ί��ʵ���Ͼ���һ���ӿ�ʵ�֡����getDelegateString()����null��÷�������true�����򷵻�false�� */
    public boolean isAddDelegate() {
        return this.getDelegateString() != null;
    }
    /**
     * ����һ��boolean����ֵ�������Ƿ��������ĸ������ԣ���Щ���԰����˼����Ժʹ������ԡ�
     * ���getSimpleFields()��getDelegateFields()ͬʱ����null��÷�������true�����򷵻�false��
     */
    public boolean isAddFields() {
        if (this.getSimpleFields() == null && this.getDelegateFields() == null)
            return false;
        else
            return true;
    }
    /**
     * ����һ��boolean����ֵ���Ծ����Ƿ���Ⱦaop��װ�䡣builderClass��������ʱ�Ƿ�������aop֧�ֵĴ������ͨ���÷������ж���
     * ���getAopFilter()��getAopBeforeListeners()��getAopReturningListeners()��getAopThrowingListeners()����ֵ����nullʱ���÷�������ֵ������false������true��
     */
    public boolean isRenderAop() {
        if (this.getAopFilter() == null && this.getAopBeforeListeners() == null && this.getAopReturningListeners() == null && this.getAopThrowingListeners() == null)
            return false;
        else
            return true;
    }
    //=======================================================================================Method
    /**��ȡ�Ѿ����ɵ��ֽ������顣*/
    public byte[] getClassBytes() {
        return this.newClassBytes;
    }
    //======================================================================================Builder
    /**��ʼ����������*/
    public final void initBuilder(ClassEngine classEngine) {
        this.newClassBytes = null; //������ֽ��롣
        this.classEngine = null; //Class���档
        this.asmClassName = null; //������ֽ�������
        this.asmSuperClassName = null; //������ֽ�������
        this.delegateString = null; //ί������By ASM
        this.delegateType = null; //ί������By Class
        this.simpleFields = null; //������
        this.delegateFields = null; //ί������
        this.aopFilter = null; //aop���������󼯺ϡ�
        this.aopBeforeListeners = null; //before�����¼���������
        this.aopReturningListeners = null; //returning�����¼���������
        this.aopThrowingListeners = null; //throwing�����¼���������
        //---------------------------------------------------------
        this.classEngine = classEngine;
        //1.config
        this.asmClassName = EngineToos.replaceClassName(classEngine.getClassName());
        this.asmSuperClassName = EngineToos.replaceClassName(classEngine.getSuperClass().getName());
        //2.AOP
        this.aopFilter = classEngine.getAopFilters();
        this.aopBeforeListeners = classEngine.getAopBeforeListeners();
        this.aopReturningListeners = classEngine.getAopReturningListeners();
        this.aopThrowingListeners = classEngine.getAopThrowingListeners();
        //3.Delegate
        Class<?>[] delegateType = classEngine.getDelegates();
        if (delegateType != null) {
            //1
            DelegateStrategy delegateStrategy = this.classEngine.getDelegateStrategy();
            ArrayList<Class<?>> delegateTypeList = new ArrayList<Class<?>>();
            for (int i = 0; i < delegateType.length; i++) {
                if (delegateStrategy.isIgnore(delegateType[i]) == true)
                    continue;
                delegateTypeList.add(delegateType[i]);
            }
            //2
            int size = delegateTypeList.size();
            if (size != 0) {
                this.delegateString = new String[size];
                this.delegateType = new Class<?>[size];
                for (int i = 0; i < delegateTypeList.size(); i++) {
                    Class<?> dType = delegateTypeList.get(i);
                    this.delegateType[i] = dType;
                    this.delegateString[i] = EngineToos.replaceClassName(dType.getName());
                }
            }
        }
        //4.Field
        this.simpleFields = classEngine.getAppendSimplePropertys();
        this.delegateFields = classEngine.getAppendDelegatePropertys();
        //5.init
        this.init(this.classEngine);
    }
    /**���ù������̹����µ��ֽ������*/
    public final ClassConfiguration builderClass() throws IOException {
        //1.������Ϣ
        ClassEngine engine = this.classEngine;
        Class<?> superClass = engine.getSuperClass();
        //2.����visitor��
        //------��һ����д��
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //------�ڶ������û���չ
        ClassAdapter visitor = this.acceptClass(writer);
        //------��������Aop
        AopClassAdapter aopAdapter = null;
        if (this.isRenderAop() == true) {
            aopAdapter = new AopClassAdapter((visitor == null) ? writer : visitor, this);
            visitor = aopAdapter;
        }
        //------���Ļ���Builder
        visitor = (visitor != null) ? new BuilderClassAdapter(visitor, this) : new BuilderClassAdapter(writer, this);
        BuilderClassAdapter builderAdapter = (BuilderClassAdapter) visitor;
        //3.Read
        ClassReader reader = new ClassReader(EngineToos.getClassInputStream(superClass));//����ClassReader
        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        this.newClassBytes = writer.toByteArray();
        this.newClassBytes = this.builder(this.newClassBytes, this.classEngine);
        if (this.newClassBytes == null)
            return null;
        else
            return new ClassConfiguration(this, builderAdapter, aopAdapter);
    }
    //======================================================================================Builder
    /**
     * {@link ClassBuilder#acceptClass(ClassVisitor)}������һ����Ϊ���ŵ���չ��ʽ���÷�������builderClass()����
     * ����visitor�������ڼ䷢����builderClass�������ֽ���ʱʹ�õ���ASM3.2�ṩ��visitorģʽ���÷����Ĳ���������Ҫ
     * д���visitor��ע��ʹ�ø���չ��ʽ����Ҫ��ϤASM3.2��ܡ�visitor���Ĳ�ι�ϵ�������ģ�<br/>
     * <b>��һ��</b>��ASM Write��<b>�ڶ���</b>���û���չ��<b>������</b>��Aop��<b>���Ļ�</b>��ASM Read
     */
    protected ClassAdapter acceptClass(final ClassWriter classVisitor) {
        return null;
    }
    /**
     * ��{@link ClassBuilder#initBuilder(ClassEngine)}������ClassEngine����֮���ڽ�������֮ǰ�÷��������
     * {@link ClassBuilder#init(ClassEngine)}��������ʱ�������ͨ����д�÷�������ʼ���Լ���������ݡ�
     */
    protected void init(final ClassEngine classEngine) {}
    /**
     * ��{@link ClassBuilder#builderClass()}������ClassEngine����֮���ڽ�����������{@link ClassConfiguration}
     * ֮ǰ�÷��������{@link ClassBuilder#builder(ClassEngine)}��������ʱ�������ͨ����д�÷�������������class���߸�дԭ���ֽ��롣
     * @param classEngine ʹ�õ��ֽ����������
     */
    protected byte[] builder(final byte[] newClassBytes, final ClassEngine classEngine) {
        return newClassBytes;
    }
}