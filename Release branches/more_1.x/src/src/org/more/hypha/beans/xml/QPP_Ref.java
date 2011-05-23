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
package org.more.hypha.beans.xml;
import org.more.hypha.AbstractPropertyDefine;
import org.more.hypha.ValueMetaData;
import org.more.hypha.beans.define.Relation_ValueMetaData;
import org.more.log.ILog;
import org.more.log.LogFactory;
import org.more.util.attribute.IAttribute;
/**
 * ��������Bean����ֵ��������
 * @version 2010-9-23
 * @author ������ (zyc@byshell.org)
 */
public class QPP_Ref implements QPP {
    private static ILog log = LogFactory.getLog(QPP_Ref.class);
    public ValueMetaData parser(IAttribute attribute, AbstractPropertyDefine property) {
        //1.����Ƿ���Խ���
        String value = (String) attribute.getAttribute("refBean");
        if (value == null)
            return null;
        //2.���н���
        Relation_ValueMetaData newMETA = new Relation_ValueMetaData();
        newMETA.setRefBean((String) value);
        newMETA.setRefPackage((String) attribute.getAttribute("refPackage"));
        log.debug("parser refBean id = {%0} , package = {%1}", value, newMETA.getRefPackage());
        return newMETA;
    }
}