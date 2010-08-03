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
package org.more.submit.casing.more;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import org.more.beans.BeanFactory;
import org.more.beans.core.ContextFactory;
import org.more.submit.ActionContext;
import org.more.submit.ActionContextBuild;
import org.more.util.Config;
/**
 * MoreBuilder�ṩ��beans�����Ϊ������submit֧�Ż����������ṩ��һЩ��������������<br/>
 * �����ָ�������ļ�����MoreBuilder���Զ��ڵ�ǰ��·����Ѱ������Ϊmore-config.xml�������ļ���<br/>
 * MoreBuilder��Ĺ��췽�����Զ��������Ƿ�Ϊ�����Ϊ�ջ�ʹ��Ĭ�ϲ��������ʹ��setConfig�������������ļ�λ�����������һ��null��ȥ����init�������쳣��
 * init������configFile��String���Ͷ����ʾ�����ļ�λ�á�����������ļ����ÿ����滻����MoreBuilder����ʱ���췽������������ļ�λ�á�
 * @version : 2010-8-2
 * @author ������(zyc@byshell.org)
 */
public class MoreBuilder implements ActionContextBuild {
    //========================================================================================Field
    public static final String DefaultConfig = "more-config.xml";
    protected BeanFactory      factory       = null;
    protected String           config        = null;
    //==================================================================================Constructor
    /**����MoreBuilder��ʹ��Ĭ�������ļ�{@link MoreBuilder#DefaultConfig}��*/
    public MoreBuilder() throws Exception {
        this.config = MoreBuilder.DefaultConfig;
    };
    /**����MoreBuilder��ʹ��ָ�������ļ������ָ�������ļ�Ϊ����ʹ��Ĭ�������ļ�{@link MoreBuilder#DefaultConfig}��*/
    public MoreBuilder(String configFile) throws Exception {
        if (configFile == null || configFile.equals(""))
            this.config = MoreBuilder.DefaultConfig;
        else
            this.config = configFile;
    };
    /**����ClientMoreBuilder�����ָ�������ļ�Ϊ����ʹ��Ĭ�������ļ�{@link MoreBuilder#DefaultConfig}��*/
    public MoreBuilder(File configFile) throws Exception {
        if (configFile == null)
            this.config = MoreBuilder.DefaultConfig;
        else
            this.config = configFile.getAbsolutePath();
    };
    //==========================================================================================Job
    public void init(Config config) throws Exception {
        if (this.factory != null)
            return;
        if (config != null) {
            Object configParam = config.getInitParameter("configFile");
            if (configParam != null)
                this.setConfig(configParam.toString());
        };
        //
        File configFile = new File(this.config);
        if (configFile.isAbsolute() == false) {
            URL url = Thread.currentThread().getContextClassLoader().getResource(this.config);
            if (url != null)
                configFile = new File(URLDecoder.decode(url.getFile(), "utf-8"));
        }
        //
        if (configFile.exists() == false || configFile.canRead() == false)
            throw new IOException("�����ļ�[" + configFile.getAbsolutePath() + "]�����ڣ������޷���ȡ��");
        factory = ContextFactory.create(configFile);//ʹ��ResourceFactory����һ��BeanResource
        factory.init();
    };
    /**��ȡ�����ļ�λ�á�*/
    public String getConfig() {
        return config;
    };
    /**���������ļ�λ�ã��˴���������null��Ϊ������������initʱ��������ָ���쳣��*/
    public void setConfig(String config) {
        this.config = config;
    };
    @Override
    public ActionContext getActionContext() {
        return new MoreContext(this.factory);
    };
};