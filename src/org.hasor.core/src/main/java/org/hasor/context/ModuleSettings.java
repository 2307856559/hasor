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
/**
 * �ýӿڿ�������ģ����Ϣ
 * @version : 2013-7-26
 * @author ������ (zyc@byshell.org)
 */
public interface ModuleSettings extends ModuleInfo {
    /***/
    public void afterMe(Class<? extends HasorModule> targetModule);
    /**Ҫ��Ŀ��ģ��������ڵ�ǰģ��֮ǰ����������<br/>
     * ע�⣺�÷�������Ҫ����Ŀ��ģ��֮����������Ŀ��ģ���Ƿ���������ǿ��Ҫ��*/
    public void beforeMe(Class<? extends HasorModule> targetModule);
    /**����Ŀ��ģ��������������<br/> 
     * ע�⣺�÷���Ҫ����Ŀ��ģ������֮����������*/
    public void followTarget(Class<? extends HasorModule> targetModule);
    /**�������ļ������ռ�*/
    public void bindingSettingsNamespace(String settingsNamespace);
    /**������ʾ����*/
    public void setDisplayName(String displayName);
    /**����������Ϣ*/
    public void setDescription(String description);
}