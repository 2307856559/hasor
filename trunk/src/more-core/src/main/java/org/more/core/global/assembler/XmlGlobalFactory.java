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
package org.more.core.global.assembler;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.more.core.error.FormatException;
import org.more.core.global.GlobalFactory;
import org.more.core.xml.XmlAttributeHook;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlParserKit;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.XmlTextHook;
import org.more.core.xml.register.XmlRegister;
import org.more.core.xml.stream.AttributeEvent;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.core.xml.stream.TextEvent;
import org.more.core.xml.stream.XmlReader;
/**
* ����Xml
* @version : 2011-9-3
* @author ������ (zyc@byshell.org)
*/
public class XmlGlobalFactory extends GlobalFactory {
    /**Ĭ��ʹ�õ������ռ䡣*/
    public static final String DefaultNameSpace    = "http://project.byshell.org/more/schema/global";
    private HashSet<String>    loadNameSpace       = new HashSet<String>();
    private boolean            isIgnoreRootElement = false;
    //
    public Map<String, Object> loadConfig(InputStream stream, String encoding) throws IOException {
        try {
            if (loadNameSpace.contains(loadNameSpace) == false)
                loadNameSpace.add(DefaultNameSpace);
            HashMap<String, Object> xmlTree = new HashMap<String, Object>();
            XmlParserKit kit = new XmlParserKit();
            kit.regeditHook("/*", new Config_ElementHook(this.isIgnoreRootElement));
            XmlRegister xmlRegister = new XmlRegister(xmlTree);
            for (String ns : loadNameSpace)
                xmlRegister.regeditKit(ns, kit);
            new XmlReader(stream).reader(xmlRegister, encoding, null);
            return xmlTree;
        } catch (Exception e) {
            if (e instanceof IOException == false)
                throw new FormatException(e);
            else
                throw (IOException) e;
        }
    };
    /**��ȡҪװ�ص������ռ伯�ϡ�*/
    public HashSet<String> getLoadNameSpace() {
        return this.loadNameSpace;
    };
    /**�ж��ڽ���xml��ʱ���Ƿ�������ڵ㡣*/
    public boolean isIgnoreRootElement() {
        return isIgnoreRootElement;
    };
    /**�����ڽ���xml��ʱ���Ƿ�������ڵ㡣*/
    public void setIgnoreRootElement(boolean isIgnoreRootElement) {
        this.isIgnoreRootElement = isIgnoreRootElement;
    };
}
class Config_ElementHook implements XmlElementHook, XmlAttributeHook, XmlTextHook {
    private boolean isIgnoreRootElement = false;
    public Config_ElementHook(boolean isIgnoreRootElement) {
        this.isIgnoreRootElement = isIgnoreRootElement;
    };
    private String getElementPath(XmlStackDecorator<Object> context) {
        StringBuffer sb = new StringBuffer();
        int count = context.getDepth();;
        //�����Ƿ���Ը�
        int beginI = 0;
        if (this.isIgnoreRootElement == true)
            beginI = 1;
        //
        for (; beginI <= count; beginI++) {
            Map<String, Object> $att = context.getParentStack(count - beginI);
            QName qname = (QName) $att.get("QName");
            //
            sb.append(qname.getLocalPart() + ".");
            if ($att.containsKey("id") == true)
                sb.append($att.get("id") + ".");
        }
        return sb.toString();
    };
    /**��Ҫ����������ƴ�ɵ��������ʹ�ø÷������뵽����С�*/
    public void putConfig(String key, String value, XmlStackDecorator<Object> context) {
        String $value = value.trim();
        if ($value == null || $value.equals("") == true)
            return;
        String $key = key.substring(0, key.length() - 1);
        //
        HashMap<String, Object> xmlTree = (HashMap<String, Object>) context.getContext();
        xmlTree.put($key, $value);
    };
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) throws XMLStreamException, IOException {
        String id = event.getAttributeValue("id");
        if (id != null)
            context.put("id", id);
        context.put("@Value", new StringBuffer(""));
        context.put("QName", event.getName());
    };
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) throws XMLStreamException, IOException {
        //1.ƴ��������
        String key = getElementPath(context);
        StringBuffer value = (StringBuffer) context.get("@Value");
        //2.���
        this.putConfig(key, value.toString(), context);
    };
    public void attribute(XmlStackDecorator<Object> context, String xpath, AttributeEvent event) throws XMLStreamException, IOException {
        context.put("QName", event.getName());
        //1.ƴ��������
        String key = getElementPath(context);
        String value = event.getValue();
        if ("id".equals(event.getElementName().toLowerCase()) == true)
            key = key + "id.";
        //2.���
        this.putConfig(key, value.toString(), context);
    };
    public void text(XmlStackDecorator<Object> context, String xpath, TextEvent event) throws XMLStreamException, IOException {
        StringBuffer sb = (StringBuffer) context.get("@Value");
        sb.append(event.getTrimText());
    };
}