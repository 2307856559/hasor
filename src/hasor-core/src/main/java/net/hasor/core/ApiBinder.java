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
import org.aopalliance.intercept.MethodInterceptor;
/**
 * Hasor�ĺ��Ľӿڣ���Ҫ�����ռ���������Ϣ��<p>
 * 
 * Hasor �ڳ�ʼ��ģ��ʱ��Ϊÿ��ģ���������һ�� ApiBinder �ӿ�ʵ����
 * <p>���� {@link ApiBinder#configModule()} ,�᷵��һ���ӿ��������õ�ǰģ�����������
 * @version : 2013-4-10
 * @author ������ (zyc@hasor.net)
 */
public interface ApiBinder extends EventContext {
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
    /**����Aop�����ʽ��ʽΪ*/
    public void bindInterceptor(String matcherExpression, MethodInterceptor interceptor);
    /**����Aop*/
    public void bindInterceptor(Matcher<Class<?>> matcherClass, Matcher<Method> matcherMethod, MethodInterceptor interceptor);
    /***/
    public static interface Matcher<T> {
        /**Returns {@code true} if this matches {@code t}, {@code false} otherwise.*/
        public boolean matches(T t);
    }
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
        public MetaDataBindingBuilder toInstance(T instance);
        /**Ϊ������һ��Provider*/
        public ScopedBindingBuilder toProvider(Provider<T> provider);
        /**Ϊ������һ�����췽��*/
        public ScopedBindingBuilder toConstructor(Constructor<? extends T> constructor);
    }
    public interface ScopedBindingBuilder extends MetaDataBindingBuilder {
        /**ע��Ϊ����*/
        public MetaDataBindingBuilder asEagerSingleton();
        /**�������Ϲ��������*/
        public MetaDataBindingBuilder toScope(Scope scope);
    }
    public interface MetaDataBindingBuilder {
        /**��ȡԪ��Ϣ��*/
        public MetaDataBindingBuilder metaData(String key, Object value);
    }
}