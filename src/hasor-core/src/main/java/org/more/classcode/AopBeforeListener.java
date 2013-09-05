/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.more.classcode;
/**
* Aop��before���棬���յ�before������¼�֪ͨʱ���Զ����øýӿڡ��ýӿڷ�����������aop���ĵ�һ�����ڷ������á���������ͼ��Before��������ӿڵĹ����㡣
* <br/><img width="400" src="doc-files/classcode_struct.png"/>
* @version 2010-9-2
* @author ������ (zyc@hasor.net)
*/
public interface AopBeforeListener {
    /**
     * ���ڽ���before������¼��ķ�����
     * @param target �����õĶ���
     * @param method �����õķ�����
     * @param args ��������������ݵĲ�����
     */
    public void beforeInvoke(final Object target, final Method method, final Object[] args) throws Throwable;
}