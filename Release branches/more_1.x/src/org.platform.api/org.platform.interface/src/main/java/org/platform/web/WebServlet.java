package org.platform.web;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.http.HttpServlet;
/**
 * ����һ��Servlet����Servlet��Ҫ�̳�{@link HttpServlet}�ࡣ
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface WebServlet {
    /**�Է����������Ϣ��
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String description() default "";
    /**�ڹ������̨��ʾ����ʱʹ��displayName���ԡ�
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String displayName() default "";
    /**Servlet�ڹ��������ϵ�˳��Ĭ�ϣ�0������Խ������Խ�ӳ١�
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public int loadOnStartup() default 0;
    /** ���������������
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public WebInitParam[] initParams() default {};
    /** Servlet���ƻ�ID */
    public String servletName() default "";
    /** The small-icon of the filter.
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String smallIcon() default "";
    /** The large-icon of the filter.
     * <br/><b>ע��</b><i>��ֵ����ͨ���������̨���������á�</i>*/
    public String largeIcon() default "";
    /**
     * URLƥ�������{@link WebServlet#urlPatterns()}���Ա�ʾͬ����Ч��
     * @see org.platform.web.WebServlet#urlPatterns()
     */
    public String[] value() default {};
    /**
     * URLƥ�������{@link WebServlet#value()}���Ա�ʾͬ����Ч��
     * @see org.platform.web.WebServlet#value()
     */
    public String[] urlPatterns() default {};
}