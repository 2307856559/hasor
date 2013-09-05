/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.more.convert.convert;
import java.lang.reflect.Array;
import java.util.Collection;
import org.more.convert.ConversionException;
import org.more.convert.Converter;
/**
 * Base {@link Converter} implementation that provides the structure
 * for handling conversion <b>to</b> and <b>from</b> a specified type.
 * <p>
 * This implementation provides the basic structure for
 * converting to/from a specified type optionally using a default
 * value or throwing a {@link ConversionException} if a
 * conversion error occurs.
 * <p>
 * Implementations should provide conversion to the specified
 * type and from the specified type to a <code>String</code> value
 * by implementing the following methods:
 * <ul>
 *     <li><code>convertToString(value)</code> - convert to a String
 *        (default implementation uses the objects <code>toString()</code>
 *        method).</li>
 *     <li><code>convertToType(Class, value)</code> - convert
 *         to the specified type</li>
 * </ul>
 *
 * @version $Revision: 640131 $ $Date: 2008-03-23 02:10:31 +0000 (Sun, 23 Mar 2008) $
 * @since 1.8.0
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractConverter implements Converter {
    /** ��ת������ʱ�Ƿ񷵻�Ĭ��ֵ��*/
    private boolean useDefault   = false;
    /**Ĭ��ֵ*/
    private Object  defaultValue = null;
    // ----------------------------------------------------------- Constructors
    /**��������<i>Converter</i>ת���������ܻ�����<code>ConversionException</code>�쳣��*/
    public AbstractConverter() {}
    /**��������<i>Converter</i>ת���������ܻ�����<code>ConversionException</code>�쳣��*/
    public AbstractConverter(Object defaultValue) {
        setDefaultValue(defaultValue);
    }
    // --------------------------------------------------------- Public Methods
    /**
     * ��ת���ڼ䷢���쳣ʱ�Ƿ�ʹ��Ĭ��ֵ��
     * @return ���<code>true</code>���ʾ����������ʱ���õ�Ĭ��ֵ�ᱻ���ء����<code>false</code>������{@link ConversionException}�쳣��
     */
    public boolean isUseDefault() {
        return this.useDefault;
    }
    /**
     * Convert the input object into an output object of the
     * specified type.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     * @return The converted value.
     * @throws ConversionException if conversion cannot be performed
     * successfully and no default is specified.
     */
    public Object convert(Class type, Object value) {
        Class sourceType = value == null ? null : value.getClass();
        Class targetType = primitive(type == null ? getDefaultType() : type);
        value = convertArray(value);//�������Դ��һ��Array �� ���� ��ôȡ�õ�һ��Ԫ�ء�
        //Missing Value
        if (value == null)
            return handleMissing(targetType);
        //
        sourceType = value.getClass();
        try {
            /*Convert --> String*/
            if (targetType.equals(String.class))
                return convertToString(value);
            /*No conversion necessary*/
            else if (targetType.equals(sourceType))
                return value;
            /*Convert --> Type*/
            else
                return convertToType(targetType, value);
        } catch (Throwable t) {
            return handleError(targetType, value, t);
        }
    }
    /**
     * ����ת������<p>
     * ���������default��������������ʱ����Ĭ��ֵ����������{@link ConversionException}�쳣��
     */
    protected Object handleError(Class type, Object value, Throwable cause) {
        if (this.useDefault)
            return handleMissing(type);
        if (cause instanceof ConversionException)
            throw (ConversionException) cause;
        else {
            String msg = "Error converting from '" + value.getClass() + "' to '" + type + "' " + cause.getMessage();
            throw new ConversionException(msg, cause);
        }
    }
    /**
     * ת�������ΪString��ʽ��<p>
     * <b>ע�⣺</b>���������ʹ��<code>toString()</code>ʵ�ָù��ܣ�����Ӧ����д�÷�������������ת�����̡�
     */
    protected String convertToString(Object value) throws Throwable {
        return value.toString();
    }
    /**ִ������ת�����롣*/
    protected abstract Object convertToType(Class type, Object value) throws Throwable;
    /**
     * Return the first element from an Array (or Collection)
     * or the value unchanged if not an Array (or Collection).
     *
     * N.B. This needs to be overriden for array/Collection converters.
     *
     * @param value The value to convert
     * @return The first element in an Array (or Collection)
     * or the value unchanged if not an Array (or Collection)
     */
    protected Object convertArray(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            if (Array.getLength(value) > 0)
                return Array.get(value, 0);
            else
                return null;
        }
        if (value instanceof Collection) {
            Collection collection = (Collection) value;
            if (collection.size() > 0)
                return collection.iterator().next();
            else
                return null;
        }
        return value;
    }
    /**����Ĭ��ֵ */
    protected void setDefaultValue(Object defaultValue) {
        this.useDefault = false;
        if (defaultValue == null)
            this.defaultValue = null;
        else
            this.defaultValue = convert(getDefaultType(), defaultValue);
        this.useDefault = true;
    }
    /**��ȡĬ��ֵ*/
    protected abstract Class getDefaultType();
    /**����ָ�����͵�Ĭ��ֵ.*/
    protected Object getDefault(Class type) {
        if (type.equals(String.class))
            return null;
        else
            return defaultValue;
    }
    /**
     * Provide a String representation of this converter.
     * @return A String representation of this converter
     */
    public String toString() {
        return toString(getClass()) + "[UseDefault=" + useDefault + "]";
    }
    /**��������ֵ������߷���ֵΪ�յ�ʱ��*/
    protected Object handleMissing(Class type) {
        if (this.useDefault || type.equals(String.class)) {
            Object value = getDefault(type);
            if (this.useDefault && value != null && !(type.equals(value.getClass()))) {
                try {
                    value = convertToType(type, defaultValue);
                } catch (Throwable t) {
                    //log().error("    Default conversion to " + toString(type) + "failed: " + t);// TODO Log
                }
            }
            return value;
        }
        throw new ConversionException("No value specified for '" + toString(type) + "'");
    }
    // ----------------------------------------------------------- Package Methods
    /** ת���������͵���װ����. */
    private Class primitive(Class type) {
        if (type == null || !type.isPrimitive()) {
            return type;
        }
        if (type == Integer.TYPE) {
            return Integer.class;
        } else if (type == Double.TYPE) {
            return Double.class;
        } else if (type == Long.TYPE) {
            return Long.class;
        } else if (type == Boolean.TYPE) {
            return Boolean.class;
        } else if (type == Float.TYPE) {
            return Float.class;
        } else if (type == Short.TYPE) {
            return Short.class;
        } else if (type == Byte.TYPE) {
            return Byte.class;
        } else if (type == Character.TYPE) {
            return Character.class;
        } else {
            return type;
        }
    }
    //
    //
    /**
     * Provide a String representation of a <code>java.lang.Class</code>.
     * @param type The <code>java.lang.Class</code>.
     * @return The String representation.
     */
    public String toString(Class type) {
        String typeName = null;
        if (type == null) {
            typeName = "null";
        } else if (type.isArray()) {
            Class elementType = type.getComponentType();
            int count = 1;
            while (elementType.isArray()) {
                elementType = elementType.getComponentType();
                count++;
            }
            typeName = elementType.getName();
            for (int i = 0; i < count; i++)
                typeName += "[]";
        } else {
            typeName = type.getName();
        }
        final String PACKAGE = "org.more.convert.convert.";
        if (typeName.startsWith("java.lang.") || typeName.startsWith("java.util.") || typeName.startsWith("java.math.")) {
            typeName = typeName.substring("java.lang.".length());
        } else if (typeName.startsWith(PACKAGE)) {
            typeName = typeName.substring(PACKAGE.length());
        }
        return typeName;
    }
}