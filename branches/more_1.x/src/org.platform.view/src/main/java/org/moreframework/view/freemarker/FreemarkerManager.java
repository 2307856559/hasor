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
package org.moreframework.view.freemarker;
import java.io.IOException;
import java.io.Writer;
import org.moreframework.context.AppContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * Freemarkerģ�幦���ṩ�ࡣ
 * @version : 2013-5-6
 * @author ������ (zyc@byshell.org)
 */
public interface FreemarkerManager {
    /**��ʼ����������*/
    public void initManager(AppContext appContext);
    /**���ٷ���*/
    public void destroyManager(AppContext appContext);
    /**��ȡ����ִ��ģ���Freemarker*/
    public Configuration getFreemarker();
    /**��ȡģ�塣*/
    public Template getTemplate(String templateName) throws TemplateException, IOException;
    //
    /**��ȡ��ִ��ģ�塣*/
    public void processTemplate(String templateName) throws TemplateException, IOException;
    /**��ȡ��ִ��ģ�塣*/
    public void processTemplate(String templateName, Object rootMap) throws TemplateException, IOException;
    /**��ȡ��ִ��ģ�塣*/
    public void processTemplate(String templateName, Object rootMap, Writer writer) throws TemplateException, IOException;
    //
    /**���ַ�����������Ϊģ��ִ�С�*/
    public String processString(String templateString) throws TemplateException, IOException;
    /**���ַ�����������Ϊģ��ִ�С�*/
    public String processString(String templateString, Object rootMap) throws TemplateException, IOException;
    //
    /**���ַ�����������Ϊģ��ִ�С�*/
    public void processString(String templateString, Writer writer) throws TemplateException, IOException;
    /**���ַ�����������Ϊģ��ִ�С�*/
    public void processString(String templateString, Object rootMap, Writer writer) throws TemplateException, IOException;
}