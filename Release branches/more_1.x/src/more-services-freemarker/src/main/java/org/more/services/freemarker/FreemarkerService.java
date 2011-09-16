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
package org.more.services.freemarker;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import org.more.hypha.Service;
import org.more.util.attribute.IAttribute;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
/**
 * Freemarker���񣬸÷����ṩ��ģ�������֧�֡�
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org)
 */
public interface FreemarkerService extends Service, IAttribute<Object>, TemplateProcess {
    /**��ȡȫ�����ö���*/
    public Configuration getPublicConfiguration();
    /**��ȡĬ������*/
    public Locale getDefaultLocale();
    /**����Ĭ������*/
    public void setDefaultLocale(Locale defaultLocale);
    /**����Ĭ�������ַ����롣*/
    public void setInEncoding(String inEncoding);
    /**��ȡĬ�������ַ����롣*/
    public String getInEncoding();
    /**����Ĭ������ַ����롣*/
    public void setOutEncoding(String outcoding);
    /**��ȡĬ������ַ����롣*/
    public String getOutEncoding();
    /**�������ϼ�rootMap�����ڽ���ģ��ʱrootMap�еĶ�����Ա�ģ����ʵ���*/
    public void setSuperRoot(IAttribute<Object> rootMap);
    /**�������ϼ�rootMap�����ڽ���ģ��ʱrootMap�еĶ�����Ա�ģ����ʵ���*/
    public void setSuperRoot(Map<?, ?> rootMap);
    /**����ģ��ĸ�·��*/
    public void setTemplateDir(File templateDir) throws IOException;
    /**���һ��ģ�壬����ӵ�ģ��ֻ��Ҫ����ģ�������ɡ�*/
    public void addTemplate(String name, String classPath);
    /**���һ��ģ�壬����ӵ�ģ��ֻ��Ҫ����ģ�������ɡ�*/
    public void addTemplate(String name, File filePath);
    /**���һ��ģ�壬����ӵ�ģ��ֻ��Ҫ����ģ�������ɡ�*/
    public void addTemplate(String name, URL urlPath);
    /**���һ��ģ�壬����ӵ�ģ��ֻ��Ҫ����ģ�������ɡ�*/
    public void addTemplate(String name, String classPath, String encoding);
    /**���һ��ģ�壬����ӵ�ģ��ֻ��Ҫ����ģ�������ɡ�*/
    public void addTemplate(String name, File filePath, String encoding);
    /**���һ��ģ�壬����ӵ�ģ��ֻ��Ҫ����ģ�������ɡ�*/
    public void addTemplate(String name, URL urlPath, String encoding);
    /**���һ��ģ�壬����ӵ�ģ��ֻ��Ҫ����ģ�������ɡ�*/
    public void addTemplateAsString(String name, String templateString);
    /**���һ��ģ��װ����*/
    public void addLoader(TemplateLoader loader);
    /**��ȡĬ��·����ģ��ִ�ж��󣬸÷������ص�{@link TemplateProcess}ֻ��װ�ص����¼���λ�õ�ģ�壺classpath�е�ģ�塢more�����ļ������õ�ģ�塢templateDir��������Ŀ¼�µ�ģ�塣*/
    public TemplateProcess getProcess();
    /**��ȡָ��·����ģ��ִ�ж��󣬸÷������ص�{@link TemplateProcess}�������{@link #getProcess()}��֧�ֵ�֮�⻹֧���������·���е�ģ�塣*/
    public TemplateProcess getProcess(File templateDir) throws IOException;
    /**ע��һ������*/
    public void regeditObject(String name, FunctionObject function);
    /**ע��һ����ǩ*/
    public void regeditObject(String name, TagObject tag);
}