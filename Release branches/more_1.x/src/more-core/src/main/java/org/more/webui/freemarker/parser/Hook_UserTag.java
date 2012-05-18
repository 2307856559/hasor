package org.more.webui.freemarker.parser;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import org.more.webui.UIInitException;
import org.more.webui.components.UIComponent;
import org.more.webui.context.Register;
import freemarker.core.Expression;
import freemarker.core.TemplateElement;
/**
 * ������ݱ�ǩԪ�ش����齨���󣬸�����freemarker��ǿ�ҵİ汾����Ҫ�󡣸���freemarker�汾���ܻ��������⡣
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
    public UIComponent beginAtBlcok(TemplateElement e) throws UIInitException {
        String tagName = e.getDescription().split(" ")[1];
        String componentType = this.register.getMappingComponentByTagName(tagName);
        if (componentType == null)
            return null;
        //A.�����齨
        UIComponent com = this.register.createComponent(componentType);
        //B.װ�����Զ���
        Map<String, Expression> namedArgs = null;
        try {
            Field field = e.getClass().getDeclaredField("namedArgs");
            field.setAccessible(true);
            namedArgs = (Map<String, Expression>) field.get(e);
            for (String key : namedArgs.keySet()) {
                Expression exp = namedArgs.get(key);
                if (exp == null)
                    continue;
                if (exp.getClass().getSimpleName().equals("StringLiteral") == true) {
                    Field valueField = exp.getClass().getDeclaredField("value");
                    valueField.setAccessible(true);
                    com.setProperty(key, (String) valueField.get(exp));
                } else
                    com.setPropertyEL(key, exp.getSource());
            }
        } catch (Exception e2) {
            throw new UIInitException("Freemarker���ݴ����޷���ȡnamedArgs��value�ֶΡ�����ʹ�ý���ʹ��freemarker 2.3.19�汾��", e2);
        }
        //C.���齨�ͱ�ǩ�����IDֵ�໥�󶨡�
        try {
            if (namedArgs.containsKey("id") == false) {
                Class<?> strV = Thread.currentThread().getContextClassLoader().loadClass("freemarker.core.StringLiteral");
                Constructor<?> cons = strV.getDeclaredConstructor(String.class);
                cons.setAccessible(true);
                Expression idExp = (Expression) cons.newInstance(com.getId());
                namedArgs.put("id", idExp);
            } else {
                Expression idExp = namedArgs.get("id");
                Field valueField = idExp.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                com.setId((String) valueField.get(idExp));
            }
        } catch (Exception e2) {
            throw new UIInitException("Freemarker���ݴ����޷�����StringLiteral���Ͷ��󡣽���ʹ�ý���ʹ��freemarker 2.3.19�汾��", e2);
        }
        return com;
    }
    @Override
    public void endAtBlcok(TemplateElement e) throws UIInitException {}
}