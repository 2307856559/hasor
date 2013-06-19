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
/**
 * ͨ�ò�ѯ�ӿ�.
 * @version : 2011-11-9
 * @author ������ (zyc@byshell.org)
 */
public interface Query {
    /**��ȡ��ѯ��sql���*/
    public String getQuery();
    //
    /*-----------------------------------------------------------------------------------XXXX*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery();
    /**ִ�в�ѯ���Բ��ҷ��������ѯ���.*/
    public List<Map<String, Object>> query();
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(int pageSize);
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique();
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique();
    /*-----------------------------------------------------------------------------------������*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(Object... params);
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(Object... params);
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(int pageSize, Object... params);
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(Object... params);
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(Object... params);
    /*-----------------------------------------------------------------------------------���ص�*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(QueryCallBack callBack, Object... params);
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(QueryCallBack callBack, Object... params);
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(int pageSize, QueryCallBack callBack, Object... params);
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(QueryCallBack callBack, Object... params);
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(QueryCallBack callBack, Object... params);
};