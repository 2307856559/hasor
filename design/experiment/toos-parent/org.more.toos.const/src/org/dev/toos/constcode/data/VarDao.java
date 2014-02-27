package org.dev.toos.constcode.data;
import java.util.List;
import org.dev.toos.constcode.metadata.ConstBean;
import org.dev.toos.constcode.metadata.ConstVarBean;
/**
 * 
 * @version : 2013-2-17
 * @author ������ (zyc@byshell.org)
 */
public abstract class VarDao {
    protected abstract void initDao() throws Throwable;
    protected abstract <T> Source<T> getSource();
    //
    //
    /**ɾ������ֵ*/
    public abstract boolean deleteVar(ConstVarBean targetVar);
    /**��ȡ���㼶������*/
    public abstract List<ConstVarBean> getVarRoots(ConstBean parentConst);
    /**��ȡ�����������ӽڵ㡣*/
    public abstract List<ConstVarBean> getVarChildren(ConstVarBean parentVar);
    /**��ӳ���,������ӳɹ�֮��־û��ĳ����������ʧ�ܷ���null��*/
    public abstract ConstVarBean addVar(ConstVarBean newVar, int newVarIndex);
    /**���³���,���ظ��³ɹ�֮��־û��ĳ����������ʧ�ܷ���null��*/
    public abstract ConstVarBean updateVar(ConstVarBean targetVar, int newVarIndex);
}