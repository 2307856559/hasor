package org.more.webui.freemarker.parser;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.more.webui.components.UIComponent;
import org.more.webui.components.UIViewRoot;
import freemarker.core.TemplateElement;
import freemarker.template.Template;
/**
 * ����ݹ�ɨ��ģ��Ԫ���Դ���{@link UIViewRoot}����
 * @version : 2012-5-13
 * @author ������ (zyc@byshell.org)
 */
public class TemplateScanner {
    private Map<String, ElementHook> blockRegister = new HashMap<String, ElementHook>();
    //
    public void addElementHook(String itemType, ElementHook hook) {
        this.blockRegister.put(itemType, hook);
    }
    /**����ģ����������{@link UIViewRoot}*/
    public UIViewRoot parser(Template template, UIViewRoot uiViewRoot) throws Throwable {
        TemplateElement rootNode = template.getRootTreeNode();
        return (UIViewRoot) parserElement(rootNode, uiViewRoot);
    }
    /**elementҪ������Ԫ�أ�componentParent��ǰ�������*/
    private UIComponent parserElement(TemplateElement element, UIComponent componentParent) throws Throwable {
        Enumeration<TemplateElement> enumItems = element.children();
        while (enumItems.hasMoreElements() == true) {
            //�ݹ�ɨ������ģ��ڵ㡣
            TemplateElement e = enumItems.nextElement();
            Class<?> blockType = e.getClass();
            ElementHook hook = this.blockRegister.get(blockType.getSimpleName());
            //componentItem��������ᱣ֤ÿ�ε���ElementHook�����UIComponent����TemplateElement��ǩ�����ĸ���UIComponent��
            //ͬʱ��Ҳ��֤�ڵݹ����parserElement�����Ĺ�����element������Զ��componentParent��������µı�ǩ��
            UIComponent componentItem = null;
            if (hook != null)
                componentItem = hook.beginAtBlcok(e);//�ڽ���Ԫ��ʱ���������һ��UIComponent�����UIComponent���뵽componentParent
            if (componentItem != null)
                componentParent.getChildren().add(componentItem);
            this.parserElement(e, (componentItem != null) ? componentItem : componentParent);//�ݹ����
            if (hook != null)
                hook.endAtBlcok(e);
        }
        return componentParent;
    }
}