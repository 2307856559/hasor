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
import java.util.Map;
/**
 * ���������װ��������������װ���������̳��Ը�����������ࡣ
 * @version 2009-4-30
 * @author ������ (zyc@byshell.org)
 */
public abstract class AttributeDecorator<T> implements IAttribute<T> {
    private IAttribute<T> source = null;
    /**
     * ��������װ������
     * @param source Ҫװ�ε�Ŀ�����Զ���
     * @throws NullPointerException �����ͼ����һ����ֵ��װ�������������쳣��
     */
    protected AttributeDecorator(IAttribute<T> source) throws NullPointerException {
        if (source == null)
            throw new NullPointerException("target source IAttribute is null.");
        else
            this.source = source;
    };
    /** ��ñ�װ�εĶ���*/
    public final IAttribute<T> getSource() {
        return this.source;
    };
    /**
     * ����װ����Ҫװ�ε�Ŀ���ࡣ���װ�����Ѿ�װ����ĳ�����Զ�����ô�÷������滻ԭ�����Զ���
     * @param source ׼���滻�����Զ���
     * @throws NullPointerException �����ͼ����һ����ֵ��װ�������������쳣��
     */
    protected final void setSource(IAttribute<T> source) throws NullPointerException {
        if (source == null)
            throw new NullPointerException("target source IAttribute is null.");
        else
            this.source = source;
    };
    public Map<String, T> toMap() {
        return new TransformToMap<T>(this);
    }
    public void clearAttribute() {
        this.source.clearAttribute();
    };
    public boolean contains(String name) {
        return this.source.contains(name);
    };
    public T getAttribute(String name) {
        return this.source.getAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.source.getAttributeNames();
    };
    public void removeAttribute(String name) {
        this.source.removeAttribute(name);
    };
    public void setAttribute(String name, T value) {
        this.source.setAttribute(name, value);
    };
    public int size() {
        return this.source.size();
    };
};