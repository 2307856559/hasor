package org.platform.api.safety;
import java.util.Map;
/**
 * �����½��֤
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface IAuthorization {
    /**��½ϵͳ*/
    public IUser login(Map<String, Object> params);
    /**�˳�ϵͳ*/
    public void exit(IUser userInfo);
}