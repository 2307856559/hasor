package org.platform.api.safety;
/**
 * Ȩ����֤����
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface IPowerPolicy {
    /**
     * ���в��Լ�顣
     * @param userInfo �û���Ϣ����
     * @param powerCode Ҫ����Ȩ�޵㡣
     */
    public boolean test(IUser userInfo, Object powerCode);
}