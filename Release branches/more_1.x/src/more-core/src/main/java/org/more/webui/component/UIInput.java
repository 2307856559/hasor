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
import org.more.webui.component.values.MethodExpression;
import org.more.webui.context.ViewContext;
import org.more.webui.event.Event;
import org.more.webui.event.EventListener;
/**
 * ���ڱ�����������������ܵ��齨ģ�ͣ���Ԫ�أ���
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class UIInput extends UIOutput {
    /**ͨ�����Ա�*/
    public enum Propertys {
        /**������RW��*/
        name,
        /**��֤�������ݵ�������ʽ��RW��*/
        verification,
        /**�������¼�OnChangeʱ��RW��*/
        onChangeEL,
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.name.name(), null);
        this.setPropertyMetaValue(Propertys.verification.name(), null);
        this.setPropertyMetaValue(Propertys.onChangeEL.name(), null);
        this.addEventListener(Event.getEvent("OnChange"), new Event_OnChange());
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
    /**���齨ֵ�����ı�֮���ajax���øñ��ʽ��������ã�*/
    public String getOnChangeEL() {
        return this.getProperty(Propertys.onChangeEL.name()).valueTo(String.class);
    }
    /**����һ��EL���ʽ�ñ��ʽ�ᵱ�齨ֵ�����ı�֮����ã�������ã�*/
    public void setOnChangeEL(String onChangeEL) {
        this.getProperty(Propertys.onChangeEL.name()).value(onChangeEL);
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
}
/**������OnChange�¼���EL����*/
class Event_OnChange implements EventListener {
    @Override
    public void onEvent(Event event, UIComponent component, ViewContext viewContext) throws Throwable {
        MethodExpression e = ((UIInput) component).getOnChangeExpression();
        if (e != null)
            viewContext.sendObject(e.execute(component, viewContext));
    }
}