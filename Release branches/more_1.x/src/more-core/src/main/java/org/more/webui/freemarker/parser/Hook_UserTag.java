/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.more.webui.freemarker.parser;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.more.webui.component.UIComponent;
import org.more.webui.context.ViewContext;
import freemarker.core.Expression;
import freemarker.core.TemplateElement;
/**
 * ������ݱ�ǩԪ�ش����齨���󣬸�����freemarker��ǿ�ҵİ汾����Ҫ�󡣸���freemarker�汾���ܻ��������⡣
 * @version : 2012-5-14
 * @author ������ (zyc@byshell.org)
 */
public class Hook_UserTag implements ElementHook {
    public static String Name = "UnifiedCall";
    public UIComponent beginAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent, ViewContext viewContext) throws ElementHookException {
        //A.�����齨
        String tagName = e.getDescription().split(" ")[1];
        UIComponent componentObject = viewContext.getUIContext().getComponent(tagName);
        if (componentObject == null)
            return null;
        componentObject.getAtts().put(Name, e);//������Դ��TemplateElement
        //B.װ�����Զ���
        Map<String, Expression> namedArgs = null;
        try {
            Field field = e.getClass().getDeclaredField("namedArgs");
            field.setAccessible(true);
            namedArgs = (Map<String, Expression>) field.get(e);
            if (namedArgs != null)
                for (String key : namedArgs.keySet()) {
                    Expression exp = namedArgs.get(key);
                    if (exp == null)
                        continue;
                    if (exp.getClass().getSimpleName().equals("StringLiteral") == true) {
                        Field valueField = exp.getClass().getDeclaredField("value");
                        valueField.setAccessible(true);
                        componentObject.setPropertyMetaValue(key, (String) valueField.get(exp));
                    } else
                        componentObject.setPropertyEL(key, exp.getSource());
                }
            else
                namedArgs = new HashMap<String, Expression>();
        } catch (Exception e2) {
            throw new ElementHookException("Freemarker���ݴ����޷���ȡnamedArgs��value�ֶΡ�����ʹ�ý���ʹ��freemarker 2.3.19�汾��", e2);
        }
        //C.���齨�ͱ�ǩ�����IDֵ�໥�󶨡�
        try {
            if (namedArgs.containsKey("id") == true) {
                Expression idExp = namedArgs.get("id");
                Field valueField = idExp.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                String comID = (String) valueField.get(idExp);
                componentObject.setComponentID(comID);
            } else
                componentObject.setComponentID(ViewContext.getCurrentViewContext().newClientID());
        } catch (Exception e2) {
            throw new ElementHookException("Freemarker���ݴ����޷�����StringLiteral���Ͷ��󡣽���ʹ�ý���ʹ��freemarker 2.3.19�汾��", e2);
        }
        return componentObject;
    }
    public void endAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent, ViewContext viewContext) throws ElementHookException {}
}