package org.dev.toos.constcode.data;
/**
 * 
 * @version : 2013-2-17
 * @author ������ (zyc@byshell.org)
 */
public interface Source<T> {
    /**��ȡ���ڲ������ݵ�����Դ����*/
    public T getSource() throws Throwable;
    /**���Ը�����Դ�����Ƿ�֧���޸Ĳ�����*/
    public boolean canModify();
    /**���������Դ���е��޸Ĳ���������������һЩ���߱��Զ����������Դ�кܴ�����塣*/
    public void save() throws Throwable;
    /**��������Դ֪��֧���Զ����档*/
    public boolean isAutoSave();
    /**��������Դ�Զ����档*/
    public void setAutoSave(boolean autoSave);
    /**����Դ���Ƿ����ݷ����仯��*/
    public boolean isUpdate();
}