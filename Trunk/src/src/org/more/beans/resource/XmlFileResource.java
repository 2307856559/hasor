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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.more.DoesSupportException;
import org.more.FormatException;
import org.more.beans.BeanResource;
import org.more.beans.info.BeanDefinition;
import org.more.beans.resource.xml.TagProcess;
import org.more.beans.resource.xml.TaskProcess;
import org.more.beans.resource.xml.XmlEngine;
import org.more.core.io.AutoCloseInputStream;
import org.more.util.attribute.AttBase;
/**
 * �ṩ����XML��Ϊ����Դ�ṩbean���ݵ�֧�֡����ʹ���޲εĹ��췽��XmlFileResource�ཫʹ���ֶ�
 * defaultConfigFile����ʾ���ļ�����ΪĬ�������ļ�λ�ã��������ļ�λ������ڳ�������Ŀ¼�¡�
 * @version 2010-1-11
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class XmlFileResource extends ArrayResource implements BeanResource {
    //========================================================================================Field
    /**  */
    private static final long  serialVersionUID  = 5085542182667236561L;
    /**Ĭ�������ļ���*/
    public final static String defaultConfigFile = "more-config.xml";
    /**�Ƿ���XSD��֤*/
    private boolean            validator         = true;
    /**�����ļ�λ��URI���ַ���*/
    private URI                sourceURI;
    //==================================================================================Constructor
    /**����XmlFileResource���󣬸ö���ʹ��XmlFileResource.defaultConfigFile��ΪĬ�������ļ�λ�á� */
    public XmlFileResource(String sourceName) {
        super(sourceName, null);
        this.sourceURI = new File(XmlFileResource.defaultConfigFile).toURI();
    };
    /**����XmlFileResource���󣬲���xmlResourceURI�������ļ�λ�á�*/
    public XmlFileResource(URI xmlResourceURI) {
        this(xmlResourceURI.toString());
        this.sourceURI = xmlResourceURI;
    };
    /**����XmlFileResource���󣬲���xmlResourceFile�������ļ�λ�á�*/
    public XmlFileResource(File xmlResourceFile) {
        this(xmlResourceFile.toString());
        this.sourceURI = xmlResourceFile.toURI();
    };
    //==========================================================================================Job
    /**�Ƿ���Schema��֤XML������ȷ�ԣ�Ĭ��ֵ��true������֤��*/
    public boolean isValidator() {
        return validator;
    };
    /**�����Ƿ���Schema��֤XML������ȷ�ԡ�*/
    public void setValidator(boolean validator) {
        this.validator = validator;
    };
    @Override
    public URI getSourceURI() {
        return this.sourceURI;
    };
    @Override
    public boolean isCacheBeanMetadata() {
        return true;
    };
    /** ͨ��Schema��֤XML�����Ƿ���ȷ������null��ʾ��֤ͨ�������򷵻ش�����Ϣ�� */
    public Exception validatorXML() {
        if (this.validator == false)
            return null;
        //----
        try {
            InputStream in = new AutoCloseInputStream(XmlFileResource.class.getResourceAsStream("/META-INF/xsl-list"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str = null;
            ArrayList<Source> sourceList = new ArrayList<Source>();
            while ((str = br.readLine()) != null) {
                InputStream xsdIn = XmlFileResource.class.getResourceAsStream(str);
                if (xsdIn != null)
                    sourceList.add(new StreamSource(xsdIn));
            }
            Source[] source = new Source[sourceList.size()];
            sourceList.toArray(source);
            //
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);//����schema����
            Schema schema = schemaFactory.newSchema(source); //����schema������������֤�ĵ��ļ���������Schema����
            Validator validator = schema.newValidator();//ͨ��Schema��������ڴ�Schema����֤��������students.xsd������֤
            Source xmlSource = new StreamSource(this.getXmlInputStream());//�õ���֤������Դ
            //��ʼ��֤���ɹ����success!!!��ʧ�����fail
            validator.validate(xmlSource);
            return null;
        } catch (Exception ex) {
            return ex;
        }
    };
    /**��ȡXML������*/
    protected InputStream getXmlInputStream() throws MalformedURLException, IOException {
        return this.sourceURI.toURL().openStream();
    };
    /*-------------------------------------------------------------------------------------------*/
    /**/
    /**/
    //=====================================================================================Override
    /**/
    /**/
    /*-------------------------------------------------------------------------------------------*/
    /**XML��������*/
    protected XmlEngine                     xmlEngine = new XmlEngine();
    /*-------------------------------------------------------*/
    /**���е�bean����*/
    private List<String>                    xmlBeanNames;
    /**����Ҫ������װ�ص�bean����*/
    private List<String>                    xmlStrartInitBeans;
    /*-------------------------------------------------------*/
    /**��̬���������Ŀ��*/
    private int                             dynamicCacheSize;
    /**��̬�������*/
    private HashMap<String, BeanDefinition> dynamicCache;
    /**��̬����������Ƽ���*/
    private LinkedList<String>              dynamicCacheNames;
    //=======================================================Protected
    protected void putDynamicCache(BeanDefinition bean) {
        if (dynamicCacheNames.size() >= this.dynamicCacheSize)
            this.dynamicCache.remove(dynamicCacheNames.removeFirst());
        this.dynamicCache.put(bean.getName(), bean);//����
    }
    // (TaskProcess)tasks.getAttribute(value)
    protected void anotherXmlEngine(XmlEngine engine) {};
    /**ִ��XML����*/
    protected Object runTask(TaskProcess task, String processXPath, Object[] params) throws Exception {
        return this.xmlEngine.runTask(this.getXmlInputStream(), task, processXPath, params);
    };
    //=============================================================Job
    /** ����Ѿ���ʼ����ִ��������ִ�г�ʼ����*/
    public synchronized void reload() throws Exception {
        if (this.isInit() == true)
            this.destroy();
        this.init();
    }
    public synchronized void init() throws Exception {
        if (this.isInit() == true)
            return;
        super.init();
        /*----------------------------------------------һ����֤XML*/
        Exception validator = this.validatorXML();
        if (validator != null)
            throw new FormatException("Schema��֤ʧ��", validator);
        /*----------------------------------------------������ʼ����Ҫ����*/
        this.dynamicCache = new HashMap<String, BeanDefinition>();//��̬�������
        this.dynamicCacheNames = new LinkedList<String>();//��̬����������Ƽ���
        this.setSourceName(this.sourceURI.toString());
        /*----------------------------------------------������ȡ��ǩ����*/
        Properties tag = new Properties();
        tag.load(new AutoCloseInputStream(XmlFileResource.class.getResourceAsStream("/org/more/beans/resource/xml/core/tagProcess.properties")));//װ�ر�ǩ������������
        for (Object tagName : tag.keySet()) {
            Class<?> tagProcessType = Class.forName(tag.getProperty((String) tagName));
            this.xmlEngine.regeditTag((String) tagName, (TagProcess) tagProcessType.newInstance());
        }
        //
        Properties task = new Properties();
        task.load(new AutoCloseInputStream(XmlFileResource.class.getResourceAsStream("/org/more/beans/resource/xml/core/taskProcess.properties")));//װ������������
        for (Object taskName : task.keySet()) {
            Class<?> taskType = Class.forName(task.getProperty((String) taskName));
            xmlEngine.setAttribute((String) taskName, (TaskProcess) taskType.newInstance());
        }
        this.anotherXmlEngine(xmlEngine);
        /*----------------------------------------------�ġ���������ִ�н����*/
        TaskProcess init_task = (TaskProcess) xmlEngine.getAttribute("init");
        AttBase att = (AttBase) this.runTask(init_task, ".*", null);
        this.dynamicCacheSize = (Integer) att.get("dynamicCache");
        HashMap<String, BeanDefinition> staticCache = (HashMap<String, BeanDefinition>) att.get("beanList");//��ȡ��̬bean����
        for (BeanDefinition b : staticCache.values())
            this.addBeanDefinition(b);
        this.xmlBeanNames = (List<String>) att.get("allNames");//��ȡ����bean����
        this.xmlStrartInitBeans = (List<String>) att.get("initBean");//��ȡ����Ҫ���ʼ����bean����
    };
    @Override
    public synchronized void destroy() {
        this.clearCache();
        this.clearAttribute();
        this.xmlBeanNames.clear();//���е�bean����
        this.xmlBeanNames = null;//���е�bean����
        this.dynamicCacheSize = 50;//��̬���������Ŀ��
        this.dynamicCache.clear();//��̬�������
        this.dynamicCache = null;//��̬�������
        this.dynamicCacheNames.clear();//��̬����������Ƽ���
        this.dynamicCacheNames = null;//��̬����������Ƽ���
        this.xmlStrartInitBeans.clear();
        this.xmlStrartInitBeans = null;
        this.xmlEngine.destroy();
        super.destroy();
    };
    @Override
    public synchronized void clearCache() throws DoesSupportException {
        this.dynamicCacheNames.clear();
        this.dynamicCache.clear();
        super.clearCache();
    }
    @Override
    public boolean containsBeanDefinition(String name) {
        if (super.containsBeanDefinition(name) == true)
            return true;
        else
            return this.xmlBeanNames.contains(name);
    }
    @Override
    protected BeanDefinition findBeanDefinition(String name) throws Exception {
        if (this.dynamicCacheNames.contains(name) == true)
            return this.dynamicCache.get(name);
        /*--------------*/
        TaskProcess find_task = (TaskProcess) xmlEngine.getAttribute("findBean");
        BeanDefinition bean = (BeanDefinition) this.runTask(find_task, ".*", new Object[] { name });
        if (bean != null)
            this.putDynamicCache(bean);
        return bean;
    }
    @Override
    public List<String> getBeanDefinitionNames() {
        List<String> staticCache = super.getBeanDefinitionNames();
        ArrayList<String> al = new ArrayList<String>(staticCache.size() + this.xmlBeanNames.size());
        al.addAll(staticCache);
        for (String str : this.xmlBeanNames)
            if (al.contains(str) == false)
                al.add(str);
        return al;
    }
    @Override
    public List<String> getStrartInitBeanDefinitionNames() {
        List<String> start_1 = super.getStrartInitBeanDefinitionNames();
        ArrayList<String> al = new ArrayList<String>(start_1.size() + this.xmlStrartInitBeans.size());
        al.addAll(start_1);
        for (String str : this.xmlStrartInitBeans)
            if (al.contains(str) == false)
                al.add(str);
        return al;
    }
};