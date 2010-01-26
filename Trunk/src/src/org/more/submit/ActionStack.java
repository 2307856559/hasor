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
import org.more.NoDefinitionException;
import org.more.StateException;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ����һ��actionִ��ʱ�Ĳ�����ջ��ÿ������ִ��Action����ʱ��submit�����Զ�����һ���µĶ�ջ��
 * ����ActionStack�����ṩ�������������֧�֡�ͨ����չ���໹�����Զ�������������
 * @version 2009-12-2
 * @author ������ (zyc@byshell.org)
 */
public class ActionStack implements IAttribute, ScopeEnum {
    //========================================================================================Field
    private static final long     serialVersionUID = 5001483997344333143L;
    private String                actionName;                             //���õ�action��
    private String                actionMethod;                           //���õ�action������
    private String                invokeString;                           //�����ַ���
    //����������
    private final ActionStack     parent;                                 //��ջ������ֻ��
    protected final IAttribute    moreStackScope;                         //Stack������ֻ��
    protected final Session       moreSessionScope;                       //Session������ֻ��
    protected final SubmitContext moreContextScope;                       //Context������ֻ��
    //���ؽű�������
    private String                resultsScript;                          //����action���֮ǰִ�е�JS�ű���
    private Object[]              resultsScriptParams;                    //����action���֮ǰִ�е�JS�ű������ݵĲ�����
    //�������������ֶ�
    private String                currentScope;                           //��ǰ�����������ǡ�
    private IAttribute            attributeForScope;                      //��ǰ��������������ӿڡ�
    private boolean               synchronizeStack = true;                //�����ǰ��������Stack�Ƿ�ͬ�������������Stack��Ĭ������(true)��
    //==================================================================================Constructor
    public ActionStack(ActionStack parent, Session moreSessionScope, SubmitContext moreContextScope) {
        this.parent = parent;
        this.moreStackScope = this.createStackScope();
        this.moreSessionScope = moreSessionScope;
        this.moreContextScope = moreContextScope;
    };
    /**��ʼ��ActionStack����*/
    public void init() {
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
    /**��ȡ��ǰ��ջ�ĸ���ջ��*/
    public ActionStack getParent() {
        return parent;
    };
    /**��ȡ��ǰ��ջ����ʱʹ�õ�Session��*/
    public Session getSession() {
        return moreSessionScope;
    };
    /**��ȡ��ǰ��ջ����ʱʹ�õ�SubmitContext��*/
    public SubmitContext getContext() {
        return moreContextScope;
    };
    /** ���ص�����Action���ý���֮����лص��Ľű����� */
    public String getResultsScript() {
        return resultsScript;
    };
    /*----------------------------------*/
    void setActionName(String actionName) {
        this.actionName = actionName;
    };
    void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    };
    void setInvokeString(String invokeString) {
        this.invokeString = invokeString;
    };
    /*----------------------------------*/
    /**��stack���������в������ԣ�getByStackTree���������stack��ջһ��һ����ҡ�*/
    protected Object getByStackTree(String key) {
        Object obj = this.moreStackScope.getAttribute(key);
        if (obj == null && this.parent != null)
            obj = this.parent.getByStackTree(key);
        return obj;
    };
    /**getParam�������ַ�����ʽ���أ�������Ҳ��������򷵻�null��*/
    public String getParamString(String key) {
        Object obj = this.getParam(key);
        return (obj == null) ? null : obj.toString();
    };
    /** ����stack->session->context���˳�����β������ԣ���stack�в���ʱ��������stack���в��ҡ�*/
    public Object getParam(String key) {
        Object obj = this.getByStackTree(key);
        if (obj == null && this.moreSessionScope != null)
            obj = this.moreSessionScope.getAttribute(key);
        if (obj == null)
            obj = this.moreContextScope.getAttribute(key);
        return obj;
    };
    /*----------------------------------*/
    /** ���õ�����Action���ý���֮����лص��Ľű����ƣ��ڶ������������˵��ýű�ʱ���ݵĲ�������Щ�ű������/META-INF/submit_scripts/Ŀ¼�� */
    public void setResultsScript(String resultsScript, Object... params) {
        this.resultsScript = resultsScript;
        this.resultsScriptParams = params;
    };
    Object[] getResultsScriptParams() {
        return resultsScriptParams;
    };
    /*----------------------------------*/
    /**��ȡһ��booleanֵ����ֵ�������Ƿ�ͬ����ǰ����������Ĳ�����ActionStack�������С�Ĭ��ֵ��true��*/
    public boolean isSynchronizeStack() {
        return synchronizeStack;
    };
    /**����һ��booleanֵ����ֵ�������Ƿ�ͬ����ǰ����������Ĳ�����ActionStack�������С�*/
    public void setSynchronizeStack(boolean synchronizeStack) {
        this.synchronizeStack = synchronizeStack;
    };
    /**��ȡ��ǰ����������*/
    public String getScope() {
        return currentScope;
    };
    /**���õ�ǰ�����������������ͼ����Ϊδ������������������NoDefinitionException�쳣��ע��currentScope��������ʹ��ScopeEnum�ӿڵĳ������塣*/
    public void setScope(String currentScope) {
        this.attributeForScope = getScopeAttribute(currentScope);
        this.currentScope = currentScope;
    };
    /*----------------------------------*/
    /**�������ͨ����д�÷����Դﵽ�滻Stack�������Ŀ�ġ�*/
    protected IAttribute createStackScope() {
        return new AttBase();
    };
    /**��ȡ��ǰ����������Ĳ����ӿ�*/
    protected IAttribute getCurrentScopeAtt() {
        return this.attributeForScope;
    }
    /*----------------------------------*/
    /**�ӵ�ǰ�����������в���һ�������Ƿ���ڣ��÷��������������*/
    public boolean contains(String name) {
        return this.attributeForScope.contains(name);
    };
    /**��ǰ����������������һ�����ԣ����synchronizeStack��������Ϊtrue�����������Ĳ�������stack��Χ���ظ�һ�Ρ����Ƕ���stack��Χ������һ�Ρ�*/
    public void setAttribute(String name, Object value) {
        this.attributeForScope.setAttribute(name, value);
        if (synchronizeStack == true && this.currentScope.equals(Scope_Stack) == false)
            this.moreStackScope.setAttribute(name, value);
    };
    /**��ǰ����������������һ�����ԣ����synchronizeStack��������Ϊtrue�����������Ĳ�������stack��Χ���ظ�һ�Ρ����Ƕ���stack��Χ������һ�Ρ�*/
    public Object getAttribute(String name) {
        return this.attributeForScope.getAttribute(name);
    };
    /**�ӵ�ǰ������������ɾ�����ԣ����synchronizeStack��������Ϊtrue�����������Ĳ�������stack��Χ���ظ�һ�Ρ����Ƕ���stack��Χ������һ�Ρ�*/
    public void removeAttribute(String name) {
        this.attributeForScope.removeAttribute(name);
        if (synchronizeStack == true && this.currentScope.equals(Scope_Stack) == false)
            this.moreStackScope.removeAttribute(name);
    };
    /**�ӵ�ǰ�����������з���������������*/
    public String[] getAttributeNames() {
        return this.attributeForScope.getAttributeNames();
    };
    /**��յ�ǰ�����������е����ԣ����synchronizeStack��������Ϊtrue�����������Ĳ�������stack��Χ���ظ�һ�Ρ����Ƕ���stack��Χ������һ�Ρ�*/
    public void clearAttribute() {
        this.attributeForScope.clearAttribute();
        if (synchronizeStack == true && this.currentScope.equals(Scope_Stack) == false)
            this.moreStackScope.clearAttribute();
    };
    /*--------*/
    protected IAttribute getScopeAttribute(String scope) {
        if (Scope_Stack.equals(scope) == true)
            return this.moreStackScope;
        else if (Scope_Session.equals(scope) == true)
            if (this.moreSessionScope != null)
                return this.moreSessionScope;
            else
                throw new StateException("��ǰ״̬��֧��session������");
        else if (Scope_Context.equals(scope) == true)
            return this.moreContextScope;
        else
            throw new NoDefinitionException("�޷��л���δ�����������");
    }
    /**��ָ�������������в���һ�������Ƿ���ڣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public boolean contains(String name, String scope) {
        return this.getScopeAttribute(scope).contains(name);
    };
    /**��ָ������������������һ�����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public void setAttribute(String name, Object value, String scope) {
        this.getScopeAttribute(scope).setAttribute(name, value);
        if (synchronizeStack == true && this.currentScope.equals(Scope_Stack) == false)
            this.moreStackScope.setAttribute(name, value);
    };
    /**��ָ�������������з���ָ�����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public Object getAttribute(String name, String scope) {
        return this.getScopeAttribute(scope).getAttribute(name);
    };
    /**��ָ��������������ɾ�����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public void removeAttribute(String name, String scope) {
        this.getScopeAttribute(scope).removeAttribute(name);
        if (synchronizeStack == true && this.currentScope.equals(Scope_Stack) == false)
            this.moreStackScope.removeAttribute(name);
    };
    /**��ָ�������������з���������������scope������ScopeEnum�ӿڵĳ������塣*/
    public String[] getAttributeNames(String scope) {
        return this.getScopeAttribute(scope).getAttributeNames();
    };
    /**���ָ�������������е����ԣ�scope������ScopeEnum�ӿڵĳ������塣*/
    public void clearAttribute(String scope) {
        this.getScopeAttribute(scope).clearAttribute();
        if (synchronizeStack == true && this.currentScope.equals(Scope_Stack) == false)
            this.moreStackScope.clearAttribute();
    };
}