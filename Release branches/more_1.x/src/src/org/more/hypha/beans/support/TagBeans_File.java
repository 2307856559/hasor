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
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.beans.define.File_ValueMetaData;
import org.more.hypha.context.XmlDefineResource;
/**
 * ���ڽ���file��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_File extends TagBeans_AbstractValueMetaDataDefine<File_ValueMetaData> {
    /**����{@link TagBeans_File}����*/
    public TagBeans_File(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link File_ValueMetaData}����*/
    protected File_ValueMetaData createDefine() {
        File_ValueMetaData metaData = new File_ValueMetaData();
        metaData.setDir(false);
        return metaData;
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        path
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        return null;
    }
    /**��������*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        String path = event.getAttributeValue("path");
        if (path == null)
            return;
        File_ValueMetaData metaData = this.getDefine(context);
        metaData.setFileObject(path);
    }
}