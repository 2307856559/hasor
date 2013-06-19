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
package org.more.workflow.runtime;
import org.more.workflow.context.RunContext;
import org.more.workflow.util.Config;
/**
 * ���̽ڵ�����ʱ�ӿڡ���ϵͳ�����нڵ��ִ�ж���Ҫͨ�����յ�����ʱ��ʵ�֣���More WorkFlowϵͳ�нڵ㸺�����ҵ��
 * ������ʱ����ҵ������ִ�С�����ҵ����Կ�����һ���ⲿӦ�ó�����ã�Ҳ������ҵ���߼�����Ҳ��������Ϣ�ķ��͡�<br/>
 * ÿ�ֲ������͵�ҵ�񶼶�Ӧ������һ����֮ƥ�������ʱ���������ʱ��������һ��ҵ�������֧�ţ��ȷ�˵Java����ҵ���
 * ����ʱ���Ǹ������Java���롣��Email����ʱ���Ǹ����շ�Email�ʼ��ġ�<br/>
 * ��ʾ��init��destroy������ϵͳ�̸߳�����ã�beforeRun,doRun,afterRun���������ĵ��ÿ�������ǰ����������ʹ�õ��̲߳�ͬ��
 * Date : 2010-6-14
 * @author ������
 */
public interface Runtime {
    /**������ʱ������֮��÷����ͻᱻ���ã��÷����������ڳ�ʼ������ʱ��ͨ���������Ի�ȡ�����ò����Լ�ϵͳ�����Ķ���*/
    public void init(Config config) throws Throwable;
    /**doRun��ִ��֮ǰ���ã��÷�����������׼������ʱ����֮ǰ��һЩ��Դ����ͬʱ�÷���Ҳ���ڽڵ��process��������֮ǰ���е��á�*/
    public void beforeRun(Config param, RunContext runContext) throws Throwable;
    /**���ڵ��process����������ʱ���ڵ���Զ����ø÷�������ִ�нڵ�ҵ���߼������ڲ��õ�runtime�������ͬ��ִ�ж�����*/
    public Object doRun(Config param, RunContext runContext) throws Throwable;
    /**doRun��ִ��֮����ã����ø÷������Ի��ս���doRun��һЩ��������ͬʱbeforeRun�������ԴҲ������������ա�ͬʱ�÷���Ҳ���ڽڵ��process��������֮����е��á�*/
    public void afterRun(Config param, RunContext runContext) throws Throwable;
    /**��û���κνڵ�ʹ�ø�runtimeʱ���ܻᱻϵͳ�����գ�ֻ��runtime������ʱ�Ż���ȸ÷�����*/
    public void destroy();
};