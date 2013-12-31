/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.core.setting;
import java.io.File;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import net.hasor.core.Settings;
import net.hasor.core.XmlNode;
import org.more.convert.ConverterUtils;
import org.more.util.ScanClassPath;
import org.more.util.StringUtils;
/**
 * Settings�ӿڵĳ���ʵ�֡�
 * @version : 2013-4-2
 * @author ������ (zyc@hasor.net)
 */
public abstract class AbstractSettings implements Settings {
    protected abstract Map<String, Object> getSettingsMap();
    /**��ȡָ��ĳ���ض������ռ��µ�Settings�ӿڶ���*/
    public abstract AbstractSettings getSetting(String namespace);
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ���ĳ��ע����ࣩ*/
    public Set<Class<?>> getClassSet(Class<?> featureType, String[] loadPackages) {
        if (featureType == null)
            return null;
        if (loadPackages == null)
            loadPackages = new String[] { "" };
        return ScanClassPath.getClassSet(loadPackages, featureType);
    }
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ���ĳ��ע����ࣩ*/
    public Set<Class<?>> getClassSet(Class<?> featureType, String loadPackages) {
        if (featureType == null)
            return null;
        loadPackages = (loadPackages == null) ? "" : loadPackages;
        String[] spanPackage = loadPackages.split(",");
        return this.getClassSet(featureType, spanPackage);
    }
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public final <T> T getToType(String name, Class<T> toType, T defaultValue) {
        Object oriObject = this.getSettingsMap().get(StringUtils.isBlank(name) ? "" : name.toLowerCase());
        if (oriObject == null)
            return defaultValue;
        //
        T var = null;
        if (oriObject instanceof String)
            //ԭʼ�������ַ�������Eval����
            var = (T) ConverterUtils.convert(toType, oriObject);
        else if (oriObject instanceof GlobalProperty)
            //ԭʼ������GlobalPropertyֱ��get
            var = ((GlobalProperty) oriObject).getValue(toType, defaultValue);
        else
            //�������Ͳ��账�����ݾ���Ҫ��ֵ��
            var = (T) oriObject;
        return var == null ? defaultValue : var;
    };
    public <T> T[] getToTypeArray(String name, Class<T> toType) {
        return this.getToTypeArray(name, toType, null);
    }
    public <T> T[] getToTypeArray(String name, Class<T> toType, T defaultValue) {
        ArrayList<T> targetObjects = new ArrayList<T>();
        for (String url : this.getSettingArray()) {
            T targetObject = this.getSetting(url).getToType(name, toType, defaultValue);
            if (targetObject == null)
                continue;//��
            //
            targetObjects.add(targetObject);
        }
        return targetObjects.toArray((T[]) Array.newInstance(toType, targetObjects.size()));
    }
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
    public Character[] getCharArray(String name) {
        return this.getToTypeArray(name, Character.class);
    }
    public Character[] getCharArray(String name, Character defaultValue) {
        return this.getToTypeArray(name, Character.class, defaultValue);
    }
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(String name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(String name, String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    };
    public String[] getStringArray(String name) {
        return this.getToTypeArray(name, String.class);
    }
    public String[] getStringArray(String name, String defaultValue) {
        return this.getToTypeArray(name, String.class, defaultValue);
    }
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(String name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(String name, Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    };
    public Boolean[] getBooleanArray(String name) {
        return this.getToTypeArray(name, Boolean.class);
    }
    public Boolean[] getBooleanArray(String name, Boolean defaultValue) {
        return this.getToTypeArray(name, Boolean.class, defaultValue);
    }
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(String name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(String name, Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    };
    public Short[] getShortArray(String name) {
        return this.getToTypeArray(name, Short.class);
    }
    public Short[] getShortArray(String name, Short defaultValue) {
        return this.getToTypeArray(name, Short.class, defaultValue);
    }
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(String name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(String name, Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    };
    public Integer[] getIntegerArray(String name) {
        return this.getToTypeArray(name, Integer.class);
    }
    public Integer[] getIntegerArray(String name, Integer defaultValue) {
        return this.getToTypeArray(name, Integer.class, defaultValue);
    }
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(String name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(String name, Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    };
    public Long[] getLongArray(String name) {
        return this.getToTypeArray(name, Long.class);
    }
    public Long[] getLongArray(String name, Long defaultValue) {
        return this.getToTypeArray(name, Long.class, defaultValue);
    }
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(String name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(String name, Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    };
    public Float[] getFloatArray(String name) {
        return this.getToTypeArray(name, Float.class);
    }
    public Float[] getFloatArray(String name, Float defaultValue) {
        return this.getToTypeArray(name, Float.class, defaultValue);
    }
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(String name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(String name, Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    };
    public Double[] getDoubleArray(String name) {
        return this.getToTypeArray(name, Double.class);
    }
    public Double[] getDoubleArray(String name, Double defaultValue) {
        return this.getToTypeArray(name, Double.class, defaultValue);
    }
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
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(String name, String format) {
        return this.getDate(name, format, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, String format, Date defaultValue) {
        String oriData = this.getToType(name, String.class);
        if (oriData == null || oriData.length() == 0)
            return defaultValue;
        //
        DateFormat dateFormat = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        dateFormat.setLenient(false);
        Date parsedDate = dateFormat.parse(oriData, pos); // ignore the result (use the Calendar)
        if (pos.getErrorIndex() >= 0 || pos.getIndex() != oriData.length() || parsedDate == null)
            return defaultValue;
        else
            return parsedDate;
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, String format, long defaultValue) {
        return this.getDate(name, format, new Date(defaultValue));
    };
    public Date[] getDateArray(String name) {
        return this.getDateArray(name, null, (Date) null);
    }
    public Date[] getDateArray(String name, Date defaultValue) {
        if (defaultValue == null)
            return this.getDateArray(name, null, (Date) null);
        else
            return this.getDateArray(name, null, defaultValue);
    }
    public Date[] getDateArray(String name, long defaultValue) {
        return this.getDateArray(name, null, defaultValue);
    }
    public Date[] getDateArray(String name, String format) {
        return this.getDateArray(name, format, null);
    }
    public Date[] getDateArray(String name, String format, Date defaultValue) {
        String[] oriDataArray = this.getToTypeArray(name, String.class);
        if (oriDataArray == null || oriDataArray.length == 0)
            return null;
        //
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        Date[] parsedDate = new Date[oriDataArray.length];
        for (int i = 0; i < oriDataArray.length; i++) {
            String oriData = oriDataArray[i];
            ParsePosition pos = new ParsePosition(0);
            parsedDate[i] = dateFormat.parse(oriData, pos); // ignore the result (use the Calendar)
            if (pos.getErrorIndex() >= 0 || pos.getIndex() != oriData.length() || parsedDate[i] == null)
                parsedDate[i] = (defaultValue == null) ? null : new Date(defaultValue.getTime());
        }
        return parsedDate;
    }
    public Date[] getDateArray(String name, String format, long defaultValue) {
        String[] oriDataArray = this.getToTypeArray(name, String.class);
        if (oriDataArray == null || oriDataArray.length == 0)
            return null;
        //
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        Date[] parsedDate = new Date[oriDataArray.length];
        for (int i = 0; i < oriDataArray.length; i++) {
            String oriData = oriDataArray[i];
            ParsePosition pos = new ParsePosition(0);
            parsedDate[i] = dateFormat.parse(oriData, pos); // ignore the result (use the Calendar)
            if (pos.getErrorIndex() >= 0 || pos.getIndex() != oriData.length() || parsedDate[i] == null)
                parsedDate[i] = new Date(defaultValue);
        }
        return parsedDate;
    }
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType) {
        return this.getToType(name, enmType, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType, T defaultValue) {
        return this.getToType(name, enmType, defaultValue);
    };
    public <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType) {
        return this.getToTypeArray(name, enmType, null);
    }
    public <T extends Enum<?>> T[] getEnumArray(String name, Class<T> enmType, T defaultValue) {
        return this.getToTypeArray(name, enmType, defaultValue);
    }
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
    public String[] getFilePathArray(String name) {
        return this.getFilePathArray(name, null);
    }
    public String[] getFilePathArray(String name, String defaultValue) {
        ArrayList<String> filePaths = new ArrayList<String>();
        for (String url : this.getSettingArray()) {
            String filePath = this.getSetting(url).getFilePath(name, defaultValue);
            if (filePath == null || filePath.length() == 0)
                continue;//��
            //
            int length = filePath.length();
            if (filePath.charAt(length - 1) == File.separatorChar)
                filePaths.add(filePath.substring(0, length - 1));
            else
                filePaths.add(filePath);
        }
        return filePaths.toArray(new String[filePaths.size()]);
    }
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
    public String[] getDirectoryPathArray(String name) {
        return this.getDirectoryPathArray(name, null);
    }
    public String[] getDirectoryPathArray(String name, String defaultValue) {
        ArrayList<String> directoryPaths = new ArrayList<String>();
        for (String url : this.getSettingArray()) {
            String filePath = this.getSetting(url).getDirectoryPath(name, defaultValue);
            if (filePath == null || filePath.length() == 0)
                continue;//��
            //
            int length = filePath.length();
            if (filePath.charAt(length - 1) == File.separatorChar)
                directoryPaths.add(filePath.substring(0, length - 1));
            else
                directoryPaths.add(filePath);
        }
        return directoryPaths.toArray(new String[directoryPaths.size()]);
    }
    /**����ȫ�����ò��������ҷ�����{@link XmlNode}��ʽ����*/
    public XmlNode getXmlProperty(String name) {
        return this.getToType(name, XmlNode.class, null);
    }
    public XmlNode[] getXmlPropertyArray(String name) {
        return this.getToTypeArray(name, XmlNode.class, null);
    }
}