package org.platform.api.dbmapping;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ��һ������ӳ�䵽���ݿ����ϡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Field {
    /**ӳ��ı�������*/
    public String column() default "";
    /**���ֶ��Ƿ������ֵ��*/
    public boolean isNull() default true;
    /**������ó��ȡ�*/
    public int length() default 1000;
    /**Ĭ��ֵ��*/
    public String defaultValue() default "";
    /**�ֶ��Ƿ������¡�*/
    public boolean update() default true;
    /**�ֶ��Ƿ������롣*/
    public boolean insert() default true;
    /**�ֶ��Ƿ�Ϊ�����ء�*/
    public boolean lazy() default false;
    /**���ݿ�ʹ�õ��������͡�*/
    public DBType dbType() default DBType.Nvarchar;
    //    public Type type() default Type.String;//  "string(default)|float|int|double|long|boolean|datetime|uuid|btye|json|bytes"
    //    /**֧�ֵ�Java�������͡�*/
    //    public enum Type {
    //        /**�ַ���*/
    //        String,
    //        /**������*/
    //        Float,
    //        /**����*/
    //        Integer,
    //        /**˫��������*/
    //        Double,
    //        /**������*/
    //        Long,
    //        /**��������*/
    //        Boolean,
    //        /**ʱ������*/
    //        Date,
    //        /**UUID*/
    //        UUID,
    //        /**�ֽ����͡�*/
    //        Btye,
    //        /**�ֽ�����*/
    //        Btyes
    //    }
}