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
package org.more.hypha.beans.define;
/**
 * ��ʾһ����һ���ļ����ļ����������ݣ���Ӧ��PropertyMetaTypeEnum����Ϊ{@link PropertyMetaTypeEnum#File}��
 * @version 2010-9-17
 * @author ������ (zyc@byshell.org)
 */
public class File_ValueMetaData extends AbstractValueMetaData {
    private String  fileObject = null; //��ʾ�ļ���Ŀ¼���ַ�����
    private boolean isDir      = false; //��ʾ�Ƿ���һ��Ŀ¼
    /**�÷������᷵��{@link PropertyMetaTypeEnum#File}��*/
    public String getMetaDataType() {
        return PropertyMetaTypeEnum.File;
    }
    /**��ȡ��ʾ�ļ���Ŀ¼���ַ���*/
    public String getFileObject() {
        return this.fileObject;
    }
    /**���ñ�ʾ�ļ���Ŀ¼���ַ���*/
    public void setFileObject(String fileObject) {
        this.fileObject = fileObject;
    }
    /**��ȡһ��ֵ��ֵ��ʾfileObject�Ƿ���һ��Ŀ¼��*/
    public boolean isDir() {
        return this.isDir;
    }
    /**����һ��ֵ��ֵ��ʾfileObject�Ƿ���һ��Ŀ¼��*/
    public void setDir(boolean isDir) {
        this.isDir = isDir;
    }
}