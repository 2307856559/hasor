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
package org.hasor.context.environment;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hasor.Hasor;
import org.hasor.context.Environment;
import org.hasor.context.HasorSettingListener;
import org.hasor.context.Settings;
import org.hasor.context.WorkSpace;
import org.more.util.StringUtils;
/**
 * Environment�ӿ�ʵ���࣬loadEnvironment�����ǳ�ʼ��������
 * @version : 2013-5-23
 * @author ������ (zyc@byshell.org)
 */
public class StandardEnvironment implements Environment, HasorSettingListener {
    private Map<String, String> envMap      = new HashMap<String, String>();
    private Map<String, String> readOnlyMap = null;
    private WorkSpace           workSpace   = null;
    //
    public StandardEnvironment(WorkSpace workSpace) {
        Hasor.assertIsNotNull(workSpace, "WorkSpace type parameter is empty!");
        this.workSpace = workSpace;
        this.workSpace.getSettings().addSettingsListener(this);
    }
    /*
     * SettingListener �ӿ�ʵ��
     *   ʵ�ָýӿڵ�Ŀ���ǣ�ͨ��ע��SettingListener��̬���»������������Ϣ��
     */
    @Override
    public void onLoadConfig(Settings newConfig) {
        //1.ϵͳ�������� & Javaϵͳ����
        this.envMap.putAll(System.getenv());
        Properties prop = System.getProperties();
        Map<String, String> hasorProp = new HashMap<String, String>();
        for (Object propKey : prop.keySet()) {
            String k = propKey.toString();
            Object v = prop.get(propKey);
            if (v != null)
                hasorProp.put(k, v.toString());
        }
        this.envMap.putAll(hasorProp);
        //2.Hasor ���б���
        Map<String, String> hasorEnv = this.getHasorEnvironment();
        hasorEnv = (hasorEnv == null) ? new HashMap<String, String>() : hasorEnv;
        this.envMap.putAll(hasorEnv);
        //3.��־���
        int keyMaxSize = 0;
        for (String key : this.envMap.keySet())
            keyMaxSize = (key.length() >= keyMaxSize) ? key.length() : keyMaxSize;
        //
        keyMaxSize = keyMaxSize + 2;
        Hasor.info("onLoadConfig Environment \n" + //
                StringUtils.fixedString(100, '-') + "\n" + //
                Hasor.formatMap4log(keyMaxSize, System.getenv()) + //
                StringUtils.fixedString(100, '-') + "\n" + //
                Hasor.formatMap4log(keyMaxSize, hasorProp) + //
                StringUtils.fixedString(100, '-') + "\n" + //
                Hasor.formatMap4log(keyMaxSize, hasorEnv) + //
                StringUtils.fixedString(100, '-'));
    }
    /**��ȡHasor�Ļ�������*/
    protected Map<String, String> getHasorEnvironment() {
        Map<String, String> hasorEnv = new HashMap<String, String>();
        hasorEnv.put("HASOR_WORK_HOME", workSpace.getWorkDir());
        hasorEnv.put("HASOR_DATA_HOME", workSpace.getDataDir());
        hasorEnv.put("HASOR_TEMP_HOME", workSpace.getTempDir());
        hasorEnv.put("HASOR_CACHE_HOME", workSpace.getCacheDir());
        hasorEnv.put("HASOR_PLUGIN_HOME", workSpace.getPluginDir());
        return hasorEnv;
    }
    /*
     * Environment �ӿ�ʵ���࣬����ʵ��Environment�ӿ���ع��ܡ�
     */
    @Override
    public String evalString(String evalString) {
        Pattern keyPattern = Pattern.compile("(?:\\{(\\w+)\\}){1,1}");//  (?:\{(\w+)\})
        Matcher keyM = keyPattern.matcher(evalString);
        ArrayList<String> data = new ArrayList<String>();
        while (keyM.find()) {
            String varKey = keyM.group(1);
            String var = this.getEnvVar(varKey);
            var = StringUtils.isBlank(var) ? ("{" + varKey + "}") : var;
            data.add(var);
        }
        String[] splitArr = keyPattern.split(evalString);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < splitArr.length; i++) {
            sb.append(splitArr[i]);
            if (data.size() > i)
                sb.append(data.get(i));
        }
        String returnData = sb.toString().replace("/", File.separator);
        Hasor.debug("evalString '%s' eval to '%s'.", evalString, returnData);
        return returnData;
    }
    @Override
    public void addEnvVar(String envName, String envValue) {
        if (StringUtils.isBlank(envName)) {
            Hasor.warning("%s env, name is empty.", envName);
            return;
        }
        //
        if (StringUtils.isBlank(envValue))
            Hasor.warning("%s env, value is empty.", envName);
        else
            Hasor.info("%s = %s.", envName, envValue);
        //
        this.envMap.put(envName, StringUtils.isBlank(envValue) ? "" : envValue);
    }
    @Override
    public void remoteEnvVar(String varName) {
        if (StringUtils.isBlank(varName)) {
            Hasor.warning("%s env, name is empty.");
            return;
        }
        this.envMap.remove(varName);
        Hasor.info("%s env removed.", varName);
    }
    @Override
    public String getEnvVar(String envName) {
        return this.getEnv().get(envName);
    }
    @Override
    public Map<String, String> getEnv() {
        if (this.readOnlyMap == null)
            this.readOnlyMap = Collections.unmodifiableMap(this.envMap);
        return this.readOnlyMap;
    }
    @Override
    public WorkSpace getWorkSpace() {
        return this.workSpace;
    }
}