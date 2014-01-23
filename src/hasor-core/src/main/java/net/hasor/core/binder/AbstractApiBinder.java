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
package net.hasor.core.binder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContextAware;
import net.hasor.core.Environment;
import net.hasor.core.EventManager;
import org.more.util.StringUtils;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.UniqueAnnotations;
import com.google.inject.name.Names;
/**
 * ��׼�� {@link ApiBinder} �ӿ�ʵ�֣�Hasor �ڳ�ʼ��ģ��ʱ��Ϊÿ��ģ���������һ�� ApiBinder �ӿ�ʵ����
 * <p>���󷽷� {@link #configModule()} ,�᷵��һ���ӿ�( {@link net.hasor.core.ApiBinder.ModuleSettings ModuleSettings} )
 * �������õ�ǰģ�����������
 * <p><b><i>��ʾ��</i></b>ģ������� {@link net.hasor.core.module.ModulePropxy} ����Ϊ {@link #configModule()} �����ṩ֧�֡�
 * @see net.hasor.core.module.ModulePropxy
 * @version : 2013-4-12
 * @author ������ (zyc@hasor.net)
 */
public abstract class AbstractApiBinder implements ApiBinder, Module {
    private Environment           environment = null;
    private BeanInfoModuleBuilder beanBuilder = new BeanInfoModuleBuilder(); /*Beans*/
    //
    protected AbstractApiBinder(Environment envContext) {
        this.environment = envContext;
    }
    public void configure(Binder binder) {
        binder.install(this.beanBuilder);
    }
    public Environment getEnvironment() {
        return this.environment;
    }
    public EventManager getEventManager() {
        return this.getEnvironment().getEventManager();
    }
    public abstract Binder getGuiceBinder();
    //    /**��ȡ����ģ��*/
    //    public ModuleInfo getModuleInfo() {
    //        return this.forModule;
    //    }
    public Set<Class<?>> findClass(Class<?> featureType) {
        if (featureType == null)
            return null;
        return this.getEnvironment().findClass(featureType);
    }
    public BeanBindingBuilder defineBean(String beanName) {
        if (StringUtils.isBlank(beanName) == true)
            throw new NullPointerException(beanName);
        return this.beanBuilder.newBeanDefine(this.getGuiceBinder()).aliasName(beanName);
    }
    //
    public <T> LinkedBindingBuilder<T> bindingType(Class<T> type) {
        return this.getGuiceBinder().bind(type).annotatedWith(UniqueAnnotations.create());
    }
    public <T> void bindingType(Class<T> type, T instance) {
        this.bindingType(type).toInstance(instance);
    }
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Class<? extends T> implementation) {
        return this.bindingType(type).to(implementation);
    }
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Provider<? extends T> provider) {
        return this.bindingType(type).toProvider(provider);
    }
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Key<? extends T> targetKey) {
        return this.bindingType(type).to(targetKey);
    }
    public <T> LinkedBindingBuilder<T> bindingType(String withName, Class<T> type) {
        return this.getGuiceBinder().bind(type).annotatedWith(Names.named(withName));
    }
    public <T> void bindingType(String withName, Class<T> type, T instance) {
        this.bindingType(withName, type).toInstance(instance);
    }
    public <T> ScopedBindingBuilder bindingType(String withName, Class<T> type, Class<? extends T> implementation) {
        return this.bindingType(withName, type).to(implementation);
    }
    public <T> ScopedBindingBuilder bindingType(String withName, Class<T> type, Provider<? extends T> provider) {
        return this.bindingType(withName, type).toProvider(provider);
    }
    public <T> ScopedBindingBuilder bindingType(String withName, Class<T> type, Key<? extends T> targetKey) {
        return this.bindingType(withName, type).to(targetKey);
    }
    /**�����ṩ Hasor �� Bean ע��֧�� */
    private class BeanInfoModuleBuilder implements Module {
        /*BeanInfo ����*/
        private final List<BeanMetaData> beanMetaDataList = new ArrayList<BeanMetaData>();
        //
        public BeanBindingBuilder newBeanDefine(Binder guiceBinder) {
            return new BeanBindingBuilderImpl(guiceBinder);
        }
        public void configure(Binder guiceBinder) {
            /*��BeanInfo�󶨵�Guice���ϣ�����ʽʹ��ʱ����findBindingsByType���������һ�����*/
            for (BeanMetaData define : this.beanMetaDataList)
                guiceBinder.bind(BeanMetaData.class).annotatedWith(UniqueAnnotations.create()).toInstance(define);
        }
        /*-----------------------------------------------------------------------------------------*/
        /** LinkedBindingBuilder�ӿڵĴ���ʵ�� */
        private class BeanBindingBuilderImpl implements BeanBindingBuilder {
            private Binder            guiceBinder = null;
            private ArrayList<String> names       = new ArrayList<String>();
            public BeanBindingBuilderImpl(Binder guiceBinder) {
                this.guiceBinder = guiceBinder;
            }
            public BeanBindingBuilder aliasName(String aliasName) {
                this.names.add(aliasName);
                return this;
            }
            public <T> LinkedBindingBuilder<T> bindType(Class<T> beanType) {
                if (this.names.isEmpty() == true)
                    throw new UnsupportedOperationException("the bean name is undefined!");
                LinkedBindingBuilder<T> beanBuilder = this.guiceBinder.bind(beanType);
                String[] aliasNames = this.names.toArray(new String[this.names.size()]);
                for (String nameItem : this.names) {
                    BeanMetaData beanInfo = new BeanMetaData(nameItem, aliasNames, beanType);
                    beanMetaDataList.add(beanInfo);
                }
                return beanBuilder;
            }
        }
    }
    public void registerAware(AppContextAware aware) {
        this.bindingType(AppContextAware.class).toInstance(aware);
    }
}