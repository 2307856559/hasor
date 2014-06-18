/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package net.hasor.core.environment;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.hasor.core.Environment;
import net.hasor.core.EventCallBackHook;
import net.hasor.core.EventContext;
import net.hasor.core.EventListener;
import net.hasor.core.Hasor;
import net.hasor.core.Settings;
import net.hasor.core.SettingsListener;
import net.hasor.core.XmlNode;
import net.hasor.core.event.StandardEventManager;
import org.more.UnhandledException;
import org.more.util.ResourceWatch;
import org.more.util.StringUtils;
import org.more.util.map.DecSequenceMap;
/**
 * {@link Environment}接口实现类。
 * @version : 2013-4-9
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractEnvironment implements Environment {
    private String[] spanPackage;
    private Settings settings;
    private Object   context;
    //---------------------------------------------------------------------------------Basic Method
    public Object getContext() {
        return this.context;
    }
    public void setContext(Object context) {
        this.context = context;
    }
    public boolean isDebug() {
        return this.settings.getBoolean("hasor.debug", false);
    }
    /**设置扫描路径*/
    public void setSpanPackage(String[] spanPackage) {
        this.spanPackage = spanPackage;
    }
    public String[] getSpanPackage() {
        return spanPackage;
    }
    public Set<Class<?>> findClass(Class<?> featureType) {
        return this.getSettings().findClass(featureType, this.spanPackage);
    }
    public Settings getSettings() {
        return this.settings;
    }
    //
    /*----------------------------------------------------------------------------------------Env*/
    /**初始化方法*/
    protected void initEnvironment() {
        Hasor.logInfo("initEnvironment.");
        //
        this.settingListenerList = new ArrayList<SettingsListener>();
        try {
            this.settings = this.createSettings();
            this.settings.refresh();
        } catch (Exception e) {
            throw new UnhandledException(e);
        }
        this.envVars = this.createEnvVars();
        this.eventManager = this.createEventManager();
        //
        String[] spanPackages = this.getSettings().getStringArray("hasor.loadPackages", "net.hasor.core.*,net.hasor.plugins.*");
        Set<String> allPack = new HashSet<String>();
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
        this.spanPackage = allPack.toArray(new String[allPack.size()]);
        Hasor.logInfo("loadPackages : " + Hasor.logString(this.spanPackage));
        //
        this.settingWatch = this.createSettingWatch();
        if (this.settingWatch != null) {
            this.settingWatch.setDaemon(true);
            this.settingWatch.start();
        }
    }
    /**创建{@link Settings}接口对象*/
    protected abstract Settings createSettings() throws IOException;
    /**在缓存目录内创建一个不重名的临时文件名。 */
    public synchronized File uniqueTempFile() throws IOException {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {}
        long markTime = System.currentTimeMillis();
        String atPath = genPath(markTime, 512);
        String fileName = atPath.substring(0, atPath.length() - 1) + "_" + String.valueOf(markTime) + ".tmp";
        File tmpFile = new File(evalEnvVar(TempPath), fileName);
        tmpFile.getParentFile().mkdirs();
        tmpFile.createNewFile();
        Hasor.logDebug("create Temp File at %s.", tmpFile);
        return tmpFile;
    }
    /**
    * 生成路径算法生成一个Path
    * @param number 参考数字
    * @param size 每个目录下可以拥有的子目录或文件数目。
    */
    public String genPath(long number, int size) {
        StringBuffer buffer = new StringBuffer();
        long b = size;
        long c = number;
        do {
            long m = number % b;
            buffer.append(m + File.separator);
            c = number / b;
            number = c;
        } while (c > 0);
        return buffer.reverse().toString();
    }
    //
    /*--------------------------------------------------------------------------------------Event*/
    private EventContext eventManager;
    public EventContext getEventManager() {
        return this.eventManager;
    }
    /**创建{@link EventContext}接口对象*/
    protected EventContext createEventManager() {
        return new StandardEventManager(this);
    }
    public void pushListener(String eventType, EventListener eventListener) {
        this.getEventManager().pushListener(eventType, eventListener);
    }
    public void addListener(String eventType, EventListener eventListener) {
        this.getEventManager().addListener(eventType, eventListener);
    }
    public void removeListener(String eventType, EventListener eventListener) {
        this.getEventManager().removeListener(eventType, eventListener);
    }
    public void fireSyncEvent(String eventType, Object... objects) {
        this.getEventManager().fireSyncEvent(eventType, objects);
    }
    public void fireSyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.getEventManager().fireSyncEvent(eventType, callBack, objects);
    }
    public void fireAsyncEvent(String eventType, Object... objects) {
        this.getEventManager().fireAsyncEvent(eventType, objects);
    }
    public void fireAsyncEvent(String eventType, EventCallBackHook callBack, Object... objects) {
        this.getEventManager().fireAsyncEvent(eventType, callBack, objects);
    }
    //
    /*-----------------------------------------------------------------------HasorSettingListener*/
    private SettingWatch           settingWatch        = null;
    private List<SettingsListener> settingListenerList = null;
    /**触发配置文件重载事件。*/
    protected void onSettingChangeEvent() {
        for (SettingsListener listener : this.settingListenerList)
            listener.reload(this.getSettings());
    }
    /**添加配置文件变更监听器。*/
    public void addSettingsListener(SettingsListener settingsListener) {
        if (this.settingListenerList.contains(settingsListener) == false)
            this.settingListenerList.add(settingsListener);
    }
    /**删除配置文件监听器。*/
    public void removeSettingsListener(SettingsListener settingsListener) {
        if (this.settingListenerList.contains(settingsListener) == true)
            this.settingListenerList.remove(settingsListener);
    }
    /**获得所有配置文件改变事件监听器。*/
    public SettingsListener[] getSettingListeners() {
        return this.settingListenerList.toArray(new SettingsListener[this.settingListenerList.size()]);
    }
    //
    /*------------------------------------------------------------------------------ResourceWatch*/
    /**创建{@link SettingWatch}对象，该方法可以返回null表示不需要监视器。*/
    protected SettingWatch createSettingWatch() {
        final SettingWatch settingWatch = new SettingWatch(this) {};
        /*设置监听器检测间隔*/
        long interval = this.getSettings().getLong("hasor.settingsMonitor.interval", 15000L);
        settingWatch.setCheckSeepTime(interval);
        /*注册一个配置文件监听器，当配置文件更新时通知监听器更新检测间隔*/
        this.addSettingsListener(new SettingsListener() {
            public void reload(Settings newConfig) {
                long interval = newConfig.getLong("hasor.settingsMonitor.interval", 15000L);
                if (interval != settingWatch.getCheckSeepTime()) {
                    Hasor.logInfo("SettingWatch to monitor configuration updates, set interval new Value is %s", interval);
                    settingWatch.setCheckSeepTime(interval);
                }
            }
        });
        return settingWatch;
    }
    /** 该类负责主配置文件的监听工作，以及引发配置文件重载事件。*/
    protected abstract static class SettingWatch extends ResourceWatch {
        private AbstractEnvironment env = null;
        //
        public SettingWatch(AbstractEnvironment env) {
            this.env = env;
        }
        public void firstStart(URI resourceURI) throws IOException {}
        /**当配置文件被检测到有修改迹象时，调用刷新进行重载。*/
        public final void onChange(URI resourceURI) throws IOException {
            this.env.getSettings().refresh();
            this.env.onSettingChangeEvent();
        }
        /**检测主配置文件是否被修改*/
        public long lastModify(URI resourceURI) throws IOException {
            if ("file".equals(resourceURI.getScheme()) == true)
                return new File(resourceURI).lastModified();
            return 0;
        }
        public synchronized void start() {
            this.setName("MasterConfiguration-Watch");
            Hasor.logInfo("settings Watch started thread name is %s.", this.getName());
            this.setDaemon(true);
            URI mainConfig = this.env.getSettingURI();
            //2.启动监听器
            try {
                if (mainConfig == null) {
                    Hasor.logWarn("ignore the master setting file, Watch Thread exit.");
                    return;
                }
                this.setResourceURI(this.env.getSettingURI());
            } catch (Exception e) {
                Hasor.logError("settings Watch start error, on : %s Settings file !%s", mainConfig, e);
            }
            //
            super.start();
        }
    }
    //
    /*-----------------------------------------------------------------------------------Env Vars*/
    private EnvVars envVars;
    public String evalString(String eval) {
        return this.envVars.evalString(eval);
    }
    public String evalEnvVar(String varName) {
        return this.envVars.evalEnvVar(varName);
    }
    public String getEnvVar(String varName) {
        return this.envVars.getEnvVar(varName);
    }
    public void addEnvVar(String varName, String value) {
        this.envVars.addEnvVar(varName, value);
    }
    public void remoteEnvVar(String varName) {
        this.envVars.remoteEnvVar(varName);
    }
    public void refreshVariables() {
        this.envVars.reload(this.getSettings());
    }
    public Map<String, String> getEnv() {
        return this.envVars.getEnv();
    }
    /**创建{@link EnvVars}接口对象*/
    protected EnvVars createEnvVars() {
        return new EnvVars(this);
    }
    /** 该类负责处理环境变量相关操作*/
    protected class EnvVars implements SettingsListener {
        /*所属的Environment*/
        private AbstractEnvironment env;
        /*最终使用的环境变量Map*/
        private Map<String, String> finalEnvMap;
        /*用户通过Api添加的环境变量Map*/
        private Map<String, String> userEnvMap;
        //
        public EnvVars(AbstractEnvironment env) {
            this.env = Hasor.assertIsNotNull(env, "InitContext type parameter is empty!");
            this.userEnvMap = new HashMap<String, String>();
            this.env.addSettingsListener(this);
            this.reload(this.env.getSettings());
        }
        public void addEnvVar(String envName, String envValue) {
            if (StringUtils.isBlank(envName)) {
                Hasor.logWarn("%s env, name is empty.", envName);
                return;
            }
            //
            if (StringUtils.isBlank(envValue))
                Hasor.logWarn("%s env, value is empty.", envName);
            else
                Hasor.logInfo("%s = %s.", envName, envValue);
            //
            this.userEnvMap.put(envName, StringUtils.isBlank(envValue) ? "" : envValue);
        }
        public void remoteEnvVar(String varName) {
            if (StringUtils.isBlank(varName)) {
                Hasor.logWarn("%s env, name is empty.");
                return;
            }
            this.userEnvMap.remove(varName);
            Hasor.logInfo("%s env removed.", varName);
        }
        public String getEnvVar(String envName) {
            return this.getEnv().get(envName);
        }
        public Map<String, String> getEnv() {
            if (this.finalEnvMap == null)
                this.finalEnvMap = new HashMap<String, String>();
            return Collections.unmodifiableMap(this.finalEnvMap);
        }
        //
        /**特殊配置的环境变量*/
        protected Map<String, String> configEnvironment() {
            Settings settings = this.env.getSettings();
            XmlNode[] xmlPropArray = settings.getXmlNodeArray("hasor.environmentVar");
            List<String> envNames = new ArrayList<String>();//用于收集环境变量名称
            for (XmlNode xmlProp : xmlPropArray) {
                for (XmlNode envItem : xmlProp.getChildren())
                    envNames.add(envItem.getName().toUpperCase());
            }
            Map<String, String> hasorEnv = new HashMap<String, String>();
            for (String envItem : envNames)
                hasorEnv.put(envItem, settings.getString("hasor.environmentVar." + envItem));
            /*单独处理work_home*/
            String workDir = settings.getString("hasor.environmentVar.HASOR_WORK_HOME", "./");
            workDir = workDir.replace("/", File.separator);
            if (workDir.startsWith("." + File.separatorChar))
                hasorEnv.put("HASOR_WORK_HOME", new File(System.getProperty("user.dir"), workDir.substring(2)).getAbsolutePath());
            else
                hasorEnv.put("HASOR_WORK_HOME", workDir);
            return hasorEnv;
        }
        /*
         * SettingListener 接口实现
         *   实现该接口的目的是，通过注册SettingListener动态更新环境变量相关信息。
         */
        public void reload(Settings newConfig) {
            //1.系统环境变量 & Java系统属性
            Map<String, String> systemEnv = new HashMap<String, String>();
            systemEnv.putAll(System.getenv());
            //2.Java属性
            Properties prop = System.getProperties();
            Map<String, String> javaProp = new HashMap<String, String>();
            for (Object propKey : prop.keySet()) {
                String k = propKey.toString();
                Object v = prop.get(propKey);
                if (v != null)
                    javaProp.put(k, v.toString());
            }
            //3.Hasor 特有变量
            Map<String, String> hasorEnv = this.configEnvironment();
            hasorEnv = (hasorEnv == null) ? new HashMap<String, String>() : hasorEnv;
            //4.设置生效
            DecSequenceMap<String, String> finalMap = new DecSequenceMap<String, String>();
            finalMap.addMap(userEnvMap);
            finalMap.addMap(hasorEnv);
            finalMap.addMap(javaProp);
            finalMap.addMap(systemEnv);
            //5.解析hasor 特有环境变量
            for (Entry<String, String> hasorEnt : hasorEnv.entrySet()) {
                String k = hasorEnt.getKey();
                String v = hasorEnt.getValue();
                finalMap.put(k, "");/*预输出，防止循环*/
                v = this.evalString(v, finalMap);
                finalMap.put(k, v);
                hasorEnt.setValue(v);
            }
            //
            this.finalEnvMap = finalMap;
            //
            /*日志输出*/
            if (Hasor.isInfoLogger()) {
                int keyMaxSize = 0;
                for (String key : finalMap.keySet())
                    keyMaxSize = (key.length() >= keyMaxSize) ? key.length() : keyMaxSize;
                keyMaxSize = keyMaxSize + 2;
                StringBuffer sb = new StringBuffer();
                sb.append("EnvVars:");
                if (isDebug()) {
                    if (!systemEnv.isEmpty()) {
                        sb.append("\n" + formatMap4log(keyMaxSize, systemEnv));
                        sb.append("\n" + StringUtils.fixedString('-', 50));
                    }
                    if (!javaProp.isEmpty()) {
                        sb.append("\n" + formatMap4log(keyMaxSize, javaProp));
                        sb.append("\n" + StringUtils.fixedString('-', 50));
                    }
                }
                if (!hasorEnv.isEmpty()) {
                    sb.append("\n" + formatMap4log(keyMaxSize, hasorEnv));
                    sb.append("\n" + StringUtils.fixedString('-', 50));
                }
                if (!userEnvMap.isEmpty()) {
                    sb.append("\n" + formatMap4log(keyMaxSize, userEnvMap));
                    sb.append("\n" + StringUtils.fixedString('-', 50));
                }
                Hasor.logInfo(sb.toString());
            }
        }
        private String formatMap4log(int colWidth, Map<String, String> mapData) {
            /*输出系统环境变量日志*/
            StringBuffer outLog = new StringBuffer("");
            for (String key : mapData.keySet()) {
                String var = mapData.get(key);
                var = (var != null) ? var.replace("\r", "\\r").replace("\n", "\\n") : var;
                outLog.append(StringUtils.fixedString(' ', colWidth - key.length()));
                outLog.append(String.format(" %s : %s", key, var));
                outLog.append('\n');
            }
            if (outLog.length() > 1)
                outLog.deleteCharAt(outLog.length() - 1);
            return outLog.toString();
        }
        //
        //
        //
        public String evalEnvVar(String varName) {
            return this.evalEnvVar(varName, new HashMap<String, String>());
        }
        public String evalString(String evalString) {
            return this.evalString(evalString, new HashMap<String, String>());
        }
        private String evalEnvVar(String varName, Map<String, String> paramMap) {
            if (paramMap.containsKey(varName))
                return paramMap.get(varName);
            paramMap.put(varName, "");/*预处理值*/
            //
            String varValue = this.getEnv().get(varName);
            if (StringUtils.isBlank(varValue))
                varValue = "";
            else
                varValue = this.evalString(varValue, paramMap);
            paramMap.put(varName, varValue);/*覆盖预处理值*/
            return varValue;
        }
        private String evalString(String evalString, Map<String, String> paramMap) {
            if (StringUtils.isBlank(evalString))
                return "";
            Pattern keyPattern = Pattern.compile("(?:%(\\w+)%){1,1}");//  (?:%(\w+)%)
            Matcher keyM = keyPattern.matcher(evalString);
            ArrayList<String> data = new ArrayList<String>();
            while (keyM.find()) {
                String varKey = keyM.group(1);
                String var = this.evalEnvVar(varKey, paramMap);
                var = StringUtils.isBlank(var) ? ("%" + varKey + "%") : var;
                data.add(var);
            }
            String[] splitArr = keyPattern.split(evalString);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < splitArr.length; i++) {
                sb.append(splitArr[i]);
                if (data.size() > i)
                    sb.append(data.get(i));
            }
            String returnData = sb.toString();
            Hasor.logDebug("evalString '%s' eval to '%s'.", evalString, returnData);
            return returnData;
        }
    }
}