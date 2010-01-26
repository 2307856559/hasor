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
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.more.util.attribute.AttBase;
/**
 * XML�������棬������Ա����ͨ��ʵ��DoEventIteration�ӿ�ʹ��runTask������ִ����չ��xml����
 * @version 2010-1-11
 * @author ������ (zyc@byshell.org)
 */
public class XmlEngine extends AttBase {
    /**  */
    private static final long           serialVersionUID = 2880738501389974190L;
    //========================================================================================Field
    private HashMap<String, TagProcess> tagProcessMap    = new HashMap<String, TagProcess>(); //��ǩ������� ns:tagname
    //========================================================================================Event
    /**ɨ��XML��processXPath��Ҫ�����xpathƥ��������ʽ��*/
    protected Object scanningXML(InputStream in, String processXPath, TaskProcess doEventIteration) throws Exception {
        XMLStreamReader reader = this.getXMLStreamReader(in);
        XmlContextStack stack = null;//��ǰ��ջ
        String tagPrefix = null;//��ǰ��ǩ�����ռ�
        String tagName = null;//��ǰ��ǩ
        int event = reader.getEventType();//��ǰ�¼�����
        while (true) {
            switch (event) {
            case XMLStreamConstants.START_DOCUMENT://�ĵ���ʼ
                stack = new XmlContextStack(null, null, null, "/");
                doEventIteration.onEvent(stack, stack.getXPath(), event, reader, tagProcessMap);
                break;
            case XMLStreamConstants.END_DOCUMENT://�ĵ�����
                doEventIteration.onEvent(stack, stack.getXPath(), event, reader, tagProcessMap);
                break;
            case XMLStreamConstants.START_ELEMENT://Ԫ�ؿ�ʼ
                tagPrefix = reader.getPrefix();
                tagName = reader.getLocalName();
                if (tagPrefix == null || tagPrefix.equals(""))
                    stack = new XmlContextStack(stack, tagPrefix, tagName, stack.getXPath() + tagName + "/");
                else
                    stack = new XmlContextStack(stack, tagPrefix, tagName, stack.getXPath() + tagPrefix + ":" + tagName + "/");
                doEventIteration.onEvent(stack, stack.getXPath(), event, reader, tagProcessMap);
                int attCount = reader.getAttributeCount();
                for (int i = 0; i < attCount; i++) {//��������
                    String attPrefix = reader.getAttributeNamespace(i);
                    String attName = reader.getAttributeLocalName(i);
                    stack.attValue = reader.getAttributeValue(i);
                    String xpath_temp;
                    if (attPrefix == null || attPrefix.equals(""))
                        xpath_temp = stack.getXPath() + "@" + attName;
                    else
                        xpath_temp = stack.getXPath() + "@" + attPrefix + ":" + attName;
                    doEventIteration.onEvent(stack, xpath_temp, XMLStreamConstants.ATTRIBUTE, reader, tagProcessMap);
                    stack.attValue = null;
                }
                break;
            case XMLStreamConstants.END_ELEMENT://Ԫ�ؽ���
                doEventIteration.onEvent(stack, stack.getXPath(), event, reader, tagProcessMap);
                stack = stack.getParent();
                break;
            case XMLStreamConstants.CDATA://������CDATA
                doEventIteration.onEvent(stack, stack.getXPath(), event, reader, tagProcessMap);
                break;
            case XMLStreamConstants.CHARACTERS://�������ַ�
                doEventIteration.onEvent(stack, stack.getXPath(), event, reader, tagProcessMap);
                break;
            }
            if (reader.hasNext() == false)
                break;
            event = reader.next();
        }
        return doEventIteration.getResult();
    };
    /**��ȡStax�Ķ��������Ķ�����������COMMENT�ڵ㡣*/
    private XMLStreamReader getXMLStreamReader(InputStream in) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        reader = factory.createFilteredReader(reader, new StreamFilter() {
            @Override
            public boolean accept(XMLStreamReader reader) {
                int event = reader.getEventType();
                if (event == XMLStreamConstants.COMMENT)
                    return false;
                else
                    return true;
            }
        });
        return reader;
    };
    //==========================================================================================Job
    /**ִ��XML����*/
    public Object runTask(InputStream xmlStream, TaskProcess task, String processXPath, Object[] params) throws Exception {
        task.setConfig(params);
        task.init();
        return this.scanningXML(xmlStream, processXPath, task);
    };
    /**ע��һ����ǩ�������*/
    public void regeditTag(String tagName, TagProcess tag) {
        if (this.tagProcessMap.containsKey(tagName) == false)
            this.tagProcessMap.put(tagName, tag);
    }
    public TagProcess getTagProcess(String tagName) {
        return this.tagProcessMap.get(tagName);
    }
    public void destroy() {
        this.tagProcessMap.clear();
    }
}