package org.platform.web;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.Filter;
/**
 * ����һ��Filter����Filter��Ҫʵ��{@link Filter}�ӿڡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface WebFilter {
    /**�Է����������Ϣ��
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String description() default "";
    /**�ڹ������̨��ʾ����ʱʹ��displayName���ԡ�
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String displayName() default "";
    /**Filter�ڹ��������ϵ�˳��Ĭ�ϣ�0������Խ������Խ�ӳ١�
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public int sort() default 0;
    /** ���������������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public WebInitParam[] initParams() default {};
    /** ���������ƻ�ID */
    public String filterName() default "";
    /** The small-icon of the filter.
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String smallIcon() default "";
    /** The large-icon of the filter.
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String largeIcon() default "";
    /**
     * URLƥ�������{@link WebFilter#urlPatterns()}���Ա�ʾͬ����Ч��
     * @see org.platform.web.WebFilter#urlPatterns()
     */
    public String[] value() default {};
    /**
     * URLƥ�������{@link WebFilter#value()}���Ա�ʾͬ����Ч��
     * @see org.platform.web.WebFilter#value()
     */
    public String[] urlPatterns() default {};
}