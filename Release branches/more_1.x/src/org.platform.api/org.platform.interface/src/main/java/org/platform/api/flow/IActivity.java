package org.platform.api.flow;
/**
 * ��������һ���ڵ㡣
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface IActivity {
    /**����*/
    public void start();
    /**ִ�й������ڵ㡣*/
    public void process();
    /**��ɹ���*/
    public void finish();
}