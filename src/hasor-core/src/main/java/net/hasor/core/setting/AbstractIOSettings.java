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
package net.hasor.core.setting;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import net.hasor.Hasor;
import net.hasor.core.Settings;
import org.more.UnhandledException;
import org.more.util.ResourcesUtils;
import org.more.util.map.Properties;
import org.more.xml.XmlNamespaceParser;
import org.more.xml.XmlParserKitManager;
import org.more.xml.XmlStackDecorator;
import org.more.xml.stream.XmlStreamEvent;
/***
 * ֧�ִ�һ���������ж�ȡ������Ϣ�ĳ����ࡣ
 * @version : 2013-9-8
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractIOSettings extends AbstractSettings implements IOSettings {
    public AbstractIOSettings() throws IOException {
        this.loadNsProp();
    }
    //
    /*װ�ؽ���������*/
    private Map<String, List<String>> nsDefine = null;
    private synchronized Map<String, List<String>> loadNsProp() throws IOException {
        if (this.nsDefine != null)
            return this.nsDefine;
        //
        HashMap<String, List<String>> defineStr = new HashMap<String, List<String>>();
        List<URL> nspropURLs = ResourcesUtils.getResources("META-INF/ns.prop");
        if (nspropURLs != null) {
            for (URL nspropURL : nspropURLs) {
                Hasor.info("find ��ns.prop�� at ��%s��.", nspropURL);
                InputStream inStream = ResourcesUtils.getResourceAsStream(nspropURL);
                if (inStream != null) {
                    /*����ns.prop*/
                    Properties prop = new Properties();
                    prop.load(inStream);
                    for (String key : prop.keySet()) {
                        String v = prop.get(key);
                        String k = key.trim();
                        List<String> nsPasser = null;
                        if (defineStr.containsKey(k) == false) {
                            nsPasser = new ArrayList<String>();
                            defineStr.put(k, nsPasser);
                        } else
                            nsPasser = defineStr.get(k);
                        nsPasser.add(v);
                    }
                    /**/
                }
            }
        }
        //
        //        HashMap<URL, List<String>> define = new HashMap<URL, List<String>>();
        //        for (Entry<String, List<String>> ent : defineStr.entrySet())
        //            define.put(new URL(ent.getKey()), ent.getValue());
        Hasor.info("load space ��%s��.", defineStr);
        this.nsDefine = defineStr;
        return this.nsDefine;
    }
    /*����������*/
    protected synchronized XmlParserKitManager getXmlParserKitManager(Map<String, Map<String, Object>> loadTo) throws IOException {
        XmlParserKitManager kitManager = new XmlParserKitManager();
        Map<String, List<String>> nsParser = loadNsProp();
        for (String xmlNS : nsParser.keySet()) {
            //��ȡͬһ�������ռ���ע��Ľ�����
            List<String> xmlParserSet = nsParser.get(xmlNS);
            //�������ڴ�������ռ������ݵ�����
            Map<String, Object> dataContainer = new HashMap<String, Object>();
            //�����������������ҽ�ע��Ľ��������õ�������
            InternalHasorXmlParserPropxy nsKit = new InternalHasorXmlParserPropxy(this, dataContainer);
            //���뵽���ؽ��������
            if (loadTo.containsKey(xmlNS) == false)
                loadTo.put(xmlNS, dataContainer);
            //
            for (String xmlParser : xmlParserSet) {
                try {
                    Class<?> xmlParserType = Class.forName(xmlParser);
                    nsKit.addTarget((SettingsXmlParser) xmlParserType.newInstance());
                    Hasor.info("add XmlNamespaceParser ��%s�� on ��%s��.", xmlParser, xmlNS);
                } catch (Exception e) {
                    throw new UnhandledException(e);
                }
            }
            /*ʹ��XmlParserKitManagerʵ�ֲ�ͬ���������ղ��������ռ��¼���֧��*/
            String xmlNSStr = null;
            try {
                xmlNSStr = URLDecoder.decode(xmlNS.toString(), "utf-8");
            } catch (Exception e) {
                xmlNSStr = xmlNS.toString();
            }
            kitManager.regeditKit(xmlNSStr, nsKit);
        }
        Hasor.info("XmlParserKitManager created!");
        return kitManager;
    }
    //
    //
    /**��ȡָ��ĳ���ض������ռ��µ�Settings�ӿڶ���*/
    public String[] getNamespaceArray() {
        return this.nsDefine.keySet().toArray(new String[this.nsDefine.size()]);
    }
    //
    /**��ȡָ��ĳ���ض������ռ��µ�Settings�ӿڶ���*/
    public final AbstractSettings getNamespace(String namespace) {
        final AbstractSettings setting = this;
        final Map<String, Object> data = this.getNamespaceSettingMap().get(namespace);
        if (data == null)
            return null;
        return new AbstractSettings() {
            public void refresh() throws IOException {
                throw new UnsupportedOperationException();
            }
            public AbstractSettings getNamespace(String namespace) {
                return setting.getNamespace(namespace);
            }
            public String[] getNamespaceArray() {
                return setting.getNamespaceArray();
            }
            public Map<String, Object> getSettingsMap() {
                return data;
            }
        };
    }
    /**�ֱ𱣴�ÿ�������ռ��µ�������ϢMap*/
    protected abstract Map<String, Map<String, Object>> getNamespaceSettingMap();
    //    /**loadConfigװ������*/
    //    private void loadConfig(URI configURI, Map<String, Map<String, Object>> loadTo) throws IOException, ParserConfigurationException, SAXException {
    //        InputStream xmlStream = ResourcesUtils.getResourceAsStream(configURI);
    //        SAXParserFactory factory = SAXParserFactory.newInstance();
    //        factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
    //        factory.setFeature("http://xml.org/sax/features/namespaces", true);
    //        SAXParser parser = factory.newSAXParser();
    //        SaxXmlParser handler = new SaxXmlParser(loadTo);
    //        parser.parse(xmlStream, handler);
    //        xmlStream.close();
    //    }
}
/***/
class InternalHasorXmlParserPropxy implements XmlNamespaceParser {
    private List<SettingsXmlParser> parserList    = new ArrayList<SettingsXmlParser>();
    private Map<String, Object>     dataContainer = null;
    private Settings                context       = null;
    //
    public InternalHasorXmlParserPropxy(Settings context, Map<String, Object> dataContainer) {
        this.context = context;
        this.dataContainer = dataContainer;
    }
    void addTarget(SettingsXmlParser newInstance) {
        if (newInstance != null)
            this.parserList.add(newInstance);
    }
    public void beginAccept() {
        for (SettingsXmlParser par : parserList)
            par.beginAccept(this.context, this.dataContainer);
    }
    public void endAccept() {
        for (SettingsXmlParser par : parserList)
            par.endAccept(this.context, this.dataContainer);
    }
    public void sendEvent(XmlStackDecorator<Object> context, String xpath, XmlStreamEvent event) throws IOException, XMLStreamException {
        for (SettingsXmlParser par : this.parserList)
            par.sendEvent(context, xpath, event);
    }
}