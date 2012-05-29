package org.more.webui.freemarker.parser;
import org.more.webui.UIInitException;
import org.more.webui.components.UIComponent;
import org.more.webui.context.FacesConfig;
import freemarker.core.TemplateElement;
import freemarker.template.Template;
/**
 * ������ݱ�ǩԪ�ش����齨���󣬸�����freemarker��ǿ�ҵİ汾����Ҫ�󡣸���freemarker�汾���ܻ��������⡣
 * @version : 2012-5-14
 * @author ������ (zyc@byshell.org)
 */
public class Hook_Include implements ElementHook {
    private FacesConfig facesConfig = null; //ע����
    //
    public Hook_Include(FacesConfig facesConfig) {
        if (facesConfig == null)
            throw new NullPointerException("param ��FacesConfig�� si null.");
        this.facesConfig = facesConfig;
    }
    @Override
    public UIComponent beginAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent) throws UIInitException {
        try {
            String includeName = e.getDescription().split(" ")[1];
            includeName = includeName.substring(1, includeName.length() - 1);
            Template includeTemp = e.getTemplate().getConfiguration().getTemplate(includeName);
            scanner.parser(includeTemp, parent);
            return null;
        } catch (Exception e2) {
            throw new UIInitException("�����쳣������include��������" + e.getDescription() + "��", e2);
        }
    }
    @Override
    public void endAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent) throws UIInitException {}
}