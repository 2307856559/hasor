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
package net.test.project.common.plugins.freemarker.function;
import javax.inject.Inject;
import net.hasor.core.AppContext;
import net.test.project.common.plugins.freemarker.FmMethod;
import net.test.project.common.plugins.freemarker.FreemarkerService;
/**
 * Freemarker ģ����ͨ�ú���
 * @version : 2013-9-24
 * @author ������(zyc@hasor.net)
 */
public class Functions {
    @Inject
    private AppContext        appContext = null;
    @Inject
    private FreemarkerService fmService  = null;
    /*-----------------------------------------------------------------*/
    //
    //
    /**��ȡ����·��*/
    @FmMethod("ctxPath")
    public String ctxPath() {
        return PlatformFilter.getLocalServletContext().getContextPath();
    };
    //
    //
    //    /**����ģ���ȡ��������*/
    //    @FmMethod("loadFtl")
    //    public String loadFtl(String templateName) {
    //        StringWriter sw = new StringWriter();
    //        return this.fmService.processTemplate(templateName, rootMap, writer);
    //    };
    //
    //
    /**��ȡ�ַ�������*/
    @FmMethod("str_settings")
    public String str_settings(String settingName, String defaultValue) {
        return this.appContext.getSettings().getString(settingName, defaultValue);
    };
    /**��ȡ��������*/
    @FmMethod("bool_settings")
    public Boolean bool_settings(String settingName, Boolean defaultValue) {
        return this.appContext.getSettings().getBoolean(settingName, defaultValue);
    };
    //
    //
}