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
package net.hasor.core.context;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import net.hasor.core.AppContext;
import net.hasor.core.Environment;
import net.hasor.core.Hasor;
import net.hasor.core.environment.StandardEnvironment;
import org.more.util.ResourcesUtils;
/**
 * {@link AppContext}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public abstract class AbstractConfigResourceAppContext extends AbstractAppContext {
    public static final String DefaultSettings = "hasor-config.xml";
    private URI                mainSettings    = null;
    //
    /**�����������ļ�*/
    protected AbstractConfigResourceAppContext() throws IOException, URISyntaxException {
        this(DefaultSettings);
    }
    /**�����������ļ�*/
    protected AbstractConfigResourceAppContext(File mainSettings) {
        this.mainSettings = mainSettings.toURI();
    }
    /**�����������ļ�*/
    protected AbstractConfigResourceAppContext(URI mainSettings) {
        this.mainSettings = mainSettings;
    }
    /**�����������ļ�*/
    protected AbstractConfigResourceAppContext(String mainSettings) throws IOException, URISyntaxException {
        URL resURL = ResourcesUtils.getResource(mainSettings);
        if (resURL == null)
            Hasor.logWarn("can't find %s.", mainSettings);
        else
            this.mainSettings = resURL.toURI();
    }
    /**��ȡ���õ��������ļ�*/
    public final URI getMainSettings() {
        return mainSettings;
    }
    //
    protected Environment createEnvironment() {
        return new StandardEnvironment(this.mainSettings);
    }
}