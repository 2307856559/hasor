package org.more.webui.freemarker.parser;
import java.lang.reflect.Field;
import java.util.Map;
import org.more.webui._.Register;
import org.more.webui.components.UIComponent;
import freemarker.core.Expression;
import freemarker.core.TemplateElement;
/**
 * ������ݱ�ǩԪ�ش����齨����
 * @version : 2012-5-14
 * @author ������ (zyc@byshell.org)
 */
public class Hook_UserTag implements ElementHook {
    private Register register = null; //ע����
    //
    public Hook_UserTag(Register register) {
        if (register == null)
            throw new NullPointerException("param ��component_Register�� si null.");
        this.register = register;
    }
    @Override
    public UIComponent beginAtBlcok(TemplateElement e) throws Throwable {
        String tagName = e.getDescription().split(" ")[1];
        String componentType = this.register.getMappingComponentByTagName(tagName);
        if (componentType == null)
            return null;
        //A.�����齨
        UIComponent com = this.register.createComponent(componentType);
        //B.װ�����Զ���
        Field field = e.getClass().getDeclaredField("namedArgs");
        field.setAccessible(true);
        Map<String, Expression> namedArgs = (Map<String, Expression>) field.get(e);
        for (String key : namedArgs.keySet()) {
            Expression exp = namedArgs.get(key);
            com.setPropertyText(key, exp.getSource());
        }
        return com;
    }
    @Override
    public void endAtBlcok(TemplateElement e) {}
}