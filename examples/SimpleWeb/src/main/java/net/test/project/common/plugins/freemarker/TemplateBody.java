package org.noe.platform.modules.freemarker;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
/**
 * 
 * @version : 2012-6-14
 * @author ������ (zyc@byshell.org)
 */
public interface TemplateBody {
    /**��ǩ����*/
    public Map<String, Object> tagProperty();
    /**��ȡ��ǩִ�л���*/
    public Environment getEnvironment();
    /**��Ⱦ�����ǩ����*/
    public void doBody(Writer arg0) throws TemplateException, IOException;
    /**��Ⱦ�����ǩ����*/
    public void doBody() throws TemplateException, IOException;
}