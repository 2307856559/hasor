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
import java.util.HashMap;
import java.util.Map;
import org.more.webui.UIInitException;
import org.more.webui.render.RenderKit;
import org.more.webui.support.UIComponent;
import freemarker.template.Configuration;
/**
 * ����������֧��webui�Ļ��������л���
 * @version : 2012-4-25
 * @author ������ (zyc@byshell.org)
 */
public abstract class FacesContext {
    private FacesConfig            facesConfig  = null;
    private Map<String, RenderKit> renderKitMap = new HashMap<String, RenderKit>();
    /*��������齨��Bean����ӳ���ϵ*/
    private Map<String, String>    componentMap = new HashMap<String, String>();
    private Map<String, Object>    att          = null;
    //
    public FacesContext(FacesConfig facesConfig) {
        this.facesConfig = facesConfig;
    };
    /**��ȡ���ö���*/
    public FacesConfig getFacesConfig() {
        return this.facesConfig;
    };
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
     * @param componentClass �齨class���͡�
     */
    public void addComponent(String tagName, String componentBeanName) {
        this.componentMap.put(tagName, componentBeanName);
    }
    /**�����齨�ı�ǩ����ȡ�齨*/
    public UIComponent getComponent(String tagName) throws UIInitException {
        String componentBeanName = this.componentMap.get(tagName);
        if (componentBeanName == null)
            return null;
        return this.getBeanContext().getBean(componentBeanName);
    }
    /**��ȡ���Լ��ϡ�*/
    public Map<String, Object> getAttribute() {
        if (this.att == null)
            this.att = new HashMap<String, Object>();
        return this.att;
    }
    /*----------------------------------------------------------------*/
    public void addTemplateString(String hashStr, String templateString) {
        a// TODO Auto-generated method stub
    }
    /**��ȡBean��������*/
    public abstract BeanManager getBeanContext();
    /**��ȡfreemarker�����ö���*/
    public abstract Configuration getFreemarker();
}