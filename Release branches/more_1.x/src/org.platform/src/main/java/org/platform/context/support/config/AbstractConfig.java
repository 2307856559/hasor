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
package org.platform.context.support.config;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.more.global.assembler.xml.XmlProperty;
import org.more.global.assembler.xml.XmlPropertyGlobalFactory;
import org.more.util.ResourceWatch;
import org.more.util.ResourcesUtil;
import org.more.util.StringUtil;
import org.more.util.map.Properties;
import org.platform.Assert;
import org.platform.Platform;
import org.platform.context.Config;
import org.platform.context.SettingListener;
import org.platform.context.Settings;
/**
 * ServletContext��ContextConfig����
 * @version : 2013-4-2
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractConfig implements Config {
    private final String                appSettingsName1    = "config.xml";
    private final String                appSettingsName2    = "static-config.xml";
    private final String                appSettingsName3    = "config-mapping.properties";
    private ServletContext              servletContext      = null;
    private final List<SettingListener> settingListenerList = new ArrayList<SettingListener>();
    //
    //
    public AbstractConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }
    @Override
    public String getInitParameter(String name) {
        return this.servletContext.getInitParameter(name);
    }
    @Override
    public Enumeration<String> getInitParameterNames() {
        return this.servletContext.getInitParameterNames();
    }
    /***/
    public String getSettingsEncoding() {
        return "utf-8";
    }
    @Override
    public void addSettingsListener(SettingListener settingsListener) {
        if (this.settingListenerList.contains(settingsListener) == false)
            this.settingListenerList.add(settingsListener);
    }
    @Override
    public void removeSettingsListener(SettingListener settingsListener) {
        if (this.settingListenerList.contains(settingsListener) == true)
            this.settingListenerList.remove(settingsListener);
    }
    /**���������������ļ�ʱ*/
    protected void reLoadConfig(Settings newConfig) {
        for (SettingListener listener : this.settingListenerList)
            listener.loadConfig(newConfig);
    }
    //
    //
    private Settings      globalConfig  = null;
    private ResourceWatch resourceWatch = null; /*��س���*/
    @Override
    public Settings getSettings() {
        if (this.globalConfig != null)
            return this.globalConfig;
        //1.finalSettings
        Map<String, Object> finalSettings = this.loadALLConfig();
        //2.resourceWatch
        if (this.resourceWatch == null) {
            try {
                URL configURL = ResourcesUtil.getResource(appSettingsName1);
                Assert.isNotNull(configURL, "Can't get to " + configURL);
                this.resourceWatch = new SettingsResourceWatch(configURL.toURI(), 15 * 1000/*15����һ��*/, this);
                this.resourceWatch.setDaemon(true);
                this.resourceWatch.start();
            } catch (Exception e) {
                Platform.error("resourceWatch start error, on : %s Settings file !%s", appSettingsName1, e);
            }
        }
        //3.globalConfig
        this.globalConfig = new Settings(finalSettings) {};
        this.globalConfig.disableCaseSensitive();
        return globalConfig;
    }
    //
    /**װ�����������ļ�*/
    protected Map<String, Object> loadALLConfig() {
        HashMap<String, Object> finalSettings = new HashMap<String, Object>();
        this.loadStaticConfig(finalSettings);
        this.loadMainConfig(finalSettings);
        this.loadMappingConfig(finalSettings);
        return finalSettings;
    }
    //
    /**װ���������ļ���̬���á�*/
    protected void loadMainConfig(Map<String, Object> toMap) {
        String encoding = this.getSettingsEncoding();
        try {
            URL configURL = ResourcesUtil.getResource(appSettingsName1);
            if (configURL != null) {
                Platform.info("load ��%s��", configURL);
                loadConfig(configURL.toURI(), encoding, toMap);
            }
        } catch (Exception e) {
            Platform.error("load ��%s�� error!%s", appSettingsName1, e);
        }
    }
    //
    /**װ�ؾ�̬���á�*/
    protected void loadStaticConfig(Map<String, Object> toMap) {
        String encoding = this.getSettingsEncoding();
        //1.װ������static-config.xml
        try {
            List<URL> streamList = ResourcesUtil.getResources(appSettingsName2);
            if (streamList != null) {
                for (URL resURL : streamList) {
                    Platform.info("load ��%s��", resURL);
                    loadConfig(resURL.toURI(), encoding, toMap);
                }
            }
        } catch (Exception e) {
            Platform.error("load ��%s�� error!%s", appSettingsName2, e);
        }
    }
    //
    /**װ������ӳ�䣬�����ǲ��յ�ӳ�����á�*/
    protected void loadMappingConfig(Map<String, Object> referConfig) {
        try {
            List<URL> mappingList = ResourcesUtil.getResources(appSettingsName3);
            if (mappingList != null)
                for (URL url : mappingList) {
                    InputStream inputStream = ResourcesUtil.getResourceAsStream(url);
                    Properties prop = new Properties();
                    prop.load(inputStream);
                    for (String key : prop.keySet()) {
                        String $propxyKey = key.toLowerCase();
                        String $key = prop.get(key).toLowerCase();
                        Object value = referConfig.get($key);
                        if (value == null)
                            Platform.warning("%s mapping to %s value is null.", $propxyKey, $key);
                        else {
                            /*���Գ�ͻ��ӳ��*/
                            if (referConfig.containsKey($propxyKey) == true) {
                                Platform.error("mapping conflict! %s has this key.", $propxyKey);
                            } else
                                referConfig.put($propxyKey, value);
                        }
                    }
                }
        } catch (Exception e) {
            Platform.error("load ��%s�� error!%s", appSettingsName3, e);
        }
    }
    //
    /**�Զ��������ļ������ռ䡣*/
    protected abstract List<String> loadNameSpaceDefinition();
    //
    /**loadConfigװ������*/
    private void loadConfig(URI configURI, String encoding, Map<String, Object> loadTo) throws IOException {
        Platform.info("PlatformSettings loadConfig Xml namespace : %s", configURI);
        XmlPropertyGlobalFactory xmlg = null;
        //1.<������Ч�������ռ�>
        try {
            xmlg = new XmlPropertyGlobalFactory();
            xmlg.setIgnoreRootElement(true);/*���Ը�*/
            /*�����Զ���������ռ�֧�֡�*/
            List<String> loadNameSpaceDefinition = this.loadNameSpaceDefinition();
            if (loadNameSpaceDefinition != null && loadNameSpaceDefinition.isEmpty() == false)
                for (String loadNS : loadNameSpaceDefinition)
                    if (StringUtil.isBlank(loadNS) == false)
                        xmlg.getLoadNameSpace().add(loadNS);
            //
            Map<String, Object> dataMap = xmlg.createMap(encoding, new Object[] { ResourcesUtil.getResourceAsStream(configURI) });
            /*�����ֵ�ϲ�����*/
            for (String key : dataMap.keySet()) {
                String $key = key.toLowerCase();
                Object $var = dataMap.get(key);
                Object $varConflict = loadTo.get(key);
                if ($varConflict != null && $varConflict instanceof XmlProperty && $var instanceof XmlProperty) {
                    XmlProperty v1 = (XmlProperty) $var;
                    XmlProperty v2 = (XmlProperty) $varConflict;
                    //
                    MergeDefaultXmlProperty v3 = new MergeDefaultXmlProperty(v1.getName());
                    v3.getAttributeMap().putAll(v2.getAttributeMap());
                    v3.getAttributeMap().putAll(v1.getAttributeMap());
                    v3.getChildren().addAll(v1.getChildren());
                    v3.getChildren().addAll(v2.getChildren());
                }
                loadTo.put($key, $var);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Platform.warning("namespcae [%s] no support!", configURI);
        }
    }
    //
    /***/
    private class SettingsResourceWatch extends ResourceWatch {
        private AbstractConfig platformConfig = null;
        public SettingsResourceWatch(URI uri, int watchStepTime, AbstractConfig platformConfig) {
            super(uri, watchStepTime);
            this.platformConfig = platformConfig;
        }
        @Override
        public void reload(URI resourceURI) throws IOException {
            Map<String, Object> newConfig = this.platformConfig.loadALLConfig();
            this.platformConfig.getSettings().setContainer(newConfig);
            this.platformConfig.reLoadConfig(this.platformConfig.getSettings());
        }
        @Override
        public long lastModify(URI resourceURI) throws IOException {
            if ("file".equals(resourceURI.getScheme()) == true)
                return new File(resourceURI).lastModified();
            return 0;
        }
        @Override
        public void firstLoad(URI resourceURI) throws IOException {}
    }
}