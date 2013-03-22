package org.dev.toos.constcode.data;
import java.util.List;
import org.dev.toos.constcode.metadata.ConstBean;
/**
 * 
 * @version : 2013-2-17
 * @author ������ (zyc@byshell.org)
 */
public abstract class ConstDao {
    protected abstract void initDao() throws Throwable;
    public abstract Source getSource();
    //
    //
    /**ɾ������*/
    public abstract boolean deleteConst(ConstBean constBean);
    /**��ȡ���㼶������*/
    public abstract List<ConstBean> getRootConst();
    /**��ȡ�����������ӽڵ㡣*/
    public abstract List<ConstBean> getConstChildren(ConstBean constPath);
    /**��ӳ���,������ӳɹ�֮��־û��ĳ����������ʧ�ܷ���null��*/
    public abstract ConstBean addConst(ConstBean constBean, int newIndex);
    /**��ӳ���,������ӳɹ�֮��־û��ĳ����������ʧ�ܷ���null��*/
    public abstract ConstBean updateConst(ConstBean constBean, int newIndex);
}