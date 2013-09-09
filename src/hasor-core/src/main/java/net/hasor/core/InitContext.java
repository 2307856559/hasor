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
import java.net.URI;
import java.util.Set;
/**
 * ��ʼ��ʹ���Ӧ�ó���������
 * @version : 2013-3-26
 * @author ������ (zyc@hasor.net)
 */
public interface InitContext {
    /**��ȡ������*/
    public Object getContext();
    /**��ȡϵͳ����ʱ��*/
    public long getAppStartTime();
    /**��ȡ�����ļ�URI*/
    public URI getSettingURI();
    /**��ȡӦ�ó������á�*/
    public Settings getSettings();
    /**��ȡ�������������ӿڡ�*/
    public Environment getEnvironment();
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType);
    //
    /**��������ļ��ı��¼���������*/
    public void addSettingsListener(HasorSettingListener listener);
    /**ɾ�������ļ��ı��¼���������*/
    public void removeSettingsListener(HasorSettingListener listener);
    /**������������ļ��ı��¼���������*/
    public HasorSettingListener[] getSettingListeners();
}