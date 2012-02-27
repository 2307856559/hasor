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
import java.util.Collections;
import java.util.HashSet;
/**
 * �ṩһ�ֶԸ��㼶������֧�֡�
 * @version 2010-9-11
 * @author ������ (zyc@byshell.org)
 */
public class DecParentAttribute<V> extends AttributeDecorator<V> {
    private IAttribute<V> parent = null;
    /**
    * ����һ��ParentAttribute���Ͷ��󣬸ù��췽���ڲ����Զ�����һ��{@link Attribute}������Ϊ��
    * @param parent ��ʹ�õĸ����Լ���
    * @throws NullPointerException ���source����Ϊ������������쳣��
    */
    public DecParentAttribute(IAttribute<V> parent) throws NullPointerException {
        this(parent, new Attribute<V>());
    };
    /**
    * ����һ��ParentAttribute���Ͷ��󣬸ù��췽�����Խ�����{@link IAttribute}�Ը��Ӳ㼶�ķ�ʽ���������
    * @param parent ��ʹ�õĸ����Լ���
    * @param source Դ���Լ���
    * @throws NullPointerException ���source����Ϊ������������쳣��
    */
    public DecParentAttribute(IAttribute<V> parent, Attribute<V> source) throws NullPointerException {
        super(source);
        if (parent == null)
            throw new NullPointerException("parent IAttribute is null.");
        this.parent = parent;
    };
    /**��ȡ�����Լ���*/
    public final IAttribute<V> getParent() {
        return this.parent;
    };
    /**
     * ����װ����Ҫװ�ε�Ŀ���ࡣ���װ�����Ѿ�װ����ĳ�����Զ�����ô�÷������滻ԭ�����Զ���
     * @param source ׼���滻�����Զ���
     * @throws NullPointerException �����ͼ����һ����ֵ��װ�������������쳣��
     */
    protected final void setParent(IAttribute<V> parent) throws NullPointerException {
        if (parent == null)
            throw new NullPointerException("target parent IAttribute is null.");
        else
            this.parent = parent;
    }
    /**���ȴӵ�ǰ���Լ���Ѱ�ң�����ҵ�����������󡣷��򵽸����Լ���ȥ�Ҳ��ҷ��ز��ҽ����*/
    @Override
    public boolean contains(String name) {
        if (super.contains(name) == false)
            return this.parent.contains(name);
        return true;
    }
    /**���ȴӵ�ǰ���Լ���Ѱ�ң�����ҵ�����������󡣷��򵽸����Լ���ȥ�Ҳ��ҷ��ز��ҽ����*/
    @Override
    public V getAttribute(String name) {
        V obj = super.getAttribute(name);
        if (obj == null)
            return this.parent.getAttribute(name);
        return obj;
    }
    /**���ص�ǰ���Լ��Լ������Լ��п��Է��ʵ��������������������ǰ���Լ��ж���������ڸ����Լ����ظ�����÷���ֻ�ᱣ��һ���������ơ�
     * <br/><b>����һ���߳ɱ�����������ʹ��HashSet�ϲ�{@link #getParent()}��{@link #getSource()}���������ķ���ֵ��</b>*/
    @Override
    public String[] getAttributeNames() {
        HashSet<String> keys = new HashSet<String>();
        Collections.addAll(keys, this.getParent().getAttributeNames());
        Collections.addAll(keys, this.getSource().getAttributeNames());
        String[] array = new String[keys.size()];
        keys.toArray(array);
        return array;
    }
    /**ȡ�ÿ���ȡ�õ�����������Ŀ��
     * <br/><b>����һ���߳ɱ�����������ʹ��HashSet�ϲ�{@link #getParent()}��{@link #getSource()}���������ķ���ֵ��</b>*/
    @Override
    public int size() {
        HashSet<String> keys = new HashSet<String>();
        Collections.addAll(keys, this.getParent().getAttributeNames());
        Collections.addAll(keys, this.getSource().getAttributeNames());
        return keys.size();
    };
}