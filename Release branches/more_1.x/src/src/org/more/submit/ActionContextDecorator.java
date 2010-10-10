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
import java.util.Iterator;
import org.more.NotFoundException;
/**
 * ActionContext�ӿ�װ���������������һ�����κθ��ӹ��ܵĿ�װ����������ͨ�������������дĳЩ�����������书�ܵ�Ŀ�ġ�
 * @version : 2010-7-26
 * @author ������(zyc@byshell.org)
 */
public abstract class ActionContextDecorator implements ActionContext {
    protected ActionContext actionContext;
    /**������װ�����ɹ���ʼ������Ҫ����true���򷵻�false��submit������������ľ����Ƿ������װ������*/
    public boolean initDecorator(ActionContext actionContext) {
        this.actionContext = actionContext;
        return true;
    };
    public boolean containsAction(String actionName) {
        return this.actionContext.containsAction(actionName);
    };
    public ActionInvoke findAction(String actionName, String invoke) throws NotFoundException {
        return this.actionContext.findAction(actionName, invoke);
    };
    public Iterator<String> getActionNameIterator() {
        return this.actionContext.getActionNameIterator();
    };
    public Object getActionProperty(String actionName, String property) {
        return this.actionContext.getActionProperty(actionName, property);
    };
    public Class<?> getActionType(String actionName) {
        return this.actionContext.getActionType(actionName);
    };
};