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
package org.more.hypha.beans.assembler;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.more.DoesSupportException;
import org.more.RepeateException;
import org.more.hypha.ApplicationContext;
import org.more.hypha.beans.ValueMetaData;
import org.more.hypha.beans.ValueMetaDataParser;
import org.more.util.ClassPathUtil;
import org.more.util.attribute.IAttribute;
/**
 * ����Ԫ��Ϣ�������ĸ���{@link ValueMetaDataParser}��������ڡ�����ᴦ��regedit-metadata.prop�������ļ���
 * @version 2011-1-21
 * @author ������ (zyc@byshell.org)
 */
public class RootValueMetaDataParser implements ValueMetaDataParser<ValueMetaData> {
    public static final String                              MetaDataConfig    = "/META-INF/resource/hypha/regedit-metadata.prop";         //HyphaApplicationContext��������Ϣ
    private Map<String, ValueMetaDataParser<ValueMetaData>> metaDataParserMap = new HashMap<String, ValueMetaDataParser<ValueMetaData>>();
    //----------------------------------------------------------------------------------------------------------
    /**��������Ԫ��Ϣ�������ĸ�����*/
    public RootValueMetaDataParser(ApplicationContext applicationContext, IAttribute flashContext) {};
    /**����{@link RootValueMetaDataParser#MetaDataConfig}��������ʾ�������ļ�������װ�������������{@link ValueMetaData}���͡�*/
    public void loadConfig() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        //װ��regedit-metadata.prop�����ļ���------------------------------------
        List<InputStream> ins = ClassPathUtil.getResource(MetaDataConfig);
        Properties prop = new Properties();
        for (InputStream is : ins)
            prop.load(is);
        for (Object key : prop.keySet()) {
            String beanBuilderClass = prop.getProperty((String) key);
            Object builder = Class.forName(beanBuilderClass).newInstance();
            this.addParser((String) key, (ValueMetaDataParser) builder);
        }
    };
    /**�ڶ���������Ч����Ϊ{@link RootValueMetaDataParser}���Ǹ���*/
    public Object parser(ValueMetaData data, ValueMetaDataParser<ValueMetaData> rootParser/*�ò�����Ч*/, ApplicationContext context) throws Throwable {
        String metaDataType = data.getMetaDataType();
        if (this.metaDataParserMap.containsKey(metaDataType) == false)
            throw new DoesSupportException("��֧�ֵ�ValueMetaData����������" + metaDataType);
        return this.metaDataParserMap.get(metaDataType).parser(data, this, context);
    };
    public Class<?> parserType(ValueMetaData data, ValueMetaDataParser<ValueMetaData> rootParser, ApplicationContext context) throws Throwable {
        String metaDataType = data.getMetaDataType();
        if (this.metaDataParserMap.containsKey(metaDataType) == false)
            throw new DoesSupportException("��֧�ֵ�ValueMetaData����������" + metaDataType);
        return this.metaDataParserMap.get(metaDataType).parserType(data, this, context);
    };
    /**ע��{@link ValueMetaDataParser}�����ע��Ľ����������ظ��������{@link RepeateException}�쳣��*/
    public void addParser(String metaDataType, ValueMetaDataParser<ValueMetaData> parser) throws RepeateException {
        if (this.metaDataParserMap.containsKey(metaDataType) == false)
            this.metaDataParserMap.put(metaDataType, parser);
    };
    /**���ע��{@link ValueMetaDataParser}�����Ҫ�Ƴ��Ľ��������������Ҳ�����׳��쳣��*/
    public void removeParser(String metaDataType) {
        if (this.metaDataParserMap.containsKey(metaDataType) == true)
            this.metaDataParserMap.remove(metaDataType);
    };
};