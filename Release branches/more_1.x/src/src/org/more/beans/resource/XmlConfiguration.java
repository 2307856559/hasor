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
package org.more.beans.resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.more.NoDefinitionException;
import org.more.RepeateException;
import org.more.beans.AbstractBeanDefine;
import org.more.beans.DefineResource;
import org.more.beans.resource.namespace.NameSpaceConfiguration;
import org.more.beans.resource.namespace.beans.TagBeans_Beans;
import org.more.core.xml.XmlParserKit;
import org.more.core.xml.XmlParserKitManager;
import org.more.core.xml.stream.XmlReader;
import org.more.util.ClassPathUtil;
import org.more.util.attribute.AttBase;
/**
 * xml�������������Ѿ������xml��������Ҫ�����й���������Ҫ�����ض�Ҫ��ע����Ӧ�������ռ��������
 * �Լ�����ֵԪ��Ϣ���������ɡ�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class XmlConfiguration extends AttBase implements DefineResource {
    /**  */
    private static final long               serialVersionUID = -2907262416329013610L;
    private String                          sourceName       = null;
    private URI                             sourceURI        = null;
    private InputStream                     sourceStream     = null;
    //
    private ArrayList<String>               defineNames      = new ArrayList<String>();
    private Map<String, AbstractBeanDefine> defineMap        = new HashMap<String, AbstractBeanDefine>();
    private ArrayList<QuickPropertyParser>  quickParser      = new ArrayList<QuickPropertyParser>();
    private XmlParserKitManager             manager          = new XmlParserKitManager();
    //========================================================================================
    /**����{@link XmlConfiguration}����init������Ҫ�ֶ����С�*/
    public XmlConfiguration() throws IOException, XMLStreamException {
        this.initRegedit(this);
    }
    /**����{@link XmlConfiguration}����sourceFile��Ҫװ�ص������ļ���*/
    public XmlConfiguration(String sourceFile) throws IOException, XMLStreamException {
        this(new File(sourceFile));
    }
    /**����{@link XmlConfiguration}����sourceURI��Ҫװ�ص������ļ���*/
    public XmlConfiguration(URI sourceURI) throws IOException, XMLStreamException {
        this.initRegedit(this);
        this.sourceURI = sourceURI;
        this.init();
    }
    /**����{@link XmlConfiguration}����sourceFile��Ҫװ�ص������ļ���*/
    public XmlConfiguration(File sourceFile) throws IOException, XMLStreamException {
        this.initRegedit(this);
        this.sourceURI = sourceFile.toURI();
        this.init();
    }
    /**����{@link XmlConfiguration}����sourceStream��Ҫװ�ص������ļ�����*/
    public XmlConfiguration(InputStream sourceStream) throws IOException, XMLStreamException {
        this.initRegedit(this);
        this.sourceStream = sourceStream;
        this.init();
    }
    //========================================================================================
    /**��ȡһ��{@link AbstractBeanDefine}���塣*/
    public AbstractBeanDefine getBeanDefine(String name) {
        return this.defineMap.get(name);
    };
    /**����ĳ�����Ƶ�bean�����Ƿ���ڡ�*/
    public boolean containsBeanDefine(String name) {
        return this.defineMap.containsKey(name);
    }
    /**���һ��Bean���壬����ӵ�Bean����ᱻִ�м�⡣*/
    public void addBeanDefine(AbstractBeanDefine define) {
        if (this.defineMap.containsKey(define.getName()) == true)
            throw new RepeateException("Bean��������[" + define.getName() + "]�ظ�");
        this.defineMap.put(define.getName(), define);
        this.defineNames.add(define.getName());
    };
    /**ʹ��ָ�������������� [jx'xi]*/
    public XmlConfiguration passerXml(InputStream in) throws XMLStreamException {
        //1.����ɨ�裬���е�һ�ν�����
        XmlReader reader = new XmlReader(in);
        this.manager.getContext().setAttribute(TagBeans_Beans.BeanDefineManager, this);
        reader.reader(this.manager, null);
        return this;
    };
    /**��ȡ{@link XmlParserKitManager}*/
    protected XmlParserKitManager getManager() {
        return this.manager;
    }
    /**ִ�г�ʼ��ע�ᡣ */
    private void initRegedit(XmlConfiguration config) throws IOException, XMLStreamException {
        String resourcePath = "/META-INF/resource/beans/regedit.xml";
        List<InputStream> ins = ClassPathUtil.getResource(resourcePath);
        NameSpaceConfiguration ns = new NameSpaceConfiguration(config);
        for (InputStream is : ins)
            new XmlReader(is).reader(ns, null);
    }
    //========================================================================================
    /**ע��һ����������ֵ��������*/
    public void regeditQuickParser(QuickPropertyParser parser) {
        if (parser == null)
            throw new NullPointerException("��������Ϊ��.");
        if (this.quickParser.contains(parser) == false)
            this.quickParser.add(parser);
    }
    /**ȡ��һ����������ֵ��������ע�ᡣ*/
    public void unRegeditQuickParser(QuickPropertyParser parser) {
        if (parser == null)
            throw new NullPointerException("��������Ϊ��.");
        if (this.quickParser.contains(parser) == true)
            this.quickParser.remove(parser);
    }
    /**��ȡע���{@link QuickPropertyParser}����*/
    public List<QuickPropertyParser> getQuickList() {
        return this.quickParser;
    }
    /**ע��һ����ǩ�������߼���*/
    public void regeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.regeditKit(namespace, kit);
    }
    /**ȡ��һ����ǩ�������߼���ע�ᡣ*/
    public void unRegeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.unRegeditKit(namespace, kit);
    }
    /**����{@link XmlConfiguration}�������{@link XmlConfiguration}ʱʹ�õ�������ʽ��ô����Ҫ֧��reset����������쳣��*/
    public void reload() throws XMLStreamException, MalformedURLException, IOException {
        this.destroy();
        this.init();
    }
    /**�������������������³�ʼ��{@link XmlConfiguration}����*/
    public void init() throws XMLStreamException, MalformedURLException, IOException {
        if (this.sourceStream == null)
            this.sourceStream = this.sourceURI.toURL().openStream();
        try {
            this.sourceStream.reset();
        } catch (Exception e) {}
        this.passerXml(this.sourceStream);
    };
    /**�������ע���Bean����*/
    public void destroy() {
        this.defineNames.clear();
        this.defineMap.clear();
    }
    //========================================================================================
    public String getSourceName() {
        return this.sourceName;
    }
    /**������Դ��*/
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public URI getSourceURI() {
        return this.sourceURI;
    }
    public List<String> getBeanDefineNames() {
        return this.defineNames;
    }
    public boolean isPrototype(String name) {
        if (this.containsBeanDefine(name) == false)
            throw new NoDefinitionException("�Ҳ�������Ϊ[" + name + "]��Bean���塣");
        AbstractBeanDefine define = this.getBeanDefine(name);
        return (define.factoryName() == null) ? false : true;
    }
    public boolean isSingleton(String name) {
        if (this.containsBeanDefine(name) == false)
            throw new NoDefinitionException("�Ҳ�������Ϊ[" + name + "]��Bean���塣");
        AbstractBeanDefine define = this.getBeanDefine(name);
        return define.isSingleton();
    }
    public boolean isFactory(String name) {
        if (this.containsBeanDefine(name) == false)
            throw new NoDefinitionException("�Ҳ�������Ϊ[" + name + "]��Bean���塣");
        AbstractBeanDefine define = this.getBeanDefine(name);
        return (define.factoryName() == null) ? false : true;
    }
}