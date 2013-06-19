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
* �������������һ�����ڽӿڣ���{@link AopInvokeFilter}�ӿڵ�doInvokeFilter������ִ��ʱ{@link AopFilterChain}���Ͳ�������
* ��ʾ�������������������������һ����������Դ�����ӿڵ�doInvokeFilter������ʾִ�й�������Դ��
* ��һ����������Դ�����ǹ�����������һ��������Ԫ��Ҳ��������ʵ����Դ����������classcode������Դ����Ŀ�귽����
* @version 2009-10-30
* @author ������ (zyc@byshell.org)
*/
public interface AopFilterChain {
    /**
     * ִ�й�������һ����������Դ�ķ�����ͨ���÷����������ù��������Ķ�����
     * @param target ִ�з����Ķ���
     * @param method �����õķ����ö����а����˱����õĴ�������ԭʼ������
     * @param args ���÷��������ݵĲ�����
     * @return ����ִ�н����
     * @throws Throwable ִ�й�������Դʱ�����쳣��
     */
    public Object doInvokeFilter(Object target, Method method, Object[] args) throws Throwable;
}