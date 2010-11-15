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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.more.core.xml.XmlParserKit;
import org.more.core.xml.XmlParserKitManager;
import org.more.core.xml.stream.XmlReader;
import org.more.hypha.ApplicationContext;
import org.more.hypha.DefineResource;
import org.more.hypha.beans.TypeManager;
import org.more.hypha.beans.support.TagBeans_Beans;
import org.more.hypha.event.Config_BeginBuildEvent;
import org.more.hypha.event.Config_EndBuildEvent;
import org.more.hypha.event.Config_LoadedXmlEvent;
import org.more.hypha.event.Config_LoadingXmlEvent;
import org.more.util.ClassPathUtil;
/**
 * ��������������{@link DefineResource}�ӿڵ��ࡣ�����ְ�����ռ��κο��Խ�������������Դ��
 * ������build����ʱ���빹�����������ݲ�ͬ�����ø�ʽ������ת��Ϊ���ڶ�ȡ��
 * �Լ�����ֵԪ��Ϣ���������ɡ�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class XmlConfiguration {
    /**  */
    private static final long   serialVersionUID = -2907262416329013610L;
    private static final String ResourcePath     = "/META-INF/resource/hypha/register.xml";
    private ArrayList<Object>   sourceArray      = new ArrayList<Object>();
    private TypeManager         typeManager      = new TypeManager();                      //���ͽ���
    private XmlParserKitManager manager          = new XmlParserKitManager();              //xml������
    //========================================================================================���췽��
    /**����{@link XmlConfiguration}����*/
    public XmlConfiguration() {}
    /**����{@link XmlConfiguration}����sourceFile��Ҫװ�ص������ļ�����·��Ӧ���൱��classpath��*/
    public XmlConfiguration(String sourceFile) {
        this.addSource(sourceFile);
    }
    /**����{@link XmlConfiguration}����sourceFiles��Ҫװ�ص������ļ�����·��Ӧ���൱��classpath��*/
    public XmlConfiguration(String[] sourceFiles) {
        for (String sf : sourceFiles)
            this.addSource(sf);
    }
    /**����{@link XmlConfiguration}����sourceURL��Ҫװ�ص������ļ���*/
    public XmlConfiguration(URL sourceURL) {
        this.addSource(sourceURL);
    }
    /**����{@link XmlConfiguration}����sourceURLs��Ҫװ�ص������ļ���*/
    public XmlConfiguration(URL[] sourceURLs) {
        for (URL url : sourceURLs)
            this.addSource(url);
    }
    /**����{@link XmlConfiguration}����sourceURI��Ҫװ�ص������ļ���*/
    public XmlConfiguration(URI sourceURI) {
        this.addSource(sourceURI);
    }
    /**����{@link XmlConfiguration}����sourceURIs��Ҫװ�ص������ļ���*/
    public XmlConfiguration(URI[] sourceURIs) {
        for (URI uri : sourceURIs)
            this.addSource(uri);
    }
    /**����{@link XmlConfiguration}����sourceFile��Ҫװ�ص������ļ���*/
    public XmlConfiguration(File sourceFile) {
        this.addSource(sourceFile);
    }
    /**����{@link XmlConfiguration}����sourceFiles��Ҫװ�ص������ļ���*/
    public XmlConfiguration(File[] sourceFiles) {
        for (File file : sourceFiles)
            this.addSource(file);
    }
    /**����{@link XmlConfiguration}����sourceStream��Ҫװ�ص������ļ�����*/
    public XmlConfiguration(InputStream sourceStream) {
        this.addSource(sourceStream);
    }
    /**����{@link XmlConfiguration}����sourceStream��Ҫװ�ص������ļ�����*/
    public XmlConfiguration(InputStream[] sourceStreams) {
        for (InputStream is : sourceStreams)
            this.addSource(is);
    }
    //========================================================================================
    private void addSourceArray(Object source) {
        if (source == null)
            throw new NullPointerException("����Ϊ��");
        if (this.sourceArray.contains(source) == false)
            this.sourceArray.add(source);
    }
    /**�����Դ��*/
    public void addSource(InputStream stream) {
        this.addSourceArray(stream);
    }
    /**�����Դ��*/
    public void addSource(URI uri) {
        this.addSourceArray(uri);
    }
    /**�����Դ��*/
    public void addSource(URL url) {
        this.addSourceArray(url);
    }
    /**�����Դ��*/
    public void addSource(File file) {
        this.addSourceArray(file);
    }
    /**�����Դ������Դ�Ĵ��·���������classpath��*/
    public void addSource(String source) {
        this.addSourceArray(source);
    }
    //========================================================================================
    /**���������ļ�����*/
    protected void passerXml(InputStream in, DefineResource conf) throws XMLStreamException {
        XmlReader reader = new XmlReader(in);
        this.manager.getContext().setAttribute(TagBeans_Beans.BeanDefineManager, this);
        reader.reader(this.manager, null);
    };
    /**��ȡ{@link XmlParserKitManager}*/
    protected XmlParserKitManager getManager() {
        return this.manager;
    }
    /**ע��һ����ǩ�������߼���*/
    public void regeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.regeditKit(namespace, kit);
    }
    /**ȡ��һ����ǩ�������߼���ע�ᡣ*/
    public void unRegeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.unRegeditKit(namespace, kit);
    }
    //========================================================================================
    public ApplicationContext buildApp(Object context) throws Throwable {
        return null;//TODO
    }
    /**����{@link DefineResourceImpl}����ע�⣺�������̰����������ļ�װ�ء�*/
    public DefineResource build(String sourceName, ClassLoader loader) throws Throwable {
        //1.����
        DefineResourceImpl conf = new DefineResourceImpl(this);
        conf.getEventManager().doEvent(new Config_BeginBuildEvent(this, conf));//��ʼ����
        if (loader == null)
            loader = ClassLoader.getSystemClassLoader();
        conf.setClassLoader(loader);
        conf.setSourceName(sourceName);
        //2.��ʼ��
        List<InputStream> ins = ClassPathUtil.getResource(ResourcePath);
        NameSpaceConfiguration ns = new NameSpaceConfiguration();
        for (InputStream is : ins)
            new XmlReader(is).reader(ns, null);
        List<NameSpaceRegister> registers = ns.getRegister();
        for (NameSpaceRegister reg : registers)
            /**��һ����������{@link NameSpaceRegisterPropxy}�����еõ�*/
            reg.initRegister(null, this, conf);
        //
        conf.loadDefine();
        conf.getEventManager().doEvent(new Config_EndBuildEvent(this, conf));//��������
        return conf;
    }
    /**ʹ�õ�ǰ��������Ϣװ��{@link DefineResource}����*/
    public synchronized DefineResource loadConfig(DefineResource conf) throws IOException, XMLStreamException {
        conf.getEventManager().doEvent(new Config_LoadingXmlEvent(this, conf));//��ʼװ��Beans
        for (Object obj : this.sourceArray)
            if (obj instanceof InputStream) {
                InputStream is = (InputStream) obj;
                try {
                    //ע��������һ����ͼ�����������ĳ���
                    is.reset();
                } catch (Exception e) {}
                this.passerXml(is, conf);
            } else if (obj instanceof URL) {
                InputStream is = ((URL) obj).openStream();
                this.passerXml(is, conf);
                is.close();
            } else if (obj instanceof URI) {
                InputStream is = ((URI) obj).toURL().openStream();
                this.passerXml(is, conf);
                is.close();
            } else if (obj instanceof File) {
                FileInputStream is = new FileInputStream((File) obj);
                this.passerXml(is, conf);
                is.close();
            } else if (obj instanceof String) {
                List<InputStream> xmlINS = ClassPathUtil.getResource((String) obj);
                for (InputStream is : xmlINS) {
                    this.passerXml(is, conf);
                    is.close();
                }
            }
        conf.getEventManager().doEvent(new Config_LoadedXmlEvent(this, conf));//װ��Beans����
        return conf;
    }
    /**��ȡ���ͽ�����*/
    public TypeManager getTypeManager() {
        return this.typeManager;
    }
}