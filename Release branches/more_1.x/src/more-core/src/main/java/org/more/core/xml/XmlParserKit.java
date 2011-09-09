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
import org.more.core.error.RepeateException;
import org.more.core.xml.stream.AttributeEvent;
import org.more.core.xml.stream.EndDocumentEvent;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartDocumentEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.core.xml.stream.TextEvent;
import org.more.core.xml.stream.XmlStreamEvent;
import org.more.util.StringUtil;
/**
 *  <b>Level 3</b>���ü����ǻ��ڼ���2����ǿ���ü�����ص��ǿ��Խ�ĳ��xpath����ʾԪ�����䴦����{@link XmlParserHook}���а󶨡�
 *  ����������ǻ���Level 2��˲��������������ռ��ͬ��Ԫ���������
 * @version 2010-9-13
 * @author ������ (zyc@byshell.org)
 */
public class XmlParserKit implements XmlNamespaceParser {
    private HashMap<String, ArrayList<XmlParserHook>> hooks = new HashMap<String, ArrayList<XmlParserHook>>();
    //----------------------------------------------------
    /**ע��һ��{@link XmlParserHook}�ӿڶ���һ��ָ����Xpath�ϣ����ע�����{@link XmlDocumentHook}�ӿڶ�������ؽ�xpath��дΪ"/"������ܵ��½��ղ����¼�������*/
    public void regeditHook(String[] xpath, XmlParserHook hook) {
        if (xpath != null && hook != null)
            for (String s : xpath)
                this.regeditHook(s, hook);
    }
    /**ע��һ��{@link XmlParserHook}�ӿڶ���һ��ָ����Xpath�ϣ����ע�����{@link XmlDocumentHook}�ӿڶ�������ؽ�xpath��дΪ"/"������ܵ��½��ղ����¼�������*/
    public void regeditHook(String xpath, XmlParserHook hook) {
        //2.����Ƿ��Ѿ����ڵ�ע�ᡣ
        ArrayList<XmlParserHook> arrayList = this.hooks.get(xpath);
        if (arrayList == null)
            arrayList = new ArrayList<XmlParserHook>();
        if (arrayList.contains(hook) == true)
            throw new RepeateException(xpath + "��·�����ظ���ͬһ��XmlParserHook��");
        arrayList.add(hook);
        this.hooks.put(xpath, arrayList);
    };
    /**�÷����ǽ��ʹ��regeditHook()����ע���һ�������*/
    public void unRegeditHook(String[] xpath, XmlParserHook hook) {
        for (String s : xpath)
            this.unRegeditHook(s, hook);
    }
    /**�÷����ǽ��ʹ��regeditHook()����ע��Ĺ�����*/
    public void unRegeditHook(String xpath, XmlParserHook hook) {
        ArrayList<XmlParserHook> arrayList = this.hooks.get(xpath);
        if (arrayList == null)
            return;
        if (arrayList.contains(hook) == true)
            arrayList.remove(hook);
    }
    //----------------------------------------------------
    public void beginAccept() {}
    public void endAccept() {}
    private ArrayList<XmlParserHook> getHooks(String xpath) {
        String xpath2 = xpath;
        for (String xp : this.hooks.keySet())
            if (StringUtil.matchWild(xp, xpath2) == true) {
                xpath2 = xp;
                break;
            }
        return this.hooks.get(xpath2);
    };
    /** */
    public void sendEvent(XmlStackDecorator<Object> context, String xpath, XmlStreamEvent event) {
        ArrayList<XmlParserHook> hooks = this.getHooks(xpath);
        if (hooks == null)
            return;
        //-----------
        if (event instanceof StartDocumentEvent) {
            //�ַ��ĵ���ʼ�¼�
            for (XmlParserHook hook : hooks)
                if (hook instanceof XmlDocumentHook)
                    ((XmlDocumentHook) hook).beginDocument(context, (StartDocumentEvent) event);
        } else if (event instanceof EndDocumentEvent) {
            //�ַ��ĵ������¼�
            for (XmlParserHook hook : hooks)
                if (hook instanceof XmlDocumentHook)
                    ((XmlDocumentHook) hook).endDocument(context, (EndDocumentEvent) event);
        } else if (event instanceof StartElementEvent) {
            //�ַ�Ԫ�ؿ�ʼ�¼�
            for (XmlParserHook hook : hooks)
                if (hook instanceof XmlElementHook)
                    ((XmlElementHook) hook).beginElement(context, xpath, (StartElementEvent) event);
        } else if (event instanceof EndElementEvent) {
            //�ַ�Ԫ�ؽ����¼�
            for (XmlParserHook hook : hooks)
                if (hook instanceof XmlElementHook)
                    ((XmlElementHook) hook).endElement(context, xpath, (EndElementEvent) event);
        } else if (event instanceof TextEvent) {
            //�ַ��ı��¼�
            for (XmlParserHook hook : hooks)
                if (hook instanceof XmlTextHook)
                    ((XmlTextHook) hook).text(context, xpath, (TextEvent) event);
        } else if (event instanceof AttributeEvent) {
            //�ַ������¼�
            for (XmlParserHook hook : hooks)
                if (hook instanceof XmlAttributeHook)
                    ((XmlAttributeHook) hook).attribute(context, xpath, (AttributeEvent) event);
        }
    }
}