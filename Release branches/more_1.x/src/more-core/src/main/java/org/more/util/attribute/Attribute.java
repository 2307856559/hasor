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
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * {@link IAttribute}���ԽӿڵĻ���ʵ���࣬���������ͨ�������ܱ������ֶ�{@link #entrySet}ʵ�ָ��߼��Ĳ�����
 * @version : 2012-2-23
 * @author ������ (zyc@byshell.org)
 */
public class Attribute<V> extends AbstractMap<String, V> implements IAttribute<V> {
    protected Set<java.util.Map.Entry<String, V>> entrySet = new HashSet<Map.Entry<String, V>>();
    /** ����һ���������Զ��� */
    public Attribute() {}
    /** ����һ���������Զ���ʹ�ò��������ʼ������ */
    public Attribute(Map<String, V> prop) {
        if (prop != null)
            this.entrySet = prop.entrySet();
    }
    public boolean contains(String name) {
        return this.containsKey(name);
    }
    public void setAttribute(String name, V value) {
        this.put(name, value);
    }
    public V getAttribute(String name) {
        return this.get(name);
    }
    public void removeAttribute(String name) {
        this.remove(name);
    }
    public String[] getAttributeNames() {
        String[] keys = new String[this.size()];
        this.keySet().toArray(keys);
        return keys;
    }
    public void clearAttribute() {
        this.clear();
    }
    public Map<String, V> toMap() {
        return this;
    }
    public Set<java.util.Map.Entry<String, V>> entrySet() {
        return this.entrySet;
    }
    public V put(String key, V value) {
        Entry<String, V> e = new SimpleEntry<V>(key, value);
        this.entrySet.add(e);
        return value;
    }
    private static class SimpleEntry<V> implements Entry<String, V> {
        private String entry_key = null;
        private V      entry_var = null;
        public SimpleEntry(String key, V value) {
            this.entry_key = key;
            this.entry_var = value;
        }
        public String getKey() {
            return this.entry_key;
        }
        public V getValue() {
            return this.entry_var;
        }
        public V setValue(V value) {
            this.entry_var = value;
            return this.entry_var;
        }
    }
}