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
package net.hasor.context.setting;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import net.hasor.Hasor;
import org.more.util.StringUtils;
/***
 * ����InputStream�ķ�ʽ��ȡSettings�ӿڵ�֧�֡�
 * @version : 2013-9-8
 * @author ������ (zyc@byshell.org)
 */
public class FileInputStreamSettings extends InputStreamSettings {
    //
    /**����{@link FileInputStreamSettings}����*/
    public FileInputStreamSettings(String fileName) throws IOException, XMLStreamException {
        this(new filef, null);
    }
    /**����{@link FileInputStreamSettings}����*/
    public FileInputStreamSettings(String[] fileNames) throws IOException, XMLStreamException {
        this(fileNames, null);
    }
    /**����{@link FileInputStreamSettings}����*/
    public FileInputStreamSettings(String fileName, String encoding) throws IOException, XMLStreamException {
        this(new String[] { fileName }, encoding);
    }
    /**����{@link FileInputStreamSettings}����*/
    public FileInputStreamSettings(String[] fileNames, String encoding) throws IOException, XMLStreamException {
        this(inStreams, null);
    }
    /**����{@link FileInputStreamSettings}����*/
    public FileInputStreamSettings(File inStream) throws IOException, XMLStreamException {
        this(new InputStream[] { inStream }, encoding);
        FileInputStream fis;
    }
    /**����{@link FileInputStreamSettings}����*/
    public FileInputStreamSettings(InputStream[] inStreams, String encoding) throws IOException, XMLStreamException {
        super();
        Hasor.assertIsNotNull(inStreams);
        for (InputStream ins : inStreams) {
            Hasor.assertIsNotNull(ins);
            this.addStream(ins);
        }
        if (StringUtils.isBlank(encoding) == false)
            this.setSettingEncoding(encoding);
        this.loadStreams();
    }
    /**{@link FileInputStreamSettings}���Ͳ�֧�ָ÷�����������ø÷�����õ�һ��{@link UnsupportedOperationException}�����쳣��*/
    public void refresh() throws IOException {
        //TODO
    }
}