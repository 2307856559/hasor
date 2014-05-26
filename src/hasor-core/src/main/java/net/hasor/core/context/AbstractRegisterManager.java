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
package net.hasor.core.context;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.hasor.core.binder.TypeRegister;
import net.hasor.core.context.listener.ContextInitializeListener;
import org.more.RepeateException;
import org.more.util.StringUtils;
/**
 * 
 * @version : 2014-5-10
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractRegisterManager implements RegisterManager, ContextInitializeListener {
    private List<TypeRegister<?>> tempRegisterList = new ArrayList<TypeRegister<?>>();
    /**��ȡTypeRegister��������*/
    protected Iterator<TypeRegister<?>> registerIterator() {
        return this.tempRegisterList.iterator();
    }
    /**ע��Bean*/
    public <T> TypeRegister<T> registerType(Class<T> type) {
        TypeRegister<T> register = this.createRegisterType(type);
        if (register == null)
            throw new NullPointerException();
        this.tempRegisterList.add(register);
        return register;
    }
    /**����һ��TypeRegister�ӿڶ���*/
    protected abstract <T> TypeRegister<T> createRegisterType(Class<T> type);
    //
    /*����register�Ƿ�Ϊ������*/
    private boolean ifAnonymity(TypeRegister<?> register) {
        return StringUtils.isBlank(register.getName());
    }
    public synchronized void doInitialize(AbstractAppContext appContext) {
        // TODO Auto-generated method stub
    }
    public void doInitializeCompleted(AbstractAppContext appContext) {
        Set<Class<?>> anonymityTypes = new HashSet<Class<?>>();
        for (TypeRegister<?> register : tempRegisterList) {
            if (ifAnonymity(register)) {
                /*��һ���жϷ�ֹ��ͬ�����͵�����ע���ظ�����configRegister����*/
                Class<?> bindType = register.getType();
                if (anonymityTypes.contains(bindType) == true)
                    throw new RepeateException(String.format("repeate bind Type : %s", bindType));
                anonymityTypes.add(bindType);
            }
        }
    }
}