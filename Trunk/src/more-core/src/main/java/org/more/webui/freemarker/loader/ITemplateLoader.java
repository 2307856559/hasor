package org.more.webui.freemarker.loader;
import freemarker.cache.TemplateLoader;
/**
 * 
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public interface ITemplateLoader extends TemplateLoader {
    /**��ȡ����*/
    public String getType();
}