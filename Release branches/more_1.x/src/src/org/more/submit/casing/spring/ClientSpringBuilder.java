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
import org.more.submit.ActionContext;
import org.more.submit.CasingBuild;
import org.more.submit.Config;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 * ClientSpringBuilder�ṩ��beans�����Ϊ������submit֧�Ż����������ṩ��һЩ��������������<br/>
 * �����ָ�������ļ�����ClientSpringBuilder���Զ��ڵ�ǰ·����Ѱ������ΪapplicationContext.xml�������ļ���<br/>
 * init��<br/>
 * ����beanFactory���ȼ����ߣ�AbstractApplicationContext���Ͷ���<br/>
 * ����configFile���ȼ����ף�String���Ͷ���
 * <br/>Date : 2009-11-21
 * @author ������
 */
public class ClientSpringBuilder extends CasingBuild {
    //========================================================================================Field
    protected AbstractApplicationContext springContext = null;
    //==================================================================================Constructor
    /**����ClientSpringBuilder��ͬʱ��ʼ��ClientSpringBuilder����ʹ��Ĭ�������ļ���ΪapplicationContext.xml���ļ������ڵ�ǰ·���¡�*/
    public ClientSpringBuilder() throws IOException {
        this.init(new File("applicationContext.xml"));
    }
    /**
     * ����ClientSpringBuilder������ͨ��isInit���������Ƿ��ʼ��ClientSpringBuilder����true��ʾ�������ҳ�ʼ��,false��ʾ��������<br/>
     * ʹ��Ĭ�������ļ���ΪapplicationContext.xml���ļ������ڵ�ǰ·���¡�
     */
    public ClientSpringBuilder(boolean isInit) throws IOException {
        if (isInit == true)
            this.init(new File("applicationContext.xml"));
    }
    /**����ClientSpringBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientSpringBuilder����*/
    public ClientSpringBuilder(String configFile) throws IOException {
        this.init(new File(configFile));
    }
    /**����ClientSpringBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientSpringBuilder����*/
    public ClientSpringBuilder(File configFile) throws IOException {
        this.init(configFile);
    }
    /**�ṩһ�����߼��ķ�ʽ����ClientSpringBuilder���󣬸ù��췽����ʹ��ָ����AbstractApplicationContext���Ͷ�����Ϊ����ActionContext��ʹ�õ�����Դ��*/
    public ClientSpringBuilder(AbstractApplicationContext springContext) {
        this.init(springContext);
    }
    //==========================================================================================Job
    protected void init(File configFile) throws IOException {
        if (configFile.exists() == false || configFile.canRead() == false)
            throw new IOException("�����ļ�[" + configFile.getAbsolutePath() + "]�����ڣ������޷���ȡ��");
        this.springContext = new FileSystemXmlApplicationContext(configFile.getAbsolutePath());
    }
    private void init(AbstractApplicationContext springContext) {
        if (springContext == null)
            throw new NullPointerException("springContext����Ϊ�ա�");
        this.springContext = springContext;
    }
    /**�÷�������ʹ��ClientSpringBuilder���췽����������ʱ��Ч��*/
    @Override
    public void init(Config config) throws Exception {
        //����beanFactory���ȼ�����
        //����configFile���ȼ�����
        super.init(config);
        //���beanFactory������
        Object beanFactory = config.getInitParameter("beanFactory");
        boolean noFactory = (beanFactory == null || beanFactory instanceof AbstractApplicationContext == false);//���û����ȷ����noFactoryֵΪtrue��
        if (noFactory == false) {
            this.init((AbstractApplicationContext) beanFactory);
            return;
        }
        //���configFile������
        Object configFile = config.getInitParameter("configFile");
        if (configFile != null)
            this.init(new File(configFile.toString()));
    }
    @Override
    protected ActionContext createActionContext() {
        return new SpringActionContext(this.springContext);
    }
}