package org.noe.platform.modules.freemarker;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * �Զ����ǩ������˸ýӿڵ������Ҫ��ʵ��{@link Tag}�ӿڡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FmTag {
    /**��ǩ����*/
    public String value();
}