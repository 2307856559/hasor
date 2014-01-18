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
package net.hasor.core;
/**
 * Hasorģ�飬�÷����ж�����ģ���������ڡ�
 * @version : 2013-3-20
 * @author ������ (zyc@hasor.net)
 */
public interface Module {
    /**��ʼ�����̣�ע�⣺apiBinder ����ֻ���� init �׶���ʹ�á�*/
    public void init(ApiBinder apiBinder) throws Throwable;
    /**�����ź�*/
    public void start(AppContext appContext) throws Throwable;
    /**ֹͣ�ź�*/
    public void stop(AppContext appContext) throws Throwable;
}