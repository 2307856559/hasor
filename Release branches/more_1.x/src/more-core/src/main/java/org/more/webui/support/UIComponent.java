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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.core.error.MoreDataException;
import org.more.core.event.AbstractEventManager;
import org.more.core.event.Event;
import org.more.core.event.EventListener;
import org.more.core.event.EventManager;
import org.more.core.ognl.OgnlException;
import org.more.util.BeanUtil;
import org.more.webui.context.ViewContext;
import org.more.webui.support.values.AbstractValueHolder;
import org.more.webui.support.values.ExpressionValueHolder;
import org.more.webui.support.values.StaticValueHolder;
/**
* ��������ĸ�������ӵ����������йؼ�������
* @version : 2011-8-4
* @author ������ (zyc@byshell.org)
*/
public abstract class UIComponent {
    private String                           componentID         = null;
    private String                           componentPath       = null;
    private UIComponent                      parent              = null;
    private List<UIComponent>                components          = new ArrayList<UIComponent>();
    /**˽���¼������������¼�ʱ���߲����ܵ��������Ӱ��*/
    private EventManager                     privateEventManager = new AbstractEventManager() {};
    private Map<String, AbstractValueHolder> propertys           = new HashMap<String, AbstractValueHolder>();
    private Map<String, Object>              atts                = new HashMap<String, Object>();
    /*-------------------------------------------------------------------------------get/set����*/
    /**���������ID*/
    public String getComponentID() {
        return componentID;
    }
    /**��������ID*/
    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }
    /**ͨ�����Ա�*/
    public static enum Propertys {
        /**�ͻ���������֮ǰ���еĵ��ã�����falseȡ������ajax����R��*/
        beforeScript,
        /**�ͻ��˽ű��ص�������R��*/
        afterScript,
        /**���ô���ص�������R��*/
        errorScript,
        /**Ajax�Ƿ�ʹ��ͬ��������R��*/
        async,
        /**��ʾ�Ƿ���Ⱦ��-��*/
        render,
        /**��ʾ�Ƿ���Ⱦ���齨��-��*/
        renderChildren,
    };
    public String getBeforeScript() {
        return this.getProperty(Propertys.beforeScript.name()).valueTo(String.class);
    }
    @NoState
    public void setBeforeScript(String beforeScript) {
        this.getProperty(Propertys.beforeScript.name()).value(beforeScript);
    }
    public String getAfterScript() {
        return this.getProperty(Propertys.afterScript.name()).valueTo(String.class);
    }
    @NoState
    public void setAfterScript(String afterScript) {
        this.getProperty(Propertys.afterScript.name()).value(afterScript);
    }
    public String getErrorScript() {
        return this.getProperty(Propertys.errorScript.name()).valueTo(String.class);
    }
    @NoState
    public void setErrorScript(String errorScript) {
        this.getProperty(Propertys.errorScript.name()).value(errorScript);
    }
    public boolean isAsync() {
        return this.getProperty(Propertys.async.name()).valueTo(Boolean.TYPE);
    }
    @NoState
    public void setAsync(boolean async) {
        this.getProperty(Propertys.async.name()).value(async);
    }
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    @NoState
    public boolean isRender() {
        return this.getProperty(Propertys.render.name()).valueTo(Boolean.TYPE);
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ�����*/
    @NoState
    public void setRender(boolean isRender) {
        this.getProperty(Propertys.render.name()).value(isRender);
    };
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ����������齨��*/
    @NoState
    public boolean isRenderChildren() {
        return this.getProperty(Propertys.renderChildren.name()).valueTo(Boolean.TYPE);
    }
    /**����һ��booleanֵ����ֵ�����Ƿ���Ⱦ����������齨��*/
    @NoState
    public void setRenderChildren(boolean isRenderChildren) {
        this.getProperty(Propertys.renderChildren.name()).value(isRenderChildren);
    }
    /*-------------------------------------------------------------------------------���ķ���*/
    /**��ȡ���ڸ��ӵ����Ե�Map����*/
    public Map<String, Object> getAtts() {
        return atts;
    };
    /**��ȡ�齨����*/
    public abstract String getComponentType();
    /**��ȡ�齨���齨���е�λ�ø�ʽΪ��/1/3/4/2*/
    public String getComponentPath() {
        if (this.componentPath == null) {
            StringBuffer buffer = new StringBuffer();
            UIComponent target = this;
            UIComponent targetParent = target.getParent();
            while (targetParent != null) {
                int index = targetParent.getChildren().indexOf(target);
                buffer.append('/');
                buffer.append(new StringBuffer(String.valueOf(index)).reverse());
                //
                target = targetParent;
                targetParent = target.getParent();
            }
            this.componentPath = buffer.reverse().toString();
        }
        return this.componentPath;
    }
    /**��ȡһ�����õĿͻ���ID*/
    public String getClientID(ViewContext viewContext) {
        if (this.getComponentID() != null)
            return getComponentID();
        else
            return "uiCID_" + viewContext.getComClientID(this);
    }
    public UIComponent getChildByPath(String componentPath) {
        if (componentPath == null || componentPath.equals("") == true)
            return null;
        if (componentPath.startsWith(this.getComponentPath()) == false)
            return null;//�ж�Ҫ��ȡ��Ŀ�겻���Լ��ĺ���
        String targetPath = componentPath.substring(this.getComponentPath().length());
        //
        int firstSpan = targetPath.indexOf('/');
        int index = Integer.parseInt(targetPath.substring(0, firstSpan));
        return this.getChildren().get(index);
    }
    /**�ڵ�ǰ������Ӽ���Ѱ��ĳ���ض�ID�����*/
    public UIComponent getChildByID(String componentID) {
        if (componentID == null)
            return null;
        if (this.getComponentID().equals(componentID) == true)
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
    public List<UIComponent> getChildren() {
        return Collections.unmodifiableList(this.components);
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
    };
    /**��ȡ�齨�ĸ�����*/
    public UIComponent getParent() {
        return this.parent;
    };
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
    };
    /*-------------------------------------------------------------------------------��������*/
    /**�������ͨ���÷�����ʼ�������*/
    protected void initUIComponent(ViewContext viewContext) {
        /*��������Ĭ��ֵ����ҳ������ֵ�����õ�ʱ���������õ�Ĭ��ֵ�ͻ�ʧЧ*/
        this.setProperty(Propertys.beforeScript.name(), "true");
        this.setProperty(Propertys.async.name(), true);//Ĭ��ʹ���첽�����¼�
        this.setProperty(Propertys.render.name(), true);
        this.setProperty(Propertys.renderChildren.name(), true);
    };
    /**�齨����ʼ�����*/
    private Boolean doInit = false;
    /**��1�׶Σ������ʼ���׶Σ��ý׶θ����ʼ�������*/
    public final void processInit(ViewContext viewContext) {
        if (this.doInit == false) {
            this.initUIComponent(viewContext);
            this.doInit = true;
        }
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
            String[] newValues = viewContext.getHttpRequest().getParameterValues(this.getComponentPath() + ":" + key);
            if (newValues == null)
                continue;
            else if (newValues.length == 1)
                this.propertys.get(key).value(newValues[0]);
            else
                this.propertys.get(key).value(newValues);
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
        if (this.getComponentPath().equals(viewContext.getTargetPath()) == true) {
            /*����ͻ�����������*/
            Event eventType = Event.getEvent(viewContext.getEvent());
            if (eventType != null)
                /**�¼�����*/
                this.doEvent(eventType, viewContext);
        }
        for (UIComponent com : this.components)
            com.processApplication(viewContext);
    };
    /*-------------------------------------------------------------------------------�¼���Ӧ*/
    /**ִ���¼�*/
    protected void doEvent(Event eventType, ViewContext viewContext) {
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
    /*-------------------------------------------------------------------------------״̬����*/
    /**��״̬�����лָ��齨״̬*/
    public void restoreState(List<?> stateData) {
        //1.���ݼ��
        if (stateData == null)
            return;
        if (stateData.size() == 0)
            throw new MoreDataException("WebUI�޷��������״̬�����������[" + this.getComponentID() + "]����������ݶ�ʧ");
        //2.�ָ���������
        Map<String, Object> mineState = (Map<String, Object>) stateData.get(0);
        for (String propName : mineState.keySet()) {
            /*�ų�����*/
            if (propName == null)
                continue;
            /*ID���Բ�����*/
            if (propName.toLowerCase().equals("id") == true)
                continue;
            /*����ע��*/
            Method rm = BeanUtil.getWriteMethod(propName, this.getClass());
            if (rm == null)
                continue;
            if (rm.getAnnotation(NoState.class) != null)
                continue;
            /*д������*/
            AbstractValueHolder vh = this.propertys.get(propName);
            if (vh != null)
                vh.value(mineState.get(propName));
        }
        //3.�ָ������
        if (stateData.size() == 2) {
            Map<String, Object> childrenState = (Map<String, Object>) stateData.get(1);
            for (UIComponent com : components)
                com.restoreState((List<?>) childrenState.get(com.getComponentPath()));
        }
    };
    /**�����齨�ĵ�ǰ״̬�����������齨��*/
    public List<Object> saveStateOnlyMe() {
        //1.�־û������״̬
        HashMap<String, Object> mineState = new HashMap<String, Object>();
        for (String propName : this.propertys.keySet()) {
            Method rm = BeanUtil.getReadMethod(propName, this.getClass());
            if (rm == null)
                continue;
            if (rm.getAnnotation(NoState.class) != null)
                continue;
            AbstractValueHolder vh = this.propertys.get(propName);
            mineState.put(propName, vh.value());
        }
        //3.���س־û�״̬
        ArrayList<Object> array = new ArrayList<Object>();
        array.add(mineState);
        return array;
    };
    /**�����齨�ĵ�ǰ״̬���������齨��*/
    public List<Object> saveState() {
        //1.�־û������״̬
        List<Object> array = this.saveStateOnlyMe();
        //2.�־û��������״̬
        HashMap<String, Object> childrenState = new HashMap<String, Object>();
        for (UIComponent com : components)
            childrenState.put(com.getComponentPath(), com.saveState());
        //3.���س־û�״̬
        array.add(childrenState);
        return array;
    };
};