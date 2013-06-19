/*
 * Copyright 2002-2005 the original author or authors.
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
package org.platform.jdbc;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
/**
 * 
 * @version : 2013-5-7
 * @author ������ (zyc@byshell.org)
 */
public interface JdbcOperations {
    /**�Ƿ���������*/
    public boolean hasTransaction();
    /**�ύ����*/
    public void commitTransaction();
    /**�ع�����*/
    public void rollbackTransaction();
    /**������*/
    public void beginTransaction();
    /*---------------------------------------------------------------------------------------------*/
    /**ʹ��{@link Connection}�ӿ�ֱ��ִ��JDBC���ݲ�����*/
    public <T> T execute(ConnectionCallback<T> action) throws SQLException;
    /**ʹ��{@link Statement}�ӿ�ִ��JDBC���ݲ�����*/
    public <T> T execute(StatementCallback<T> action) throws SQLException;
    /**ʹ��{@link PreparedStatement}�ӿ�ִ��JDBC���ݲ�����*/
    public <T> T execute(String callString, PreparedStatementCallback<T> action) throws SQLException;
    /**ʹ��{@link CallableStatement}�ӿ�ִ��JDBC���ݲ�����*/
    public <T> T execute(String callString, CallableStatementCallback<T> action) throws SQLException;
    /**ִ��sql��������ִ��DDL��䡣*/
    public void execute(String sqlQuery) throws SQLException;
    /**ִ��sql��������ִ��DDL��䡣*/
    public void execute(String sqlQuery, Object... args) throws SQLException;
    //
    /**����{@link PreparedStatement}�ӿڶ�������ѯSQL��
     * @see java.sql.Types */
    public List<Map<String, Object>> queryForList(String sqlQuery) throws SQLException;
    /**����{@link PreparedStatement}�ӿڶ�������ѯSQL��
     * @see java.sql.Types */
    public List<Map<String, Object>> queryForList(String sqlQuery, Object... args) throws SQLException;
    //
    /**����{@link PreparedStatement}�ӿڶ�������ѯSQL��
     * @see java.sql.Types */
    public Map<String, Object> queryForMap(String sqlQuery) throws SQLException;
    /**����{@link PreparedStatement}�ӿڶ�������ѯSQL��
     * @see java.sql.Types */
    public Map<String, Object> queryForMap(String sqlQuery, Object... args) throws SQLException;
    //
    /**ִ��SQL������䣬������Ӱ���������*/
    public int update(String sqlQuery) throws SQLException;
    /**ִ��SQL������䣬������Ӱ���������*/
    public int update(String sqlQuery, Object... args) throws SQLException;
    //
    /**��һ������ִ�������������*/
    public int[] batchUpdate(String[] sqls);
    /**��һ������ִ�������������*/
    public int[] batchUpdate(String sqls, List<Object[]> args);
    /**��һ������ִ�������������*/
    public int[] batchUpdate(String sqls, List<Object[]> args, List<int[]> argTypes);
    //
    /**��ѯһ����¼�������ַ�����ʽ������*/
    public String queryForString(String sql) throws SQLException;
    /**��ѯһ����¼�������ַ�����ʽ������*/
    public String queryForString(String sql, Object... args) throws SQLException;
    /**��ѯһ����¼������int��ʽ������*/
    public int queryForInt(String sql) throws SQLException;
    /**��ѯһ����¼������int��ʽ������*/
    public int queryForInt(String sql, Object... args) throws SQLException;
    /**��ѯһ����¼������long��ʽ������*/
    public long queryForLong(String sql) throws SQLException;
    /**��ѯһ����¼������long��ʽ������*/
    public long queryForLong(String sql, Object... args) throws SQLException;
    /**��ѯһ����¼������Object��ʽ������*/
    public <T> T queryForObject(String sql, Class<T> requiredType) throws SQLException;
    /**��ѯһ����¼������Object��ʽ������*/
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws SQLException;
}