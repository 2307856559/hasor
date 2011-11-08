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
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.more.core.error.MoreStateException;
import org.more.core.error.RepeateException;
import org.more.core.xml.stream.AttributeEvent;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.core.xml.stream.XmlAccept;
import org.more.core.xml.stream.XmlStreamEvent;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
/**
 * <b>Level 2</b>���ü����xml���ʲ��Թ�ע��xmlԪ�ػ������������ռ�Ķ�Ӧ�ԣ�ʹ��XmlParserKitManager
 * ����ר�����ڷ���ĳ�������ռ��µ�Ԫ�ء�ÿ�������ռ�Ľ���������һ��{@link XmlParserKit}���Ͷ���
 * ��ʹ��Level 2�������xml��ʱ����Ҫ�������ռ����������Ӧ���������ҽ���Level 1�Ĺ��߽���ɨ��xml��
 * @version 2010-9-8
 * @author ������ (zyc@byshell.org)
 */
public class XmlParserKitManager implements XmlAccept {
    /**ע��������ռ�������߼�*/
    private HashMap<String, ArrayList<XmlNamespaceParser>> regeditXmlParserKit = new HashMap<String, ArrayList<XmlNamespaceParser>>();
    /**���ǰ׺�������ռ�ӳ��*/
    private XmlStackDecorator<Object>                      activateStack       = null;
    /**һ���ڷַ�xml�¼���������һ�´��ڵĻ�����*/
    private XmlStackDecorator<Object>                      context             = new XmlStackDecorator<Object>(new AttBase<Object>());
    /**��ȡ�������󣬵�{@link IAttribute}���Խӿڡ�*/
    public IAttribute<Object> getContext() {
        return context.getSource();
    }
    /**
     * ��ĳ�������ռ䴦������һ���������ϡ��󶨵Ľ������������ڼ�����xml�¼�����Ϣ��
     * �����ͼ�ظ�����ĳ���������������ռ�Ķ�Ӧ��ϵ�������{@link RepeateException}�쳣��
     * @param namespace Ҫ�󶨵������ռ䡣
     * @param kit Ҫ�����Ľ�������
     */
    public void regeditKit(String namespace, XmlNamespaceParser kit) {
        if (namespace == null || kit == null)
            throw new NullPointerException("namespace��kit��������Ϊ�ա�");
        ArrayList<XmlNamespaceParser> list = null;
        if (this.regeditXmlParserKit.containsKey(namespace) == true)
            list = this.regeditXmlParserKit.get(namespace);
        else
            list = new ArrayList<XmlNamespaceParser>();
        if (list.contains(kit) == true)
            throw new RepeateException("namespace ��" + namespace + "�� and kit ��" + kit + "�� is repeat");
        list.add(kit);
        this.regeditXmlParserKit.put(namespace, list);
    }
    /**
     * ���ע��ʹ��regeditKit����ע���namespace��{@link XmlParserKit}������ϵ��
     * @param namespace Ҫ����󶨵������ռ䡣
     * @param kit Ҫ��������Ľ�������
     */
    public void unRegeditKit(String namespace, XmlNamespaceParser kit) {
        if (namespace == null || kit == null)
            throw new NullPointerException("namespace or kit is null.");
        if (this.regeditXmlParserKit.containsKey(namespace) == false)
            return;
        ArrayList<XmlNamespaceParser> list = this.regeditXmlParserKit.get(namespace);
        if (list.contains(kit) == true)
            list.remove(kit);
    }
    /**��ʼ{@link XmlAccept}�ӿڵĵ��ã��÷�����Ҫ��������״̬��*/
    public void beginAccept() {
        this.activateStack = new XmlStackDecorator<Object>(new AttBase<Object>());
        for (ArrayList<XmlNamespaceParser> alList : this.regeditXmlParserKit.values())
            for (XmlNamespaceParser xnp : alList)
                xnp.beginAccept();
    }
    /**����{@link XmlAccept}�ӿڵĵ��á�*/
    public void endAccept() {
        this.activateStack = null;
        for (ArrayList<XmlNamespaceParser> alList : this.regeditXmlParserKit.values())
            for (XmlNamespaceParser xnp : alList)
                xnp.endAccept();
    }
    /*--------------------------------------------------------------*/
    /**�ַ��¼���{@link XmlParserKit}�б�*/
    private void issueEvent(XmlStreamEvent e, XmlStackDecorator<Object> activateStack) throws XMLStreamException, IOException {
        //�����¼�
        if (e.isPublicEvent() == true)
            for (String namespace : this.regeditXmlParserKit.keySet()) {
                QName currentElement = e.getCurrentElement();
                String xpath = null;
                if (currentElement == null)
                    xpath = "/";//�ض��� ��ʼ�ĵ����߽����ĵ��¼���
                else {
                    String prefix = currentElement.getPrefix();
                    NameSpace ns = (NameSpace) this.activateStack.getAttribute(prefix);
                    if (ns == null)
                        throw new MoreStateException("��������ǰ׺[" + prefix + "]����������ռ�û�б����");
                    xpath = ns.getXpath();
                }
                ArrayList<XmlNamespaceParser> alList = this.regeditXmlParserKit.get(namespace);
                if (alList != null)
                    for (XmlNamespaceParser kit : alList)
                        kit.sendEvent(this.context, xpath, e);
            }
        else {
            //����˽���¼�
            String prefix = e.getCurrentElement().getPrefix();
            prefix = (prefix == null) ? "" : prefix;
            NameSpace ns = activateStack.getNameSpace(prefix);
            ArrayList<XmlNamespaceParser> alList = this.regeditXmlParserKit.get(ns.getUri());
            if (alList != null)
                for (XmlNamespaceParser kit : alList)
                    kit.sendEvent(this.context, ns.getXpath(), e);
        }
    }
    /**ʵ��{@link XmlAccept}�ӿ����ڽ����¼��ķ�����*/
    public synchronized void sendEvent(XmlStreamEvent e) throws XMLStreamException, IOException {
        //1.����StartElementEvent
        if (e instanceof StartElementEvent) {
            this.activateStack.createStack();
            StartElementEvent ee = (StartElementEvent) e;
            //
            int nsCount = ee.getNamespaceCount();
            for (int i = 0; i < nsCount; i++) {
                String prefix = ee.getNamespacePrefix(i);
                String uri = ee.getNamespaceURI(i);
                prefix = (prefix == null) ? "" : prefix;
                NameSpace ns = this.activateStack.getNameSpace(prefix);
                if (ns == null)
                    //�����µ������ռ�
                    this.activateStack.addNameSpace(prefix, new NameSpace(uri, "/"));
            }
            //���ɵ�ǰ�ڵ��xpath��ר�������ռ��µ�xpath��
            String prefix = ee.getPrefix();
            prefix = (prefix == null) ? "" : prefix;
            NameSpace ns = this.activateStack.getNameSpace(prefix);
            ns.appendXPath(ee.getElementName(), false);
            this.issueEvent(e, this.activateStack);
            //���ִ�е���������ɾ��������xpath����Ϊreader�����ٷ�����end�¼���
            if (e.isSkip() == true)
                ns.removeXPath();
            return;
        } else
        //2.����EndElementEvent
        if (e instanceof EndElementEvent) {
            this.activateStack.dropStack();
            EndElementEvent ee = (EndElementEvent) e;
            //
            NameSpace ns = this.activateStack.getNameSpace(ee.getPrefix());
            this.issueEvent(e, this.activateStack);
            ns.removeXPath();
            return;
        } else
        //3.����AttributeEvent
        if (e instanceof AttributeEvent) {
            AttributeEvent ee = (AttributeEvent) e;
            String prefix = ee.getName().getPrefix();
            prefix = (prefix == null) ? "" : prefix;
            if (prefix.equals("") == true)
                prefix = ee.getCurrentElement().getPrefix();
            prefix = (prefix == null) ? "" : prefix;
            //
            NameSpace ns = this.activateStack.getNameSpace(prefix);
            ns.appendXPath(ee.getElementName(), true);
            this.issueEvent(e, this.activateStack);
            ns.removeXPath();
            return;
        } else
            //4.�ַ��¼�
            this.issueEvent(e, this.activateStack);
    }
}