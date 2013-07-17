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
package org.hasor.web.resource.loader;
import java.io.InputStream;
import org.hasor.web.resource.ResourceLoader;
import org.more.util.StringUtils;
/**
 * ���ڴ���һ�����Դ�classpath�л�ȡ��Դ��ResourceLoader��
 * @version : 2013-6-6
 * @author ������ (zyc@byshell.org)
 */
public class ClassPathResourceLoader implements ResourceLoader {
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
    public InputStream getResourceAsStream(String name) {
        if (StringUtils.isBlank(name))
            return null;
        String $name = this.packageName + (name.charAt(0) == '/' ? name : "/" + name);
        $name = $name.replaceAll("/{2}", "/");
        if ($name.charAt(0) == '/')
            $name = $name.substring(1);
        return this.classLoader.getResourceAsStream($name);
    }
}