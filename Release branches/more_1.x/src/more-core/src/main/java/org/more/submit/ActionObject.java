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
import java.util.Map;
/**
 * �ýӿ���һ���ɵ��õ�action����ͨ���ӿڷ������Զ�action���е��á�
 * @version : 2011-7-14
 * @author ������ (zyc@byshell.org)
 */
public interface ActionObject {
    /**��ȡaction���������ռ䡣*/
    public String getNameSpace();
    /**��ȡaction�ַ�����*/
    public String getActionString();
    /**ִ��action�����ҷ���ִ�н����*/
    public Object doAction(Object... objects) throws Throwable;
    /**ִ��action�����ҷ���ִ�н����*/
    public Object doAction(Map<String, ?> params) throws Throwable;
    /**ִ��action�����ҷ���ִ�н�����µ�{@link ActionStack}������ڲ�������ʾ��{@link ActionStack}֮�ϡ�*/
    public Object doAction(ActionStack stack, Object... objects) throws Throwable;
    /**ִ��action�����ҷ���ִ�н�����µ�{@link ActionStack}������ڲ�������ʾ��{@link ActionStack}֮�ϡ�*/
    public Object doAction(ActionStack stack, Map<String, ?> params) throws Throwable;
};