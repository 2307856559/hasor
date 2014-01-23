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
package net.test.project.common.plugins.freemarker.loader.resource;
import java.io.InputStream;
import java.net.URL;
import net.test.project.common.plugins.freemarker.loader.IResourceLoader;
/**
 * {@link ClassPathResourceLoader}��ʹ��һ��classpath·����Ϊ���·����
 * ������������һ����Ϊ��org.more.res������Դ����
 * {@link #getResourceAsStream(String)}��������Ϊ��/abc/aa/htm����
 * ��ô�����Դ��ʵ�ʵ�ַ��λ��classpath�µġ�org/more/res/abc/aa/htm����
 * @version : 2011-9-14
 * @author ������ (zyc@byshell.org) 
 */
public class ClassPathResourceLoader implements IResourceLoader {
    private String      packageName = null;
    private ClassLoader classLoader = null;
    /***/
    public ClassPathResourceLoader(String packageName, ClassLoader classLoader) {
        this.packageName = packageName;
        this.classLoader = classLoader;
    }
    /**��ȡ��Դ��ȡ�İ�·����*/
    public String getPackageName() {
        return this.packageName;
    }
    /**��ȡװ����Դʹ�õ���װ������*/
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    public URL getResource(String name) {
        String $name = this.packageName + "/" + name;
        if ($name.charAt(0) == '/')
            $name = $name.substring(1);
        $name = $name.replaceAll("/{2}", "/");
        return this.classLoader.getResource($name);
    }
    public InputStream getResourceAsStream(String name) {
        String $name = this.packageName + "/" + name;
        if ($name.charAt(0) == '/')
            $name = $name.substring(1);
        $name = $name.replaceAll("/{2}", "/");
        return this.classLoader.getResourceAsStream($name);
    }
}