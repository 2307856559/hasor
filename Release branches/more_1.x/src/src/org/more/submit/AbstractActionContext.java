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
import org.more.CastException;
import org.more.FormatException;
import org.more.NoDefinitionException;
import org.more.submit.annotation.Action;
/**
 * ActionContext�ӿڵĻ���ʵ�֣������ฺ���ṩ����action�����Լ�action����Ĺ����������ȡ��
 * ��ϸ���ܲο�{@link ActionContext ActionContext�ӿ�}
 * @version 2009-12-1
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractActionContext implements ActionContext {
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
    public ActionInvoke findAction(String actionName, String invoke) throws NoDefinitionException, FormatException, CastException {
        //1.���ּ�� NoDefinitionException
        if (this.testActionName(actionName) == false)
            throw new NoDefinitionException("�Ҳ�������Ϊ[" + actionName + "]��Action��");
        //2.Action��Ǽ�� NoDefinitionException
        Class<?> actionType = this.getActionType(actionName);
        Action action = actionType.getAnnotation(Action.class);
        if (action == null || action.isAction() == false)
            if (this.testActionMark(actionName) == false)
                throw new NoDefinitionException("�Ҳ�������Ϊ[" + actionName + "]��Action��");
        //3.���ͼ�� FormatException
        if (this.testActionType(actionType) == false)
            throw new FormatException("[" + actionName + "]����һ����Ч�ĵ�Action���͡�");
        //4.������ CastException
        Object actionObject = this.getActionBean(actionName);
        if (actionObject == null)
            throw new NullPointerException("����Action[" + actionName + "]������Ϊnull��");
        else if (this.testActionObject(actionObject) == false)
            throw new CastException("[" + actionName + "]����һ����Ч�ĵ�Action����");
        //���ض���
        return new PropxyActionInvoke(actionObject, invoke);
    }
    /**
     * 1.���ּ�⣬�׳�NoDefinitionException<br/>
     * �÷�����ҪΪ�˼���Ƿ���Բ��ҵ�name�����Ķ���
     */
    protected boolean testActionName(String name) throws NoDefinitionException {
        return true;
    };
    /**
     * 2.Action��Ǽ�⣬�׳�NoDefinitionException<br/>
     * �÷�����ҪΪ�˼��testActionName���Ķ����Ƿ���һ��action������action����Ӧ���б�ǵ�������ע�⻹�������ļ���
     */
    protected boolean testActionMark(String actionName) throws NoDefinitionException {
        return true;
    };
    /**3.���ͼ�⣬�׳�FormatException����Ŀ������������Actionע�Ⲣ��û��ָ��isActionע������Ϊfalseʱ���÷��������ᱻ���á�*/
    protected boolean testActionType(Class<?> actionType) throws FormatException {
        return true;
    };
    /**4.�����⣬�׳�CastException�����������Ŀ�����Ϊnull�򲻻���ø÷�����*/
    protected boolean testActionObject(Object actionObject) throws CastException {
        return true;
    };
    /**��ȡָ������action����*/
    protected abstract Object getActionBean(String actionName);
    /**��ȡָ������action��˽�й������������顣*/
    protected abstract ActionFilter[] getPrivateFilterBean(String actionName);
    /**��ȡָ������action�Ĺ��й������������顣*/
    protected abstract ActionFilter[] getPublicFilterBean(String actionName);
}