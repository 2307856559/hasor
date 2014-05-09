/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.more.xml;
/**
 * ������{@link XmlParserKitManager}�࣬���ڱ�������ռ��xpath�Ķ�Ӧ��ϵ��
 * @version 2010-9-12
 * @author ������ (zyc@hasor.net)
 */
public class NameSpace {
    private String       uri   = null; //�����ռ�
    private StringBuffer xpath = null; //xpath
    /***/
    public NameSpace(String uri, String xpath) {
        this.uri = uri;
        this.xpath = new StringBuffer(xpath);
    }
    /**��ȡ�����ռ䡣*/
    public String getUri() {
        return uri;
    }
    /**��ȡxpath��*/
    public String getXpath() {
        return this.xpath.toString();
    }
    /**׷��xpathһ���ڵ㡣*/
    void appendXPath(String name, boolean isAttribute) {
        if (this.xpath.indexOf("/") != this.xpath.length() - 1)
            this.xpath.append("/");
        if (isAttribute == true)
            this.xpath.append("@");
        this.xpath.append(name);
    }
    /**ɾ��xpath�����һ���ڵ㡣*/
    void removeXPath() {
        int index = this.xpath.lastIndexOf("/");
        index = (index == 0) ? 1 : index;
        this.xpath = this.xpath.delete(index, this.xpath.length());
    }
}