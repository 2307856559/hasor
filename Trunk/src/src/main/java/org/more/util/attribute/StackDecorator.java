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
import org.more.core.error.SupportException;
/**
 * ������װ���������ø�����װ�������������Լ���������һ�����Զѡ������ݽṹ������ơ�
 * ���ڸ�װ�����ϻ�ȡĳ������ʱ��StackDecorator���Ͷ���������ڵ�ǰ���Զ���Ѱ������Ҳ�����ȥ��һ������ȥѰ�ҡ�
 * ͨ��createStack�������Դ���һ���µ����Զѡ���dropStack������������λ�����Զ����ϲ㡣
 * @version 2010-9-11
 * @author ������ (zyc@byshell.org)
 */
public class StackDecorator extends AbstractAttDecorator {
    //========================================================================================Field
    private int depth = 0;
    //==================================================================================Constructor
    public StackDecorator(IAttribute source) throws NullPointerException {
        super(source);
    }
    //==========================================================================================JOB
    /**����StackDecorator������createStack��dropStack�����ж���Ҫ�ı䴴��source���Ϊ�˱�������setSource������StackDecoratorװ������֧�ָ÷�����*/
    public void setSource(IAttribute source) throws SupportException {
        throw new SupportException("StackDecoratorװ������֧�ָ÷�����");
    }
    /**��ȡԴ*/
    public IAttribute getSource() {
        IAttribute att = super.getSource();
        if (depth == 0)
            return att;
        return ((ParentDecorator) att).getSource();
    }
    /** �÷�����getSource()��������ֵһ���� */
    public IAttribute getCurrentStack() {
        return super.getSource();
    }
    /** ��ȡ��ǰ�ѵĸ��ѡ� */
    public IAttribute getParentStack() {
        if (depth == 0)
            return null;
        IAttribute att = super.getSource();
        return ((ParentDecorator) att).getParent();
    }
    /**���������Զ��ϴ���һ���ѡ�*/
    public synchronized void createStack() {
        IAttribute source = super.getSource();
        super.setSource(new ParentDecorator(new AttBase(), source));
        depth++;
    }
    /**���ٵ�ǰ��ε����Զѣ������ǰ���ԶѲ���������ĸ�Դ�����������ε����ԶѲ�����ǰ���Զ��滻Ϊ�����Զѡ������ɹ�����true���򷵻�false��*/
    public synchronized boolean dropStack() {
        if (depth == 0)
            return false;
        //
        IAttribute source = super.getSource();
        if (source instanceof ParentDecorator) {
            super.setSource(((ParentDecorator) source).getParent());
            depth--;
            return true;
        }
        return false;
    }
    /**��ȡ��ǰ�ѵ���ȣ���ֵ�����ŵ���createStack���������ӣ�����dropStack���������١�*/
    public int getDepth() {
        return this.depth;
    }
}