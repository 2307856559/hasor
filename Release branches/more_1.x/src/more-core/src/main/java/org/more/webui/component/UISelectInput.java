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
import org.more.webui.component.support.NoState;
import org.more.webui.context.ViewContext;
/**
 * ���ڱ����Ӷ��ֵ�н���ѡ����齨ģ�ͣ���Ԫ�أ���
 * @version : 2012-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class UISelectInput extends UIInput {
    /**ͨ�����Ա�*/
    public static enum Propertys {
        /** ���ݣ�-��*/
        listData,
        /**��ʾ�����ֶΣ�R��*/
        keyField,
        /**ֵ�ֶΣ�R��*/
        varField,
    }
    @Override
    protected void initUIComponent(ViewContext viewContext) {
        super.initUIComponent(viewContext);
        this.setPropertyMetaValue(Propertys.listData.name(), null);
        this.setPropertyMetaValue(Propertys.keyField.name(), "key");
        this.setPropertyMetaValue(Propertys.varField.name(), "value");
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
    /**ѡ���Ψһֵ��(R)<br/>getSelectValue�Ƕ�{@link #getSelectValues()}���������졣
     * �÷���ֻ�᷵��{@link #getSelectValues()}��������ֵ�ĵ�һ��Ԫ�أ�������������Ԫ���򷵻�null��*/
    public Object getSelectValue() {
        Object[] returnData = getSelectValues();
        if (returnData != null && returnData.length != 0)
            return returnData[0];
        return null;
    }
    /**ѡ���ֵ��(R)<br/>SelectValue�����Ƕ�value������ǿ���͡�
     * ��valueֵΪObjectʱselectvalue��һ��ֻ��һ��Ԫ�ص����顣
     * ���valueΪString��selectvalue����ݡ��������ַ�����֡�
     * ���valueΪ����򼯺���selectValue���ؼ��ϵ�������ʽ��*/
    public Object[] getSelectValues() {
        Object var = this.getValue();
        if (var == null || var.getClass().isArray() == false) {
            if (var instanceof String)
                return ((String) var).split(",");
            return new Object[] { var };
        }
        Class<?> varType = var.getClass();
        if (varType.isArray() == true)
            return (Object[]) var;
        else if (var instanceof List == true)
            return ((List) var).toArray();
        else
            return null;
    }
}