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
package org.more.core.copybean.ext;
import java.util.ArrayList;
import java.util.Iterator;
import org.more.core.copybean.BeanType;
import org.more.core.copybean.PropertyReaderWrite;
import org.more.util.attribute.IAttribute;
/**
 * IAttribute���д����ʹ�ø�����Ϊ��д������ʵ�ִ�IAttribute�����п������Ի�����IAttribute�п������ԡ�
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class AttRW extends BeanType<IAttribute<Object>> {
    /**  */
    private static final long serialVersionUID = 5550209216691841191L;
    public boolean checkObject(Object object) {
        return object instanceof IAttribute;
    }
    protected Iterator<String> iteratorNames(IAttribute<Object> obj) {
        ArrayList<String> ns = new ArrayList<String>(0);
        for (String n : obj.getAttributeNames())
            ns.add(n);
        return ns.iterator();
    }
    protected PropertyReaderWrite<IAttribute<Object>> getPropertyRW(IAttribute<Object> obj, String name) {
        AttReaderWrite prw = new AttReaderWrite();
        prw.setName(name);
        prw.setObject(obj);
        return prw;
    }
}
/**
 * IAttribute���͵����Զ�д��
 * Date : 2009-5-21
 * @author ������
 */
class AttReaderWrite extends PropertyReaderWrite<IAttribute<Object>> {
    /**  */
    private static final long serialVersionUID = -2857886652147342020L;
    public Object get() {
        IAttribute<Object> att = this.getObject();
        return att.getAttribute(this.getName());
    }
    public void set(Object value) {
        IAttribute<Object> att = this.getObject();
        att.setAttribute(this.getName(), value);
    }
    public Class<?> getPropertyClass() {
        return Object.class;
    }
}