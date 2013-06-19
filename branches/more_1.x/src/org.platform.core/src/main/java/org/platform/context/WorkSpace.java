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
package org.platform.context;
import java.io.File;
import java.io.IOException;
/**
 * ��ʾһ�������ռ�����
 * @version : 2013-5-23
 * @author ������ (zyc@byshell.org)
 */
public interface WorkSpace {
    /**��ȡ������Ŀ¼������·������*/
    public String getWorkDir();
    /**��ȡ�����ļ�Ŀ¼������·������*/
    public String getDataDir();
    /**��ȡ��ʱ�����ļ�Ŀ¼��*/
    public String getTempDir();
    /**��ȡ����Ŀ¼��*/
    public String getCacheDir();
    /**��ȡ�����ļ�Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getDataDir(String name);
    /**��ȡ��ʱ�����ļ�Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getTempDir(String name);
    /**��ȡ����Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getCacheDir(String name);
    /**����ʱĿ¼�´���һ������������ʱ�ļ����أ�����ʱ�ļ���������������˳�֮����ͬ������Ŀ¼һͬɾ����*/
    public File createTempFile() throws IOException;
    /**
    * ����·���㷨��
    * @param target Ŀ��
    * @param dirSize ÿ��Ŀ¼�¿���ӵ�е���Ŀ¼���ļ���Ŀ��
    */
    public String genPath(long number, int size);
    /**��ȡ����*/
    public Settings getSettings();
}