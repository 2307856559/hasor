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
/**
 * ���ฺ���ṩĿ����󷽷���ActionInvoke�ӿ���ʽ��
 * <br/>Date : 2009-12-1
 * @author ������
 */
class PropxyActionInvoke implements ActionInvoke {
    //========================================================================================Field
    /** �������Ŀ����� */
    private Object target = null;
    /** Ҫ���õ�Ŀ�귽������ */
    private String invoke = null;
    //==================================================================================Constructor
    public PropxyActionInvoke(Object target, String invoke) {
        this.target = target;
        this.invoke = invoke;
    }
    //==========================================================================================Job
    @Override
    public Object invoke(ActionStack stack) throws Throwable {
        Method method = this.target.getClass().getMethod(this.invoke, ActionStack.class);
        return method.invoke(target, stack);
    }
}