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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.more.core.copybean.BeanType;
import org.more.core.copybean.PropertyReaderWrite;
import org.more.util.BeanUtil;
/**
 * �������д����ʹ�ø�����Ϊ��д������ʵ�ִӶ����п������Ի���������п������ԡ�
 * �������Զ�дʱֻ֧�ֱ�׼get/set����������Boolean����(��װ����)ֻ֧��get/set��
 * �����boolean����(��������)��ֻ֧��is,setis��
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public class ObjectRW extends BeanType<Object> {
    /**  */
    private static final long serialVersionUID = -7254414264895159995L;
    public boolean checkObject(Object object) {
        return true;
    }
    protected Iterator<String> iteratorNames(Object obj) {
        List<String> fields = BeanUtil.getPropertysAndFields(obj.getClass());
        return fields.iterator();
    }
    protected PropertyReaderWrite<Object> getPropertyRW(Object obj, String name) {
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
class ObjectReaderWrite extends PropertyReaderWrite<Object> {
    /**  */
    private static final long serialVersionUID = 677145100804681671L;
    private boolean           canRead          = false;              //��ȡ����
    private boolean           canWrite         = false;              //д�뷽��
    //
    private Class<?>          type             = null;               //
    /** ��ȡget/set���� */
    public void init() {
        Field field = BeanUtil.getField(this.getName(), this.getObject().getClass());
        if (field != null)
            canRead = canWrite = true;//ȫ������true
        Method readM = BeanUtil.getReadMethod(this.getName(), this.getObject().getClass());
        if (readM != null && canRead == false)
            canRead = true;
        Method writeM = BeanUtil.getWriteMethod(this.getName(), this.getObject().getClass());
        if (writeM != null && canWrite == false)
            canWrite = true;
    }
    public Object get() {
        return BeanUtil.readPropertyOrField(this.getObject(), this.getName());
    }
    public void set(Object value) {
        BeanUtil.writePropertyOrField(this.getObject(), this.getName(), value);
    }
    public boolean canWrite() {
        return this.canWrite;
    }
    public boolean canReader() {
        return this.canRead;
    }
    public Class<?> getPropertyClass() {
        return this.type;
    }
}