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
package net.hasor.core.context.init;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.hasor.core.InitContext;
import net.hasor.core.Settings;
import net.hasor.core.setting.InitContextSettings;
/**
 * {@link InitContext}�ӿ�ʵ���ࡣ
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public class StandardInitContext extends DefaultInitContext {
    public StandardInitContext() throws IOException {
        super();
    }
    public StandardInitContext(URI mainSettings) throws IOException {
        super(mainSettings);
    }
    public StandardInitContext(URI mainSettings, Object context) throws IOException {
        super(mainSettings, context);
    }
    public StandardInitContext(File mainSettings) throws IOException {
        super((mainSettings == null) ? null : mainSettings.toURI());
    }
    public StandardInitContext(File mainSettings, Object context) throws IOException {
        super((mainSettings == null) ? null : mainSettings.toURI(), context);
    }
    //---------------------------------------------------------------------------------Basic Method
    protected Settings createSettings(URI settingURI) throws IOException {
        InitContextSettings settings = null;
        if (settingURI == null)
            settings = new InitContextSettings();
        else
            settings = new InitContextSettings(settingURI);
        this.settingURI = settings.getSettingURI();
        return settings;
    }
}