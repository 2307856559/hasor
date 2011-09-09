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
package org.more.core.copybean;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
/**
 * ִ�����Կ������࣬����������PropertyReaderWrite��Ĺ��������˶����Կ���������һ��
 * PropertyReaderWrite����ķ�����ʹ���Կ�����Ϊ���ܡ��������������ν������Կ����ġ�
 * @version 2009-5-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class Copy extends PropertyReaderWrite<Object> implements Serializable, Cloneable {
    /**  */
    private static final long serialVersionUID = -4040330892099587195L;
    /** ���ֶ��Ǵ��ִ�е�ǰ������CopyBeanUtil���� */
    protected CopyBeanUtil    copyBeanUtil     = null;
    /**
     * ����ִ�е�ǰ������CopyBeanUtil����
     * @param copyBeanUtil ִ�е�ǰ������CopyBeanUtil����
     */
    void setCopyBeanUtil(CopyBeanUtil copyBeanUtil) {
        this.copyBeanUtil = copyBeanUtil;
    }
    /** Bean�ڿ�����������֧�ֵ�����ת���� */
    private Collection<ConvertType>     convertType = null;
    /** ���Զ�д�������Կ���ʹ�ø����Ե�ֵ����������AbstractReaderWrite�е� */
    private PropertyReaderWrite<Object> rw          = null;
    /**
     * ���ÿ�����������֧�ֵ�����ת�����ϡ�
     * @param convertType ������������֧�ֵ�����ת�����ϡ�
     */
    void setConvertType(Collection<ConvertType> convertType) {
        this.convertType = convertType;
    }
    /**
     * ���ĳһ����������֧�ֵ�ת�����
     * @param from ��ʲô����
     * @param to ת����ʲô����
     * @return ����ĳһ����������֧�ֵ�ת�����
     */
    protected ConvertType getConvertType(Class<?> from, Class<?> to) {
        for (ConvertType type : this.convertType)
            if (type.checkType(from, to) == true)
                return type;
        return null;
    }
    /**
     * �������Ե�Ŀ��AbstractReaderWrite���Զ�д���С���������ɹ��򷵻�true���򷵻�fale��
     * ��������������ν��п�����
     * @param toObject ׼��������Ŀ�����
     * @return ��������ɹ��򷵻�true���򷵻�fale��
     */
    public abstract boolean copyTo(PropertyReaderWrite<Object> toObject);
    protected Object clone() throws CloneNotSupportedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            //
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return ois.readObject();
        } catch (Exception e) {
            throw new CloneNotSupportedException("�ڿ�¡ʱ�����쳣 msg=" + e.getMessage());
        }
    }
    public boolean canReader() {
        return this.rw.canReader();
    }
    public boolean canWrite() {
        return this.rw.canWrite();
    }
    public Object get() {
        return this.rw.get();
    }
    public String getName() {
        return this.rw.getName();
    }
    public Object getObject() {
        return this.rw.getObject();
    }
    public void set(Object value) {
        this.rw.set(value);
    }
    public void setName(String name) {
        this.rw.setName(name);
    }
    public void setObject(Object object) {
        this.rw.setObject(object);
    }
    public PropertyReaderWrite<Object> getRw() {
        return rw;
    }
    public void setRw(PropertyReaderWrite<Object> rw) {
        this.rw = rw;
    }
    public Class<?> getPropertyClass() {
        return this.rw.getPropertyClass();
    }
}