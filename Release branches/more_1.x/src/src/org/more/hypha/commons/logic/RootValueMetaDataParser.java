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
package org.more.hypha.commons.logic;
import java.util.HashMap;
import java.util.Map;
import org.more.core.error.RepeateException;
import org.more.core.error.SupportException;
import org.more.core.log.ILog;
import org.more.core.log.LogFactory;
import org.more.hypha.ApplicationContext;
import org.more.hypha.ValueMetaData;
/**
 * ����Ԫ��Ϣ�������ĸ���{@link ValueMetaDataParser}��������ڡ�
 * @version 2011-1-21
 * @author ������ (zyc@byshell.org)
 */
abstract class RootValueMetaDataParser implements ValueMetaDataParser<ValueMetaData> {
    private static ILog                                     log               = LogFactory.getLog(RootValueMetaDataParser.class);
    private Map<String, ValueMetaDataParser<ValueMetaData>> metaDataParserMap = new HashMap<String, ValueMetaDataParser<ValueMetaData>>();
    /*------------------------------------------------------------------------------*/
    /**�ڶ���������Ч����Ϊ{@link RootValueMetaDataParser}���Ǹ���*/
    public Object parser(Object targetObject, ValueMetaData data, ValueMetaDataParser<ValueMetaData> rootParser/*�ò�����Ч*/, ApplicationContext context) throws Throwable {
        if (data == null) {
            log.error("parser ValueMetaData to Object happen an error , ValueMetaData params is null, please check it.");
            return null;
        }
        String metaDataType = data.getMetaDataType();
        if (this.metaDataParserMap.containsKey(metaDataType) == false) {
            log.error("{%0} MetaData is doesn`t Support.", metaDataType);
            throw new SupportException(metaDataType + " MetaData is doesn`t Support.");
        }
        return this.metaDataParserMap.get(metaDataType).parser(targetObject, data, this, context);
    };
    /**ע��{@link ValueMetaDataParser}�����ע��Ľ����������ظ��������{@link RepeateException}�쳣��*/
    public void addParser(String metaDataType, ValueMetaDataParser<ValueMetaData> parser) throws RepeateException {
        if (metaDataType == null || parser == null) {
            log.warning("addParser error metaDataType or parser is null.");
            return;
        }
        if (this.metaDataParserMap.containsKey(metaDataType) == true)
            log.info("addParser {%0} is exist,use new engine Repeate it OK!", metaDataType);
        else
            log.debug("addParser {%0} OK!", metaDataType);
        this.metaDataParserMap.put(metaDataType, parser);
    };
    /**���ע��{@link ValueMetaDataParser}�����Ҫ�Ƴ��Ľ��������������Ҳ�����׳��쳣��*/
    public void removeParser(String metaDataType) {
        if (metaDataType == null) {
            log.warning("addParser error metaDataType is null.");
            return;
        }
        if (this.metaDataParserMap.containsKey(metaDataType) == true) {
            log.debug("removeParser {%0}.", metaDataType);
            this.metaDataParserMap.remove(metaDataType);
        }
    };
};