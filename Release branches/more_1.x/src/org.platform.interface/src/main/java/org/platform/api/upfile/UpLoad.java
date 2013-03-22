package org.platform.api.upfile;
/**
 * �ϴ��ļ��Ĵ����࣬��Ҫʵ��{@link IUpFile}�ӿڡ�
 * @see org.platform.api.upfile.IUpFile
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public @interface UpLoad {
    /**�ϴ��ļ���ע������*/
    public String value() default "";
    /**�ϴ��ļ��������󳤶ȡ�Ĭ�ϣ�-1(�����)*/
    public long maxSize() default -1;
    /**�ϴ��ļ��������С���ȡ�Ĭ�ϣ�0*/
    public long minSize() default 0;
    /**�Ƿ�������ļ��ϴ���*/
    public boolean allowMulti() default false;
    /**������ļ����͡�*/
    public String[] allowFiles();
    /**�ϴ�����Ҫ���趨��Ĭ��:Require.Simple*/
    public Require require() default Require.Simple;
    /**
     * �ϴ�����Ҫ���趨ö��
     * @version : 2013-3-12
     * @author ������ (zyc@byshell.org)
     */
    public static enum Require {
        /**���ϴ���������Ҫ��Level 0*/
        Simple,
        /**
         * ��Ҫͨ�����Լ�顣Level 1
         * @see org.platform.api.upfile.IUpFilePolicy
         */
        PassPolicy
    }
}