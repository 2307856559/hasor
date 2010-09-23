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
package org.more.beans.define;
import org.more.beans.ValueMetaData;
/**
* ValueMetaData��һ����ʱʵ��
* @version 2010-9-21
* @author ������ (zyc@byshell.org)
*/
public class QuickProperty_ValueMetaData extends ValueMetaData {
    private String value       = null; //value
    private String enumeration = null; //enum
    private String date        = null; //date
    private String format      = null; //format
    private String refBean     = null; //refBean
    private String refScope    = null; //refScope
    private String file        = null; //file
    private String directory   = null; //directory
    private String uriLocation = null; //uriLocation
    /**����null*/
    public PropertyMetaTypeEnum getPropertyType() {
        return null;
    }
    /**��ȡ xml attribute value*/
    public String getValue() {
        return value;
    }
    /**���� xml attribute value*/
    public void setValue(String value) {
        this.value = value;
    }
    /**��ȡ xml attribute enum*/
    public String getEnumeration() {
        return enumeration;
    }
    /**���� xml attribute enum*/
    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }
    /**��ȡ xml attribute date*/
    public String getDate() {
        return date;
    }
    /**���� xml attribute date*/
    public void setDate(String date) {
        this.date = date;
    }
    /**��ȡ xml attribute format*/
    public String getFormat() {
        return format;
    }
    /**���� xml attribute format*/
    public void setFormat(String format) {
        this.format = format;
    }
    /**��ȡ xml attribute refBean*/
    public String getRefBean() {
        return refBean;
    }
    /**���� xml attribute refBean*/
    public void setRefBean(String refBean) {
        this.refBean = refBean;
    }
    /**��ȡ xml attribute refScope*/
    public String getRefScope() {
        return refScope;
    }
    /**���� xml attribute refScope*/
    public void setRefScope(String refScope) {
        this.refScope = refScope;
    }
    /**��ȡ xml attribute file*/
    public String getFile() {
        return file;
    }
    /**���� xml attribute file*/
    public void setFile(String file) {
        this.file = file;
    }
    /**��ȡ xml attribute directory*/
    public String getDirectory() {
        return directory;
    }
    /**���� xml attribute directory*/
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    /**��ȡ xml attribute uriLocation*/
    public String getUriLocation() {
        return uriLocation;
    }
    /**���� xml attribute uriLocation*/
    public void setUriLocation(String uriLocation) {
        this.uriLocation = uriLocation;
    }
}