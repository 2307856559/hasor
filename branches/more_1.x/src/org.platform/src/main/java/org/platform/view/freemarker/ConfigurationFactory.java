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
package org.platform.view.freemarker;
import org.more.webui.freemarker.loader.ConfigTemplateLoader;
import org.platform.context.AppContext;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
/**
 * 
 * @version : 2013-5-16
 * @author ������ (zyc@byshell.org)
 */
public interface ConfigurationFactory {
    /**�Ƿ�����freemarker.*/
    public static final String FreemarkerConfig_Enable               = "freemarker.enable";
    /**freemarkerģ���׺�����ż�����.*/
    public static final String FreemarkerConfig_Suffix               = "freemarker.suffixSet";
    /**��ģ�崦�����쳣ʱ�Ĵ���ʽ.*/
    public static final String FreemarkerConfig_OnError              = "freemarker.onError";
    /**Configuration���󴴽�����.*/
    public static final String FreemarkerConfig_ConfigurationFactory = "freemarker.configurationFactory";
    /**FreeMarker����.*/
    public static final String FreemarkerConfig_Settings             = "freemarker.settings";
    /**FreeMarkerװ��������.*/
    public static final String FreemarkerConfig_TemplateLoader       = "freemarker.templateLoader";
    //
    /*** ��ȡ���úõ�freemarker{@link Configuration}����*/
    public Configuration configuration(AppContext appContext);
    /***/
    public TemplateLoader createTemplateLoader(AppContext appContext);
    //
    public ConfigTemplateLoader createConfigTemplateLoader(AppContext appContext);
}