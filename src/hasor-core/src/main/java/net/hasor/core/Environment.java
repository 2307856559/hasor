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
package net.hasor.core;
import java.net.URI;
import java.util.Set;
/**
 * ����֧��
 * @version : 2013-6-19
 * @author ������ (zyc@hasor.net)
 */
public interface Environment {
    /**��ȡ�����ļ�URI*/
    public URI getSettingURI();
    /**��ȡӦ�ó������á�*/
    public Settings getSettings();
    /**��ȡɨ��·��*/
    public String[] getSpanPackage();
    /**�ڿ��ɨ����ķ�Χ�ڲ��Ҿ��������༯�ϡ������������Ǽ̳е��ࡢ��ǵ�ע�⣩*/
    public Set<Class<?>> findClass(Class<?> featureType);
    /**�ж��Ƿ�Ϊ����ģʽ��*/
    public boolean isDebug();
    //
    /*--------------------------------------------------------------------------------------Event*/
    /**pushPhaseEvent����ע���ʱ����������յ�һ���¼�֮��ᱻ�Զ�ɾ����*/
    public void pushListener(String eventType, EventListener eventListener);
    /**���һ�������¼����¼���������*/
    public void addListener(String eventType, EventListener eventListener);
    /**ɾ��ĳ����������ע�ᡣ*/
    public void removeListener(String eventType, EventListener eventListener);
    /**ͬ����ʽ�׳��¼�������������ʱ�Ѿ�ȫ����������¼��ַ���<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ���ж��¼��ַ��׳��������쳣��*/
    public void fireSyncEvent(String eventType, Object... objects);
    /**ͬ����ʽ�׳��¼�������������ʱ�Ѿ�ȫ����������¼��ַ���<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ�÷������̵��쳣�������ַ��¼������̵����쳣����һ������ķ�ʽ���֡�*/
    public void fireSyncEvent(String eventType, EventCallBackHook callBack, Object... objects);
    /**�첽��ʽ�׳��¼���fireAsyncEvent�����ĵ��ò��������ʱ��ʼִ���¼�������һ�����¼�������������<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ�÷������̵��쳣�������ַ��¼������̵����쳣����һ������ķ�ʽ���֡�*/
    public void fireAsyncEvent(String eventType, Object... objects);
    /**�첽��ʽ�׳��¼���fireAsyncEvent�����ĵ��ò��������ʱ��ʼִ���¼�������һ�����¼�������������<p>
     * ע�⣺��ĳ��ʱ��������׳��쳣ʱ���ж��¼��ַ�����������ִ��Ȩ�����쳣����ӿڡ�*/
    public void fireAsyncEvent(String eventType, EventCallBackHook callBack, Object... objects);
    //
    /*----------------------------------------------------------------------------------------Env*/
    /**��ȡ����Ŀ¼������·�������ÿ�����config.xml�ġ�<b>environmentVar.HASOR_WORK_HOME</b>���ڵ������á�*/
    public static final String Work_Home          = "HASOR_WORK_HOME";
    /**��ȡ��ʱ�ļ����Ŀ¼������·�������ÿ�����config.xml�ġ�<b>environmentVar.HASOR_TEMP_PATH</b>���ڵ������á�*/
    public static final String TempPath           = "HASOR_TEMP_PATH";
    /**��ȡ�����ռ���ר�����ڴ����־��Ŀ¼�ռ䣬���ÿ�����config.xml�ġ�<b>environmentVar.HASOR_LOG_PATH</b>���ڵ������á�*/
    public static final String LogPath            = "HASOR_LOG_PATH";
    /**��ȡ�����ռ���ר�����ڴ��ģ��������Ϣ��Ŀ¼�ռ䣬���ÿ�����config.xml�ġ�<b>environmentVar.HASOR_PLUGIN_PATH</b>���ڵ������á�*/
    public static final String PluginPath         = "HASOR_PLUGIN_PATH";
    /**��ȡ�����ռ���ר�����ڴ��ģ��������Ϣ��Ŀ¼�ռ䣬���ÿ�����config.xml�ġ�<b>environmentVar.HASOR_PLUGIN_SETTINGS</b>���ڵ������á�*/
    public static final String PluginSettingsPath = "HASOR_PLUGIN_SETTINGS";
    //
    /**�����ַ��������ַ����ж���Ļ��������滻Ϊ��������ֵ�����������������ִ�Сд��<br/>
     * <font color="ff0000"><b>ע��</b></font>��ֻ�б��ٷֺŰ��������Ĳ��ֲű�������Ϊ������������
     * ����޷�����ĳ����������ƽ̨���׳�һ�����棬���ҽ�������������ͬ�ٷֺ���Ϊ��������ֵһ�𷵻ء�<br/>
     * <font color="00aa00"><b>����</b></font>�������ж����˱���Hasor_Home=C:/hasor��Java_Home=c:/app/java������Ľ������Ϊ
     * <div>%hasor_home%/tempDir/uploadfile.tmp&nbsp;&nbsp;--&gt;&nbsp;&nbsp;C:/hasor/tempDir/uploadfile.tmp</div>
     * <div>%JAVA_HOME%/bin/javac.exe&nbsp;&nbsp;--&gt;&nbsp;&nbsp;c:/app/java/bin/javac.exe</div>
     * <div>%work_home%/data/range.png&nbsp;&nbsp;--&gt;&nbsp;&nbsp;%work_home%/data/range.png��������һ������</div>
     * */
    public String evalString(String eval);
    /**����ָ�����ƵĻ�������ֵ.*/
    public String evalEnvVar(String varName);
    /**���ݻ����������ƻ�ȡ����������ֵ����������ڸû��������Ķ����򷵻�null.*/
    public String getEnvVar(String varName);
    /**��ӻ�����������ӵĻ�������������Ӱ�쵽ϵͳ��������������ʹ���ڲ�Map���滷�������Ӷ�����Ӱ��JVM�������С�*/
    public void addEnvVar(String varName, String value);
    /**ɾ�������������÷������ڲ�Mapɾ��������Ļ�����������������Ŀ����Ϊ�˱���Ӱ��JVM�������С�*/
    public void remoteEnvVar(String varName);
}