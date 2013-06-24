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
package org.platform.binder.support;
import java.util.ArrayList;
import java.util.List;
import org.platform.binder.ApiBinder.BeanBindingBuilder;
import org.platform.binder.BeanInfo;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.internal.UniqueAnnotations;
/**
 * ���ڴ���BeanInfoDefinition�ӿڶ���Ĵ���
 * @version : 2013-4-12
 * @author ������ (zyc@byshell.org)
 */
class BeanInfoModuleBuilder implements Module {
    /*BeanInfo ����*/
    private final List<BeanInfoDefinition> beanInfoDefinitions = new ArrayList<BeanInfoDefinition>();
    //
    public BeanBindingBuilder newBeanDefine(Binder binder) {
        return new BeanBindingBuilderImpl(binder);
    }
    @Override
    public void configure(Binder binder) {
        /*��BeanInfo�󶨵�Guice���ϣ�����ʽʹ��ʱ����findBindingsByType���������һ�����*/
        for (Provider<? extends BeanInfo> define : this.beanInfoDefinitions)
            binder.bind(BeanInfo.class).annotatedWith(UniqueAnnotations.create()).toProvider(define);
    }
    /*-----------------------------------------------------------------------------------------*/
    /** LinkedBindingBuilder�ӿڵĴ���ʵ�� */
    private class BeanBindingBuilderImpl implements BeanBindingBuilder {
        private Binder            binder = null;
        private ArrayList<String> names  = new ArrayList<String>();
        public BeanBindingBuilderImpl(Binder binder) {
            this.binder = binder;
        }
        @Override
        public BeanBindingBuilder aliasName(String aliasName) {
            this.names.add(aliasName);
            return this;
        }
        @Override
        public <T> LinkedBindingBuilder<T> bindType(Class<T> beanClass) {
            if (this.names.isEmpty() == true)
                throw new UnsupportedOperationException("the bean name is undefined!");
            Key<T> beanKey = Key.get(beanClass);
            LinkedBindingBuilder<T> beanBuilder = binder.bind(beanKey);
            String[] aliasNames = this.names.toArray(new String[this.names.size()]);
            for (String nameItem : this.names) {
                BeanInfoDefinition beanInfo = new BeanInfoDefinition(nameItem, aliasNames, beanKey, beanClass);
                beanInfoDefinitions.add(beanInfo);
            }
            return beanBuilder;
        }
    }
}