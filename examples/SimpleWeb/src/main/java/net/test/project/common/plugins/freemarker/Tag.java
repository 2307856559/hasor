package org.noe.platform.modules.freemarker;
import java.io.IOException;
import java.util.Map;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
/***
 * �Զ����ǩ
 * @version : 2013-5-14
 * @author ������ (zyc@byshell.org)
 */
public interface Tag {
    /**׼����ʼִ�б�ǩ*/
    public boolean beforeTag(Map<String, Object> propxy, Environment environment) throws TemplateException;
    /**ִ�б�ǩ*/
    public void doTag(Map<String, Object> propxy, TemplateBody body) throws TemplateException, IOException;
    /**��ǩִ�����*/
    public void afterTag(Map<String, Object> propxy, Environment environment) throws TemplateException;
}