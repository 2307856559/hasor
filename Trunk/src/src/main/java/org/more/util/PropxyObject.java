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
package org.more.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import org.more.core.error.InvokeException;
/**
 * ����������ö���ͨ�������ṩ�ķ�������ʹ����������java�ķ�����ơ�ֱ�������Ĳ���ջ���������
 * Ȼ��ֱ�ӵ���ָ��������ɵ��á�������ͨ��getResult�������ط���ֵ��������һ��������
 * @version 2009-7-11
 * @author ������ (zyc@byshell.org)
 */
public class PropxyObject {
    /** ���׼�����÷���ʱ���ݵĲ����б����ݣ���˳�� */
    private ArrayList<Object> list       = new ArrayList<Object>();
    /** �������Ŀ����� */
    private Object            target     = null;
    private Class<?>          targetType = null;
    /** �������õķ���ֵ */
    private Object            result     = null;
    /**
     * ����MRMI������ö���ͨ�������ṩ�ķ�������ʹ����������java�ķ�����ǡ�
     * ֱ�������Ĳ���ջ���������Ȼ��ֱ�ӵ���ָ��������ɵ��á�������ͨ��getResult�������ط���ֵ��
     */
    public PropxyObject(Object target) {
        this.target = target;
        this.targetType = target.getClass();
    }
    /**
     * ����MRMI������ö���ͨ�������ṩ�ķ�������ʹ����������java�ķ�����ǡ�
     * ֱ�������Ĳ���ջ���������Ȼ��ֱ�ӵ���ָ��������ɵ��á�������ͨ��getResult�������ط���ֵ��
     */
    public PropxyObject(Class<?> target) {
        this.targetType = target;
    }
    /**
     * �÷����������Ƿ������ʽ����Ŀ��ķ�����
     * @param methodName Ҫ���õķ��䷽������
     * @throws InvokeException �ڷ��������ڼ䷢���쳣�� 
     */
    public Object invokeMethod(String methodName) throws InvokeException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method invokeMethod = null;
        //������÷���
        Method[] ms = this.targetType.getMethods();
        for (Method m : ms) {
            //1.���ֲ���ȵĺ���
            if (m.getName().equals(methodName) == false)
                continue;
            //2.Ŀ�귽�������б������types�ֶ��д�ŵĸ�����һ���ĺ��ԡ�
            Class<?>[] paramTypes = m.getParameterTypes();
            if (paramTypes.length != this.list.size())
                continue;
            //3.����в������Ͳ�һ����Ҳ����---1
            boolean isFind = true;
            for (int i = 0; i < paramTypes.length; i++) {
                Object param_object = this.list.get(i);
                if (param_object == null)
                    continue;
                //
                if (paramTypes[i].isAssignableFrom(param_object.getClass()) == false) {
                    isFind = false;
                    break;
                }
            }
            //5.����в������Ͳ�һ����Ҳ����---2
            if (isFind == false)
                continue;
            //��������ִ�е���
            invokeMethod = m;
        }
        //
        if (invokeMethod == null)
            //���÷���Ϊ��
            throw new InvokeException("�޷�����Ŀ�귽��[" + methodName + "]��");
        else {
            //��������
            if (this.target == null) {
                //�ж��Ƿ�Ϊ��̬��
                int mm = invokeMethod.getModifiers();
                if ((mm | Modifier.STATIC) != mm)
                    throw new InvokeException("Ŀ�귽��[" + invokeMethod + "]�����Ǿ�̬�������ܵ��á�");
                this.result = invokeMethod.invoke(null, this.list.toArray());
            } else
                this.result = invokeMethod.invoke(this.target, this.list.toArray());
        }
        return this.result;
    }
    public Object getResult() {
        return this.result;
    }
    public void put(Object value) {
        list.add(value);
    }
    public void clear() {
        list.clear();
    }
    public Object getTarget() {
        return target;
    }
}