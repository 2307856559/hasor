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
import static org.platform.PlatformConfigEnum.Workspace_CacheDir;
import static org.platform.PlatformConfigEnum.Workspace_CacheDir_Absolute;
import static org.platform.PlatformConfigEnum.Workspace_DataDir;
import static org.platform.PlatformConfigEnum.Workspace_DataDir_Absolute;
import static org.platform.PlatformConfigEnum.Workspace_TempDir;
import static org.platform.PlatformConfigEnum.Workspace_TempDir_Absolute;
import static org.platform.PlatformConfigEnum.Workspace_WorkDir;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.more.core.global.Global;
import org.platform.Assert;
import com.google.inject.Injector;
/**
 * 
 * @version : 2013-3-26
 * @author ������ (zyc@byshell.org)
 */
public abstract class AppContext {
    private InitContext initContext = null;
    /**��ȡӦ�ó������á�*/
    public Global getSettings() {
        InitContext initContext = this.getInitContext();
        Assert.isNotNull(initContext, "AppContext.getInitContext() return is null.");
        return initContext.getConfig().getSettings();
    };
    /**��ȡ��ʼ��������*/
    public InitContext getInitContext() {
        if (this.initContext == null)
            this.initContext = this.getGuice().getInstance(InitContext.class);
        return this.initContext;
    }
    /**���Guice������*/
    public abstract Injector getGuice();
    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    public <T> T getBean(Class<T> beanType) {
        return this.getGuice().getInstance(beanType);
    };
    //    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    //    public abstract <T extends IService> T getService(String servicesName);
    //    /**ͨ�����ʹ�������ʵ����ʹ��guice*/
    //    public abstract <T extends IService> T getService(Class<T> servicesType);
    //    /**ͨ�����ƴ���beanʵ����ʹ��guice��*/
    //    public <T> T getBean(String name) {
    //        Class<T> classType = this.getBeanType(name);
    //        if (classType == null)
    //            return null;
    //        return this.getBean(classType);
    //    };
    //    /**ͨ������ȡBean�����͡�*/
    //    public abstract <T> Class<T> getBeanType(String name);
    //    /**��ȡ�Ѿ�ע���Bean���ơ�*/
    //    public abstract List<String> getBeanNames();
    /**��ȡ������Ŀ¼������·������*/
    public String getWorkDir() {
        return this.getSettings().getDirectoryPath(Workspace_WorkDir);
    };
    /**��ȡ�����ļ�Ŀ¼������·������*/
    public String getDataDir() {
        String workDir = getWorkDir();
        String dataDir = this.getSettings().getDirectoryPath(Workspace_DataDir);
        boolean absolute = this.getSettings().getBoolean(Workspace_DataDir_Absolute);
        if (absolute == false)
            return str2path(new File(workDir, dataDir).getAbsolutePath());
        else
            return str2path(new File(dataDir).getAbsolutePath());
    };
    /**��ȡ��ʱ�����ļ�Ŀ¼��*/
    public String getTempDir() {
        String workDir = getWorkDir();
        String tempDir = this.getSettings().getDirectoryPath(Workspace_TempDir);
        boolean absolute = this.getSettings().getBoolean(Workspace_TempDir_Absolute);
        if (absolute == false)
            return str2path(new File(workDir, tempDir).getAbsolutePath());
        else
            return str2path(new File(tempDir).getAbsolutePath());
    };
    /**��ȡ����Ŀ¼��*/
    public String getCacheDir() {
        String workDir = getWorkDir();
        String cacheDir = this.getSettings().getDirectoryPath(Workspace_CacheDir);
        boolean absolute = this.getSettings().getBoolean(Workspace_CacheDir_Absolute);
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
    /*----------------------------------------------------------------------*/
    /**����һ��UUID�ַ�����32���ַ������ȡ�*/
    public static String genIDBy32() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    /**����һ��UUID�ַ�����36���ַ������ȡ�*/
    public static String genIDBy36() {
        return UUID.randomUUID().toString();
    }
    /**
     * ����·���㷨��
     * @param number ����
     * @param size ÿ��Ŀ¼�¿���ӵ�е���Ŀ¼���ļ���Ŀ��
     */
    public static String genPath(long number, int size) {
        StringBuffer buffer = new StringBuffer();
        long b = size;
        long c = number;
        do {
            long m = number % b;
            buffer.append(m + File.separator);
            c = number / b;
            number = c;
        } while (c > 0);
        return buffer.reverse().toString();
    };
}