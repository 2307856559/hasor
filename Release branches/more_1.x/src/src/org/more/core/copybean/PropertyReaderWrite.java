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
package org.more.core.copybean;
import java.io.Serializable;
/**
 * �������Զ�д����������Խ�ĳ�����Ա��浽�����һ�������ϻ��߽�ĳ�����Դ�ĳ�������϶�ȡ������
 * ������һ�������࣬�������������Ӧ����ζ�д���ԡ�������಻֧�ֶ�����д��������Ӧ����д����
 * canReader��canWrite������canReader��canWrite�����ڸ�����ʼ�շ���true��
 * Date : 2009-5-15
 * @author ������
 */
public abstract class PropertyReaderWrite implements Serializable {
    /** ������ */
    private String name   = null;
    /** ������ԵĶ��� */
    private Object object = null;
    /**
     * ��ȡĿ������ֵ���÷���Ӧ��������ʵ�֡�
     * @return ����Ŀ������ֵ���÷���Ӧ��������ʵ�֡�
     */
    public abstract Object get();
    /**
     * ��ָ����ֵд������Զ��󣬸÷���Ӧ��������ʵ�֡�
     * @param value Ҫд���ֵ���ݡ�
     */
    public abstract void set(Object value);
    /**
     * ���Ը����Զ�ȡ���Ƿ�֧�ֶ����������֧�ַ���true���򷵻�false��
     * @return ���ز��Ը����Զ�д���Ƿ�֧�ֶ����������֧�ַ���true���򷵻�false��
     */
    public boolean canReader() {
        return true;
    }
    /**
     * ���Ը����Զ�ȡ���Ƿ�֧��д���������֧�ַ���true���򷵻�false��
     * @return ���ز��Ը����Զ�д���Ƿ�֧�ֶ����������֧�ַ���true���򷵻�false��
     */
    public boolean canWrite() {
        return true;
    }
    /**
     * ��ø����Զ�д����ʾ���������ơ�
     * @return ���ظ����Զ�д����ʾ���������ơ�
     */
    public String getName() {
        return this.name;
    }
    /**
     * ����������
     * @param name Ҫ���õ�������
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * ��ø����Զ�д��׼����������������
     * @return ���ظ����Զ�д��׼����������������
     */
    public Object getObject() {
        return object;
    }
    /**
     * ���ô�����ԵĶ���
     * @param object Ҫ���õĴ�����ԵĶ���
     */
    public void setObject(Object object) {
        this.object = object;
    }
    /**
     * ��õ�ǰ���Ե��������͡�
     * @return ���ص�ǰ���Ե��������͡�
     * @throws Exception 
     */
    public abstract Class<?> getPropertyClass();
}
