package org.noe.platform.modules.freemarker;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * �Զ��庯������ע�����ڷ����ϣ����췽�����⣩��
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface FmMethod {
    /**��������*/
    public String value();
}