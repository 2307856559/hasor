/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.core;
import java.util.List;
/**
 * �ýӿ�ֻ�ܻ�ȡģ����Ϣ��ע�⣺�ýӿڲ�Ҫ����ȥʵ������
 * @version : 2013-7-26
 * @author ������ (zyc@hasor.net)
 */
public interface ModuleInfo {
    /**��ȡ�󶨵������ļ������ռ�*/
    public String getSettingsNamespace();
    /**��ȡ��ʾ����*/
    public String getDisplayName();
    /**��ȡ������Ϣ*/
    public String getDescription();
    /**������ʾ����*/
    public void setDisplayName(String displayName);
    /**����������Ϣ*/
    public void setDescription(String description);
    /**��ȡģ����Ϣ��������ģ�����*/
    public Module getTarget();
    /**��ȡģ�������ģ��*/
    public List<Dependency> getDependency();
    //
    /**��ģ���Ƿ�׼���ã���ģ�龭����init������Ϊ׼����.*/
    public boolean isReady();
    /**��ģ���Ƿ�׼���ã���ģ�龭����init������Ϊ׼����.*/
    public boolean isDependencyReady();
    /**ģ���Ƿ�����.*/
    public boolean isStart();
}