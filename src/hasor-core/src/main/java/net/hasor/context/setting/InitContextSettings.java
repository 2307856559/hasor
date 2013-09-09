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
package net.hasor.context.setting;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import net.hasor.Hasor;
import org.more.util.ResourcesUtils;
/**
 * �̳���FileSettings���࣬�����Զ�װ��Classpath�����о�̬�����ļ���
 * �����Զ�װ���������ļ����������ļ�Ӧ��ֻ��һ������
 * @version : 2013-9-9
 * @author ������(zyc@hasor.net)
 */
public class InitContextSettings extends FileSettings {
    /**Ĭ���������ļ�����*/
    public static final String MainConfigName   = "hasor-config.xml";
    /**Ĭ�Ͼ�̬�����ļ�����*/
    public static final String StaticConfigName = "static-config.xml";
    private URI                mainConfig;
    //
    //
    /**����{@link InitContextSettings}���Ͷ���*/
    public InitContextSettings() throws IOException, XMLStreamException {
        this(MainConfigName);
    }
    /**����{@link InitContextSettings}���Ͷ���*/
    public InitContextSettings(String mainConfig) throws IOException, XMLStreamException {
        super();
        Hasor.assertIsNotNull(mainConfig);
        File configFile = new File(mainConfig);
        if (configFile.exists() == false || configFile.canRead() == false || configFile.isDirectory() == true)
            return;
        this.mainConfig = configFile.toURI();
        this.refresh();
    }
    /**����{@link InitContextSettings}���Ͷ���*/
    public InitContextSettings(File mainConfig) throws IOException, XMLStreamException {
        this((mainConfig == null) ? null : mainConfig.toURI());
    }
    /**����{@link InitContextSettings}���Ͷ���*/
    public InitContextSettings(URI mainConfig) throws IOException, XMLStreamException {
        super();
        Hasor.assertIsNotNull(mainConfig);
        this.mainConfig = mainConfig;
        this.refresh();
    }
    //
    @Override
    protected void readyLoad() throws IOException {
        super.readyLoad();
        //1.װ������static-config.xml
        List<URL> streamList = ResourcesUtils.getResources(StaticConfigName);
        if (streamList != null) {
            for (URL resURL : streamList) {
                InputStream stream = ResourcesUtils.getResourceAsStream(resURL);
                Hasor.info("load ��%s��", resURL);
                this.addStream(stream);
            }
        }
        //2.װ������static-config.xml
        if (this.mainConfig != null) {
            InputStream stream = ResourcesUtils.getResourceAsStream(this.mainConfig);
            Hasor.info("load ��%s��", this.mainConfig);
            this.addStream(stream);
        } else
            Hasor.warning("cannot load the root configuration file ��%s��", mainConfig);
    }
}