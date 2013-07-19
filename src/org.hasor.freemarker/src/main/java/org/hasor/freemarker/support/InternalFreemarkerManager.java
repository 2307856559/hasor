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
package org.hasor.freemarker.support;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.inject.Inject;
import org.hasor.context.AppContext;
import org.hasor.context.HasorSettingListener;
import org.hasor.context.Settings;
import org.hasor.freemarker.ConfigurationFactory;
import org.hasor.freemarker.FreemarkerManager;
import org.hasor.freemarker.loader.ConfigTemplateLoader;
import org.more.util.CommonCodeUtils;
import com.google.inject.Singleton;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 
 * @version : 2013-5-17
 * @author ������ (zyc@byshell.org)
 */
@Singleton
class InternalFreemarkerManager implements FreemarkerManager, HasorSettingListener {
    @Inject
    private AppContext             appContext;
    private ConfigTemplateLoader   stringLoader;
    private ConfigurationFactory   configurationFactory;
    private volatile Configuration configuration;
    private ReadWriteLock          configurationLock = new ReentrantReadWriteLock();
    //
    //
    @Override
    public void start() {
        this.configurationLock.writeLock().lock();//����(д)
        this.stringLoader = new ConfigTemplateLoader();
        this.configurationFactory = this.appContext.getInstance(ConfigurationFactory.class);;
        this.appContext.getSettings().addSettingsListener(this);
        this.configurationLock.writeLock().unlock();//����(д)
        /*Ҫ�ŵ������棬����������������*/
        this.getFreemarker();
    }
    @Override
    public void stop() {
        this.configurationLock.writeLock().lock();//����(д)
        this.stringLoader = null;
        this.configurationFactory = null;
        this.configuration = null;
        this.appContext.getSettings().removeSettingsListener(this);
        this.configurationLock.writeLock().unlock();//����(д)
    }
    @Override
    public boolean isRunning() {
        return configuration != null;
    }
    //
    @Override
    public void onLoadConfig(Settings newConfig) {
        this.configurationLock.writeLock().lock();//����(д)
        this.configuration = null;
        this.configurationLock.writeLock().unlock();//����(д)
    }
    public final Configuration getFreemarker() {
        this.configurationLock.readLock().lock();//����(��)
        if (this.configuration == null) {
            /*������*/
            this.configurationLock.readLock().unlock();
            this.configurationLock.writeLock().lock();
            if (this.configuration == null) {
                /*׼��*/
                if (this.configurationFactory == null) {
                    this.configurationLock.writeLock().unlock();
                    throw new NullPointerException("ConfigurationFactory is not set.");
                }
                /*����*/
                this.configurationLock.readLock().lock();//����(��)
                if (this.configuration == null) {
                    this.configuration = this.configurationFactory.configuration(this.appContext);
                    ArrayList<TemplateLoader> loaders = new ArrayList<TemplateLoader>();
                    if (this.configuration.getTemplateLoader() != null)
                        loaders.add(this.configuration.getTemplateLoader());
                    loaders.add(this.configurationFactory.createConfigTemplateLoader(this.appContext));
                    loaders.add(this.configurationFactory.createTemplateLoader(this.appContext));
                    this.configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
                }
            }
            /*������*/
            this.configurationLock.readLock().lock();
            this.configurationLock.writeLock().unlock();
        }
        this.configurationLock.readLock().unlock();//����(��)
        return this.configuration;
    }
    @Override
    public Template getTemplate(String templateName) throws TemplateException, IOException {
        return this.getFreemarker().getTemplate(templateName);
    }
    //
    //
    @Override
    public void processTemplate(String templateName) throws TemplateException, IOException {
        this.processTemplate(templateName, null, null);
    }
    @Override
    public void processTemplate(String templateName, Object rootMap) throws TemplateException, IOException {
        this.processTemplate(templateName, rootMap, null);
    }
    @Override
    public void processTemplate(String templateName, Object rootMap, Writer writer) throws TemplateException, IOException {
        Writer writerTo = (writer == null) ? new InternalNoneWriter() : writer;
        this.getTemplate(templateName).process(rootMap, writerTo);
    }
    //
    //
    @Override
    public String processString(String templateString) throws TemplateException, IOException {
        StringWriter stringWriter = new StringWriter();
        this.processString(templateString, null, stringWriter);
        return stringWriter.toString();
    }
    @Override
    public String processString(String templateString, Object rootMap) throws TemplateException, IOException {
        StringWriter stringWriter = new StringWriter();
        this.processString(templateString, rootMap, stringWriter);
        return stringWriter.toString();
    }
    //
    //
    @Override
    public void processString(String templateString, Writer writer) throws TemplateException, IOException {
        this.processString(templateString, null, writer);
    }
    @Override
    public void processString(String templateString, Object rootMap, Writer writer) throws TemplateException, IOException {
        //A.ȡ��ָ��
        String hashStr = null;
        try {
            /*ʹ��MD5����*/
            hashStr = CommonCodeUtils.MD5.getMD5(templateString);
        } catch (NoSuchAlgorithmException e) {
            /*ʹ��hashCode*/
            hashStr = String.valueOf(templateString.hashCode());
        }
        hashStr += ".temp";
        //B.�����ݼ��뵽ģ��������С�
        this.stringLoader.addTemplateAsString(hashStr, templateString);
        //C.ִ��ָ��ģ��
        Writer writerTo = (writer == null) ? new InternalNoneWriter() : writer;
        Template temp = this.getTemplate(hashStr);
        if (temp != null)
            temp.process(rootMap, writerTo);
    }
}