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
package org.more.task.task;
import org.more.task.Task;
/**
 * �����ִ�е����񣬸��������в����������񴴽��ö�����Խ��κ�����Task���ͻ���Runnable�ӿڵ�
 * ���������������Task����������һ��ӵ�������񼯺ϵ�������ô��Щ�����񽫻���Ϊһ������������ִ�С�
 * ��ʾ��Task�����Ѿ�ʵ����Runnable�ӿ����TaskҲ���Կ�����Runnable�ӿڶ���
 * ͨ��ItemTask���񲻿��Ի���й�TaskLocation�ӿڵĹ���֧�֡�
 * Date : 2009-5-15
 * @author ������
 */
public class ItemTask extends Task {
    /**  */
    private static final long serialVersionUID = 1573066209349130377L;
    /** ����Ŀ�� */
    private Runnable          runnable         = null;
    /**
     * ������ִ�е������������������һ���ض���ִ������
     * @param runnable ׼��ִ�е�Ŀ������
     */
    public ItemTask(Runnable runnable) {
        this.runnable = runnable;
    }
    /** ִ�е���Ŀ���run������ */
    @Override
    protected void doRun() throws Exception {
        this.runnable.run();
    }
    /**
     * ���ItemTask��ִ�з������÷�����ʵ����Runnable�ӿڵ��������
     * @return ����ItemTask��ִ�з�����
     */
    public Runnable getRunnable() {
        return runnable;
    }
    /**
     * ��õ�ǰ����������ִ�е�������󣬷����Լ�(this)
     * @return ��õ�ǰ����������ִ�е�������󣬷����Լ�(this)
     */
    @Override
    public Task getCurrent() {
        return this;
    }
}