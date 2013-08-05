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
package org.hasor.mvc.resource.loader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.hasor.mvc.resource.ResourceLoader;
import org.more.util.io.AutoCloseInputStream;
/**
 * ��һ��File�����������·����Ϊ��·������Դ��ȡ����ڸ�·���¡�
 * @version : 2011-9-17
 * @author ������ (zyc@byshell.org)
 */
public class PathResourceLoader implements ResourceLoader {
    private String dirPath = null;
    public PathResourceLoader(String dirPath) {
        this.dirPath = dirPath;
    }
    @Override
    public InputStream getResourceAsStream(String resourcePath) {
        String $name = this.dirPath + "/" + resourcePath;
        $name = $name.replaceAll("/{2}", "/");
        File file = new File($name);
        try {
            if (file.exists() && file.isFile())
                return new AutoCloseInputStream(new FileInputStream(file));
        } catch (Exception e) {}
        return null;
    }
}