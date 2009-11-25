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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.more.DoesSupportException;
import org.more.InvokeException;
import org.more.NoDefinitionException;
import org.more.ResourceException;
import org.more.beans.BeanResource;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.CreateTypeEnum;
import org.more.beans.resource.xml.XMLEngine;
import org.more.util.attribute.AttBase;
/**
 * 
 * <br/>Date : 2009-11-25
 * @author ������
 */
public class XmlFileResource extends AttBase implements BeanResource {
    //========================================================================================Field
    /**  */
    private static final long               serialVersionUID    = 5085542182667236561L;
    private File                            xmlFile             = null;                                 //�����ļ�
    private URI                             xmlURI              = null;                                 //�����ļ�
    private URL                             xmlURL              = null;                                 //�����ļ�
    private String                          resourceDescription = null;                                 //˵��
    private ArrayList<String>               initNames           = null;                                 //Ҫ������ʱװ�ص�bean
    private ArrayList<String>               allNames            = null;                                 //Ҫ������ʱװ�ص�bean
    /*---------------------*/
    /** XML�������� */
    protected XMLEngine                     xmlEngine           = null;
    /**��̬���������Ŀ��*/
    protected int                           staticCacheSize     = 10;
    /**��̬�������*/
    private HashMap<String, BeanDefinition> staticCache         = new HashMap<String, BeanDefinition>();
    /**��̬���������Ŀ��*/
    protected int                           dynamicCacheSize    = 50;
    /**��̬�������*/
    private HashMap<String, BeanDefinition> dynamicCache        = new HashMap<String, BeanDefinition>();
    /***/
    private LinkedList<String>              dynamicCacheNames   = new LinkedList<String>();
    //==================================================================================Constructor
    /**����XmlFileResource����*/
    public XmlFileResource() {
        this.xmlEngine = new XMLEngine();
    }
    /**����XmlFileResource���󣬲���filePath�������ļ�λ�á�*/
    public XmlFileResource(String filePath) throws Exception {
        this();
        this.xmlFile = new File(filePath);
        this.reload();
    }
    /**����XmlFileResource���󣬲���file�������ļ�λ�á�*/
    public XmlFileResource(File file) throws Exception {
        this();
        this.xmlFile = file;
        this.reload();
    }
    /**����XmlFileResource���󣬲���xmlURI�������ļ�λ�á�*/
    public XmlFileResource(URI xmlURI) throws Exception {
        this();
        this.xmlURI = xmlURI;
        this.reload();
    }
    /**����XmlFileResource���󣬲���xmlURL�������ļ�λ�á�*/
    public XmlFileResource(URL xmlURL) throws Exception {
        this();
        this.xmlURL = xmlURL;
        this.reload();
    }
    //=========================================================================================Impl
    @SuppressWarnings("unchecked")
    private void reload() throws Exception {
        this.clearCache();
        AttBase att = (AttBase) this.xmlEngine.runTask(this.getXmlInputStream(), "init", ".*");
        this.dynamicCacheSize = (Integer) att.get("dynamicCache");
        this.staticCacheSize = (Integer) att.get("staticCache");
        this.staticCache = (HashMap<String, BeanDefinition>) att.get("beanList");
        this.initNames = (ArrayList<String>) att.get("initBean");
        this.allNames = (ArrayList<String>) att.get("allNames");
    }
    /**��ȡXML��������*/
    private InputStream getXmlInputStream() {
        try {
            if (this.xmlFile != null)
                return new FileInputStream(this.xmlFile);
            if (this.xmlURL != null)
                this.xmlURL.openConnection().getInputStream();
            if (this.xmlURI != null)
                this.xmlURI.toURL().openConnection().getInputStream();
            throw new NoDefinitionException("û�ж����κ�XML����Դ��Ϣ��");
        } catch (IOException e) {
            throw new ResourceException("�޷���ȡXML������������msg=" + e.getMessage());
        }
    }
    @Override
    public void clearCache() throws DoesSupportException {
        this.staticCache.clear();
        this.dynamicCache.clear();
    }
    @Override
    public boolean containsBeanDefinition(String name) {
        return this.allNames.contains(name);
    }
    @Override
    public BeanDefinition getBeanDefinition(String name) {
        try {
            if (this.staticCache.containsKey(name) == true)
                return this.staticCache.get(name);
            if (this.dynamicCache.containsKey(name) == true)
                return this.dynamicCache.get(name);
            BeanDefinition bean = (BeanDefinition) this.xmlEngine.runTask(this.getXmlInputStream(), "findBean", ".*", name);
            if (bean != null) {
                if (dynamicCacheNames.size() >= this.dynamicCacheSize)
                    this.dynamicCache.remove(dynamicCacheNames.removeFirst());
                else
                    this.dynamicCache.put(bean.getName(), bean);//����
            }
            return bean;
        } catch (Exception e) {
            throw new InvokeException("ִ��findBean�����ڼ䷢���쳣��", e);
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getBeanDefinitionNames() {
        return (List<String>) this.allNames.clone();
    }
    @Override
    public String getResourceDescription() {
        return resourceDescription;
    }
    @Override
    public File getSourceFile() {
        return this.xmlFile;
    }
    @Override
    public String getSourceName() {
        if (this.xmlFile != null)
            return this.xmlFile.getName();
        if (this.xmlURL != null)
            this.xmlURL.getFile();
        if (this.xmlURI != null)
            this.xmlURI.getPath();
        return null;
    }
    @Override
    public URI getSourceURI() {
        return this.xmlURI;
    }
    @Override
    public URL getSourceURL() {
        return this.xmlURL;
    }
    @Override
    public List<String> getStrartInitBeanDefinitionNames() {
        return initNames;
    }
    @Override
    public boolean isCacheBeanMetadata() {
        return true;
    }
    /**
     * ����ĳ����Bean�Ƿ�Ϊ����ģʽ���������Ŀ��bean�������򷵻�false��
     * @param name Ҫ���Ե�Bean���ơ�
     * @return ���ز��Խ�����������ԭ��ģʽ�����򷵻�true,���򷵻�false��
     */
    @Override
    public boolean isFactory(String name) {
        try {
            if (this.staticCache.containsKey(name) == true)
                return (this.staticCache.get(name).getCreateType() == CreateTypeEnum.Factory) ? true : false;
            if (this.dynamicCache.containsKey(name) == true)
                return (this.dynamicCache.get(name).getCreateType() == CreateTypeEnum.Factory) ? true : false;
            BeanDefinition bean = (BeanDefinition) this.xmlEngine.runTask(this.getXmlInputStream(), "findBean", ".*", name);
            if (bean == null)
                return false;
            return (bean.getCreateType() == CreateTypeEnum.Factory) ? true : false;
        } catch (Exception e) {
            throw new InvokeException("ִ��findBean�����ڼ䷢���쳣��", e);
        }
    }
    @Override
    public boolean isPrototype(String name) {
        return !isSingleton(name);
    }
    @Override
    public boolean isSingleton(String name) {
        try {
            String str = (String) this.xmlEngine.runTask(this.getXmlInputStream(), "getAttribute", ".*", name, "singleton");
            return str.equals("true") ? true : false;
        } catch (Exception e) {
            throw new InvokeException("ִ��getAttribute�����ڼ䷢���쳣��", e);
        }
    }
}