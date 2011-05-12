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
import java.util.HashMap;
import java.util.Map;
import org.more.NoDefinitionException;
import org.more.NotFoundException;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����һ��actionִ��ʱ�Ĳ�����ջ��ÿ������ִ��Action����ʱ��submit�����Զ�����һ���µĶ�ջ������ActionStack�����ṩ�������������֧�֡�
 * ���������ͨ���ܱ�����putScope������ע�������������ܱ���createStackScope���������滻��ǰstack��ջ��������ݴ洢����
 * @version : 2010-7-27
 * @author ������(zyc@byshell.org)
 */
public class ActionStack implements IAttribute, ScopeEnum {
    //========================================================================================Field
    private static final long       serialVersionUID    = 5001483997344333143L;
    private String                  actionName          = null;                             //���õ�action��
    private String                  actionMethod        = null;                             //���õ�action������
    private String                  invokeString        = null;                             //�����ַ���
    //����������
    private String                  currentMark         = null;                             //��ǰ�������ǡ�
    private IAttribute              currentScope;                                           //��ǰ��ջ��������ݴ洢����
    private Map<String, IAttribute> scopeMap            = new HashMap<String, IAttribute>();
    //���ؽű�������
    private String                  resultsScriptEngine = "JavaScript";                     //ִ�нű���������
    private String                  resultsScript       = null;                             //����action���֮ǰִ�е�JS�ű���
    private Object[]                resultsScriptParams = null;                             //����action���֮ǰִ�е�JS�ű������ݵĲ�����
    //�������������ֶ�
    //==================================================================================Constructor
    public ActionStack(String actionName, String actionMethod, ActionStack parent, Session moreSessionScope, SubmitContext moreContextScope) {
        this.invokeString = actionName + "." + actionMethod;
        this.actionName = actionName;
        this.actionMethod = actionMethod;
        this.putScope(Scope_Stack, this.createStackScope());//Stack������
        this.putScope(Scope_Parent, parent);//Parent������
        this.putScope(Scope_Session, moreSessionScope);//Session������
        this.putScope(Scope_Context, moreContextScope);//Context������
        this.setScope(Scope_Stack);
    };
    //==========================================================================================Job
    /**���action�����ַ�����*/
    public String getInvokeString() {
        return invokeString;
    };
    /**��ȡ��ǰ�¼�������Action����*/
    public String getActionName() {
        return actionName;
    };
    /**��ȡ��ǰ�¼�������Action��������*/
    public String getActionMethod() {
        return actionMethod;
    };
    /*--------------------------------------------------------------------*/
    /**��ȡ��ǰ��ջ�ĸ���ջ��*/
    public ActionStack getParent() {
        return (ActionStack) this.scopeMap.get(Scope_Parent);
    };
    /**��ȡ��ǰ��ջ����ʱʹ�õ�Session��*/
    public Session getSession() {
        return (Session) this.scopeMap.get(Scope_Session);
    };
    /**��ȡ��ǰ��ջ����ʱʹ�õ�SubmitContext��*/
    public SubmitContext getContext() {
        return (SubmitContext) this.scopeMap.get(Scope_Context);
    };
    /**��ȡ���ջ������������ӿڣ��÷������ܵ��л��������Ӱ�졣*/
    public IAttribute getThisStack() {
        return this.scopeMap.get(Scope_Stack);
    };
    /**��ȡ��ǰ����ʹ�õĲ���������Ĳ����ӿڣ�����л�����������÷����ķ���ֵҲ������л���*/
    public IAttribute getCurrentScopeAtt() {
        return this.currentScope;
    };
    /**
     * ��ȡָ����������Ĳ����ӿڡ���ͬ��getCurrentScopeAtt��������������Ի�ȡ������Ҫ���������������л�������
     * �����ͼ��ȡ�����ڵ��������������{@link NotFoundException}�쳣��
     */
    public IAttribute getScopeAttribute(String scope) {
        if (this.scopeMap.containsKey(scope) == false)
            throw new NotFoundException("�޷���á�" + scope + "��������������������û��ע�ᡣ");
        return this.scopeMap.get(scope);
    };
    /*--------------------------------------------------------------------*/
    /**��stack���������в������ԣ�getByStackTree���������stack��ջһ��һ����ҡ��������漰��session��context������������*/
    protected Object getByStackTree(String key) {
        Object obj = this.getThisStack().getAttribute(key);
        if (obj == null && this.getParent() != null)
            obj = this.getParent().getByStackTree(key);
        return obj;
    };
    /**getParam�������ַ�����ʽ���أ�������Ҳ��������򷵻�null��*/
    public String getParamString(String key) {
        Object obj = this.getParam(key);
        return (obj == null) ? null : obj.toString();
    };
    /** ����stack->parent->session->context���˳�����β������ԣ���stack�в���ʱ��������stack���в��ҡ�*/
    public Object getParam(String key) {
        Object obj = this.getByStackTree(key);
        if (obj == null && this.getSession() != null)
            obj = this.getSession().getAttribute(key);
        if (obj == null)
            obj = this.getContext().getAttribute(key);
        return obj;
    };
    /*--------------------------------------------------------------------*/
    /**��ȡ��ִ�лص��ű�ʱʹ�õĽű���������*/
    public String getResultsScriptEngine() {
        return resultsScriptEngine;
    }
    /**���õ�ִ�лص��ű�ʱʹ�õĽű���������*/
    public void setResultsScriptEngine(String resultsScriptEngine) {
        this.resultsScriptEngine = resultsScriptEngine;
    }
    /** ���ص�����Action���ý���֮����лص��Ľű����� */
    public String getResultsScript() {
        return resultsScript;
    };
    /** ���õ�����Action���ý���֮����лص��Ľű����ƣ��ڶ������������˵��ýű�ʱ���ݵĲ�������Щ�ű������/META-INF/submit_scripts/Ŀ¼�� */
    public void setResultsScript(String resultsScript, Object... params) {
        this.resultsScript = resultsScript;
        this.resultsScriptParams = params;
    };
    Object[] getResultsScriptParams() {
        return resultsScriptParams;
    };
    /*--------------------------------------------------------------------*/
    /**��ȡ��ǰ����������*/
    public String getScope() {
        return this.currentMark;
    };
    /**���õ�ǰ�����������������ͼ����Ϊδ������������������{@link NotFoundException}�쳣��ע��scope����������ScopeEnum�ӿڵĳ������塣*/
    public void setScope(String scope) throws NoDefinitionException {
        if (this.scopeMap.containsKey(scope) == false)
            throw new NotFoundException("�������õ�δ�����������������������û��ע�ᡣ");
        this.currentScope = this.scopeMap.get(scope);
        this.currentMark = scope;
    };
    /**��һ��������ע������滻��scopeMap�С�*/
    protected void putScope(String scopeKEY, IAttribute scope) {
        if (scope != null)
            this.scopeMap.put(scopeKEY, scope);
    };
    /**��������Map���Ƴ�һ���������ע�ᡣ*/
    protected void removeScope(String scopeKEY) {
        if (scopeKEY != null)
            this.scopeMap.remove(scopeKEY);
    };
    /**�����Ƿ����ĳ�����õ�ע�ᡣ������ڷ���true���򷵻�false��*/
    public boolean containsScopeKEY(String scopeKEY) {
        return this.scopeMap.containsKey(scopeKEY);
    };
    /**�������ͨ����д�÷����Դﵽ�滻Stack�������Ŀ�ġ�*/
    protected IAttribute createStackScope() {
        return new AttBase();
    };
    /*--------------------------------------------------------------------*/
    /**�ӵ�ǰ�����������в���һ�������Ƿ���ڡ�*/
    public boolean contains(String name) {
        return this.currentScope.contains(name);
    };
    /**��ǰ����������������һ�����ԡ�*/
    public void setAttribute(String name, Object value) {
        this.currentScope.setAttribute(name, value);
    };
    /**��ǰ����������������һ�����ԡ�*/
    public Object getAttribute(String name) {
        return this.currentScope.getAttribute(name);
    };
    /**�ӵ�ǰ������������ɾ�����ԡ�*/
    public void removeAttribute(String name) {
        this.currentScope.removeAttribute(name);
    };
    /**�ӵ�ǰ�����������з���������������*/
    public String[] getAttributeNames() {
        return this.currentScope.getAttributeNames();
    };
    /**��յ�ǰ�����������е����ԡ�*/
    public void clearAttribute() {
        this.currentScope.clearAttribute();
    };
    public Map<String, Object> toMap() {
        return this.currentScope.toMap();
    };
    /**��Map�����в����������ǰ�����������С�*/
    public void putALL(Map<String, ?> params) {
        if (params == null)
            return;
        IAttribute att = this.getCurrentScopeAtt();
        for (String key : params.keySet())
            att.setAttribute(key, params.get(key));
    };
    /*--------*/
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
    }
};