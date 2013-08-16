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
package org.hasor.context;
/**
 * �첽�¼��ص��ӿڡ�
 * @version : 2013-4-12
 * @author ������ (zyc@hasor.net)
 */
public interface AsyncCallBackHook {
    /**��ִ���¼������������쳣ʱ���ø÷�����*/
    public void handleException(String eventType, Object[] objects, Throwable e);
    /**������첽�¼�����ʱ�ص���<p>
     * ע�⣺�������첽�¼��ַ�������{@link #handleException(String, Object[], Throwable)}�����Ƿ񱻵��ã��÷����������ڵı�ִ�С�*/
    public void handleComplete(String eventType, Object[] objects);
}