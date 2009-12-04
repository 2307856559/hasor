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
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * ��ִ��Action����ʱ��submit3.0�Զ�����һ�����ö�ջ�����action�ڴ������ٴε���������action��ôsubmit���ᴴ����һ����ջ����
 * �������ջ�������Ϊ�ĸ��¶�ջ�ĸ�����ͨ��ActionStack���Ի�ȡ��ִ��doActionʱ�򴫵ݵĲ�����
 * <br/>Date : 2009-12-2
 * @author ������
 */
public class ActionStack implements IAttribute {
    //========================================================================================Field
    private static final long serialVersionUID = 5001483997344333143L;
    private IAttribute        stackAtt         = new AttBase();
    private ActionStack       parent           = null;                //
    private String            actionName       = null;                //
    private String            actionMethod     = null;                //
    private String            invokeString     = null;                //
    private Session           session          = null;                //
    private SubmitContext     context          = null;
    //==================================================================================Constructor
    ActionStack(ActionStack parent, Session session, SubmitContext context) {
        this.parent = parent;
        this.session = session;
        this.context = context;
    };
    //==========================================================================================Job
    /**
     * ���action�����ַ�����
     * @return ����action�����ַ�����
     */
    public String getInvokeString() {
        return invokeString;
    };
    /**
     * ��ȡ��ǰ�¼�������Action����
     * @return �����¼�������Action����
     */
    public String getActionName() {
        return actionName;
    };
    /**
     * ��ȡ��ǰ�¼�������Action��������
     * @return ���ص�ǰ�¼�������Action������
     */
    public String getActionMethod() {
        return actionMethod;
    };
    /**
     * �ӵ�ǰ��ջ�л�ȡָ�����ƵĶ�ջ�����������ǰ��ջ�������м����������Զ��򸸼���ջ����һֱ�������ջΪֹ��
     * �������ջҲ�޷������ò���ʱ�����Ҷ���ջ��sessionȻ����Ҷ���ջ��contextֱ��������null��
     * @param key Ҫ��ȡ�Ĳ������ơ�
     * @return ���ػ�ȡ�Ķ�ջ������
     */
    public Object getParam(String key) {
        Object obj = this.getAttribute(key);
        if (obj == null && this.parent != null)
            obj = this.parent.getParam(key);
        //
        if (this.session != null)
            obj = this.session.getAttribute(key);
        if (obj == null)
            obj = this.context.getAttribute(key);
        return obj;
    };
    /**
     * �ӵ�ǰ��ջ�л�ȡָ�����ƵĶ�ջ�����������ǰ��ջ�������м����������Զ��򸸼���ջ����һֱ�������ջΪֹ��
     * �������ջҲ�޷������ò���ʱ�����Ҷ���ջ��sessionȻ����Ҷ���ջ��contextֱ��������null��
     * @param key Ҫ��ȡ�Ĳ������ơ�
     * @return ���ػ�ȡ�Ķ�ջ������
     */
    public String getParamString(String key) {
        Object obj = this.getParam(key);
        return (obj == null) ? null : obj.toString();
    }
    /**
     * ��ȡ��ǰ��ջ�ĸ���ջ��
     * @return ���ص�ǰ��ջ�ĸ���ջ��
     */
    public ActionStack getParent() {
        return parent;
    };
    /**
     * ��ȡ��ǰ��ջ����ʱʹ�õ�Session��
     * @return ���ص�ǰ��ջ����ʱʹ�õ�Session��
     */
    public Session getSession() {
        return session;
    };
    /**
     * ��ȡ��ǰ��ջ����ʱʹ�õ�SubmitContext��
     * @return ���ص�ǰ��ջ����ʱʹ�õ�SubmitContext��
     */
    public SubmitContext getContext() {
        return context;
    }
    //==========================================================================================Job
    void setParent(ActionStack parent) {
        this.parent = parent;
    };
    void setActionName(String actionName) {
        this.actionName = actionName;
    };
    void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    };
    void setInvokeString(String invokeString) {
        this.invokeString = invokeString;
    }
    //==========================================================================================Att
    @Override
    public void clearAttribute() {
        this.stackAtt.clearAttribute();
    }
    @Override
    public boolean contains(String name) {
        return this.stackAtt.contains(name);
    }
    @Override
    public Object getAttribute(String name) {
        return this.stackAtt.getAttribute(name);
    }
    @Override
    public String[] getAttributeNames() {
        return this.stackAtt.getAttributeNames();
    }
    @Override
    public void removeAttribute(String name) {
        this.stackAtt.removeAttribute(name);
    }
    @Override
    public void setAttribute(String name, Object value) {
        this.stackAtt.setAttribute(name, value);
    }
}