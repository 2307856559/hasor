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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.core.error.MoreDataException;
import org.more.core.event.AbstractEventManager;
import org.more.core.event.Event;
import org.more.core.event.EventListener;
import org.more.core.event.EventManager;
import org.more.webui.ViewContext;
import org.more.webui._.Register;
import org.more.webui._.ValueHolder;
import org.more.webui.event.ActionEvent;
import org.more.webui.event.InitInvokeEvent;
/**
* ��������ĸ�������ӵ����������йؼ�������
* @version : 2011-8-4
* @author ������ (zyc@byshell.org)
*/
public abstract class UIComponent {
    private List<UIComponent>        components          = null;
    private boolean                  isRender            = true;
    /**˽���¼������������¼�ʱ���߲����ܵ��������Ӱ��*/
    private EventManager             privateEventManager = new AbstractEventManager() {};
    private Map<String, ValueHolder> propertys           = new HashMap<String, ValueHolder>();
    //
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**���ID*/
        id,
    };
    /**��ȡ����������ƣ�ÿ������������п������ڱ�ʾ�����͵�Type����*/
    public abstract String getComponentType();
    /**���������ID*/
    public String getId() {
        String componentID = this.getProperty(Propertys.id.name()).valueTo(String.class);
        if (componentID == null)
            this.setId(Register.generateID(this.getClass()));
        return componentID;
    };
    /**��������ID*/
    public void setId(String componentID) {
        this.getProperty(Propertys.id.name()).value(componentID);
    };
    /**�ڵ�ǰ������Ӽ���Ѱ��ĳ���ض�ID�����*/
    public UIComponent getChildByID(String componentID) {
        if (this.getId().equals(componentID) == true)
            return this;
        for (UIComponent component : this.getChildren()) {
            UIComponent com = component.getChildByID(componentID);
            if (com != null)
                return com;
        }
        return null;
    };
    /**��ȡһ��int����ֵ������ǰ����й��ж��ٸ���Ԫ��*/
    public int getChildCount() {
        return this.getChildren().size();
    };
    /**��ȡһ��Ԫ�ؼ��ϣ��ü����Ǵ��������ĳ���*/
    public List<UIComponent> getChildren() {
        if (this.components == null)
            this.components = new ArrayList<UIComponent>();
        return this.components;
    };
    /**�������ͨ���÷�����ʼ�������*/
    protected void initUIComponent(ViewContext viewContext) {};
    /**��ȡ�������Եļ��ϡ�*/
    public Map<String, ValueHolder> getPropertys() {
        return this.propertys;
    };
    /**�������ڱ�ʾ������Ե��ַ�����propertyText�������ý�����ֵ��Ϊ���֣�EL���͡�${...}�����ַ������ݡ�*/
    public void setPropertyText(String propertyName, String propertyText) {
        //TODO ��ͬ���͵��������ô���
        ValueHolder value = this.getProperty(propertyName);
        if (value != null && value.equalsText(propertyText) == true)
            return;
        /*����һ���µ����Զ���������Լ���*/
        value = new ValueHolder(propertyText);
        this.getPropertys().put(propertyName, value);
    };
    /**��ȡ���ڱ�ʾ������Ե��ַ�����*/
    public String getPropertyText(String propertyName, String propertyText) {
        ValueHolder value = this.getProperty(propertyName);
        if (value != null)
            return value.getPropertyText();
        return null;
    };
    /**��ȡ���ڱ�ʾ������Զ���*/
    public ValueHolder getProperty(String propertyName) {
        return this.getPropertys().get(propertyName);
    };
    /**����ֵ���õ������С�*/
    public void setProperty(String propertyName, Object newValue) {
        ValueHolder value = this.getProperty(propertyName);
        if (value != null)
            value.value(newValue);
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    public boolean isRender() {
        return this.isRender;
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    public void setRender(boolean isRender) {
        this.isRender = isRender;
    };
    /*-------------------------------------------------------------------------------*/
    /**��1�׶Σ������ʼ���׶Σ��ý׶θ����ʼ�������*/
    public void processInit(ViewContext viewContext) {
        /*�������г�ʼ�������¼�*/
        this.privateEventManager.popEvent(Event.getEvent(InitInvokeEvent.class));
        this.initUIComponent(viewContext);
        for (UIComponent com : this.getChildren())
            com.processInit(viewContext);
    };
    /**��3�׶Σ��������������������һ�µ����Թ��������ϡ�*/
    public void processApplyRequest(ViewContext viewContext) {
        /*�����������Ҫ����������ֵ���뵽������*/
        for (String key : this.propertys.keySet()) {
            /*�����������������������б����ǡ�componentID:attName��*/
            String newValue = viewContext.getHttpRequest().getParameter(this.getId() + ":" + key);
            if (newValue != null)
                this.propertys.get(key).value(newValue);
        }
        for (UIComponent com : this.getChildren())
            com.processApplyRequest(viewContext);
    };
    /**��4�׶Σ��ý׶������ṩһ����֤���ݵĺϷ��ԡ�*/
    public void processValidate(ViewContext viewContext) {
        for (UIComponent com : this.getChildren())
            com.processValidate(viewContext);
    };
    /**��5�׶Σ������ģ���е���ֵӦ�õ���Bean*/
    public void processUpdate(ViewContext viewContext) {
        /*��������ע�ᵽpropertys�е�����ֵ*/
        for (String key : this.propertys.keySet()) {
            ValueHolder vh = this.propertys.get(key);
            vh.updateModule();
        }
        for (UIComponent com : this.getChildren())
            com.processUpdate(viewContext);
    };
    /**��6�׶Σ�����Action�����Ϳͻ��˻ش�����Ϣ*/
    public void processApplication(ViewContext viewContext) {
        /*�������ж����¼�*/
        this.privateEventManager.popEvent(Event.getEvent(ActionEvent.class));
        for (UIComponent com : this.getChildren())
            com.processApplication(viewContext);
    };
    /*-------------------------------------------------------------------------------*/
    /**�����¼������¼��ᱻ���͵�˽���¼��������С�*/
    protected void pushEvent(Event eventType, Object... objects) {
        this.privateEventManager.pushEvent(eventType, objects);
    };
    /**֪ͨ�¼��������׳�ĳһ�����͵��¼���*/
    protected void popEvent(Event event) {
        this.privateEventManager.popEvent(event);
    };
    /**���һ�������¼����¼���������*/
    public void addEventListener(Event eventType, EventListener<?> listener) {
        this.privateEventManager.addEventListener(eventType, listener);
    };
    /**��ȡ˽���¼���������*/
    protected EventManager getEventManager() {
        return this.privateEventManager;
    };
    /*-------------------------------------------------------------------------------*/
    /**��״̬�����лָ�״̬*/
    public void restoreState(Object[] stateData) {
        //1.���ݼ��
        if (stateData == null)
            return;
        if (stateData.length != 2)
            throw new MoreDataException("WebUI�޷��������״̬�����������[" + this.getId() + "]����������ݶ�ʧ");
        //2.�ָ���������
        Map<String, Object> mineState = (Map<String, Object>) stateData[0];
        for (String propName : mineState.keySet()) {
            ValueHolder vh = this.propertys.get(propName);
            vh.value(mineState.get(propName));
        }
        //3.�ָ������
        Map<String, Object> childrenState = (Map<String, Object>) stateData[1];
        List<UIComponent> uic = getChildren();
        for (UIComponent com : uic)
            com.restoreState((Object[]) childrenState.get(com.getId()));
    };
    /**�������������ȡ����*/
    public Object[] saveState() {
        //1.�־û������״̬
        HashMap<String, Object> mineState = new HashMap<String, Object>();
        for (String propName : this.propertys.keySet()) {
            ValueHolder vh = this.propertys.get(propName);
            mineState.put(propName, vh.value());
        }
        //2.�־û��������״̬
        HashMap<String, Object> childrenState = new HashMap<String, Object>();
        List<UIComponent> uic = getChildren();
        for (UIComponent com : uic)
            childrenState.put(com.getId(), com.saveState());
        //3.���س־û�״̬
        Object[] thisState = new Object[2];
        thisState[0] = mineState;
        thisState[1] = childrenState;
        return thisState;
    }
};