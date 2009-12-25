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
 *    ���������װ��������������װ���������̳��Ը�����������ࡣ�����װ���������ṩ
 * �˶�ԭʼ���Զ����һ��get/set������ͨ��װ������get/set�������Է�����ڲ�ͬ��װ��
 * ���Ͻ����л����߲���װ����Ƕ�ס�ע�⣺����װ�����еı�����������Ӱ�쵽װ�ε�Ŀ���ࡣ
 * Date : 2009-4-30
 * @author ������
 */
public abstract class AbstractAttDecorator implements IAttribute {
    //========================================================================================Field
    /** ԭʼ�������� */
    private IAttribute source = null;
    /**
     * ��������װ������
     * @param source Ҫװ�ε�Ŀ�����Զ���
     * @throws NullPointerException �����ͼ����һ����ֵ��װ�������������쳣��
     */
    protected AbstractAttDecorator(IAttribute source) throws NullPointerException {
        if (source == null)
            throw new NullPointerException("װ��Ŀ�����Զ���Ϊ�ա�");
        else
            this.source = source;
    }
    //==================================================================================Constructor
    /**
     * ���װ����װ�ε�ԭʼ���Զ���
     * @return ����װ����װ�ε�ԭʼ���Զ���
     */
    public IAttribute getSource() {
        return source;
    }
    /**
     * ����װ����Ҫװ�ε�Ŀ���ࡣ���װ�����Ѿ�װ����ĳ�����Զ�����ô�÷������滻ԭ�����Զ���
     * @param source ׼���滻�����Զ���
     * @throws NullPointerException �����ͼ����һ����ֵ��װ�������������쳣��
     */
    public void setSource(AttBase source) throws NullPointerException {
        if (source == null)
            throw new NullPointerException("װ��Ŀ�����Զ���Ϊ�ա�");
        else
            this.source = source;
    }
    @Override
    public void clearAttribute() {
        this.source.clearAttribute();
    }
    @Override
    public boolean contains(String name) {
        return this.source.contains(name);
    }
    @Override
    public Object getAttribute(String name) {
        return this.source.getAttribute(name);
    }
    @Override
    public String[] getAttributeNames() {
        return this.source.getAttributeNames();
    }
    @Override
    public void removeAttribute(String name) {
        this.source.removeAttribute(name);
    }
    @Override
    public void setAttribute(String name, Object value) {
        this.source.setAttribute(name, value);
    }
}