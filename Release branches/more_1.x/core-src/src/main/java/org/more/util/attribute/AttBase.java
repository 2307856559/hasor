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
public class AttBase implements IAttribute, Map<String, Object>, Serializable {
    //========================================================================================Field
    private static final long   serialVersionUID = 5330675593787806813L;
    /** ������Եļ��� */
    private Map<String, Object> prop;
    //==================================================================================Constructor
    //
    /**
     * ����һ���������Զ������ԵĴ��ʹ��HashMap��Ϊ����ʢװ�����������Խӿ�ʵ���ࡣ
     * ע�⣺IAttribute�ӿ����Լ����ӽӿڵĹ��ܽ��������õ�Map�ӿ��ϡ�������Ա��Ȼ����ͨ��Map�ӿڲ���AttBase�е����ݡ�
     * ����Map�ӿڽ�����֧��IAttribute�ӿڵ�����װ�����ӿ����ԡ�
     */
    public AttBase() {
        this.prop = new HashMap<String, Object>();
    }
    /**
     * ����һ���������Զ��󣬴�����Ե�ʢװ���������κ�Map�ӿ��ӽӿڼ���ʵ���ࡣ���prop�����ṩ����
     * Hashtable��Attribute�ӿڽ����ɽ��ܿ�ֵ�����ԡ����prop����Ϊ��BaseAtt��Ĭ��ʹ��HashMap��Ϊ
     * ����ʢװ����
     * @param prop ����ʢװ�������prop����Ϊ��BaseAtt��Ĭ��ʹ��HashMap��Ϊ����ʢװ����
     */
    protected AttBase(Map<String, Object> prop) {
        if (prop == null)
            this.prop = new HashMap<String, Object>();
        else
            this.prop = prop;
    }
    //==========================================================================================Job
    /**
     * ��ȡBaseAtt������ʢװ����
     * @return ���ػ�ȡBaseAtt������ʢװ����
     */
    protected Map<String, Object> getProp() {
        return this.prop;
    }
    /**
     * ����BaseAtt�����Ա�����󣬸÷��������滻ԭ��BaseAtt�����ڲ������Ա������
     * @param prop �������Զ��󱣴�Map
     */
    protected void setProp(HashMap<String, Object> prop) {
        this.prop = prop;
    }
    public void clearAttribute() {
        this.prop.clear();
    }
    public boolean contains(String name) {
        return this.prop.containsKey(name);
    }
    public Object getAttribute(String name) {
        return this.prop.get(name);
    }
    public String[] getAttributeNames() {
        String[] keys = new String[this.prop.size()];
        this.prop.keySet().toArray(keys);
        return keys;
    }
    public void removeAttribute(String name) {
        this.prop.remove(name);
    }
    public void setAttribute(String name, Object value) {
        this.prop.put(name, value);
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
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return this.prop.entrySet();
    }
    public Object get(Object key) {
        return this.prop.get(key);
    }
    public boolean isEmpty() {
        return this.prop.isEmpty();
    }
    public Set<String> keySet() {
        return this.prop.keySet();
    }
    public Object put(String key, Object value) {
        return this.prop.put(key, value);
    }
    public void putAll(Map<? extends String, ? extends Object> m) {
        this.prop.putAll(m);
    }
    public Object remove(Object key) {
        return this.prop.remove(key);
    }
    public int size() {
        return this.prop.size();
    }
    public Collection<Object> values() {
        return this.prop.values();
    }
    public Map<String, Object> toMap() {
        return this.prop;
    }
}