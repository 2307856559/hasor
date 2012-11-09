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
package org.more.webui.render.support;
import java.io.IOException;
import java.io.Writer;
import org.more.webui.component.UIComponent;
import org.more.webui.context.ViewContext;
import org.more.webui.tag.TemplateBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
/**
 * �齨��Ⱦ��
 * @version : 2012-5-18
 * @author ������ (zyc@byshell.org)
 */
public interface Render<T extends UIComponent> {
    /**��ʼ��Ⱦ�齨*/
    public void beginRender(ViewContext viewContext, T component, TemplateBody arg3, Writer writer) throws IOException, TemplateModelException;
    /**������Ⱦ*/
    public void render(ViewContext viewContext, T component, TemplateBody arg3, Writer writer) throws IOException, TemplateException;
    /**�齨��Ⱦ����*/
    public void endRender(ViewContext viewContext, T component, TemplateBody arg3, Writer writer) throws IOException, TemplateModelException;
}