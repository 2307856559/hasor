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
package org.more.util.attribute;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
/**
 * 
 * @version : 2011-7-22
 * @author ������ (zyc@byshell.org)
 */
public class SequenceStack<T> implements IAttribute<T> {
    private LinkedList<IAttribute<T>> attList = new LinkedList<IAttribute<T>>();
    //
    public void putStack(IAttribute<T> scope) {
        if (this.attList.contains(scope) == false)
            this.attList.addFirst(scope);
    };
    public boolean contains(String name) {
        for (IAttribute<?> iatt : this.attList)
            return iatt.contains(name);
        return false;
    };
    public T getAttribute(String name) {
        for (IAttribute<T> iatt : this.attList)
            return iatt.getAttribute(name);
        return null;
    };
    public String[] getAttributeNames() {
        ArrayList<String> as = new ArrayList<String>();
        for (IAttribute<?> iatt : this.attList)
            for (String n : iatt.getAttributeNames())
                if (as.contains(n) == false)
                    as.add(n);
        String[] array = new String[as.size()];
        as.toArray(array);
        return array;
    };
    public Map<String, T> toMap() {
        return new TransformToMap<T>(this);
    };
    /**�÷���ֻ������һ�������{@link IAttribute}���������á�*/
    public void clearAttribute() {
        this.attList.get(0).clearAttribute();
    };
    /**�÷���ֻ������һ�������{@link IAttribute}���������á�*/
    public void removeAttribute(String name) {
        this.attList.get(0).removeAttribute(name);
    };
    /**�÷���ֻ������һ�������{@link IAttribute}���������á�*/
    public void setAttribute(String name, T value) {
        this.attList.get(0).setAttribute(name, value);
    };
};