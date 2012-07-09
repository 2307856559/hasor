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
import java.io.IOException;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.more.util.CommonCodeUtil;
import org.more.webui.UIInitException;
import org.more.webui.freemarker.loader.ConfigTemplateLoader;
import org.more.webui.freemarker.loader.MultiTemplateLoader;
import org.more.webui.render.RenderKit;
import org.more.webui.support.UIComponent;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
/**
 * ����������֧��webui�Ļ��������л���
 * @version : 2012-4-25
 * @author ������ (zyc@byshell.org)
 */
public abstract class FacesContext {
    private FacesConfig              facesConfig          = null;
    private Map<String, RenderKit>   renderKitMap         = new HashMap<String, RenderKit>();
    //
    private Set<String>              componentSet         = new HashSet<String>();
    private Map<String, Class<?>>    componentTypeMap     = new HashMap<String, Class<?>>();
    private Map<String, UIComponent> componentObjectMap   = new HashMap<String, UIComponent>();
    //
    private Map<String, Object>      att                  = null;
    private ConfigTemplateLoader     configTemplateLoader = new ConfigTemplateLoader();
    //
    public FacesContext(FacesConfig facesConfig) {
        this.facesConfig = facesConfig;
    };
    /**��ȡ���ö���*/
    public FacesConfig getEnvironment() {
        return this.facesConfig;
    }
    /*----------------------------------------------------------------*/
    /**���RenderKit��*/
    public void addRenderKit(String scope, RenderKit kit) {
        this.renderKitMap.put(scope, kit);
    }
    /**��ȡָ����ǩ������������Ⱦ����*/
    public RenderKit getRenderKit(String scope) {
        return this.renderKitMap.get(scope);
    }
    /**
     * ���һ���齨��ע�ᡣ
     * @param tagName �齨�ı�ǩ����
     * @param componentBeanType �齨class���͡�
     */
    public void addComponentType(String tagName, Class<?> componentBeanType) {
        this.componentTypeMap.put(tagName, componentBeanType);
        this.componentSet.add(tagName);
    }
    /**
     * ���һ���齨��ע�ᡣ
     * @param tagName �齨�ı�ǩ����
     * @param componentBeanObject �齨����
     */
    public void addComponentObject(String tagName, UIComponent componentBeanObject) {
        this.componentObjectMap.put(tagName, componentBeanObject);
        this.componentSet.add(tagName);
    }
    /**��ȡ�Ѿ�ע����齨��*/
    public Set<String> getComponentSet() {
        return Collections.unmodifiableSet(this.componentSet);
    }
    /**�����齨�ı�ǩ����ȡ�齨*/
    public UIComponent getComponent(String tagName) throws UIInitException {
        if (componentObjectMap.containsKey(tagName) == true)
            return componentObjectMap.get(tagName);
        Class<?> componentBeanType = this.componentTypeMap.get(tagName);
        if (componentBeanType == null)
            return null;
        return this.getBeanContext().getBean(componentBeanType);
    }
    /**��ȡ���Լ��ϡ�*/
    public Map<String, Object> getAttribute() {
        if (this.att == null)
            this.att = new HashMap<String, Object>();
        return this.att;
    }
    private Configuration cfg = null;
    public final Configuration getFreemarker() {
        if (this.cfg == null) {
            this.cfg = createFreemarker();
            cfg.setDefaultEncoding(this.getEnvironment().getPageEncoding());
            cfg.setOutputEncoding(this.getEnvironment().getOutEncoding());
            cfg.setLocalizedLookup(this.getEnvironment().isLocalizedLookup());
            //
            TemplateLoader[] loaders = null;
            if (cfg.getTemplateLoader() != null) {
                loaders = new TemplateLoader[2];
                loaders[1] = cfg.getTemplateLoader();
            } else
                loaders = new TemplateLoader[1];
            loaders[0] = this.configTemplateLoader;
            cfg.setTemplateLoader(new MultiTemplateLoader(loaders));
        }
        return this.cfg;
    }
    public void processTemplateString(String templateString, Writer writer, Map<String, Object> rootMap) throws TemplateException, IOException {
        //A.ȡ��ָ��
        String hashStr = null;
        try {
            /*ʹ��MD5����*/
            hashStr = CommonCodeUtil.MD5.getMD5(templateString);
        } catch (NoSuchAlgorithmException e) {
            /*ʹ��hashCode*/
            hashStr = String.valueOf(templateString.hashCode());
        }
        hashStr += ".temp";
        //B.�����ݼ��뵽ģ��������С�
        this.configTemplateLoader.addTemplateAsString(hashStr, templateString);
        //C.ִ��ָ��ģ��
        this.getFreemarker().getTemplate(hashStr).process(rootMap, writer);
    }
    /*----------------------------------------------------------------*/
    /**��ȡBean��������*/
    public abstract BeanManager getBeanContext();
    /**��ȡfreemarker�����ö���*/
    public abstract Configuration createFreemarker();
}