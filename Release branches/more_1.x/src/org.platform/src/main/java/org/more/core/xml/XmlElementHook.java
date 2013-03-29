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
package org.more.core.xml;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
/**
 * �ù������ڴ���Ԫ�ؿ�ʼ��Ԫ�ؽ����¼���
 * @version 2010-9-13
 * @author ������ (zyc@byshell.org)
 */
public interface XmlElementHook extends XmlParserHook {
    /**
     * ������һ����ʼ��ǩʱ���ڸ÷����п���ͨ��event�����ȡ�������ǩ�����ԡ�
     * @param context ���������ġ�
     * @param xpath ��ǰ��ǩ��������������ռ��е�xpath��
     * @param event �¼���
     */
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) throws XMLStreamException, IOException;
    /**
     * ������һ��������ǩʱ��
     * @param context ���������ġ�
     * @param xpath ��ǰ��ǩ��������������ռ��е�xpath��
     * @param event �¼���
     */
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) throws XMLStreamException, IOException;
}