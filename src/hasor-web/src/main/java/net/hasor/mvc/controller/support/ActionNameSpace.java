/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.mvc.controller.support;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hasor.core.AppContext;
/** 
 * �����ռ����������ͬ��action�����ռ��µ�action���������Զ����ڲ�ͬ�Ŀ������¡�
 * @version : 2013-4-20
 * @author ������ (zyc@hasor.net)
 */
class ActionNameSpace {
    private String                        namespace        = null;
    private Map<String, ActionDefineImpl> actionInvokeMap  = new HashMap<String, ActionDefineImpl>();
    private List<ActionDefineImpl>        actionInvokeList = new ArrayList<ActionDefineImpl>();
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
        return this.actionInvokeList;
    }
    /**��ȡ�������ж����action������*/
    public ActionDefineImpl getActionByName(String actionMethod) {
        return this.actionInvokeMap.get(actionMethod);
    }
    /**��ʼ��NameSpace*/
    public void initNameSpace(AppContext appContext) {
        for (ActionDefineImpl invoke : this.actionInvokeMap.values())
            invoke.initInvoke(appContext);
    }
    /**���Action*/
    public void putActionDefine(ActionDefineImpl define) {
        if (define == null)
            return;
        this.actionInvokeMap.put(define.getTargetMethod().getName(), define);
        this.actionInvokeList.add(define);
    }
}