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
package org.more.hypha.beans.assembler.parser;
import org.more.hypha.beans.define.AbstractValueMetaData;
import org.more.log.ILog;
import org.more.log.LogFactory;
import org.more.util.attribute.IAttribute;
/**
 * ������һ�������࣬���������ʹ���仺�湦�ܡ�
 * @version : 2011-5-23
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractBase_Parser {
    private static ILog         log        = LogFactory.getLog(AbstractBase_Parser.class);
    private static final String CacheType  = "$ParserCacheType";
    private static final String CacheValue = "$ParserCacheValue";
    /**�ӻ����л�ȡ���������͡� */
    protected Class<?> getTypeForCache(AbstractValueMetaData data) {
        IAttribute fungiAtt = data.getFungi();
        if (fungiAtt.contains(CacheType) == true) {
            Class<?> setType = (Class<?>) fungiAtt.getAttribute(CacheType);
            log.debug("return from fungi cache ,cacheName is {%0}, type = {%1}", CacheType, setType);
            return setType;
        }
        log.debug("cache is null.");
        return null;
    }
    /**��������*/
    protected void putTypeToCache(AbstractValueMetaData data, Class<?> type) {
        IAttribute fungiAtt = data.getFungi();
        log.debug("cache ValueMetaData = {%0}, type = {%1}.", data, type);
        fungiAtt.setAttribute(CacheType, type);
    }
    /**�ӻ����л�ȡ������ֵ�� */
    protected Object getValueForCache(AbstractValueMetaData data) {
        IAttribute fungiAtt = data.getFungi();
        if (fungiAtt.contains(CacheValue) == true) {
            Object value = fungiAtt.getAttribute(CacheValue);
            log.debug("return from fungi cache ,cacheName is {%0}, value = {%1}", CacheValue, value);
            return value;
        }
        log.debug("cache is null.");
        return null;
    }
    /**����ֵ*/
    protected void putValueToCache(AbstractValueMetaData data, Object value) {
        IAttribute fungiAtt = data.getFungi();
        log.debug("cache ValueMetaData = {%0}, value = {%1}.", data, value);
        fungiAtt.setAttribute(CacheValue, value);
    }
}