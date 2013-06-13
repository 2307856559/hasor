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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.more.util.io.AutoCloseInputStream;
import org.more.util.map.DecSequenceMap;
import org.more.util.map.Properties;
/**
 * classpath������
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public abstract class ResourcesUtils {
    /** �����¼� */
    public static class ScanEvent {
        private String      name    = null;
        private boolean     isRead  = false; //�Ƿ�ɶ���
        private boolean     isWrite = false; //�Ƿ��д
        //
        private InputStream stream  = null;
        private File        file    = null;
        //
        /**����{@link ScanEvent}*/
        ScanEvent(String name, File file) {
            this.isRead = file.canRead();
            this.isWrite = file.canWrite();
            this.file = file;
            this.name = name;
        }
        /**����{@link ScanEvent}*/
        ScanEvent(String name, JarEntry entry, InputStream stream) {
            this.isRead = !entry.isDirectory();
            this.isWrite = false;
            this.stream = stream;
            this.name = name;
        }
        //----------------------------------
        public String getName() {
            return name;
        }
        public boolean isRead() {
            return this.isRead;
        }
        public boolean isWrite() {
            return this.isWrite;
        }
        public InputStream getStream() throws FileNotFoundException {
            if (this.stream != null)
                return this.stream;
            if (this.file != null && this.isRead == true)
                return new FileInputStream(this.file);
            return null;
        }
    }
    /**ɨ��classpathʱ�ҵ���Դ�Ļص��ӿڷ�����*/
    public static interface ScanItem {
        /**
         * �ҵ���Դ(����ֵΪtrue��ʾ�ҵ�Ԥ�ڵ���Դ����ɨ�裬false��ʾ����ɨ��ʣ�µ���Դ)
         * @param event �ҵ���Դ�¼���
         * @param isInJar �ҵ�����Դ�Ƿ���jar�ļ��
         */
        public boolean goFind(ScanEvent event, boolean isInJar);
    };
    /*------------------------------------------------------------------------------*/
    private static String formatResource(String resourcePath) {
        if (resourcePath.length() > 1)
            if (resourcePath.charAt(0) == '/')
                resourcePath = resourcePath.substring(1);
        return resourcePath;
    }
    private static ClassLoader getCurrentLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    /**�ϳ����������ļ���������Ϣ��һ��{@link Map}�ӿ��С�*/
    public static Map<String, String> getPropertys(String[] resourcePaths) throws IOException {
        return getPropertys(Arrays.asList(resourcePaths).iterator());
    }
    /**�ϳ����������ļ���������Ϣ��һ��{@link Map}�ӿ��С�*/
    public static Map<String, String> getPropertys(Iterator<String> iterator) throws IOException {
        if (iterator == null)
            return null;
        //
        DecSequenceMap<String, String> iatt = new DecSequenceMap<String, String>();
        while (iterator.hasNext() == true) {
            String str = iterator.next();
            Map<String, String> att = getPropertys(str);
            if (att != null)
                iatt.addMap(att);
        }
        return iatt;
    }
    /**��ȡһ�������ļ���������{@link Map}�ӿڵ���ʽ���ء�*/
    public static Map<String, String> getPropertys(String resourcePath) throws IOException {
        Properties prop = new Properties();
        InputStream in = getResourceAsStream(formatResource(resourcePath));
        if (in != null)
            prop.load(in);
        return prop;
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ��*/
    public static URL getResource(String resourcePath) throws IOException {
        resourcePath = formatResource(resourcePath);
        URL url = getCurrentLoader().getResource(resourcePath);
        return url;
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ�б�*/
    public static List<URL> getResources(String resourcePath) throws IOException {
        resourcePath = formatResource(resourcePath);
        //
        ArrayList<URL> urls = new ArrayList<URL>();
        Enumeration<URL> eurls = getCurrentLoader().getResources(resourcePath);
        while (eurls.hasMoreElements() == true) {
            URL url = eurls.nextElement();
            urls.add(url);
        }
        return urls;
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ����������ʽ���ء�*/
    public static InputStream getResourceAsStream(File resourceFile) throws IOException {
        return getResourceAsStream(resourceFile.toURI().toURL());
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ����������ʽ���ء�*/
    public static InputStream getResourceAsStream(URI resourceURI) throws IOException {
        return getResourceAsStream(resourceURI.toURL());
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ����������ʽ���ء�*/
    public static InputStream getResourceAsStream(URL resourceURL) throws IOException {
        String protocol = resourceURL.getProtocol();
        File path = new File(URLDecoder.decode(resourceURL.getFile(), "utf-8"));
        if (protocol.equals("file") == true) {
            //�ļ�
            if (path.canRead() == true && path.isFile() == true)
                return new AutoCloseInputStream(new FileInputStream(path));
        } else if (protocol.equals("jar") == true) {
            //JAR�ļ�
            JarFile jar = ((JarURLConnection) resourceURL.openConnection()).getJarFile();
            String jarFile = jar.getName().replace("\\", "/");
            String resourcePath = URLDecoder.decode(resourceURL.getPath(), "utf-8");
            int beginIndex = resourcePath.indexOf(jarFile) + jarFile.length();
            String entPath = resourcePath.substring(beginIndex + 2);
            ZipEntry e = jar.getEntry(entPath);
            return jar.getInputStream(e);
        } else if (protocol.equals("classpath") == true) {
            String resourcePath = formatResource(resourceURL.getPath());
            return getCurrentLoader().getResourceAsStream(resourcePath);
        }
        // TODO �ô���������Э�����Դ���ء�����OSGi��Э�顣
        return null;
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ����������ʽ���ء�*/
    public static InputStream getResourceAsStream(String resourcePath) throws IOException {
        resourcePath = formatResource(resourcePath);
        return getCurrentLoader().getResourceAsStream(resourcePath);
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ�б���������ʽ���ء�*/
    public static List<InputStream> getResourcesAsStream(String resourcePath) throws IOException {
        ArrayList<InputStream> iss = new ArrayList<InputStream>();
        List<URL> urls = getResources(resourcePath);//�Ѿ����ù���formatResource(resourcePath);
        for (URL url : urls) {
            InputStream in = getResourceAsStream(url);//�Ѿ����ù���formatResource(resourcePath);
            if (in != null)
                iss.add(in);
        }
        return iss;
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
    /*------------------------------------------------------------------------------*/
    /**��ĳһ��Ŀ¼ִ��ɨ�衣*/
    private static boolean scanDir(File dirFile, String wild, ScanItem item, File contextDir) {
        String contextPath = contextDir.getAbsolutePath().replace("\\", "/");
        //1.��������ľ���һ���ļ���
        if (dirFile.isDirectory() == false) {
            //1)ȥ��������Ŀ¼
            String dirPath = dirFile.getAbsolutePath().replace("\\", "/");
            if (dirPath.startsWith(contextPath) == true)
                dirPath = dirPath.substring(contextPath.length(), dirPath.length());
            //2)�������
            if (StringUtils.matchWild(wild, dirPath) == false)
                return false;
            //3)ִ�з���
            return item.goFind(new ScanEvent(dirPath, dirFile), false);
        }
        //----------
        for (File f : dirFile.listFiles()) {
            //1)ȥ��������Ŀ¼
            String dirPath = f.getAbsolutePath().replace("\\", "/");
            if (dirPath.startsWith(contextPath) == true)
                dirPath = dirPath.substring(contextPath.length() + 1, dirPath.length());
            //3)ִ�з���
            if (f.isDirectory() == true) {
                //ɨ���ļ����е�����
                if (scanDir(f, wild, item, contextDir) == true)
                    return true;
            }
            //2)�������
            if (StringUtils.matchWild(wild, dirPath) == false)
                continue;
            if (item.goFind(new ScanEvent(dirPath, f), false) == true)
                return true;
        }
        return false;
    }
    /**��ĳһ��jar�ļ�ִ��ɨ�衣*/
    public static boolean scanJar(JarFile jarFile, String wild, ScanItem item) throws IOException {
        final Enumeration<JarEntry> jes = jarFile.entries();
        while (jes.hasMoreElements() == true) {
            JarEntry e = jes.nextElement();
            String name = e.getName();
            if (StringUtils.matchWild(wild, name) == true)
                if (e.isDirectory() == false)
                    if (item.goFind(new ScanEvent(name, e, jarFile.getInputStream(e)), true) == true)
                        return true;
        }
        return false;
    }
    /**
     * ɨ��classpathĿ¼�е���Դ��ÿ������һ����Դʱ����������{@link ScanItem}�ӿڵ�һ�ε��á���ע���׸��ַ���������ͨ�����
     * �����Դ�Ǵ�����jar���е���ô�ڻ�ȡ�Ķ���������ʱҪ�ڻص��д�����ϡ�
     * ��ɨ���ڼ��������ʱ�˳�ɨ����{@link ScanItem}�ӿڵķ���ֵ��true���ɡ�
     * @param wild ɨ���ڼ�Ҫ�ų���Դ��ͨ�����
     * @param item ���ҵ���Դʱִ�лص��Ľӿڡ�
     */
    public static void scan(String wild, ScanItem item) throws IOException, URISyntaxException {
        if (wild == null || wild.equals("") == true)
            return;
        char firstChar = wild.charAt(0);
        if (firstChar == '?' || firstChar == '*')
            throw new IllegalArgumentException("classpath��ɨ�費֧���׸���ĸΪͨ����ַ���");
        //ȷ��λ��
        int index1 = wild.indexOf('?');
        int index2 = wild.indexOf('*');
        index1 = (index1 == -1) ? index1 = wild.length() : index1;
        index2 = (index2 == -1) ? index2 = wild.length() : index2;
        int index = (index1 > index2) ? index2 : index1;
        //
        String _wild = wild.substring(0, index);
        if (_wild.charAt(_wild.length() - 1) == '/')
            _wild = _wild.substring(0, _wild.length() - 1);
        Enumeration<URL> urls = getCurrentLoader().getResources(_wild);
        List<URL> dirs = rootDir();
        //
        while (urls.hasMoreElements() == true) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            boolean res = false;
            if (protocol.equals("file") == true) {
                File f = new File(url.toURI());
                res = scanDir(f, wild, item, new File(has(dirs, url).toURI()));
            } else if (protocol.equals("jar") == true) {
                JarURLConnection urlc = (JarURLConnection) url.openConnection();
                res = scanJar(urlc.getJarFile(), wild, item);
            }
            if (res == true)
                return;
        }
    };
    private static URL has(List<URL> dirs, URL one) {
        for (URL u : dirs)
            if (one.toString().startsWith(u.toString()) == true)
                return u;
        return null;
    }
    private static List<URL> rootDir() throws IOException {
        Enumeration<URL> roote = getCurrentLoader().getResources("");
        ArrayList<URL> rootList = new ArrayList<URL>();
        while (roote.hasMoreElements() == true)
            rootList.add(roote.nextElement());
        return rootList;
    };
    /*------------------------------------------------------------------------------*/
}