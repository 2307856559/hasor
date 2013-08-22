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
package org.hasor.context.core;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.hasor.Hasor;
import org.hasor.context.Environment;
import org.hasor.context.EventManager;
import org.hasor.context.InitContext;
import org.hasor.context.LifeCycle;
import org.hasor.context.Settings;
import org.hasor.context.environment.StandardEnvironment;
import org.hasor.context.event.StandardEventManager;
import org.hasor.context.setting.HasorSettings;
import org.more.util.ScanClassPath;
import org.more.util.StringUtils;
/**
 * {@link InitContext}�ӿ�ʵ���ࡣ
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public class StandardInitContext implements InitContext {
    private long                startTime;   //ϵͳ����ʱ��
    private String              mainConfig;
    private String[]            spanPackage;
    private Settings            settings;
    private Environment         environment;
    private EventManager        eventManager;
    private Map<String, Object> attributeMap;
    private Object              context;
    //
    public StandardInitContext() throws IOException {
        this("hasor-config.xml");
    }
    public StandardInitContext(String mainConfig) throws IOException {
        this(mainConfig, null);
    }
    public StandardInitContext(String mainConfig, Object context) throws IOException {
        this.mainConfig = mainConfig;
        this.setContext(context);
        this.initContext();
    }
    /**��ʼ������*/
    protected void initContext() throws IOException {
        this.startTime = System.currentTimeMillis();
        this.attributeMap = this.createAttributeMap();
        this.settings = this.createSettings();
        this.environment = this.createEnvironment();
        this.eventManager = this.createEventManager();
        //
        String[] spanPackages = this.getSettings().getStringArray("hasor.loadPackages");
        ArrayList<String> allPack = new ArrayList<String>();
        for (String packs : spanPackages) {
            if (StringUtils.isBlank(packs) == true)
                continue;
            String[] packArray = packs.split(",");
            for (String pack : packArray) {
                if (StringUtils.isBlank(packs) == true)
                    continue;
                allPack.add(pack.trim());
            }
        }
        //�����Ŀ���Ǳ������ơ�org������com������net�������İ�����������λ��
        Collections.sort(allPack, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -o1.compareToIgnoreCase(o2);
            }
        });
        this.spanPackage = allPack.toArray(new String[allPack.size()]);
        Hasor.info("loadPackages : " + Hasor.logString(this.spanPackage));
        //
        ((LifeCycle) this.settings).start();
    }
    /**����{@link Settings}�ӿڶ���*/
    protected Settings createSettings() throws IOException {
        return new HasorSettings(this.getMainConfig());
    }
    /**����{@link Environment}�ӿڶ���*/
    protected Environment createEnvironment() {
        return new StandardEnvironment(this.getSettings());
    }
    /**����{@link EventManager}�ӿڶ���*/
    protected EventManager createEventManager() {
        return new StandardEventManager(this.getSettings());
    }
    /**������������*/
    protected Map<String, Object> createAttributeMap() {
        return new HashMap<String, Object>();
    }
    //
    public long getAppStartTime() {
        return this.startTime;
    }
    public Object getContext() {
        return context;
    }
    /**��ȡ�������ļ�*/
    public String getMainConfig() {
        return mainConfig;
    }
    /**����������*/
    public void setContext(Object context) {
        this.context = context;
    }
    public Settings getSettings() {
        return this.settings;
    }
    public Environment getEnvironment() {
        return this.environment;
    }
    public EventManager getEventManager() {
        return this.eventManager;
    }
    /**��ȡ���Խӿ�*/
    public Map<String, Object> getAttributeMap() {
        return attributeMap;
    }
    public Set<Class<?>> getClassSet(Class<?> featureType) {
        return ScanClassPath.getClassSet(this.spanPackage, featureType);
    }
    protected void finalize() throws Throwable {
        ((LifeCycle) this.settings).stop();
        super.finalize();
    }
}