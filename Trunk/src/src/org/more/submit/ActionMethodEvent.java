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
import java.util.Map;
import org.more.util.attribute.AttBase;
/**
 * action�����¼�����
 * Date : 2009-6-29
 * @author ������
 */
@SuppressWarnings("unchecked")
public class ActionMethodEvent extends AttBase {
    /**  */
    private static final long serialVersionUID = 7174117801875259949L;
    private ActionManager     context          = null;
    private String            invokeString     = null;                // 
    private String            actionName       = null;                // 
    private String            method           = null;                //������õ�Ŀ�귽������
    private String            invokeMethod     = null;                //ʵ�ʵ��õ�Ŀ�귽������
    private Map               param            = null;                //�������
    //============================================================
    /**
     * ��ô�����õ�Ŀ�귽������
     * @return ���ش�����õ�Ŀ�귽������
     */
    public String getMethod() {
        return method;
    }
    /**
     * ���ʵ�ʵ��õ�Ŀ�귽������
     * @return ����ʵ�ʵ��õ�Ŀ�귽������
     */
    public String getInvokeMethod() {
        return invokeMethod;
    }
    /**
     * ��ȡ��������
     * @param name ������
     * @return ���ز�������
     */
    public Object getParam(String name) {
        if (this.param.containsKey(name) == false)
            return null;
        return this.param.get(name);
    }
    /**
     * ��ȡ��������
     * @param name ������
     * @return ���ز�������
     */
    public String getParamString(String name) {
        if (this.param.containsKey(name) == false)
            return null;
        Object obj = this.param.get(name);
        if (obj == null)
            return null;
        else
            return obj.toString();
    }
    /**
     * ��ȡ����������
     * @return ���ز���������
     */
    public String[] getParamNames() {
        String[] ns = new String[this.param.size()];
        this.param.keySet().toArray(ns);
        return ns;
    }
    /**
     * ��õ���Action�����ơ�
     * @return ���ص���Action�����ơ�
     */
    public String getActionName() {
        return actionName;
    }
    /**
     * ��õ��õ������ġ�
     * @return ���ص��õ������ġ�
     */
    public ActionManager getContext() {
        return context;
    }
    /**
     * ���paramMap��������
     * @return ����paramMap��������
     */
    public Map getParamMap() {
        return this.param;
    }
    /**
     * ���action�����ַ�����
     * @return ����action�����ַ�����
     */
    public String getInvokeString() {
        return invokeString;
    }
    void setMethod(String method) {
        this.method = method;
    }
    void setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
    }
    void setParam(Map param) {
        this.param = param;
    }
    void setContext(ActionManager context) {
        this.context = context;
    }
    void setActionName(String actionName) {
        this.actionName = actionName;
    }
    void setInvokeString(String invokeString) {
        this.invokeString = invokeString;
    }
}