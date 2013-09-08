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
package net.hasor.context.setting;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import net.hasor.Hasor;
import org.more.util.map.DecSequenceMap;
import org.more.xml.XmlParserKitManager;
import org.more.xml.stream.XmlReader;
/***
 * ����Reader�ķ�ʽ��ȡSettings�ӿڵ�֧�֡�
 * @version : 2013-9-8
 * @author ������ (zyc@byshell.org)
 */
public class ReaderSettings extends AbstractIOSettings {
    private LinkedList<Reader> pendingReader = new LinkedList<Reader>();
    //
    /**����{@link ReaderSettings}����*/
    public ReaderSettings(Reader inReader) throws IOException, XMLStreamException {
        this(new Reader[] { inReader });
    }
    /**����{@link ReaderSettings}����*/
    public ReaderSettings(Reader[] inReaders) throws IOException, XMLStreamException {
        super();
        Hasor.assertIsNotNull(inReaders);
        for (Reader ins : inReaders) {
            Hasor.assertIsNotNull(ins);
            this.addReader(ins);
        }
        this.loadReaders();
    }
    /**��һ����������ӵ������ش����б�ʹ��load�������ش������б��е�����
     * ע�⣺�������б��е���һ��װ����Ͻ���Ӵ������б��������ȥ��*/
    public void addReader(Reader reader) {
        if (reader != null)
            if (this.pendingReader.contains(reader) == false)
                this.pendingReader.add(reader);
    }
    /**loadװ�����д�������������û�д���������ֱ��return��*/
    public final synchronized void loadReaders() throws IOException, XMLStreamException {
        if (this.pendingReader.isEmpty() == true)
            return;
        //����װ�ػ���
        Map<String, Map<String, Object>> loadTo = this.getNamespaceSettingMap();
        XmlParserKitManager xmlParserKit = this.getXmlParserKitManager(loadTo);
        xmlParserKit.setContext(this);
        Reader inReader = null;
        //
        this.readyLoad();//׼��װ��
        while ((inReader = this.pendingReader.pollFirst()) != null) {
            new XmlReader(inReader).reader(xmlParserKit, null);
            inReader.close();
        }
        //
        this.loadFinish();//���װ��
    }
    /**׼��װ��*/
    protected void readyLoad() {}
    /**���װ��*/
    protected void loadFinish() {}
    /**{@link ReaderSettings}���Ͳ�֧�ָ÷�����������ø÷�����õ�һ��{@link UnsupportedOperationException}�����쳣��*/
    public void refresh() throws IOException {
        throw new UnsupportedOperationException();
    }
    //
    private DecSequenceMap<String, Object>   mergeSettingsMap     = new DecSequenceMap<String, Object>();
    private Map<String, Map<String, Object>> namespaceSettingsMap = new HashMap<String, Map<String, Object>>();
    //
    protected Map<String, Map<String, Object>> getNamespaceSettingMap() {
        return namespaceSettingsMap;
    }
    protected Map<String, Object> getSettingsMap() {
        return mergeSettingsMap;
    }
    protected synchronized XmlParserKitManager getXmlParserKitManager(Map<String, Map<String, Object>> loadTo) throws IOException {
        XmlParserKitManager kitManager = super.getXmlParserKitManager(loadTo);
        this.mergeSettingsMap.removeAllMap();
        for (Map<String, Object> ent : loadTo.values())
            this.mergeSettingsMap.addMap(ent);
        return kitManager;
    }
}