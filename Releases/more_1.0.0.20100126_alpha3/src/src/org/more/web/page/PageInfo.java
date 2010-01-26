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
package org.more.web.page;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * ��ҳ��ǩ�齨��ʹ�õ����ݶ��󡣿���ͨ����չ�ö����Դﵽ��ͬ�ķ�ҳ������ԡ�
 * @version 2009-6-17
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class PageInfo {
    private List     list                = new ArrayList(); //������Ŀ�ļ��϶���
    private Iterator iterator            = null;           //�б�ִ�еĵ�����
    private Object   iteratorCurrentItem = null;           //���������ڵ����ĵ�ǰ��Ŀ����
    //=========================================================================
    //��ʼ������
    public void initData() {
        if (this.list == null)
            this.list = new ArrayList();
    }
    /**
     * ��ȡ��������
     * @return ������������
     */
    public int getSize() {
        return this.list.size();
    }
    /**
     * ��ȡ�ķ�ҳ���ţ���0��ʼ���㡣����Ҳ���ָ�����󷵻�null��
     * @param index Ҫ��ȡ�ķ�ҳ���ţ���0��ʼ���㡣����Ҳ���ָ�����󷵻�null��
     * @return ����ĳһ����ҳ���0��ʼ���㡣����Ҳ���ָ�����󷵻�null��
     */
    public Object getItem(int index) {
        return (this.list.size() > index && index >= 0) ? this.list.get(index) : null;
    }
    /**
     * ��ӷ�ҳ������ǰ��ҳ�������ڲ����ҳ��ʾʱ���ø÷������ܵ��·�ҳ��ʾ��JSPҳ�����쳣��
     * @param obj Ҫ��ӵ�Ŀ�����
     */
    public void addItem(Object obj) {
        this.list.add(obj);
    }
    /**
     * ɾ��ָ��λ�õķ�ҳ������ǰ��ҳ�������ڲ����ҳ��ʾʱ���ø÷������ܵ��·�ҳ��ʾ��JSPҳ�����쳣��
     * @param index Ҫɾ����Ŀ���ҳ���š�
     */
    public void removeItem(int index) {
        this.list.remove(index);
    }
    /**
     * ɾ��ָ��λ�õķ�ҳ������ǰ��ҳ�������ڲ����ҳ��ʾʱ���ø÷������ܵ��·�ҳ��ʾ��JSPҳ�����쳣��
     * @param obj Ҫɾ����Ŀ���ҳ�����
     */
    public void removeItem(Object obj) {
        this.list.remove(obj);
    }
    /** ɾ������ */
    public void removeAll() {
        this.list.clear();
    }
    /**
     * �÷��������ǲ���item��Ŀ�Ƿ��ǲ²��λ�ã��÷����Ǵӵ�һ����ʼƥ�䡣
     * ����²��λ����ȷ�򷵻�true���򷵻�false��
     * @param index �²����Ŀλ�á�
     * @param item ���²����Ŀ��
     * @return ����²��λ����ȷ�򷵻�true���򷵻�false��
     */
    boolean isFirstIndex(int index, Object item) {
        //(�Ӻ��濪ʼ)����item�ڼ����е�λ�õ���index�򷵻�true���򷵻�false��
        return (list.indexOf(item) == index) ? true : false;
    }
    /**
     * �÷��������ǲ���item��Ŀ�Ƿ��ǲ²��λ�ã��÷����Ǵ����һ����ʼƥ�䡣
     * ����²��λ����ȷ�򷵻�true���򷵻�false��
     * @param index �²����Ŀλ�á�
     * @param item ���²����Ŀ��
     * @return ����²��λ����ȷ�򷵻�true���򷵻�false��
     */
    boolean isLastIndex(int index, Object item) {
        //(��ǰ�濪ʼ)����item�ڼ����е�λ�õ���index�򷵻�true���򷵻�false��
        return (list.lastIndexOf(item) == (list.size() - 1) - index) ? true : false;
    }
    Object getCurrentItem() {
        return this.iteratorCurrentItem;
    }
    boolean hasNext() {
        return this.iterator.hasNext();
    }
    Object next() {
        this.iteratorCurrentItem = this.iterator.next();
        return this.iteratorCurrentItem;
    }
    void release() {
        this.iteratorCurrentItem = null;
        this.iterator = null;
        this.iterator = this.list.iterator();
    }
    List getList() {
        return list;
    }
}
