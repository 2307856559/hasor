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
package org.more.core.global;
import java.io.File;
import java.util.Date;
import org.more.util.attribute.IAttribute;
/**
* ȫ�ֳ�����ȡ��
* @version : 2011-9-3
* @author ������ (zyc@byshell.org)
*/
public abstract class Global extends AbstractGlobal {
    /*------------------------------------------------------------------------*/
    public Global(IAttribute<Object> configs) {
        super(configs);
    };
    public Global() {
        super();
    };
    /*------------------------------------------------------------------------*/
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ����*/
    public Object getObject(Enum<?> name) {
        return this.getToType(name, Object.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ����*/
    public Object getObject(String name) {
        return this.getToType(name, Object.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Object getObject(Enum<?> name, Object defaultValue) {
        return this.getToType(name, Object.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Object getObject(String name, Object defaultValue) {
        return this.getToType(name, Object.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(Enum<?> name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(String name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character getChar(Enum<?> name, Character defaultValue) {
        return this.getToType(name, Character.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character getChar(String name, Character defaultValue) {
        return this.getToType(name, Character.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(Enum<?> name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(String name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(Enum<?> name, String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(String name, String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(Enum<?> name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(String name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(Enum<?> name, Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(String name, Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(Enum<?> name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(String name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(Enum<?> name, Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(String name, Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(Enum<?> name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(String name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(Enum<?> name, Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(String name, Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(Enum<?> name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(String name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(Enum<?> name, Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(String name, Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(Enum<?> name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(String name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(Enum<?> name, Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(String name, Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(Enum<?> name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(String name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(Enum<?> name, Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(String name, Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(Enum<?> name) {
        return this.getToType(name, Date.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(String name) {
        return this.getToType(name, Date.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(Enum<?> name, Date defaultValue) {
        return this.getToType(name, Date.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, Date defaultValue) {
        return this.getToType(name, Date.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(Enum<?> name, long defaultValue) {
        return this.getToType(name, Date.class, new Date(defaultValue));
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, long defaultValue) {
        return this.getToType(name, Date.class, new Date(defaultValue));
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(Enum<?> name) {
        return this.getFilePath(name, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(String name) {
        return this.getFilePath(name, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(Enum<?> name, String defaultValue) {
        String filePath = this.getToType(name, String.class);
        if (filePath == null || filePath.length() == 0)
            return defaultValue;//��
        //
        int length = filePath.length();
        if (filePath.charAt(length - 1) == File.separatorChar)
            return filePath.substring(0, length - 1);
        else
            return filePath;
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(String name, String defaultValue) {
        String filePath = this.getToType(name, String.class);
        if (filePath == null || filePath.length() == 0)
            return defaultValue;//��
        //
        int length = filePath.length();
        if (filePath.charAt(length - 1) == File.separatorChar)
            return filePath.substring(0, length - 1);
        else
            return filePath;
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(Enum<?> name) {
        return this.getDirectoryPath(name, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(String name) {
        return this.getDirectoryPath(name, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(Enum<?> name, String defaultValue) {
        String filePath = this.getToType(name, String.class);
        if (filePath == null || filePath.length() == 0)
            return defaultValue;//��
        //
        int length = filePath.length();
        if (filePath.charAt(length - 1) == File.separatorChar)
            return filePath;
        else
            return filePath + File.separatorChar;
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(String name, String defaultValue) {
        String filePath = this.getToType(name, String.class);
        if (filePath == null || filePath.length() == 0)
            return defaultValue;//��
        //
        int length = filePath.length();
        if (filePath.charAt(length - 1) == File.separatorChar)
            return filePath;
        else
            return filePath + File.separatorChar;
    };
};