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
/**
 * ActionContext�ӿڵĻ���ʵ�֣������ฺ���ṩ����action�����Լ�action����Ĺ����������ȡ��
 * ��ϸ���ܲο�{@link ActionContext ActionContext�ӿ�}
 * <br/>Date : 2009-12-1
 * @author ������
 */
public abstract class AbstractActionContext implements ActionContext {
    /**����Action���ƻ�ȡAction����*/
    protected abstract Object getActionBean(String actionName);
    /**��ȡָ������action��˽�й������������顣*/
    protected abstract ActionFilter[] getPrivateFilterBean(String actionName);
    /**��ȡָ������action�Ĺ��й������������顣*/
    protected abstract ActionFilter[] getPublicFilterBean(String actionName);
    @Override
    public ActionInvoke configPrivateFilter(String actionName, ActionInvoke invokeObject) {
        ActionFilter[] afList = this.getPrivateFilterBean(actionName);
        if (afList == null)
            return invokeObject;
        FilterChain chain = new FilterChain(invokeObject);
        for (ActionFilter af : afList)
            chain = new FilterChain(chain, af);
        return new FilterActionInvoke(chain);
    }
    @Override
    public ActionInvoke configPublicFilter(String actionName, ActionInvoke invokeObject) {
        ActionFilter[] afList = this.getPublicFilterBean(actionName);
        if (afList == null)
            return invokeObject;
        FilterChain chain = new FilterChain(invokeObject);
        for (ActionFilter af : afList)
            chain = new FilterChain(chain, af);
        return new FilterActionInvoke(chain);
    }
    @Override
    public ActionInvoke findAction(String actionName, String invoke) throws NoDefinitionException {
        if (this.containsAction(actionName) == false)
            throw new NoDefinitionException("�Ҳ�������Ϊ[" + actionName + "]��Action��");
        PropxyActionInvoke pai = new PropxyActionInvoke(this.getActionBean(actionName), invoke);
        return pai;
    }
}