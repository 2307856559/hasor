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
import org.more.webui.UIInitException;
import org.more.webui.context.FacesContext;
import org.more.webui.support.UIComponent;
import freemarker.core.TemplateElement;
import freemarker.template.Template;
/**
 * ������ݱ�ǩԪ�ش����齨���󣬸�����freemarker��ǿ�ҵİ汾����Ҫ�󡣸���freemarker�汾���ܻ��������⡣
 * @version : 2012-5-14
 * @author ������ (zyc@byshell.org)
 */
public class Hook_Include implements ElementHook {
    @Override
    public UIComponent beginAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent, FacesContext uiContext) throws UIInitException {
        try {
            String includeName = e.getDescription().split(" ")[1];
            includeName = includeName.substring(1, includeName.length() - 1);
            Template includeTemp = e.getTemplate().getConfiguration().getTemplate(includeName);
            scanner.parser(includeTemp, parent, uiContext);
            return null;
        } catch (Exception e2) {
            throw new UIInitException("�����쳣������include��������" + e.getDescription() + "��", e2);
        }
    }
    @Override
    public void endAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent, FacesContext uiContext) throws UIInitException {}
}