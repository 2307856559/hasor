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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.hasor.core.Hasor;
import net.hasor.core.setting.xml.SaxXmlParser;
/***
 * ����InputStream�ķ�ʽ��ȡSettings�ӿڵ�֧�֡�
 * @version : 2013-9-8
 * @author ������ (zyc@byshell.org)
 */
public class InputStreamSettings extends AbstractBaseSettings implements IOSettings {
    private LinkedList<InputStream> pendingStream = new LinkedList<InputStream>();
    /**���������������Դ*/
    protected InputStreamSettings() {}
    //
    /**����{@link InputStreamSettings}����*/
    public InputStreamSettings(InputStream inStream) throws IOException {
        this(new InputStream[] { inStream });
    }
    /**����{@link InputStreamSettings}����*/
    public InputStreamSettings(InputStream[] inStreams) throws IOException {
        Hasor.assertIsNotNull(inStreams);
        if (inStreams.length == 0)
            return;
        for (InputStream ins : inStreams) {
            Hasor.assertIsNotNull(ins);
            this.addStream(ins);
        }
        this.loadSettings();
    }
    //
    //
    /**��һ����������ӵ������ش����б�ʹ��load�������ش������б��е�����
     * ע�⣺�������б��е���һ��װ����Ͻ���Ӵ������б��������ȥ��*/
    public void addStream(InputStream stream) {
        if (stream != null)
            if (this.pendingStream.contains(stream) == false)
                this.pendingStream.add(stream);
    }
    //
    /**loadװ�����д�������������û�д���������ֱ��return��*/
    public synchronized void loadSettings() throws IOException {
        this.readyLoad();//׼��װ��
        {
            if (this.pendingStream.isEmpty() == true)
                return;
            //����װ�ػ���
            Map<String, Map<String, Object>> loadTo = new HashMap<String, Map<String, Object>>();
            InputStream inStream = null;
            //
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                factory.setFeature("http://xml.org/sax/features/namespaces", true);
                SAXParser parser = factory.newSAXParser();
                SaxXmlParser handler = new SaxXmlParser(loadTo);
                while ((inStream = this.pendingStream.pollFirst()) != null) {
                    parser.parse(inStream, handler);
                    inStream.close();
                }
            } catch (Exception e) {
                throw new IOException(e);
            }
            //
            this.cleanData();
            this.getNamespaceSettingMap().putAll(loadTo);
            for (Map<String, Object> ent : loadTo.values())
                this.getSettingsMap().addMap(ent);
        }
        this.loadFinish();//���װ��
    }
    /**׼��װ��*/
    protected void readyLoad() throws IOException {}
    /**���װ��*/
    protected void loadFinish() throws IOException {}
    /**{@link InputStreamSettings}���Ͳ�֧�ָ÷��������ø÷����������κ����á�*/
    public void refresh() throws IOException {}
}