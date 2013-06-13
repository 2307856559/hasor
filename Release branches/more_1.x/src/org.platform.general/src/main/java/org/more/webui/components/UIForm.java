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
package org.more.webui.components;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.more.webui.component.UIComponent;
import org.more.webui.component.support.NoState;
import org.more.webui.component.values.MethodExpression;
import org.more.webui.context.ViewContext;
import org.more.webui.event.Event;
import org.more.webui.event.EventListener;
import org.more.webui.render.form.FormRender;
/**
 * <b>�齨ģ��</b>�����齨��
 * <br><b>������¼�</b>��OnSubmit
 * <br><b>��Ⱦ��</b>��{@link FormRender}
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class UIForm extends UIComponent {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**���ĵݽ���ַ��RW��*/
        submitAction,
        /**Action���������������submitAction����������Ի�ʧЧ����-��*/
        submitEL,
    }
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.submitAction.name(), null);
        this.setPropertyMetaValue(Propertys.submitEL.name(), null);
        this.addEventListener(AjaxForm_Event_OnSubmit.SubmitEvent, new AjaxForm_Event_OnSubmit());
    }
    /**��ȡform EL�ַ���*/
    @NoState
    public String getSubmitEL() {
        return this.getProperty(Propertys.submitEL.name()).valueTo(String.class);
    }
    /**����form EL�ַ���*/
    @NoState
    public void setSubmitEL(String submitEL) {
        this.getProperty(Propertys.submitEL.name()).value(submitEL);
    }
    public MethodExpression getSubmitExpression() {
        String actionString = this.getSubmitEL();
        if (actionString == null || actionString.equals("")) {} else
            return new MethodExpression(actionString);
        return null;
    }
    /**���е�����*/
    public Map<String, Object> getFormData() {
        Map<String, UIInput> comDataMap = this.getFormDataAsCom();
        Map<String, Object> formData = new HashMap<String, Object>();
        for (Entry<String, UIInput> ent : comDataMap.entrySet())
            formData.put(ent.getKey(), ent.getValue().getValue());
        return formData;
    }
    /**���е������齨����*/
    public Map<String, UIInput> getFormDataAsCom() {
        Map<String, UIInput> comDataMap = new HashMap<String, UIInput>();
        for (UIComponent com : this.getChildren())
            if (com instanceof UIInput == true) {
                UIInput input = (UIInput) com;
                if (input.getName() == null || input.getName().equals("") == true) {} else
                    comDataMap.put(input.getName(), input);
            }
        return comDataMap;
    }
}
/**������OnSubmit�¼���EL����*/
class AjaxForm_Event_OnSubmit implements EventListener {
    public static Event SubmitEvent = Event.getEvent("OnSubmit");
    public void onEvent(Event event, UIComponent component, ViewContext viewContext) throws Throwable {
        MethodExpression e = ((UIForm) component).getSubmitExpression();
        if (e != null)
            viewContext.sendObject(e.execute(component, viewContext));
    }
};