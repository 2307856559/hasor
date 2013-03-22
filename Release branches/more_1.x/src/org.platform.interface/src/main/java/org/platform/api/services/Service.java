package org.platform.api.services;
import org.platform.api.safety.Power;
/**
 * ����һ��Services����
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public @interface Service {
    /**Servlet����������*/
    public String scope() default "";
    /** The description of the filter */
    public String description() default "";
    /** The display name of the filter */
    public String displayName() default "";
    /**Service �Ĺ�����Χ��*/
    public Access access() default Access.Private;
    /**Service ���ơ�*/
    public String[] value();
    /**�Ƿ��ӳٳ�ʼ����*/
    public boolean lazyInit() default false;
    /**
     * ������Χö��
     * @version : 2013-3-12
     * @author ������ (zyc@byshell.org)
     */
    public static enum Access {
        /**��ȫ������*/
        Public,
        /**
         * ��Ҫͨ����֤֮��ſ���ʹ�á�������Ƶ��ض���Ȩ�޵�������ʹ��{@link Power}ע��������á�
         * @see org.platform.faces.safety.Power
         */
        Protected,
        /**ֻ��Ӧ�ó����ڲ�ʹ�ã����������⹫����*/
        Private,
    }
}