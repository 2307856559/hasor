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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/**
 *
 * @version 2010-9-7
 * @author ������ (zyc@byshell.org)
 */
public class XmlWriter implements XmlAccept {
    private OutputStream xmlStrema     = null; //��ȡXml���ݵ��������
    private boolean      ignoreComment = true; //�Ƿ����Xml�е�����ע�ͽڵ㡣
    private boolean      ignoreSpace   = true; //�Ƿ����Xml�пɺ��ԵĿո�
    //--------------------------------------------------------------------
    /**����һ��XmlWriter��������д��xml�¼�����fileName������������Xml�ļ���*/
    public XmlWriter(String fileName) throws FileNotFoundException {
        this.xmlStrema = new FileOutputStream(fileName);
    }
    /**����һ��XmlWriter��������д��xml�¼�����file������������Xml�ļ���*/
    public XmlWriter(File file) throws FileNotFoundException {
        this.xmlStrema = new FileOutputStream(file);
    }
    /**����һ��XmlWriter��������д��xml�¼�����xmlStrema���������������С�*/
    public XmlWriter(OutputStream xmlStrema) {
        if (xmlStrema == null)
            throw new NullPointerException("OutputStream���Ͳ���Ϊ�ա�");
        this.xmlStrema = xmlStrema;
    }
    //--------------------------------------------------------------------
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ������д��XML�ڼ䷢�ֵ������ڵ㡣����true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public boolean isIgnoreComment() {
        return this.ignoreComment;
    }
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ������д��XML�ڼ䷢�ֵ������ڵ㡣true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public void setIgnoreComment(boolean ignoreComment) {
        this.ignoreComment = ignoreComment;
    }
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ�����ڶ�ȡXML�ڼ䷢�ֵĿɺ��ԵĿո��ַ������� [XML], 2.10 "White Space Handling"��������true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public boolean isIgnoreSpace() {
        return this.ignoreSpace;
    }
    /**����һ��booleanֵ����ֵ��ʾ���Ƿ���д��XML�ڼ���Կɺ��ԵĿո��ַ������� [XML], 2.10 "White Space Handling"����true��ʾ���ԣ�false��ʾ�����ԡ�*/
    public void setIgnoreSpace(boolean ignoreSpace) {
        this.ignoreSpace = ignoreSpace;
    }
    //--------------------------------------------------------------------
    public void reset() {
        // TODO Auto-generated method stub
    }
    public void sendEvent(XmlStreamEvent e) {
        //1.ִ�к��ԡ�
        if (e instanceof TextEvent == true) {
            TextEvent textE = (TextEvent) e;
            if (textE.isCommentEvent() == true && this.ignoreComment == true)
                return;
            if (textE.isSpaceEvent() == true && this.ignoreSpace == true)
                return;
        }
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(this.xmlStrema);
        // TODO Auto-generated method stub
    }
}