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
package net.hasor.context.setting;
import java.io.IOException;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import net.hasor.context.Settings;
import org.more.xml.XmlStackDecorator;
import org.more.xml.stream.XmlStreamEvent;
/**
 * HasorXml�������ӿ�
 * @version : 2013-7-13
 * @author ������ (zyc@hasor.net)
 */
public interface HasorXmlParser {
    /**ÿ����ʼ����һ��Xml�ļ�ʱ���á�*/
    public void beginAccept(Settings context, Map<String, Object> dataContainer);
    /**��xml�ļ��������ʱ���á�*/
    public void endAccept(Settings context, Map<String, Object> dataContainer);
    /**�ڽ���xml�����е��ã����з������¼�����������һ�������ռ��µġ�*/
    public void sendEvent(XmlStackDecorator<Object> context, String xpath, XmlStreamEvent event) throws IOException, XMLStreamException;
}