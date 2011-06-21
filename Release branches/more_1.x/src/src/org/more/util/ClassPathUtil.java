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
package org.more.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * classpath������
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public abstract class ClassPathUtil {
    /**ɨ��classpathʱ�ҵ���Դ�Ļص��ӿڷ�����*/
    public interface ScanItem {
        /**
         * �ҵ���Դ��
         * @param event �ҵ���Դ�¼���
         * @param isInJar �ҵ�����Դ�Ƿ���jar�ļ��
         * @param context ��Դ���������ģ�������jar�е���Դ�ò�����ʾ���������ĸ�jar��������Ŀ¼�е���Դ�ò�����ʾ�ľ��������Դ�ļ���
         */
        public boolean goFind(ScanEvent event, boolean isInJar, File context) throws Throwable;
    };
    /*------------------------------------------------------------------------------*/
    private static String[]    CLASS_PATH_PROP    = { "java.class.path", "java.ext.dirs", "sun.boot.class.path" };
    /**ClassPathĿ¼�б�*/
    public static List<File>   CLASS_PATH_Files   = null;
    /**ClassPathĿ¼�б�*/
    public static List<String> CLASS_PATH_Strings = null;
    /*------------------------------------------------------------------------------*/
    static {
        CLASS_PATH_Files = new ArrayList<File>();
        CLASS_PATH_Strings = new ArrayList<String>();
        String delim = ":";
        if (System.getProperty("os.name").indexOf("Windows") != -1)
            delim = ";";
        for (String pro : CLASS_PATH_PROP) {
            String[] pathes = System.getProperty(pro).split(delim);
            for (String path : pathes) {
                CLASS_PATH_Files.add(new File(path));
                CLASS_PATH_Strings.add(path);
            }
        }
    }
    /*------------------------------------------------------------------------------*/
    /**���classpath������JAR�ļ�*/
    public static List<File> getJars() {
        LinkedList<File> jars = new LinkedList<File>();
        for (File classPath : CLASS_PATH_Files)
            if (classPath.isFile() == true)
                jars.add(classPath);
        return jars;
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ�б���������ʽ���ء�*/
    public static List<InputStream> getResource(String resourcePath) throws IOException {
        LinkedList<InputStream> ins = new LinkedList<InputStream>();
        for (File classPath : CLASS_PATH_Files) {
            InputStream is = null;
            if (classPath.isFile() == true)
                //1.Jar�ļ�
                try {
                    FileInputStream fis = new FileInputStream(classPath);
                    JarInputStream jis = new JarInputStream(fis, false);
                    is = getResourceByZip(jis, resourcePath);
                } catch (Exception e) {}
            else {
                //2.Ŀ¼
                File res = new File(classPath, resourcePath);
                if (res.canRead() == true)
                    is = new FileInputStream(res);
            }
            if (is != null)
                ins.add(is);
        }
        return ins;
    }
    /**��ȡzip����ָ�����Ǹ���Դ���÷����������zip����ʼ����ȡ����ֻ���������λ�ü�������*/
    public static InputStream getResourceByZip(ZipInputStream zipIN, String resourcePath) throws IOException {
        ZipEntry e = null;
        while ((e = zipIN.getNextEntry()) != null) {
            if (e.getName().equals(resourcePath) == true)
                return zipIN;
            zipIN.closeEntry();
        }
        return null;
    }
    //-----------------------------------------------------------------------------
    /**��ĳһ��Ŀ¼ִ��ɨ�衣*/
    private static boolean scanDir(File dirFile, String wild, ScanItem item, File contextDir) throws Throwable {
        String contextPath = contextDir.getAbsolutePath().replace("\\", "/");
        //1.��������ľ���һ���ļ���
        if (dirFile.isDirectory() == false) {
            //1)ȥ��������Ŀ¼
            String dirPath = dirFile.getAbsolutePath().replace("\\", "/");
            if (dirPath.startsWith(contextPath) == true)
                dirPath = dirPath.substring(contextPath.length(), dirPath.length());
            //2)�������
            if (StringUtil.matchWild(wild, dirPath) == false)
                return false;
            //3)ִ�з���
            return item.goFind(new ScanEvent(dirPath, dirFile), false, dirFile);
        }
        //----------
        for (File f : dirFile.listFiles()) {
            //1)ȥ��������Ŀ¼
            String dirPath = f.getAbsolutePath().replace("\\", "/");
            if (dirPath.startsWith(contextPath) == true)
                dirPath = dirPath.substring(contextPath.length(), dirPath.length());
            //3)ִ�з���
            if (f.isDirectory() == true) {
                //ɨ���ļ����е�����
                if (scanDir(f, wild, item, contextDir) == true)
                    return true;
            }
            //2)�������
            if (StringUtil.matchWild(wild, dirPath) == false)
                continue;
            if (item.goFind(new ScanEvent(dirPath, f), false, f) == true)
                return true;
        }
        return false;
    }
    /**��ĳһ��jar�ļ�ִ��ɨ�衣*/
    public static boolean scanJar(File jarFile, String wild, ScanItem item) throws Throwable {
        FileInputStream fis = new FileInputStream(jarFile);
        JarInputStream jis = new JarInputStream(fis, false);
        JarEntry e = null;
        while ((e = jis.getNextJarEntry()) != null) {
            String name = "/" + e.getName();
            if (StringUtil.matchWild(wild, name) == true)
                if (e.isDirectory() == false)
                    if (item.goFind(new ScanEvent(name, e, jis), true, jarFile) == true) {
                        jis.close();
                        return true;
                    }
        }
        jis.closeEntry();
        jis.close();
        return false;
    }
    /**
     * ɨ��classpathĿ¼�е���Դ��ÿ������һ����Դʱ����������{@link ScanItem}�ӿڵ�һ�ε��á�
     * �����Դ�Ǵ�����jar���е���ô�ڻ�ȡ�Ķ���������ʱҪ�ڻص��д�����ϡ�
     * ��ɨ���ڼ��������ʱ�˳�ɨ����{@link ScanItem}�ӿڵķ���ֵ��true���ɡ�
     * @param wild ɨ���ڼ�Ҫ�ų���Դ��ͨ�����
     * @param item ���ҵ���Դʱִ�лص��Ľӿڡ�
     */
    public static void scan(String wild, ScanItem item) throws Throwable {
        for (File classPath : CLASS_PATH_Files) {
            boolean res = false;
            if (classPath.isFile() == true)
                res = scanJar(classPath, wild, item);
            else if (classPath.isDirectory() == true)
                res = scanDir(classPath, wild, item, classPath);
            if (res == true)
                return;
        }
    }
}