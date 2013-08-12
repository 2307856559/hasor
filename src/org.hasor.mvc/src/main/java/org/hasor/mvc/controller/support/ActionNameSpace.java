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
package org.hasor.mvc.controller.support;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hasor.context.AppContext;
import org.hasor.mvc.controller.HttpMethod;
import org.more.util.StringConvertUtils;
/** 
 * �����ռ����������ͬ��action�����ռ��µ�action���������Զ����ڲ�ͬ�Ŀ������¡�
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
class ActionNameSpace {
    private String                                         namespace       = null;
    private Map<HttpMethod, Map<String, ActionDefineImpl>> actionInvokeMap = new HashMap<HttpMethod, Map<String, ActionDefineImpl>>();
    //
    public ActionNameSpace(String namespace) {
        this.namespace = namespace;
    }
    /**��ȡ���������ơ�*/
    public String getNameSpace() {
        return this.namespace;
    }
    /**��ȡע�������Action*/
    public List<ActionDefineImpl> getActions() {
        ArrayList<ActionDefineImpl> actionList = new ArrayList<ActionDefineImpl>();
        for (Map<String, ActionDefineImpl> invokeMap : this.actionInvokeMap.values())
            for (ActionDefineImpl invoke : invokeMap.values())
                actionList.add(invoke);
        return actionList;
    }
    /**��ȡ�������ж����action������*/
    public ActionDefineImpl getActionByName(String method, String actionMethodName) {
        HttpMethod httpMethod = StringConvertUtils.parseEnum(method, HttpMethod.class, HttpMethod.Any);
        Map<String, ActionDefineImpl> actionMap = actionInvokeMap.get(httpMethod);
        actionMap = (actionMap != null) ? actionMap : actionInvokeMap.get(HttpMethod.Any);
        if (actionMap != null) {
            return actionMap.get(actionMethodName);
        }
        return null;
    }
    /**��ʼ��NameSpace*/
    public void initNameSpace(AppContext appContext) {
        for (Map<String, ActionDefineImpl> invokeMap : this.actionInvokeMap.values())
            for (ActionDefineImpl invoke : invokeMap.values())
                invoke.initInvoke(appContext);
    }
    /**����NameSpace*/
    public void destroyNameSpace(AppContext appContext) {
        this.actionInvokeMap.clear();
    }
    /**���Action*/
    public void putActionDefine(ActionDefineImpl define) {
        if (define == null)
            return;
        for (HttpMethod httpMethod : define.getHttpMethod()) {
            Map<String, ActionDefineImpl> invokeMap = this.actionInvokeMap.get(httpMethod);
            if (invokeMap == null) {
                invokeMap = new HashMap<String, ActionDefineImpl>();
                this.actionInvokeMap.put(httpMethod, invokeMap);
            }
            String actionName = define.getTargetMethod().getName();
            invokeMap.put(actionName, define);
        }
    }
}