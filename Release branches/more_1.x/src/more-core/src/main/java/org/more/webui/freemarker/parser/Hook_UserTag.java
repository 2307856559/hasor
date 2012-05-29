package org.more.webui.freemarker.parser;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import org.more.webui.UIInitException;
import org.more.webui.components.UIComponent;
import org.more.webui.context.FacesConfig;
import freemarker.core.Expression;
import freemarker.core.TemplateElement;
/**
 * ������ݱ�ǩԪ�ش����齨���󣬸�����freemarker��ǿ�ҵİ汾����Ҫ�󡣸���freemarker�汾���ܻ��������⡣
 * @version : 2012-5-14
 * @author ������ (zyc@byshell.org)
 */
public class Hook_UserTag implements ElementHook {
    private FacesConfig facesConfig = null; //ע����
    //
    public Hook_UserTag(FacesConfig facesConfig) {
        if (facesConfig == null)
            throw new NullPointerException("param ��FacesConfig�� si null.");
        this.facesConfig = facesConfig;
    }
    @Override
    public UIComponent beginAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent) throws UIInitException {
        //A.�����齨
        String tagName = e.getDescription().split(" ")[1];
        UIComponent componentObject = this.facesConfig.createComponent(tagName);
        if (componentObject == null)
            return null;
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
                    componentObject.setProperty(key, (String) valueField.get(exp));
                } else
                    componentObject.setPropertyEL(key, exp.getSource());
            }
        } catch (Exception e2) {
            throw new UIInitException("Freemarker���ݴ����޷���ȡnamedArgs��value�ֶΡ�����ʹ�ý���ʹ��freemarker 2.3.19�汾��", e2);
        }
        //C.���齨�ͱ�ǩ�����IDֵ�໥�󶨡�
        try {
            String comID = null;
            if (namedArgs.containsKey("id") == false) {
                Class<?> strV = Thread.currentThread().getContextClassLoader().loadClass("freemarker.core.StringLiteral");
                Constructor<?> cons = strV.getDeclaredConstructor(String.class);
                cons.setAccessible(true);
                comID = FacesConfig.generateID(componentObject.getClass());//�����µ�ID
                Expression idExp = (Expression) cons.newInstance(comID);
                namedArgs.put("id", idExp);
            } else {
                Expression idExp = namedArgs.get("id");
                Field valueField = idExp.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                comID = (String) valueField.get(idExp);
            }
            componentObject.setId(comID);
        } catch (Exception e2) {
            throw new UIInitException("Freemarker���ݴ����޷�����StringLiteral���Ͷ��󡣽���ʹ�ý���ʹ��freemarker 2.3.19�汾��", e2);
        }
        return componentObject;
    }
    @Override
    public void endAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent) throws UIInitException {}
}