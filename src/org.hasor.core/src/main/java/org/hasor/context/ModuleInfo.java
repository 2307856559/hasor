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
package org.hasor.context;
import java.util.List;
/**
 * �ýӿ�ֻ�ܻ�ȡģ����Ϣ��ע�⣺�ýӿڲ�Ҫ����ȥʵ������
 * @version : 2013-7-26
 * @author ������ (zyc@byshell.org)
 */
public interface ModuleInfo {
    /**��ȡ�󶨵������ļ������ռ�*/
    public String getSettingsNamespace();
    /**��ȡ��ʾ����*/
    public String getDisplayName();
    /**��ȡ������Ϣ*/
    public String getDescription();
    /**��ȡģ����Ϣ��������ģ�����*/
    public HasorModule getModuleObject();
    /**��ȡģ�������ģ��*/
    public List<Dependency> getDependency();
    /**��ģ��û��ͨ��configuration����ʱfalse������Ϊtrue.*/
    public boolean isReady();
    /**��ģ��ով���start�׶Σ��÷�������ֵΪtrue�����򷵻�ֵΪfalse.*/
    public boolean isRunning();
    /**��ģ��û��ͨ��init�׶η���ֵΪfalse������Ϊtrue.*/
    public boolean isInit();
    /**�ж�������ģ���Ƿ��Ѿ��������������Ϊһ����ѡ��������������Ŀ��ʹû��readyҲ�ᱻ�ж�Ϊready��*/
    public boolean isDependencyReady();
    /**�ж�������ģ���Ƿ��Ѿ��������������Ϊһ����ѡ��������������Ŀ��ʹû��RunningҲ�ᱻ�ж�ΪRunning��*/
    public boolean isDependencyRunning();
    /**�ж�������ģ���Ƿ�ͨ����isInit���������Ϊһ����ѡ��������������Ŀ��ʹû��initҲ�ᱻ�ж�Ϊinit��*/
    public boolean isDependencyInit();
}