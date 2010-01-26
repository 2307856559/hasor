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
import java.util.List;
import java.util.Set;
import org.more.core.copybean.BeanType;
import org.more.core.copybean.PropertyReaderWrite;
/**
 * Set���д����ʹ�ø�����Ϊ��д������ʵ�ִ�Set�����п������Ի�����Set�п������ԡ�
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class SetRW extends BeanType {
    /**  */
    private static final long serialVersionUID = -3987939968587042522L;
    @Override
    public boolean checkObject(Object object) {
        return object instanceof List;
    }
    @Override
    protected Iterator<String> iteratorNames(Object obj) {
        //alû���κ�����ֻ��Ϊ�˵õ�һ�������������ҿ��Խ��뵽getPropertyRW����
        ArrayList al = new ArrayList();
        al.add("1");
        return al.iterator();
    }
    @Override
    protected PropertyReaderWrite getPropertyRW(Object obj, String name) {
        SetReaderWrite prw = new SetReaderWrite();
        prw.setName(name);
        prw.setObject(obj);
        return prw;
    }
}
/**
 * Set���͵����Զ�д��
 * Date : 2009-5-21
 * @author ������
 */
@SuppressWarnings("unchecked")
class SetReaderWrite extends PropertyReaderWrite {
    /**  */
    private static final long serialVersionUID = 2185263906672697512L;
    @Override
    public Object get() {
        Set att = (Set) this.getObject();
        return att;
    }
    @Override
    public void set(Object value) {
        Set att = (Set) this.getObject();
        Set fromList = (Set) value;//׼����value��ֵ���õ�att��
        att.addAll(fromList);
    }
    @Override
    public Class<?> getPropertyClass() {
        return Object.class;
    }
}