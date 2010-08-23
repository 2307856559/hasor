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
import java.lang.reflect.Method;
import java.util.Iterator;
/**
 * Action��Դ���������õ�������ɨ��ÿһ��action����ķ������Ե��������пɵ��õ�action invokeString��
 * @version : 2010-7-27
 * @author ������(zyc@byshell.org)
 */
class InvokeStringIterator implements Iterator<String> {
    private Iterator<String> actionNameIterator  = null;
    private ActionContext    actionContext       = null;
    //
    private String           currentActionName   = null;
    private Method[]         actionObjectMethods = null;
    private int              currentMethodIndex  = 0;
    /*----------------------------------------------*/
    public InvokeStringIterator(ActionContext actionContext) {
        this.actionContext = actionContext;
    };
    public boolean hasNext() {
        if (actionNameIterator.hasNext() == false && actionObjectMethods.length > currentMethodIndex)
            return false;
        else
            return true;
    };
    private Method readNextMethod_2() {
        if (this.actionObjectMethods == null || this.currentMethodIndex > actionObjectMethods.length)
            return null;
        this.currentMethodIndex++;
        return this.actionObjectMethods[this.currentMethodIndex];
    };
    private Method readNextMethod() {
        //1.���Զ�ȡһ��
        Method m = readNextMethod_2();
        if (m != null)
            return m;
        //2.������һ��Action
        if (this.actionNameIterator.hasNext() == true) {
            //����������ȡ���action�����з����б������÷���ָ��Ϊ-1��
            this.currentActionName = this.actionNameIterator.next();
            Class<?> type = this.actionContext.getActionType(this.currentActionName);
            this.actionObjectMethods = type.getMethods();
        } else {
            //����Ѿ����������һ��action���ƣ��������������ݡ�
            this.currentActionName = null;
            this.actionObjectMethods = null;
        }
        this.currentMethodIndex = -1;
        //4.�ٴγ���
        return readNextMethod_2();
    };
    public String next() {
        while (true) {
            Method m = this.readNextMethod();
            if (m == null)
                return null;
            if (m.getParameterTypes().length != 1)
                continue;
            if (ActionStack.class.isAssignableFrom(m.getParameterTypes()[0]) == false)
                continue;
            return this.currentActionName + m.getName();
        }
    };
    public void remove() {
        throw new UnsupportedOperationException("ActionInvokeStringIterator��֧�ָò�����");
    };
};