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
package net.hasor.context.core;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hasor.Hasor;
import net.hasor.context.XmlProperty;
import net.hasor.context.setting.FileInputStreamSettings;
import org.more.util.ResourcesUtils;
import org.more.util.map.DecSequenceMap;
import org.more.util.map.Properties;
/**
 * Settings�ӿڵ�ʵ�֣������ṩ�˶�hasor-config.xml��static-config.xml��config-mapping.properties�ļ��Ľ���֧�֡�
 * ����֮�⻹�ṩ�˶�config.xml�����ļ��ĸı�������������ļ�Ӧ��ֻ��һ������
 * @version : 2013-4-2
 * @author ������ (zyc@hasor.net)
 */
public class HasorSettings extends FileInputStreamSettings {
    public synchronized void refresh() throws IOException {
        //
        // 
        this.loadStaticResources();//���뾲̬�����ļ�
        this.loadMainConfig();//�������������ļ�
        //
        this.loadStreams();
        //
        DecSequenceMap<String, Object> fullData = this.getSettingsMap();
        Map<String, Object> mappingData = this.loadMappingConfig(fullData);
        fullData.addMap(mappingData);
        //
        //
        //        this.settingNsMap = finalSettings;
        //        //4.�ϲ���ͬ�����ռ��µ�������
        //        for (Map<String, Object> ent : this.settingNsMap.values())
        //            this.settingMap.addMap(ent);
        //        //5.ȡ��ӳ����
        //        Map<String, Object> finalMapping = this.loadMappingConfig(this.settingMap);
        //        this.settingMap.addMap(finalMapping);
        //        //6.�����¼�
        //        this.doEvent();
    }
    /*-------------------------------------------------------------------------------------------------------
     * 
     * ���������ļ� ��ط���
     * 
     */
    /**��static-config.xml�����ļ�������װ�ص�����ָ����map�У���������ظ��������滻�ϲ�������*/
    protected void loadStaticResources() throws IOException {
        final String staticConfig = "static-config.xml";
        //1.װ������static-config.xml
        try {
            List<URL> streamList = ResourcesUtils.getResources(staticConfig);
            if (streamList != null) {
                for (URL resURL : streamList) {
                    InputStream stream = ResourcesUtils.getResourceAsStream(resURL);
                    Hasor.info("load ��%s��", resURL);
                    this.addStream(stream);
                }
            }
        } catch (Exception e) {
            Hasor.error("load ��%s�� error!%s", staticConfig, e);
        }
    }
    /**װ���������ļ���̬���á�*/
    protected void loadMainResource(String mainConfig) {
        try {
            URL configURL = ResourcesUtils.getResource(mainConfig);
            if (configURL != null) {
                InputStream stream = ResourcesUtils.getResourceAsStream(configURL);
                Hasor.info("load ��%s��", configURL);
                this.addStream(stream);
            } else
                Hasor.warning("cannot load the root configuration file ��%s��", mainConfig);
        } catch (Exception e) {
            Hasor.error("load ��%s�� error!%s", mainConfig, e);
        }
    }
    /**װ������ӳ�䣬�����ǲ��յ�ӳ�����á�*/
    protected Map<String, Object> loadMappingConfig(Map<String, Object> referConfig) {
        final String configMapping = "config-mapping.properties";
        Map<String, Object> mappingSettings = new HashMap<String, Object>();
        try {
            List<URL> mappingList = ResourcesUtils.getResources(configMapping);
            if (mappingList != null)
                for (URL url : mappingList) {
                    InputStream inputStream = ResourcesUtils.getResourceAsStream(url);
                    Properties prop = new Properties();
                    prop.load(inputStream);
                    for (String key : prop.keySet()) {
                        String $propxyKey = key.toLowerCase();
                        String $key = prop.get(key).toLowerCase();
                        Object value = referConfig.get($key);
                        if (value == null) {
                            Hasor.warning("%s mapping to %s value is null.", $propxyKey, $key);
                            continue;
                        }
                        value = (value instanceof XmlProperty) ? ((XmlProperty) value).getText() : value;
                        /*���Գ�ͻ��ӳ��*/
                        if (referConfig.containsKey($propxyKey) == true) {
                            Hasor.error("mapping conflict! %s has this key.", $propxyKey);
                        } else
                            mappingSettings.put($propxyKey, value);
                    }
                }
        } catch (Exception e) {
            Hasor.error("load ��%s�� error!%s", configMapping, e);
        }
        return mappingSettings;
    }
}