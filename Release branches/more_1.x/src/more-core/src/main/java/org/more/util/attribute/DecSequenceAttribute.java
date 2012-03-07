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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * ��һ��{@link IAttribute}���������еķ�ʽ���в���
 * ���ֽṹ{@link #setAttribute(String, Object)}��{@link #removeAttribute(String)}��{@link #clearAttribute()}
 * ��������ֻ��Ӱ�쵽{@link #getSource()}���ص����Զ���ͨ�����췽�������滻����
 * @version : 2011-7-22
 * @author ������ (zyc@byshell.org)
 */
public class DecSequenceAttribute<V> extends AttributeDecorator<V> {
    private Map<String, IAttribute<V>> attMap  = new HashMap<String, IAttribute<V>>();
    private List<IAttribute<V>>        attList = new ArrayList<IAttribute<V>>();
    /**����DecSequenceAttribute����ʹ���½���Attribute������Ϊ����Դ��*/
    public DecSequenceAttribute() {
        this(new Attribute<V>(), null);
    };
    /**����DecSequenceAttribute����ʹ���½���Attribute������Ϊ����Դ��*/
    public DecSequenceAttribute(Collection<IAttribute<V>> collection) {
        this(new Attribute<V>(), collection);
    };
    /**
     * ����DecSequenceAttribute����ʹ��һ�����е�Attribute������Ϊ����Դ��
     * @param source ����Դ��
     */
    public DecSequenceAttribute(IAttribute<V> source, Collection<IAttribute<V>> collection) {
        super(source);
        if (collection != null)
            for (IAttribute<V> att : collection)
                if (att != null)
                    this.attList.add(att);
    };
    public void putAtt(String name, IAttribute<V> att) {
        if (this.attMap.containsKey(name) == false) {
            this.attMap.put(name, att);
            this.attList.add(att);
        }
    };
    public List<IAttribute<V>> getAttList() {
        return this.attList;
    };
    public int getAttCount() {
        return this.attList.size();
    };
    public boolean containsAtt(String name) {
        return this.attMap.containsKey(name);
    };
    public IAttribute<V> removeAtt(String name) {
        if (this.attMap.containsKey(name) == true) {
            IAttribute<V> att = this.attMap.remove(name);
            if (att != null)
                this.attList.remove(att);
            return att;
        }
        return null;
    };
    public void putAtt(IAttribute<V> att) {
        this.putAtt(UUID.randomUUID().toString(), att);
    };
    /**��{@link IAttribute}�ӿڶ��󵯳����С�*/
    public void popStack(String name) {};
    /**��ȡ������ָ��λ�õ����Զ���*/
    public IAttribute<V> getIndex(int index) {
        return this.attList.get(index);
    };
    public boolean contains(String name) {
        if (super.contains(name) == true)
            return true;
        for (IAttribute<?> iatt : this.attList)
            if (iatt.contains(name) == true)
                return true;
        return false;
    };
    public V getAttribute(String name) {
        V res = super.getAttribute(name);
        if (res == null)
            for (IAttribute<V> iatt : this.attList) {
                res = iatt.getAttribute(name);
                if (res != null)
                    return res;
            }
        return res;
    };
    /**���ص�ǰ���Լ��Լ��������Լ��е����������������ظ�����<br/><b>����һ���߳ɱ�������</b>*/
    public String[] getAttributeNames() {
        HashSet<String> names = new HashSet<String>();
        Collections.addAll(names, super.getAttributeNames());
        for (IAttribute<?> attItem : this.attList)
            Collections.addAll(names, attItem.getAttributeNames());
        String[] array = new String[names.size()];
        names.toArray(array);
        return array;
    };
    /**�������ж���Ԫ���е�����������<br/><b>����һ���߳ɱ�������*/
    public int size() {
        return this.getAttributeNames().length;
    };
};