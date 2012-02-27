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
/**
 * ջ�ṹ���Զ������ø�����װ�������������Լ���������һ������ջ��
 * @version 2010-9-11
 * @author ������ (zyc@byshell.org)
 */
public class DecStackDecorator<V> extends AttributeDecorator<V> {
    private int depth = 0;
    public DecStackDecorator() {
        super(new Attribute<V>());
    };
    public DecStackDecorator(IAttribute<V> source) throws NullPointerException {
        super(source);
    };
    /**��ȡ��ǰ�ѵ���ȣ���ֵ�����ŵ���createStack���������ӣ�����dropStack���������١�*/
    public final int getDepth() {
        return this.depth;
    };
    /** �÷�����getSource()��������ֵһ���� */
    public IAttribute<V> getCurrentStack() {
        return super.getSource();
    };
    /** ��ȡ��ǰ�ѵĸ��ѣ�������ܣ��� */
    public IAttribute<V> getParentStack() {
        if (depth == 0)
            return null;
        IAttribute<V> att = super.getSource();
        return ((DecParentAttribute<V>) att).getParent();
    };
    /**����������ջ�ϴ���һ���µ�ջ������Ҳ���л��������ջ�ϡ�*/
    public synchronized void createStack() {
        IAttribute<V> source = super.getSource();
        DecParentAttribute<V> parent = new DecParentAttribute<V>(source);
        this.setSource(parent);
        depth++;
    };
    /**���ٵ�ǰ��ε�����ջ�������ջ��ִ�иò�����������{@link IndexOutOfBoundsException}�����쳣��*/
    public synchronized boolean dropStack() {
        if (depth == 0)
            throw new IndexOutOfBoundsException();
        IAttribute<V> source = super.getSource();
        if (source instanceof DecParentAttribute) {
            super.setSource(((DecParentAttribute<V>) source).getParent());
            depth--;
            return true;
        }
        return false;
    };
}