/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
import java.sql.SQLException;
import net.hasor.jdbc.datasource.DataSourceUtils;
import net.hasor.jdbc.template.core.JdbcTemplate;
import net.hasor.jdbc.transaction.TransactionBehavior;
import net.hasor.jdbc.transaction.TransactionManager;
import net.hasor.jdbc.transaction.TransactionStatus;
import net.hasor.jdbc.transaction.support.DefaultTransactionManager;
import net.test.simple._10_jdbc.AbstractJDBCTest;
import org.junit.Test;
/**
 * RROPAGATION_REQUIRES_NEW����������
 * @version : 2013-12-10
 * @author ������(zyc@hasor.net)
 */
public class Tarn_REQUIRED_New_Test extends AbstractJDBCTest {
    /*������������û������*/
    @Test
    public void noTarn_Test() throws IOException, URISyntaxException, SQLException {
        JdbcTemplate jdbc = this.getJdbcTemplate();
        TransactionManager tm = new DefaultTransactionManager(jdbc.getDataSource());
        {
            //begin
            TransactionStatus status = tm.getTransaction(TransactionBehavior.RROPAGATION_REQUIRES_NEW);
            jdbc.execute("insert into TB_User values('deb4f4c8','����.TD.���','belon','123','belon@hasor.net','2011-06-08 20:08:08');");//ִ�в������
            System.out.println(jdbc.queryForInt("select count(*) from TB_User where userUUID='deb4f4c8'"));
            //commit
            //status.setReadOnly();//�����������Ϊֻ���������еݽ������ᱻ�ع���
            tm.commit(status);
        }
        System.out.println(jdbc.queryForInt("select count(*) from TB_User where userUUID='deb4f4c8'"));
    }
    /*�����������д�������*/
    @Test
    public void hasTarn_Test() throws IOException, URISyntaxException, SQLException {
        JdbcTemplate jdbc = this.getJdbcTemplate();
        TransactionManager tm = new DefaultTransactionManager(jdbc.getDataSource());
        //1.��ȡ���Ӳ���������
        Connection con = DataSourceUtils.getConnection(jdbc.getDataSource());
        con.setAutoCommit(false);
        {
            //begin
            TransactionStatus status = tm.getTransaction(TransactionBehavior.RROPAGATION_REQUIRES_NEW);
            jdbc.execute("insert into TB_User values('deb4f4c8','����.TD.���','belon','123','belon@hasor.net','2011-06-08 20:08:08');");//ִ�в������
            System.out.println(jdbc.queryForInt("select count(*) from TB_User where userUUID='deb4f4c8'"));
            //commit
            tm.commit(status);
            System.out.println(jdbc.queryForInt("select count(*) from TB_User where userUUID='deb4f4c8'"));
        }
        DataSourceUtils.releaseConnection(con, jdbc.getDataSource());
    }
}