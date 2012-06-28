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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.more.webui.UIInitException;
import org.more.webui.context.FacesContext;
import org.more.webui.support.UIComponent;
import org.more.webui.support.UIViewRoot;
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
    public UIComponent parser(Template template, UIComponent uiViewRoot, FacesContext uiContext) throws UIInitException {
        TemplateElement rootNode = template.getRootTreeNode();
        return parserElement(rootNode, uiViewRoot, uiContext);
    }
    /**elementҪ������Ԫ�أ�componentParent��ǰ�������*/
    private UIComponent parserElement(TemplateElement element, UIComponent componentParent, FacesContext uiContext) throws UIInitException {
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
                componentItem = hook.beginAtBlcok(this, e, componentParent, uiContext);//�ڽ���Ԫ��ʱ���������һ��UIComponent�����UIComponent���뵽componentParent
            if (componentItem != null)
                componentParent.addChildren(componentItem);
            this.parserElement(e, (componentItem != null) ? componentItem : componentParent, uiContext);//�ݹ����
            if (hook != null)
                hook.endAtBlcok(this, e, componentParent, uiContext);
        }
        return componentParent;
    }
}