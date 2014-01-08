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
package net.hasor.core.setting;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.hasor.core.Hasor;
import org.more.util.ResourcesUtils;
/**
 * �̳���FileSettings���࣬�����Զ�װ��Classpath�����о�̬�����ļ���
 * �����Զ�װ���������ļ����������ļ�Ӧ��ֻ��һ������
 * @version : 2013-9-9
 * @author ������(zyc@hasor.net)
 */
public class StandardContextSettings extends InputStreamSettings {
    /**Ĭ�Ͼ�̬�����ļ�����*/
    public static final String StaticSettingName = "static-config.xml";
    private URI                settingURI;
    //
    /**����{@link StandardContextSettings}���Ͷ���*/
    public StandardContextSettings(String mainSettings) throws IOException, URISyntaxException {
        URL url = ResourcesUtils.getResource(mainSettings);
        this.settingURI = url.toURI();
        this.refresh();
    }
    /**����{@link StandardContextSettings}���Ͷ���*/
    public StandardContextSettings(File mainSettings) throws IOException {
        this.settingURI = mainSettings.toURI();
        this.refresh();
    }
    /**����{@link StandardContextSettings}���Ͷ���*/
    public StandardContextSettings(URI mainSettings) throws IOException {
        this.settingURI = mainSettings;
        this.refresh();
    }
    /**��ȡ�����ļ�URI*/
    public URI getSettingURI() {
        return settingURI;
    }
    //
    @Override
    protected void readyLoad() throws IOException {
        super.readyLoad();
        //1.װ������static-config.xml
        List<URL> streamList = ResourcesUtils.getResources(StaticSettingName);
        //2.����ȷ��λ��jar���е���Դ�������ȼ�����
        //��Ϊ���Ǽ�����˳��ģ�����ȼ���λ��jar�е���Դ��Ȼ�󸲸����ǡ�
        Collections.sort(streamList, new Comparator<URL>() {
            public int compare(URL o1, URL o2) {
                String o1p = o1.getProtocol();
                String o2p = o2.getProtocol();
                if (o1p.equals(o2p))
                    return 0;
                if (o1p.equals("jar"))
                    return -1;
                if (o2p.equals("jar"))
                    return 1;
                return 0;
            }
        });
        if (streamList != null) {
            for (URL resURL : streamList) {
                InputStream stream = ResourcesUtils.getResourceAsStream(resURL);
                Hasor.logInfo("load ��%s��", resURL);
                this.addStream(stream);
            }
        }
        //2.װ��hasor-config.xml
        if (this.settingURI != null) {
            InputStream stream = ResourcesUtils.getResourceAsStream(this.settingURI);
            Hasor.logInfo("load ��%s��", this.settingURI);
            this.addStream(stream);
        }
    }
    @Override
    public void refresh() throws IOException {
        Hasor.logInfo("reload configuration.");
        this.cleanData();
        //
        try {
            this.loadSettings();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}