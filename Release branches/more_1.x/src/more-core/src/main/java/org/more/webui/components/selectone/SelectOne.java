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
package org.more.webui.components.selectone;
import java.util.List;
import org.more.webui.component.NoState;
import org.more.webui.component.UIInput;
import org.more.webui.component.support.UICom;
import org.more.webui.context.ViewContext;
/**
 * ����ѡ����齨
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
@UICom(tagName = "ui_SelectOne")
public class SelectOne extends UIInput {
    /**ͨ�����Ա�*/
    public static enum Propertys {
        /** ���ݣ�-��*/
        listData,
        /**��ʾ�����ֶΣ�R��*/
        keyField,
        /**ֵ�ֶΣ�R��*/
        varField
    }
    @Override
    public String getComponentType() {
        return "ui_SelectOne";
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setProperty(Propertys.keyField.name(), "key");
        this.setProperty(Propertys.varField.name(), "value");
    }
    @NoState
    public List<?> getListData() {
        return this.getProperty(Propertys.listData.name()).valueTo(List.class);
    }
    @NoState
    public void setListData(List<?> listData) {
        this.getProperty(Propertys.listData.name()).value(listData);
    }
    public String getKeyField() {
        return this.getProperty(Propertys.keyField.name()).valueTo(String.class);
    }
    @NoState
    public void setKeyField(String keyField) {
        this.getProperty(Propertys.keyField.name()).value(keyField);
    }
    public String getVarField() {
        return this.getProperty(Propertys.varField.name()).valueTo(String.class);
    }
    @NoState
    public void setVarField(String varField) {
        this.getProperty(Propertys.varField.name()).value(varField);
    }
}