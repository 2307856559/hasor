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
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.more.beans.info.BeanDefinition;
import org.more.beans.resource.annotation.core.Scan_ClassAnno;
import org.more.beans.resource.annotation.core.Tag_Anno;
import org.more.beans.resource.annotation.util.AnnoEngine;
import org.more.beans.resource.annotation.util.AnnoProcess;
import org.more.beans.resource.xml.XmlEngine;
import org.more.core.io.AutoCloseInputStream;
/**
 * ��չXmlFileResource���ṩע�����÷�ʽ��֧�֣������ļ��е����ñȽ�ע��������Ȩ��
 * @version 2010-1-10
 * @author ������ (zyc@byshell.org)
 */
public class AnnoXmlFileResource extends XmlFileResource {
    //========================================================================================Field
    /**  */
    private static final long serialVersionUID = -4764919069857076109L;
    //==================================================================================Constructor
    /**����AnnoXmlFileResource����*/
    public AnnoXmlFileResource() {
        super((String) null);
    };
    /**����AnnoXmlFileResource���󣬲���file�������ļ�λ�á�*/
    public AnnoXmlFileResource(File file) {
        super(file);
    };
    /**����AnnoXmlFileResource���󣬲���xmlURI�������ļ�λ�á�*/
    public AnnoXmlFileResource(URI xmlURI) {
        super(xmlURI);
    };
    //=====================================================================================Job Core
    private Tag_Anno            annoTag  = new Tag_Anno();
    private Scan_ClassAnno      annoScan = new Scan_ClassAnno();
    /**���е�bean����*/
    private Map<String, String> annoBeanNameMap;
    /**����Ҫ������װ�ص�bean����*/
    private List<String>        annoStrartInitBeans;
    /*-------------------------------------------------*/
    protected void anotherClassAnnoEngine(Scan_ClassAnno annoEngine) {}
    @Override
    protected void anotherXmlEngine(XmlEngine engine) {
        super.anotherXmlEngine(engine);
        engine.regeditTag("anno", annoTag);
    };
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void init() throws Exception {
        if (this.isInit() == true)
            return;
        super.init();//ִ�г�ʼ���������ڳ�ʼ��ʱ���Զ����õ�anno��ǩ��������
        this.annoBeanNameMap = this.annoTag.getScanBeansResult();//��ȡɨ�赽��bean����������ӳ����
        this.annoStrartInitBeans = this.annoTag.getScanInitBeansResult();//��ȡҪ���ʼ����bean�������
        this.annoTag.lockScan();//����ɨ�������ڽ���ǰ���ڴ���anno:annoɨ���ǩ��ɨ�������
        this.annoScan.init();
        Properties tag = new Properties();
        tag.load(new AutoCloseInputStream(XmlFileResource.class.getResourceAsStream("/org/more/beans/resource/annotation/core/anno.properties")));//װ�ر�ǩ������������
        for (Object key : tag.keySet()) {
            Class<? extends Annotation> forAnno = (Class<? extends Annotation>) Class.forName((String) key);
            Class<?> forProcess = Class.forName(tag.getProperty((String) key));
            this.annoScan.regeditAnno(forAnno, (AnnoProcess) forProcess.newInstance());
        }
        this.anotherClassAnnoEngine(this.annoScan);
    };
    @Override
    public synchronized void destroy() {
        this.annoTag.unLockScan();//����ɨ����������
        this.annoBeanNameMap.clear();
        this.annoBeanNameMap = null;
        this.annoStrartInitBeans.clear();
        this.annoStrartInitBeans = null;
        this.annoTag.destroy();//������ǩ����
        this.annoScan.destroy();
        super.destroy();
    };
    @Override
    public boolean containsBeanDefinition(String name) {
        if (super.containsBeanDefinition(name) == true)
            return true;
        else
            return this.annoBeanNameMap.containsKey(name);
    }
    @Override
    protected BeanDefinition findBeanDefinition(String name) throws Exception {
        BeanDefinition bean = super.findBeanDefinition(name);
        if (bean != null)
            return bean;
        if (this.annoBeanNameMap.containsKey(name) == false)
            return null;
        AnnoEngine ae = new AnnoEngine();
        return (BeanDefinition) ae.runTask(Class.forName(this.annoBeanNameMap.get(name)), annoScan, new BeanDefinition()).context;
    }
    @Override
    public List<String> getBeanDefinitionNames() {
        List<String> superCache = super.getBeanDefinitionNames();
        ArrayList<String> al = new ArrayList<String>(superCache.size() + this.annoBeanNameMap.size());
        al.addAll(superCache);
        al.addAll(this.annoBeanNameMap.keySet());
        return al;
    };
    public List<String> getStrartInitBeanDefinitionNames() {
        List<String> superCache = super.getStrartInitBeanDefinitionNames();
        ArrayList<String> al = new ArrayList<String>(superCache.size() + this.annoStrartInitBeans.size());
        al.addAll(superCache);
        al.addAll(this.annoStrartInitBeans);
        return al;
    };
}