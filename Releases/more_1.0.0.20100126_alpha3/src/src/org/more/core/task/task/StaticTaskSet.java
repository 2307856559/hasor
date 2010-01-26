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
import java.util.LinkedList;
import java.util.List;
import org.more.core.task.LocationTask;
import org.more.core.task.Task;
import org.more.core.task.TaskState;
/**
 * ������������������ListTask�������񡣸���������Ĵ���Ŀ����Ϊ��ʹ������԰�������������
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class StaticTaskSet extends LocationTask {
    /**  */
    private static final long serialVersionUID = -6342672748568848797L;
    /** ��������񼯺� */
    private List<Task>        list             = new LinkedList<Task>();
    /** ��ǰ����ִ�е������� */
    private Task              currentTaskCache = null;
    /**
     * ���Ҫִ�е�����
     * @param item �µ�ִ������
     * @throws UnsupportedOperationException ����������ִ��ʱ���ø÷��������׳����쳣��
     */
    public void addTaskItem(Task item) throws UnsupportedOperationException {
        this.log.debug("addTaskItem Task name=" + item.getName());
        if (this.getState() == TaskState.Run)
            throw new UnsupportedOperationException("���������в�������ø÷�����");
        this.list.add(item);
        super.setCountTask(this.list.size());
    }
    /**
     * ���Ҫִ�е�����
     * @param item �µ�ִ������
     * @throws UnsupportedOperationException ����������ִ��ʱ���ø÷��������׳����쳣��
     */
    public void addTaskItem(Runnable item) throws UnsupportedOperationException {
        this.log.debug("addTaskItem Runnable");
        this.addTaskItem(new ItemTask(item));
    }
    /**
     * ���Ҫִ�е�����
     * @param item �µ�ִ������
     * @throws UnsupportedOperationException ����������ִ��ʱ���ø÷��������׳����쳣��
     */
    public void addTaskItem(List<? extends Runnable> item) throws UnsupportedOperationException {
        this.log.debug("addTaskItem list size=" + item.size());
        for (Runnable i : item)
            if (i instanceof Task)
                this.addTaskItem((Task) i);
            else
                this.addTaskItem(i);
    }
    @Override
    protected void doRun() throws Exception {
        this.log.debug("Task doRun...");
        //���þ�̬�����������������Ƕ��١�
        super.setCountTask(this.list.size());
        //ѭ��ÿ������
        for (int i = 0; i < this.list.size(); i++) {
            this.currentTaskCache = this.list.get(i);
            this.currentTaskCache.run();//ִ��
            super.step();//ִ���������һ����
        }
        this.currentTaskCache = null;
    }
    @Override
    public Task getCurrent() {
        if (this.currentTaskCache == null)
            return this;
        else
            return this.currentTaskCache;
    }
}