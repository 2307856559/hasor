package org.platform.api.dbmapping;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ����һ��ʵ�����
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Entity {
    /**ʵ�����ƣ�Ĭ��ʹ�ü��������Ϊ��ֵ��*/
    public String name() default "";
    /**ʵ��ӳ��ı�����Ĭ��ʹ�ü��������Ϊ��ֵ��*/
    public String table() default "";
    /***/
    public Mode mode() default Mode.Upate;
    public boolean lazy() default false;
    //
    public static enum Mode {
        /***/
        CreateDrop,
        /***/
        Upate
    }
}