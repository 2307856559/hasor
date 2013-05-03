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
package org.platform.web.action;
import org.platform.context.AppContext;
/** 
 * ����ʹ����ڣ������ʵ����ϵͳ�����ṩ��
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
public interface ActionManager {
    /**��ȡĬ�ϻ���*/
    public ICache<Object> getDefaultCache();
    /**��ȡ����*/
    public ICache<Object> getCache(String cacheName);
    /**�����������ͻ�ȡ�����͵�Key��������*/
    public IKeyBuilder getKeyBuilder(Class<?> sampleType);
    /**��ʼ�������������*/
    public void initManager(AppContext appContext);
    /**���ٻ������*/
    public void destroyManager(AppContext appContext);
}