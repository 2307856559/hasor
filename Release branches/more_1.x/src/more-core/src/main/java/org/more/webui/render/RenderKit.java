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
package org.more.webui.render;
import java.util.HashMap;
import java.util.Map;
import org.more.webui.context.FacesContext;
import org.more.webui.tag.TagObject;
/**
 * 
 * @version : 2012-5-22
 * @author ������ (zyc@byshell.org)
 */
public class RenderKit {
    private FacesContext        facesContext  = null;
    /*��ǩ���󼯺�*/
    private Map<String, Object> tagObjectMap  = new HashMap<String, Object>();
    /*��Ⱦ��ӳ��*/
    private Map<String, String> renderMapping = new HashMap<String, String>();
    /*----------------------------------------------------------------*/
    public void initKit(FacesContext facesContext) {
        this.facesContext = facesContext;
    }
    /**��ȡ�Ѿ�ע��ı�ǩ���󼯺ϡ�*/
    public Map<String, Object> getTags() {
        return this.tagObjectMap;
    }
    /**ע���ǩ�ֻ࣬�ܶ��Ѿ�ע��render���齨����ע�ᡣ*/
    public void addTag(String tagName, TagObject tagObject) {
        if (tagObject == null)
            throw new NullPointerException("TagObject���Ͳ�������Ϊ�ա�");
        this.tagObjectMap.put(tagName, tagObject);
    }
    /**��ȡ��Ⱦ������*/
    public Render<?> getRender(String tagName) {
        String mappingName = this.renderMapping.get(tagName);
        return this.facesContext.getBeanContext().getBean(mappingName);
    }
    /**�����Ⱦ��ӳ�䡣*/
    public void addRenderMapping(String tagName, String beanName) {
        this.renderMapping.put(tagName, beanName);
        this.tagObjectMap.put(tagName, new TagObject());//���Ĭ�ϱ�ǩ
    }
}