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
package org.more.core.task.task;
import org.more.core.task.Task;
/**
 * �ȴ����񣬸�����ִ��ʱ���Զ�����Thread.sleep������˯�ߵ�ǰ�̡߳�
 * �����������ǿ��������Ż�CPU��Դ�Ϳ�������ִ�м����
 * Date : 2009-5-15
 * @author ������
 */
public class WaitTask extends Task {
    /**  */
    private static final long serialVersionUID = 8058549358582784372L;
    /** ��ǰ�ȴ�����ִ��ʱ�ĵȴ�ʱ�� */
    private int               wait             = 50;
    /** �����ȴ�����Ĭ����������50���롣 */
    public WaitTask() {}
    /**
     * �����ȴ�����
     * @param wait
     */
    public WaitTask(int wait) {
        this.wait = wait;
    }
    /** ִ�еȴ� */
    protected void doRun() throws Exception {
        this.log.debug("Task doRun... wait " + this.wait);
        Thread.sleep(this.wait);
    }
    /**
     * ��õȴ�������ĵȴ�ʱ����������Ϊ��λ��
     * @return ��õȴ�������ĵȴ�ʱ����������Ϊ��λ��
     */
    public int getWait() {
        return wait;
    }
    /**
     * ���õȴ�������ĵȴ�ʱ����������Ϊ��λ��
     * @param wait Ҫ���õĵȴ�ʱ��
     * @throws IllegalArgumentException ����ͼ����һ��С��0����ֵʱ�������쳣��
     */
    public void setWait(int wait) throws IllegalArgumentException {
        if (wait < 0)
            throw new IllegalArgumentException();
        else
            this.wait = wait;
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
