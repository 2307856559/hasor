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
package org.more.submit.casing.spring;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import javax.servlet.ServletContext;
import org.more.FormatException;
import org.more.submit.ActionContext;
import org.more.submit.ActionContextBuild;
import org.more.util.Config;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 * SpringBuilder�ṩ��spring2.5Ϊ������submit֧�Ż����������ṩ��һЩ��������������<br/>
 * �����ָ�������ļ�����SpringBuilder���Զ��ڵ�ǰ��·����Ѱ������ΪapplicationContext.xml�������ļ���<br/>
 * SpringBuilder��Ĺ��췽�����Զ��������Ƿ�Ϊ�����Ϊ�ջ�ʹ��Ĭ�ϲ��������ʹ��setConfig�������������ļ�λ�����������һ��null��ȥ����init�������쳣��
 * init������configFile��String���Ͷ����ʾ�����ļ�λ�á�����������ļ����ÿ����滻����SpringBuilder����ʱ���췽������������ļ�λ�á�
 * @version : 2010-8-2
 * @author ������(zyc@byshell.org)
 */
public class SpringBuilder implements ActionContextBuild {
    //========================================================================================Field
    public static final String           DefaultConfig = "applicationContext.xml";
    protected AbstractApplicationContext springContext = null;
    protected String                     config        = null;
    private File                         baseDir       = null;
    //==================================================================================Constructor
    /**����ClientMoreBuilder��ͬʱ��ʼ��ClientMoreBuilder����ʹ��Ĭ�������ļ���Ϊmore-config.xml���ļ������ڵ�ǰ·���¡�*/
    public SpringBuilder() throws Exception {
        this.config = SpringBuilder.DefaultConfig;
    };
    /**����ClientMoreBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientMoreBuilder����*/
    public SpringBuilder(String configFile) throws Exception {
        if (configFile == null || configFile.equals(""))
            this.config = SpringBuilder.DefaultConfig;
        else
            this.config = configFile;
    };
    /**����ClientMoreBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientMoreBuilder����*/
    public SpringBuilder(File configFile) throws Exception {
        if (configFile == null)
            this.config = SpringBuilder.DefaultConfig;
        else
            this.config = configFile.getAbsolutePath();
    };
    //==========================================================================================Job
    /**��ȡ�����ļ�λ�á�*/
    public String getConfig() {
        return config;
    };
    /**���������ļ�λ�ã��˴���������null��Ϊ������������initʱ��������ָ���쳣��*/
    public void setConfig(String config) {
        this.config = config;
    };
    /**��ȡSpring�����Ķ���������ø�������init���������������á�*/
    public AbstractApplicationContext getSpringContext() {
        return springContext;
    };
    /**��ȡSpring�����Ķ���������ø�������init���������������á�*/
    public void setSpringContext(AbstractApplicationContext springContext) {
        this.springContext = springContext;
    };
    /**��ʼ��springContext����*/
    public void init(Config config) throws Exception {
        if (config != null)
            //������WEB�����
            if (config.getContext() instanceof ServletContext) {
                ServletContext sc = (ServletContext) config.getContext();
                Object context = sc.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");//��ȡspring������
                if (context instanceof AbstractApplicationContext == true)
                    this.springContext = (AbstractApplicationContext) context;
            };
        //
        if (this.springContext != null)
            return;
        //
        if (config != null) {
            Object configParam = config.getInitParameter("configFile");
            if (configParam != null)
                this.setConfig(configParam.toString());
        };
        //
        File configFile = new File(this.baseDir, this.config);
        if (configFile.isAbsolute() == false) {
            URL url = ClassLoader.getSystemResource(this.config);
            if (url != null)
                configFile = new File(URLDecoder.decode(url.getFile(), "utf-8"));
        }
        //
        if (configFile.exists() == false || configFile.canRead() == false)
            throw new IOException("�����ļ�[" + configFile.getAbsolutePath() + "]�����ڣ������޷���ȡ��");
        this.springContext = new FileSystemXmlApplicationContext(configFile.getAbsolutePath());
        this.springContext.refresh();
    };
    public ActionContext getActionContext() {
        return new StringContext(this.springContext);
    }
    public void setBaseDir(File baseDir) {
        if (baseDir.isAbsolute() == false)
            throw new FormatException("baseDir������һ������·����");
        this.baseDir = baseDir;
    }
}