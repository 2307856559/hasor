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
package org.more.submit;
import java.util.ArrayList;
/**
 * �������ṩActionʱ����ṩ��Action����װ����������������ΪContext�����ڲ���Actionʱ���Զ�����һ��Context���Ҳ����ڵ�Action��
 * ���������Factory���߱��ġ����ҵ�Ŀ��action֮��context�ӿڻ�Ҫ�������������װ�䡣���ṩActionManagerʹ�á�
 * Date : 2009-6-23
 * @author ������
 */
public class ActionContext {
    private ActionContext parentContext = null;               //����������
    private ActionFactory factory       = null;               //����action����
    private FilterManager filterManager = new FilterManager(); //���ع�����������
    //===============================================================
    private ActionContext(ActionFactory factory, ActionContext parent) {
        this.factory = factory;
        this.parentContext = parent;
    }
    public static ActionContext newInstance(ActionFactory factory) {
        return new ActionContext(factory, null);
    }
    public static ActionContext newInstance(ActionFactory factory, ActionContext parent) {
        return new ActionContext(factory, parent);
    }
    //===============================================================
    /**
     * ���Ҳ�����ָ�����Ƶ�Action���������ǰContext���Ҳ���ָ�����Ƶ�Action��Context���Զ�������Context�в��ң�
     * ���Ҳ���ָ�����Ƶ�Action�����򷵻�null���ҵ���action�÷������Զ�װ������������÷������ص�action���������
     * PropxyAction���ʹ���������Ҫ������մ����������Ҫ����PropxyAction�����getFinalTarget������
     * @param name Ҫ���ҵ�action��
     * @return �����ҵ���Action��������Ҳ���ָ�����Ƶ�Action�����򷵻�null��
     */
    public PropxyAction findAction(String name) {
        Object action_obj = this.factory.findAction(name);
        PropxyAction action = null;
        //
        if (action_obj == null)
            if (this.parentContext != null)
                action = this.parentContext.findAction(name);
            else
                return null;
        else {
            action = new PropxyAction();
            action.setTarget(action_obj);
            action.setName(name);
        }
        //���action�Ϲ����������Ƽ���
        String[] filters = this.getActionFilterNames(name);
        //װ��action������
        if (this.filterManager != null)
            action = this.filterManager.installFilter(action, filters);
        return action;
    }
    /**
     * ����ָ����action���󣬻�ȡaction��������
     * @param action ָ����action����
     * @return ��ȡ��action��������
     */
    public String getActionName(PropxyAction action) {
        String ns = action.getName();
        if (ns == null)
            if (this.parentContext != null)
                return this.parentContext.getActionName(action);
            else
                return null;
        else
            return ns;
    }
    /**
     * ����ָ�����Ƶ�Action���������ǰContext���Ҳ���ָ�����Ƶ�Action��Context���Զ�������Context�в��ң�
     * ���Ҳ���ָ�����Ƶ�Action�����򷵻�flase�����򷵻�true��
     * @param name Ҫ���ҵ�action��
     * @return ���ز���Action�Ľ��������Ҳ���ָ�����Ƶ�Action���󷵻�false���򷵻�true��
     */
    public boolean containsAction(String name) {
        if (this.factory.containsAction(name) == false)
            if (this.parentContext != null)
                return this.parentContext.containsAction(name);
            else
                return false;
        else
            return true;
    }
    /**
     * ����ָ����action����ȡ���action�ϵĹ����������ϣ����������Ѿ����չ���������˳����д������
     * �����ǰcontext�в�����ָ�����Ƶ�action��÷����Զ�����һ����context�в��ҡ�
     * @param actionName action��
     * @return ���ظ���ָ����action����ȡ���action�ϵĹ����������ϣ����������Ѿ����չ���������˳����д������
     */
    public String[] getActionFilterNames(String actionName) {
        if (this.containsAction(actionName) == false)
            if (this.parentContext != null)
                return this.parentContext.factory.getActionFilterNames(actionName);
            else
                return new String[0];
        else
            return this.factory.getActionFilterNames(actionName);
    }
    /**
     * ���Ҳ�����ָ��action��ĳһ�����ԣ����Ҳ���ʱ�򷵻�null��
     * @param name Ҫ���ҵ�action��
     * @param propName Ҫ���ҵ�������
     * @return ���ز��Ҳ�����ָ��action��ĳһ�����ԣ����Ҳ���ʱ�򷵻�null��
     */
    public Object findActionProp(String name, String propName) {
        if (this.containsAction(name) == false)
            if (this.parentContext != null)
                return this.parentContext.factory.findActionProp(name, propName);
            else
                return new String[0];
        else
            return this.factory.findActionProp(name, propName);
    }
    /**
     * ���õ�ǰAction�����ĸ��������������˸�����֮�������ǰ�������Ҳ���ָ����Action�򵽸�������ȥ���ҡ�
     * @param parent Ҫ���õĸ����𻷾���
     */
    public void setParent(ActionContext parent) {
        this.parentContext = parent;
    }
    /**
     * ��õ�ǰAction�����ĸ�������
     * @return ���ص�ǰAction�����ĸ�������
     */
    public ActionContext getParent() {
        return this.parentContext;
    }
    /**
     * ����context��ʹ�õĹ�����������
     * @param factory Ҫ���ù�������������
     */
    public void setFilterFactory(FilterFactory factory) {
        this.filterManager.setFactory(factory);
    }
    /**
     * ��ȡcontext��ʹ�õĹ�����������
     * @return ����context��ʹ�õĹ�����������
     */
    public FilterFactory getFilterFactory() {
        return this.filterManager.getFactory();
    }
    /**
     * ��ȡFactory������Action�����Ƽ��ϡ�
     * @return ����Factory������Action�����Ƽ��ϡ�
     */
    public String[] getActionNames() {
        String[] me = this.factory.getActionNames();
        String[] parent = null;
        if (this.parentContext != null)
            parent = this.parentContext.getActionNames();
        else
            parent = new String[0];
        //
        ArrayList<String> al = new ArrayList<String>();
        for (String m : me)
            al.add(m);
        for (String p : parent)
            al.add(p);
        //
        String[] res = new String[al.size()];
        al.toArray(res);
        //
        return res;
    }
    public Class<?> getType(String name) {
        if (this.containsAction(name) == false)
            if (this.parentContext != null)
                return this.parentContext.getType(name);
            else
                return null;
        else
            return this.factory.getType(name);
    }
    public boolean isPrototype(String name) {
        if (this.containsAction(name) == false)
            if (this.parentContext != null)
                return this.parentContext.isPrototype(name);
            else
                return false;
        else
            return this.factory.isPrototype(name);
    }
    public boolean isSingleton(String name) {
        if (this.containsAction(name) == false)
            if (this.parentContext != null)
                return this.parentContext.isSingleton(name);
            else
                return false;
        else
            return this.factory.isSingleton(name);
    }
}
