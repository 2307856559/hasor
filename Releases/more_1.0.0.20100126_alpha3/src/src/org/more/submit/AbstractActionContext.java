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
import java.util.Arrays;
import java.util.Iterator;
import org.more.CastException;
import org.more.NoDefinitionException;
import org.more.submit.annotation.Action;
import org.more.submit.annotation.ActionFilters;
import org.more.util.MergeIterator;
/**
 * {@link ActionContext ActionContext�ӿ�}�Ļ���ʵ�֣�������Ҫ����action����Ĵ�������������װ�䡢ͬʱaction���Ҳ�ɸ��ฺ��<br/>
 * �����ฺ���ṩ{@link ActionObjectFactory ActionObjectFactory�ӿ�}�����Լ���չ�������ܡ�
 * @version 2009-12-1
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractActionContext implements ActionContext {
    private ActionObjectFactory objectFactory;
    /**��ʼ��*/
    public void init() {
        this.initContext();
        this.objectFactory = this.createActionObjectFactory();
        if (this.objectFactory == null)
            throw new NullPointerException("createActionObjectFactory�������ܷ���null��");
    }
    /**��ʼ�������������ͨ����д�÷�������ɳ�ʼ�����̡�*/
    protected abstract void initContext();
    /**�÷�����������д���ҷ���ActionObjectFactory���Ͷ���*/
    protected abstract ActionObjectFactory createActionObjectFactory();
    /*----------------------------------------------------------------------------------���*/
    /**
     * 1.���ּ�⣬�׳�NoDefinitionException<br/>
     * �÷�����ҪΪ�˼���Ƿ���Բ��ҵ�name�����Ķ����������ͨ����д�÷���������ĳ�����Ƶ�action�Ƿ���ڡ�
     */
    protected boolean testActionName(String name) throws NoDefinitionException {
        return this.objectFactory.contains(name);
    };
    /**
     * 2.Action��Ǽ�⣬�׳�NoDefinitionException<br/>
     * �÷�����ҪΪ�˼��testActionName���Ķ����Ƿ���һ��action��testActionName��������ͬ��ȷ��ĳ�����ƵĶ���
     * �Ƿ���ڶ��޷�ȷ��������Ƿ�ΪAction��testActionMark������������ȷ��Ŀ�������һ��Action��
     */
    protected boolean testActionMark(String actionName) throws NoDefinitionException {
        return true;
    };
    /**3.�����⣬�׳�CastException�����������Ŀ�����Ϊnull�򲻻���ø÷�����*/
    protected boolean testActionObject(Object actionObject) throws CastException {
        return true;
    };
    @Override
    public boolean containsAction(String actionName) {
        //1.���ּ��
        if (this.testActionName(actionName) == false)
            return false;
        //2.Action��Ǽ��
        Class<?> actionType = this.getActionType(actionName);
        Action action = actionType.getAnnotation(Action.class);
        if (action == null || action.isAction() == false)
            if (this.testActionMark(actionName) == false)
                return false;
        return true;
    }
    @Override
    public ActionInvoke findAction(String actionName, String invoke) throws NoDefinitionException, CastException {
        if (containsAction(actionName) == false)
            throw new NoDefinitionException("�Ҳ�������Ϊ[" + actionName + "]��Action��");
        //3.������ CastException
        Object actionObject = this.objectFactory.findObject(actionName);
        if (actionObject == null)
            throw new NullPointerException("����Action[" + actionName + "]������Ϊnull��");
        else if (this.testActionObject(actionObject) == false)
            throw new CastException("[" + actionName + "]����һ����Ч�ĵ�Action����");
        //���ض���
        return new PropxyActionInvoke(actionObject, invoke);
    }
    /*----------------------------------------------------------------------------------������*/
    /**���ù�����*/
    private ActionInvoke configFilter(Iterator<String> filterNameIterator, ActionInvoke invokeObject) {
        FilterChain chain = new FilterChain(invokeObject);
        while (filterNameIterator.hasNext() == true)
            //���ӹ�����
            chain = new FilterChain(chain, this.objectFactory.getActionFilter(filterNameIterator.next()));
        //����װ����
        return new FilterActionInvoke(chain);
    }
    @Override
    @SuppressWarnings("unchecked")
    public ActionInvoke configPrivateFilter(String actionName, ActionInvoke invokeObject) {
        Class<?> actionType = this.getActionType(actionName);
        //����ActionFiltersע��
        ActionFilters filters = actionType.getAnnotation(ActionFilters.class);
        if (filters == null)
            return this.configFilter(this.objectFactory.getPrivateFilterNames(actionName), invokeObject);
        else {
            //��һ�������������ڵ���ActionFiltersע������
            Iterator<String> first = Arrays.asList(filters.value()).iterator();
            //�ڶ��������������ڵ���getPrivateFilterNames����
            Iterator<String> second = this.objectFactory.getPrivateFilterNames(actionName);
            //�ϲ�����������װ�������
            return this.configFilter(new MergeIterator(first, second), invokeObject);
        }
    }
    @Override
    public ActionInvoke configPublicFilter(String actionName, ActionInvoke invokeObject) {
        //��ȡ���й��������Ƶ�����������װ��������й�������
        return this.configFilter(this.objectFactory.getPublicFilterNames(actionName), invokeObject);
    }
    @Override
    public Class<?> getActionType(String actionName) {
        return this.objectFactory.getObjectType(actionName);
    }
    @Override
    public Iterator<String> getActionNameIterator() {
        //ʹ��ActionNameIterator�������ڵ��������й��˲���action�����ơ�
        return new ActionNameIterator(this.objectFactory.getObjectNameIterator(), this);
    }
    @Override
    public Iterator<String> getActionInvokeStringIterator() {
        /*
         * ActionInvokeStringIterator���ֱ��ٴε���ÿһ����getActionNameIterator��������ʾ��Action������
         * ����������п��Ե��õ�invokeString
         */
        return new InvokeStringIterator(this.getActionNameIterator(), this.objectFactory);
    }
}