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
package org.more.hypha.configuration;
import java.util.ArrayList;
import org.more.InitializationException;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.core.xml.stream.TextEvent;
import org.more.core.xml.stream.XmlAccept;
import org.more.core.xml.stream.XmlStreamEvent;
import org.more.util.StringConvert;
/**
 * ������Ϊ�˽���regedit.xml�������ġ�
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
class NameSpaceConfiguration implements XmlAccept {
    private XmlConfiguration  config     = null;
    private ArrayList<String> schemaList = new ArrayList<String>();
    public NameSpaceConfiguration(XmlConfiguration config) {
        this.config = config;
    }
    public void beginAccept() {}
    public void endAccept() {}
    //
    private StringBuffer currentNamespace    = null;
    private StringBuffer currentFactory      = null;
    private StringBuffer currentSchema       = null;
    private String       currentSchemaEnable = null;
    //
    public void sendEvent(XmlStreamEvent e) {
        if (e.getXpath().equals("/Configuration/regedit") == true)
            this.processRegedit(e);
        else if (e.getXpath().startsWith("/Configuration/regedit/namespace") == true)
            this.p_namespace(e);
        else if (e.getXpath().startsWith("/Configuration/regedit/factory") == true)
            this.p_factory(e);
        else if (e.getXpath().startsWith("/Configuration/regedit/schema") == true)
            this.p_schema(e);
    }
    /**����regedit��ǩ*/
    private void processRegedit(XmlStreamEvent e) {
        if (e instanceof StartElementEvent) {
            this.currentNamespace = new StringBuffer();
            this.currentFactory = new StringBuffer();
            this.currentSchema = new StringBuffer();
            this.currentSchemaEnable = null;
        } else if (e instanceof EndElementEvent)
            try {
                Class<?> factory = Class.forName(this.currentFactory.toString());
                NameSpaceRegister obj = (NameSpaceRegister) factory.newInstance();
                obj.initRegister(this.currentNamespace.toString(), this.config);
                if (StringConvert.parseBoolean(this.currentSchemaEnable) == true)
                    this.schemaList.add(this.schemaList.toString());
            } catch (Exception err) {
                throw new InitializationException("ִ��regedit.xm�����ļ�����.[" + this.currentFactory.toString() + "]���ܱ���ȷִ��", err);
            }
    }
    /**����namespace��ǩ*/
    private void p_namespace(XmlStreamEvent e) {
        if (e instanceof TextEvent == false)
            return;
        TextEvent ee = (TextEvent) e;
        this.currentNamespace.append(ee.getTrimText());
    }
    /**����factory��ǩ*/
    private void p_factory(XmlStreamEvent e) {
        if (e instanceof TextEvent == false)
            return;
        TextEvent ee = (TextEvent) e;
        this.currentFactory.append(ee.getTrimText());
    }
    /**����schema��ǩ*/
    private void p_schema(XmlStreamEvent e) {
        if (e instanceof StartElementEvent == true) {
            StartElementEvent ee = (StartElementEvent) e;
            currentSchemaEnable = ee.getAttributeValue("enable");
        }
        if (e instanceof TextEvent == false)
            return;
        TextEvent ee = (TextEvent) e;
        this.currentSchema.append(ee.getTrimText());
    }
}