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
import java.util.List;
import java.util.Set;
import org.more.UndefinedException;
import com.google.inject.Injector;
import com.google.inject.Provider;
/**
 * Hasor�ĺ��Ľӿڣ���ΪӦ�ó����ṩ��һ��ͳһ�����ý�������л�����
 * @version : 2013-3-26
 * @author ������ (zyc@hasor.net)
 */
public interface AppContext {
    //
    //----------------------------------------------------------------------------------Event
    /**�����¼���������ģ���ʼ��֮��������
     * @see net.hasor.core.context.AbstractAppContext*/
    public static final String ContextEvent_Initialized = "ContextEvent_Initialized";
    /**�����¼���������ģ�� start �׶�֮��������
     * @see net.hasor.core.context.AbstractAppContext*/
    public static final String ContextEvent_Started     = "ContextEvent_Started";
    /**ģ���¼�����ģ���յ� start �����ź�֮��������
     * @see net.hasor.core.module.ModuleProxy*/
    public static final String ModuleEvent_Started      = "ModuleEvent_Started";
    //
    //----------------------------------------------------------------------------------Bean
    /**ͨ������ȡBean�����͡�*/
    public <T> Class<T> getBeanType(String name);
    /**�������Ŀ�����͵�Bean�򷵻�Bean�����ơ�*/
    public String getBeanName(Class<?> targetClass);
    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    public String[] getBeanNames();
    /**ͨ�����ƴ���beanʵ����ʹ��guice�������ȡ��bean�������������{@link UndefinedException}�����쳣��*/
    public <T> T getBean(String name);
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getInstance(Class<T> targetClass);
    //
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> List<T> findBindingBean(Class<T> bindingType);
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> List<Provider<T>> findBindingProvider(Class<T> bindingType);
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> T findBindingBean(String withName, Class<T> bindingType);
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> Provider<T> findBindingProvider(String withName, Class<T> bindingType);
    //
    //----------------------------------------------------------------------------------Context
    /**���Guice������*/
    public Injector getGuice();
    /**��ȡ������*/
    public Object getContext();
    /**��ȡϵͳ����ʱ��*/
    public long getStartTime();
    /**��ȡӦ�ó������á�*/
    public Settings getSettings();
    /**��ȡ�����ӿڡ�*/
    public Environment getEnvironment();
    /**��ȡ�¼�������*/
    public EventManager getEventManager();
    /**�������ģ��*/
    public ModuleInfo[] getModules();
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> findClass(Class<?> featureType);
    //
    //----------------------------------------------------------------------------------Life
    /**��ʾAppContext�Ƿ�׼���á�*/
    public boolean isReady();
    /**������������ģ�鷢�������źţ�����������״̬��ΪStart�����÷����᳢��init����ģ�飩*/
    public void start();
    /**�ж������Ƿ�������״̬*/
    public boolean isStart();
}