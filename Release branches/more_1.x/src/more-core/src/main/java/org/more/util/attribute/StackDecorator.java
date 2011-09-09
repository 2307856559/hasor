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
public class StackDecorator<T> extends AbstractAttDecorator<T> {
    //========================================================================================Field
    private int depth = 0;
    //==================================================================================Constructor
    public StackDecorator(IAttribute<T> source) throws NullPointerException {
        super(source);
    }
    //==========================================================================================JOB
    /**����StackDecorator������createStack��dropStack�����ж���Ҫ�ı䴴��source���Ϊ�˱�������setSource������StackDecoratorװ������֧�ָ÷�����*/
    public void setSource(IAttribute<T> source) throws SupportException {
        throw new SupportException("StackDecoratorװ������֧�ָ÷�����");
    }
    /**��ȡԴ*/
    public IAttribute<T> getSource() {
        IAttribute<T> att = super.getSource();
        if (depth == 0)
            return att;
        return ((ParentDecorator<T>) att).getSource();
    }
    /** �÷�����getSource()��������ֵһ���� */
    public IAttribute<T> getCurrentStack() {
        return super.getSource();
    }
    /** ��ȡ��ǰ�ѵĸ��ѡ� */
    public IAttribute<T> getParentStack() {
        if (depth == 0)
            return null;
        IAttribute<T> att = super.getSource();
        return ((ParentDecorator<T>) att).getParent();
    }
    /**���������Զ��ϴ���һ���ѡ�*/
    public synchronized void createStack() {
        IAttribute<T> source = super.getSource();
        super.setSource(new ParentDecorator<T>(new AttBase<T>(), source));
        depth++;
    }
    /**���ٵ�ǰ��ε����Զѣ������ǰ���ԶѲ���������ĸ�Դ�����������ε����ԶѲ�����ǰ���Զ��滻Ϊ�����Զѡ������ɹ�����true���򷵻�false��*/
    public synchronized boolean dropStack() {
        if (depth == 0)
            return false;
        //
        IAttribute<T> source = super.getSource();
        if (source instanceof ParentDecorator) {
            super.setSource(((ParentDecorator<T>) source).getParent());
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