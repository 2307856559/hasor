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
package org.more.webui.tag;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.more.core.error.LostException;
import org.more.webui.context.ViewContext;
import org.more.webui.render.Render;
import org.more.webui.render.RenderKit;
import org.more.webui.support.UICom;
import org.more.webui.support.UIComponent;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;
/**
 * ͨ�ñ�ǩ����
 * @version : 2012-5-13
 * @author ������ (zyc@byshell.org)
 */
public class TagObject implements TemplateDirectiveModel {
    public final void execute(Environment arg0, Map arg1, TemplateModel[] arg2, TemplateDirectiveBody arg3) throws TemplateException, IOException {
        //0.�������
        HashMap<String, Object> objMap = new HashMap<String, Object>();
        if (arg1 != null)
            for (Object key : arg1.keySet()) {
                TemplateModel item = (TemplateModel) arg1.get(key);
                objMap.put(key.toString(), DeepUnwrap.permissiveUnwrap(item));
            }
        this.exec(arg0, objMap, arg2, arg3);
    }
    public void exec(Environment arg0, Map<String, Object> objMap, TemplateModel[] arg2, TemplateDirectiveBody arg3) throws TemplateException, IOException {
        //A.��ȡ�齨
        ViewContext viewContext = ViewContext.getCurrentViewContext();
        UIComponent component = null;
        try {
            String componentID = objMap.get("id").toString();
            component = viewContext.getViewRoot().getChildByID(componentID);
            component.setupPropertys(objMap);
        } catch (Exception e) {
            throw new TemplateException("�޷���λ�齨ID.", e, arg0);
        }
        //B.�ж�ʱ����Ҫִ����Ⱦ
        if (component.isRender() == false)
            return;
        //C.��ȡ��Ⱦ��
        UICom uicom = component.getClass().getAnnotation(UICom.class);
        if (uicom == null)
            throw new LostException("�齨��" + component.getId() + "���������޷���λ������Ⱦ����");
        RenderKit kit = viewContext.getUIContext().getRenderKit();
        Render renderer = kit.getRender(uicom.tagName());
        //D.������Ⱦ
        TemplateBody body = new TemplateBody(arg3, arg0);
        Writer writer = arg0.getOut();
        renderer.beginRender(viewContext, component, body, writer);
        if (component.isRenderChildren() == true && arg3 != null)
            renderer.render(viewContext, component, body, writer);
        renderer.endRender(viewContext, component, body, writer);
    }
}