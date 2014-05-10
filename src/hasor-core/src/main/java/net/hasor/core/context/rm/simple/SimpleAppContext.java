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
package net.hasor.core.context.rm.simple;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.inject.Provider;
import net.hasor.core.AppContext;
import net.hasor.core.RegisterInfo;
import net.hasor.core.Scope;
import net.hasor.core.binder.TypeRegister;
import net.hasor.core.binder.register.AbstractTypeRegister;
import net.hasor.core.builder.BeanBuilder;
import net.hasor.core.context.AbstractConfigResourceAppContext;
import org.more.util.StringUtils;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.name.Names;
/**
 * {@link AppContext}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public class SimpleAppContext extends AbstractConfigResourceAppContext {
    /**�����������ļ�*/
    public SimpleAppContext() throws IOException, URISyntaxException {
        super();
    }
    /**�����������ļ�*/
    public SimpleAppContext(File mainSettings) {
        super(mainSettings);
    }
    /**�����������ļ�*/
    public SimpleAppContext(URI mainSettings) {
        super(mainSettings);
    }
    /**�����������ļ�*/
    public SimpleAppContext(String mainSettings) throws IOException, URISyntaxException {
        super(mainSettings);
    }
    //
    /*-------------------------------------------------------------------------------RegisterInfo*/
    private Map<Class<?>, List<RegisterInfo<?>>> registerInfoMap = new HashMap<Class<?>, List<RegisterInfo<?>>>();
    protected <T> TypeRegister<T> registerType(Class<T> bindType) {
        List<RegisterInfo<?>> list = this.registerInfoMap.get(bindType);
        if (list == null)
            list = new ArrayList<RegisterInfo<?>>();
        AbstractTypeRegister<T> register = new AbstractTypeRegister<T>(bindType) {};
        list.add(register);
        return register;
    }
    protected Iterator<RegisterInfo<?>> localRegisterIterator() {
        /*�÷������߼����õ���������Map��Value���б�ʾ��RegisterInfo����������Map��Value��һ��List����˵������б�Ҫ�ڽ��ж��εݽ�������*/
        final Iterator<List<RegisterInfo<?>>> entIterator = this.registerInfoMap.values().iterator();
        return new Iterator<RegisterInfo<?>>() {
            private Iterator<RegisterInfo<?>> regIterator = new ArrayList<RegisterInfo<?>>(0).iterator();
            public RegisterInfo<?> next() {
                while (true) {
                    if (this.regIterator.hasNext() == false) {
                        /*1.��ǰList�������ˣ�����û�пɵ�����List�� --> break */
                        if (entIterator.hasNext() == false)
                            break;
                        /*2.��ǰList�������ˣ�������һ��List*/
                        this.regIterator = entIterator.next().iterator();
                    }
                    /*һ��Ҫ���ж�һ�飬����ܿ�����һ��������û���ݶ��׳�NoSuchElementException�쳣�������ʱ���׳�����쳣�ǲ��ʵ��ġ�*/
                    if (this.regIterator.hasNext())
                        /*3.��ǰ������������*/
                        break;
                }
                //
                if (this.regIterator.hasNext() == false)
                    throw new NoSuchElementException();
                return regIterator.next();
            }
            public boolean hasNext() {
                if (entIterator.hasNext() == false && regIterator.hasNext() == false)
                    return false;
                return true;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    protected <T> Iterator<RegisterInfo<T>> localRegisterIterator(Class<T> type) {
        List<RegisterInfo<?>> infoList = this.registerInfoMap.get(type);
        if (infoList==null ||| infoList.isEmpty())
            return new ArrayList<T>(0).iterator();
        // TODO Auto-generated method stub
        return null;
    }
    //
    /*--------------------------------------------------------------------------------BeanBuilder*/
    protected BeanBuilder getBeanBuilder() {
        return new BeanBuilder() {
            public <T> T getInstance(RegisterInfo<T> oriType) {
                Provider<T> provider = oriType.getProvider();
                if (provider != null)
                    return provider.get();
                return getGuice().getInstance(oriType.getType());
            }
        };
    }
    private Injector guiceInjector = null;
    /**��ȡGuice*/
    public Injector getGuice() {
        return this.guiceInjector;
    }
    //
    /*------------------------------------------------------------------------------------Process*/
    protected void doInitialize() {
        //do nothing
    }
    protected void doInitializeCompleted() {
        Guice.createInjector(new com.google.inject.Module() {
            public void configure(Binder binder) {
                Iterator<RegisterInfo<?>> localRegister = localRegisterIterator();
                while (localRegister.hasNext()) {
                    AbstractTypeRegister<Object> register = (AbstractTypeRegister<Object>) localRegister.next();
                    //1.������
                    AnnotatedBindingBuilder<Object> annoBinding = binder.bind(register.getType());
                    LinkedBindingBuilder<Object> linkedBinding = annoBinding;
                    ScopedBindingBuilder scopeBinding = annoBinding;
                    //2.������
                    String name = register.getName();
                    if (!StringUtils.isBlank(name))
                        linkedBinding = annoBinding.annotatedWith(Names.named(name));
                    //3.��ʵ��
                    if (register.getProvider() != null)
                        scopeBinding = linkedBinding.toProvider(new IntProvider(register.getProvider()));
                    else if (register.getImplConstructor() != null)
                        scopeBinding = linkedBinding.toConstructor(register.getImplConstructor());
                    else if (register.getImplType() != null)
                        scopeBinding = linkedBinding.to(register.getImplType());
                    //4.������
                    if (register.isSingleton()) {
                        scopeBinding.asEagerSingleton();
                        continue;/*���岽�����д���*/
                    }
                    //5.��������
                    Scope scope = register.getScope();
                    if (scope != null)
                        scopeBinding.in(new IntScope(scope));
                    //
                }
            }
        });
    }
    protected void doStart() {
        //do nothing
    }
    protected void doStartCompleted() {
        //do nothing
    }
    private static class IntProvider implements com.google.inject.Provider<Object> {
        private Provider<Object> provider;
        public IntProvider(Provider<Object> provider) {
            this.provider = provider;
        }
        public Object get() {
            return this.provider.get();
        }
    };
    private static class IntScope implements com.google.inject.Scope {
        private Scope scope = null;
        public IntScope(Scope scope) {
            this.scope = scope;
        }
        public String toString() {
            return this.scope.toString();
        };
        public <T> com.google.inject.Provider<T> scope(Key<T> key, com.google.inject.Provider<T> unscoped) {
            // TODO Auto-generated method stub
            return null;s
        }
    };
}