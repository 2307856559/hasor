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
package org.more.services.freemarker.loader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.more.services.freemarker.ResourceLoader;
import freemarker.cache.TemplateLoader;
/**
* {@link TemplateLoader}�ӿڵĴ����ࡣ
* @version : 2011-9-17
* @author ������ (zyc@byshell.org)
*/
public class PropxyTemplateLoader implements TemplateLoader, ResourceLoader {
    private TemplateLoader templateLoader = null;
    public PropxyTemplateLoader(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }
    public InputStream getResourceAsStream(String resourcePath) throws IOException {
        if (this.templateLoader instanceof ResourceLoader)
            return ((ResourceLoader) this.templateLoader).getResourceAsStream(resourcePath);
        return null;
    }
    public Object findTemplateSource(String name) throws IOException {
        return this.templateLoader.findTemplateSource(name);
    }
    public long getLastModified(Object templateSource) {
        return this.templateLoader.getLastModified(templateSource);
    }
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return this.getReader(templateSource, encoding);
    }
    public void closeTemplateSource(Object templateSource) throws IOException {
        this.closeTemplateSource(templateSource);
    }
}