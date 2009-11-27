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
package org.more.core.classcode;
/**
 * �������д��������Method���Ͷ��󣬷ֱ���AOP�������ͱ�����ķ�����������Ա���ؿ��Ǵ���ϸ�ڡ�
 * ���������𴥷�AOP�����������������������ԭʼ�������÷����п��ܰ�����ע�ⷽ����Ϣ��
 * �ö����ǵ�����AOP����ʱ��̬���Զ����������͡�
 * Date : 2009-10-19
 * @author ������
 */
public class AOPMethods {
    //========================================================================================Field
    /** ԭʼ������ԭʼ�����а�������Ч��ע����Ϣ�� */
    private final java.lang.reflect.Method method;
    /** �����������ִ�д�������invoke������ֵݹ���á� */
    private final java.lang.reflect.Method propxyMethod;
    //==================================================================================Constructor
    /** ����AOPMethods���󡣵�һ��������ԭʼ��������ڶ��������Ǵ��������� */
    public AOPMethods(java.lang.reflect.Method method, java.lang.reflect.Method propxyMethod) {
        this.method = method;
        this.propxyMethod = propxyMethod;
    }
    //==========================================================================================JOB
    public java.lang.reflect.Method getMethod() {
        return method;
    }
    public java.lang.reflect.Method getPropxyMethod() {
        return propxyMethod;
    }
}