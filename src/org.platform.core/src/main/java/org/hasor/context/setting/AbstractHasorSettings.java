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
package org.hasor.context.setting;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.hasor.context.Settings;
import org.hasor.context.XmlProperty;
import org.more.util.StringConvertUtils;
import org.more.util.StringUtils;
/**
 * Settings�ӿڵĳ���ʵ�֡�
 * @version : 2013-4-2
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractHasorSettings implements Settings {
    /**��ȡ������Map*/
    protected abstract Map<String, Object> getSettingMap();
    /**��ָ���������ļ���Ϊ�������ļ����롣*/
    public abstract void load(String mainConfig) throws IOException;
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public final <T> T getToType(String name, Class<T> toType, T defaultValue) {
        Object oriObject = this.getSettingMap().get(StringUtils.isBlank(name) ? "" : name.toLowerCase());
        if (oriObject == null)
            return defaultValue;
        //
        T var = null;
        if (oriObject instanceof String)
            //ԭʼ�������ַ�������Eval����
            var = StringConvertUtils.changeType((String) oriObject, toType);
        else if (oriObject instanceof GlobalProperty)
            //ԭʼ������GlobalPropertyֱ��get
            var = ((GlobalProperty) oriObject).getValue(toType, defaultValue);
        else
            //�������Ͳ��账�����ݾ���Ҫ��ֵ��
            var = (T) oriObject;
        return var;
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public final <T> T getToType(String name, Class<T> toType) {
        return this.getToType(name, toType, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ����*/
    public Object getObject(String name) {
        return this.getToType(name, Object.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Object getObject(String name, Object defaultValue) {
        return this.getToType(name, Object.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(String name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character getChar(String name, Character defaultValue) {
        return this.getToType(name, Character.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(String name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(String name, String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(String name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(String name, Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(String name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(String name, Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(String name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(String name, Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(String name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(String name, Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(String name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(String name, Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(String name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(String name, Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(String name) {
        return this.getToType(name, Date.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, Date defaultValue) {
        return this.getToType(name, Date.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, long defaultValue) {
        return this.getToType(name, Date.class, new Date(defaultValue));
    };
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType) {
        return this.getToType(name, enmType, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType, T defaultValue) {
        return this.getToType(name, enmType, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(String name) {
        return this.getFilePath(name, null);
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
    public String getDirectoryPath(String name) {
        return this.getDirectoryPath(name, null);
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
    }
    /**����ȫ�����ò��������ҷ�����{@link XmlProperty}��ʽ����*/
    public XmlProperty getXmlProperty(String name) {
        return this.getToType(name, XmlProperty.class, null);
    }
}