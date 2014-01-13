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
package net.test.simple._10_jdbc.curd;
import net.hasor.jdbc.core.JdbcTemplate;
import net.test.simple._10_jdbc.AbstractJDBCTest;
import org.junit.Test;
/***
 * ������ɾ�Ĳ����
 * @version : 2014-1-13
 * @author ������(zyc@hasor.net)
 */
public class CURD_Test extends AbstractJDBCTest {
    /*ʹ�� insert ��������*/
    @Test
    public void insert() throws Exception {
        /*��ȡ JDBC �����ӿڡ�*/
        JdbcTemplate jdbc = getJdbcTemplate();
        String insertUser = "insert into TB_User values('deb4f4c8-5ba1-4f76-8b4a-c2be028bf57b','����.��¡','belon','123','belon@hasor.net','2011-06-08 20:08:08');";
        jdbc.execute(insertUser);//ִ�в������
        //
    }
    /*ʹ�� update ��������*/
    @Test
    public void update() throws Exception {
        /*��ȡ JDBC �����ӿڡ�*/
        JdbcTemplate jdbc = getJdbcTemplate();
    }
    /*ʹ�� delete ɾ������*/
    @Test
    public void delete() throws Exception {
        /*��ȡ JDBC �����ӿڡ�*/
        JdbcTemplate jdbc = getJdbcTemplate();
        //
    }
    /*ʹ�� select ��ѯ����*/
    @Test
    public void select() throws Exception {
        /*��ȡ JDBC �����ӿڡ�*/
        JdbcTemplate jdbc = getJdbcTemplate();
        //
    }
}