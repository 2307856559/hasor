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
package org.more.hypha.xml.context;
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
import org.more.hypha.Event;
import org.more.hypha.context.ArrayDefineResource;
import org.more.hypha.xml.context._NameSpaceConfiguration.RegisterBean;
import org.more.hypha.xml.event.XmlLoadEvent;
import org.more.hypha.xml.event.XmlLoadedEvent;
import org.more.hypha.xml.event.XmlLoadingEvent;
import org.more.hypha.xml.event.ReloadDefineEvent;
import org.more.hypha.xml.support.XmlNameSpaceRegister;
import org.more.hypha.xml.support.beans.QPP_ROOT;
import org.more.hypha.xml.support.beans.TagBeans_Beans;
import org.more.util.ClassPathUtil;
import org.more.util.attribute.IAttribute;
/**
 * �����Ǽ̳���{@link ArrayDefineResource}�࣬ͨ��������Զ�ȡ�����������ļ��е��ඨ����Ϣ��
 * @version 2010-11-30
 * @author ������ (zyc@byshell.org)
 */
public class XmlDefineResource extends ArrayDefineResource {
    private static final String               ResourcePath        = "/META-INF/resource/hypha/register.xml";            //��ǩע����
    private static final String               DefaultResourcePath = "/META-INF/resource/hypha/default-hypha-config.xml"; //Ĭ�������ļ�
    private ArrayList<Object>                 sourceArray         = new ArrayList<Object>();
    private QPP_ROOT                          typeManager         = new QPP_ROOT();                                     //���ͽ���
    private XmlParserKitManager               manager             = new XmlParserKitManager();                          //xml������
    private boolean                           loadMark            = false;                                              //�Ƿ��Ѿ�ִ�й�װ��.
    //========================================================================================��̬����
    private static List<XmlNameSpaceRegister> registerObjects     = null;
    private static class RegisterPropxy implements XmlNameSpaceRegister {
        private RegisterBean         registerBean   = null;
        private XmlNameSpaceRegister registerObject = null;
        public RegisterPropxy(RegisterBean registerBean) {
            this.registerBean = registerBean;
        };
        public void initRegister(String namespaceURL, XmlDefineResource resource, IAttribute flash) throws Throwable {
            if (this.registerObject == null)
                this.registerObject = (XmlNameSpaceRegister) Class.forName(this.registerBean.registerClass).newInstance();
            this.registerObject.initRegister(this.registerBean.namespace, resource, flash);
        };
    };
    //
    /**��������ռ���������¶�ȡ��register.xml�������ļ���*/
    private static void r_s_init(XmlDefineResource resource) throws Throwable {
        XmlDefineResource.registerObjects = null;
        XmlDefineResource.s_init(resource);
    }
    private static void s_init(XmlDefineResource resource) throws Throwable {
        //�ؼ�-��ʼ��
        if (XmlDefineResource.registerObjects == null) {
            List<InputStream> ins = ClassPathUtil.getResource(ResourcePath);
            _NameSpaceConfiguration ns = new _NameSpaceConfiguration();
            for (InputStream is : ins)
                new XmlReader(is).reader(ns, null);
            List<RegisterBean> registerBeans = ns.getRegisterBean();
            for (RegisterBean reg : registerBeans)
                registerObjects.add(new RegisterPropxy(reg));
        }
        for (XmlNameSpaceRegister reg : registerObjects)
            /**��һ����������{@link NameSpaceRegisterPropxy}�����еõ�*/
            reg.initRegister(null, resource, resource.getFlash());
        //װ������λ��Ĭ������λ�õ������ļ�
        List<InputStream> ins = ClassPathUtil.getResource(DefaultResourcePath);
        for (InputStream source : ins)
            resource.addSource(source);
    }
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
    /**����{@link XmlDefineResource}���󣬸÷�������������ɨ��ClassPath�е������ռ�ע�ᡣ*/
    public static XmlDefineResource newInstance() throws Throwable {
        return new XmlDefineResource(false);
    };
    /**����{@link XmlDefineResource}���󣬸÷�������������ɨ��ClassPath�е������ռ�ע�ᡣ*/
    public static XmlDefineResource newInstanceByNew() throws Throwable {
        return new XmlDefineResource(true);
    };
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
    /**��ȡһ��״̬��״̬�����Ƿ��Ѿ�׼���ã�{@link XmlDefineResource}�����е�ִ����װ�ط���֮��÷�������true���򷵻�false��*/
    public boolean isReady() {
        return this.loadMark;
    };
    /**��ȡ{@link XmlParserKitManager}*/
    protected XmlParserKitManager getManager() {
        return this.manager;
    };
    /**��ȡ���ͽ�����(xmlר��)*/
    public QPP_ROOT getTypeManager() {
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
    /**�ֶ�ִ������װ�ض���������ظ�װ�ؿ��ܲ����쳣���ö�����������{@link XmlLoadingEvent}�¼�*/
    public synchronized void loadDefine() throws IOException, XMLStreamException {
        this.getEventManager().doEvent(Event.getEvent(XmlLoadEvent.class), this);//��ʼװ��Beans
        for (Object obj : this.sourceArray) {
            this.getEventManager().doEvent(Event.getEvent(XmlLoadingEvent.class), this, obj);
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
        }
        this.getEventManager().doEvent(Event.getEvent(XmlLoadedEvent.class), this);//װ��Beans����
        this.loadMark = true;
    };
    /**����װ�����ã��÷���������ִ��clearDefine()���������ִ��loadDefine()����ִ��֮ǰ�÷���������{@link ReloadDefineEvent}�¼���*/
    public synchronized void reloadDefine() throws IOException, XMLStreamException {
        this.getEventManager().doEvent(Event.getEvent(ReloadDefineEvent.class), this);//����Beans
        this.clearDefine();
        this.getFlash().clearAttribute();
        this.loadDefine();
    }
    public synchronized void clearDefine() {
        super.clearDefine();
        this.loadMark = false;
    }
}