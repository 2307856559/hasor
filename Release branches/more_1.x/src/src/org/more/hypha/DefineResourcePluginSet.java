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
package org.more.hypha;
/**
 * ���������Ҫ֧����չ�����ò�������Ҫʵ�ָýӿڣ�
 * ����ͨ���ýӿڵ�getExpandDefine����ȡ�йض����ϵ�һЩ������չ�������á�
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public interface DefineResourcePluginSet {
    /**������չ����ȡ��չĿ�����*/
    public DefineResourcePlugin getPlugin(String name);
    /**����һ��������������������滻�����Ĳ��ע�ᡣ*/
    public void setPlugin(String name, DefineResourcePlugin plugin);
    /**ɾ��һ�����еĲ��ע�ᡣ*/
    public void removePlugin(String name);
}