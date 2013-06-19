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
package org.more.hypha.xml;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.more.core.error.LoadException;
import org.more.core.log.Log;
import org.more.core.log.LogFactory;
import org.more.core.xml.XmlParserKit;
import org.more.core.xml.register.XmlRegister;
import org.more.core.xml.stream.XmlReader;
import org.more.hypha.context.array.ArrayDefineResource;
import org.more.hypha.xml._NameSpaceConfiguration.RegisterBean;
import org.more.util.ResourcesUtil;
import org.more.webui.event.Event;
/**
 * �����Ǽ̳���{@link ArrayDefineResource}�࣬ͨ��������Զ�ȡ�����������ļ��е��ඨ����Ϣ��
 * @version 2010-11-30
 * @author ������ (zyc@byshell.org)
 */
public class XmlDefineResource extends ArrayDefineResource {
    private static Log          log                 = LogFactory.getLog(XmlDefineResource.class);
    private static final String ResourcePath        = "META-INF/resource/hypha/register.xml";            //��ǩע����
    private static final String DefaultResourcePath = "META-INF/resource/hypha/default-hypha-config.xml"; //Ĭ�������ļ�
    private ArrayList<Object>   sourceArray         = new ArrayList<Object>();
    private XmlRegister         xmlRegister         = new XmlRegister();                                 //xml������
    private boolean             loadMark            = false;                                             //�Ƿ��Ѿ�ִ�й�װ��.
    /*------------------------------------------------------------*/
    /*------------------------------------------------------------*/
    /**����{@link XmlDefineResource}���󣬲���sinit�����Ƿ�����װ�������ռ������*/
    private XmlDefineResource(boolean sinit) throws IOException, XMLStreamException, LoadException {
        if (sinit == true) {
            log.info("create XmlDefineResource yes reload 'register.xml'");
            r_s_init(this);
        } else {
            log.info("create XmlDefineResource no reload 'register.xml'");
            s_init(this);
        }
    }
    /**����{@link XmlDefineResource}����*/
    public XmlDefineResource() throws IOException, XMLStreamException, LoadException {
        this(false);//������װ�������ռ�ע�ᡣ
    }
    /**����{@link XmlDefineResource}����sourceFile��Ҫװ�ص������ļ�����·��Ӧ���൱��classpath��*/
    public XmlDefineResource(String sourceFile) throws IOException, XMLStreamException, LoadException {
        this();
        this.addSource(sourceFile);
    }
    /**����{@link XmlDefineResource}����sourceFiles��Ҫװ�ص������ļ�����·��Ӧ���൱��classpath��*/
    public XmlDefineResource(String[] sourceFiles) throws IOException, XMLStreamException, LoadException {
        this();
        for (String sf : sourceFiles)
            this.addSource(sf);
    }
    /**����{@link XmlDefineResource}����sourceURL��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URL sourceURL) throws IOException, XMLStreamException, LoadException {
        this();
        this.addSource(sourceURL);
    }
    /**����{@link XmlDefineResource}����sourceURLs��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URL[] sourceURLs) throws IOException, XMLStreamException, LoadException {
        this();
        for (URL url : sourceURLs)
            this.addSource(url);
    }
    /**����{@link XmlDefineResource}����sourceURI��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URI sourceURI) throws IOException, XMLStreamException, LoadException {
        this();
        this.addSource(sourceURI);
    }
    /**����{@link XmlDefineResource}����sourceURIs��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URI[] sourceURIs) throws IOException, XMLStreamException, LoadException {
        this();
        for (URI uri : sourceURIs)
            this.addSource(uri);
    }
    /**����{@link XmlDefineResource}����sourceFile��Ҫװ�ص������ļ���*/
    public XmlDefineResource(File sourceFile) throws IOException, XMLStreamException, LoadException {
        this();
        this.addSource(sourceFile);
    }
    /**����{@link XmlDefineResource}����sourceFiles��Ҫװ�ص������ļ���*/
    public XmlDefineResource(File[] sourceFiles) throws IOException, XMLStreamException, LoadException {
        this();
        for (File file : sourceFiles)
            this.addSource(file);
    }
    /**����{@link XmlDefineResource}����sourceStream��Ҫװ�ص������ļ�����*/
    public XmlDefineResource(InputStream sourceStream) throws IOException, XMLStreamException, LoadException {
        this();
        this.addSource(sourceStream);
    }
    /**����{@link XmlDefineResource}����sourceStream��Ҫװ�ص������ļ�����*/
    public XmlDefineResource(InputStream[] sourceStreams) throws IOException, XMLStreamException, LoadException {
        this();
        for (InputStream is : sourceStreams)
            this.addSource(is);
    }
    /*------------------------------------------------------------*/
    /**����{@link XmlDefineResource}���󣬸÷�������������ɨ��ClassPath�е������ռ�ע�ᡣ*/
    public static XmlDefineResource newInstance() throws IOException, XMLStreamException, LoadException {
        return new XmlDefineResource(false);
    };
    /**����{@link XmlDefineResource}���󣬸÷�������������ɨ��ClassPath�е������ռ�ע�ᡣ*/
    public static XmlDefineResource newInstanceByNew() throws IOException, XMLStreamException, LoadException {
        return new XmlDefineResource(true);
    };
    private void addSourceArray(Object source) {
        if (source == null) {
            log.warning("addSource source is null.");
            return;
        }
        if (this.sourceArray.contains(source) == false) {
            log.debug("addSource {%0}.", source);
            this.sourceArray.add(source);
        } else
            log.warning("addSource source error ,exist source. {%0}", source);
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
    /**��ȡһ��״̬��״̬�����Ƿ��Ѿ�׼���ã�{@link XmlDefineResource}�����е�ִ����װ�ط���֮��÷�������true���򷵻�false��*/
    public boolean isReady() {
        return this.loadMark;
    }
    /**���������ļ�����*/
    protected synchronized void passerXml(InputStream in, DefineResource conf) throws XMLStreamException, IOException {
        new XmlReader(in).reader(this.xmlRegister, null);/*�ڶ����������ų�·��*/
    };
    /**�ֶ�ִ������װ�ض���������ظ�װ�ؿ��ܲ����쳣���ö�����������{@link XmlLoadingEvent}�¼�*/
    public synchronized void loadDefine() throws IOException, XMLStreamException {
        log.info("loadDefine source count = {%0}.", this.sourceArray.size());
        this.throwEvent(Event.getEvent(XmlLoadEvent.class), this);//��ʼװ��Beans
        int count = this.sourceArray.size();
        for (int i = 0; i < count; i++) {
            Object obj = this.sourceArray.get(i);
            log.info("passerXml {%0} of {%1}. source = {%2}.", i, count, obj);
            this.throwEvent(Event.getEvent(XmlLoadingEvent.class), this, obj);
            if (obj instanceof InputStream) {
                InputStream is = (InputStream) obj;
                try {
                    //ע��������һ����ͼ�����������ĳ���
                    log.debug("reset InputStream. Stream = {%0}", is);
                    is.reset();
                } catch (Exception e) {
                    log.warning("reset InputStream error ,Stream not supported.");
                }
                this.passerXml(is, this);
            } else if (obj instanceof URL) {
                URL url = ((URL) obj);
                log.debug("load URL '{%0}'", url);
                InputStream is = url.openStream();
                this.passerXml(is, this);
                is.close();
            } else if (obj instanceof URI) {
                URI uri = ((URI) obj);
                log.debug("load URI '{%0}'", uri);
                InputStream is = uri.toURL().openStream();
                this.passerXml(is, this);
                is.close();
            } else if (obj instanceof File) {
                File file = (File) obj;
                log.debug("load File '{%0}'", file);
                FileInputStream is = new FileInputStream(file);
                this.passerXml(is, this);
                is.close();
            } else if (obj instanceof String) {
                List<URL> urls = ResourcesUtil.getResources((String) obj);
                log.debug("load String '{%0}' include [{%1}]", obj, urls);
                for (URL url : urls) {
                    InputStream is = url.openConnection().getInputStream();
                    this.passerXml(is, this);
                    is.close();
                }
            }
        }
        this.throwEvent(Event.getEvent(XmlLoadedEvent.class), this);//װ��Beans����
        log.info("loadDefine finish!");
        this.loadMark = true;
    };
    /**����װ�����ã��÷���������ִ��clearDefine()���������ִ��loadDefine()����ִ��֮ǰ�÷���������{@link XmlReloadDefineEvent}�¼���*/
    public synchronized void reloadDefine() throws IOException, XMLStreamException {
        log.info("throw XmlReloadDefineEvent...");
        this.throwEvent(Event.getEvent(XmlReloadDefineEvent.class), this);//����Beans
        this.clearDefine();
        this.getFlash().clearAttribute();
        this.loadDefine();
    }
    public synchronized void clearDefine() {
        super.clearDefine();
        this.loadMark = false;
    }
}