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
package org.moreframework.context;
import java.util.Set;
import com.google.inject.Injector;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public interface AppContext extends BeanContext {
    /**��ȡ������*/
    public Object getContext();
    /**��ȡϵͳ����ʱ��*/
    public long getAppStartTime();
    /**��ȡӦ�ó������á�*/
    public Settings getSettings();
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType);
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getInstance(Class<T> beanType);
    //    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    //    public  <T extends IService> T getService(String servicesName);
    //    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    //    public  <T extends IService> T getService(Class<T> servicesType);
    /**���Guice������*/
    public Injector getGuice();
    /**��ù����ռ�����*/
    public WorkSpace getWorkSpace();
    /**��ȡ�������������ӿڡ�*/
    public Environment getEnvironment();
}