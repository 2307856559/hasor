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
package org.more.core.xml.stream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.more.core.xml.stream.event.AttributeEvent;
import org.more.core.xml.stream.event.EndDocumentEvent;
import org.more.core.xml.stream.event.EndElementEvent;
import org.more.core.xml.stream.event.StartDocumentEvent;
import org.more.core.xml.stream.event.StartElementEvent;
import org.more.core.xml.stream.event.TextEvent;
import org.more.core.xml.stream.event.TextEvent.Type;
/**
 * xml��ȡ��������Ĺ����ǽ�xml������ת����Ϊxml�¼��������ҿ�����ɨ��xmlʱִ��xml�ĺ��Բ��ԡ�
 * @version 2010-9-8
 * @author ������ (zyc@byshell.org)
 */
public class XmlReader {
    private InputStream xmlStrema     = null; //��ȡXml���ݵ���������
    private boolean     ignoreComment = true; //�Ƿ����Xml�е�����ע�ͽڵ㡣
    private boolean     ignoreSpace   = true; //�Ƿ����Xml�пɺ��ԵĿո�
    //--------------------------------------------------------------------
    /**����һ��XmlReader���������Ķ�fileName������������Xml�ļ���*/
    public XmlReader(String fileName) throws FileNotFoundException {
        this.xmlStrema = new FileInputStream(fileName);
    }
    /**����һ��XmlReader���������Ķ�file������������Xml�ļ���*/
    public XmlReader(File file) throws FileNotFoundException {
        this.xmlStrema = new FileInputStream(file);
    }
    /**����һ��XmlReader���������Ķ�xmlStrema������������Xml�ļ�����*/
    public XmlReader(InputStream xmlStrema) {
        if (xmlStrema == null)
            throw new NullPointerException("InputStream���Ͳ���Ϊ�ա�");
        this.xmlStrema = xmlStrema;
    }
    //--------------------------------------------------------------------
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ�����ڶ�ȡXML�ڼ䷢�ֵ������ڵ㡣����true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public boolean isIgnoreComment() {
        return this.ignoreComment;
    }
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ�����ڶ�ȡXML�ڼ䷢�ֵ������ڵ㡣true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public void setIgnoreComment(boolean ignoreComment) {
        this.ignoreComment = ignoreComment;
    }
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ�����ڶ�ȡXML�ڼ䷢�ֵĿɺ��ԵĿո��ַ������� [XML], 2.10 "White Space Handling"��������true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public boolean isIgnoreSpace() {
        return this.ignoreSpace;
    }
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ��ڶ�ȡXML�ڼ���Կɺ��ԵĿո��ַ������� [XML], 2.10 "White Space Handling"����true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public void setIgnoreSpace(boolean ignoreSpace) {
        this.ignoreSpace = ignoreSpace;
    }
    //--------------------------------------------------------------------
    /** ��ȡStax�Ķ����Ĺ��������������ͨ���÷�������չXmlReader�ڶ�ȡxml�ڼ���Ժ��Ե���Ŀ��*/
    protected StreamFilter getXmlStreamFilter() {
        return null;
    };
    /**
     * �÷��������ھ�������XPath�Ƿ���һ�������Ĺ�ϵ���÷����ķ���ֵ�����˽������Ƿ�������xml��Ŀ�������������д������ɸ���Ŀ��ơ�
     * @param currentXPath ��ǰ������ɨ�赽��XPath��
     * @param testXPath ��ʾ������Ե�XPath��
     * @return ����һ��booleanֵ����ֵ�������Ƿ���Ե�ǰXPath��Ŀ��
     */
    protected boolean ignoreXPath(String currentXPath, String testXPath) {
        if (testXPath == null)
            return false;
        //TODO XPath�Ƚ��㷨���Ƚ�currentXPath�Ƿ�����testXPath��Χ�ڵ�
        if (currentXPath.startsWith(testXPath) == true)
            return true;
        else
            return false;
    }
    /**
     * ִ�н���Xml�ļ��������γ�xml�¼�������Щ�¼��������뵽{@link XmlAccept}���Ͷ����С�
     * ���������ignoreXPath���������γ��¼���ʱXmlReader���ᷢ���������xpath��xml�¼�����
     * @param accept ָ���¼������ն���
     * @param ignoreXPath ָ��Ҫ���Ե�XPath·����
     */
    public void reader(XmlAccept accept, String ignoreXPath) throws XMLStreamException {
        if (accept == null)
            return;
        //1.׼��ɨ������档
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(this.xmlStrema);
        StreamFilter filter = new NullStreamFilter(this, this.getXmlStreamFilter());
        reader = factory.createFilteredReader(reader, filter);
        //2.׼������XPath
        StringBuffer currentXPath = new StringBuffer("/");
        //3.��ѯ�����¼���
        while (true) {
            //(1).�����¼�����
            int xmlEvent = reader.getEventType();//��ǰ�¼�����
            XmlStreamEvent event = null;
            //(2).�����¼�����
            switch (xmlEvent) {
            case XMLStreamConstants.START_DOCUMENT:
                //��ʼ�ĵ�
                event = new StartDocumentEvent(currentXPath.toString(), reader);
                break;
            case XMLStreamConstants.END_DOCUMENT:
                //�����ĵ�
                event = new EndDocumentEvent(currentXPath.toString(), reader);
                break;
            case XMLStreamConstants.START_ELEMENT:
                //��ʼԪ��
                if (currentXPath.indexOf("/") != currentXPath.length() - 1)
                    currentXPath.append("/");
                currentXPath.append(this.getName(reader.getName()));
                event = new StartElementEvent(currentXPath.toString(), reader);
                break;
            case XMLStreamConstants.END_ELEMENT:
                //����Ԫ��
                event = new EndElementEvent(currentXPath.toString(), reader);
                int index = currentXPath.lastIndexOf("/");
                currentXPath = currentXPath.delete(index, currentXPath.length());
                break;
            case XMLStreamConstants.COMMENT:
                //ע��
                event = new TextEvent(currentXPath.toString(), reader, Type.Comment);
                break;
            case XMLStreamConstants.CDATA:
                //CDATA����
                event = new TextEvent(currentXPath.toString(), reader, Type.CDATA);
                break;
            //---------------------------------------------
            case XMLStreamConstants.SPACE:
                //���Ժ��ԵĿո�
                event = new TextEvent(currentXPath.toString(), reader, Type.Space);
                break;
            case XMLStreamConstants.CHARACTERS:
                //�ַ�����
                event = new TextEvent(currentXPath.toString(), reader, Type.Chars);
                break;
            }
            //(3).ִ�к���
            if (xmlEvent == XMLStreamConstants.COMMENT && this.ignoreComment == true) {
                //ִ�к���
                xmlEvent = this.readEvent(reader);
                continue;
            } else if (xmlEvent == XMLStreamConstants.SPACE && this.ignoreSpace == true) {
                //ִ�к���
                xmlEvent = this.readEvent(reader);
                continue;
            }
            //(4).�����¼�
            this.pushEvent(accept, event, ignoreXPath);
            if (xmlEvent == XMLStreamConstants.START_ELEMENT) {
                int attCount = reader.getAttributeCount();
                for (int i = 0; i < attCount; i++) {
                    //���������¼�
                    String namespace = reader.getAttributeNamespace(i);
                    String localName = reader.getAttributeLocalName(i);
                    String prefix = reader.getAttributePrefix(i);
                    //
                    namespace = (namespace == null) ? "" : namespace;
                    localName = (localName == null) ? "" : localName;
                    prefix = (prefix == null) ? "" : prefix;
                    //
                    QName qn = new QName(namespace, localName, prefix);
                    StringBuffer currentXPathTemp = new StringBuffer(currentXPath.toString());
                    currentXPathTemp.append("/@");
                    currentXPathTemp.append(this.getName(qn));
                    event = new AttributeEvent(currentXPathTemp.toString(), reader, i);
                    this.pushEvent(accept, event, ignoreXPath);
                }
            }
            //(5).��ȡ��һ��xml�ĵ����¼���
            xmlEvent = this.readEvent(reader);
            if (xmlEvent == 0)
                break;
        }
        //
    }
    private int readEvent(XMLStreamReader reader) throws XMLStreamException {
        if (reader.hasNext() == false)
            return 0;
        return reader.next();
    }
    private String getName(QName qname) {
        String prefix = qname.getPrefix();
        StringBuffer sb = new StringBuffer();
        if (prefix == null || prefix.equals("") == true) {} else {
            sb.append(prefix);
            sb.append(":");
        }
        return sb.append(qname.getLocalPart()).toString();
    }
    /**ִ��XPath�����жϡ�*/
    private void pushEvent(XmlAccept accept, XmlStreamEvent e, String ignoreXPath) {
        //(3).XPath�����ж�
        boolean ignore = this.ignoreXPath(e.getXpath(), ignoreXPath);
        if (ignore == false)
            this.pushEvent(accept, e);
    }
    /**���������¼��ķ������������ͨ����չ�÷����������¼��ڼ䴦��һЩ����������*/
    protected void pushEvent(XmlAccept accept, XmlStreamEvent e) {
        if (accept != null)
            accept.sendEvent(e);
    }
}
/**
 * �����Ŀ���ǿ��Բ��ܿ�StreamFilter���Ե�Ӱ�졣
 * @version 2010-9-8
 * @author ������ (zyc@byshell.org)
 */
class NullStreamFilter implements StreamFilter {
    private StreamFilter parentFilter;
    public NullStreamFilter(XmlReader reader, StreamFilter parentFilter) {
        this.parentFilter = parentFilter;
    }
    public boolean accept(XMLStreamReader reader) {
        boolean accept = true;
        if (this.parentFilter != null)
            accept = this.parentFilter.accept(reader);
        return accept;
    }
}