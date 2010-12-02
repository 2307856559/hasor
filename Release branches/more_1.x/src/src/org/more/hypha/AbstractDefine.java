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
package org.more.hypha;
import java.util.HashMap;
import java.util.Map;
import org.more.hypha.beans.BeanDefinePlugin;
import org.more.hypha.beans.BeanDefinePluginSet;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����������������Ϣ��Ҫ���ɵĸ��࣬�����ṩ����չ�����ӿ�{@link BeanDefinePluginSet}�ӿڵ�ʵ�ֺ�{@link IAttribute}�ӿ�ʵ�֡�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractDefine implements IAttribute, BeanDefinePluginSet {
    private IAttribute                    attribute  = null; //����
    private Map<String, BeanDefinePlugin> pluginList = null; //��չ��������
    //========================================================================================
    /**������չDefine����������*/
    public BeanDefinePlugin getPlugin(String name) {
        if (this.pluginList == null)
            return null;
        return this.pluginList.get(name);
    };
    /**����һ��������������������滻�����Ĳ��ע�ᡣ*/
    public void setPlugin(String name, BeanDefinePlugin plugin) {
        if (this.pluginList == null)
            this.pluginList = new HashMap<String, BeanDefinePlugin>();
        this.pluginList.put(name, plugin);
    };
    /**ɾ��һ�����еĲ��ע�ᡣ*/
    public void removePlugin(String name) {
        this.pluginList.remove(name);
    };
    //========================================================================================
    protected IAttribute getAttribute() {
        if (this.attribute == null)
            this.attribute = new AttBase();
        return this.attribute;
    }
    public boolean contains(String name) {
        return this.getAttribute().contains(name);
    };
    public void setAttribute(String name, Object value) {
        this.getAttribute().setAttribute(name, value);
    };
    public Object getAttribute(String name) {
        return this.getAttribute().getAttribute(name);
    };
    public void removeAttribute(String name) {
        this.getAttribute().removeAttribute(name);
    };
    public String[] getAttributeNames() {
        return this.getAttribute().getAttributeNames();
    };
    public void clearAttribute() {
        this.getAttribute().clearAttribute();
    };
}