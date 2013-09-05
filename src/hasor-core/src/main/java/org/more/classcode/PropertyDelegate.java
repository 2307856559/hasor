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
 * ί�����ԣ�classcode�齨�ṩһ�����͵�������̬����ͳ��bean��get/set���������һ��˽���ֶΡ�
 * ��ί���������ǵ������get/set������һ���ӿ��С��Ӷ�����ӵ����Կ���ͨ���ӿڵ���Ӧ������������
 * @version 2010-9-3
 * @author ������ (zyc@hasor.net)
 */
public interface PropertyDelegate<T> {
    /**��ȡ���ί�����Ե��������͡�*/
    public Class<? extends T> getType();
    /**��ί�����Ե�get���������������������Ķ���*/
    public T get(Object target) throws PropertyException;
    /**��ί�����Ե�set��������һ�����������������Ķ��󣬵ڶ��������������õ���ֵ��*/
    public void set(Object target, T newValue) throws PropertyException;
}