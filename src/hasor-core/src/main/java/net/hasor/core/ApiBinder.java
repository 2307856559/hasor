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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import javax.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
/**
 * Hasor�ĺ��Ľӿڣ���Ҫ�����ռ���������Ϣ��<p>
 * 
 * Hasor �ڳ�ʼ��ģ��ʱ��Ϊÿ��ģ���������һ�� ApiBinder �ӿ�ʵ����
 * <p>���� {@link ApiBinder#configModule()} ,�᷵��һ���ӿ��������õ�ǰģ�����������
 * @version : 2013-4-10
 * @author ������ (zyc@hasor.net)
 */
public interface ApiBinder {
    /**��ȡӦ�ó������á�*/
    public Settings getSettings();
    /**��ȡ�����ӿڡ�*/
    public Environment getEnvironment();
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> findClass(Class<?> featureType);
    /**ע��һ����Ҫ AppContextAware ���ࡣ�ýӿڻ��� AppContext �������һʱ��ע�� AppContext��*/
    public void registerAware(AppContextAware aware);
    //
    /*----------------------------------------------------------------------------------------Aop*/
    /**����Aop*/
    public void bindInterceptor(String matcherExpression, MethodInterceptor interceptor);
    /**����Aop*/
    public void bindInterceptor(Matcher<Class<?>> matcherClass, Matcher<Method> matcherMethod, MethodInterceptor interceptor);
    /***/
    public static interface Matcher<T> {
        /**Returns {@code true} if this matches {@code t}, {@code false} otherwise.*/
        public boolean matches(T t);
    }
    //
    /*--------------------------------------------------------------------------------------Event*/
    /**pushPhaseEvent����ע���ʱ����������յ�һ���¼�֮��ᱻ�Զ�ɾ����*/
    public void pushListener(String eventType, EventListener eventListener);
    /**���һ�������¼����¼���������*/
    public void addListener(String eventType, EventListener eventListener);
    /**ɾ��ĳ����������ע�ᡣ*/
    public void removeListener(String eventType, EventListener eventListener);
    /**ͬ����ʽ�׳��¼�������������ʱ�Ѿ�ȫ����������¼��ַ���<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ���ж��¼��ַ��׳��������쳣��*/
    public void fireSyncEvent(String eventType, Object... objects);
    /**ͬ����ʽ�׳��¼�������������ʱ�Ѿ�ȫ����������¼��ַ���<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ�÷������̵��쳣�������ַ��¼������̵����쳣����һ������ķ�ʽ���֡�*/
    public void fireSyncEvent(String eventType, EventCallBackHook callBack, Object... objects);
    /**�첽��ʽ�׳��¼���fireAsyncEvent�����ĵ��ò��������ʱ��ʼִ���¼�������һ�����¼�������������<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ�÷������̵��쳣�������ַ��¼������̵����쳣����һ������ķ�ʽ���֡�*/
    public void fireAsyncEvent(String eventType, Object... objects);
    /**�첽��ʽ�׳��¼���fireAsyncEvent�����ĵ��ò��������ʱ��ʼִ���¼�������һ�����¼�������������<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ���ж��¼��ַ�����������ִ��Ȩ�����쳣����ӿڡ�*/
    public void fireAsyncEvent(String eventType, EventCallBackHook callBack, Object... objects);
    //
    /*-------------------------------------------------------------------------------------Module*/
    /**����ģ��������ϵ��*/
    public ModuleSettings configModule();
    /**�ýӿڿ�������ģ����Ϣ��*/
    public interface ModuleSettings extends ModuleInfo {
        /**�������ƣ�ǿ��Ŀ��ģ��������ǰģ��(������)��*/
        public void reverse(Class<? extends Module> targetModule);
        /**ǿ������������Ŀ��ģ�����������������������ģ��û�гɹ����������ģ�鲻��������<br/> 
         * ע�⣺�÷���Ҫ����Ŀ��ģ������֮����������*/
        public void forced(Class<? extends Module> targetModule);
        /**��������Ҫ��Ŀ��ģ��������ڵ�ǰģ��֮ǰ����������<br/>
         * ע�⣺�÷�������Ҫ����Ŀ��ģ��֮����������Ŀ��ģ���Ƿ���������ǿ��Ҫ��*/
        public void weak(Class<? extends Module> targetModule);
    }
    //
    /*------------------------------------------------------------------------------------Binding*/
    /** */
    public <T> NamedBindingBuilder<T> bindingType(Class<T> type);
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * @see #bindingType(Class) */
    public <T> void bindingType(Class<T> type, T instance);
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * @see #bindingType(Class) */
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Class<? extends T> implementation);
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * @see #bindingType(Class) */
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Provider<T> provider);
    /**Ϊ�󶨵Ķ���ָ��һ�����ƽ��а󶨣���ͬ���Ƶ����Ͱ�ֻ�ܰ�һ�Ρ�
     * @see #bindingType(Class)*/
    public <T> LinkedBindingBuilder<T> bindingType(String withName, Class<T> type);
    /**Ϊ�󶨵Ķ���ָ��һ�����ƽ��а󶨣���ͬ���Ƶ����Ͱ�ֻ�ܰ�һ�Ρ�
     * @see #bindingType(String, Class)*/
    public <T> void bindingType(String withName, Class<T> type, T instance);
    /**Ϊ�󶨵Ķ���ָ��һ�����ƽ��а󶨣���ͬ���Ƶ����Ͱ�ֻ�ܰ�һ�Ρ�
     * @see #bindingType(String, Class)*/
    public <T> ScopedBindingBuilder bindingType(String withName, Class<T> type, Class<? extends T> implementation);
    /**Ϊ�󶨵Ķ���ָ��һ�����ƽ��а󶨣���ͬ���Ƶ����Ͱ�ֻ�ܰ�һ�Ρ�
     * @see #bindingType(String, Class)*/
    public <T> ScopedBindingBuilder bindingType(String withName, Class<T> type, Provider<T> provider);
    //
    /*---------------------------------------------------------------------------------------Bean*/
    /**ע��һ��bean��*/
    public BeanBindingBuilder defineBean(String beanName);
    //
    public interface BeanBindingBuilder {
        /**����*/
        public BeanBindingBuilder aliasName(String aliasName);
        /**��������*/
        public BeanBindingBuilder setProperty(String attName, Object attValue);
        /**bean�󶨵����͡�*/
        public <T> LinkedBindingBuilder<T> bindType(Class<T> beanType);
    }
    public interface NamedBindingBuilder<T> extends LinkedBindingBuilder<T> {
        public LinkedBindingBuilder<T> nameWith(String name);
    }
    public interface LinkedBindingBuilder<T> extends ScopedBindingBuilder {
        /**Ϊ������һ��ʵ����*/
        public ScopedBindingBuilder to(Class<? extends T> implementation);
        /**Ϊ������һ��ʵ��*/
        public ScopedBindingBuilder toInstance(T instance);
        /**Ϊ������һ��Provider*/
        public ScopedBindingBuilder toProvider(Provider<T> provider);
        /**Ϊ������һ�����췽��*/
        public ScopedBindingBuilder toConstructor(Constructor<? extends T> constructor);
    }
    public interface ScopedBindingBuilder {
        /**ע��Ϊ����*/
        public void asEagerSingleton();
        /**�������Ϲ��������*/
        public void toScope(Scope scope);
    }
}