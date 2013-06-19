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
package org.more.submit.ext.filter;
import org.more.submit.ActionStack;
/**
 * submit��actionִ�й�������ͬʱ���������Ҳ����action��������
 * @version 2009-11-28
 * @author ������ (zyc@byshell.org)
 */
public interface ActionFilter {
    /**
     * ��action��������֮����������doActionFilter�����ᱻ�Զ����á���������ִ����������ô�͵���chain������execute�����ȿɡ�
     * @param stack ������Ŀ�귽��ʱϣ����Ŀ�귽�����ݵ��¼�����
     * @param chain ��ִ�й�����ʱ�ò����������ھ����Ƿ����ִ�й�������
     * @return ����ִ�й�����֮���ִ�н����
     * @throws Throwable ��ִ��ʱ�����쳣
     */
    public Object doActionFilter(ActionStack stack, FilterChain chain) throws Throwable;
};