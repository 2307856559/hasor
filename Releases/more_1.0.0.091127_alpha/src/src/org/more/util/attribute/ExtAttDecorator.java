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
import org.more.NoDefinitionException;
import org.more.RepeateException;
/**
 * ��չ����װ��������װ����ʵ����IExtAttribute�ӿڵĹ��ܡ�
 * Date : 2009-4-30
 * @author ������
 */
public class ExtAttDecorator extends AbstractAttDecorator implements IExtAttribute {
    /** �����滻���� */
    private int replaceMode = IExtAttribute.ReplaceMode_Replace;
    /**
     * ��չ����װ��������װ����ʵ����IExtAttribute�ӿڵĹ��ܡ��滻��������IExtAttribute�ӿڶ��塣
     * �÷���������Ĭ�ϲ���IExtAttribute.ReplaceMode_Replace��
     * @param source Ҫװ�ε�Ŀ�����Զ���
     */
    public ExtAttDecorator(IAttribute source) {
        super(source);
    }
    /**
     * ����һ������װ��������װ��������Ҫ�������������Զ�����滻���Ե�֧�֡��滻��������IExtAttribute�ӿڶ��塣
     * @param source Ҫװ�ε�Ŀ�����Զ���
     * @param replaceMode Ҫ���ĵ��滻���Բ���ֵ����ֵ������IExtAttribute.ReplaceMode������ġ�
     *                   ���ʹ����һ�������ڵ�ֵ���ж����������NoDefinitionException�쳣��
     * @throws NoDefinitionException ������һ�������ڵ����Բ��ԡ�
     */
    public ExtAttDecorator(IAttribute source, int replaceMode) throws NoDefinitionException {
        super(source);
        this.setReplacMode(replaceMode);
    }
    @Override
    public int getReplaceMode() {
        return this.replaceMode;
    }
    /**
     * �ı���չ����ʵ�����������滻���ԡ�
     * @param replaceMode Ҫ���ĵ��滻���Բ���ֵ����ֵ������IExtAttribute.ReplaceMode������ġ�
     *                   ���ʹ����һ�������ڵ�ֵ���ж����������NoDefinitionException�쳣��
     * @throws NoDefinitionException ������һ�������ڵ����Բ��ԡ�
     */
    protected void setReplacMode(int replaceMode) throws NoDefinitionException {
        if (replaceMode == IExtAttribute.ReplaceMode_Original || replaceMode == IExtAttribute.ReplaceMode_Replace || replaceMode == IExtAttribute.ReplaceMode_Throw)
            this.replaceMode = replaceMode;
        else
            throw new NoDefinitionException("��֧�ֵ������滻���� " + replaceMode);
    }
    public void setAttribute(String name, Object value) throws RepeateException {
        if (this.getSource().contains(name) == true)
            switch (this.replaceMode) {
            case IExtAttribute.ReplaceMode_Original://����ԭʼ����
                break;
            case IExtAttribute.ReplaceMode_Throw://�׳��쳣
                throw new RepeateException("�Ѿ����ڵ����� " + name);
            default://ExtAttribute.ReplaceMode_Replace �������滻
                this.getSource().setAttribute(name, value);
            }
        else
            this.getSource().setAttribute(name, value);
    }
}
