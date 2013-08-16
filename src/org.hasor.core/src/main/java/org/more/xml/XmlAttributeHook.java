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
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.more.xml.stream.AttributeEvent;
/**
 * ������һ��������Ҫ����ʱʹ�øýӿڣ�ʹ�øýӿڿ������ڽ����ض������ԡ�
 * @version 2010-9-13
 * @author ������ (zyc@hasor.net)
 */
public interface XmlAttributeHook extends XmlParserHook {
    /**
     * ������һ������ʱ��
     * @param context ���������ġ�
     * @param xpath ��ǰ��ǩ��������������ռ��е�xpath��
     * @param event �¼���
     */
    public void attribute(XmlStackDecorator<Object> context, String xpath, AttributeEvent event) throws XMLStreamException, IOException;
}