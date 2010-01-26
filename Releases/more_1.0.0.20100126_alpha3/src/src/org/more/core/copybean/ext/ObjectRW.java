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
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import org.more.core.copybean.BeanType;
import org.more.core.copybean.PropertyReaderWrite;
/**
 * �������д����ʹ�ø�����Ϊ��д������ʵ�ִӶ����п������Ի���������п������ԡ�
 * �������Զ�дʱֻ֧�ֱ�׼get/set����������Boolean����(��װ����)ֻ֧��get/set��
 * �����boolean����(��������)��ֻ֧��is,setis��
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class ObjectRW extends BeanType {
    /**  */
    private static final long serialVersionUID = -7254414264895159995L;
    @Override
    public boolean checkObject(Object object) {
        return true;
    }
    @Override
    protected Iterator<String> iteratorNames(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        ArrayList<String> ns = new ArrayList<String>(0);
        for (Field n : fields)
            ns.add(n.getName());
        return ns.iterator();
    }
    @Override
    protected PropertyReaderWrite getPropertyRW(Object obj, String name) {
        ObjectReaderWrite prw = new ObjectReaderWrite();
        prw.setName(name);
        prw.setObject(obj);
        prw.init();
        return prw;
    }
}
/**
 * Object���͵����Զ�д���������Զ�д��ֻ��Ա�׼get/set
 * Date : 2009-5-21
 * @author ������
 */
class ObjectReaderWrite extends PropertyReaderWrite {
    /**  */
    private static final long serialVersionUID = 677145100804681671L;
    private Method            read_method      = null;               //��ȡ����
    private Method            write_method     = null;               //д�뷽��
    private Class<?>          type             = null;               //
    /** ��ȡget/set���� */
    public void init() {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(this.getName(), this.getObject().getClass());
            this.read_method = pd.getReadMethod();
            this.write_method = pd.getWriteMethod();
            this.type = pd.getPropertyType();
        } catch (Exception e) {
            this.read_method = null;
            this.write_method = null;
            this.type = null;
        }
    }
    @Override
    public Object get() {
        try {
            return this.read_method.invoke(this.getObject());
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public void set(Object value) {
        if (value == null)
            return;
        //
        try {
            Object inObject = value;
            //���õ�Ŀ������������
            if (this.write_method.getParameterTypes()[0].isArray() == true)
                inObject = new Object[] { value };
            //���õ�Ŀ�����Բ�������
            else if (this.write_method.getParameterTypes()[0].isArray() == false)
                if (inObject.getClass().isArray() == true)
                    inObject = Array.get(value, 0);
            //����Ŀ��д�뷽���Զ�����п���
            this.write_method.invoke(this.getObject(), inObject);
        } catch (Exception e) {}
    }
    @Override
    public boolean canWrite() {
        return (this.write_method == null) ? false : true;
    }
    @Override
    public boolean canReader() {
        return (this.read_method == null) ? false : true;
    }
    @Override
    public Class<?> getPropertyClass() {
        return this.type;
    }
}