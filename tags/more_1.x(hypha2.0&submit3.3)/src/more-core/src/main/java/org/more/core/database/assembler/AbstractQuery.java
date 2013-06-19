/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"){};
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
package org.more.core.database.assembler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.more.core.copybean.CopyBeanUtil;
import org.more.core.database.DataBaseSupport;
import org.more.core.database.PagesList;
import org.more.core.database.Query;
import org.more.core.database.QueryCallBack;
import org.more.core.error.LoadException;
/**
 * ͨ�ò�ѯ�ӿ�ʵ����.
 * Date : 2010-6-21
 * @author ������
 */
public abstract class AbstractQuery<T extends AbstractDataBaseSupport> implements Query {
    private String queryString = null;
    private T      support     = null;
    /***/
    public AbstractQuery(String queryString, T support) {
        this.queryString = queryString;
        this.support = support;
    };
    /**��ȡ��ѯ�����*/
    public String getQuery() {
        return this.queryString;
    };
    /**��ȡ���β�ѯ��ʹ�õ�{@link DataBaseSupport}�ӿڡ�*/
    protected T getSupport() {
        return this.support;
    };
    /*-----------------------------------------------------------------------------------XXXX*/
    public int executeQuery() {
        return this.executeQuery(new DefaultQueryCallBack(this), new Object[0]);
    }
    /**ִ�в�ѯ���Բ��ҷ��������ѯ���.*/
    public List<Map<String, Object>> query() {
        return this.query(new DefaultQueryCallBack(this), new Object[0]);
    };
    /**ִ�в�ѯ���Բ��ҷ��������ѯ���.*/
    public <E> List<E> query(Class<E> toType) {
        return this.query(toType, new DefaultQueryCallBack(this), new Object[0]);
    };
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(int pageSize) {
        return this.queryForPages(pageSize, new DefaultQueryCallBack(this), new Object[0]);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique() {
        return this.firstUnique(new DefaultQueryCallBack(this), new Object[0]);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E firstUnique(Class<E> toType) {
        return this.firstUnique(toType, new DefaultQueryCallBack(this), new Object[0]);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique() {
        return this.lastUnique(new DefaultQueryCallBack(this), new Object[0]);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E lastUnique(Class<E> toType) {
        return this.lastUnique(toType, new DefaultQueryCallBack(this), new Object[0]);
    };
    /*-----------------------------------------------------------------------------------������*/
    public int executeQuery(Object... params) {
        return this.executeQuery(new DefaultQueryCallBack(this), params);
    }
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(Object... params) {
        return this.query(new DefaultQueryCallBack(this), params);
    };
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public <E> List<E> query(Class<E> toType, Object... params) {
        return this.query(toType, new DefaultQueryCallBack(this), params);
    };
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(int pageSize, Object... params) {
        return this.queryForPages(pageSize, new DefaultQueryCallBack(this), params);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(Object... params) {
        return this.firstUnique(new DefaultQueryCallBack(this), params);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E firstUnique(Class<E> toType, Object... params) {
        return this.firstUnique(toType, new DefaultQueryCallBack(this), params);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(Object... params) {
        return this.lastUnique(new DefaultQueryCallBack(this), params);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E lastUnique(Class<E> toType, Object... params) {
        return this.lastUnique(toType, new DefaultQueryCallBack(this), params);
    };
    /*-----------------------------------------------------------------------------------���ص�*/
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public <E> List<E> query(Class<E> toType, QueryCallBack callBack, Object... params) {
        List<Map<String, Object>> resultSet = this.query(callBack, params);
        ArrayList<E> resList = new ArrayList<E>();
        for (Map<String, Object> entity : resultSet)
            try {
                E entityObject = toType.newInstance();
                CopyBeanUtil.copyTo(entity, entityObject);
                resList.add(entityObject);
            } catch (Exception e) {
                throw new LoadException("create type ��" + toType + "�� or copy property error.", e);
            }
        return resList;
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(QueryCallBack callBack, Object... params) {
        List<?> list = this.query(callBack, params);
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E firstUnique(Class<E> toType, QueryCallBack callBack, Object... params) {
        try {
            Object obj = this.firstUnique(callBack, params);
            E entityObject = toType.newInstance();
            CopyBeanUtil.copyTo(obj, entityObject);
            return entityObject;
        } catch (Exception e) {
            throw new LoadException("create type ��" + toType + "�� or copy property error.", e);
        }
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(QueryCallBack callBack, Object... params) {
        List<?> list = this.query(callBack, params);
        if (list == null || list.size() == 0)
            return null;
        return list.get(list.size() - 1);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E lastUnique(Class<E> toType, QueryCallBack callBack, Object... params) {
        try {
            Object obj = this.lastUnique(callBack, params);
            E entityObject = toType.newInstance();
            CopyBeanUtil.copyTo(obj, entityObject);
            return entityObject;
        } catch (Exception e) {
            throw new LoadException("create type ��" + toType + "�� or copy property error.", e);
        }
    };
};