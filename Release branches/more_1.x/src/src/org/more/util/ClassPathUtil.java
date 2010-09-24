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
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * classpath������
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
public class ClassPathUtil {
    private static String[]   CLASS_PATH_PROP  = { "java.class.path", "java.ext.dirs", "sun.boot.class.path" };
    private static List<File> CLASS_PATH_ARRAY = getClassPath();
    private static List<File> getClassPath() {
        List<File> ret = new ArrayList<File>();
        String delim = ":";
        if (System.getProperty("os.name").indexOf("Windows") != -1)
            delim = ";";
        for (String pro : CLASS_PATH_PROP) {
            String[] pathes = System.getProperty(pro).split(delim);
            for (String path : pathes)
                ret.add(new File(path));
        }
        return ret;
    }
    /**���classpath������JAR�ļ�*/
    public static List<File> getJars() {
        LinkedList<File> jars = new LinkedList<File>();
        for (File classPath : CLASS_PATH_ARRAY)
            if (classPath.isFile() == true)
                jars.add(classPath);
        return jars;
    }
    /**��ȡclasspath�п��ܴ��ڵ���Դ�б���������ʽ���ء�*/
    public static List<InputStream> getResource(String resourcePath) throws IOException {
        LinkedList<InputStream> ins = new LinkedList<InputStream>();
        for (File classPath : CLASS_PATH_ARRAY) {
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
}