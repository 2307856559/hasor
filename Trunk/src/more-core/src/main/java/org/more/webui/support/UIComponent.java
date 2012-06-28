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
package org.more.webui.support;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.more.core.error.MoreDataException;
import org.more.core.event.AbstractEventManager;
import org.more.core.event.Event;
import org.more.core.event.EventListener;
import org.more.core.event.EventManager;
import org.more.core.ognl.OgnlException;
import org.more.webui.context.ViewContext;
import org.more.webui.support.values.AbstractValueHolder;
import org.more.webui.support.values.ExpressionValueHolder;
import org.more.webui.support.values.MethodExpression;
import org.more.webui.support.values.StaticValueHolder;
/**
* ��������ĸ�������ӵ����������йؼ�������
* @version : 2011-8-4
* @author ������ (zyc@byshell.org)
*/
public abstract class UIComponent {
    private String                           componentID         = null;
    //    private String                           clientID            = null;
    private UIComponent                      parent              = null;
    private List<UIComponent>                components          = new ArrayList<UIComponent>();
    /**˽���¼������������¼�ʱ���߲����ܵ��������Ӱ��*/
    private EventManager                     privateEventManager = new AbstractEventManager() {};
    private Map<String, AbstractValueHolder> propertys           = new HashMap<String, AbstractValueHolder>();
    /*-------------------------------------------------------------------------------get/set����*/
    /**���������ID*/
    public String getId() {
        return this.componentID;
    };
    /**��������ID*/
    public void setId(String componentID) {
        this.componentID = componentID;
    };
    public String getClientID(ViewContext viewContext) {
        //        if (clientID == null)
        return "uiCID_" + viewContext.getComClientID(this);
        //        return clientID;
    }
    /**ͨ�����Ա�*/
    public static enum Propertys {
        /**�ͻ���������֮ǰ���еĵ��ã�����falseȡ������ajax����*/
        beforeScript,
        /**�ͻ��˽ű��ص�����*/
        afterScript,
        /**���ô���ص�����*/
        errorScript,
        /**Ajax�Ƿ�ʹ��ͬ������*/
        async,
        /**Action����*/
        actionEL,
        /**��ʾ�Ƿ���Ⱦ*/
        isRender,
        /**��ʾ�Ƿ���Ⱦ���齨*/
        isRenderChildren,
    };
    public String getBeforeScript() {
        return this.getProperty(Propertys.beforeScript.name()).valueTo(String.class);
    }
    public void setBeforeScript(String beforeScript) {
        this.getProperty(Propertys.beforeScript.name()).value(beforeScript);
    }
    public String getAfterScript() {
        return this.getProperty(Propertys.afterScript.name()).valueTo(String.class);
    }
    public void setAfterScript(String afterScript) {
        this.getProperty(Propertys.afterScript.name()).value(afterScript);
    }
    public String getErrorScript() {
        return this.getProperty(Propertys.errorScript.name()).valueTo(String.class);
    }
    public void setErrorScript(String errorScript) {
        this.getProperty(Propertys.errorScript.name()).value(errorScript);
    }
    public boolean isAsync() {
        return this.getProperty(Propertys.async.name()).valueTo(Boolean.TYPE);
    }
    public void setAsync(boolean async) {
        this.getProperty(Propertys.async.name()).value(async);
    }
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    public boolean isRender() {
        return this.getProperty(Propertys.isRender.name()).valueTo(Boolean.TYPE);
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    public void setRender(boolean isRender) {
        this.getProperty(Propertys.isRender.name()).value(isRender);
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ����������齨��*/
    public boolean isRenderChildren() {
        return this.getProperty(Propertys.isRenderChildren.name()).valueTo(Boolean.TYPE);
    }
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ����������齨��*/
    public void setRenderChildren(boolean isRenderChildren) {
        this.getProperty(Propertys.isRenderChildren.name()).value(isRenderChildren);
    }
    /**��ȡAction EL�ַ���*/
    public String getActionEL() {
        return this.getProperty(Propertys.actionEL.name()).valueTo(String.class);
    }
    /**����Action EL�ַ���*/
    public void setActionEL(String action) {
        this.getProperty(Propertys.actionEL.name()).value(action);
    }
    /*-------------------------------------------------------------------------------���ķ���*/
    /**�ڵ�ǰ������Ӽ���Ѱ��ĳ���ض�ID�����*/
    public UIComponent getChildByID(String componentID) {
        if (componentID == null)
            return null;
        if (this.getId().equals(componentID) == true)
            return this;
        for (UIComponent component : this.components) {
            UIComponent com = component.getChildByID(componentID);
            if (com != null)
                return com;
        }
        return null;
    };
    /**��ȡһ��int����ֵ������ǰ����й��ж��ٸ���Ԫ��*/
    public int getChildCount() {
        return this.components.size();
    };
    /**��ȡһ��Ԫ�ؼ��ϣ��ü����Ǵ��������ĳ���*/
    public Iterator<UIComponent> getChildren() {
        return this.components.iterator();
    };
    /**��ȡһ���齨�б���б��а����˸��齨�Լ����齨���������齨��*/
    public List<UIComponent> getALLChildren() {
        ArrayList<UIComponent> list = new ArrayList<UIComponent>();
        list.add(this);
        for (UIComponent uic : components)
            list.addAll(uic.getALLChildren());
        return list;
    };
    /**������齨*/
    public void addChildren(UIComponent componentItem) {
        componentItem.setParent(this);
        this.components.add(componentItem);
    }
    /**��ȡ�齨�ĸ�����*/
    public UIComponent getParent() {
        return this.parent;
    }
    /**�����齨�ĸ�����*/
    private void setParent(UIComponent parent) {
        this.parent = parent;
    }
    /**��ȡ�������Եļ��ϡ�*/
    public Map<String, AbstractValueHolder> getPropertys() {
        return this.propertys;
    };
    /**��ȡ���ڱ�ʾ������Զ���*/
    public AbstractValueHolder getProperty(String propertyName) {
        AbstractValueHolder value = this.getPropertys().get(propertyName);
        if (value == null)
            return new StaticValueHolder();
        return value;
    };
    /**���һ��EL��ʽ���齨�����Բ���readString��writeString�ֱ��Ӧ��ҵ���齨�Ķ�д���ԡ�*/
    public void setPropertyEL(String propertyName, String readString, String writeString) {
        AbstractValueHolder value = this.getPropertys().get(propertyName);
        ExpressionValueHolder elValueHolder = null;
        if (value == null || value instanceof ExpressionValueHolder == false)
            elValueHolder = new ExpressionValueHolder(readString, writeString);
        this.getPropertys().put(propertyName, elValueHolder);
    };
    /**�÷����ὫelString��������ΪreadString�͡�writeString��*/
    public void setPropertyEL(String propertyName, String elString) {
        this.setPropertyEL(propertyName, elString, elString);
    };
    /**�������ڱ�ʾ������Ե��ַ�����*/
    public void setProperty(String propertyName, Object newValue) {
        AbstractValueHolder value = this.getPropertys().get(propertyName);
        if (value == null)
            value = new StaticValueHolder(newValue);
        this.getPropertys().put(propertyName, value);
    };
    /**��map�е�����ȫ����װ����ǰ�齨��*/
    public void setupPropertys(Map<String, Object> objMap) {
        if (objMap != null)
            for (String key : this.propertys.keySet())
                if (objMap.containsKey(key) == true) {
                    AbstractValueHolder vh = this.propertys.get(key);
                    Object newValue = objMap.get(key);
                    vh.value(newValue);
                }
    }
    /*-------------------------------------------------------------------------------��������*/
    /**�������ͨ���÷�����ʼ�������*/
    protected void initUIComponent(ViewContext viewContext) {
        /*��������Ĭ��ֵ����ҳ������ֵ�����õ�ʱ���������õ�Ĭ��ֵ�ͻ�ʧЧ*/
        //        this.clientID = null;
        this.setProperty(Propertys.beforeScript.name(), "true");
        this.setProperty(Propertys.async.name(), true);//Ĭ��ʹ���첽����
        this.setProperty(Propertys.isRender.name(), true);
        this.setProperty(Propertys.isRenderChildren.name(), true);
        this.setProperty(Propertys.actionEL.name(), null);
    };
    /**��1�׶Σ������ʼ���׶Σ��ý׶θ����ʼ�������*/
    public void processInit(ViewContext viewContext) {
        this.initUIComponent(viewContext);
        /*�������ԣ��������Իᱣ֤ÿ�����������ڵ�����ֵ����UI�ж����ԭʼֵ��*/
        for (AbstractValueHolder vh : this.propertys.values())
            vh.reset();
        for (UIComponent com : this.components)
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
        for (UIComponent com : this.components)
            com.processApplyRequest(viewContext);
    };
    /**��4�׶Σ��ý׶������ṩһ����֤���ݵĺϷ��ԡ�*/
    public void processValidate(ViewContext viewContext) {
        for (UIComponent com : this.components)
            com.processValidate(viewContext);
    };
    /**��5�׶Σ������ģ���е���ֵӦ�õ���Bean*/
    public void processUpdate(ViewContext viewContext) throws OgnlException {
        /*��������ע�ᵽpropertys�е�����ֵ*/
        for (String key : this.propertys.keySet()) {
            AbstractValueHolder vh = this.propertys.get(key);
            if (vh.isUpdate() == true)
                vh.updateModule(this, viewContext);
        }
        for (UIComponent com : this.components)
            com.processUpdate(viewContext);
    };
    /**��6�׶Σ�����Action�����Ϳͻ��˻ش����¼�*/
    public void processApplication(ViewContext viewContext) throws OgnlException {
        if (this.getId().equals(viewContext.getTarget()) == true) {
            /*����ͻ�����������*/
            if (viewContext.getEvent() != null)
                /**�¼�����*/
                this.doEvent(viewContext);
            else
                /**Action����*/
                this.doAction(viewContext);//����
        }
        for (UIComponent com : this.components)
            com.processApplication(viewContext);
    };
    /*-------------------------------------------------------------------------------�¼���Ӧ*/
    /**ִ���¼�*/
    protected void doEvent(ViewContext viewContext) {
        Event eventType = Event.getEvent(viewContext.getEvent());
        this.privateEventManager.doEvent(eventType, this, viewContext);
    };
    /**�����¼������¼��ᱻ���͵�˽���¼��������С�*/
    protected void pushEvent(Event eventType, Object... objects) {
        this.privateEventManager.pushEvent(eventType, objects);
    };
    /**֪ͨ�¼��������׳�ĳһ�����͵��¼���*/
    protected void popEvent(Event event) {
        this.privateEventManager.popEvent(event);
    };
    /**���һ�������¼����¼���������*/
    public void addEventListener(Event eventType, EventListener listener) {
        this.privateEventManager.addEventListener(eventType, listener);
    };
    /**��ȡ˽���¼���������*/
    protected EventManager getEventManager() {
        return this.privateEventManager;
    };
    /*-------------------------------------------------------------------------------Action���ô���*/
    /**ִ��action����*/
    protected void doAction(ViewContext viewContext) throws OgnlException {
        MethodExpression me = this.getActionExpression();
        if (me != null)
            me.execute(this, viewContext);
    };
    public MethodExpression getActionExpression() {
        String actionString = this.getActionEL();
        if (actionString == null || actionString.equals("")) {} else
            return new MethodExpression(actionString);
        return null;
    }
    /*-------------------------------------------------------------------------------״̬����*/
    /**��״̬�����лָ�״̬*/
    public void restoreState(Object[] stateData) {
        //1.���ݼ��
        if (stateData == null)
            return;
        if (stateData.length != 2)
            throw new MoreDataException("WebUI�޷��������״̬�����������[" + this.getId() + "]����������ݶ�ʧ");
        //2.�ָ���������
        Map<String, Object> mineState = (Map<String, Object>) stateData[0];
        for (String propName : mineState.keySet())
            /*�����ID֮�����������*/
            if ("id".equals(propName) == false) {
                AbstractValueHolder vh = this.propertys.get(propName);
                if (vh != null)
                    vh.value(mineState.get(propName));
            }
        //3.�ָ������
        Map<String, Object> childrenState = (Map<String, Object>) stateData[1];
        for (UIComponent com : components)
            com.restoreState((Object[]) childrenState.get(com.getId()));
    };
    /**�������������ȡ����*/
    public Object[] saveState() {
        //1.�־û������״̬
        HashMap<String, Object> mineState = new HashMap<String, Object>();
        for (String propName : this.propertys.keySet()) {
            AbstractValueHolder vh = this.propertys.get(propName);
            mineState.put(propName, vh.value());
        }
        //2.�־û��������״̬
        HashMap<String, Object> childrenState = new HashMap<String, Object>();
        for (UIComponent com : components)
            childrenState.put(com.getId(), com.saveState());
        //3.���س־û�״̬
        Object[] thisState = new Object[2];
        thisState[0] = mineState;
        thisState[1] = childrenState;
        return thisState;
    }
};