package org.platform.api.upfile;
import java.io.IOException;
import java.util.List;
/**
 * �����ļ��ϴ��Ľӿ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface IUpFile {
    /***
     * �����ļ��ϴ���
     * @param upData �ϴ�����Я����������Ϣ��
     * @param list ���ֵ��ļ������ֶ���Ŀ��
     */
    public void doUpFile(IUpInfo upData, List<IFileItem> list) throws IOException;
}