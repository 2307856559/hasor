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
package org.more.util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * �ϲ�����ͬ���Ͷ���Ĺ����ࡣ
 * @version : 2012-2-10
 * @author ������ (zyc@byshell.org)
 */
public abstract class MergeUtils {
    /**�ϲ�����������*/
    public static <T> Iterator<T> mergeIterator(final Iterator<T> iterator1, final Iterator<T> iterator2) {
        final Iterator<T> i1 = (iterator1 != null) ? iterator1 : new ArrayList<T>(0).iterator();
        final Iterator<T> i2 = (iterator2 != null) ? iterator2 : new ArrayList<T>(0).iterator();
        return new Iterator<T>() {
            private Iterator<T> it = i1;
            public boolean hasNext() {
                return (i1.hasNext() || i2.hasNext()) ? true : false;
            }
            public T next() {
                if (this.it.hasNext() == false)
                    this.it = i2;
                return this.it.next();
            }
            public void remove() {
                this.it.remove();
            }
        };
    };
    /**�ϲ�����{@link List}���ж�������Դ��equals������*/
    public static <T> List<T> mergeList(List<T> data1, List<T> data2) {
        return mergeList(data1, data2, new Comparator<T>() {
            public int compare(T o1, T o2) {
                return (o1.equals(o2) == true) ? 0 : 1;
            }
        });
    }
    /**�ϲ�����{@link List}��ʹ��{@link Comparator}�ӿ��ж��Ƿ��ظ�������0��ʾ�ظ�����*/
    public static <T> List<T> mergeList(List<T> data1, List<T> data2, Comparator<T> comparator) {
        //1.׼������
        List<T> d1 = (data1 != null) ? data1 : new ArrayList<T>(0);
        List<T> d2 = (data2 != null) ? data2 : new ArrayList<T>(0);
        //2.ִ��Array�ϲ�&ȥ��
        ArrayList<T> array = new ArrayList<T>(d1);
        for (T itemTarget : d2) {
            boolean has = false;
            for (T itemHas : array)
                if (comparator.compare(itemTarget, itemHas) == 0) {
                    has = true;
                    break;
                }
            if (has == false)
                array.add(itemTarget);
        }
        return array;
    };
    /**�ϲ�����{@link Map}���ϲ���ͬkey��map��ͬkey��ֻ�ᱣ��һ����*/
    public static <K, V> Map<K, V> mergeMap(Map<K, V> dataMap1, Map<K, V> dataMap2) {
        return mergeMap(dataMap1, dataMap2, null);
    };
    /**�ϲ�����{@link Map}��ʹ��{@link Comparator}�ӿ��ж���ͬ��key�����Ǹ����ӿڷ���ֵ����0ʹ��o2��С��0ʹ��o1��0������ͻ���ԣ���*/
    public static <K, V> Map<K, V> mergeMap(Map<K, V> dataMap1, Map<K, V> dataMap2, Comparator<Map.Entry<K, V>> comparator) {
        //1.׼������
        Map<K, V> m1 = (dataMap1 != null) ? dataMap1 : new HashMap<K, V>();
        Map<K, V> m2 = (dataMap2 != null) ? dataMap2 : new HashMap<K, V>();
        //2.ִ��Map�ϲ�&ȥ��
        HashMap<K, V> hashMap = new HashMap<K, V>(m1);
        for (Map.Entry<K, V> e_m2 : m2.entrySet()) {
            V target = e_m2.getValue();
            boolean remove = false;
            for (Map.Entry<K, V> e_m1 : hashMap.entrySet())
                if (e_m1.getKey().equals(e_m2.getKey()) == true) {
                    int res = comparator.compare(e_m1, e_m2);
                    if (res == 0)
                        remove = true;//����0
                    else if (res < 0)
                        target = e_m1.getValue();//С��0
                    else
                        target = e_m2.getValue(); //����0
                }
            if (remove == true)
                hashMap.remove(e_m2.getKey());
            else
                hashMap.put(e_m2.getKey(), target);
        }
        return hashMap;
    }
}