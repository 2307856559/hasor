package org.platform.dbmapping;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ��չ�������
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface ExtTable {
    /**���ڴ����չ���Ա�ı�����*/
    public String extTable();
    /**�����У�������Ҫ����varchar��*/
    public String extPKColumn();
    /**��չ���Ա����ڹ�������������ϵ�����С�*/
    public String extFKColumn();
    /**key�ֶΣ�����extMode����Rowģʽ����Ч��*/
    public String extKeyColumn() default "key";
    /**var�ֶΣ�����extMode����Rowģʽ����Ч��*/
    public String extVarColumn() default "var";
}