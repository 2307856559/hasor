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
package org.hasor.web.controller.support;
/** 
 * �����ռ����������ͬ��action�����ռ��µ�action���������Զ����ڲ�ͬ�Ŀ������¡�
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
public interface ActionNameSpace {
    /**��ȡ���������ơ�*/
    public String getNameSpace();
    /**��ȡ�������ж����action������*/
    public ActionInvoke getActionByName(String httpMethod, String actionName);
}