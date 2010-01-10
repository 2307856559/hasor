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
package org.more.beans.resource;
import java.io.File;
import java.net.URI;
import java.net.URL;
/**
 * ��չXmlFileResource���ṩע�����÷�ʽ��֧�֡�
 * @version 2010-1-10
 * @author ������ (zyc@byshell.org)
 */
public class AnnoXmlFileResource extends XmlFileResource {
    /**  */
    private static final long serialVersionUID = -4764919069857076109L;
    /**����AnnoXmlFileResource����validatorXML��ʾ�Ƿ�����֤��*/
    public AnnoXmlFileResource(boolean validatorXML) {
        super(validatorXML);
    }
    /**����AnnoXmlFileResource���󣬲���filePath�������ļ�λ�á�validatorXML��ʾ�Ƿ�����֤��*/
    public AnnoXmlFileResource(String filePath, boolean validatorXML) {
        super(filePath, validatorXML);
    }
    /**����AnnoXmlFileResource���󣬲���file�������ļ�λ�á�validatorXML��ʾ�Ƿ�����֤��*/
    public AnnoXmlFileResource(File file, boolean validatorXML) {
        super(file, validatorXML);
    }
    /**����AnnoXmlFileResource���󣬲���xmlURI�������ļ�λ�á�validatorXML��ʾ�Ƿ�����֤��*/
    public AnnoXmlFileResource(URI xmlURI, boolean validatorXML) {
        super(xmlURI, validatorXML);
    }
    /**����AnnoXmlFileResource���󣬲���xmlURL�������ļ�λ�á�validatorXML��ʾ�Ƿ�����֤��*/
    public AnnoXmlFileResource(URL xmlURL, boolean validatorXML) {
        super(xmlURL, validatorXML);
    }
    @Override
    protected String getTagPropertiesConfig() {
        return "/org/more/beans/resource/annotation/tagProcess.properties";
    }
}