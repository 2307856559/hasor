package org.more.webui.freemarker.parser;
import org.more.webui.UIInitException;
import org.more.webui.components.UIComponent;
import freemarker.core.TemplateElement;
/**
 * freemarkerģ��Ԫ�ؿ鹳�ӡ�
 * @version : 2012-5-14
 * @author ������ (zyc@byshell.org)
 */
public interface ElementHook {
    /**��ʼ����������ģ���ǩ*/
    public UIComponent beginAtBlcok(TemplateElement e) throws UIInitException;
    /**����������ģ���ǩ����*/
    public void endAtBlcok(TemplateElement e) throws UIInitException;
}