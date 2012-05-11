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
package org.more.webui.freemarker.loader.template.mto;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
/**
 * ģ����Դ��ȡ�࣬������һ�������ࡣ��ʵ���������ζ�ȡ��ͬ��ʽ����Դ��
 * @version : 2011-9-16
 * @author ������ (zyc@byshell.org)
 */
public interface AbstractTemplateObject {
    public final static String DefaultEncoding = "utf-8";
    /**��ȡ�ö�������޸�ʱ�䡣*/
    public long lastModified();
    /**��ȡ�����{@link Reader}���ڻ�ȡ֮ǰ������Ҫ����{@link #openObject()}�����򿪸ö���*/
    public Reader getReader(String encoding) throws IOException;
    /**��ȡ�����{@link InputStream}��*/
    public InputStream getInputStream() throws IOException;
    /**����Դ*/
    public void openObject();
    /**�ر���Դ*/
    public void closeObject();
};