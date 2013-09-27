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
package net.hasor.web.controller.support;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.hasor.core.AppContext;
import net.hasor.web.controller.ControllerInvoke;
/** 
 * �����ռ����������ͬ��action�����ռ��µ�action���������Զ����ڲ�ͬ�Ŀ������¡�
 * @version : 2013-4-20
 * @author ������ (zyc@hasor.net)
 */
class ControllerNameSpace {
    private String                        namespace;
    private Map<String, ControllerInvoke> actionInvokeMap;
    //
    public ControllerNameSpace(String namespace) {
        this.namespace = namespace;
        this.actionInvokeMap = new HashMap<String, ControllerInvoke>();
    }
    //
    public String getNameSpace() {
        return this.namespace;
    }
    //
    public void addAction(Method targetMethod, AppContext appContext) {
        this.actionInvokeMap.put(targetMethod.getName(), new ControllerInvoke(targetMethod, appContext));
    }
    //
    public ControllerInvoke getActionByName(String actionMethodName) {
        return actionInvokeMap.get(actionMethodName);
    }
}