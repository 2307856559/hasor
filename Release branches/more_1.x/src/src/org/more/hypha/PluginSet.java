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
import java.util.List;
/**
 * ������ϣ�������ڹҽ�һЩ����Ĺ��ܡ��ýӿ������ڹ���Ҫ�ҽӵĲ�����ϡ�
 * ����ͨ���ýӿڵ�getPlugin����ȡ�йض����ϵ�һЩ������չ�������á�
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public interface PluginSet<T> {
    /**������չ����ȡ��չĿ�����*/
    public Plugin<T> getPlugin(String name);
    /**����һ����չ�������չ�������滻��������չע�ᡣ*/
    public void setPlugin(String name, Plugin<T> plugin);
    /**ɾ��һ�����е���չע�ᡣ*/
    public void removePlugin(String name);
    /**��ȡ��ע����չ�����Ƽ��ϡ�*/
    public List<String> getPluginNames();
};