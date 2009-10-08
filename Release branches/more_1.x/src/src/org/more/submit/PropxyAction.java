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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.more.InvokeException;
/**
 * �����Ǵ���Action��Ĵ����ࡣ����Ĺ���������һ������ͨ��name�����������ĸ������Ĵ������
 * ע��PropxyAction�಻����Ϊ����Ŀ���ࡣ����ʹ��getFinalTarget����ʱ���᷵������Ŀ��
 * PropxyAction���target������������Ŀ��PropxyAction�ࡣ������Գ���PropxyAction�����ࡣ
 * Date : 2009-6-26
 * @author ������
 */
@SuppressWarnings("unchecked")
public class PropxyAction {
    /** ��ǰ����Action������ */
    private String       name              = null;
    /** �������Ŀ��action���� */
    protected Object     target            = null;
    /** Ҫ�����õ�Ŀ����󷽷�����ǩ�������б� */
    private Class[]      invokeParamsTypes = new Class[] { ActionMethodEvent.class };
    public static String DefaultMethodName = "execute";
    /**
     * �÷����������Ƿ������ʽ����Ŀ��action�ķ�����
     * @param methodName Ҫ���õķ��䷽����
     * @param event ����ķ�������
     * @return ���ص���Ŀ�����ʱ�ķ���ֵ
     * @throws InvokeException �ڷ��������ڼ䷢���쳣��
     */
    public Object execute(String methodName, ActionMethodEvent event) throws InvokeException {
        String method = methodName;
        Method defaultMethod = null;//Ĭ�Ϸ���
        Method invokeMethod = null;//׼�����õķ���
        //������Ʋ��Ϸ���ֱ�ӽ����Ƹ���ΪĬ�Ϸ���������
        if (methodName == null || methodName.equals("") || methodName.equals("execute") == true)
            method = PropxyAction.DefaultMethodName;
        //������÷���
        Method[] ms = this.getFinalTarget().getClass().getMethods();
        for (Method m : ms) {
            //1.Ŀ�귽�������б������types�ֶ��д�ŵĸ�����һ���ĺ��ԡ�
            Class[] paramTypes = m.getParameterTypes();
            if (paramTypes.length != this.invokeParamsTypes.length)
                continue;
            //2.����в������Ͳ�һ����Ҳ����---1
            boolean isFind = true;
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i].isAssignableFrom(this.invokeParamsTypes[i]) == false) {
                    isFind = false;
                    break;
                }
            }
            //---2.����в������Ͳ�һ����Ҳ����---2
            if (isFind == false)
                continue;
            //3.������������Ĭ�Ϸ�������ΪĬ�Ϸ�����
            if (m.getName().equals(PropxyAction.DefaultMethodName) == true)
                defaultMethod = m;
            //4.���ֲ���ȵĺ���
            if (m.getName().equals(method) == false)
                continue;
            //��������ִ�е���
            invokeMethod = m;
        }
        //����Ĭ�Ϸ���
        if (invokeMethod == null && defaultMethod == null)
            //���÷�����Ĭ�Ϸ�����Ϊ��
            throw new InvokeException("�޷�����Ŀ�귽��[" + method + "]����Ĭ�Ϸ���[" + PropxyAction.DefaultMethodName + "]Ҳ�޷����ã�");
        else if (invokeMethod == null)
            //���÷���Ϊ��
            return this.invoke(defaultMethod, event);
        else
            //��������
            return this.invoke(invokeMethod, event);
    }
    private Object invoke(Method m, ActionMethodEvent event) throws InvokeException {
        try {
            event.setInvokeMethod(m.getName());//ʵ�ʵ��õķ�������
            return m.invoke(this.target, event);
        } catch (Exception e) {
            Throwable ee = (e instanceof InvocationTargetException) ? e.getCause() : e;
            if (ee instanceof InvokeException)
                throw (InvokeException) ee;
            else
                throw new InvokeException(ee);
        }
    }
    /**
     * ��ñ������Ŀ����� 
     * @return ���ر������Ŀ��Action����
     */
    public Object getTarget() {
        return target;
    }
    /**
     * ��ñ����������Ŀ��Action���� 
     * @return ���ر����������Ŀ��Action����
     */
    public Object getFinalTarget() {
        Object obj = this.getTarget();
        while (true)
            if (obj == null)
                return null;
            else if (obj instanceof PropxyAction)
                obj = ((PropxyAction) obj).getTarget();
            else
                return obj;
    }
    /**
     * ���Action����
     * @return ����Action����
     */
    public String getName() {
        return name;
    }
    void setTarget(Object target) {
        this.target = target;
    }
    void setName(String name) {
        this.name = name;
    }
}
