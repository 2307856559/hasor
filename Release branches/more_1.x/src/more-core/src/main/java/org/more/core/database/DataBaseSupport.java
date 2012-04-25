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
package org.more.core.database;
import java.util.List;
import java.util.Map;
import org.more.core.database.meta.TableMetaData;
/**
 * ���ݿ���������ӿڣ��ýӿڲ�֧������������
 * @version : 2011-11-9
 * @author ������ (zyc@byshell.org)
 */
public interface DataBaseSupport {
    public TableMetaData getTableInfo(String tableName);
    //    /**����map���в��������*/
    //    public void insertBatchForMap(String tableName, Map<String, Object> values);
    //    /**����map���и��²�����*/
    //    public void updateBatchForMap(String tableName, Map<String, Object> values, Map<String, Object> whereMap);
    //    
    //    
    //    /**����map���в��������*/
    //    public void insertForMap(String tableName, Map<String, Object> values);
    //    /**����map���и��²�����*/
    //    public void updateForMap(String tableName, Map<String, Object> values, Map<String, Object> whereMap);
    /**����SQL��䴴��һ����ѯ�ӿڶ���.���øö�����Խ��и��Ӳ�ѯ.*/
    public Query createQuery(String queryString);
    /*-----------------------------------------------------------------------------------XXXX*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(String queryString);
    /**ִ�в�ѯ���Բ��ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(String queryString);
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(String queryString, int pageSize);
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(String queryString);
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(String queryString);
    /*-----------------------------------------------------------------------------------������*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(String queryString, Object... params);
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(String queryString, Object... params);
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(String queryString, int pageSize, Object... params);
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(String queryString, Object... params);
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(String queryString, Object... params);
    /*-----------------------------------------------------------------------------------���ص�*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(String queryString, QueryCallBack callBack, Object... params);
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(String queryString, QueryCallBack callBack, Object... params);
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(String queryString, int pageSize, QueryCallBack callBack, Object... params);
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(String queryString, QueryCallBack callBack, Object... params);
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(String queryString, QueryCallBack callBack, Object... params);
};