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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.core.error.InitializationException;
import org.more.core.error.SupportException;
import org.more.util.ResourcesUtil;
import org.more.util.StringConvertUtil;
import org.more.util.attribute.IAttribute;
/**
 * Bean���������࣬���������ʵ���˿��Խ�Bean���Կ���������bean�л��߿�����map�С�
 * �����߿���ͨ����չBeanType���Խ��ܸ����bean���͡�ϵͳ���Ѿ�֧����
 * Map,Object,IAttribute,ServletRequest.getParameterMap()��
 * ��ʾ��Ĭ�ϵĿ�����ʽ�����(value)�������Ҫǳ��������Ҫ����changeDefaultCopy����
 * �ı��俽�����͡���ѡ�Ŀ�������������һ�������(value)��һ����ǳ����(ref)��
 * ʵ��������ᴦ��java8���������ͼ���string�Լ�date��һ��10�����͡���10�����ͻ����������ǳ������ʽִ�С�
 * @version 2009-5-20
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public abstract class CopyBeanUtil {
    /*˳�������ȼ�˳��*/
    public static final String[]              configs            = new String[] { "META-INF/resource/core/copybean_config.properties", "META-INF/copybean_config.properties", "copybean_config.properties" };
    private ArrayList<Convert<Object>>        convertList        = new ArrayList<Convert<Object>>();
    private ArrayList<PropertyReader<Object>> propertyReaderList = new ArrayList<PropertyReader<Object>>();
    private ArrayList<PropertyWrite<Object>>  propertyWriteList  = new ArrayList<PropertyWrite<Object>>();
    private boolean                           nullValueCP        = true;
    //
    //
    /**����JsonUtil�����ַ������л�ʹ��˫���Ż����� */
    protected CopyBeanUtil() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (String cfg : configs) {
            IAttribute<String> attList = ResourcesUtil.getPropertys(cfg);
            String convertList = attList.getAttribute("ConvertList");
            if (convertList != null) {
                String[] $convertList = convertList.split(",");
                for (String $con : $convertList)
                    this.convertList.add((Convert<Object>) this.createObject($con, loader));
            }
            String propertyReaderList = attList.getAttribute("PropertyReaderList");
            if (propertyReaderList != null) {
                String[] $propertyReaderList = propertyReaderList.split(",");
                for (String $prop : $propertyReaderList)
                    this.propertyReaderList.add((PropertyReader<Object>) this.createObject($prop, loader));
            }
            String propertyWriteList = attList.getAttribute("PropertyWriteList");
            if (propertyWriteList != null) {
                String[] $propertyWriteList = propertyWriteList.split(",");
                for (String $prop : $propertyWriteList)
                    this.propertyWriteList.add((PropertyWrite<Object>) this.createObject($prop, loader));
            }
        }
        IAttribute<String> attList = ResourcesUtil.getPropertys(configs);
        nullValueCP = StringConvertUtil.parseBoolean(attList.getAttribute("NullValueCP"), true);
    };
    private Object createObject(String className, ClassLoader loader) {
        try {
            Class<?> cls = loader.loadClass(className);
            return cls.newInstance();
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }
    public int copyPropertys(Object fromObject, Object toObject) {
        if (fromObject == null || toObject == null)
            return 0;//��ִ�п�����
        //
        for (PropertyReader<Object> reader : this.propertyReaderList) {
            Class<?> fromClass = fromObject.getClass();
            if (reader.getTargetClass().isAssignableFrom(fromClass) == false)
                continue;
            List<String> propertysNames = reader.getPropertyNames(fromObject);
            return this.copyPropertys(fromObject, toObject, propertysNames);
        }
        return 0;//û�п���������.
    };
    public int copyPropertys(Object fromObject, Object toObject, List<String> propertysNames) {
        if (fromObject == null || toObject == null || propertysNames == null)
            return 0;//��ִ�п�����
        HashMap<String, String> propertysMapping = new HashMap<String, String>();
        for (String name : propertysNames)
            propertysMapping.put(name, name);
        return this.copyPropertys(fromObject, toObject, propertysMapping);
    };
    public int copyPropertys(Object fromObject, Object toObject, Map<String, String> propertysMapping) {
        PropertyReader<Object> readerObject = null;
        PropertyWrite<Object> writeObject = null;
        //�õ�readerObject,writeObject
        for (PropertyReader<Object> reader : this.propertyReaderList)
            if (reader.getTargetClass().isAssignableFrom(fromObject.getClass()) == true) {
                readerObject = reader;
                break;
            }
        for (PropertyWrite<Object> write : this.propertyWriteList)
            if (write.getTargetClass().isAssignableFrom(toObject.getClass()) == true) {
                writeObject = write;
                break;
            }
        //
        if (readerObject == null || writeObject == null)
            throw new SupportException("Don't support Object " + fromObject + " or " + toObject);
        //copy
        int i = 0;
        for (String key : propertysMapping.keySet())
            try {
                String fromProp = key;
                String toProp = propertysMapping.get(key);
                //1.�Ƿ��ܶ�д
                if (readerObject.canReader(fromProp, fromObject) == false)
                    continue;
                Object newValue = readerObject.readProperty(fromProp, fromObject);
                if (this.nullValueCP == false)
                    continue;
                if (writeObject.canWrite(toProp, toObject, newValue) == false)
                    continue;
                //2.����ת��
                Class<?> toType = writeObject.getTargetClass();
                Convert<Object> userConv = null;
                for (Convert<Object> conv : convertList)
                    if (conv.checkConvert(toType) == true) {
                        userConv = conv;
                        break;
                    }
                //3.����
                if (userConv != null) {
                    newValue = userConv.convert(newValue);
                    if (writeObject.writeProperty(toProp, toObject, newValue) == true)
                        i++;
                }
            } catch (Exception e) {/*�������쳣*/}
        return i;
    };
    /**
     * ��ȡĿ������ֵ���÷���Ӧ��������ʵ�֡�
     * @return ����Ŀ������ֵ���÷���Ӧ��������ʵ�֡�
     */
    public Object readProperty(String propertyName, Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (PropertyReader<Object> reader : this.propertyReaderList)
            if (reader.getTargetClass().isAssignableFrom(target.getClass()) == true)
                if (reader.canReader(propertyName, target) == true)
                    return reader.readProperty(propertyName, target);
        return null;
    };
    /**
     * ��ȡĿ������ֵ���÷���Ӧ��������ʵ�֡�
     * @param propertyName Ҫд�������
     * @param target Ҫд���Ŀ��bean
     * @param newValue д�����ֵ
     * @return ����Ŀ������ֵ���÷���Ӧ��������ʵ�֡�
     */
    public boolean writeProperty(String propertyName, Object target, Object newValue) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (PropertyWrite<Object> write : this.propertyWriteList)
            if (write.getTargetClass().isAssignableFrom(target.getClass()) == true)
                if (write.canWrite(propertyName, target, newValue) == true)
                    return write.writeProperty(propertyName, target, newValue);
        return false;
    }
    /*---------------------------------------------------------------------------------*/
    private static CopyBeanUtil defaultUtil = null;
    /**������ζ�����һ���µ�CopyBeanUtilʵ����*/
    public static CopyBeanUtil newInstance() {
        try {
            return new CopyBeanUtil() {};
        } catch (Exception e) {
            if (e instanceof InitializationException == true)
                throw (InitializationException) e;
            if (e instanceof RuntimeException == true)
                throw (RuntimeException) e;
            throw new InitializationException(e);
        }
    };
    /**��ȡһ��CopyBeanUtilʵ�����÷���������һ�ε��ø÷���������ʵ������*/
    public static CopyBeanUtil getCopyBeanUtil() {
        if (defaultUtil == null)
            defaultUtil = newInstance();
        return defaultUtil;
    };
    public static int copyTo(Object fromObject, Object toObject) {
        return getCopyBeanUtil().copyPropertys(fromObject, toObject);//û�п���������.
    };
    public static int copyTo(Object fromObject, Object toObject, List<String> propertysNames) {
        return getCopyBeanUtil().copyPropertys(fromObject, toObject, propertysNames);
    };
    public static int copyTo(Object fromObject, Object toObject, Map<String, String> propertysMapping) {
        return getCopyBeanUtil().copyPropertys(fromObject, toObject, propertysMapping);
    };
    /**
     * ��ȡĿ������ֵ���÷���Ӧ��������ʵ�֡�
     * @return ����Ŀ������ֵ���÷���Ӧ��������ʵ�֡�
     */
    public static Object getProperty(String propertyName, Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return getCopyBeanUtil().readProperty(propertyName, target);
    };
    /**
     * ��ȡĿ������ֵ���÷���Ӧ��������ʵ�֡�
     * @param propertyName Ҫд�������
     * @param target Ҫд���Ŀ��bean
     * @param newValue д�����ֵ
     * @return ����Ŀ������ֵ���÷���Ӧ��������ʵ�֡�
     */
    public static boolean setProperty(String propertyName, Object target, Object newValue) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return getCopyBeanUtil().writeProperty(propertyName, target, newValue);
    };
}