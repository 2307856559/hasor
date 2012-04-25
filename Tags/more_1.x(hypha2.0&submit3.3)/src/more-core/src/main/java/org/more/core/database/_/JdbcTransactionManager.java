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
package org.more.core.database._;
import java.sql.Connection;
import javax.sql.DataSource;
import org.more.core.classcode.AopFilterChain;
import org.more.core.classcode.AopInvokeFilter;
import org.more.core.classcode.Method;
import org.more.core.error.TransformException;
/**
 * JobSupport�������������
 * @version 2009-12-15
 * @author ������ (zyc@byshell.org)
 */
public class JdbcTransactionManager implements AopInvokeFilter {
    //========================================================================================Field
    private DataSource           dataSource;
    private boolean              autoCommit = false;
    private ThreadLocal<Boolean> running    = new ThreadLocal<Boolean>(); //��ҵ������Ƕ���������ʱ�ö���ȷ���Զ��������ʼ������Ϊһ��ԭ�ӡ�
    //==========================================================================================Job
    public boolean isAutoCommit() {
        return autoCommit;
    }
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public Object doFilter(Object target, Method method, Object[] args, AopFilterChain chain) throws Throwable {
        String mn = method.getProxyMethod().getName();
        if (mn.equals("getDataSource") == true || mn.equals("setDataSource") == true || //
                mn.equals("getJdbcDaoSupport") == true || mn.equals("setJdbcDaoSupport") == true)
            return chain.doInvokeFilter(target, method, args);
        //----------
        if (target instanceof JobSupport == false)
            throw new TransformException("����" + target.getClass() + "û�м̳�JobSupport�����޷�ʵ��������ơ�");
        JobSupport ds = (JobSupport) target;
        //
        Boolean isRunning = this.running.get();
        if (isRunning == null || isRunning == false) {
            this.running.set(true);
            /*---------------------------*/
            if (ds.getDataSource() == null)
                ds.setDataSource(dataSource);
            Connection conn = null;
            if (ds.getJdbcDaoSupport() == null) {
                conn = this.dataSource.getConnection();
                ds.setJdbcDaoSupport(new JdbcDaoSupport(conn));
            } else
                conn = ds.getJdbcDaoSupport().connection;
            //
            try {
                this.begin(conn);
                Object obj = chain.doInvokeFilter(target, method, args);
                this.commit(conn);
                return obj;
            } catch (Exception e) {
                this.rollBack(conn);
                throw e;
            } finally {
                ds.getConnection().close();
                ds.setJdbcDaoSupport(null);
                this.running.remove();
            }
            /*---------------------------*/
        } else
            return chain.doInvokeFilter(target, method, args);
    }
    //==========================================================================================Job
    private void begin(Connection conn) throws Throwable {
        if (autoCommit == false)
            conn.setAutoCommit(false);
    };
    private void commit(Connection conn) throws Throwable {
        if (autoCommit == false)
            conn.commit();
    };
    private void rollBack(Connection conn) throws Throwable {
        if (autoCommit == false)
            conn.rollback();
    }
}