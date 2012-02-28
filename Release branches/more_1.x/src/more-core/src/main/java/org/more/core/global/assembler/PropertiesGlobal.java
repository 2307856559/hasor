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
package org.more.core.global.assembler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import org.more.core.global.Global;
import org.more.core.global.GlobalFactory;
import org.more.core.io.ReaderInputStream;
import org.more.util.ResourcesUtil;
/**
* 
* @version : 2011-9-3
* @author ������ (zyc@byshell.org)
*/
public class PropertiesGlobal implements GlobalFactory {
    protected Properties createProperties(InputStream stream) throws IOException {
        Properties prop = new Properties();
        if (stream != null)
            prop.load(stream);
        return prop;
    }
    protected Properties createProperties(Reader reader, String encoding) throws IOException {
        Properties prop = new Properties();
        if (reader != null)
            if (encoding == null)
                prop.load(new ReaderInputStream(reader));
            else
                prop.load(new ReaderInputStream(reader, encoding));
        return prop;
    }
    /*------------------------------------------------------------------------*/
    public Global createGlobal(Object... objects) throws IOException {
        Global global = Global.newInterInstance();
        for (Object obj : objects)
            if (obj instanceof File) {
                //�ļ�װ�أ�keyֵ���ļ�����key�൱�����Ե�������
                File fileObject = (File) obj;
                InputStream stream = ResourcesUtil.getResourceAsStream(fileObject);
                global.addScope(fileObject.getName(), this.createProperties(stream));//�������
            } else if (obj instanceof URL) {
                //URLװ�أ�keyֵ��getFile����key�൱�����Ե�������
                URL urlObject = (URL) obj;
                InputStream stream = ResourcesUtil.getResourceAsStream(urlObject);
                global.addScope(urlObject.getPath(), this.createProperties(stream));//�������
            } else if (obj instanceof URI) {
                //URIװ�أ�keyֵ��getPath����key�൱�����Ե�������
                URI uriObject = (URI) obj;
                InputStream stream = ResourcesUtil.getResourceAsStream(uriObject);
                global.addScope(uriObject.getPath(), this.createProperties(stream));//�������
            } else if (obj instanceof String) {
                //�ַ���װ��
                String stringObject = (String) obj;
                String name = new File(stringObject).getName();
                List<InputStream> streams = ResourcesUtil.getResourcesAsStream(stringObject);
                for (InputStream stream : streams)
                    global.addScope(name, this.createProperties(stream));//�������
            } else if (obj instanceof Reader) {
                //Readerװ�أ�keyֵ�ǿ��ַ�����key�൱�����Ե�������
                Reader readerObject = (Reader) obj;
                global.addScope("", this.createProperties(readerObject, null));//������� XXX: �ڶ�������null��ʾʹ�õı���
            } else if (obj instanceof InputStream) {
                //InputStreamװ�أ�keyֵ�ǿ��ַ�����key�൱�����Ե�������
                InputStream streamObject = (InputStream) obj;
                global.addScope("", this.createProperties(streamObject));//�������
            }
        return global;
    }
};