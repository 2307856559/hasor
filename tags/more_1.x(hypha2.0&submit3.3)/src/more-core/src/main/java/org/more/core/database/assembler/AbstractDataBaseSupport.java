package org.more.core.database.assembler;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.more.core.database.DataBaseSupport;
import org.more.core.database.PagesList;
import org.more.core.database.Query;
import org.more.core.database.QueryCallBack;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
/**
 * 
 * @version : 2011-11-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDataBaseSupport implements DataBaseSupport {
    protected Log      logger     = LogFactory.getLog(this.getClass());
    private DataSource dataSource = null;
    /***/
    public AbstractDataBaseSupport(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**��ȡʹ�õ�����Դ����*/
    public DataSource getDataSource() {
        return this.dataSource;
    }
    /**��ȡ���Ӷ���*/
    protected Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
    public void insertForMap(String tableName, Map<String, Object> values) {
        // TODO Auto-generated method stub
    }
    public void updateForMap(String tableName, Map<String, Object> values, Map<String, Object> whereMap) {
        // TODO Auto-generated method stub
    }
    /*-----------------------------------------------------------------------------------XXXX*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(String queryString) {
        return this.createQuery(queryString).executeQuery();
    };
    /**ִ�в�ѯ���Բ��ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(String queryString) {
        return this.createQuery(queryString).query();
    };
    /**ִ�в�ѯ���Բ��ҷ��������ѯ���.*/
    public <E> List<E> query(String queryString, Class<E> toType) {
        return this.createQuery(queryString).query(toType);
    };
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(String queryString, int pageSize) {
        return this.createQuery(queryString).queryForPages(pageSize);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(String queryString) {
        return this.createQuery(queryString).firstUnique();
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E firstUnique(String queryString, Class<E> toType) {
        return this.createQuery(queryString).firstUnique(toType);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(String queryString) {
        return this.createQuery(queryString).lastUnique();
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E lastUnique(String queryString, Class<E> toType) {
        return this.createQuery(queryString).lastUnique(toType);
    };
    /*-----------------------------------------------------------------------------------������*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(String queryString, Object... params) {
        return this.createQuery(queryString).executeQuery(params);
    };
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(String queryString, Object... params) {
        return this.createQuery(queryString).query(params);
    };
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public <E> List<E> query(String queryString, Class<E> toType, Object... params) {
        return this.createQuery(queryString).query(toType, params);
    };
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(String queryString, int pageSize, Object... params) {
        return this.createQuery(queryString).queryForPages(pageSize, params);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(String queryString, Object... params) {
        return this.createQuery(queryString).firstUnique(params);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E firstUnique(String queryString, Class<E> toType, Object... params) {
        return this.createQuery(queryString).firstUnique(toType, params);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(String queryString, Object... params) {
        return this.createQuery(queryString).lastUnique(params);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E lastUnique(String queryString, Class<E> toType, Object... params) {
        return this.createQuery(queryString).lastUnique(toType, params);
    };
    /*-----------------------------------------------------------------------------------���ص�*/
    /**ִ�в�ѯ������Ӱ���������*/
    public int executeQuery(String queryString, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).executeQuery(callBack, params);
    };
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public List<Map<String, Object>> query(String queryString, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).query(callBack, params);
    };
    /**ִ�в�ѯ���ҷ��������ѯ���.*/
    public <E> List<E> query(String queryString, Class<E> toType, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).query(toType, callBack, params);
    };
    /**ִ�в�ѯ������ѯ������з�ҳ��*/
    public PagesList queryForPages(String queryString, int pageSize, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).queryForPages(pageSize, callBack, params);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object firstUnique(String queryString, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).firstUnique(callBack, params);
    };
    /**��ȡ����ѯ����ĵ�һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E firstUnique(String queryString, Class<E> toType, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).firstUnique(toType, callBack, params);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public Object lastUnique(String queryString, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).lastUnique(callBack, params);
    };
    /**��ȡ����ѯ��������һ������.�����ѯ���Ϊ���򷵻�null.*/
    public <E> E lastUnique(String queryString, Class<E> toType, QueryCallBack callBack, Object... params) {
        return this.createQuery(queryString).lastUnique(toType, callBack, params);
    };
};