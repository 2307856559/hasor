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
package net.hasor.mvc.resource.support;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.hasor.Hasor;
import net.hasor.context.ModuleSettings;
import net.hasor.context.anno.DefineModule;
import net.hasor.mvc.resource.ResourceLoaderCreator;
import net.hasor.mvc.resource.ResourceLoaderDefine;
import net.hasor.servlet.AbstractWebHasorModule;
import net.hasor.servlet.WebApiBinder;
import net.hasor.servlet.anno.support.ServletAnnoSupportModule;
/**
 * ����װ��jar���е���Դ����������Lv_1
 * @version : 2013-4-8
 * @author ������ (zyc@hasor.net)
 */
@DefineModule(description = "org.hasor.web.resource���������֧�֡�")
public class ServletResourceSupportModule extends AbstractWebHasorModule {
    public void configuration(ModuleSettings info) {
        info.beforeMe(ServletAnnoSupportModule.class);
    }
    public void init(WebApiBinder apiBinder) {
        /*��Settings�����ǲ�֧�����ظ���*/
        ResourceSettings settings = new ResourceSettings();
        settings.onLoadConfig(apiBinder.getInitContext().getSettings());
        //
        if (settings.isEnable() == false)
            return;
        //
        apiBinder.getGuiceBinder().bind(ResourceSettings.class).toInstance(settings);
        //
        this.loadResourceLoader(apiBinder);
        apiBinder.filter("*").through(ResourceLoaderFilter.class);
    }
    //
    /**װ��TemplateLoader*/
    protected void loadResourceLoader(WebApiBinder event) {
        //1.��ȡ
        Set<Class<?>> resourceLoaderCreatorSet = event.getClassSet(ResourceLoaderDefine.class);
        if (resourceLoaderCreatorSet == null)
            return;
        List<Class<ResourceLoaderCreator>> resourceLoaderCreatorList = new ArrayList<Class<ResourceLoaderCreator>>();
        for (Class<?> cls : resourceLoaderCreatorSet) {
            if (ResourceLoaderCreator.class.isAssignableFrom(cls) == false) {
                Hasor.warning("loadResourceLoader : not implemented ResourceLoaderCreator. class=%s", cls);
            } else {
                resourceLoaderCreatorList.add((Class<ResourceLoaderCreator>) cls);
            }
        }
        //3.ע�����
        ResourceBinderImplements loaderBinder = new ResourceBinderImplements();
        for (Class<ResourceLoaderCreator> creatorType : resourceLoaderCreatorList) {
            ResourceLoaderDefine creatorAnno = creatorType.getAnnotation(ResourceLoaderDefine.class);
            String defineName = creatorAnno.configElement();
            loaderBinder.bindLoaderCreator(defineName, creatorType);
            Hasor.info("loadResourceLoader %s at %s.", defineName, creatorType);
        }
        loaderBinder.configure(event.getGuiceBinder());
    }
}