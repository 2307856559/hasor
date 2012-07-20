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
import org.more.core.event.Event;
import org.more.core.event.Event.Sequence;
import org.more.core.event.EventListener;
import org.more.webui.context.ViewContext;
import org.more.webui.support.values.MethodExpression;
/**
 * �������빦�ܵ��齨
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class UIInput extends UIComponent {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**������RW��*/
        name,
        /**��ֵ��RW��*/
        value,
        /**��֤,������ʽ��RW��*/
        verification,
        /**�������¼�OnChangeʱ��RW��*/
        onChangeEL,
        /**�������¼�OnLoadDataʱ��RW��*/
        onLoadDataEL,
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.getEventManager().addEventListener(Event.getEvent("OnChange"), new Event_OnChange());
        this.getEventManager().addEventListener(Event.getEvent("OnLoadData"), new Event_OnLoadData());
        this.setProperty(Propertys.value.name(), null);
    }
    /*-------------------------------------------------------------------------------*/
    /**��ȡ�齨����*/
    public String getName() {
        return this.getProperty(Propertys.name.name()).valueTo(String.class);
    }
    /**�����齨����*/
    public void setName(String name) {
        this.getProperty(Propertys.name.name()).value(name);
    }
    /**��ȡ�齨��ֵ*/
    public Object getValue() {
        return this.getProperty(Propertys.value.name()).valueTo(Object.class);
    }
    /**�����齨��ֵ*/
    public void setValue(String value) {
        this.getProperty(Propertys.value.name()).value(value);
    }
    /**���齨ֵ�����ı�֮���ajax���øñ��ʽ��������ã�*/
    public String getOnChangeEL() {
        return this.getProperty(Propertys.onChangeEL.name()).valueTo(String.class);
    }
    /**����һ��EL���ʽ�ñ��ʽ�ᵱ�齨ֵ�����ı�֮����ã�������ã�*/
    public void setOnChangeEL(String onChangeEL) {
        this.getProperty(Propertys.onChangeEL.name()).value(onChangeEL);
    }
    /**����ͼװ������ʱEL���ñ��ʽ��������ã�*/
    public String getOnLoadDataEL() {
        return this.getProperty(Propertys.onLoadDataEL.name()).valueTo(String.class);
    }
    /**����ͼװ������ʱEL���ñ��ʽ��������ã�*/
    public void setOnLoadDataEL(String onLoadDataEL) {
        this.getProperty(Propertys.onLoadDataEL.name()).value(onLoadDataEL);
    }
    /**��֤,������ʽ��������ã�*/
    public String getVerification() {
        return this.getProperty(Propertys.verification.name()).valueTo(String.class);
    }
    /**��֤,������ʽ��������ã�*/
    public void setVerification(String verification) {
        this.getProperty(Propertys.verification.name()).value(verification);
    }
    /*-------------------------------------------------------------------------------*/
    private MethodExpression onChangeExp = null;
    public MethodExpression getOnChangeExpression() {
        if (this.onChangeExp == null) {
            String onChangeExpString = this.getOnChangeEL();
            if (onChangeExpString == null || onChangeExpString.equals("")) {} else
                this.onChangeExp = new MethodExpression(onChangeExpString);
        }
        return this.onChangeExp;
    }
    private MethodExpression onLoadDataExp = null;
    public MethodExpression getOnLoadDataExpression() {
        if (this.onLoadDataExp == null) {
            String onLoadDataExpString = this.getOnLoadDataEL();
            if (onLoadDataExpString == null || onLoadDataExpString.equals("")) {} else
                this.onLoadDataExp = new MethodExpression(onLoadDataExpString);
        }
        return this.onLoadDataExp;
    }
}
/**������OnChange�¼���EL����*/
class Event_OnChange implements EventListener {
    @Override
    public void onEvent(Event event, Sequence sequence) throws Throwable {
        UIInput component = (UIInput) sequence.getParams()[0];
        ViewContext viewContext = (ViewContext) sequence.getParams()[1];
        MethodExpression e = component.getOnChangeExpression();
        if (e != null)
            viewContext.sendAjaxData(e.execute(component, viewContext));
    }
}
/**������OnLoadData�¼���EL����*/
class Event_OnLoadData implements EventListener {
    @Override
    public void onEvent(Event event, Sequence sequence) throws Throwable {
        UIInput component = (UIInput) sequence.getParams()[0];
        ViewContext viewContext = (ViewContext) sequence.getParams()[1];
        MethodExpression e = component.getOnLoadDataExpression();
        if (e != null)
            viewContext.sendAjaxData(e.execute(component, viewContext));
    }
}