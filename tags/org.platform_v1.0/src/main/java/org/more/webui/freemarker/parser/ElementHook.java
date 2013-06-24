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
import org.more.webui.component.UIComponent;
import org.more.webui.context.ViewContext;
import freemarker.core.TemplateElement;
/**
 * freemarkerģ��Ԫ�ؿ鹳�ӡ�
 * @version : 2012-5-14
 * @author ������ (zyc@byshell.org)
 */
public interface ElementHook {
    /**��ʼ����������ģ���ǩ*/
    public UIComponent beginAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent, ViewContext viewContext) throws ElementHookException;
    /**����������ģ���ǩ����*/
    public void endAtBlcok(TemplateScanner scanner, TemplateElement e, UIComponent parent, ViewContext viewContext) throws ElementHookException;
}