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
package org.more.xml;
import java.util.HashMap;
import org.more.util.map.DecStackMap;
/**
 * ����̳���{@link DecStackMap}װ�������������ṩ��һ��context�����֧�֡�
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public class XmlStackDecorator<T> extends DecStackMap<String, Object> {
    private T context = null;
    /**��ȡContext*/
    public T getContext() {
        return context;
    }
    /**����Context*/
    public void setContext(T context) {
        this.context = context;
    }
    //---------------
    private HashMap<String, NameSpace> nameSpaceMap = new HashMap<String, NameSpace>();
    public NameSpace getNameSpace(String prefix) {
        return this.nameSpaceMap.get(prefix);
    }
    void addNameSpace(String prefix, NameSpace nameSpace) {
        this.nameSpaceMap.put(prefix, nameSpace);
    }
}