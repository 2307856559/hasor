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
package org.hasor.test.ext.mappingfile.setting;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hasor.Hasor;
import net.hasor.core.XmlNode;
import net.hasor.core.setting.StandardContextSettings;
import org.more.util.ResourcesUtils;
import org.more.util.map.DecSequenceMap;
import org.more.util.map.Properties;
/**
 * 
 * @version : 2013-9-9
 * @author ������(zyc@hasor.net)
 */
public class MappingContextSettings extends StandardContextSettings {
    /**ӳ�������ļ�*/
    public static final String MappingConfigName = "config-mapping.properties";
    //
    //
    /**����{@link MappingContextSettings}���Ͷ���*/
    public MappingContextSettings(String mainConfig) throws IOException {
        super(mainConfig);
    }
    /**����{@link MappingContextSettings}���Ͷ���*/
    public MappingContextSettings(File mainConfig) throws IOException {
        super(mainConfig);
    }
    /**����{@link MappingContextSettings}���Ͷ���*/
    public MappingContextSettings(URI mainConfig) throws IOException {
        super(mainConfig);
    }
    //
    protected void loadFinish() throws IOException {
        super.loadFinish();
        DecSequenceMap<String, Object> referConfig = new DecSequenceMap<String, Object>();
        Map<String, Map<String, Object>> nsMap = this.getNamespaceSettingMap();
        for (Map<String, Object> ent : nsMap.values())
            referConfig.addMap(ent);
        //
        Map<String, Object> mappingSettings = new HashMap<String, Object>();
        List<URL> mappingList = ResourcesUtils.getResources(MappingConfigName);
        if (mappingList == null)
            return;
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
                value = (value instanceof XmlNode) ? ((XmlNode) value).getText() : value;
                /*���Գ�ͻ��ӳ��*/
                if (referConfig.containsKey($propxyKey) == true) {
                    Hasor.error("mapping conflict! %s has this key.", $propxyKey);
                } else
                    mappingSettings.put($propxyKey, value);
            }
        }
        //
        this.getSettingsMap().addMap(mappingSettings);
    }
}