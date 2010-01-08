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
import java.util.Enumeration;
import javax.servlet.ServletContext;
import org.more.InvokeException;
import org.more.submit.Config;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 * WebSpringBuilder����չ��ClientSpringBuilder�ṩ��web��֧�֣�������ʹ�õ�configFile������һ�������վ���web���·����<br/>
 * ���û��ָ��configFile��������configFileĬ�Ͻ���ʾΪ��/WEB-INF/applicationContext.xml�������ļ���<br/>
 * ���ȣ����������spring�ļ�����WebSpringBuilder����Զ���ServletContext�в���AbstractApplicationContext���Ͷ���<br/>
 * ��Σ����������beanFactory������ʹ��beanFactory������ָ����AbstractApplicationContext���Ͷ���
 * Ȼ�󣬻����configFile����������װ��Spring�����ļ���
 * @version 2010-1-5
 * @author ������ (zyc@byshell.org)
 */
public class WebSpringBuilder extends ClientSpringBuilder implements Config {
    public static final String Default_ConfigXML = "/WEB-INF/applicationContext.xml";
    //==================================================================================Constructor
    /** WebSpringBuilder����չ��ClientSpringBuilder�ṩ��web��֧�֣����ཫconfigFile��������ʾ��·����web���·��ת��Ϊ����·����*/
    public WebSpringBuilder() throws IOException {
        super(false);
    }
    //==========================================================================================Abs
    /**�����ʼ�����*/
    @Override
    public void init(Config config) throws Exception {
        //���ȼ���spring������->beanFactory����->configFile����
        System.out.println("init WebSpringBuilder...");
        ServletContext sc = (ServletContext) config.getContext();
        this.springContext = this.getSpringContext(sc);
        if (this.springContext != null) {
            this.config = config;
            return;
        } else
            super.init(this.getWebSpringBuilderConfig(config));
        System.out.println("init WebSpringBuilder OK");
    }
    /**����XmlWebApplicationContext����*/
    @Override
    protected void init(File configFile) throws IOException {
        if (configFile.exists() == false || configFile.canRead() == false)
            throw new IOException("�����ļ�[" + configFile.getAbsolutePath() + "]�����ڣ������޷���ȡ��");
        FileSystemXmlApplicationContext webApp = new FileSystemXmlApplicationContext();
        //webApp.setServletContext((ServletContext) this.config.getContext());
        webApp.setConfigLocation(configFile.getAbsolutePath());
        webApp.refresh();
        this.springContext = webApp;
    }
    /**�����ServletContext�л�ȡSpring�������� */
    private AbstractApplicationContext getSpringContext(ServletContext sc) {
        //��ȡspring������
        Object context = sc.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
        if (context == null)
            return null;
        else if (context instanceof AbstractApplicationContext == false)
            throw new InvokeException("�޷���ȡorg.springframework.web.context.WebApplicationContext.ROOT���Զ��󣬸����Զ���û�м̳�spring��AbstractApplicationContext������");
        return (AbstractApplicationContext) context;
    }
    //=========================================================================================Impl
    private Config propxyConfig;
    protected Config getWebSpringBuilderConfig(Config config) {
        this.propxyConfig = config;
        return this;
    }
    /**������beanFactory��configFile����*/
    @Override
    public Object getInitParameter(String name) {
        if ("configFile".equals(name) == true) {
            ServletContext sc = (ServletContext) this.propxyConfig.getContext();
            Object obj = this.propxyConfig.getInitParameter(name);
            if (obj == null)
                return sc.getRealPath(WebSpringBuilder.Default_ConfigXML);
            else
                return sc.getRealPath(obj.toString());
        } else
            return this.propxyConfig.getInitParameter(name);
    }
    @Override
    public Object getContext() {
        return this.propxyConfig.getContext();
    }
    @Override
    public Enumeration<String> getInitParameterNames() {
        return this.propxyConfig.getInitParameterNames();
    }
}