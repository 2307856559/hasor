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
package net.hasor.core.context.rm.simple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.inject.Provider;
import net.hasor.core.Environment;
import net.hasor.core.RegisterInfo;
import net.hasor.core.binder.TypeRegister;
import net.hasor.core.binder.register.AbstractTypeRegister;
import net.hasor.core.builder.BeanBuilder;
import net.hasor.core.context.RegisterManager;
import net.hasor.core.context.RegisterManagerCreater;
/**
 * 
 * @version : 2014-5-10
 * @author ������ (zyc@byshell.org)
 */
public class SimpleRegisterManagerCreater implements RegisterManagerCreater {
    public RegisterManager create(Environment env) {
        return new SimpleRegisterManager();
    }
}
/*RegisterManager�ӿ�ʵ��*/
class SimpleRegisterManager implements RegisterManager, BeanBuilder {
    /*-----------------------------------------------------------------Collect SimpleTypeRegister*/
    private Map<Class<?>, List<RegisterInfo<?>>> registerInfoMap = new HashMap<Class<?>, List<RegisterInfo<?>>>();
    public <T> TypeRegister<T> registerType(Class<T> bindType) {
        List<RegisterInfo<?>> list = this.registerInfoMap.get(bindType);
        if (list == null)
            list = new ArrayList<RegisterInfo<?>>();
        SimpleTypeRegister<T> register = new SimpleTypeRegister<T>(bindType);
        list.add(register);
        return register;
    }
    public BeanBuilder getBeanBuilder() {
        return this;
    }
    /*---------------------------------------------------------------------------impl BeanBuilder*/
    public <T> T getInstance(RegisterInfo<T> oriType) {
        //1.��TypeRegister
        if (oriType instanceof SimpleTypeRegister == false) {
            Provider<T> provider = oriType.getProvider();
            if (provider != null)
                return provider.get();
            else
                return oriType.getType().newInstance();
        }
        //2.
        // TODO Auto-generated method stub
        return null;
    }
    public Iterator<RegisterInfo<?>> getRegisterIterator() {
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
    public <T> Iterator<RegisterInfo<T>> getRegisterIterator(Class<T> type) {
        List<RegisterInfo<?>> infoList = this.registerInfoMap.get(type);
        if (infoList == null || infoList.isEmpty())
            return new ArrayList<RegisterInfo<T>>(0).iterator();
        // 
        RegisterInfo<T>[] infoArray = (RegisterInfo<T>[]) infoList.toArray();
        return Arrays.asList(infoArray).iterator();
    }
}
//
/*---------------------------------------------------------------------------------------Util*/
class SimpleTypeRegister<T> extends AbstractTypeRegister<T> {
    public SimpleTypeRegister(Class<T> type) {
        super(type);
    }
}