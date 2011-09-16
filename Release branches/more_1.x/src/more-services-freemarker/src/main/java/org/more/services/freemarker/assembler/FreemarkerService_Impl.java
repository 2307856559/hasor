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
package org.more.services.freemarker.assembler;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.more.hypha.ApplicationContext;
import org.more.hypha.commons.AbstractService;
import org.more.services.freemarker.FreemarkerService;
import org.more.services.freemarker.FunctionObject;
import org.more.services.freemarker.TagObject;
import org.more.services.freemarker.TemplateBlock;
import org.more.services.freemarker.TemplateProcess;
import org.more.services.freemarker.loader.MoreTemplateLoader;
import org.more.util.attribute.AttBase;
import org.more.util.attribute.IAttribute;
import org.more.util.attribute.SequenceStack;
import org.more.util.attribute.TransformToAttribute;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * {@link FreemarkerService}�ӿ�ʵ���ࡣ
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org)
 */
public class FreemarkerService_Impl extends AbstractService implements FreemarkerService {
    private Configuration         cfg           = null;
    private IAttribute<Object>    thisAtt       = new AttBase<Object>();
    private IAttribute<Object>    tfMap         = null;                        //�ö��������ڴ�ű�ǩ�ͺ���
    private SequenceStack<Object> rootMap       = null;
    //
    private List<TemplateLoader>  loaderList    = null;
    //
    private MoreTemplateLoader    moreLoader    = new MoreTemplateLoader(this);
    private MultiTemplateLoader   multiLoader   = null;
    private Locale                defaultLocale = null;
    private String                inEncoding    = "utf-8";
    private String                outEncoding   = "utf-8";
    /*-------------------------------------------------------------*/
    public void start() {
        this.cfg = new Configuration();
        this.loaderList = new ArrayList<TemplateLoader>();
        this.tfMap = new AttBase<Object>();
        this.loaderList.add(this.moreLoader);
    }
    public void stop() {
        this.cfg = null;
        this.loaderList = null;
        this.tfMap = null;
        this.moreLoader = new MoreTemplateLoader(this);
        this.multiLoader = null;
    }
    /*-------------------------------------------------------------*/
    public void setSuperRoot(IAttribute<Object> superRoot) {
        if (superRoot != null)
            this.thisAtt = superRoot;
    }
    public void setSuperRoot(Map<?, ?> superRoot) {
        if (superRoot != null)
            this.thisAtt = new TransformToAttribute<Object>((Map<Object, Object>) superRoot);
    }
    public void setTemplateDir(File templateDir) throws IOException {
        this.addLoader(new FileTemplateLoader(templateDir));
    }
    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
    public String getInEncoding() {
        return this.inEncoding;
    }
    public void setInEncoding(String inEncoding) {
        this.inEncoding = inEncoding;
    }
    public void setOutEncoding(String outEncoding) {
        this.outEncoding = outEncoding;
        this.multiLoader = null;
    }
    public String getOutEncoding() {
        return this.outEncoding;
    }
    public void addTemplate(String name, URL urlPath) {
        this.moreLoader.addTemplate(name, urlPath);
    }
    public void addTemplate(String name, URL urlPath, String encoding) {
        this.moreLoader.addTemplate(name, urlPath, encoding);
    }
    public void addTemplate(String name, String classPath) {
        this.moreLoader.addTemplate(name, classPath);
    }
    public void addTemplate(String name, String classPath, String encoding) {
        this.moreLoader.addTemplate(name, classPath, encoding);
    }
    public void addTemplate(String name, File filePath) {
        this.moreLoader.addTemplate(name, filePath);
    }
    public void addTemplate(String name, File filePath, String encoding) {
        this.moreLoader.addTemplate(name, filePath, encoding);
    }
    public void addTemplateAsString(String name, String templateString) {
        this.moreLoader.addTemplateAsString(name, templateString);
    }
    public synchronized void addLoader(TemplateLoader loader) {
        if (this.loaderList.contains(loader) == true)
            return;
        this.loaderList.add(loader);
        this.multiLoader = null;
    }
    /*-------------------------------------------------------------*/
    public Configuration getPublicConfiguration() {
        this.multiLoader = null;//���øö����Ŀ����Ϊ����Ч���á�
        return this.cfg;
    }
    /**��ȡһ��{@link Configuration}���󣬸ö����ǿ�¡��cfg��*/
    private synchronized Configuration getConfiguration() {
        // FileTemplateLoader f_Loader=new FileTemplateLoader();
        // ClassTemplateLoader c_Loader=new ClassTemplateLoader(loaderClass, path);
        // WebappTemplateLoader w_Loader=new WebappTemplateLoader(servletContext);
        if (this.multiLoader != null)
            return (Configuration) this.cfg.clone();
        //
        TemplateLoader[] loaders = new TemplateLoader[this.loaderList.size()];
        this.loaderList.toArray(loaders);
        this.multiLoader = new MultiTemplateLoader(loaders);
        this.cfg.setTemplateLoader(this.multiLoader);
        this.cfg.setOutputEncoding(this.outEncoding);
        //
        return (Configuration) this.cfg.clone();
    }
    public TemplateProcess getProcess() {
        return new TemplateProcess_Impl(this, this.getConfiguration());
    }
    public TemplateProcess getProcess(File templateDir) throws IOException {
        Configuration cfg = getConfiguration();
        //
        TemplateLoader[] loaders = new TemplateLoader[2];
        loaders[0] = cfg.getTemplateLoader();
        loaders[1] = new FileTemplateLoader(templateDir);
        //
        cfg.setTemplateLoader(new MultiTemplateLoader(loaders));
        return new TemplateProcess_Impl(this, cfg);
    }
    /*-------------------------------------------------------------*/
    public boolean containsTemplate(String templateName) throws IOException {
        return this.getProcess().containsTemplate(templateName);
    }
    public boolean containsTemplate(String templateName, Locale locale) throws IOException {
        return this.getProcess().containsTemplate(templateName, locale);
    }
    public boolean containsTemplate(TemplateBlock templateBlock) throws IOException {
        return this.getProcess().containsTemplate(templateBlock);
    }
    public Template getTemplate(TemplateBlock templateBlock) throws IOException {
        return this.getProcess().getTemplate(templateBlock);
    }
    public Template getTemplate(String templateName, String encoding, Locale locale) throws IOException {
        return this.getProcess().getTemplate(templateName, encoding, locale);
    }
    public void process(TemplateBlock templateBlock, Writer out) throws IOException, TemplateException {
        this.getProcess().process(templateBlock, out);
    }
    public void process(String templateName, String encoding, Map<String, ?> rootMap, Writer out) throws TemplateException, IOException {
        this.getProcess().process(templateName, encoding, rootMap, out);
    }
    public void process(String templateName, Map<String, ?> rootMap, Writer out) throws TemplateException, IOException {
        this.getProcess().process(templateName, rootMap, out);
    }
    public void process(String templateName, Writer out) throws TemplateException, IOException {
        this.getProcess().process(templateName, out);
    }
    public String getTemplateBody(String templateName) throws IOException {
        return this.getProcess().getTemplateBody(templateName);
    }
    public String getTemplateBody(String templateName, String encoding) throws IOException {
        return this.getProcess().getTemplateBody(templateName, encoding);
    }
    public String getTemplateBody(String templateName, String encoding, Locale locale) throws IOException {
        return this.getProcess().getTemplateBody(templateName, encoding, locale);
    }
    public String getTemplateBody(TemplateBlock templateBlock) throws IOException {
        return this.getProcess().getTemplateBody(templateBlock);
    }
    public Reader getTemplateBodyAsReader(String templateName, Locale locale) throws IOException {
        return this.getProcess().getTemplateBodyAsReader(templateName, locale);
    }
    public Reader getTemplateBodyAsReader(String templateName, String encoding, Locale locale) throws IOException {
        return this.getProcess().getTemplateBodyAsReader(templateName, encoding, locale);
    }
    public Reader getTemplateBodyAsReader(TemplateBlock templateBlock) throws IOException {
        return this.getProcess().getTemplateBodyAsReader(templateBlock);
    }
    /*-------------------------------------------------------------*/
    private IAttribute<Object> getSuperRoot() {
        if (this.rootMap == null) {
            this.rootMap = new SequenceStack<Object>();
            this.rootMap.putStack(this.tfMap);
            this.rootMap.putStack(this.thisAtt);
            ApplicationContext appContext = this.getContext();
            if (appContext != null)
                this.rootMap.putStack(appContext);
        }
        return this.rootMap;
    }
    public void setAttribute(String name, Object value) {
        this.thisAtt.setAttribute(name, value);
    }
    public void removeAttribute(String name) {
        this.thisAtt.removeAttribute(name);
    }
    public void clearAttribute() {
        this.thisAtt.clearAttribute();
    }
    public boolean contains(String name) {
        return this.getSuperRoot().contains(name);
    }
    public Object getAttribute(String name) {
        return this.getSuperRoot().getAttribute(name);
    }
    public String[] getAttributeNames() {
        return this.getSuperRoot().getAttributeNames();
    }
    public Map<String, Object> toMap() {
        return this.getSuperRoot().toMap();
    }
    public void regeditObject(String name, FunctionObject function) {
        this.tfMap.setAttribute(name, function);
    }
    public void regeditObject(String name, TagObject tag) {
        this.tfMap.setAttribute(name, tag);
    }
}