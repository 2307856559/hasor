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
package org.more.global;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.util.ResourcesUtil;
/**
 * {@link Global}��������
 * @version : 2011-9-29
 * @author ������ (zyc@byshell.org)
 */
public abstract class GlobalFactory {
    /**Ĭ�ϱ���*/
    public final static String DefaultEncoding = "utf-8";
    /**����{@link Global}����*/
    protected abstract Map<String, Object> loadConfig(InputStream stream, String encoding) throws IOException;
    /*------------------------------------------------------------------------*/
    /**������Դ��������Global����ʹ�õ�Map��*/
    public Map<String, Object> createMap(String encoding, Object[] objects) throws IOException {
        //1.��������
        HashMap<String, Object> globalMap = new HashMap<String, Object>();
        if (encoding == null)
            encoding = DefaultEncoding;
        //2.����װ��
        for (Object obj : objects)
            if (obj instanceof File) {
                //�ļ�װ�أ�keyֵ���ļ�����key�൱�����Ե�������
                File fileObject = (File) obj;
                InputStream stream = ResourcesUtil.getResourceAsStream(fileObject);
                globalMap.putAll(this.loadConfig(stream, encoding));//�������
            } else if (obj instanceof URL) {
                //URLװ�أ�keyֵ��getFile����key�൱�����Ե�������
                URL urlObject = (URL) obj;
                InputStream stream = ResourcesUtil.getResourceAsStream(urlObject);
                globalMap.putAll(this.loadConfig(stream, encoding));//�������
            } else if (obj instanceof URI) {
                //URIװ�أ�keyֵ��getPath����key�൱�����Ե�������
                URI uriObject = (URI) obj;
                InputStream stream = ResourcesUtil.getResourceAsStream(uriObject);
                globalMap.putAll(this.loadConfig(stream, encoding));//�������
            } else if (obj instanceof String) {
                //�ַ���װ��
                String stringObject = (String) obj;
                List<InputStream> streams = ResourcesUtil.getResourcesAsStream(stringObject);
                for (InputStream stream : streams)
                    globalMap.putAll(this.loadConfig(stream, encoding));//�������
            } else if (obj instanceof InputStream) {
                //�ַ���װ��
                InputStream streamObject = (InputStream) obj;
                globalMap.putAll(this.loadConfig(streamObject, encoding));//�������
            }
        return globalMap;
    }
    public Global createGlobal(String encoding, Object[] objects) throws IOException {
        Map<String, Object> dataMap = createMap(encoding, objects);
        return AbstractGlobal.newInterInstance(dataMap);
    };
}