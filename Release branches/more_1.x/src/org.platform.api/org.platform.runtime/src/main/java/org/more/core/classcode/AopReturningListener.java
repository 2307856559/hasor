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
* Aop��returning���棬���յ�returning������¼�֪ͨʱ���Զ����øýӿڡ��ýӿڷ�����������aop���ĵ�һ�����ڷ������á���������ͼ��Returning��������ӿڵĹ����㡣
* ��������returning�ǵ�filter_start�������֮���ڽ��еġ����returning����Ϣ����{@link AopInvokeFilter}������������֮������Ҳ�ܵ�����������Ӱ�졣
* <br/><img width="400" src="doc-files/classcode_struct.png"/>
* @version 2010-9-2
* @author ������ (zyc@byshell.org)
*/
public interface AopReturningListener {
    /**
     * ���ڽ���returning������¼��ķ�����
     * @param target �����õĶ���
     * @param method �����õķ�����
     * @param args ��������������ݵĲ�����
     * @param result �ò�����ִ�е���֮��ķ�������ֵ��
     */
    public void returningInvoke(Object target, Method method, Object[] args, Object result) throws Throwable;
}