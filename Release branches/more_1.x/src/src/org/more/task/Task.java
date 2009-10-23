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
package org.more.task;
import java.io.Serializable;
import java.util.UUID;
import org.more.log.ILog;
import org.more.log.LogFactory;
import org.more.util.attribute.AttBase;
/**
 * ������Ļ��࣬�κ�������󶼱���̳иýӿڣ���more.taskϵͳ������ӵ�ж�����Ψһ��ʶID��
 * ���ID�ı�ʶ��ʽ�� �������� + | + UUID��������Ա����ͨ�����ID��ϵͳ�����ж�������в�����
 * Date : 2009-5-15
 * @author ������
 */
public abstract class Task extends AttBase implements Runnable, Serializable {
    /**  */
    private static final long serialVersionUID = -5330043409377797416L;
    /** �����־ */
    protected ILog            log              = LogFactory.getLog("org_more_task");
    /** ��ʶ�����UUID */
    private String            uuid             = null;
    /** ������ */
    private String            name             = null;
    /** ����˵�� */
    private String            description      = null;
    /** ��ǰ�����ִ��״̬�� */
    private TaskState         state            = null;
    /** ������������� */
    protected Task() {
        this.log.debug("create Task name=" + this.name + " class=" + this.getClass().getSimpleName());
        //����UUID
        this.uuid = UUID.randomUUID().toString();
        //��������
        this.name = this.getClass().getSimpleName() + "|" + this.uuid.replace("-", "");
        //��������״̬
        this.state = TaskState.New;
    }
    /**
     * �������˵����Ϣ��
     * @return ��������˵����Ϣ��
     */
    public String getDescription() {
        return description;
    }
    /**
     * ��������˵����Ϣ��
     * @param description Ҫ���õ�����˵����Ϣ��
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * ����������ơ�Ĭ������������������������+IDֵ��
     * @return �����������ơ�
     */
    public String getName() {
        return this.name;
    }
    /**
     * �����������ơ�
     * @param name Ҫ���õ��������ơ�
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * �������ID����IDֵ�������ò��Ҹ�IDֵ���Զ����ɵġ�����������д�÷����Է����Զ����IDֵ��
     * @return ��������ID����IDֵ�������ò��Ҹ�IDֵ���Զ����ɵġ�����������д�÷����Է����Զ����IDֵ��
     */
    public String getUuid() {
        return uuid;
    }
    /**
     * ��õ�ǰ�����ִ��״̬��
     * @return ���ص�ǰ�����ִ��״̬��
     */
    public TaskState getState() {
        return state;
    }
    @Override
    public void run() {
        //֪ͨ��ʼִ��
        this.beginRun();
        try {
            //ִ��
            this.doRun();
        } catch (Exception e) {
            //��ִ��ʱ�����쳣
            this.onException(e);
        }
        //֪ͨ����ִ��
        this.endRun();
    }
    /**
     * ��õ�ǰ����������ִ�е��������Task��Ĭ�ϼ��÷���ʵ��Ϊ����this��
     * ���������д�÷�����ʵ�ַ�����������ִ�е�������Task����
     * @return ��õ�ǰ����������ִ�е��������
     */
    public abstract Task getCurrent();
    /**
     * ���Ե�ǰ�����Ƿ�����ִ�С��������true���ʾ����ִ�С�����Ϊfalse��ʾ����û��ִ�С�
     * @return ���ز��Ե�ǰ�����Ƿ�����ִ�С��������true���ʾ����ִ�С�����Ϊfalse��ʾ����û��ִ�С�
     */
    public boolean isRun() {
        return (this.state == TaskState.Run) ? true : false;//�Ƿ�����ִ��
    };
    /** �޸�״̬Ϊ��ʼִ�С� */
    private void beginRun() {
        this.log.debug("Task[" + this.name + "] : beginRun");
        this.state = TaskState.Run;
    };
    /**
     * ���񷽷��壬����ʵ�ָ÷��������ĳ���ض�������
     * @throws Exception �����ִ�й����з����쳣��
     */
    protected abstract void doRun() throws Exception;
    /**
     * ��ִ��doRun����ʱ�����쳣��
     * @param e ִ��doRun�����������쳣����
     */
    protected void onException(Exception e) {
        this.log.debug("Task[" + this.name + "] : onException message = " + e.getMessage());
    };
    /** �޸�״̬Ϊִ����ϡ� */
    private void endRun() {
        this.log.debug("Task[" + this.name + "] : endRun");
        this.state = TaskState.RunEnd;
    }
}
