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
package org.more.beans.resource.xml;
import org.more.util.attribute.AttBase;
/**
 * ����XML�ڵ�ʱ�Ľڵ��ջ���Ӵ����ĵ���ʼ��������ջ����ÿһ��ڵ㶼����һ���µĶ�ջ��ע�⣺�������Բ������µĶ�ջ����<br/>
 * ͨ����ջ�����򸸽ڵ㴫����Ҫ�����ݺ�һЩ�������ݡ�
 * <br/>Date : 2009-11-21
 * @author ������
 */
public class ContextStack extends AttBase {
    /**  */
    private static final long serialVersionUID = 4300339262589765696L;
    private String            tagName          = null;                //��ǰ��ǩ�����ջ������ı�ǩ����
    private ContextStack      parent           = null;                //��ǰ��ջ�ĸ�����ջ��
    private String            xpath            = null;                //��ǰ��ջ��ǩ������xpathλ�á�
    /**��ǰ��ջ�б������Ҫ���ݶ���*/
    public Object             context          = null;
    /**���ܸ��ӵ�����ֵ*/
    public String             attValue         = null;
    ContextStack(ContextStack parent, String tagName, String xpath) {
        this.parent = parent;
        this.tagName = tagName;
        this.xpath = xpath;
    }
    /**��ȡ��ǰ��ǩ�����ջ������ı�ǩ����*/
    public String getTagName() {
        return tagName;
    }
    /**��ȡ��ǰ��ջ��ǩ������xpathλ�á�*/
    public String getXPath() {
        return this.xpath;
    }
    /**��ȡ��ǰ��ջ�ĸ�����ջ��*/
    public ContextStack getParent() {
        return this.parent;
    }
}