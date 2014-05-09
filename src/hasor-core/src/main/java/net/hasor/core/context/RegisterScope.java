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
package net.hasor.core.context;
import java.util.Iterator;
import java.util.List;
import net.hasor.core.RegisterInfo;
/**
 * 
 * @version : 2014-4-4
 * @author ������(zyc@hasor.net)
 */
public interface RegisterScope {
    /**����*/
    public RegisterScope getParentScope();
    /**��ȡ�÷�Χ�ڵ�����RegisterInfoע�ᡣ*/
    public Iterator<RegisterInfo<?>> getRegisterIterator();
    //
    /**����RegisterInfo*/
    public <T> List<RegisterInfo<T>> findRegisterInfo(Class<T> bindType);
    /**����RegisterInfo*/
    public <T> RegisterInfo<T> findRegisterInfo(String withName, Class<T> bindingType);
}