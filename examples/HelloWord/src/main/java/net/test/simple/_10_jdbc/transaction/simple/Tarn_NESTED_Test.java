/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package net.test.simple._10_jdbc.transaction.simple;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import net.hasor.jdbc.datasource.DataSourceUtils;
import net.hasor.jdbc.template.core.JdbcTemplate;
import net.hasor.jdbc.transaction.TransactionBehavior;
import net.hasor.jdbc.transaction.TransactionManager;
import net.hasor.jdbc.transaction.TransactionStatus;
import net.hasor.jdbc.transaction.support.DefaultTransactionManager;
import net.test.simple._10_jdbc.AbstractJDBCTest;
import org.junit.Test;
/**
 * RROPAGATION_NESTED：嵌套事务
 * @version : 2013-12-10
 * @author 赵永春(zyc@hasor.net)
 */
public class Tarn_NESTED_Test extends AbstractJDBCTest {
    /*条件：环境中没有事务。*/
    @Test
    public void noTarn_Test() throws IOException, URISyntaxException, SQLException {
        DataSource jdbcDS = this.getDataSource();
        TransactionManager tm = new DefaultTransactionManager(jdbcDS);
        {
            /*Begin，开始事务*/
            TransactionStatus status = tm.getTransaction(TransactionBehavior.PROPAGATION_NESTED);
            /*申请连接*/
            Connection con = DataSourceUtils.getConnection(jdbcDS);
            //
            //
            con.createStatement().executeUpdate("insert into TB_User values('deb4f4c8','安妮.TD.雨果','belon','123','belon@hasor.net','2011-06-08 20:08:08');");
            ResultSet res = con.createStatement().executeQuery("select count(*) from TB_User where userUUID='deb4f4c8'");
            res.next();
            System.out.println(res.getInt(1));
            //
            //
            /*释放连接*/
            DataSourceUtils.releaseConnection(con, jdbcDS);
            /*commit，递交事务*/
            tm.commit(status);
        }
        System.out.println(this.getJdbcTemplate().queryForInt("select count(*) from TB_User where userUUID='deb4f4c8'"));
    }
    /*条件：环境中存在事务。*/
    @Test
    public void hasTarn_Test() throws IOException, URISyntaxException, SQLException {
        JdbcTemplate jdbc = this.getJdbcTemplate();
        TransactionManager tm = new DefaultTransactionManager(jdbc.getDataSource());
        //1.获取连接并创建事务
        Connection con = DataSourceUtils.getConnection(jdbc.getDataSource());
        con.setAutoCommit(false);
        System.out.println(jdbc.queryForInt("select count(*) from TB_User "));
        jdbc.execute("insert into TB_User values('18c48158','蒙奇.TD.雨果','belon','123','belon@hasor.net','2011-06-08 20:08:08');");//执行插入语句
        {
            //begin
            TransactionStatus status = tm.getTransaction(TransactionBehavior.PROPAGATION_NESTED);
            jdbc.execute("insert into TB_User values('deb4f4c8','安妮.TD.雨果','belon','123','belon@hasor.net','2011-06-08 20:08:08');");//执行插入语句
            System.out.println(jdbc.queryForInt("select count(*) from TB_User"));
            //rollBack
            tm.rollBack(status);
            System.out.println(jdbc.queryForInt("select count(*) from TB_User "));
        }
        DataSourceUtils.releaseConnection(con, jdbc.getDataSource());
    }
}