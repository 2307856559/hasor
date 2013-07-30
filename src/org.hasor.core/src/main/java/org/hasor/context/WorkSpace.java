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
package org.hasor.context;
import java.io.File;
import java.io.IOException;
/**
 * Ӧ�ó������ռ�����
 * @version : 2013-5-23
 * @author ������ (zyc@byshell.org)
 */
public interface WorkSpace {
    /**��ȡ{@link Settings}�ӿڷ�����*/
    public Settings getSettings();
    /**��ȡ����Ŀ¼������·�������ÿ�����config.xml�ġ�<b>workspace.workDir</b>���ڵ������á�<br/>
     * <font color="00aa00"><b>��ʾ</b></font>���ýڵ����������֧�ֻ�������������*/
    public String getWorkDir();
    /**��ȡ�����ļ����Ŀ¼������·�������ÿ�����config.xml�ġ�<b>workspace.dataDir</b>���ڵ������á�<br/>
     * <font color="00aa00"><b>��ʾ</b></font>���ýڵ����������֧�ֻ�������������*/
    public String getDataDir();
    /**��ȡ��ʱ�ļ����Ŀ¼������·�������ÿ�����config.xml�ġ�<b>workspace.tempDir</b>���ڵ������á�<br/>
     * <font color="00aa00"><b>��ʾ</b></font>���ýڵ����������֧�ֻ�������������*/
    public String getTempDir();
    /**��ȡ�����ļ����Ŀ¼������·�������ÿ�����config.xml�ġ�<b>workspace.cacheDir</b>���ڵ������á�<br/>
     * <font color="00aa00"><b>��ʾ</b></font>���ýڵ����������֧�ֻ�������������*/
    public String getCacheDir();
    /**��ȡ�����ռ���ר�����ڴ��ģ��������Ϣ��Ŀ¼�ռ䣬���ÿ�����config.xml�ġ�<b>workspace.pluginDir</b>���ڵ������á�<br/>
     * <font color="00aa00"><b>��ʾ</b></font>���ýڵ����������֧�ֻ�������������*/
    public String getPluginDir();
    /**��ȡ�����ռ���ר�����ڴ����־��Ŀ¼�ռ䣬���ÿ�����config.xml�ġ�<b>workspace.logDir</b>���ڵ������á�<br/>
     * <font color="00aa00"><b>��ʾ</b></font>���ýڵ����������֧�ֻ�������������*/
    public String getLogDir();
    /**����{@link #getDataDir()}��·����Ϊ��·����������subPath��������ʾ������Ŀ¼��<br/>
     * <font color="00aa00"><b>��ʾ</b></font>��������֧�ְ�������������*/
    public String getDataDir(String subPath);
    /**����{@link #getTempDir()}��·����Ϊ��·����������subPath��������ʾ����ʱĿ¼��<br/>
     * <font color="00aa00"><b>��ʾ</b></font>��������֧�ְ�������������*/
    public String getTempDir(String subPath);
    /**����{@link #getCacheDir()}��·����Ϊ��·����������subPath��������ʾ�Ļ���Ŀ¼��<br/>
     * <font color="00aa00"><b>��ʾ</b></font>��������֧�ְ�������������*/
    public String getCacheDir(String subPath);
    /**����{@link #getPluginDir()}��·����Ϊ��·����������model��������ʾ��ģ��˽�пռ䡣<br/>
     * <font color="00aa00"><b>��ʾ</b></font>��������֧�ְ�������������*/
    public String getPluginDir(Class<?> hasorModule);
    /**����{@link #getLogDir()}��·����Ϊ��·����������subPath��������ʾ����־Ŀ¼��<br/>
     * <font color="00aa00"><b>��ʾ</b></font>��������֧�ְ�������������*/
    public String getLogDir(String subPath);
    /**����ʱĿ¼�´���һ������������ʱ�ļ����أ�����ʱ�ļ���������������˳�֮����ͬ������Ŀ¼һͬɾ����*/
    public File createTempFile() throws IOException;
    /**
    * ����·���㷨����һ��Path
    * @param target Ŀ��
    * @param dirSize ÿ��Ŀ¼�¿���ӵ�е���Ŀ¼���ļ���Ŀ��
    */
    public String genPath(long number, int size);
}