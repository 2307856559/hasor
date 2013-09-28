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
package net.hasor.web.resource.loader;
import java.io.IOException;
import java.io.InputStream;
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.core.XmlNode;
import net.hasor.web.resource.ResourceLoader;
import net.hasor.web.resource.ResourceLoaderCreator;
import net.hasor.web.resource.ResourceLoaderDefine;
import org.more.util.StringUtils;
/**
 * ���ڴ���һ�����Դ�classpath�л�ȡ��Դ��ResourceLoader��
 * @version : 2013-6-6
 * @author ������ (zyc@hasor.net)
 */
@ResourceLoaderDefine(configElement = "ClasspathLoader")
public class ClassPathResourceLoaderCreator implements ResourceLoaderCreator {
    public ResourceLoader newInstance(AppContext appContext, XmlNode xmlConfig) throws IOException {
        String config = xmlConfig.getText();
        config = StringUtils.isBlank(config) ? "/" : config;
        Hasor.info("loadClassPath %s", config);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ClassPathResourceLoader classpathLoader = new ClassPathResourceLoader(config, loader);
        return classpathLoader;
    }
}
/**
 * ���ڴ���һ�����Դ�classpath�л�ȡ��Դ��ResourceLoader��
 * @version : 2013-6-6
 * @author ������ (zyc@hasor.net)
 */
class ClassPathResourceLoader implements ResourceLoader {
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