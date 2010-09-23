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
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.more.RepeateException;
import org.more.StateException;
import org.more.core.xml.stream.AttributeEvent;
import org.more.core.xml.stream.EndDocumentEvent;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartDocumentEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.core.xml.stream.TextEvent;
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
    private XmlStackDecorator                              activateStack       = null;
    /**һ���ڷַ�xml�¼���������һ�´��ڵĻ�����*/
    private XmlStackDecorator                              context             = new XmlStackDecorator(new AttBase());
    /**��ȡ�������󣬵�{@link IAttribute}���Խӿڡ�*/
    public IAttribute getContext() {
        return context.getSource();
    }
    /**
     * ��ĳ�������ռ䴦������һ���������ϡ��󶨵Ľ������������ڼ�����xml�¼�����Ϣ��
     * �����ͼ�ظ�����ĳ���������������ռ�Ķ�Ӧ��ϵ�������{@link RepeateException}�쳣��
     * @param namespace Ҫ�󶨵������ռ䡣
     * @param kit Ҫ�����Ľ�������
     */
    public void regeditKit(String namespace, XmlNamespaceParser kit) throws RepeateException {
        if (namespace == null || kit == null)
            throw new NullPointerException("namespace��kit��������Ϊ�ա�");
        ArrayList<XmlNamespaceParser> list = null;
        if (this.regeditXmlParserKit.containsKey(namespace) == true)
            list = this.regeditXmlParserKit.get(namespace);
        else
            list = new ArrayList<XmlNamespaceParser>();
        if (list.contains(kit) == true)
            throw new RepeateException("�����ռ�[" + namespace + "]�������" + kit + "�ظ�ע�ᡣ");
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
            throw new NullPointerException("namespace��kit��������Ϊ�ա�");
        if (this.regeditXmlParserKit.containsKey(namespace) == false)
            return;
        ArrayList<XmlNamespaceParser> list = this.regeditXmlParserKit.get(namespace);
        if (list.contains(kit) == true)
            list.remove(kit);
    }
    /**��ʼ{@link XmlAccept}�ӿڵĵ��ã��÷�����Ҫ��������״̬��*/
    public void beginAccept() {
        this.activateStack = new XmlStackDecorator(new AttBase());
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
    /**ʵ��{@link XmlAccept}�ӿ����ڽ����¼��ķ�����*/
    public void sendEvent(XmlStreamEvent e) {
        //1.������ջ�����������ռ䴦������
        if (e instanceof StartElementEvent) {
            StartElementEvent ee = (StartElementEvent) e;
            int nsCount = ee.getNamespaceCount();
            this.activateStack.createStack();//����һ����ջ
            for (int i = 0; i < nsCount; i++) {
                String prefix = ee.getNamespacePrefix(i);
                String uri = ee.getNamespaceURI(i);
                prefix = (prefix == null) ? "" : prefix;
                this.activateStack.setAttribute(prefix, new NameSpace(uri, "/"));
            }
        }
        //2.�ϳ�NameSpaceר�е�XPath
        if (e instanceof StartElementEvent) {
            StartElementEvent ee = (StartElementEvent) e;
            //(2).�ϳ�NameSpaceר�е�XPath
            String prefix = ee.getPrefix();
            NameSpace ns = (NameSpace) this.activateStack.getAttribute(prefix);
            ns.appendXPath(ee.getElementName(), false);
        } else if (e instanceof AttributeEvent) {
            AttributeEvent ee = (AttributeEvent) e;
            //(2).�ϳ�NameSpaceר�е�XPath
            String prefix = ee.getPrefix();
            NameSpace ns = (NameSpace) this.activateStack.getAttribute(prefix);
            ns.appendXPath(ee.getElementName(), true);
        }
        //3.ȷ���¼������ķ�Χ.
        boolean isPublic = false;
        if (e instanceof StartDocumentEvent)
            isPublic = true;
        else if (e instanceof EndDocumentEvent)
            isPublic = true;
        else if (e instanceof TextEvent) {
            TextEvent ee = (TextEvent) e;
            if (ee.isCommentEvent() == true)
                isPublic = true;
        }
        //4.�ַ��¼�--˽��
        if (isPublic == false) {
            QName qname = e.getCurrentElement();
            String prefix = qname.getPrefix();
            prefix = (prefix == null) ? "" : prefix;
            NameSpace atNS = (NameSpace) this.activateStack.getAttribute(prefix);
            if (atNS != null) {
                ArrayList<XmlNamespaceParser> kitList = this.regeditXmlParserKit.get(atNS.getUri());
                if (kitList != null)
                    this.issueEvent(e, atNS.getXpath(), kitList);
            }
        }
        //4.�ַ��¼�--����
        if (isPublic == true) {
            for (String namespace : this.regeditXmlParserKit.keySet()) {
                QName currentElement = e.getCurrentElement();
                String xpath = null;
                if (currentElement == null)
                    xpath = "/";//�ض��� ��ʼ�ĵ����߽����ĵ��¼���
                else {
                    String prefix = currentElement.getPrefix();
                    NameSpace atNS = (NameSpace) this.activateStack.getAttribute(prefix);
                    if (atNS == null)
                        throw new StateException("��������ǰ׺[" + prefix + "]����������ռ�û�б����");
                    xpath = atNS.getXpath();
                }
                ArrayList<XmlNamespaceParser> kitList = this.regeditXmlParserKit.get(namespace);
                this.issueEvent(e, xpath, kitList);
            }
        }
        //5.�ϳ�NameSpaceר�е�XPath
        String prefix = null;
        if (e instanceof EndElementEvent) {
            EndElementEvent ee = (EndElementEvent) e;
            prefix = ee.getPrefix();
        } else if (e instanceof AttributeEvent) {
            AttributeEvent ee = (AttributeEvent) e;
            prefix = ee.getPrefix();
        }
        if (prefix != null) {
            NameSpace ns = (NameSpace) this.activateStack.getAttribute(prefix);
            ns.removeXPath();
        }
        //6.���ٶ�ջ���ۻ������ռ䴦������
        if (e instanceof EndElementEvent)
            this.activateStack.dropStack();//���ٶ�ջ
    }
    /**�÷��������ڷַ��¼���{@link XmlParserKit}�������ϡ�*/
    private void issueEvent(XmlStreamEvent rootEvent, String xpath, ArrayList<XmlNamespaceParser> parserKitList) {
        for (XmlNamespaceParser kit : parserKitList)
            kit.sendEvent(this.context, xpath, rootEvent);
    }
}