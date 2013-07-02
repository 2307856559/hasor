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
package org.more.xml;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.more.xml.stream.XmlStreamEvent;
/**
 * Level 2��������¼������ߡ�����{@link XmlParserKitManager}���߽���ϸ�ֵ�xml�¼�����֮�󶼷��͵��˸ýӿ��С�
 * @version 2010-9-13
 * @author ������ (zyc@byshell.org)
 */
public interface XmlNamespaceParser {
    /**���յ���ʼ�������ź�ʱ���÷�����Ҫ���ڳ�ʼ����������*/
    public void beginAccept();
    /**���յ�ֹͣ�������ź�ʱ���÷�����Ҫ�������������ĺ�����������*/
    public void endAccept();
    /**�÷�����beginAccept��endAccept���������ڼ䷴�����ã�ÿ��Level 1����һ���¼�����֪ͨ��Level 2��Ȼ����Level 2���зַ���*/
    public void sendEvent(XmlStackDecorator<Object> context, String xpath, XmlStreamEvent event) throws IOException, XMLStreamException;
}