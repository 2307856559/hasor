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
package org.more.services.freemarker.loader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.more.services.freemarker.FreemarkerService;
import org.more.services.freemarker.loader.mto.AbstractTemplateObject;
import org.more.services.freemarker.loader.mto.ClassPath_TemplateObject;
import org.more.services.freemarker.loader.mto.File_TemplateObject;
import org.more.services.freemarker.loader.mto.String_TemplateObject;
import org.more.services.freemarker.loader.mto.URL_TemplateObject;
import freemarker.cache.TemplateLoader;
/**
 * ���������ļ�����ӵ�ģ�塣
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org) 
 */
public class MoreTemplateLoader implements TemplateLoader {
    private Map<String, AbstractTemplateObject> objectMap = null;
    private FreemarkerService                   service   = null;
    //
    public MoreTemplateLoader(FreemarkerService service) {
        this.objectMap = new HashMap<String, AbstractTemplateObject>();
        this.service = service;
    };
    /**��classpath�е�һ����Դ��ַ��Ϊģ��������ӵ�װ�����С�*/
    public void addTemplate(String name, String classPath) {
        this.addTemplate(name, classPath, null);
    };
    public void addTemplate(String name, String classPath, String encoding) {
        String $encoding = encoding;
        $encoding = ($encoding != null) ? encoding : this.service.getInEncoding();
        this.objectMap.put(name, new ClassPath_TemplateObject(classPath, $encoding));
    }
    /**��{@link File}��ַ��Ϊģ��������ӵ�װ�����С�*/
    public void addTemplate(String name, File filePath) {
        this.addTemplate(name, filePath, null);
    };
    public void addTemplate(String name, File filePath, String encoding) {
        String $encoding = encoding;
        $encoding = ($encoding != null) ? encoding : this.service.getInEncoding();
        this.objectMap.put(name, new File_TemplateObject(filePath, $encoding));
    }
    /**��{@link URL}��ַ��Ϊģ��������ӵ�װ�����С�*/
    public void addTemplate(String name, URL urlPath) {
        this.addTemplate(name, urlPath, null);
    };
    public void addTemplate(String name, URL urlPath, String encoding) {
        String $encoding = encoding;
        $encoding = ($encoding != null) ? encoding : this.service.getInEncoding();
        this.objectMap.put(name, new URL_TemplateObject(urlPath, $encoding));
    }
    /**���ַ�����Ϊģ��������ӵ�װ�����С�*/
    public void addTemplateAsString(String name, String templateString) {
        this.objectMap.put(name, new String_TemplateObject(templateString));
    }
    public Object findTemplateSource(String arg0) throws IOException {
        if (this.objectMap.containsKey(arg0) == false)
            return null;
        return this.objectMap.get(arg0);
    }
    public long getLastModified(Object arg0) {
        AbstractTemplateObject mto = (AbstractTemplateObject) arg0;
        return mto.lastModified();
    }
    public Reader getReader(Object arg0, String encoding) throws IOException {
        if (arg0 instanceof AbstractTemplateObject == false)
            return null;
        AbstractTemplateObject mto = (AbstractTemplateObject) arg0;
        mto.openObject();
        return mto.getReader(encoding);
    }
    public void closeTemplateSource(Object arg0) throws IOException {
        if (arg0 instanceof AbstractTemplateObject == true)
            ((AbstractTemplateObject) arg0).closeObject();
    }
}