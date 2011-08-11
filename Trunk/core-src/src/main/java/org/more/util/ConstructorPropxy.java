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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.more.core.error.InvokeException;
/**
 * ����������ö���ͨ�������ṩ�ķ�������ʹ����������java�ķ�����ơ�ֱ�������Ĳ���ջ���������
 * Ȼ��ֱ�ӵ���ָ��������ɵ��á�������ͨ��getResult�������ط���ֵ��������һ��������
 * @version 2009-7-11
 * @author ������ (zyc@byshell.org)
 */
public class ConstructorPropxy {
    /** ���׼�����÷���ʱ���ݵĲ����б����ݣ���˳�� */
    private ArrayList<Object> list       = new ArrayList<Object>();
    /** �������Ŀ����� */
    private Class<?>          targetType = null;
    /** �������õķ���ֵ */
    private Object            result     = null;
    /**
     * ����MRMI������ö���ͨ�������ṩ�ķ�������ʹ����������java�ķ�����ǡ�
     * ֱ�������Ĳ���ջ���������Ȼ��ֱ�ӵ���ָ��������ɵ��á�������ͨ��getResult�������ط���ֵ��
     */
    public ConstructorPropxy(Class<?> target) {
        this.targetType = target;
    }
    /**
     * �÷����������Ƿ������ʽ����Ŀ��Ĺ��췽����
     * @throws InvokeException �ڷ��������ڼ䷢���쳣��  
     */
    public Object newInstance() throws InvokeException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> invokeConstructor = null;
        //������÷���
        Constructor<?>[] cs = this.targetType.getConstructors();
        for (Constructor<?> c : cs) {
            Class<?>[] cparams = c.getParameterTypes();
            if (cparams.length != list.size())
                continue;//�������Ȳ�һ�£��ų�
            //3.����в������Ͳ�һ����Ҳ����---1
            boolean isFind = true;
            for (int i = 0; i < cparams.length; i++) {
                Object param_object = list.get(i);
                if (param_object == null)
                    continue;
                //
                if (this.isAssignableFrom(cparams[i], param_object) == false) {
                    try {
                        //���Խ�Ŀ������ת��Ϊ����Ҫ�ĸ�ʽ
                        Object obj = StringConvertUtil.changeType(param_object, cparams[i]);
                        list.set(i, obj);
                        continue;
                    } catch (Exception e) {}
                    isFind = false;
                    break;
                }
            }
            //5.����в������Ͳ�һ����Ҳ����---2
            if (isFind == false)
                continue;
            //��������ִ�е���
            invokeConstructor = c;
        }
        //
        if (invokeConstructor == null)
            //���÷���Ϊ��
            throw new InvokeException("�޷�����Ŀ�깹�췽����");
        else
            //��������
            this.result = invokeConstructor.newInstance(this.list.toArray());
        return this.result;
    }
    private boolean isAssignableFrom(Class<?> type, Object obj) {
        if (type.isPrimitive() == false)
            return type.isAssignableFrom(obj.getClass());
        //
        Class<?> type2 = obj.getClass();
        if (type == Boolean.TYPE && type2 == Boolean.class)
            return true;
        if (type == Byte.TYPE && type2 == Byte.class)
            return true;
        if (type == Short.TYPE && type2 == Short.class)
            return true;
        if (type == Integer.TYPE && type2 == Integer.class)
            return true;
        if (type == Long.TYPE && type2 == Long.class)
            return true;
        if (type == Float.TYPE && type2 == Float.class)
            return true;
        if (type == Double.TYPE && type2 == Double.class)
            return true;
        if (type == Character.TYPE && type2 == Character.class)
            return true;
        return false;
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
}