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
package org.more.hypha.configuration;
import java.lang.reflect.Method;
import java.util.Date;
import org.more.DoesSupportException;
import org.more.PropertyException;
import org.more.hypha.beans.define.VariableBeanDefine;
import org.more.util.StringConvert;
import org.more.util.StringUtil;
/**
 * namespace���з����漰����xml���඼��Ҫ���ɵ��࣬����Ŀ����Ϊ���ṩһ��ͳһ��{@link DefineResourceImpl}�����ȡ�ӿں�һЩTag����ʱ�Ĺ���������
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public abstract class Tag_Abstract {
    private DefineResourceImpl configuration = null;
    /**����Tag_Abstract����*/
    public Tag_Abstract(DefineResourceImpl configuration) {
        this.configuration = configuration;
    }
    /**��ȡ{@link DefineResourceImpl}����*/
    protected DefineResourceImpl getConfiguration() {
        return this.configuration;
    }
    //================================================================================================================�����Է���
    /**��ö���ж�����{@link VariableBeanDefine}����Ա�ʾ�Ļ������͡�*/
    protected enum VariableType {
        /**null���ݡ�*/
        Null,
        /**�������͡�*/
        Boolean,
        /**�ֽ����͡�*/
        Byte,
        /**���������͡�*/
        Short,
        /**�������͡�*/
        Int,
        /**���������͡�*/
        Long,
        /**�����ȸ��������͡�*/
        Float,
        /**˫���ȸ��������͡�*/
        Double,
        /**�ַ����͡�*/
        Char,
        /**�ַ������͡�*/
        String,
        /**ʱ������*/
        Date,
    }
    /**����ö�ٻ�ȡ���������Class��*/
    protected static Class<?> getBaseType(VariableType typeEnum) {
        if (typeEnum == null)
            return null;
        else if (typeEnum == VariableType.Boolean)
            return boolean.class;
        else if (typeEnum == VariableType.Byte)
            return byte.class;
        else if (typeEnum == VariableType.Short)
            return short.class;
        else if (typeEnum == VariableType.Int)
            return int.class;
        else if (typeEnum == VariableType.Long)
            return long.class;
        else if (typeEnum == VariableType.Float)
            return float.class;
        else if (typeEnum == VariableType.Double)
            return double.class;
        else if (typeEnum == VariableType.Char)
            return char.class;
        else if (typeEnum == VariableType.String)
            return String.class;
        else if (typeEnum == VariableType.Date)
            return Date.class;
        else
            return null;
    }
    /**����ĳ�����Ƶķ������÷���������һ��������*/
    private Method findMethod(String methodName, Class<?> type) {
        for (Method m : type.getMethods())
            if (m.getName().equals(methodName) == true)
                if (m.getParameterTypes().length == 1)
                    return m;
        return null;
    }
    /**ִ������ע�룬����ע��int,short,long,�Ȼ�������֮��÷�����֧��ע��ö�����͡�*/
    protected final void putAttribute(Object define, String attName, Object value) {
        if (define == null || attName == null)
            throw new NullPointerException("����������Ҫע���������Ϊ�ա�");
        //1.���ҷ���
        String methodName = "set" + StringUtil.toUpperCase(attName);
        Method writeMethod = this.findMethod(methodName, define.getClass());
        if (writeMethod == null)
            throw new DoesSupportException(define.getClass().getSimpleName() + "������֧��д����[" + attName + "]����(������[" + methodName + "]������)");
        //2.ִ������ת��
        Class<?> toType = writeMethod.getParameterTypes()[0];
        Object attValueObject = StringConvert.changeType(value, toType);
        //3.ִ������ע��
        try {
            writeMethod.invoke(define, attValueObject);
        } catch (Exception e) {
            throw new PropertyException("��Method.invoke�ڼ䷢���쳣���޷���" + attName + ",����д��[" + define + "]����" + e.getMessage());
        }
    };
}