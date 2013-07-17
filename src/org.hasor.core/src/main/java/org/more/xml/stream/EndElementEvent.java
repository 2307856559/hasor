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
package org.more.xml.stream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
/**
 * ��������ǩ����ʱ��
 * @version 2010-9-8
 * @author ������ (zyc@byshell.org)
 */
public class EndElementEvent extends XmlStreamEvent {
    public EndElementEvent(String xpath, XMLStreamReader reader) {
        super(xpath, reader);
    }
    /**��ȡԪ������{@link QName}����*/
    public QName getName() {
        return this.getReader().getName();
    }
    /**��ȡԪ����(�����������ռ�ǰ׺)��*/
    public String getElementName() {
        return this.getName().getLocalPart();
    }
    /**��ȡԪ�������ռ�ǰ׺��*/
    public String getPrefix() {
        return this.getName().getPrefix();
    }
    /**��ȡԪ�������ռ䡣*/
    public String getNamespaceURI() {
        return this.getName().getNamespaceURI();
    }
    /**��ȡ�����Ԫ���϶���������ռ�������*/
    public int getNamespaceCount() {
        return this.getReader().getNamespaceCount();
    }
    /**��ȡ�����Ԫ���϶����ָ�������������ռ�ǰ׺��*/
    public String getNamespacePrefix(int index) {
        return this.getReader().getNamespacePrefix(index);
    }
    /**��ȡ�����Ԫ���϶����ָ�������������ռ�URI��*/
    public String getNamespaceURI(int index) {
        return this.getReader().getNamespaceURI(index);
    }
    /**ʹ��ָ���������ռ�ǰ׺��ȡ�����ռ�URI��*/
    public String getNamespaceURI(String prefix) {
        return this.getReader().getNamespaceURI(prefix);
    }
    /**���¼����ĵ���{@link StartElementEvent}���Ͷ��󣬱����ԵĶ����{@link QName}�����뵱ǰ�����{@link QName}��ͬ������Ҫ��ͬһ��xpath�¡�*/
    public boolean isPartner(XmlStreamEvent e) {
        if (e instanceof StartElementEvent == false)
            return false;
        //
        StartElementEvent ende = (StartElementEvent) e;
        QName qnameA = this.getName();
        QName qnameB = ende.getName();
        if (qnameA.equals(qnameB) == false)
            return false;
        if (this.getXpath().equals(ende.getXpath()) == false)
            return false;
        return true;
    };
    /**Ԫ�ؽ����¼������ǹ����¼���*/
    public boolean isPublicEvent() {
        return false;
    }
}