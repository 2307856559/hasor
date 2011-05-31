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
package org.more.core.task;
/**
 * �ýӿ������ڶ�λ�����Ѿ�ִ�еĲ��������ͻ��ж���û��ִ�С�ͨ���ýӿڵ�getRatio���������Ի��ָ�����ȵı���ֵ��
 * @version 2009-5-16
 * @author ������ (zyc@byshell.org)
 */
public abstract class LocationTask extends Task {
    private static final long serialVersionUID = 5140955177850271048L;
    /** �ܹ������� */
    private int               count            = 0;
    /** ��ǰִ�е������� */
    private int               index            = 0;
    /** ���������������ִ�е���������� */
    private Task              currentTask      = null;
    /**
     * ���ÿɶ�λ���������������������õ�ֵС�� 1 ��newCount����ֵ�ᱻ 1 ȡ����
     * ͬʱ�����ǰgetPosition()��������ֵ�����������õ�newCount����ֵ��ϵͳ�Զ�����
     * newCount����ֵ��ΪgetPosition()��������ֵ��
     * @param newCount ���õ�����ֵ��
     */
    protected void setCountTask(int newCount) {
        this.log.debug(this.index + "/" + this.count + " setCountTask " + newCount);
        if (newCount < 1)
            this.count = 1;
        else
            this.count = newCount;
        if (this.index > this.count)
            this.index = this.count;
    }
    /**
     * ���õ�ǰ��λ���������λ�á�������õ�ֵС�� 0 ��index����ֵ�ᱻ 0 ȡ����
     * ������õ�ֵ����getCount()�ķ���ֵ�����getCount()����ֵ��Ϊ���õĲ�����
     * @param index ���õ�����ֵ��
     */
    protected void setIndexTask(int index) {
        this.log.debug(this.index + "/" + this.count + " setIndexTask " + index);
        if (index < 0)
            this.index = 0;
        else if (index > this.count)
            this.index = this.count;
        else
            this.index = index;
    }
    /** ��λ�����Զ�����1������Ѿ��������������Բ������÷����൱��setIndexTask(getPosition()++)�� */
    protected void step() {
        this.log.debug(this.index + "/" + this.count + " step");
        this.index++;
        if (this.index > this.count)
            this.index = this.count;
    }
    /** ��λ������������󡣸÷����൱��setIndexTask(getCount())�� */
    protected void stepToEnd() {
        this.log.debug(this.index + "/" + this.count + " stepToEnd");
        this.index = this.count;
    }
    /** ��������������ִ�е���������󡣸÷������Ի���getCurrent()�����Ľ��������getCount��getPosition��getRatio�����ĵ��á� */
    public void updateCurrent() {
        this.currentTask = this.getCurrent();
    }
    /** �÷�����Ϊ��getCount��getPosition��getRatio�ڵ��ù�����ʹ�õ���ͬһ����������׼���ġ���Ϊ�ڶ��߳�����ºܿ��ܵ���������������ʱʹ�õ����������һ����*/
    private LocationTask getCurrentLocationTask() {
        //�����ǰ�������Ϊ�ա�����ø��µ�ǰ������󷽷����¡�
        if (this.currentTask == null)
            this.updateCurrent();
        if (this.currentTask == null)
            return null;
        else if (this.currentTask instanceof LocationTask)
            return (LocationTask) this.currentTask;
        else
            return null;
    }
    /**
     * �������ִ�еĲ��������������Ǵ���0��һ���������Ҹ÷���ֵ������ָ����Ա�ʾ����ִ��ʱ��Ҫ�������ܲ�������
     * @return ��������ִ�еĲ��������������Ǵ���0��һ���������Ҹ÷���ֵ������ָ����Ա�ʾ����ִ��ʱ��Ҫ�������ܲ�������
     */
    public int getCount() {
        //���showChildTaskLocation����Ϊfalse�򷵻ص�ǰ������������ֵ
        if (this.showChildTaskLocation == false)
            return this.count;
        //��ȡ��ǰִ���������
        LocationTask lt = this.getCurrentLocationTask();
        if (lt == null)
            return this.count;
        //�����ǰִ�ж���Ϊ���򷵻ص�ǰ�����count
        return (lt == this) ? this.count : lt.getCount();
    }
    /**
     * �������ǰ��ִ�в��裬�����ǽ��� 0 �� getCount��
     * @return ��������ǰ��ִ�в��裬�����ǽ��� 0 �� getCount��
     */
    public int getPosition() {
        //���showChildTaskLocation����Ϊfalse�򷵻ص�ǰ������������ֵ
        if (this.showChildTaskLocation == false)
            return this.index;
        //��ȡ��ǰִ���������
        LocationTask lt = this.getCurrentLocationTask();
        if (lt == null)
            return this.index;
        //�����ǰִ�ж���Ϊ���򷵻ص�ǰ�����index
        return (lt == this) ? this.index : lt.getPosition();
    }
    /**
     * ��õ�ǰ�����ִ�еĲ������ܲ���ı�ֵ����ֵ��һ��δ��������ĸ����������showChildTaskLocation���Ա�����Ϊtrue��
     * ��÷�������������ִ�е�����Ľ���ֵ������������������������������Ȼ��������ʱ���ø÷�����ֱ�ӷ���getPosition()
     * ��getCount()�ı�ֵ��
     * @return ����getPosition()��getCount()�ı�ֵ����ֵ��һ��δ��������ĸ�������
     */
    public float getRatio() {
        float percentage = (float) this.getPosition() / (float) this.getCount();//�����ֵ
        return percentage;//����ָ�����ȵı���ֵ
    }
    //=============================================================================================
    /**  */
    private boolean showChildTaskLocation = false;
    /**
     * ��õ�ʹ��getCount()��getPosition()��getRatio()����������λ��ǰ����ִ�н���ʱ����ʾ������������Ȼ��ǵ�ǰ����Ľ��ȡ�
     * �����������LocationTask�����������ʾ��ǰ����Ľ��ȡ�
     * @return ���ص�ǰ�Ƿ���ʾ��������ȵ�ֵ��
     */
    public boolean isShowChildTaskLocation() {
        return showChildTaskLocation;
    }
    /**
     * ���õ�ʹ��getCount()��getPosition()��getRatio()����������λ��ǰ����ִ�н���ʱ����ʾ������������Ȼ��ǵ�ǰ����Ľ��ȡ�
     * �����������LocationTask�����������ʾ��ǰ����Ľ��ȡ�
     * ����������α����<pre>
     * LocationTask t1 = new LocationTask();//1
     * t1.addTask(new Task());//2
     * t1.addTask(new Task());//3
     * t1.addTask(new Task());//4
     * LocationTask t2 new LocationTask();//5
     * t2.addTask(new Task());//6
     * t2.addTask(t1);
     * t2.addTask(new Task());//7
     * t2.run();
     * t2.setShowChildTaskLocation(true);
     * </pre>
     * ��������Ĵ��������6,1[2,3,4],7��������ִ����6״̬ʱgetRatio()������ʾ����6�����ִ�н��ȡ��������ִ�е�2����ʾ����
     * 2�����ִ�н��ȣ���������Ա�����Ϊfalse��ִ����6״̬ʱgetRatio()������ʾ����6�����ִ�н��ȡ��������ִ�е�2����ʾ����
     * 1�����ִ�н��ȡ�
     * @param showChildTaskLocation
     */
    public void setShowChildTaskLocation(boolean showChildTaskLocation) {
        this.showChildTaskLocation = showChildTaskLocation;
    }
}