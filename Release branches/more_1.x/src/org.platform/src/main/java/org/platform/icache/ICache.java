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
package org.platform.icache;
import org.platform.context.AppContext;
import org.platform.context.setting.Config;
import com.google.inject.ImplementedBy;
/**
 * ����ʹ����ڣ������ʵ����ϵͳ�����ṩ��
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
@ImplementedBy(NoneCache.class)
public interface ICache {
    /**��ʼ��Cache*/
    public void initCache(AppContext appContext, Config config);
    /**����*/
    public void destroy();
    /**��һ��������뻺�档*/
    public void toCache(String key, Object value);
    /**��һ��������뻺�档*/
    public void toCache(String key, Object value, long timeout);
    /**����key�ӻ����л�ȡ�������*/
    public Object fromCache(String key);
    /**�жϻ������Ƿ���Ҫ��Ķ���*/
    public boolean hasCache(String key);
    /**ɾ��ĳ�����������*/
    public void remove(String key);
    /**��ջ���*/
    public void clear();
}