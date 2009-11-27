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
/**
 * ���ɵ�����װ��������װ����ʵ����IFreeAttribute�ӿڵĹ��ܡ�
 * Date : 2009-4-30
 * @author ������
 */
public class FreeAttDecorator extends ExtAttDecorator implements IFreeAttribute {
    /**
     * ����һ�����ɵ�����װ��������װ����ʵ����IFreeAttribute�ӿڵĹ��ܡ�
     * �÷���������Ĭ�ϲ���IExtAttribute.ReplaceMode_Replace��
     * @param source Ҫװ�ε�Ŀ�����Զ���
     */
    public FreeAttDecorator(IAttribute source) {
        super(source);
    }
    /**
     * ����һ������װ��������װ������չ��ExtAttDecoratorװ���������ṩ�˸��������滻���Ե�֧�֡�
     * @param source Ҫװ�ε�Ŀ�����Զ���
     * @param replaceMode Ҫ���ĵ��滻���Բ���ֵ����ֵ������IExtAttribute.ReplaceMode������ġ�
     *                   ���ʹ����һ�������ڵ�ֵ���ж����������NoDefinitionException�쳣��
     * @throws NoDefinitionException ������һ�������ڵ����Բ��ԡ�
     */
    public FreeAttDecorator(IAttribute source, int replaceMode) throws NoDefinitionException {
        super(source);
        this.setReplacMode(replaceMode);
    }
    @Override
    public void changeOriginal() {
        this.setReplacMode(IFreeAttribute.ReplaceMode_Original);
    }
    @Override
    public void changeReplace() {
        this.setReplacMode(IFreeAttribute.ReplaceMode_Replace);
    }
    @Override
    public void changeThrow() {
        this.setReplacMode(IFreeAttribute.ReplaceMode_Throw);
    }
}
