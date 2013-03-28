package org.platform.dbmapping;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ��ʾһ�����ݿ�������
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ID {
    /**ӳ��ı�������*/
    public String column() default "";
    /**���ֶ��Ƿ������ֵ��*/
    public boolean isNull() default true;
    /**������ó��ȡ�*/
    public int length() default 1000;
    /**Ĭ��ֵ��*/
    public String defaultValue() default "";
    /**�ֶ��Ƿ������¡�*/
    public boolean updateMode() default true;
    /**�ֶ��Ƿ������롣*/
    public boolean insertMode() default true;
    /**�ֶ��Ƿ�Ϊ�����ء�*/
    public boolean lazy() default false;
    /**���ݿ�ʹ�õ��������͡�*/
    public DBType dbType() default DBType.UUID;
    //
    //
    /**ʹ�õ��������ɲ��ԡ�*/
    public String keyGenerator() default "uuid.string";
}