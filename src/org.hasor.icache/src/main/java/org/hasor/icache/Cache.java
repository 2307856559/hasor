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
package org.hasor.icache;
import org.hasor.context.AppContext;
/**
 * ����ʹ����ڣ������Ĭ��ʹ��HashMap��Ϊʵ�֡�
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
public interface Cache<T> {
    /**��ʼ��Cache*/
    public void initCache(AppContext appContext);
    /**����Cache*/
    public void destroy(AppContext appContext);
    /**��һ��������뻺�档*/
    public boolean toCache(String key, T value);
    /**��һ��������뻺�档*/
    public boolean toCache(String key, T value, long timeout);
    /**����key�ӻ����л�ȡ�������*/
    public T fromCache(String key);
    /**�жϻ������Ƿ���Ҫ��Ķ���*/
    public boolean hasCache(String key);
    /**ɾ��ĳ�����������*/
    public boolean remove(String key);
    /**ˢ��ָ��key�Ļ���ʱ��*/
    public boolean refreshCache(String key);
    /**��ջ���*/
    public boolean clear();
}