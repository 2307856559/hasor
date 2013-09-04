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
package net.hasor.freemarker;
import net.hasor.context.AppContext;
import net.hasor.freemarker.loader.ConfigTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
/**
 * 
 * @version : 2013-5-16
 * @author ������ (zyc@byshell.org)
 */
public interface ConfigurationFactory {
    /*** ��ȡ���úõ�freemarker{@link Configuration}����*/
    public Configuration configuration(AppContext appContext);
    /***/
    public TemplateLoader createTemplateLoader(AppContext appContext);
    //
    public ConfigTemplateLoader createConfigTemplateLoader(AppContext appContext);
}