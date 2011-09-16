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
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * ����ִ��ģ��Ľӿڡ�
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org)
 */
public interface TemplateProcess {
    /**����ģ�������ж��Ƿ���ڸ�ģ���ļ���*/
    public boolean containsTemplate(String templateName) throws IOException;
    /**����ģ�������ж��Ƿ���ڸ�ģ���ļ���*/
    public boolean containsTemplate(String templateName, Locale locale) throws IOException;
    /**����ģ�������ж��Ƿ���ڸ�ģ���ļ���*/
    public boolean containsTemplate(TemplateBlock templateBlock) throws IOException;
    /**��ȡģ��*/
    public Template getTemplate(TemplateBlock templateBlock) throws IOException;
    /**��ȡģ��*/
    public Template getTemplate(String templateName, String encoding, Locale locale) throws IOException;
    /**
     * ִ��ģ���ļ���
     * @param templateBlock ģ���
     * @param out ģ��ִ�����λ��
     */
    public void process(TemplateBlock templateBlock, Writer out) throws IOException, TemplateException;
    /**
     * ִ��ģ���ļ���
     * @param templateName ģ����
     * @param encoding ģ���ļ��ַ�����
     * @param rootMap root���󣬸�Map�еĶ��������ģ���з��ʵ���
     * @param out ģ��ִ�����λ��
     */
    public void process(String templateName, String encoding, Map<String, ?> rootMap, Writer out) throws IOException, TemplateException;
    /**
     * ִ��ģ���ļ���
     * @param templateName ģ����
     * @param rootMap root���󣬸�Map�еĶ��������ģ���з��ʵ���
     * @param out ģ��ִ�����λ��
     */
    public void process(String templateName, Map<String, ?> rootMap, Writer out) throws IOException, TemplateException;
    /**
     * ִ��ģ���ļ���
     * @param templateName ģ����
     * @param out ģ��ִ�����λ��
     */
    public void process(String templateName, Writer out) throws IOException, TemplateException;
    /**
     * ����ģ������ȡģ�����ݡ�
     * @param templateName ģ����
     * @return ����ģ������
     */
    public String getTemplateBody(String templateName) throws IOException;
    /**
     * ����ģ������ȡģ�����ݡ�
     * @param templateName ģ����
     * @return ����ģ������
     */
    public String getTemplateBody(String templateName, String encoding) throws IOException;
    /**
     * ����ģ������ȡģ�����ݡ�
     * @param templateName ģ����
     * @return ����ģ������
     */
    public String getTemplateBody(String templateName, String encoding, Locale locale) throws IOException;
    /**
     * ����ģ���ȡģ�����ݡ�
     * @param templateBlock
     * @return ����ģ������
     */
    public String getTemplateBody(TemplateBlock templateBlock) throws IOException;
    /**
     * ����ģ������ȡģ�����ݡ�
     * @param templateName ģ����
     * @return ����ģ�����ݵ�������
     */
    public Reader getTemplateBodyAsReader(String templateName, Locale locale) throws IOException;
    /**
     * ����ģ������ȡģ�����ݡ�
     * @param templateName ģ����
     * @return ����ģ�����ݵ�������
     */
    public Reader getTemplateBodyAsReader(String templateName, String encoding, Locale locale) throws IOException;
    /**
     * ����ģ���ȡģ�����ݡ�
     * @param templateBlock
     * @return ����ģ�����ݵ�������
     */
    public Reader getTemplateBodyAsReader(TemplateBlock templateBlock) throws IOException;
}