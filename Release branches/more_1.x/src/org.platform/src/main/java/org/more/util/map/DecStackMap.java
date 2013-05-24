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
package org.more.util.map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.more.util.MergeUtils;
/**
 * �ṩһ��ջ�ṹ�Ĳ���Map�������Զ������ø�����װ�������������Լ���������һ������ջ��
 * @version 2010-9-11
 * @author ������ (zyc@byshell.org)
 */
public class DecStackMap<K, T> extends DecSequenceMap<K, T> {
    /** ����һ���������Զ��� */
    public DecStackMap() {
        this(null);
    };
    /** ����һ���������Զ��󣬲����ǵ�һ��ջ���� */
    public DecStackMap(Map<K, T> entryMap) {
        super(entryMap);
    };
    @Override
    public T put(K key, T value) {
        return this.getMapList().get(0).put(key, value);
    }
    @Override
    public T remove(Object key) {
        return this.getMapList().get(0).remove(key);
    }
    @Override
    protected StackSimpleSet<K, T> createSet() {
        return new StackSimpleSet<K, T>();
    };
    /**��ȡ��ǰ�ѵ���ȣ���ֵ�����ŵ���createStack���������ӣ�����dropStack���������١�*/
    public final int getDepth() {
        return this.entrySet().mapList.size() - 1;
    };
    /**����������ջ�ϴ���һ���µ�ջ������Ҳ���л��������ջ�ϡ�*/
    public synchronized void createStack() {
        StackSimpleSet<K, T> stackList = (StackSimpleSet<K, T>) this.entrySet();
        stackList.mapList.addFirst(new HashMap<K, T>());
    };
    /**���ٵ�ǰ��ε�����ջ�������ջ��ִ�иò�����������{@link IndexOutOfBoundsException}�����쳣��*/
    public synchronized void dropStack() {
        StackSimpleSet<K, T> stackList = (StackSimpleSet<K, T>) this.entrySet();
        if (stackList.mapList.size() == 0)
            throw new IndexOutOfBoundsException();
        stackList.removeFirst();
    };
    /*----------------------------------------------------------------------*/
    public static class StackSimpleSet<K, T> extends SimpleSet<K, T> {
        private LinkedList<Map<K, T>> mapList = null;
        public StackSimpleSet() {
            this.mapList = new LinkedList<Map<K, T>>();
            super.mapList = this.mapList;
        }
        public void removeFirst() {
            this.mapList.removeFirst();
        }
        @Override
        public Iterator<java.util.Map.Entry<K, T>> iterator() {
            Iterator<java.util.Map.Entry<K, T>> seqIter = null;
            for (Map<K, T> mapItem : this.mapList)
                seqIter = MergeUtils.mergeIterator(seqIter, mapItem.entrySet().iterator());
            return seqIter;
        }
        @Override
        public int size() {
            int count = 0;
            for (Map<K, T> map : mapList)
                count += map.size();
            return count;
        }
    };
    /** ��ȡָ����ȵĸ��ѣ�������ܣ���0����ǰ�㣬����Խ���ȡ�����Խ� */
    public Map<K, T> getParentStack(int depth) {
        if (depth < 0 || depth > this.getDepth())
            throw new IndexOutOfBoundsException();
        return this.entrySet().mapList.get(depth);
    };
    /** ��ȡ��ǰ�ѵĸ��ѣ�������ܣ��� */
    public Map<K, T> getParentStack() {
        return this.getParentStack(this.getDepth() - 1);
    };
}