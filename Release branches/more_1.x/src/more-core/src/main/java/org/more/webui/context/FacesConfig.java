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
package org.more.webui.context;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import org.more.webui.UIInitException;
import org.more.webui.freemarker.loader.ITemplateLoader;
import org.more.webui.freemarker.loader.template.ClassPathTemplateLoader;
import org.more.webui.freemarker.loader.template.DirTemplateLoader;
import org.more.webui.freemarker.loader.template.MultiTemplateLoader;
import org.more.webui.freemarker.parser.Hook_Include;
import org.more.webui.freemarker.parser.Hook_UserTag;
import org.more.webui.freemarker.parser.TemplateScanner;
import org.more.webui.support.UIComponent;
import org.more.webui.support.UIViewRoot;
import freemarker.template.Template;
/**
 * ���ฺ�𴴽�webui�ĸ����齨�Լ���ȡ�����Ҫ���á�
 * @version : 2012-5-22
 * @author ������ (zyc@byshell.org)
 */
public class FacesConfig {
    private String                     encoding           = "utf-8";                         //�ַ�����
    private boolean                    localizedLookup    = false;                           //�Ƿ����ù��ʻ��Ķ�֧��
    private String                     facesSuffix        = ".xhtml";
    private FilterConfig               initConfig         = null;
    private ArrayList<ITemplateLoader> templateLoaderList = new ArrayList<ITemplateLoader>();
    /**�齨�ı�ǩ���Ͷ�Ӧ���齨����*/
    private Map<String, Class<?>>      componentMap       = new HashMap<String, Class<?>>();
    //    /**�齨�ı�ǩ���Ͷ�Ӧ����Ⱦ��*/
    //    private Map<String, RenderKit>     renderKitMap       = new HashMap<String, RenderKit>();
    /*----------------------------------------------------------------*/
    public FacesConfig(FilterConfig initConfig) {
        this.initConfig = initConfig;
    }
    /*----------------------------------------------------------------*/
    private static long genID = 0;
    /**����������ͣ����ɸ����ID*/
    public static String generateID(Class<? extends UIComponent> compClass) {
        return "com_" + (genID++);
    }
    /**��ȡһ��booleanֵ��ֵ������ģ���Ƿ�֧�ֹ��ʻ���*/
    public boolean isLocalizedLookup() {
        return this.localizedLookup;
    }
    public void setLocalizedLookup(boolean localizedLookup) {
        this.localizedLookup = localizedLookup;
    }
    /**��ȡҳ��ʹ�õ��ַ�����*/
    public String getEncoding() {
        return this.encoding;
    };
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**��ȡһ����չ�������Ǿ߱�����չ�����ļ�������ΪUI�ļ���*/
    public String getFacesSuffix() {
        return this.facesSuffix;
    }
    public void setFacesSuffix(String facesSuffix) {
        this.facesSuffix = facesSuffix;
    }
    public String getInitConfig(String key) {
        return this.initConfig.getInitParameter(key);
    }
    public ServletContext getServletContext() {
        return this.initConfig.getServletContext();
    }
    /*----------------------------------------------------------------*/
    /**���{@link ITemplateLoader}����*/
    public void addLoader(ITemplateLoader loader) {
        this.templateLoaderList.add(loader);
    }
    /**���һ������ΪLoader��·����*/
    public void addLoader(String packageName) {
        ClassPathTemplateLoader loader = new ClassPathTemplateLoader(packageName);
        this.addLoader(loader);
    }
    /**���һ��·����ΪLoader��·����*/
    public void addLoader(File templateDir) throws IOException {
        DirTemplateLoader loader = new DirTemplateLoader(templateDir);
        this.addLoader(loader);
    }
    /**��������ӵ�Loader���س�һ��{@link MultiTemplateLoader}����*/
    public MultiTemplateLoader getMultiTemplateLoader() {
        MultiTemplateLoader multiLoader = new MultiTemplateLoader();
        for (ITemplateLoader loader : this.templateLoaderList)
            multiLoader.addTemplateLoader(loader);
        return multiLoader;
    }
    /**
     * ���һ���齨��ע�ᡣ
     * @param tagName �齨�ı�ǩ����
     * @param componentClass �齨class���͡�
     */
    public void addComponent(String tagName, Class<?> componentClass) {
        this.componentMap.put(tagName, componentClass);
    }
    //    /**����һ��{@link RenderKit}*/
    //    public RenderKit getRenderKit(String scope) {
    //        RenderKit kit = this.renderKitMap.get(scope);
    //        if (kit == null) {
    //            kit = new RenderKit();
    //            this.renderKitMap.put(scope, kit);
    //        }
    //        return kit;
    //    }
    //    /**��ӱ�ǩ��ӳ���ϵ��*/
    //    public void addRender(String scope, String tagName, Class<?> renderClass) {
    //        this.getRenderKit(scope).addRender(tagName, renderClass);
    //    }
    /*----------------------------------------------------------------*/
    /**���ڴ���һ��{@link UIViewRoot}����*/
    public UIViewRoot createViewRoot(Template template, String templateFile, FacesContext uiContext) throws UIInitException, IOException {
        //A.����ɨ����
        TemplateScanner scanner = new TemplateScanner();
        scanner.addElementHook("UnifiedCall", new Hook_UserTag(this));/*UnifiedCall��@add*/
        scanner.addElementHook("Include", new Hook_Include(this));/*Include��@Include*/
        //B.����ģ���ȡUIViewRoot
        return (UIViewRoot) scanner.parser(template, new UIViewRoot(), uiContext);
    }
    /**�����齨�ı�ǩ���������齨*/
    public UIComponent createComponent(String tagName, FacesContext uiContext) throws UIInitException {
        Class<?> comClass = this.componentMap.get(tagName);
        if (comClass != null)
            return (UIComponent) AppUtil.getObj(comClass);
        else
            return null;
    }
}