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
package org.more.jdbc;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
/**
 * ������ҵ�����ʹ��Dao��֧�Ż������û����п��Ի��JdbcDaoSupport�����Բ������ݿ⡣
 * ͬʱ���TransactionManager��������������DaoSupport��ô�����Ի��������ƵĹ��ܡ�
 * finalize���������releaseJdbcDaoSupport�������ͷ���Դ��
 * @version 2009-12-16
 * @author ������ (zyc@byshell.org)
 */
public class JobSupport {
    //========================================================================================Field
    private JdbcDaoSupport jdbcDaoSupport; //
    private DataSource     dataSource;    //
    //==========================================================================================Job
    /**��ȡDaoSupport��ʹ�õ�Jdbc����Դ��*/
    public DataSource getDataSource() {
        return dataSource;
    }
    /**����DaoSupport��ʹ�õ�Jdbc����Դ��*/
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**��ȡ����Jdbc��dao�����������������JdbcDaoSupport����DaoSupport����ͼ����һ�������������������������֮��JdbcDaoSupport���������������𴴽���*/
    protected JdbcDaoSupport getJdbcDaoSupport() throws SQLException {
        if (jdbcDaoSupport == null)
            jdbcDaoSupport = new JdbcDaoSupport(this.dataSource.getConnection());
        return jdbcDaoSupport;
    }
    /** ��ȡ���ݿ����� */
    protected Connection getConnection() throws SQLException {
        return this.getJdbcDaoSupport().connection;
    }
    /**����jdbc����*/
    void setJdbcDaoSupport(JdbcDaoSupport jdbcDaoSupport) {
        this.jdbcDaoSupport = jdbcDaoSupport;
    }
    /**�ͷ�Dao֧�Ż����÷����ᵼ������ĵݽ����������ٴε���getJdbcDaoSupport����ʱDao֧�Ż������ٴα����´������÷�����Ҫ�����ֶ��ͷ�������Դ��*/
    public void releaseJdbcDaoSupport() throws SQLException {
        this.jdbcDaoSupport.connection.commit();
        this.jdbcDaoSupport.connection.close();
        this.jdbcDaoSupport = null;
    }
    /**finalize���������releaseJdbcDaoSupport�������ͷ���Դ��*/
    protected void finalize() throws Throwable {
        try {
            this.releaseJdbcDaoSupport();
        } catch (Exception e) {}
    }
}