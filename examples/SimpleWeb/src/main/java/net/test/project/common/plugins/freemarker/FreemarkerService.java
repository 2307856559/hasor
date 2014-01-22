package org.noe.platform.modules.freemarker;
import java.io.IOException;
import java.io.Writer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * Freemarkerģ�幦���ṩ�ࡣ
 * @version : 2013-5-6
 * @author ������ (zyc@byshell.org)
 */
public interface FreemarkerService {
    /**��ȡ����ִ��ģ���Freemarker*/
    public Configuration getFreemarker();
    /**��ȡģ�塣*/
    public Template getTemplate(String templateName) throws TemplateException, IOException;
    //
    /**��ȡ��ִ��ģ�塣*/
    public void processTemplate(String templateName) throws TemplateException, IOException;
    /**��ȡ��ִ��ģ�塣*/
    public void processTemplate(String templateName, Object rootMap) throws TemplateException, IOException;
    /**��ȡ��ִ��ģ�塣*/
    public void processTemplate(String templateName, Object rootMap, Writer writer) throws TemplateException, IOException;
    //
    /**���ַ�����������Ϊģ��ִ�С�*/
    public String processString(String templateString) throws TemplateException, IOException;
    /**���ַ�����������Ϊģ��ִ�С�*/
    public String processString(String templateString, Object rootMap) throws TemplateException, IOException;
    //
    /**���ַ�����������Ϊģ��ִ�С�*/
    public void processString(String templateString, Writer writer) throws TemplateException, IOException;
    /**���ַ�����������Ϊģ��ִ�С�*/
    public void processString(String templateString, Object rootMap, Writer writer) throws TemplateException, IOException;
}