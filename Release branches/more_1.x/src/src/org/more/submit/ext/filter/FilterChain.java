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
import org.more.submit.ActionInvoke;
import org.more.submit.ActionStack;
/**
 * action�������������࣬ʹ�ø���ķ�������ִ��action������������һ������ֱ�����յ�Action���á�
 * @version 2009-11-27
 * @author ������ (zyc@byshell.org)
 */
public class FilterChain {
    //========================================================================================Field
    private ActionFilter thisFilter      = null; //��ʾ���������ĵ�ǰ��������
    private FilterChain  nextFilterChain = null; //������������һ����������
    private ActionInvoke targetInvoke    = null; //�����������ն���
    //==================================================================================Constructor
    /**�������������˵�һ��֮��������㡣*/
    FilterChain(FilterChain nextFilterChain, ActionFilter thisFilter) {
        this.thisFilter = thisFilter;
        this.nextFilterChain = nextFilterChain;
    };
    /**������������һ��*/
    FilterChain(ActionInvoke targetInvoke) {
        this.targetInvoke = targetInvoke;
    };
    //==========================================================================================Job
    /**
     * ִ����һ��Action�������Ļ��ڲ��ҷ���ִ�н�������ִ���ڼ䷢���쳣������Throwable�����쳣��
     * @return ����action��ִ��֮��Ľ������
     * @throws Throwable ��������쳣��
     */
    public Object doInvokeFilter(ActionStack stack) throws Throwable {
        if (this.nextFilterChain != null)
            return this.thisFilter.doActionFilter(stack, nextFilterChain);//�����action���������м��һ��������ִ����һ�����ڡ�
        else
            return this.targetInvoke.invoke(stack);//����ǹ������������һ��������ִ��Ŀ��action������
    };
};