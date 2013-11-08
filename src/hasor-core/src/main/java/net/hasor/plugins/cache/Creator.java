package net.hasor.plugins.cache;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ����һ����������ṩ�ߣ�����˸ýӿڵ������Ҫ��ʵ��{@link CacheCreator}�ӿڡ�
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Creator {}