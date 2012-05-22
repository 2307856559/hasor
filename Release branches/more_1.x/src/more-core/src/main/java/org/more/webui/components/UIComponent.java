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
import org.more.core.ognl.OgnlException;
import org.more.webui.context.ViewContext;
import org.more.webui.event.ActionEvent;
import org.more.webui.event.InitInvokeEvent;
/**
* ��������ĸ�������ӵ����������йؼ�������
* @version : 2011-8-4
* @author ������ (zyc@byshell.org)
*/
public abstract class UIComponent {
    private String                   componentID         = null;
    private List<UIComponent>        components          = null;
    private boolean                  isRender            = true;
    private boolean                  isRenderChildren    = true;
    /**˽���¼������������¼�ʱ���߲����ܵ��������Ӱ��*/
    private EventManager             privateEventManager = new AbstractEventManager() {};
    private Map<String, ValueHolder> propertys           = new HashMap<String, ValueHolder>();
    private Map<String, UIParamter>  params              = new HashMap<String, UIParamter>();
    //
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**���ID*/
        id,
    };
    /**��ȡ����ı�ǩ���ơ�*/
    public abstract String getTagName();
    /**���������ID*/
    public String getId() {
        return this.componentID;
    };
    /**��������ID*/
    public void setId(String componentID) {
        this.componentID = componentID;
    };
    /**��ȡһ���������*/
    public UIParamter getParamter(String name) {
        return this.params.get(name);
    };
    /**��ȡ����Ĳ���Map*/
    public Map<String, UIParamter> getParamters() {
        return this.params;
    };
    /**���һ��������������������ظ��µĻ��滻�ɵġ�*/
    public void addParamter(UIParamter uip) {
        if (uip != null)
            this.params.put(uip.getName(), uip);
    };
    /**���һ��������������������ظ��µĻ��滻�ɵġ�*/
    public void addParamter(String key, Object value) {
        UIParamter uip = new UIParamter();
        uip.setName(key);
        uip.setValue(value);
        this.addParamter(uip);
    };
    /**�ڵ�ǰ������Ӽ���Ѱ��ĳ���ض�ID�����*/
    public UIComponent getChildByID(String componentID) {
        if (componentID == null)
            return null;
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
    /**��ȡһ���齨�б���б��а����˸��齨�Լ����齨���������齨��*/
    public List<UIComponent> getALLChildren() {
        ArrayList<UIComponent> list = new ArrayList<UIComponent>();
        list.add(this);
        for (UIComponent uic : getChildren())
            list.addAll(uic.getALLChildren());
        return list;
    };
    /**�������ͨ���÷�����ʼ�������*/
    protected void initUIComponent(ViewContext viewContext) {};
    /**��ȡ�������Եļ��ϡ�*/
    public Map<String, ValueHolder> getPropertys() {
        return this.propertys;
    };
    /**��ȡ���ڱ�ʾ������Զ���*/
    public ValueHolder getProperty(String propertyName) {
        return this.getPropertys().get(propertyName);
    };
    /**�������ڱ�ʾ������Ե��ַ�����propertyText�������ý�����ֵ��Ϊ���֣�EL���͡�${...}�����ַ������ݡ�*/
    public void setPropertyEL(String propertyName, String elString) {
        ValueHolder value = this.getProperty(propertyName);
        ExpressionValueHolder elValueHolder = null;
        if (value == null || value instanceof ExpressionValueHolder == false)
            elValueHolder = new ExpressionValueHolder(elString);
        this.getPropertys().put(propertyName, elValueHolder);
    };
    /**�������ڱ�ʾ������Ե��ַ�����*/
    public void setProperty(String propertyName, String newValue) {
        ValueHolder value = this.getProperty(propertyName);
        if (value == null)
            value = new StaticValueHolder(newValue);
        this.getPropertys().put(propertyName, value);
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    public boolean isRender() {
        return this.isRender;
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    public void setRender(boolean isRender) {
        this.isRender = isRender;
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ����������齨��*/
    public boolean isRenderChildren() {
        return this.isRenderChildren;
    }
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ����������齨��*/
    public void setRenderChildren(boolean isRenderChildren) {
        this.isRenderChildren = isRenderChildren;
    }
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
    public void processUpdate(ViewContext viewContext) throws OgnlException {
        /*��������ע�ᵽpropertys�е�����ֵ*/
        for (String key : this.propertys.keySet()) {
            ValueHolder vh = this.propertys.get(key);
            vh.updateModule(this, viewContext);
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