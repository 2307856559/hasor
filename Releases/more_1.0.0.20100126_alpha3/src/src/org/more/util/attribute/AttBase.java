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
import java.util.Properties;
import java.util.Set;
/**
 * �������Խӿ�ʵ���ࡣע�⣺IAttribute�ӿ����Լ����ӽӿڵĹ��ܽ��������õ�Map�ӿ��ϡ�
 * ������Ա��Ȼ����ͨ��Map�ӿڲ���AttBase�е����ݡ�����Map�ӿڽ�����֧��IAttribute�ӿڵ�����װ�����ӿ����ԡ�
 * @version 2009-4-29
 * @author ������ (zyc@byshell.org)
 */
public class AttBase implements IAttribute, IAttTransform, Map<String, Object>, Serializable {
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
    @Override
    public void clearAttribute() {
        this.prop.clear();
    }
    @Override
    public boolean contains(String name) {
        return this.prop.containsKey(name);
    }
    @Override
    public Object getAttribute(String name) {
        return this.prop.get(name);
    }
    @Override
    public String[] getAttributeNames() {
        String[] keys = new String[this.prop.size()];
        this.prop.keySet().toArray(keys);
        return keys;
    }
    @Override
    public void removeAttribute(String name) {
        this.prop.remove(name);
    }
    @Override
    public void setAttribute(String name, Object value) {
        this.prop.put(name, value);
    }
    @Override
    public void fromProperties(Properties prop) {
        for (Object ks : prop.keySet())
            this.prop.put(ks.toString(), prop.get(ks));
    }
    @Override
    public Properties toProperties() {
        Properties prop = new Properties();
        for (String ks : this.prop.keySet()) {
            if (this.prop.get(ks) != null)
                prop.put(ks, this.prop.get(ks));
        }
        return prop;
    }
    //==================================================================================Map�ӿ�ʵ��
    @Override
    public void clear() {
        this.prop.clear();
    }
    @Override
    public boolean containsKey(Object key) {
        return this.prop.containsKey(key);
    }
    @Override
    public boolean containsValue(Object value) {
        return this.prop.containsValue(value);
    }
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return this.prop.entrySet();
    }
    @Override
    public Object get(Object key) {
        return this.prop.get(key);
    }
    @Override
    public boolean isEmpty() {
        return this.prop.isEmpty();
    }
    @Override
    public Set<String> keySet() {
        return this.prop.keySet();
    }
    @Override
    public Object put(String key, Object value) {
        return this.prop.put(key, value);
    }
    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        this.prop.putAll(m);
    }
    @Override
    public Object remove(Object key) {
        return this.prop.remove(key);
    }
    @Override
    public int size() {
        return this.prop.size();
    }
    @Override
    public Collection<Object> values() {
        return this.prop.values();
    }
}