package org.platform.api.upfile;
import java.util.List;
/**
 * �ϴ��ļ��Ĳ��Լ�顣
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface IUpFilePolicy {
    /**
     * ���в��Լ�顣
     * @param userInfo �û���Ϣ
     * @param upData �ϴ�������
     * @param list ���������ļ��б�
     */
    public boolean test(IUpInfo upData, List<IFileItem> list);
}