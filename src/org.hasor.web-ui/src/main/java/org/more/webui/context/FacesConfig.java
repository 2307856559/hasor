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
package org.more.webui.context;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import org.more.util.StringConvertUtils;
import org.more.webui.freemarker.parser.Hook_Include;
import org.more.webui.freemarker.parser.Hook_UserTag;
import org.more.webui.freemarker.parser.TemplateScanner;
import org.more.webui.web.DefaultWebUIFactory;
import org.more.webui.web.WebUIFactory;
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
    /**��ȡ��ʼ���Ļ���������*/
    public String getInitConfig(String key) {
        return this.initConfig.getInitParameter(key);
    }
    /**��ȡServletContext*/
    public ServletContext getServletContext() {
        return this.initConfig.getServletContext();
    }
    private TemplateScanner templateScanner = null;
    public TemplateScanner getTemplateScanner() {
        if (templateScanner == null) {
            this.templateScanner = new TemplateScanner();
            this.templateScanner.addElementHook(Hook_UserTag.Name, new Hook_UserTag());/*UnifiedCall��@add*/
            this.templateScanner.addElementHook(Hook_Include.Name, new Hook_Include());/*Include��@Include*/
        }
        return this.templateScanner;
    }
    /*--------------------------------------------------------------------------*/
    private String factoryName = null;
    public String getWebUIFactoryClass() {
        if (this.factoryName == null)
            this.factoryName = StringConvertUtils.parseString(this.getInitConfig(WebUIConfig.FactoryName.value()), DefaultWebUIFactory.class.getName());
        return this.factoryName;
    };
    //
    private Boolean localizedLookup = null;
    /**��ȡһ��booleanֵ��ֵ������ģ���Ƿ�֧�ֹ��ʻ���*/
    public boolean isLocalizedLookup() {
        if (this.localizedLookup == null)
            this.localizedLookup = StringConvertUtils.parseBoolean(this.getInitConfig(WebUIConfig.LocalizedLookup.value()), false);
        return localizedLookup;
    };
    //
    private String outEncoding = null;
    /**�������*/
    public String getOutEncoding() {
        if (this.outEncoding == null)
            this.outEncoding = StringConvertUtils.parseString(this.getInitConfig(WebUIConfig.OutEncoding.value()), "utf-8");
        return this.outEncoding;
    };
    //
    private String pageEncoding = null;
    /**��ȡҳ��ʹ�õ��ַ�����*/
    public String getPageEncoding() {
        if (this.pageEncoding == null)
            this.pageEncoding = StringConvertUtils.parseString(this.getInitConfig(WebUIConfig.PageEncoding.value()), "utf-8");
        return this.pageEncoding;
    };
    //
    private String facesSuffix = null;
    /**��ȡһ����չ�������Ǿ߱�����չ�����ļ�������ΪUI�ļ���*/
    public String getFacesSuffix() {
        if (this.facesSuffix == null)
            this.facesSuffix = StringConvertUtils.parseString(this.getInitConfig(WebUIConfig.FacesSuffix.value()), ".xhtml");
        return this.facesSuffix;
    };
    //
    private String scanPackages = null;
    /**ɨ��İ���Ĭ��:org.*,com.*,net.*,java.* */
    public String getScanPackages() {
        if (this.scanPackages == null)
            this.scanPackages = StringConvertUtils.parseString(this.getInitConfig(WebUIConfig.ScanPackages.value()), "org.*,com.*,net.*,java.*");
        return this.scanPackages;
    };
}