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
package org.more.hypha.context;
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
import org.more.hypha.DefineResource;
import org.more.hypha.beans.TypeManager;
import org.more.hypha.beans.support.TagBeans_Beans;
import org.more.hypha.event.Config_LoadedXmlEvent;
import org.more.hypha.event.Config_LoadingXmlEvent;
import org.more.hypha.event.ReloadDefineEvent;
import org.more.util.ClassPathUtil;
/**
 * 
 * @version 2010-11-30
 * @author ������ (zyc@byshell.org)
 */
public class XmlDefineResource extends ArrayDefineResource {
    private static final String               ResourcePath = "/META-INF/resource/hypha/register.xml";
    private ArrayList<Object>                 sourceArray  = new ArrayList<Object>();
    private TypeManager                       typeManager  = new TypeManager();                      //���ͽ���
    private XmlParserKitManager               manager      = new XmlParserKitManager();              //xml������
    //
    //========================================================================================��̬����
    private static List<XmlNameSpaceRegister> registers    = null;
    private static synchronized void r_s_init(XmlDefineResource resource) throws Throwable {
        XmlDefineResource.registers = null;
        XmlDefineResource.s_init(resource);
    }
    private static synchronized void s_init(XmlDefineResource resource) throws Throwable {
        //�ؼ�-��ʼ��
        if (XmlDefineResource.registers == null) {
            List<InputStream> ins = ClassPathUtil.getResource(ResourcePath);
            _NameSpaceConfiguration ns = new _NameSpaceConfiguration();
            for (InputStream is : ins)
                new XmlReader(is).reader(ns, null);
            XmlDefineResource.registers = ns.getRegister();
        }
        for (XmlNameSpaceRegister reg : registers)
            /**��һ����������{@link NameSpaceRegisterPropxy}�����еõ�*/
            reg.initRegister(null, resource);
    }
    /**����{@link XmlDefineResource}���󣬸÷�������������ɨ��ClassPath�е������ռ�ע�ᡣ*/
    public static XmlDefineResource newInstance() throws Throwable {
        return new XmlDefineResource(false);
    };
    /**����{@link XmlDefineResource}���󣬸÷�������������ɨ��ClassPath�е������ռ�ע�ᡣ*/
    public static XmlDefineResource newInstanceByNew() throws Throwable {
        return new XmlDefineResource(true);
    };
    //========================================================================================���췽��
    /**����{@link XmlDefineResource}���󣬲���sinit�����Ƿ�����װ�������ռ������*/
    private XmlDefineResource(boolean sinit) throws Throwable {
        if (sinit == true)
            r_s_init(this);
        else
            s_init(this);
    }
    /**����{@link XmlDefineResource}����*/
    public XmlDefineResource() throws Throwable {
        this(false);//������װ�������ռ�ע�ᡣ
    }
    /**����{@link XmlDefineResource}����sourceFile��Ҫװ�ص������ļ�����·��Ӧ���൱��classpath��*/
    public XmlDefineResource(String sourceFile) throws Throwable {
        this();
        this.addSource(sourceFile);
    }
    /**����{@link XmlDefineResource}����sourceFiles��Ҫװ�ص������ļ�����·��Ӧ���൱��classpath��*/
    public XmlDefineResource(String[] sourceFiles) throws Throwable {
        this();
        for (String sf : sourceFiles)
            this.addSource(sf);
    }
    /**����{@link XmlDefineResource}����sourceURL��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URL sourceURL) throws Throwable {
        this();
        this.addSource(sourceURL);
    }
    /**����{@link XmlDefineResource}����sourceURLs��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URL[] sourceURLs) throws Throwable {
        this();
        for (URL url : sourceURLs)
            this.addSource(url);
    }
    /**����{@link XmlDefineResource}����sourceURI��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URI sourceURI) throws Throwable {
        this();
        this.addSource(sourceURI);
    }
    /**����{@link XmlDefineResource}����sourceURIs��Ҫװ�ص������ļ���*/
    public XmlDefineResource(URI[] sourceURIs) throws Throwable {
        this();
        for (URI uri : sourceURIs)
            this.addSource(uri);
    }
    /**����{@link XmlDefineResource}����sourceFile��Ҫװ�ص������ļ���*/
    public XmlDefineResource(File sourceFile) throws Throwable {
        this();
        this.addSource(sourceFile);
    }
    /**����{@link XmlDefineResource}����sourceFiles��Ҫװ�ص������ļ���*/
    public XmlDefineResource(File[] sourceFiles) throws Throwable {
        this();
        for (File file : sourceFiles)
            this.addSource(file);
    }
    /**����{@link XmlDefineResource}����sourceStream��Ҫװ�ص������ļ�����*/
    public XmlDefineResource(InputStream sourceStream) throws Throwable {
        this();
        this.addSource(sourceStream);
    }
    /**����{@link XmlDefineResource}����sourceStream��Ҫװ�ص������ļ�����*/
    public XmlDefineResource(InputStream[] sourceStreams) throws Throwable {
        this();
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
    /**��ȡ{@link XmlParserKitManager}*/
    protected XmlParserKitManager getManager() {
        return this.manager;
    };
    /**��ȡ���ͽ�����*/
    public TypeManager getTypeManager() {
        return this.typeManager;
    };
    /**ע��һ����ǩ�������߼���*/
    public synchronized void regeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.regeditKit(namespace, kit);
    };
    /**ȡ��һ����ǩ�������߼���ע�ᡣ*/
    public synchronized void unRegeditXmlParserKit(String namespace, XmlParserKit kit) {
        this.manager.unRegeditKit(namespace, kit);
    };
    /**���������ļ�����*/
    protected synchronized void passerXml(InputStream in, DefineResource conf) throws XMLStreamException {
        XmlReader reader = new XmlReader(in);
        this.manager.getContext().setAttribute(TagBeans_Beans.BeanDefineManager, this);
        reader.reader(this.manager, null);
    };
    /**�ֶ�ִ������װ�ض���������ظ�װ�ؿ��ܲ����쳣���ö�����������{@link Config_LoadingXmlEvent}�¼�*/
    public synchronized void loadDefine() throws IOException, XMLStreamException {
        this.getEventManager().doEvent(new Config_LoadingXmlEvent(this, this));//��ʼװ��Beans
        for (Object obj : this.sourceArray)
            if (obj instanceof InputStream) {
                InputStream is = (InputStream) obj;
                try {
                    //ע��������һ����ͼ�����������ĳ���
                    is.reset();
                } catch (Exception e) {}
                this.passerXml(is, this);
            } else if (obj instanceof URL) {
                InputStream is = ((URL) obj).openStream();
                this.passerXml(is, this);
                is.close();
            } else if (obj instanceof URI) {
                InputStream is = ((URI) obj).toURL().openStream();
                this.passerXml(is, this);
                is.close();
            } else if (obj instanceof File) {
                FileInputStream is = new FileInputStream((File) obj);
                this.passerXml(is, this);
                is.close();
            } else if (obj instanceof String) {
                List<InputStream> xmlINS = ClassPathUtil.getResource((String) obj);
                for (InputStream is : xmlINS) {
                    this.passerXml(is, this);
                    is.close();
                }
            }
        this.getEventManager().doEvent(new Config_LoadedXmlEvent(this, this));//װ��Beans����
    };
    /**����װ�����ã��÷���������ִ��clearDefine()���������ִ��loadDefine()����ִ��֮ǰ�÷���������{@link ReloadDefineEvent}�¼���*/
    public synchronized void reloadDefine() throws IOException, XMLStreamException {
        this.getEventManager().doEvent(new ReloadDefineEvent(this));//����Beans
        this.clearDefine();
        this.clearPlugin();
        this.loadDefine();
    }
}