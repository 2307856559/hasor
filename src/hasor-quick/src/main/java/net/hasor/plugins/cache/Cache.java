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
package net.hasor.plugins.cache;
import java.io.Serializable;
import java.util.Set;
/**
 * ����ӿڡ�
 * @version : 2013-4-20
 * @author ������ (zyc@byshell.org)
 */
public interface Cache {
    /**��һ��������뻺�档*/
    public boolean toCache(Serializable key, Object value);
    /**����key�ӻ����л�ȡ�������*/
    public Object fromCache(Serializable key);
    /**�жϻ������Ƿ���Ҫ��Ķ���*/
    public boolean hasCache(Serializable key);
    /**ɾ��ĳ����������ݡ�*/
    public boolean remove(Serializable key);
    /**��ջ��档*/
    public boolean clearCache();
    /**Ŀǰ�����С��*/
    public int size();
    /**��ȡ������Keys��*/
    public Set<Serializable> keys();
    /**�رղ�ͣ�û���*/
    public void close();
}