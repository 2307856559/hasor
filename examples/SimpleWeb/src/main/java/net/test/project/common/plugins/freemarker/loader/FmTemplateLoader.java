package org.noe.platform.modules.freemarker.loader;
import freemarker.cache.TemplateLoader;
/**
 * 
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public interface FmTemplateLoader extends TemplateLoader {
    /**��ȡ����*/
    public String getType();
}