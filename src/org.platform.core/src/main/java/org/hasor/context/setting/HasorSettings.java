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
package org.hasor.context.setting;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.hasor.HasorFramework;
import org.hasor.context.SettingListener;
import org.hasor.context.Settings;
import org.hasor.context.XmlProperty;
import org.more.util.ResourceWatch;
import org.more.util.ResourcesUtils;
import org.more.util.StringConvertUtils;
import org.more.util.StringUtils;
import org.more.util.map.Properties;
import org.more.xml.XmlParserKitManager;
import org.more.xml.stream.XmlReader;
/**
 * Settings�ӿڵ�ʵ�֣������ṩ�˶�config.xml��static-config.xml��config-mapping.properties�ļ��Ľ���֧�֡�
 * ����֮�⻹�ṩ�˶�config.xml�����ļ��ĸı�������������ļ�Ӧ��ֻ��һ������
 * @version : 2013-4-2
 * @author ������ (zyc@byshell.org)
 */
public class HasorSettings extends ResourceWatch implements Settings {
    //    private DecSequenceMap<String, String>   settingMap   = new DecSequenceMap<String, String>();
    //    private Map<String, Map<String, String>> nsSettingMap = new HashMap<String, Map<String, String>>();
    public HasorSettings() {
        super();
    }
    /*-------------------------------------------------------------------------------------------------------
     * 
     * ���������ļ� ��ط���
     * 
     */
    private String                    mainSettings    = "config.xml";
    private String                    settingEncoding = "utf-8";
    private Map<String, List<String>> nsDefine        = null;
    private XmlParserKitManager       initKitManager  = null;
    /**��ȡ���������ļ�ʱʹ�õ��ַ����롣*/
    public String getSettingEncoding() {
        return this.settingEncoding;
    }
    /**���ý��������ļ�ʱʹ�õ��ַ����롣*/
    public void setSettingEncoding(String encoding) {
        this.settingEncoding = encoding;
    }
    /**����Xml�����ռ�������б�*/
    private Map<String, List<String>> loadNsProp() throws IOException {
        if (this.nsDefine != null)
            return this.nsDefine;
        //
        HashMap<String, List<String>> define = new HashMap<String, List<String>>();
        List<URL> nspropURLs = ResourcesUtils.getResources("ns.prop");
        if (nspropURLs != null) {
            for (URL nspropURL : nspropURLs) {
                HasorFramework.info("find ��ns.prop�� at ��%s��.", nspropURL);
                InputStream inStream = ResourcesUtils.getResourceAsStream(nspropURL);
                if (inStream != null) {
                    /*����ns.prop*/
                    Properties prop = new Properties();
                    prop.load(inStream);
                    for (String key : prop.keySet()) {
                        String v = prop.get(key);
                        String k = key.trim();
                        List<String> nsPasser = null;
                        if (define.containsKey(k) == false) {
                            nsPasser = new ArrayList<String>();
                            define.put(k, nsPasser);
                        } else
                            nsPasser = define.get(k);
                        nsPasser.add(v);
                    }
                    /**/
                }
            }
        }
        HasorFramework.info("load space ��%s��.", define);
        this.nsDefine = define;
        return this.nsDefine;
    }
    /**����������*/
    private XmlParserKitManager loadXmlParserKitManager(Map<String, Map<String, String>> loadTo) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (this.initKitManager != null)
            return this.initKitManager;
        XmlParserKitManager kitManager = new XmlParserKitManager();
        Map<String, List<String>> nsParser = this.loadNsProp();
        for (String xmlNS : nsParser.keySet()) {
            //��ȡͬһ�������ռ���ע��Ľ�����
            List<String> xmlParserSet = nsParser.get(xmlNS);
            //�������ڴ�������ռ������ݵ�����
            Map<String, String> dataContainer = new HashMap<String, String>();
            //�����������������ҽ�ע��Ľ��������õ�������
            InternalHasorXmlParserPropxy nsKit = new InternalHasorXmlParserPropxy(dataContainer);
            //���뵽���ؽ��������
            loadTo.put(xmlNS, dataContainer);
            //
            for (String xmlParser : xmlParserSet) {
                Class<?> xmlParserType = Class.forName(xmlParser);
                nsKit.addTarget((HasorXmlParser) xmlParserType.newInstance());
                HasorFramework.info("add XmlNamespaceParser ��%s�� on ��%s��.", xmlParser, xmlNS);
            }
            /*ʹ��XmlParserKitManagerʵ�ֲ�ͬ���������ղ��������ռ��¼���֧��*/
            kitManager.regeditKit(xmlNS, nsKit);
        }
        HasorFramework.info("XmlParserKitManager created!");
        this.initKitManager = kitManager;
        return kitManager;
    }
    /**��static-config.xml�����ļ�������װ�ص�����ָ����map�У���������ظ��������滻�ϲ�������*/
    protected void loadStaticConfig(Map<String, Map<String, String>> toMap) throws IOException {
        final String staticConfig = "static-config.xml";
        //1.װ������static-config.xml
        try {
            List<URL> streamList = ResourcesUtils.getResources(staticConfig);
            if (streamList != null) {
                for (URL resURL : streamList) {
                    loadConfig(resURL.toURI(), toMap);
                    HasorFramework.info("load ��%s��", resURL);
                }
            }
        } catch (Exception e) {
            HasorFramework.error("load ��%s�� error!%s", staticConfig, e);
        }
    }
    /**װ���������ļ���̬���á�*/
    protected void loadMainConfig(String mainConfig, Map<String, Map<String, String>> toMap) {
        try {
            URL configURL = ResourcesUtils.getResource(mainConfig);
            if (configURL != null) {
                loadConfig(configURL.toURI(), toMap);
                HasorFramework.info("load ��%s��", configURL);
            }
        } catch (Exception e) {
            HasorFramework.error("load ��%s�� error!%s", mainConfig, e);
        }
    }
    /**װ������ӳ�䣬�����ǲ��յ�ӳ�����á�*/
    protected void loadMappingConfig(Map<String, Object> referConfig) {
        final String configMapping = "config-mapping.properties";
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
                            HasorFramework.warning("%s mapping to %s value is null.", $propxyKey, $key);
                            continue;
                        }
                        value = (value instanceof XmlProperty) ? ((XmlProperty) value).getText() : value;
                        /*���Գ�ͻ��ӳ��*/
                        if (referConfig.containsKey($propxyKey) == true) {
                            HasorFramework.error("mapping conflict! %s has this key.", $propxyKey);
                        } else
                            referConfig.put($propxyKey, value);
                    }
                }
        } catch (Exception e) {
            HasorFramework.error("load ��%s�� error!%s", configMapping, e);
        }
    }
    /**loadConfigװ������*/
    private void loadConfig(URI configURI, Map<String, Map<String, String>> loadTo) throws IOException, XMLStreamException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String encoding = this.getSettingEncoding();
        InputStream stream = ResourcesUtils.getResourceAsStream(configURI);
        //��ȡ������
        XmlParserKitManager xmlAccept = null;
        xmlAccept = this.loadXmlParserKitManager(loadTo);
        xmlAccept.setContext(this);
        //����Xml
        new XmlReader(stream).reader(xmlAccept, encoding, null);
        //
        //
        //
        //
        //
        //
        //
        //������Ҫ�����ݺϲ�����
        //
        System.out.println();
        //        Map<String, Map<String, String>> data = xmlAccept.getReturnData();
        //        //
        //        XmlPropertyGlobalFactory xmlg = null;
        //        //1.<������Ч�������ռ�>
        //        try {
        //            xmlg = new XmlPropertyGlobalFactory();
        //            xmlg.setIgnoreRootElement(true);/*���Ը�*/
        //            /*�����Զ���������ռ�֧�֡�*/
        //            if (this.loadNameSpaceList != null && this.loadNameSpaceList.isEmpty() == false)
        //                for (String loadNS : this.loadNameSpaceList)
        //                    if (StringUtils.isBlank(loadNS) == false)
        //                        xmlg.getLoadNameSpace().add(loadNS);
        //            //
        //            Map<String, Object> dataMap = xmlg.createMap(encoding, new Object[] { ResourcesUtils.getResourceAsStream(configURI) });
        //            /*�����ֵ�ϲ����⣨���ø��Ǻ�׷�ӵĲ��ԣ�*/
        //            for (String key : dataMap.keySet()) {
        //                String $key = key.toLowerCase();
        //                Object $var = dataMap.get(key);
        //                Object $varConflict = loadTo.get($key);
        //                if ($varConflict != null && $varConflict instanceof XmlProperty && $var instanceof XmlProperty) {
        //                    XmlProperty $new = (XmlProperty) $var;
        //                    XmlProperty $old = (XmlProperty) $varConflict;
        //                    XmlProperty $final = $old.clone();
        //                    /*���ǲ���*/
        //                    $final.getAttributeMap().putAll($new.getAttributeMap());
        //                    $final.setText($new.getText());
        //                    /*׷�Ӳ���*/
        //                    List<XmlProperty> $newChildren = new ArrayList<XmlProperty>($new.getChildren());
        //                    List<XmlProperty> $oldChildren = new ArrayList<XmlProperty>($old.getChildren());
        //                    Collections.reverse($newChildren);
        //                    Collections.reverse($oldChildren);
        //                    $final.getChildren().clear();
        //                    $final.getChildren().addAll($oldChildren);
        //                    $final.getChildren().addAll($newChildren);
        //                    Collections.reverse($final.getChildren());
        //                    loadTo.put($key, $final);
        //                } else
        //                    loadTo.put($key, $var);
        //            }
        //        } catch (Exception e) {
        //            HasorFramework.warning("namespcae [%s] no support!", configURI);
        //        }
    }
    public static void main(String[] args) throws IOException {
        HasorSettings settings = new HasorSettings();
        settings.refresh();
    }
    @Override
    public Settings getNamespace(URL namespace) {
        // TODO Auto-generated method stub
        return null;
    }
    private HashMap<String, String> getSettingMap() {
        // TODO Auto-generated method stub
        return null;
    }
    /*-------------------------------------------------------------------------------------------------------
     * 
     * �����ļ������ı��¼� ��ط���
     * 
     */
    private final List<SettingListener> settingListenerList = new ArrayList<SettingListener>();
    @Override
    public void refresh() throws IOException {
        Map<String, Map<String, String>> finalSettings = new HashMap<String, Map<String, String>>();
        this.loadStaticConfig(finalSettings);
        this.loadMainConfig(this.mainSettings, finalSettings);
        //        this.loadMappingConfig(finalSettings);
        this.doEvent();
    }
    /**���������ļ��޸ļ���*/
    @Override
    public synchronized void start() {
        try {
            URL configURL = ResourcesUtils.getResource(this.mainSettings);
            if (configURL == null) {
                HasorFramework.warning("Can't get to mainConfig %s.", configURL);
                return;
            }
            this.setResourceURI(configURL.toURI());
            this.setDaemon(true);
            HasorFramework.warning("settings Watch started thread name is %s.", this.getName());
            super.start();
        } catch (Exception e) {
            HasorFramework.error("settings Watch start error, on : %s Settings file !%s", this.mainSettings, e);
        }
    }
    /**/
    public void firstStart(URI resourceURI) throws IOException {}
    /**�������ļ�����⵽���޸ļ���ʱ������ˢ�½������ء�*/
    public final void onChange(URI resourceURI) throws IOException {
        this.refresh();
    }
    /**����������ļ��Ƿ��޸�*/
    public long lastModify(URI resourceURI) throws IOException {
        if ("file".equals(resourceURI.getScheme()) == true)
            return new File(resourceURI).lastModified();
        return 0;
    }
    /**���������ļ������¼���*/
    protected void doEvent() {
        for (SettingListener listener : this.settingListenerList)
            listener.reLoadConfig(this);
    }
    /**��������ļ������������*/
    @Override
    public void addSettingsListener(SettingListener settingsListener) {
        if (this.settingListenerList.contains(settingsListener) == false)
            this.settingListenerList.add(settingsListener);
    }
    /**ɾ�������ļ���������*/
    @Override
    public void removeSettingsListener(SettingListener settingsListener) {
        if (this.settingListenerList.contains(settingsListener) == true)
            this.settingListenerList.remove(settingsListener);
    }
    @Override
    public SettingListener[] getSettingListeners() {
        return this.settingListenerList.toArray(new SettingListener[this.settingListenerList.size()]);
    }
    /*-------------------------------------------------------------------------------------------------------
     * 
     * ��������
     * 
     */
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public final <T> T getToType(String name, Class<T> toType, T defaultValue) {
        Object oriObject = this.getSettingMap().get(StringUtils.isBlank(name) ? "" : name);
        if (oriObject == null)
            return defaultValue;
        //
        T var = null;
        if (oriObject instanceof String)
            //ԭʼ�������ַ�������Eval����
            var = StringConvertUtils.changeType((String) oriObject, toType);
        else if (oriObject instanceof GlobalProperty)
            //ԭʼ������GlobalPropertyֱ��get
            var = ((GlobalProperty) oriObject).getValue(toType, defaultValue);
        else
            //�������Ͳ��账�����ݾ���Ҫ��ֵ��
            var = (T) oriObject;
        return var;
    };
    /**����ȫ�����ò��������ҷ���toType����ָ�������͡�*/
    public final <T> T getToType(String name, Class<T> toType) {
        return this.getToType(name, toType, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ����*/
    public Object getObject(String name) {
        return this.getToType(name, Object.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Object}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Object getObject(String name, Object defaultValue) {
        return this.getToType(name, Object.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ����*/
    public Character getChar(String name) {
        return this.getToType(name, Character.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Character}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Character getChar(String name, Character defaultValue) {
        return this.getToType(name, Character.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ����*/
    public String getString(String name) {
        return this.getToType(name, String.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link String}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public String getString(String name, String defaultValue) {
        return this.getToType(name, String.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ����*/
    public Boolean getBoolean(String name) {
        return this.getToType(name, Boolean.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Boolean}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Boolean getBoolean(String name, Boolean defaultValue) {
        return this.getToType(name, Boolean.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ����*/
    public Short getShort(String name) {
        return this.getToType(name, Short.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Short}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Short getShort(String name, Short defaultValue) {
        return this.getToType(name, Short.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ����*/
    public Integer getInteger(String name) {
        return this.getToType(name, Integer.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Integer}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Integer getInteger(String name, Integer defaultValue) {
        return this.getToType(name, Integer.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ����*/
    public Long getLong(String name) {
        return this.getToType(name, Long.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Long}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Long getLong(String name, Long defaultValue) {
        return this.getToType(name, Long.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ����*/
    public Float getFloat(String name) {
        return this.getToType(name, Float.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Float}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Float getFloat(String name, Float defaultValue) {
        return this.getToType(name, Float.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ����*/
    public Double getDouble(String name) {
        return this.getToType(name, Double.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Double}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Double getDouble(String name, Double defaultValue) {
        return this.getToType(name, Double.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ����*/
    public Date getDate(String name) {
        return this.getToType(name, Date.class);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, Date defaultValue) {
        return this.getToType(name, Date.class, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public Date getDate(String name, long defaultValue) {
        return this.getToType(name, Date.class, new Date(defaultValue));
    };
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType) {
        return this.getToType(name, enmType, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Enum}��ʽ���󡣵ڶ�������ΪĬ��ֵ��*/
    public <T extends Enum<?>> T getEnum(String name, Class<T> enmType, T defaultValue) {
        return this.getToType(name, enmType, defaultValue);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(String name) {
        return this.getFilePath(name, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link Date}��ʽ�������ڱ�ʾ�ļ������ڶ�������ΪĬ��ֵ��*/
    public String getFilePath(String name, String defaultValue) {
        String filePath = this.getToType(name, String.class);
        if (filePath == null || filePath.length() == 0)
            return defaultValue;//��
        //
        int length = filePath.length();
        if (filePath.charAt(length - 1) == File.separatorChar)
            return filePath.substring(0, length - 1);
        else
            return filePath;
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(String name) {
        return this.getDirectoryPath(name, null);
    };
    /**����ȫ�����ò��������ҷ�����{@link File}��ʽ�������ڱ�ʾĿ¼�����ڶ�������ΪĬ��ֵ��*/
    public String getDirectoryPath(String name, String defaultValue) {
        String filePath = this.getToType(name, String.class);
        if (filePath == null || filePath.length() == 0)
            return defaultValue;//��
        //
        int length = filePath.length();
        if (filePath.charAt(length - 1) == File.separatorChar)
            return filePath;
        else
            return filePath + File.separatorChar;
    }
    /**����ȫ�����ò��������ҷ�����{@link XmlProperty}��ʽ����*/
    public XmlProperty getXmlProperty(String name) {
        return this.getToType(name, XmlProperty.class, null);
    }
}