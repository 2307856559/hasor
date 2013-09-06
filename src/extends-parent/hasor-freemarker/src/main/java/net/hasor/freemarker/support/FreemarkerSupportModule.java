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
package net.hasor.freemarker.support;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.hasor.Hasor;
import net.hasor.context.ApiBinder;
import net.hasor.context.AppContext;
import net.hasor.context.ModuleSettings;
import net.hasor.context.anno.DefineModule;
import net.hasor.context.anno.support.AnnoSupportModule;
import net.hasor.context.module.AbstractHasorModule;
import net.hasor.freemarker.ConfigurationFactory;
import net.hasor.freemarker.FmMethod;
import net.hasor.freemarker.FmTag;
import net.hasor.freemarker.FmTemplateLoaderCreator;
import net.hasor.freemarker.FmTemplateLoaderDefine;
import net.hasor.freemarker.FreemarkerManager;
import net.hasor.freemarker.Tag;
/**
 * Freemarker�����ӳ�һ����������Ϊ��Ҫ����icache����������
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
@DefineModule(description = "org.hasor.freemarker���������֧�֡�")
public class FreemarkerSupportModule extends AbstractHasorModule {
    public void configuration(ModuleSettings info) {
        info.beforeMe(AnnoSupportModule.class);
        //freemarker�������
        try {
            Thread.currentThread().getContextClassLoader().loadClass("freemarker.template.Configuration");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**��ʼ��.*/
    public void init(ApiBinder apiBinder) {
        //
        apiBinder.getGuiceBinder().bind(FreemarkerManager.class).to(InternalFreemarkerManager.class);
        String configurationFactory = apiBinder.getInitContext().getSettings().getString("hasor-freemarker.configurationFactory", DefaultFreemarkerFactory.class.getName());
        try {
            apiBinder.getGuiceBinder().bind(ConfigurationFactory.class).to((Class<? extends ConfigurationFactory>) Class.forName(configurationFactory)).asEagerSingleton();
        } catch (Exception e) {
            Hasor.error("bind configurationFactory error %s", e);
            apiBinder.getGuiceBinder().bind(ConfigurationFactory.class).to(DefaultFreemarkerFactory.class).asEagerSingleton();
        }
        //
        this.loadTemplateLoader(apiBinder);
        this.loadFmTag(apiBinder);
        this.loadFmMethod(apiBinder);
    }
    //
    /**װ��TemplateLoader*/
    protected void loadTemplateLoader(ApiBinder event) {
        //1.��ȡ
        Set<Class<?>> templateLoaderCreatorSet = event.getClassSet(FmTemplateLoaderDefine.class);
        if (templateLoaderCreatorSet == null)
            return;
        List<Class<FmTemplateLoaderCreator>> templateLoaderCreatorList = new ArrayList<Class<FmTemplateLoaderCreator>>();
        for (Class<?> cls : templateLoaderCreatorSet) {
            if (FmTemplateLoaderCreator.class.isAssignableFrom(cls) == false) {
                Hasor.warning("loadTemplateLoader : not implemented ITemplateLoaderCreator. class=%s", cls);
            } else {
                templateLoaderCreatorList.add((Class<FmTemplateLoaderCreator>) cls);
            }
        }
        //3.ע�����
        FmBinderImplements freemarkerBinder = new FmBinderImplements();
        for (Class<FmTemplateLoaderCreator> creatorType : templateLoaderCreatorList) {
            FmTemplateLoaderDefine creatorAnno = creatorType.getAnnotation(FmTemplateLoaderDefine.class);
            String defineName = creatorAnno.configElement();
            freemarkerBinder.bindTemplateLoaderCreator(defineName, creatorType);
            Hasor.info("loadTemplateLoader %s at %s.", defineName, creatorType);
        }
        freemarkerBinder.configure(event.getGuiceBinder());
    }
    //
    /**װ��FmTag*/
    protected void loadFmTag(ApiBinder event) {
        //1.��ȡ
        Set<Class<?>> fmTagSet = event.getClassSet(FmTag.class);
        if (fmTagSet == null)
            return;
        List<Class<Tag>> fmTagList = new ArrayList<Class<Tag>>();
        for (Class<?> cls : fmTagSet) {
            if (Tag.class.isAssignableFrom(cls) == false) {
                Hasor.warning("loadFmTag : not implemented IFmTag or IFmTag2. class=%s", cls);
            } else {
                fmTagList.add((Class<Tag>) cls);
            }
        }
        //3.ע�����
        FmBinderImplements freemarkerBinder = new FmBinderImplements();
        for (Class<Tag> fmTagType : fmTagList) {
            FmTag fmTagAnno = fmTagType.getAnnotation(FmTag.class);
            String tagName = fmTagAnno.value();
            freemarkerBinder.bindTag(tagName, fmTagType);
            Hasor.info("loadFmTag %s at %s.", tagName, fmTagType);
        }
        freemarkerBinder.configure(event.getGuiceBinder());
    }
    //
    /**װ��FmMethod*/
    protected void loadFmMethod(ApiBinder event) {
        //1.��ȡ
        Set<Class<?>> fmMethodSet = event.getClassSet(Object.class);
        if (fmMethodSet == null)
            return;
        FmBinderImplements freemarkerBinder = new FmBinderImplements();
        for (Class<?> fmMethodType : fmMethodSet) {
            try {
                Method[] m1s = fmMethodType.getMethods();
                for (Method fmMethod : m1s) {
                    if (fmMethod.isAnnotationPresent(FmMethod.class) == true) {
                        FmMethod fmMethodAnno = fmMethod.getAnnotation(FmMethod.class);
                        String funName = fmMethodAnno.value();
                        freemarkerBinder.bindMethod(funName, fmMethod);
                        Hasor.info("loadFmMethod %s at %s.", funName, fmMethod);
                    }
                }
            } catch (NoClassDefFoundError e) {/**/}
        }
        //3.ע�����
        freemarkerBinder.configure(event.getGuiceBinder());
    }
    //
    /***/
    public void start(AppContext appContext) {
        FreemarkerManager freemarkerManager = appContext.getInstance(FreemarkerManager.class);
        freemarkerManager.start();
    }
    public void stop(AppContext appContext) {
        FreemarkerManager freemarkerManager = appContext.getInstance(FreemarkerManager.class);
        freemarkerManager.stop();
        Hasor.info("freemarker is stop.");
    }
}