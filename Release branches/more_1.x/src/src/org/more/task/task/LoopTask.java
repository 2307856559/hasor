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
/**
 * ѭ��ִ�����񣬵�����Ҫ�ظ�ִ�е�����Ϳ���ʹ��LoopTask��������LoopTask���Դ���ִ��ָ������������
 * Ҳ�������ڴ�������ѭ��������LoopTaskͨ��������������ִ�д������ﵽ��ЩĿ�ġ�����ϣ������ִ��ʱ
 * ������������ִ�д���Ϊ0��LoopTask���Ĭ��ִ�д��������޴Ρ�
 * Date : 2009-5-15
 * @author ������
 */
public class LoopTask extends ItemTask {
    /**  */
    private static final long serialVersionUID = 1573066209349130377L;
    /** ������ǰѭ�������Ƿ������һ��ִ�еı�ǣ����Ϊtrue���ʾҪ�������һ��ִ�С�false���ʾ��ǰִ�н���֮������ִ�С� */
    private int               doRunCount       = -1;
    /**
     * ������ִ�е������������������һ���ض���ִ������
     * @param runnable ׼��ִ�е�Ŀ������
     */
    public LoopTask(Runnable runnable) {
        super(runnable);
    }
    @Override
    protected void doRun() throws Exception {
        while (this.doRunCount != 0) {
            if (this.doRunCount > 0)
                //���� 0 ����ѭ������
                this.doRunCount--;
            super.doRun();
        }
    }
    @Override
    protected void onException(Exception e) {
        this.doRunCount = 0;
        super.onException(e);
    }
    /** ����������ִ�����֮���ڽ�����һ��ִ�С� */
    public void breakRun() {
        this.doRunCount = 0;
    }
    /**
     * ��ȡ��ǰ����ִ�ж��ٴΣ�������ص���0���ʾ��ǰ����ִ�У��������һ���������ʾ��ǰ��������ִ�С�
     * @return ��ȡ��ǰ����ִ�ж��ٴΣ�������ص���0���ʾ��ǰ����ִ�У��������һ���������ʾ��ǰ��������ִ�С�
     */
    public int getDoRunCount() {
        return doRunCount;
    }
    /**
     * ���õ�ǰ����ִ�ж��ٴΣ�������õ�ֵ��0���ʾ��ǰ����ִ�У�������õ�ֵ��һ���������ʾ��ǰ��������ִ�С�
     * @param doRunCount Ҫ�����õ�ִ�д���
     */
    public void setDoRunCount(int doRunCount) {
        this.doRunCount = doRunCount;
    }
}