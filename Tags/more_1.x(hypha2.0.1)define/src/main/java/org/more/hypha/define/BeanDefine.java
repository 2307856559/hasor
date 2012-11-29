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
package org.more.hypha.define;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.more.hypha.utils.DefineUtils;
/**
 * TemplateBeanDefine�����ڶ���һ��bean��ģ�塣
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public abstract class BeanDefine extends AbstractDefine {
    /**Base Info*/
    //
    /*ÿ��BeanΨһ��IDֵ��*/
    private String                      id            = null;
    /*����,�ڲ�ͬ��scope�¿����ظ����塣*/
    private String                      name          = null;
    /*������������*/
    private String                      scope         = null;
    /*��ʼ������&���췽������*/
    private List<ParamDefine>           initParams    = new ArrayList<ParamDefine>();
    /*Ӧ�õ�ģ��*/
    private String                      useTemplate   = null;
    /*------------------------------------------------------------------*/
    /**Mark Info*/
    //
    /*�����־*/
    private boolean                     abstractMark  = false;
    /*��̬��־*/
    private boolean                     singletonMark = false;
    /*�ӳ�װ�ر�־*/
    private boolean                     lazyMark      = true;
    /*������Ϣ*/
    private String                      description   = null;
    /*------------------------------------------------------------------*/
    /**Create Info*/
    //
    /*����Bean���ƻ�ID��*/
    private String                      factoryBean   = null;
    /*����Bean,�����ڴ�����bean�ķ�������*/
    private String                      factoryMethod = null;
    /*��ʼ���������������ڣ��׶�-������*/
    private String                      initMethod    = null;
    /*���ٷ������������ڣ��׶�-������*/
    private String                      destroyMethod = null;
    /*------------------------------------------------------------------*/
    /**Member Info*/
    //
    /*���Գ�Ա*/
    private Map<String, PropertyDefine> propertys     = new HashMap<String, PropertyDefine>();
    /*������Ա*/
    private Map<String, MethodDefine>   methods       = new HashMap<String, MethodDefine>();
    /*------------------------------------------------------------------*/
    /**���ؾ����������ַ�����*/
    public String toString() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + " UID=" + DefineUtils.getFullName(this);
    };
    //
    /**��ȡBeanΨһ��IDֵ��*/
    public String getId() {
        return id;
    }
    /**����BeanΨһ��IDֵ��*/
    public void setId(String id) {
        this.id = id;
    }
    /**��ȡ����,�����ڲ�ͬ��scope�¿����ظ����塣*/
    public String getName() {
        return name;
    }
    /**��������,�����ڲ�ͬ��scope�¿����ظ����塣*/
    public void setName(String name) {
        this.name = name;
    }
    /**��ȡ������������*/
    public String getScope() {
        return scope;
    }
    /**����������������*/
    public void setScope(String scope) {
        this.scope = scope;
    }
    /**��ȡ��ʼ������&���췽������*/
    public List<ParamDefine> getInitParams() {
        return initParams;
    }
    /**���ó�ʼ������&���췽������*/
    public void setInitParams(List<ParamDefine> initParams) {
        this.initParams = initParams;
    }
    /**��ȡӦ�õ�ģ��*/
    public String getUseTemplate() {
        return useTemplate;
    }
    /**����Ӧ�õ�ģ��*/
    public void setUseTemplate(String useTemplate) {
        this.useTemplate = useTemplate;
    }
    /**��ȡ�����־*/
    public boolean isAbstractMark() {
        return abstractMark;
    }
    /**���ó����־*/
    public void setAbstractMark(boolean abstractMark) {
        this.abstractMark = abstractMark;
    }
    /**��ȡ��̬��־*/
    public boolean isSingletonMark() {
        return singletonMark;
    }
    /**���õ�̬��־*/
    public void setSingletonMark(boolean singletonMark) {
        this.singletonMark = singletonMark;
    }
    /**��ȡ�ӳ�װ�ر�־*/
    public boolean isLazyMark() {
        return lazyMark;
    }
    /**�����ӳ�װ�ر�־*/
    public void setLazyMark(boolean lazyMark) {
        this.lazyMark = lazyMark;
    }
    /**��ȡ������Ϣ*/
    public String getDescription() {
        return description;
    }
    /**����������Ϣ*/
    public void setDescription(String description) {
        this.description = description;
    }
    /**��ȡ����Bean���ƻ�ID��*/
    public String getFactoryBean() {
        return factoryBean;
    }
    /**���ù���Bean���ƻ�ID��*/
    public void setFactoryBean(String factoryBean) {
        this.factoryBean = factoryBean;
    }
    /**��ȡ����Bean,�����ڴ�����bean�ķ�������*/
    public String getFactoryMethod() {
        return factoryMethod;
    }
    /**���ù���Bean,�����ڴ�����bean�ķ�������*/
    public void setFactoryMethod(String factoryMethod) {
        this.factoryMethod = factoryMethod;
    }
    /**��ȡ��ʼ���������������ڣ��׶�-������*/
    public String getInitMethod() {
        return initMethod;
    }
    /**���ó�ʼ���������������ڣ��׶�-������*/
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }
    /**��ȡ���ٷ������������ڣ��׶�-������*/
    public String getDestroyMethod() {
        return destroyMethod;
    }
    /**�������ٷ������������ڣ��׶�-������*/
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }
    /**��ȡ���Գ�Ա*/
    public Map<String, PropertyDefine> getPropertys() {
        return propertys;
    }
    /**�������Գ�Ա*/
    public void setPropertys(Map<String, PropertyDefine> propertys) {
        this.propertys = propertys;
    }
    /**��ȡ������Ա*/
    public Map<String, MethodDefine> getMethods() {
        return methods;
    }
    /**���÷�����Ա*/
    public void setMethods(Map<String, MethodDefine> methods) {
        this.methods = methods;
    }
}