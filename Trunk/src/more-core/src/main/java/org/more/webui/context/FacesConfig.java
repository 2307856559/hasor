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
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import org.more.util.StringConvertUtil;
import org.more.webui.support.UIComponent;
import org.more.webui.web.DefaultWebUIFactory;
/**
 * ���ฺ�𴴽�webui�ĸ����齨�Լ���ȡ�����Ҫ���á�
 * @version : 2012-5-22
 * @author ������ (zyc@byshell.org)
 */
public class FacesConfig {
    public static enum WebUIConfig {
        /**ҳ��ģ��������룬Ĭ��utf-8��*/
        OutEncoding("WebUI_OutEncoding"),
        /**ҳ���ַ����룬Ĭ��utf-8��*/
        PageEncoding("WebUI_PageEncoding"),
        /**���ʻ���֧�֣�Ĭ��false��*/
        LocalizedLookup("WebUI_Localized"),
        /**���ص���չ����Ĭ��.xhtml*/
        FacesSuffix("WebUI_Faces"),
        /**{@link WebUIFactory}�ӿ�ʵ���࣬Ĭ�ϣ�DefaultWebUIFactory��*/
        FactoryName("WebUI_FactoryName"),
        /**ɨ��İ���Ĭ��:org.*,com.*,net.*,java.**/
        ScanPackages("WebUI_ScanPackages"), ;
        //
        private String value = null;
        WebUIConfig(String value) {
            this.value = value;
        }
        public String value() {
            return this.value;
        }
    }
    private FilterConfig initConfig = null;
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
    /*--------------------------------------------------------------------------*/
    private String factoryName = null;
    public String getWebUIFactoryClass() {
        if (this.factoryName == null)
            this.factoryName = StringConvertUtil.parseString(this.getInitConfig(WebUIConfig.FactoryName.value()), DefaultWebUIFactory.class.getName());
        return this.factoryName;
    };
    //
    private Boolean localizedLookup = null;
    /**��ȡһ��booleanֵ��ֵ������ģ���Ƿ�֧�ֹ��ʻ���*/
    public boolean isLocalizedLookup() {
        if (this.localizedLookup == null)
            this.localizedLookup = StringConvertUtil.parseBoolean(this.getInitConfig(WebUIConfig.LocalizedLookup.value()), false);
        return localizedLookup;
    };
    //
    private String outEncoding = null;
    /**�������*/
    public String getOutEncoding() {
        if (this.outEncoding == null)
            this.outEncoding = StringConvertUtil.parseString(this.getInitConfig(WebUIConfig.OutEncoding.value()), "utf-8");
        return this.outEncoding;
    };
    //
    private String pageEncoding = null;
    /**��ȡҳ��ʹ�õ��ַ�����*/
    public String getPageEncoding() {
        if (this.pageEncoding == null)
            this.pageEncoding = StringConvertUtil.parseString(this.getInitConfig(WebUIConfig.PageEncoding.value()), "utf-8");
        return this.pageEncoding;
    };
    //
    private String facesSuffix = null;
    /**��ȡһ����չ�������Ǿ߱�����չ�����ļ�������ΪUI�ļ���*/
    public String getFacesSuffix() {
        if (this.facesSuffix == null)
            this.facesSuffix = StringConvertUtil.parseString(this.getInitConfig(WebUIConfig.FacesSuffix.value()), ".xhtml");
        return this.facesSuffix;
    };
    //
    private String scanPackages = null;
    /**ɨ��İ���Ĭ��:org.*,com.*,net.*,java.* */
    public String getScanPackages() {
        if (this.scanPackages == null)
            this.scanPackages = StringConvertUtil.parseString(this.getInitConfig(WebUIConfig.ScanPackages.value()), "org.*,com.*,net.*,java.*");
        return this.scanPackages;
    };
}