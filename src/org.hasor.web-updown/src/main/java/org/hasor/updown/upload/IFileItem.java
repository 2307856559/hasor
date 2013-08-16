/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package org.hasor.updown.upload;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
/**
 * �ϴ�����Ŀ��
 * @version : 2013-3-12
 * @author ������ (zyc@byshell.org)
 */
public interface IFileItem {
    /**�ϴ����ļ�����*/
    public String getFileName();
    /**��ȡ�ϴ��ı�����*/
    public String getFieldName();
    /**�ϴ��������Ƿ�Ϊһ������Ŀ��*/
    public boolean isFormField();
    /**��ȡ�ϴ����ݵ���������*/
    public InputStream getInputStream() throws IOException;
    /**��ȡ�ϴ����ݵ��������*/
    public OutputStream getOutputStream() throws IOException;
    /**��ȡ�ϴ����ݵ�ContentType��*/
    public String getContentType();
    /**�ϴ������Ƿ�������ڴ档*/
    public boolean isInMemory();
    /**�ϴ�����Ŀ��С��*/
    public long getSize();
    /**���ֽڷ�ʽ�����ϴ����������ݡ�*/
    public byte[] get();
    /**����String��ʽ���ϴ������ݣ�������ʾʹ�õ��ַ����롣*/
    public String getString(String encoding) throws UnsupportedEncodingException;
    /**����String��ʽ���ϴ������ݡ�*/
    public String getString();
    /**���ϴ�����д�뵽һ���ļ������ϣ��ڶ���������ʾ�������ļ�����ʱ�Ƿ�������д��*/
    public boolean write(File file, boolean overwrite) throws IOException;
    /**ɾ���ϴ��ļ������ݡ�*/
    public void delete();
}