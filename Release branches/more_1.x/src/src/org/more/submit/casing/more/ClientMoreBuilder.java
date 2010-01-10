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
import org.more.beans.BeanFactory;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.resource.AnnoXmlFileResource;
import org.more.submit.ActionContext;
import org.more.submit.CasingBuild;
import org.more.submit.Config;
/**
 * ClientMoreBuilder�ṩ��beans�����Ϊ������submit֧�Ż����������ṩ��һЩ��������������<br/>
 * �����ָ�������ļ�����ClientMoreBuilder���Զ��ڵ�ǰ·����Ѱ������Ϊmore-config.xml�������ļ���<br/>
 * init��<br/>
 * ����beanFactory���ȼ����ߣ�BeanFactory���Ͷ���<br/>
 * ����configFile���ȼ����ף�String���Ͷ���
 * @version 2010-1-5
 * @author ������ (zyc@byshell.org)
 */
public class ClientMoreBuilder extends CasingBuild {
    //========================================================================================Field
    protected BeanFactory factory = null;
    //==================================================================================Constructor
    /**����ClientMoreBuilder��ͬʱ��ʼ��ClientMoreBuilder����ʹ��Ĭ�������ļ���Ϊmore-config.xml���ļ������ڵ�ǰ·���¡�*/
    public ClientMoreBuilder() throws IOException {
        this.init(new File("more-config.xml"));
    }
    /**
     * ����ClientMoreBuilder������ͨ��isInit���������Ƿ��ʼ��ClientMoreBuilder����true��ʾ�������ҳ�ʼ��,false��ʾ��������<br/>
     * ʹ��Ĭ�������ļ���Ϊmore-config.xml���ļ������ڵ�ǰ·���¡�
     */
    public ClientMoreBuilder(boolean isInit) throws IOException {
        if (isInit == true)
            this.init(new File("more-config.xml"));
    }
    /**����ClientMoreBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientMoreBuilder����*/
    public ClientMoreBuilder(String configFile) throws IOException {
        this.init(new File(configFile));
    }
    /**����ClientMoreBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientMoreBuilder����*/
    public ClientMoreBuilder(File configFile) throws IOException {
        this.init(configFile);
    }
    /**�ṩһ�����߼��ķ�ʽ����ClientMoreBuilder���󣬸ù��췽����ʹ��ָ����BeanFactory���Ͷ�����Ϊ����ActionContext��ʹ�õ�����Դ��*/
    public ClientMoreBuilder(BeanFactory beanFactory) {
        this.init(beanFactory);
    }
    //==========================================================================================Job
    private void init(File configFile) throws IOException {
        if (configFile.exists() == false || configFile.canRead() == false)
            throw new IOException("�����ļ�[" + configFile.getAbsolutePath() + "]�����ڣ������޷���ȡ��");
        this.init(new ResourceBeanFactory(new AnnoXmlFileResource(configFile, true), null));
    }
    private void init(BeanFactory beanFactory) {
        if (beanFactory == null)
            throw new NullPointerException("BeanFactory���Ͳ�������Ϊ�ա�");
        this.factory = beanFactory;
    }
    @Override
    public void init(Config config) throws Exception {
        //����beanFactory���ȼ�����
        //����configFile���ȼ�����
        super.init(config);
        //���beanFactory������
        Object beanFactory = config.getInitParameter("beanFactory");
        boolean noFactory = (beanFactory == null || beanFactory instanceof BeanFactory == false);//���û����ȷ����noFactoryֵΪtrue��
        if (noFactory == false) {
            this.init((BeanFactory) beanFactory);
            return;
        }
        //���configFile������
        Object configFile = config.getInitParameter("configFile");
        if (configFile != null)
            this.init(new File(configFile.toString()));
    }
    @Override
    protected ActionContext createActionContext() {
        return new MoreActionContext(this.factory);
    }
}