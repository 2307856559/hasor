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
package org.more.submit.impl;
import java.net.URI;
import java.util.Map;
import org.more.submit.ActionStack;
import org.more.submit.SubmitService;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����һ��actionִ��ʱ�Ĳ�����ջ��ÿ������ִ��Action����ʱ��submit�����Զ�����һ���µĶ�ջ������ActionStack�����ṩ�������������֧�֡�
 * ���������ͨ���ܱ�����putScope������ע�������������ܱ���createStackScope���������滻��ǰstack��ջ��������ݴ洢����
 * @version : 2010-7-27
 * @author ������(zyc@byshell.org)
 */
public class DefaultActionStack implements ActionStack {
    private AttBase       attBase = new AttBase();
    private ActionStack   parent  = null;
    private SubmitService service = null;
    //
    /*--------*/
    public DefaultActionStack(URI uri, ActionStack parent, SubmitService service) {
        this.parent = parent;
        this.service = service;
    };
    /*--------*/
    public ActionStack getParent() {
        return this.parent;
    };
    public String getParamString(String key) {
        Object param = this.getParam(key);
        if (param != null)
            return param.toString();
        return null;
    };
    public SubmitService getSubmitService() {
        return this.service;
    };
    public Object getParam(String key) {
        Object obj = this.attBase.get(key);
        if (obj == null && this.parent != null)
            obj = this.parent.getParam(key);
        if (obj == null)
            obj = this.service.getScopeStack().getAttribute(key);
        return obj;
    };
    /*--------*/
    /**�ӵ�ǰ�����������в���һ�������Ƿ���ڡ�*/
    public boolean contains(String name) {
        return this.attBase.contains(name);
    };
    /**��ǰ����������������һ�����ԡ�*/
    public void setAttribute(String name, Object value) {
        this.attBase.setAttribute(name, value);
    };
    /**��ǰ����������������һ�����ԡ�*/
    public Object getAttribute(String name) {
        return this.attBase.getAttribute(name);
    };
    /**�ӵ�ǰ������������ɾ�����ԡ�*/
    public void removeAttribute(String name) {
        this.attBase.removeAttribute(name);
    };
    /**�ӵ�ǰ�����������з���������������*/
    public String[] getAttributeNames() {
        return this.attBase.getAttributeNames();
    };
    /**��յ�ǰ�����������е����ԡ�*/
    public void clearAttribute() {
        this.attBase.clearAttribute();
    };
    public Map<String, Object> toMap() {
        return this.attBase.toMap();
    };
    /**��Map�����в����������ǰ�����������С�*/
    public void putALL(Map<String, ?> params) {
        if (params == null)
            return;
        this.attBase.putAll(params);
    };
    /*--------*/
    protected IAttribute getScopeAttribute(String scope) {
        return this.service.getScope(scope);
    }
    /**��ָ�������������в���һ�������Ƿ���ڣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public boolean contains(String name, String scope) {
        return this.getScopeAttribute(scope).contains(name);
    };
    /**��ָ������������������һ�����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public void setAttribute(String name, Object value, String scope) {
        this.getScopeAttribute(scope).setAttribute(name, value);
    };
    /**��ָ�������������з���ָ�����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public Object getAttribute(String name, String scope) {
        return this.getScopeAttribute(scope).getAttribute(name);
    };
    /**��ָ��������������ɾ�����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public void removeAttribute(String name, String scope) {
        this.getScopeAttribute(scope).removeAttribute(name);
    };
    /**��ָ�������������з���������������scope������ScopeEnum�ӿڵĳ������塣*/
    public String[] getAttributeNames(String scope) {
        return this.getScopeAttribute(scope).getAttributeNames();
    };
    /**���ָ�������������е����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public void clearAttribute(String scope) {
        this.getScopeAttribute(scope).clearAttribute();
    };
    /**��Map�����в��������ָ���Ĳ����������С�*/
    public void putALL(Map<String, ?> params, String scope) {
        IAttribute att = this.getScopeAttribute(scope);
        for (String key : params.keySet())
            att.setAttribute(key, params.get(key));
    };
};