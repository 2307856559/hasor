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
package org.hasor.context;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;
/**
 * �����ļ�����
 * @version : 2013-4-23
 * @author ������ (zyc@byshell.org)
 */
public interface Settings {
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ���ĳ��ע����ࣩ*/
    public Set<Class<?>> getClassSet(Class<?> featureType, String[] loadPackages);
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ���ĳ��ע����ࣩ*/
    public Set<Class<?>> getClassSet(Class<?> featureType, String loadPackages);
    /**��ȡָ��ĳ���ض������ռ��µ�Settings�ӿڶ���*/
    public String[] getNamespaceArray();
    /**��ȡָ��ĳ���ض������ռ��µ�Settings�ӿڶ���*/
    public Settings getNamespace(String namespace);
    /**ǿ������װ�������ļ����÷��������������ļ������¼���*/
    public void refresh() throws IOException;
    /**��������ļ��ı��¼���������*/
    public void addSettingsListener(HasorSettingListener listener);
    /**ɾ�������ļ��ı��¼���������*/
    public void removeSettingsListener(HasorSettingListener listener);
    /**������������ļ��ı��¼���������*/
    public HasorSettingListener[] getSettingListeners();
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(String name);
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character getChar(String name, Character defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(String name);
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(String name, String defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(String name);
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(String name, Boolean defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(String name);
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(String name, Short defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(String name);
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(String name, Integer defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(String name);
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(String name, Long defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(String name);
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(String name, Float defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(String name);
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(String name, Double defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(String name);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, Date defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, long defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType);
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType, T defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(String name);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(String name, String defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(String name);
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(String name, String defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link XmlProperty}��ʽ����*/
    public XmlProperty getXmlProperty(String name);
    //
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character[] getCharArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character[] getCharArray(String name, Character defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String[] getStringArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String[] getStringArray(String name, String defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean[] getBooleanArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean[] getBooleanArray(String name, Boolean defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short[] getShortArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short[] getShortArray(String name, Short defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer[] getIntegerArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer[] getIntegerArray(String name, Integer defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long[] getLongArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long[] getLongArray(String name, Long defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float[] getFloatArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float[] getFloatArray(String name, Float defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double[] getDoubleArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double[] getDoubleArray(String name, Double defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date[] getDateArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date[] getDateArray(String name, Date defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date[] getDateArray(String name, long defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType);
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType, T defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String[] getFilePathArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String[] getFilePathArray(String name, String defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String[] getDirectoryPathArray(String name);
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String[] getDirectoryPathArray(String name, String defaultValue);
    /**����ȫ�����ò��������ҷ�����{@link XmlProperty}��ʽ����*/
    public XmlProperty[] getXmlPropertyArray(String name);
}