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
package org.more.webui.component;
import java.util.List;
import org.more.webui.component.values.MethodExpression;
import org.more.webui.context.ViewContext;
import org.more.webui.event.Event;
import org.more.webui.event.EventListener;
import org.more.webui.web.PostFormEnum;
/**
 * <b>�齨ģ��</b>����������ĸ���ͬʱҲ���𱣴�������ͼ���������齨��ʹ��@UIComע��ע�ᡣ
 * <br><b>������¼�</b>��OnInvoke
 * <br><b>��Ⱦ��</b>����
 * @version : 2012-3-29
 * @author ������ (zyc@byshell.org)
 */
public class UIViewRoot extends UIComponent {
    public UIViewRoot() {
        this.setComponentID("com_root");
    }
    @Override
    public String getComponentType() {
        return "ui_ViewRoot";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        this.addEventListener(UIViewRoot_Event_OnAction.ActionEvent, new UIViewRoot_Event_OnAction());
        super.initUIComponent(viewContext);
    }
    public void restoreState(String componentPath, List<?> stateData) {
        UIComponent com = this.getChildByPath(componentPath);
        com.restoreState(stateData);
    }
    public List<?> saveState(String componentPath) {
        UIComponent com = this.getChildByPath(componentPath);
        return com.saveState();
    }
}
/**������OnInvoke�¼���EL����*/
class UIViewRoot_Event_OnAction implements EventListener {
    public static Event ActionEvent = Event.getEvent("OnInvoke");
    @Override
    public void onEvent(Event event, UIComponent component, ViewContext viewContext) throws Throwable {
        String invokeString = viewContext.getHttpRequest().getParameter(PostFormEnum.PostForm_InvokeStringKey.value());
        if (invokeString == null || invokeString.equals("") == true)
            return;
        MethodExpression e = new MethodExpression(invokeString);
        if (e != null)
            viewContext.sendObject(e.execute(component, viewContext));
    }
};