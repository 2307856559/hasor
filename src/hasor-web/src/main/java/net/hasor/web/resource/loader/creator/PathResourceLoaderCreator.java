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
package net.hasor.web.resource.loader.creator;
import java.io.File;
import java.io.IOException;
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.core.XmlNode;
import net.hasor.web.resource.ResourceLoader;
import net.hasor.web.resource.ResourceLoaderCreator;
import net.hasor.web.resource.ResourceLoaderDefine;
import net.hasor.web.resource.loader.PathResourceLoader;
import org.more.util.StringUtils;
/**
 * ���ڴ���һ�����Դ�classpath�л�ȡ��Դ��ResourceLoader��
 * @version : 2013-6-6
 * @author ������ (zyc@hasor.net)
 */
@ResourceLoaderDefine(configElement = "PathLoader")
public class PathResourceLoaderCreator implements ResourceLoaderCreator {
    public ResourceLoader newInstance(AppContext appContext, XmlNode xmlConfig) throws IOException {
        String body = xmlConfig.getText();
        body = StringUtils.isBlank(body) ? "" : body;
        body = appContext.getEnvironment().evalString(body);
        File fileBody = new File(body);
        if (fileBody.exists() == false)
            if (fileBody.mkdirs() == false)
                return null;
        Hasor.info("loadPath %s -> %s", xmlConfig.getText(), fileBody);
        PathResourceLoader dirTemplateLoader = new PathResourceLoader(fileBody.getAbsolutePath());
        return dirTemplateLoader;
    }
}