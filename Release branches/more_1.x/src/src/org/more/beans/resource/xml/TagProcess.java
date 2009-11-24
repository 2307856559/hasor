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
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
/**
 * �ڽ���XMLʱ���ڱ�ʾ������Ҫ�����һ����ǩ�¼������������Ա��Ҫ�������ļ����Զ���һ����ǩ������Զ���ı�ǩ��������Ҫ�̳и��ࡣ
 * <br/>Date : 2009-11-21
 * @author ������
 */
public class TagProcess implements XMLStreamConstants {
    /**�����ֱ�ǩ��ʼ��*/
    public void doStartEvent(String xPath, XMLStreamReader xmlReader, ContextStack context) {};
    /**�����ֱ�ǩ������*/
    public void doEndEvent(String xPath, XMLStreamReader xmlReader, ContextStack context) {}
    /**��������ǩ�ı����ݣ�ע��CDATA��CDATA�����ı����������ı����ݡ�*/
    public void doCharEvent(String xPath, XMLStreamReader reader, ContextStack context) {};
}