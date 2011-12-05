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
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * �������Խӿ�ʵ���ࡣע�⣺IAttribute�ӿ����Լ����ӽӿڵĹ��ܽ��������õ�Map�ӿ��ϡ�
 * ������Ա��Ȼ����ͨ��Map�ӿڲ���AttBase�е����ݡ�����Map�ӿڽ�����֧��IAttribute�ӿڵ�����װ�����ӿ����ԡ�
 * @version 2009-4-29
 * @author ������ (zyc@byshell.org)
 */
public class AttBase<T> implements IAttribute<T>, Map<String, T>, Serializable {
    //========================================================================================Field
    private static final long serialVersionUID = 5330675593787806813L;
    /** ������Եļ��� */
    private Map<String, T>    prop             = null;
    //==================================================================================Constructor
    /**
     * ����һ���������Զ������ԵĴ��ʹ��HashMap��Ϊ����ʢװ�����������Խӿ�ʵ���ࡣ
     * ע�⣺IAttribute�ӿ����Լ����ӽӿڵĹ��ܽ��������õ�Map�ӿ��ϡ�������Ա��Ȼ����ͨ��Map�ӿڲ���AttBase�е����ݡ�
     * ����Map�ӿڽ�����֧��IAttribute�ӿڵ�����װ�����ӿ����ԡ�
     */
    public AttBase() {
        this.prop = new HashMap<String, T>();
    }
    /**
     * ����һ���������Զ��󣬴�����Ե�ʢװ���������κ�Map�ӿ��ӽӿڼ���ʵ���ࡣ���prop�����ṩ����
     * Hashtable��Attribute�ӿڽ����ɽ��ܿ�ֵ�����ԡ����prop����Ϊ��BaseAtt��Ĭ��ʹ��HashMap��Ϊ����ʢװ����
     * @param prop ����ʢװ�������prop����Ϊ��BaseAtt��Ĭ��ʹ��HashMap��Ϊ����ʢװ����
     */
    public AttBase(Map<String, T> prop) {
        if (prop == null)
            this.prop = new HashMap<String, T>();
        else
            this.prop = prop;
    }
    //==========================================================================================Job
    /**
     * ��ȡBaseAtt������ʢװ����
     * @return ���ػ�ȡBaseAtt������ʢװ����
     */
    protected Map<String, T> getMap() {
        return this.prop;
    }
    /**
     * ����BaseAtt�����Ա�����󣬸÷��������滻ԭ��BaseAtt�����ڲ������Ա������
     * @param prop �������Զ��󱣴�Map
     */
    protected void setProp(HashMap<String, T> prop) {
        this.prop = prop;
    }
    public void clearAttribute() {
        this.clear();
    }
    public boolean contains(String name) {
        return this.containsKey(name);
    }
    public T getAttribute(String name) {
        return this.get(name);
    }
    public String[] getAttributeNames() {
        String[] keys = new String[this.prop.size()];
        this.keySet().toArray(keys);
        return keys;
    }
    public void removeAttribute(String name) {
        this.remove(name);
    }
    public void setAttribute(String name, T value) {
        this.put(name, value);
    }
    //==================================================================================Map�ӿ�ʵ��
    public void clear() {
        this.prop.clear();
    }
    public boolean containsKey(Object key) {
        return this.prop.containsKey(key);
    }
    public boolean containsValue(Object value) {
        return this.prop.containsValue(value);
    }
    public Set<java.util.Map.Entry<String, T>> entrySet() {
        return this.prop.entrySet();
    }
    public T get(Object key) {
        return this.prop.get(key);
    }
    public boolean isEmpty() {
        return this.prop.isEmpty();
    }
    public Set<String> keySet() {
        return this.prop.keySet();
    }
    public T put(String key, T value) {
        return this.prop.put(key, value);
    }
    public void putAll(Map<? extends String, ? extends T> m) {
        this.prop.putAll(m);
    }
    public T remove(Object key) {
        return this.prop.remove(key);
    }
    public int size() {
        return this.prop.size();
    }
    public Collection<T> values() {
        return this.prop.values();
    }
    public Map<String, T> toMap() {
        return new TransformToMap<T>(this);
    }
}