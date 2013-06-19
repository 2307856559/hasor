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
package org.more.hypha.xml.tags.beans.qpp;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.hypha.AbstractPropertyDefine;
import org.more.hypha.ValueMetaData;
import org.more.hypha.define.URI_ValueMetaData;
import org.more.util.attribute.IAttribute;
/**
 * ��������ֵ��������Ĭ��ֵ��null��
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class QPP_URILocation implements QPP {
    private static Log log = LogFactory.getLog(QPP_URILocation.class);
    public ValueMetaData parser(IAttribute<String> attribute, AbstractPropertyDefine property) {
        String value = attribute.getAttribute("uriLocation");
        if (value == null)
            return null;
        //2.���н���
        URI_ValueMetaData newMETA = new URI_ValueMetaData();
        newMETA.setUriObject(value);
        log.debug("parser URI = {%0}", value);
        return newMETA;
    }
}