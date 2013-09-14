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
import java.util.Set;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
/**
 * ApiBinder
 * @version : 2013-4-10
 * @author ������ (zyc@hasor.net)
 */
public interface ApiBinder {
    /**��ȡ��ʼ������*/
    public Environment getEnvironment();
    /**��ȡ���ڳ�ʼ��Guice��Binder��*/
    public Binder getGuiceBinder();
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * ʹ������ķ����������»�ȡ�󶨵����͡�
     * <pre>
     * AppContext :
     *   appContext.findBindingsByType(BeanInfo.class);
     * Guice :
     *   TypeLiteral INFO_DEFS = TypeLiteral.get(BeanInfo.class);
     *   appContext.getGuice().findBindingsByType(INFO_DEFS);
     * </pre>
     * */
    public <T> LinkedBindingBuilder<T> bindingType(Class<T> type);
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * @see ApiBinder#bindingType(Class); */
    public <T> void bindingType(Class<T> type, T instance);
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * @see ApiBinder#bindingType(Class); */
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Class<? extends T> implementation);
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * @see ApiBinder#bindingType(Class); */
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Provider<? extends T> provider);
    /**������Ķ����ǰһ�������ϡ�����ͨ��AppContextʹ�ð󶨵��������»�ȡ�󶨵Ķ���
     * @see ApiBinder#bindingType(Class); */
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Key<? extends T> targetKey);
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> getClassSet(Class<?> featureType);
    //
    //
    /**����ģ��������ϵ��*/
    public DependencySettings dependency();
    /**ע��һ��bean��*/
    public BeanBindingBuilder newBean(String beanName);
    /**����ע��Bean*/
    public static interface BeanBindingBuilder {
        /**����*/
        public BeanBindingBuilder aliasName(String aliasName);
        /**bean�󶨵����͡�*/
        public <T> LinkedBindingBuilder<T> bindType(Class<T> beanClass);
    }
    /** �ýӿڿ�������ģ����Ϣ */
    public interface DependencySettings {
        /**�������ƣ�ǿ��Ŀ��ģ��������ǰģ��(������)��*/
        public void reverse(Class<? extends Module> targetModule);
        /**ǿ������������Ŀ��ģ�����������������������ģ��û�гɹ����������ģ�鲻��������<br/> 
         * ע�⣺�÷���Ҫ����Ŀ��ģ������֮����������*/
        public void forced(Class<? extends Module> targetModule);
        /**��������Ҫ��Ŀ��ģ��������ڵ�ǰģ��֮ǰ����������<br/>
         * ע�⣺�÷�������Ҫ����Ŀ��ģ��֮����������Ŀ��ģ���Ƿ���������ǿ��Ҫ��*/
        public void weak(Class<? extends Module> targetModule);
    }
}