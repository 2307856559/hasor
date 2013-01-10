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
package org.more.appengine;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
/**
* 
* @version : 2013-1-7
* @author ������ (zyc@byshell.org)
*/
public class WorkSpace {
    private Settings settings = null;
    /**����{@link WorkSpace}���Ͷ���*/
    protected WorkSpace(Settings settings) {
        if (settings == null)
            throw new NullPointerException();
        this.settings = settings;
    }
    /**��ȡ{@link Settings}ȫ�����ö���*/
    protected Settings getSettings() {
        return this.settings;
    }
    /**��ȡ������Ŀ¼������·������*/
    public String getWorkDir() {
        return this.getSettings().getDirectoryPath("workspace.workDir");
    };
    /**��ȡ�����ļ�Ŀ¼������·������*/
    public String getDataDir() {
        String workDir = getWorkDir();
        String dataDir = this.getSettings().getDirectoryPath("workspace.dataDir");
        boolean absolute = this.getSettings().getBoolean("workspace.dataDir.absolute");
        if (absolute == false)
            return str2path(new File(workDir, dataDir).getAbsolutePath());
        else
            return str2path(new File(dataDir).getAbsolutePath());
    };
    /**��ȡ��ʱ�����ļ�Ŀ¼��*/
    public String getTempDir() {
        String workDir = getWorkDir();
        String tempDir = this.getSettings().getDirectoryPath("workspace.tempDir");
        boolean absolute = this.getSettings().getBoolean("workspace.tempDir.absolute");
        if (absolute == false)
            return str2path(new File(workDir, tempDir).getAbsolutePath());
        else
            return str2path(new File(tempDir).getAbsolutePath());
    };
    /**��ȡ����Ŀ¼��*/
    public String getCacheDir() {
        String workDir = getWorkDir();
        String cacheDir = this.getSettings().getDirectoryPath("workspace.cacheDir");
        boolean absolute = this.getSettings().getBoolean("workspace.cacheDir.absolute");
        if (absolute == false)
            return str2path(new File(workDir, cacheDir).getAbsolutePath());
        else
            return str2path(new File(cacheDir).getAbsolutePath());
    };
    /**��ȡ�����ļ�Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getDataDir(String name) {
        if (StringUtils.isBlank(name) == true)
            return getDataDir();
        else
            return str2path(new File(getDataDir(), name).getAbsolutePath());
    };
    /**��ȡ��ʱ�����ļ�Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getTempDir(String name) {
        if (StringUtils.isBlank(name) == true)
            return getTempDir();
        else
            return str2path(new File(getTempDir(), name).getAbsolutePath());
    };
    /**��ȡ����Ŀ¼���Զ���name������ӵ�����ֵ�С�*/
    public String getCacheDir(String name) {
        if (StringUtils.isBlank(name) == true)
            return getCacheDir();
        else
            return str2path(new File(getCacheDir(), name).getAbsolutePath());
    };
    //-----------------------------------------------------------------
    private File tempFileDirectory = null;
    /**����ʱĿ¼�´���һ������������ʱ�ļ����أ�����ʱ�ļ���������������˳�֮����ͬ������Ŀ¼һͬɾ����*/
    public File createTempFile() throws IOException {
        if (this.tempFileDirectory == null) {
            this.tempFileDirectory = new File(this.getTempDir("tempFile"));
            this.tempFileDirectory.deleteOnExit();
        }
        long markTime = System.currentTimeMillis();
        String atPath = long2path(markTime, 2000);
        String fileName = "work_" + String.valueOf(markTime) + ".tmp";
        File tmpFile = new File(new File(this.tempFileDirectory, atPath), fileName);
        tmpFile.createNewFile();
        return tmpFile;
    };
    private static String str2path(String oriPath) {
        int length = oriPath.length();
        if (oriPath.charAt(length - 1) == File.separatorChar)
            return oriPath;
        else
            return oriPath + File.separatorChar;
    };
    private static String long2path(long a, int size) {
        StringBuffer buffer = new StringBuffer();
        long b = size;
        long c = a;
        do {
            long m = a % b;
            c = a / b;
            a = c;
            buffer.append(m + File.separator);
        } while (c > 0);
        return buffer.toString();
    };
}