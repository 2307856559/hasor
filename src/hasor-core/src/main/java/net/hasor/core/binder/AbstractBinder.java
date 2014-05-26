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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContextAware;
import net.hasor.core.Environment;
import net.hasor.core.EventCallBackHook;
import net.hasor.core.EventListener;
import net.hasor.core.Provider;
import net.hasor.core.Scope;
import net.hasor.core.Settings;
import net.hasor.core.binder.matcher.AopMatchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.more.util.StringUtils;
/**
 * ��׼�� {@link ApiBinder} �ӿ�ʵ�֣�Hasor �ڳ�ʼ��ģ��ʱ��Ϊÿ��ģ���������һ�� ApiBinder �ӿ�ʵ����
 * <p>���󷽷� {@link #configModule()} ,�᷵��һ���ӿ�( {@link net.hasor.core.ApiBinder.ModuleSettings ModuleSettings} )
 * �������õ�ǰģ�����������
 * <p><b><i>��ʾ��</i></b>ģ������� {@link net.hasor.core.module.ModuleProxy} ����Ϊ {@link #configModule()} �����ṩ֧�֡�
 * @see net.hasor.core.module.ModuleProxy
 * @version : 2013-4-12
 * @author ������ (zyc@hasor.net)
 */
public abstract class AbstractBinder implements ApiBinder {
    private Environment environment = null;
    //
    protected AbstractBinder(Environment envContext) {
        this.environment = envContext;
    }
    public Environment getEnvironment() {
        return this.environment;
    }
    public Settings getSettings() {
        return this.getEnvironment().getSettings();
    }
    public void registerAware(AppContextAware aware) {
        this.bindingType(AppContextAware.class).toInstance(aware);
    }
    public Set<Class<?>> findClass(Class<?> featureType) {
        if (featureType == null)
            return null;
        return this.getEnvironment().findClass(featureType);
    }
    //
    /*--------------------------------------------------------------------------------------Event*/
    public void pushListener(String eventType, EventListener eventListener) {
        this.getEnvironment().pushListener(eventType, eventListener);
    }
    public void addListener(String eventType, EventListener eventListener) {
        this.getEnvironment().addListener(eventType, eventListener);
    }
    public void removeListener(String eventType, EventListener eventListener) {
        this.getEnvironment().removeListener(eventType, eventListener);
    }
    public void fireSyncEvent(String eventType, Object... objects) {
        this.getEnvironment().fireSyncEvent(eventType, objects);
    }
    public void fireSyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.getEnvironment().fireSyncEvent(eventType, callBack, objects);
    }
    public void fireAsyncEvent(String eventType, Object... objects) {
        this.getEnvironment().fireAsyncEvent(eventType, objects);
    }
    public void fireAsyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.getEnvironment().fireAsyncEvent(eventType, callBack, objects);
    }
    //
    /*------------------------------------------------------------------------------------Binding*/
    /**ע��һ������*/
    protected abstract <T> TypeRegister<T> registerType(Class<T> type);
    public <T> NamedBindingBuilder<T> bindingType(Class<T> type) {
        /*��������ע��һ������*/
        TypeRegister<T> typeRegister = this.registerType(type);
        return new NamedBindingBuilderImpl<T>(typeRegister);
    }
    public <T> void bindingType(Class<T> type, T instance) {
        this.bindingType(type).toInstance(instance);
    }
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Class<? extends T> implementation) {
        return this.bindingType(type).to(implementation);
    }
    public <T> ScopedBindingBuilder bindingType(Class<T> type, Provider<T> provider) {
        return this.bindingType(type).toProvider(provider);
    }
    public <T> LinkedBindingBuilder<T> bindingType(String withName, Class<T> type) {
        return this.bindingType(type).nameWith(withName);
    }
    public <T> void bindingType(String withName, Class<T> type, T instance) {
        this.bindingType(type).nameWith(withName).toInstance(instance);
    }
    public <T> ScopedBindingBuilder bindingType(String withName, Class<T> type, Class<? extends T> implementation) {
        return this.bindingType(type).nameWith(withName).to(implementation);
    }
    public <T> ScopedBindingBuilder bindingType(String withName, Class<T> type, Provider<T> provider) {
        return this.bindingType(type).nameWith(withName).toProvider(provider);
    }
    //
    /*---------------------------------------------------------------------------------------Bean*/
    private static long referIndex = 0;
    private static long referIndex() {
        return referIndex++;
    }
    public BeanBindingBuilder defineBean(String beanName) {
        return new BeanBindingBuilderImpl().aliasName(beanName);
    }
    /** BeanBindingBuilder�ӿ�ʵ�� */
    private class BeanBindingBuilderImpl implements BeanBindingBuilder {
        private ArrayList<String>   names    = new ArrayList<String>();
        private Map<String, Object> property = new HashMap<String, Object>();
        public BeanBindingBuilder aliasName(String aliasName) {
            if (!StringUtils.isBlank(aliasName))
                this.names.add(aliasName);
            return this;
        }
        public BeanBindingBuilder setProperty(String attName, Object attValue) {
            this.property.put(attName, attValue);
            return this;
        }
        public <T> LinkedBindingBuilder<T> bindType(Class<T> beanType) {
            if (this.names.isEmpty() == true)
                throw new UnsupportedOperationException("the bean name is undefined!");
            /*��Bean����ע�ᵽHasor�ϣ����Ҹ������ID,���ں�BeanInfo�󶨡�*/
            String referID = beanType.getName() + "#" + String.valueOf(referIndex());
            LinkedBindingBuilder<T> returnData = bindingType(beanType).nameWith(referID);
            //
            String[] aliasNames = this.names.toArray(new String[this.names.size()]);
            BeanInfoData beanInfo = new BeanInfoData(aliasNames, referID, beanType, this.property);
            /*�����ֺ�BeanInfo��һ��*/
            for (String nameItem : this.names) {
                bindingType(BeanInfo.class).nameWith(nameItem).toInstance(beanInfo);
            }
            return returnData;
        }
    }
    /** NamedBindingBuilder�ӿ�ʵ�� */
    private class NamedBindingBuilderImpl<T> implements NamedBindingBuilder<T> {
        private TypeRegister<T> typeRegister;
        public NamedBindingBuilderImpl(TypeRegister<T> typeRegister) {
            this.typeRegister = typeRegister;
        }
        public ScopedBindingBuilder to(Class<? extends T> implementation) {
            this.typeRegister.toImpl(implementation);
            return this;
        }
        public MetaDataBindingBuilder toInstance(T instance) {
            this.typeRegister.toInstance(instance);
            return this;
        }
        public ScopedBindingBuilder toProvider(Provider<T> provider) {
            this.typeRegister.toProvider(provider);
            return this;
        }
        public ScopedBindingBuilder toConstructor(Constructor<? extends T> constructor) {
            this.typeRegister.toConstructor(constructor);
            return this;
        }
        public MetaDataBindingBuilder asEagerSingleton() {
            this.typeRegister.setSingleton();
            return this;
        }
        public LinkedBindingBuilder<T> nameWith(String name) {
            this.typeRegister.setName(name);
            return this;
        }
        public MetaDataBindingBuilder toScope(Scope scope) {
            this.typeRegister.setScope(scope);
            return this;
        }
        public MetaDataBindingBuilder metaData(String key, Object value) {
            this.typeRegister.setMetaData(key, value);
            return this;
        }
    }
    //
    /*---------------------------------------------------------------------------------------Bean*/
    public void bindInterceptor(String matcherExpression, MethodInterceptor interceptor) {
        Matcher<Class<?>> matcherClass = AopMatchers.expressionClass(matcherExpression);
        Matcher<Method> matcherMethod = AopMatchers.expressionMethod(matcherExpression);
        this.bindInterceptor(matcherClass, matcherMethod, interceptor);
    }
    public void bindInterceptor(Matcher<Class<?>> matcherClass, Matcher<Method> matcherMethod, MethodInterceptor interceptor) {
        AopMatcherRegisterData ard = new AopMatcherRegisterData(matcherClass, matcherMethod, interceptor);
        String referID = AopMatcherRegister.class.getName() + "#" + String.valueOf(referIndex());
        this.bindingType(AopMatcherRegister.class).nameWith(referID).toInstance(ard).metaData(AopConst.AopAssembly, true);/*����һ������*/
    }
}