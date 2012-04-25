package org.more.core.database.assembler.jdbc.sqlserver;
import javax.sql.DataSource;
import org.more.core.database.assembler.AbstractDataBaseSupport;
/**
 * SQL Server��Ĭ��ʵ�֡�
 * @version : 2011-11-9
 * @author ������ (zyc@byshell.org)
 */
public class SQLDataBaseSupport extends AbstractDataBaseSupport {
    public SQLDataBaseSupport(DataSource dataSource) {
        super(dataSource);
    }
    /**����sql��䴴��һ����ѯ�ӿڶ���.���øö�����Խ��и��Ӳ�ѯ.*/
    public SQLQuery createQuery(String sql) {
        return new SQLQuery(sql, this);
    };
};