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
        /**ֵ*/
        value,
        /**�������ı�ʱEL���ñ��ʽ��ajax����*/
        onChangeEL,
    }
    private boolean addEventListener = false;
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        if (addEventListener == false) {
            this.getEventManager().addEventListener(Event.getEvent("OnChange"), new Event_OnChange());
            this.addEventListener = true;
        }
        this.setProperty(Propertys.value.name(), null);
    }
    /*-------------------------------------------------------------------------------*/
    /**��ȡ�齨ֵ*/
    public Object getValue() {
        return this.getProperty(Propertys.value.name()).valueTo(Object.class);
    }
    /**�����齨ֵ*/
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
    public void onEvent(Event event, Sequence sequence) throws Throwable {
        UIInput component = (UIInput) sequence.getParams()[0];
        ViewContext viewContext = (ViewContext) sequence.getParams()[1];
        MethodExpression e = component.getOnChangeExpression();
        if (e != null)
            e.execute(component, viewContext);
    }
    /*-------------------------------------------------------------------------------*/
}