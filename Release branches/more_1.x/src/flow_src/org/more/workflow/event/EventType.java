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
package org.more.workflow.event;
/**
 * �¼����ͱ�
 * Date : 2010-5-21
 * @author ������
 */
public interface EventType {
    /**�������Ա���ȡʱʱ������*/
    public final static String GetValueEvent    = "GetValueEvent";
    /**�������Ա�����ʱ������*/
    public final static String SetValueEvent    = "SetValueEvent";
    /**����ͼ����Ԫ��Ϣ����ʾ��ģ�Ͷ���ʱ��*/
    public final static String NewInstanceEvent = "NewInstanceEvent";
    /**��ģ�ͱ�����ʱ��*/
    public final static String UpdataModeEvnet  = "UpdataModeEvnet";
    /**������״̬����ʱ��*/
    public final static String SaveStateEvent   = "SaveStateEvent";
    /**������״̬������״̬�ָ�ʱ��*/
    public final static String LoadStateEvent   = "LoadStateEvent";
};