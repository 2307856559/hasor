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
import java.util.Set;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import org.more.util.ClassUtil;
import org.more.webui.support.UIComponent;
/**
 * ���ฺ�𴴽�webui�ĸ����齨�Լ���ȡ�����Ҫ���á�
 * @version : 2012-5-22
 * @author ������ (zyc@byshell.org)
 */
public class FacesConfig {
    private String       encoding        = "utf-8";                //�ַ�����
    private boolean      localizedLookup = false;                  //�Ƿ����ù��ʻ��Ķ�֧��
    private String       facesSuffix     = ".xhtml";
    private FilterConfig initConfig      = null;
    private String[]     scanPackages    = { "org", "com", "net" }; //ɨ������·��
    /*----------------------------------------------------------------*/
    public FacesConfig(FilterConfig initConfig) {
        this.initConfig = initConfig;
    }
    private static long genID = 0;
    /**����������ͣ����ɸ����ID*/
    public static String generateID(Class<? extends UIComponent> compClass) {
        return "com_" + (genID++);
    }
    /**��ȡ��ʼ���Ļ���������*/
    public String getInitConfig(String key) {
        return this.initConfig.getInitParameter(key);
    }
    /**��ȡServletContext*/
    public ServletContext getServletContext() {
        return this.initConfig.getServletContext();
    }
    /**��ȡһ��booleanֵ��ֵ������ģ���Ƿ�֧�ֹ��ʻ���*/
    public boolean isLocalizedLookup() {
        return this.localizedLookup;
    }
    /**����һ��booleanֵ��ֵ������ģ���Ƿ�֧�ֹ��ʻ���*/
    public void setLocalizedLookup(boolean localizedLookup) {
        this.localizedLookup = localizedLookup;
    }
    /**��ȡҳ��ʹ�õ��ַ�����*/
    public String getEncoding() {
        return this.encoding;
    };
    /**����ҳ��ʹ�õ��ַ�����*/
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**��ȡһ����չ�������Ǿ߱�����չ�����ļ�������ΪUI�ļ���*/
    public String getFacesSuffix() {
        return this.facesSuffix;
    }
    /**����һ����չ�������Ǿ߱�����չ�����ļ�������ΪUI�ļ���*/
    public void setFacesSuffix(String facesSuffix) {
        this.facesSuffix = facesSuffix;
    }
    /**��ȡɨ������·��*/
    public String[] getScanPackages() {
        return scanPackages;
    }
    /**����ɨ������·��*/
    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }
    /*----------------------------------------------------------------*/
    private ClassUtil classUtil = null;
    public Set<Class<?>> getClassSet(Class<?> targetType) {
        if (classUtil == null)
            this.classUtil = ClassUtil.newInstance(this.scanPackages);
        return classUtil.getClassSet(targetType);
    }
}