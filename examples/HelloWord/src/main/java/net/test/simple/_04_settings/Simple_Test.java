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
package net.test.simple._04_settings;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;
import net.hasor.core.Hasor;
import net.hasor.core.Settings;
import net.hasor.core.setting.InputStreamSettings;
import org.junit.Test;
import org.more.util.ResourcesUtils;
/**
 * Settings �ӿڹ��ܲ��ԡ�
 * @version : 2013-7-16
 * @author ������ (zyc@hasor.net)
 */
public class Simple_Test {
    /*��ȡ Xml ��������Ϣ����*/
    @Test
    public void test_Simple() throws IOException, URISyntaxException {
        System.out.println("--->>test_Simple<<--");
        InputStream inStream = ResourcesUtils.getResourceAsStream("net/test/simple/_04_settings/simple-config.xml");
        InputStreamSettings settings = new InputStreamSettings(inStream);
        settings.loadSettings();//װ�������ļ�
        //
        String myName = settings.getString("mySelf.myName");
        Hasor.logInfo("my Name is %s.", myName);
        //
        int myAge = settings.getInteger("mySelf.myAge");
        Hasor.logInfo("my Age is %s.", myAge);
        //
        Date myBirthday = settings.getDate("mySelf.myBirthday");
        Hasor.logInfo("my Birthday is %s.", myBirthday);//TODO ��Ҫ���ͨ�ø�ʽת������
        //
        String myWork = settings.getString("mySelf.myWork");
        Hasor.logInfo("my Work is %s.", myWork);
        //
        String myProjectURL = settings.getString("mySelf.myProjectURL");
        Hasor.logInfo("my Project is %s.", myProjectURL);
    }
    /*���� Xml �����д��ڶ�������ռ�����£��ֱ��ȡ��ͬ�����ռ����ͬ���ã���ͬһ��Xml �ļ�����*/
    @Test
    public void test_all_in_one() throws IOException {
        System.out.println("--->>test_all_in_one<<--");
        InputStream inStream = ResourcesUtils.getResourceAsStream("net/test/simple/_04_settings/ns-all-in-one-config.xml");
        InputStreamSettings settings = new InputStreamSettings(inStream);
        settings.loadSettings();//װ�������ļ�
        //
        Settings ns1_settings = settings.getSettings("http://mode1.myProject.net");
        Settings ns2_settings = settings.getSettings("http://mode2.myProject.net");
        //
        String ns1_local = ns1_settings.getString("appSettings.serverLocal.url");
        String ns2_local = ns2_settings.getString("appSettings.serverLocal.url");
        String[] all_local = settings.getStringArray("appSettings.serverLocal.url");
        //
        Hasor.logInfo("ns1 is %s.", ns1_local);
        Hasor.logInfo("ns2 is %s.", ns2_local);
        Hasor.logInfo("ns is %s.", (Object) all_local);//ͬʱȡ��ȫ�������ռ��µ���ͬ���ýڵ�������Ϣ��
    }
    /*���� Xml �����д��ڶ�������ռ�����£��ֱ��ȡ��ͬ�����ռ����ͬ���ã��ڲ�ͬ Xml �ļ�����*/
    @Test
    public void test_mergeNS() throws IOException {
        System.out.println("--->>test_mergeNS<<--");
        InputStream ns1_inStream = ResourcesUtils.getResourceAsStream("net/test/simple/_04_settings/ns1-config.xml");
        InputStream ns2_inStream = ResourcesUtils.getResourceAsStream("net/test/simple/_04_settings/ns2-config.xml");
        InputStreamSettings settings = new InputStreamSettings(new InputStream[] { ns1_inStream, ns2_inStream });
        settings.loadSettings();//װ�������ļ�
        //
        Settings ns1_settings = settings.getSettings("http://mode1.myProject.net");
        Settings ns2_settings = settings.getSettings("http://mode2.myProject.net");
        //
        String ns1_local = ns1_settings.getString("appSettings.serverLocal.url");
        String ns2_local = ns2_settings.getString("appSettings.serverLocal.url");
        String[] all_local = settings.getStringArray("appSettings.serverLocal.url");//ͬʱȡ��ȫ�������ռ��µ���ͬ���ýڵ�������Ϣ��
        //
        Hasor.logInfo("ns1 is %s.", ns1_local);
        Hasor.logInfo("ns2 is %s.", ns2_local);
        Hasor.logInfo("ns is %s.", (Object) all_local);
    }
}