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
package org.more.util.attribute;
import org.more.core.error.DefineException;
import org.more.core.error.RepeateException;
/**
 * ��չ�����Բ����ӿڣ��ýӿ���ǿ��Attribute�ӿڡ��ýӿڽ��������������ʱ������ͻ�����⡣
 * �����õ���������ԭ�����Է���������ͻʱ����ʹ�á����Ե��滻ԭ�򡱽����滻�����������¼����滻ԭ��:
 * ReplaceMode_Replace  (�������滻)��ԭ����Ĭ���滻ԭ��<br/>
 * ReplaceMode_Original(����������ֵ����ԭʼ����)<br/>
 * ReplaceMode_Throw   (��������������滻�������׳��쳣)��
 * @version 2009-4-30
 * @author ������ (zyc@byshell.org)
 */
public class ExtAttDecorator<T> extends AbstractAttDecorator<T> {
    //========================================================================================Field
    /** ���滻ģʽ(replacMode����)����ReplaceMode_Replaceʱ��������������������滻ԭ�����ԡ����滻ԭ����Ĭ���滻ԭ�� */
    public static final int ReplaceMode_Replace  = 0;
    /** 
     * ���滻ģʽ(replacMode����)����ReplaceMode_Originalʱ���������������������������Ա���ԭʼ���ԣ�
     * ������ģʽ�¿���ѡ�����Ƴ��������������ԡ� 
     */
    public static final int ReplaceMode_Original = 1;
    /** ���滻ģʽ(replacMode����)����ReplaceMode_Throwʱ��������������������׳�RepeateException�쳣�� */
    public static final int ReplaceMode_Throw    = 2;
    /** �����滻���� */
    private int             replaceMode          = ReplaceMode_Replace;
    //==================================================================================Constructor
    /**
     * ��չ����װ��������װ����ʵ����IExtAttribute�ӿڵĹ��ܡ��滻��������IExtAttribute�ӿڶ��塣
     * �÷���������Ĭ�ϲ���IExtAttribute.ReplaceMode_Replace��
     * @param source Ҫװ�ε�Ŀ�����Զ���
     */
    public ExtAttDecorator(IAttribute<T> source) {
        super(source);
    }
    /**
     * ����һ������װ��������װ��������Ҫ�������������Զ�����滻���Ե�֧�֡�
     * ���ʹ����һ�������ڵ�ֵ���ж����������NoDefinitionException�쳣��
     * @param source Ҫװ�ε�Ŀ�����Զ���
     * @param replaceMode Ҫ���ĵ��滻���Բ���ֵ����ֵ������ReplaceMode������ġ�
     * @throws DefineException ������һ�������ڵ����Բ��ԡ�
     */
    public ExtAttDecorator(IAttribute<T> source, int replaceMode) throws DefineException {
        super(source);
        this.setReplacMode(replaceMode);
    }
    //==========================================================================================Job
    /**
     * ��ȡ���Ե��滻ģʽ��ֵ����ExtAttDecorator���ReplaceMode�ֶζ��塣
     * @return �������Ե��滻ģʽ��ֵ����ExtAttDecorator���ReplaceMode�ֶζ��塣
     */
    public int getReplaceMode() {
        return this.replaceMode;
    }
    /**
     * �ı���չ����ʵ�����������滻���ԣ����ʹ����һ�������ڵ�ֵ���ж����������NoDefinitionException�쳣��
     * @param replaceMode Ҫ���ĵ��滻���Բ���ֵ����ֵ������ReplaceMode������ġ�
     * @throws DefineException ������һ�������ڵ����Բ��ԡ�
     */
    protected void setReplacMode(int replaceMode) throws DefineException {
        if (replaceMode == ReplaceMode_Original || replaceMode == ReplaceMode_Replace || replaceMode == ReplaceMode_Throw)
            this.replaceMode = replaceMode;
        else
            throw new DefineException("��֧�ֵ������滻���� " + replaceMode);
    }
    /**
     * �������ԣ����滻ģʽ(replaceMode����)����ReplaceMode_Replaceʱ��������������������滻ԭ�����ԡ�<br/>
     * ���滻ģʽ(replaceMode����)����ReplaceMode_Originalʱ���������������������������Ա���ԭʼ���ԣ�
     * ������ģʽ���û�����ѡ�����Ƴ��������������ԡ�<br/> ���滻ģʽ(replaceMode����)����ReplaceMode_Throwʱ��
     * ������������������׳�RepeateException�쳣��
     * @param name Ҫ�����������
     * @param value Ҫ���������ֵ
     * @throws RepeateException ���滻ģʽ(replaceMode����)����ReplaceMode_Throwʱ������������������ԡ�
     */
    public void setAttribute(String name, T value) throws RepeateException {
        if (this.getSource().contains(name) == true)
            switch (this.replaceMode) {
            case ReplaceMode_Original://����ԭʼ����
                break;
            case ReplaceMode_Throw://�׳��쳣
                throw new RepeateException("�Ѿ����ڵ����� " + name);
            default://ExtAttribute.ReplaceMode_Replace �������滻
                this.getSource().setAttribute(name, value);
            }
        else
            this.getSource().setAttribute(name, value);
    }
}