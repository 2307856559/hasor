package org.platform.api.safety;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Ȩ�����ã��������õ��༶��ͷ��������ϡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Power {
    /**Ȩ�޵����*/
    public String value() default "";
    /**Ȩ����֤�ȼ���*/
    public Level level() default Level.PassLogin;
    /**
     * ��֤����ö��
     * @version : 2013-3-12
     * @author ������ (zyc@byshell.org)
     */
    public static enum Level {
        /**���ɷ��ʡ�Level 0*/
        Free,
        /**��Ҫ������½��Level 1*/
        PassLogin,
        /**
         * ��Ҫͨ�����Լ�顣Level 2
         * @see org.platform.api.safety.IPowerPolicy
         */
        PassPolicy
    }
}