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
package org.more.submit.acs.simple;
import java.lang.reflect.Method;
import org.more.core.classcode.EngineToos;
import org.more.core.error.InvokeException;
import org.more.core.error.LoadException;
import org.more.submit.ActionInvoke;
import org.more.submit.ActionStack;
import org.more.submit.impl.AbstractAC;
/**
 * �򵥵�ACʵ�֣���AC��ֱ�Ӵ���class����
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public class AC_Simple extends AbstractAC {
    public ClassLoader classLoader = null;
    //
    public ActionInvoke findAction(String name, String userInfo) throws Throwable {
        name = name.substring(0, name.indexOf("("));
        String classType = EngineToos.splitPackageName(name);
        String methodName = EngineToos.splitSimpleName(name);
        if (this.classLoader == null)
            this.classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> type = this.classLoader.loadClass(classType);
        //
        Object obj = this.getBean(type);
        if (obj == null)
            throw new LoadException("װ��action�����쳣��");
        return new DefaultActionInvoke(obj, methodName);
    };
    /**����������ָ���Ķ���*/
    protected Object getBean(Class<?> type) throws Throwable {
        return type.newInstance();
    };
};
class DefaultActionInvoke implements ActionInvoke {
    private Object target = null;
    private String method = null;
    public DefaultActionInvoke(Object target, String method) {
        this.target = target;
        this.method = method;
    }
    public Object invoke(ActionStack stack) throws Throwable {
        Class<?> type = this.target.getClass();
        Method[] m = type.getMethods();
        Method method = null;
        for (int i = 0; i < m.length; i++) {
            if (m[i].getName().equals(this.method) == false)
                continue; //���Ʋ�һ�º���
            if (m[i].getParameterTypes().length != 1)
                continue; //�������Ȳ�һ�º���
            if (ActionStack.class.isAssignableFrom(m[i].getParameterTypes()[0]) == true) {
                method = m[i];//��������
                break;
            }
        }
        if (method == null)//����Ҳ��������������쳣
            throw new InvokeException("�޷�ִ��[" + this.method + "]���Ҳ���ƥ��ķ�����");
        return method.invoke(target, stack);
    }
}