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
package org.more.hypha.anno;
import org.more.hypha.DefineResource;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ע��ע����������ýӿڵĹ����Ǹ�����ղ�������{@link AnnoServices}�ӿ�ע���ע����ӡ�
 * ���Խ����������ע�ᵽ{@link AnnoServices}����С�
 * @version 2010-10-26
 * @author ������ (zyc@byshell.org) 
 */
public interface KeepWatchParser {
    /**
     * ����ע���������
     * @param beanType �����ӵ���Bean��
     * @param resource {@link DefineResource}����
     * @param plugin {@link AnnoServices}����
     */
    public void process(Class<?> beanType, XmlDefineResource resource, AnnoServices plugin);
};