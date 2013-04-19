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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.more.core.error.MoreStateException;
import org.more.core.error.RepeateException;
import org.more.xml.stream.AttributeEvent;
import org.more.xml.stream.EndElementEvent;
import org.more.xml.stream.StartElementEvent;
import org.more.xml.stream.XmlAccept;
import org.more.xml.stream.XmlStreamEvent;
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
    //
    //
    private XmlStackDecorator<Object> getXmlStack() {
        if (this.activateStack == null)
            this.activateStack = new XmlStackDecorator<Object>();
        return this.activateStack;
    }
    //    /**��ȡ�������󣬵�{@link IAttribute}���Խӿڡ�*/
    //    public IAttribute<Object> getAttContext() {
    //        return getXmlStack().get.getSource();
    //    }
    /**���ð󶨵������Ķ���*/
    public void setContext(Object context) {
        getXmlStack().setContext(context);
    }
    /**��ȡ�󶨵������Ķ���*/
    public Object getContext() {
        return getXmlStack().getContext();
    }
    /**���һ�������ռ䴦�����Ƿ��Ѿ��󶨵�ĳ�������ռ��ϡ�*/
    public boolean isRegeditKit(String namespace, XmlNamespaceParser kit) {
        if (this.regeditXmlParserKit.containsKey(namespace) == false)
            return false;
        ArrayList<XmlNamespaceParser> parserList = this.regeditXmlParserKit.get(namespace);
        return parserList.contains(kit);
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
    /**��ȡע��������ռ伯��*/
    public Set<String> getNamespace() {
        return Collections.unmodifiableSet(this.regeditXmlParserKit.keySet());
    }
    /**��ȡָ�������ռ�����ע��Ľ��������ϡ�*/
    public List<XmlNamespaceParser> getXmlNamespaceParser(String namespace) {
        List<XmlNamespaceParser> parserList = this.regeditXmlParserKit.get(namespace);
        if (parserList == null)
            return Collections.unmodifiableList(new ArrayList<XmlNamespaceParser>());
        else
            return Collections.unmodifiableList(parserList);
    }
    /**��ʼ{@link XmlAccept}�ӿڵĵ��ã��÷�����Ҫ��������״̬��*/
    public void beginAccept() {
        this.getXmlStack();
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
                    NameSpace ns = (NameSpace) this.activateStack.get(prefix);
                    if (ns == null)
                        throw new MoreStateException("��������ǰ׺[" + prefix + "]����������ռ�û�б����");
                    xpath = ns.getXpath();
                }
                ArrayList<XmlNamespaceParser> alList = this.regeditXmlParserKit.get(namespace);
                if (alList != null)
                    for (XmlNamespaceParser kit : alList)
                        kit.sendEvent(this.getXmlStack(), xpath, e);
            }
        else {
            //����˽���¼�
            String prefix = e.getCurrentElement().getPrefix();
            prefix = (prefix == null) ? "" : prefix;
            NameSpace ns = activateStack.getNameSpace(prefix);
            ArrayList<XmlNamespaceParser> alList = this.regeditXmlParserKit.get(ns.getUri());
            if (alList != null)
                for (XmlNamespaceParser kit : alList)
                    kit.sendEvent(this.getXmlStack(), ns.getXpath(), e);
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
            EndElementEvent ee = (EndElementEvent) e;
            NameSpace ns = this.activateStack.getNameSpace(ee.getPrefix());
            this.issueEvent(e, this.activateStack);
            ns.removeXPath();
            this.activateStack.dropStack();
            return;
        } else
        //3.����AttributeEvent
        if (e instanceof AttributeEvent) {
            this.activateStack.createStack();
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
            this.activateStack.dropStack();
            return;
        } else
            //4.�ַ��¼�
            this.issueEvent(e, this.activateStack);
    }
}