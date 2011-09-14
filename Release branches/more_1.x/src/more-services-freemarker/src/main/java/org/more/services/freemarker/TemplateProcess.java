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
import java.io.InputStream;
import java.io.Writer;
import java.util.Map;
/**
 * ����ִ��ģ��Ľӿڡ�
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org)
 */
public interface TemplateProcess {
    /**����ģ�������ж��Ƿ���ڸ�ģ���ļ���*/
    public boolean containsTemplate(String templateName);
    /**����ģ�������ж��Ƿ���ڸ�ģ���ļ���*/
    public boolean containsTemplate(TemplateBlock templateBlock);
    /**
     * ִ��ģ���ļ���
     * @param templateName ģ����
     * @param encoding ģ���ļ��ַ�����
     * @param rootMap root���󣬸�Map�еĶ��������ģ���з��ʵ���
     * @param out ģ��ִ�����λ��
     */
    public void process(String templateName, String encoding, Map<String, ?> rootMap, Writer out);
    /**
     * ִ��ģ���ļ���
     * @param templateBlock ģ��
     * @param encoding ģ���ļ��ַ�����
     * @param rootMap root���󣬸�Map�еĶ��������ģ���з��ʵ���
     * @param out ģ��ִ�����λ��
     */
    public void process(TemplateBlock templateBlock, String encoding, Map<String, ?> rootMap, Writer out);
    /**
     * ִ��ģ���ļ���
     * @param templateName ģ����
     * @param rootMap root���󣬸�Map�еĶ��������ģ���з��ʵ���
     * @param out ģ��ִ�����λ��
     */
    public void process(String templateName, Map<String, ?> rootMap, Writer out);
    /**
     * ִ��ģ���ļ���
     * @param templateBlock ģ��
     * @param rootMap root���󣬸�Map�еĶ��������ģ���з��ʵ���
     * @param out ģ��ִ�����λ��
     */
    public void process(TemplateBlock templateBlock, Map<String, ?> rootMap, Writer out);
    /**
     * ִ��ģ���ļ���
     * @param templateName ģ����
     * @param out ģ��ִ�����λ��
     */
    public void process(String templateName, Writer out);
    /**
     * ִ��ģ���ļ���
     * @param templateBlock ģ��
     * @param out ģ��ִ�����λ��
     */
    public void process(TemplateBlock templateBlock, Writer out);
    /**
     * ����ģ������ȡģ�����ݡ�
     * @param templateName ģ����
     * @return ����ģ������
     */
    public String getTemplateBody(String templateName);
    /**
     * ����ģ���ȡģ�����ݡ�
     * @param templateBlock
     * @return ����ģ������
     */
    public String getTemplateBody(TemplateBlock templateBlock);
    /**
     * ����ģ������ȡģ�����ݡ�
     * @param templateName ģ����
     * @return ����ģ�����ݵ�������
     */
    public InputStream getTemplateBodyAsStream(String templateName);
    /**
     * ����ģ���ȡģ�����ݡ�
     * @param templateBlock
     * @return ����ģ�����ݵ�������
     */
    public InputStream getTemplateBodyAsStream(TemplateBlock templateBlock);
}