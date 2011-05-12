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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * �����ְ���ǽ�{@link Map}�ӿڶ���ת��Ϊ{@link IAttribute}�ӿڶ���
 * ������ע�⣬{@link Map}��Key����ΪString���ͷ�������޷�ͨ���ַ�����ʽ��Key��ȡ��ֵ��
 * Date : 2011-4-12
 * @author ������ (zyc@byshell.org)
 */
public class TransformToMap implements Map<String, Object> {
    private IAttribute values = null;
    /**����һ��{@link TransformToMap}���󣬸ö���������ǽ�{@link IAttribute}ת��Ϊ{@link Map}�ӿڡ�*/
    public TransformToMap(IAttribute values) {
        this.values = values;
    };
    public int size() {
        return this.values.getAttributeNames().length;
    };
    public boolean isEmpty() {
        return (this.size() == 0) ? true : false;
    };
    /**Key�������ַ����͵ĵġ�*/
    public boolean containsKey(Object key) {
        return this.values.contains(key.toString());
    };
    public boolean containsValue(Object value) {
        for (String k : this.values.getAttributeNames()) {
            Object obj = this.values.getAttribute(k);
            if (obj != null)
                if (obj.equals(value) == true)
                    return true;
        }
        return false;
    };
    /**Key�������ַ����͵ĵġ�*/
    public Object get(Object key) {
        return this.values.getAttribute((String) key);
    };
    public Object put(String key, Object value) {
        this.values.setAttribute(key, value);
        return value;
    };
    public Object remove(Object key) {
        String k = (String) key;
        Object value = this.values.getAttribute(k);
        this.values.removeAttribute(k);
        return value;
    };
    public void putAll(Map<? extends String, ? extends Object> m) {
        for (String key : m.keySet())
            this.put(key, m.get(key));
    };
    public void clear() {
        this.values.clearAttribute();
    };
    public Set<String> keySet() {
        HashSet<String> al = new HashSet<String>();
        for (String k : this.values.getAttributeNames())
            al.add(k);
        return al;
    };
    public Collection<Object> values() {
        ArrayList<Object> al = new ArrayList<Object>(this.size());
        for (String k : this.values.getAttributeNames())
            al.add(this.values.getAttribute(k));
        return al;
    };
    public Set<Map.Entry<String, Object>> entrySet() {
        HashSet<Map.Entry<String, Object>> al = new HashSet<Map.Entry<String, Object>>();
        for (String k : this.values.getAttributeNames())
            al.add(new Entry(k, this.values));
        return al;
    };
    private class Entry implements Map.Entry<String, Object> {
        private IAttribute values = null;
        private String     key    = null;
        public Entry(String key, IAttribute values) {
            this.key = key;
            this.values = values;
        };
        public Object setValue(Object value) {
            this.values.setAttribute(this.key, value);
            return value;
        };
        public Object getValue() {
            return this.values.getAttribute(this.key);
        };
        public String getKey() {
            return this.key;
        };
    };
};