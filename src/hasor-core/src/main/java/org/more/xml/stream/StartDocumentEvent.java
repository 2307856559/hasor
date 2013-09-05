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
package org.more.xml.stream;
import javax.xml.stream.XMLStreamReader;
/**
 * ����ʼ�Ķ�xml�ĵ�ʱ��
 * @version 2010-9-8
 * @author ������ (zyc@hasor.net)
 */
public class StartDocumentEvent extends XmlStreamEvent {
    public StartDocumentEvent(String xpath, XMLStreamReader reader) {
        super(xpath, reader);
    }
    /**������������֪���򷵻�������룻���δ֪���򷵻� null��*/
    public String getEncoding() {
        return this.getReader().getEncoding();
    }
    /**��ȡ�� xml ������������ xml �汾�����û�������汾���򷵻� null��*/
    public String getVersion() {
        return this.getReader().getVersion();
    }
    /**���� xml �������������ַ����롣*/
    public String getCharacterEncoding() {
        return this.getReader().getCharacterEncodingScheme();
    }
    /**���¼����ĵ���{@link EndDocumentEvent}���Ͷ���*/
    public boolean isPartner(XmlStreamEvent e) {
        return e instanceof EndDocumentEvent;
    };
    /**�ĵ������¼����ǹ����¼���*/
    public boolean isPublicEvent() {
        return true;
    }
}