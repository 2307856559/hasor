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
package org.more.hypha.beans.support;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.more.FormatException;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.beans.define.URI_ValueMetaData;
import org.more.hypha.configuration.DefineResourceImpl;
/**
 * ���ڽ���uri��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_URI extends TagBeans_AbstractValueMetaDataDefine<URI_ValueMetaData> {
    /**����{@link TagBeans_URI}����*/
    public TagBeans_URI(DefineResourceImpl configuration) {
        super(configuration);
    }
    /**����{@link URI_ValueMetaData}����*/
    protected URI_ValueMetaData createDefine() {
        return new URI_ValueMetaData();
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        uriLocation
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        return null;
    }
    /**��������*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        String uriLocation = event.getAttributeValue("uriLocation");
        if (uriLocation == null)
            return;
        URI_ValueMetaData metaData = this.getDefine(context);
        try {
            metaData.setUriObject(new URI(uriLocation));
        } catch (URISyntaxException e) {
            throw new FormatException("����uri�������ݷ����쳣�������uri��ʽ���ݣ�[" + uriLocation + "]");
        }
    }
}