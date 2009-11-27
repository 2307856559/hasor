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
* �������������ɵ�����������AOP������������ø����doInvoke�Դ����������ö�����
* ͨ��doInvoke��������AOP���������ĵ��á�
* <br/><br/>�ڿ��������п�����Ա������������ʵ������
* Date : 2009-10-30
* @author ������
*/
public class ImplAOPFilterChain implements AOPFilterChain {
    private AOPInvokeFilter thisFilter      = null; //��ʾ���������ĵ�ǰ��������
    private AOPFilterChain  nextFilterChain = null; //������������һ����������
    /** */
    public ImplAOPFilterChain(AOPInvokeFilter thisFilter, AOPFilterChain nextFilterChain) {
        this.thisFilter = thisFilter;
        this.nextFilterChain = nextFilterChain;
    }
    @Override
    public Object doInvokeFilter(Object target, AOPMethods methods, Object[] args) throws Throwable {
        if (this.nextFilterChain != null)
            return this.thisFilter.doFilter(target, methods, args, this.nextFilterChain);
        else
            return methods.getMethod().invoke(target, args);
    }
    /** �÷��������ɵ����з����ܵ�AOP����ʱ����á� */
    public Object doInvoke(Object target, String methodDesc, Object[] args) {
        try {
            ClassEngine ce = (ClassEngine) target.getClass().getClassLoader();
            return this.thisFilter.doFilter(target, ce.aopMethods.get(methodDesc), args, this.nextFilterChain);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}