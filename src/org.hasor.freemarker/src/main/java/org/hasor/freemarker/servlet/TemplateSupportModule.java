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
package org.hasor.freemarker.servlet;
import org.hasor.context.ModuleSettings;
import org.hasor.context.anno.Module;
import org.hasor.freemarker.support.FreemarkerSupportModule;
import org.hasor.servlet.AbstractWebHasorModule;
import org.hasor.servlet.WebApiBinder;
import org.hasor.servlet.anno.support.ServletAnnoSupportModule;
/**
 * ģ����� 
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@Module(displayName = "TemplatePlatformListener", description = "org.hasor.view.template���������֧�֡�")
public class TemplateSupportModule extends AbstractWebHasorModule {
    public void configuration(ModuleSettings info) {
        info.beforeMe(ServletAnnoSupportModule.class);//��hasor-servlet����֮ǰ
        info.beforeMe(FreemarkerSupportModule.class);//��FreemarkerSupportModule����֮ǰ
        //freemarker�������
        try {
            Thread.currentThread().getContextClassLoader().loadClass("freemarker.template.Configuration");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**��ʼ��.*/
    public void init(WebApiBinder apiBinder) {
        TempSettings tempSettings = new TempSettings();
        tempSettings.onLoadConfig(apiBinder.getInitContext().getSettings());
        apiBinder.getGuiceBinder().bind(TempSettings.class).toInstance(tempSettings);
        //
        if (tempSettings.isEnable() == false)
            return;
        //
        String[] suffix = tempSettings.getSuffix();
        if (suffix != null)
            for (String suf : suffix)
                apiBinder.serve(suf).with(FmHttpServlet.class);
    }
}