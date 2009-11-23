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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.more.DoesSupportException;
import org.more.InitializationException;
import org.more.beans.BeanResource;
import org.more.beans.info.BeanDefinition;
import org.more.beans.resource.xml.XMLEngine;
import org.more.util.attribute.AttBase;
/**
 * 
//-----------------------------------------------------------------------------
//�����������췽��
//��������������������
//�ġ�������������
 * <br/>Date : 2009-11-21
 * @author Administrator
 */
@SuppressWarnings("unchecked")
public class XmlFileResource extends AttBase implements BeanResource {
    //========================================================================================Field
    /**  */
    private static final long                 serialVersionUID = 5085542182667236561L;
    protected XMLEngine                       xmlEngine        = null;                                 //
    protected int                             staticCacheSize  = 10;                                   //
    protected HashMap<String, BeanDefinition> staticCache      = new HashMap<String, BeanDefinition>(); //��̬����
    protected int                             dynamicCacheSize = 50;                                   //
    protected HashMap<String, BeanDefinition> dynamicCache     = new HashMap<String, BeanDefinition>(); //��̬����
    protected ArrayList                       initBeanNS       = null;                                 //Ҫ���ʼ����bean���ơ�
    protected ArrayList                       allBeanNS        = null;                                 //����bean���ơ�
    //==================================================================================Constructor
    /**����XmlFileResource����*/
    public XmlFileResource() {
        this.xmlEngine = new XMLEngine();
    }
    /**����XmlFileResource���󣬲���filePath�������ļ�λ�á�*/
    public XmlFileResource(String filePath) throws FileNotFoundException {
        this();
        try {
            this.xmlEngine.scanningXML(new FileInputStream(filePath), ".*");
        } catch (XMLStreamException e) {
            throw new InitializationException("�ڶ�ȡXML����ʱ�����쳣����Ϣ:" + e.getMessage());
        }
    }
    /**����XmlFileResource���󣬲���file�������ļ�λ�á�*/
    public XmlFileResource(File file) throws FileNotFoundException {
        this();
        try {
            this.xmlEngine.scanningXML(new FileInputStream(file), ".*");
        } catch (XMLStreamException e) {
            throw new InitializationException("�ڶ�ȡXML����ʱ�����쳣����Ϣ:" + e.getMessage());
        }
    }
    /**����XmlFileResource���󣬲���in�������ļ�����*/
    public XmlFileResource(InputStream in) {
        this();
        try {
            this.xmlEngine.scanningXML(in, ".*");
        } catch (XMLStreamException e) {
            throw new InitializationException("�ڶ�ȡXML����ʱ�����쳣����Ϣ:" + e.getMessage());
        }
    }
    //=========================================================================================Impl
    @Override
    public void clearCache() throws DoesSupportException {
        this.staticCache.clear();
        this.dynamicCache.clear();
    }
    @Override
    public boolean containsBeanDefinition(String name) {
        return this.xmlEngine.testPath("/beans/bean/@name=" + name);
    }
    @Override
    public BeanDefinition getBeanDefinition(String name) {
        if (this.staticCache.containsKey(name) == true)
            return this.staticCache.get(name);
        if (this.dynamicCache.containsKey(name) == true)
            return this.dynamicCache.get(name);
        BeanDefinition bean = this.xmlEngine.findBeanDefinition(name);
        if (bean != null) {
            this.dynamicCache.put(bean.getName(), bean);//����
        }
        return bean;
    }
    @Override
    public List<String> getBeanDefinitionNames() {
        return (List<String>) this.allBeanNS.clone();
    }
    @Override
    public String getResourceDescription() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public File getSourceFile() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getSourceName() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public URI getSourceURI() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public URL getSourceURL() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<String> getStrartInitBeanDefinitionNames() {
        return (List<String>) this.initBeanNS.clone();
    }
    @Override
    public boolean isCacheBeanMetadata() {
        return true;
    }
    @Override
    public boolean isFactory(String name) {
        String isFactory = this.xmlEngine.getPath("");
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean isPrototype(String name) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean isSingleton(String name) {
        // TODO Auto-generated method stub
        return false;
    }
}