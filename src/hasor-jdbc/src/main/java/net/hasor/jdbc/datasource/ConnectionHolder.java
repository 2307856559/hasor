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
package net.hasor.jdbc.datasource;
import java.sql.Connection;
import java.util.Map;
import javax.sql.DataSource;
import net.hasor.plugins.transaction.core.ds.ConnectionHandle;
/**
 * 
 * @version : 2013-10-30
 * @author ������(zyc@hasor.net)
 */
class ConnectionHolder {
    private static final ThreadLocal<Map<DataSource, ConnectionHandle>> ResourcesLocal = new ThreadLocal<Map<DataSource, ConnectionHandle>>();
    private int                                                         referenceCount;
    /**�������ü���,һ����Ϊ�������ѱ�����*/
    public void requested() {
        this.referenceCount++;
    }
    /**�������ü���,һ����Ϊ�������ѱ��ͷš�*/
    public void released() {
        this.referenceCount--;
        if (!isOpen() && this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }
    private boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }
    /**��ȡ����*/
    public Connection getConnection() {};
}