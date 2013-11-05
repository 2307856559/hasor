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
import com.google.inject.Key;
import com.google.inject.Provider;
/**
 * Ӧ�ó���������
 * @version : 2013-3-26
 * @author ������ (zyc@hasor.net)
 */
public interface AppContext {
    /**�����¼���������ģ�� init �׶�֮ǰ������
     * @see net.hasor.core.context.AbstractAppContext*/
    public static final String ContextEvent_Init   = "ContextEvent_Init";
    /**�����¼���������ģ�� start �׶�֮ǰ������
     * @see net.hasor.core.context.AbstractAppContext*/
    public static final String ContextEvent_Start  = "ContextEvent_Start";
    /**�����¼���������ģ�鴦���� stop �׶�֮��������
     * @see net.hasor.core.context.AbstractAppContext*/
    public static final String ContextEvent_Stoped = "ContextEvent_Stoped";
    /**ģ���¼�����ģ���յ� start �����ź�֮��������
     * @see net.hasor.core.module.ModulePropxy*/
    public static final String ModuleEvent_Start   = "ModuleEvent_Start";
    /**ģ���¼�����ģ�鴦���� stop �����ź�֮��������
     * @see net.hasor.core.module.ModulePropxy*/
    public static final String ModuleEvent_Stoped  = "ModuleEvent_Stoped";
    //
    /**ע�����
     * @see net.hasor.core.services.ServicesRegisterHandler*/
    public <T> void registerService(Class<T> type, T serviceBean, Object... objects);
    /**ע�����
     * @see net.hasor.core.services.ServicesRegisterHandler*/
    public <T> void registerService(Class<T> type, Class<? extends T> serviceType, Object... objects);
    /**ע�����
     * @see net.hasor.core.services.ServicesRegisterHandler*/
    public <T> void registerService(Class<T> type, Key<? extends T> serviceKey, Object... objects);
    /**���ע�����
     * @see net.hasor.core.services.ServicesRegisterHandler*/
    public <T> void unRegisterService(Class<T> type, T serviceBean);
    /**���ע�����
     * @see net.hasor.core.services.ServicesRegisterHandler*/
    public <T> void unRegisterService(Class<T> type, Class<? extends T> serviceType);
    /**���ע�����
     * @see net.hasor.core.services.ServicesRegisterHandler*/
    public <T> void unRegisterService(Class<T> type, Key<? extends T> serviceKey);
    //
    /**ͨ������ȡBean�����͡�*/
    public <T> Class<T> getBeanType(String name);
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType);
    /**�������Ŀ�����͵�Bean�򷵻�Bean�����ơ�*/
    public String getBeanName(Class<?> targetClass);
    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    public String[] getBeanNames();
    /**ͨ�����ƴ���beanʵ����ʹ��guice�������ȡ��bean�������������{@link UndefinedException}�����쳣��*/
    public <T> T getBean(String name);
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getInstance(Class<T> beanType);
    /**���Guice������*/
    public Injector getGuice();
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> List<T> getInstanceByBindingType(Class<T> bindingType);
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> List<Provider<T>> getProviderByBindingType(Class<T> bindingType);
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> T getInstanceByBindingType(String withName, Class<T> bindingType);
    /**ͨ��һ�����ͻ�ȡ���а󶨵������͵��ϵĶ���ʵ����*/
    public <T> Provider<T> getProviderByBindingType(String withName, Class<T> bindingType);
    //
    /**��ȡ������*/
    public Object getContext();
    /**��ȡϵͳ����ʱ��*/
    public long getStartTime();
    /**��ʾAppContext�Ƿ�׼���á�*/
    public boolean isReady();
    /**��ȡӦ�ó������á�*/
    public Settings getSettings();
    /**��ȡ�����ӿڡ�*/
    public Environment getEnvironment();
    /**��ȡ�¼������ӿڡ�*/
    public EventManager getEventManager();
    /**�������ģ��*/
    public ModuleInfo[] getModules();
    //
    /**������������ģ�鷢�������źţ�����������״̬��ΪStart�����÷����᳢��init����ģ�飩*/
    public void start();
    /**ֹͣ��������ģ�鷢��ֹͣ�źţ�����������״̬��ΪStop��*/
    public void stop();
    /**�ж������Ƿ�������״̬*/
    public boolean isStart();
}