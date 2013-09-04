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
package net.hasor.context.environment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.hasor.Hasor;
import net.hasor.context.Environment;
import net.hasor.context.HasorSettingListener;
import net.hasor.context.Settings;
import net.hasor.context.XmlProperty;
import org.more.util.StringUtils;
import org.more.util.map.DecSequenceMap;
/**
 * Environment�ӿ�ʵ���࣬loadEnvironment�����ǳ�ʼ��������
 * @version : 2013-5-23
 * @author ������ (zyc@hasor.net)
 */
public class StandardEnvironment implements Environment, HasorSettingListener {
    /*������Settings*/
    private Settings            settings;
    /*����ʹ�õĻ�������Map*/
    private Map<String, String> finalEnvMap;
    /*�û�ͨ��Api��ӵĻ�������Map*/
    private Map<String, String> userEnvMap;
    //
    //
    //
    public StandardEnvironment(Settings settings) {
        this.settings = Hasor.assertIsNotNull(settings, "Settings type parameter is empty!");
        this.userEnvMap = new HashMap<String, String>();
        settings.addSettingsListener(this);
    }
    public Settings getSettings() {
        return this.settings;
    }
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
        this.userEnvMap.put(envName, StringUtils.isBlank(envValue) ? "" : envValue);
    }
    public void remoteEnvVar(String varName) {
        if (StringUtils.isBlank(varName)) {
            Hasor.warning("%s env, name is empty.");
            return;
        }
        this.userEnvMap.remove(varName);
        Hasor.info("%s env removed.", varName);
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
    //
    //
    /**�������õĻ�������*/
    protected Map<String, String> configEnvironment() {
        Settings settings = this.getSettings();
        XmlProperty[] xmlPropArray = settings.getXmlPropertyArray("environmentVar");
        List<String> envNames = new ArrayList<String>();//�����ռ�������������
        for (XmlProperty xmlProp : xmlPropArray) {
            for (XmlProperty envItem : xmlProp.getChildren())
                envNames.add(envItem.getName().toUpperCase());
        }
        Map<String, String> hasorEnv = new HashMap<String, String>();
        for (String envItem : envNames)
            hasorEnv.put(envItem, settings.getString("environmentVar." + envItem));
        /*��������work_home*/
        String workDir = settings.getString("environmentVar.HASOR_WORK_HOME", "./");
        workDir = workDir.replace("/", File.separator);
        if (workDir.startsWith("." + File.separatorChar))
            hasorEnv.put("HASOR_WORK_HOME", new File(System.getProperty("user.dir"), workDir.substring(2)).getAbsolutePath());
        else
            hasorEnv.put("HASOR_WORK_HOME", workDir);
        return hasorEnv;
    }
    /*
     * SettingListener �ӿ�ʵ��
     *   ʵ�ָýӿڵ�Ŀ���ǣ�ͨ��ע��SettingListener��̬���»������������Ϣ��
     */
    public void onLoadConfig(Settings newConfig) {
        //1.ϵͳ�������� & Javaϵͳ����
        Map<String, String> systemEnv = new HashMap<String, String>();
        systemEnv.putAll(System.getenv());
        //2.Java����
        Properties prop = System.getProperties();
        Map<String, String> javaProp = new HashMap<String, String>();
        for (Object propKey : prop.keySet()) {
            String k = propKey.toString();
            Object v = prop.get(propKey);
            if (v != null)
                javaProp.put(k, v.toString());
        }
        //3.Hasor ���б���
        Map<String, String> hasorEnv = this.configEnvironment();
        hasorEnv = (hasorEnv == null) ? new HashMap<String, String>() : hasorEnv;
        //4.������Ч
        DecSequenceMap<String, String> finalMap = new DecSequenceMap<String, String>();
        finalMap.addMap(userEnvMap);
        finalMap.addMap(hasorEnv);
        finalMap.addMap(javaProp);
        finalMap.addMap(systemEnv);
        //5.����hasor ���л�������
        for (Entry<String, String> hasorEnt : hasorEnv.entrySet()) {
            String k = hasorEnt.getKey();
            String v = hasorEnt.getValue();
            finalMap.put(k, "");/*Ԥ�������ֹѭ��*/
            v = this.evalString(v, finalMap);
            finalMap.put(k, v);
            hasorEnt.setValue(v);
        }
        //
        this.finalEnvMap = finalMap;
        //
        /*��־���*/
        int keyMaxSize = 0;
        for (String key : finalMap.keySet())
            keyMaxSize = (key.length() >= keyMaxSize) ? key.length() : keyMaxSize;
        keyMaxSize = keyMaxSize + 2;
        StringBuffer sb = new StringBuffer();
        sb.append("onLoadConfig Environment \n");
        sb.append(StringUtils.fixedString('-', 100) + "\n");
        sb.append(formatMap4log(keyMaxSize, systemEnv) + "\n");
        sb.append(StringUtils.fixedString('-', 100) + "\n");
        sb.append(formatMap4log(keyMaxSize, javaProp) + "\n");
        sb.append(StringUtils.fixedString('-', 100) + "\n");
        sb.append(formatMap4log(keyMaxSize, hasorEnv) + "\n");
        sb.append(StringUtils.fixedString('-', 100) + "\n");
        sb.append(formatMap4log(keyMaxSize, userEnvMap));
        Hasor.info(sb.toString());
    }
    private static String formatMap4log(int colWidth, Map<String, String> mapData) {
        /*���ϵͳ����������־*/
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
        paramMap.put(varName, "");/*Ԥ����ֵ*/
        //
        String varValue = this.getEnv().get(varName);
        if (StringUtils.isBlank(varValue))
            varValue = "";
        else
            varValue = this.evalString(varValue, paramMap);
        paramMap.put(varName, varValue);/*����Ԥ����ֵ*/
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
        Hasor.debug("evalString '%s' eval to '%s'.", evalString, returnData);
        return returnData;
    }
    //
    //
    //
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
        Hasor.debug("create Temp File at %s.", tmpFile);
        return tmpFile;
    };
    /**
    * ����·���㷨����һ��Path
    * @param target Ŀ��
    * @param dirSize ÿ��Ŀ¼�¿���ӵ�е���Ŀ¼���ļ���Ŀ��
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
}