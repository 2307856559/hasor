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
/**
 * webui����
 * @version : 2012-1-1
 * @author ������ (zyc@byshell.org)
 */
public class UIParamter extends UIComponent {
    public String getComponentType() {
        return "WebUI.UIParamter";
    };
    /*-------------------------------------------------------------------------------����*/
    /**{@link UIParamter}�������б�*/
    public enum Propertys {
        name, value
    };
    /**��ȡ������*/
    public String getName() {
        return this.getProperty(Propertys.name.name()).valueTo(String.class);
    };
    /**���ò�����*/
    public void setName(String name) {
        this.setProperty(Propertys.name.name(), name);
    };
    /**��ȡ����ֵ*/
    public Object getValue() {
        return this.getProperty(Propertys.value.name()).valueTo(Object.class);
    };
    /**���ò���ֵ*/
    public void setValue(Object newValue) {
        this.setProperty(Propertys.value.name(), newValue);
    };
}