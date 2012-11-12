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
import java.util.HashMap;
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.hypha.define.File_ValueMetaData;
/**
 * ���ڽ���directory��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Directory extends TagBeans_AbstractValueMetaDataDefine<File_ValueMetaData> {
    /**����{@link TagBeans_Directory}����*/
    public TagBeans_Directory(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link File_ValueMetaData}����*/
    protected File_ValueMetaData createDefine(XmlStackDecorator<Object> context) {
        File_ValueMetaData metaData = new File_ValueMetaData();
        metaData.setDir(true);
        return metaData;
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        fileObject
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.fileObject, "path");
        return propertys;
    }
}