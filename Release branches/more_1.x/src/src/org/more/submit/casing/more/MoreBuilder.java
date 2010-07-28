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
import org.more.beans.core.ContextFactory;
import org.more.submit.ActionContext;
import org.more.submit.ActionContextBuild;
import org.more.util.Config;
/**
 * ClientMoreBuilder�ṩ��beans�����Ϊ������submit֧�Ż����������ṩ��һЩ��������������<br/>
 * �����ָ�������ļ�����ClientMoreBuilder���Զ��ڵ�ǰ·����Ѱ������Ϊmore-config.xml�������ļ���<br/>
 * init��<br/>
 * ����beanFactory���ȼ����ߣ�BeanFactory���Ͷ���<br/>
 * ����configFile���ȼ����ף�String���Ͷ���
 * @version 2010-1-5
 * @author ������ (zyc@byshell.org)
 */
public class MoreBuilder extends ActionContextBuild {
    //========================================================================================Field
    public static final File DefaultConfig = new File("more-config.xml");
    protected BeanFactory    factory       = null;
    protected File           config        = null;
    //==================================================================================Constructor
    /**����ClientMoreBuilder��ͬʱ��ʼ��ClientMoreBuilder����ʹ��Ĭ�������ļ���Ϊmore-config.xml���ļ������ڵ�ǰ·���¡�*/
    public MoreBuilder() throws Exception {
        this.config = MoreBuilder.DefaultConfig;
    };
    /**����ClientMoreBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientMoreBuilder����*/
    public MoreBuilder(String configFile) throws Exception {
        this.config = new File(configFile);
    };
    /**����ClientMoreBuilder��ͨ��configFile����������ʹ���Ǹ������ļ���ʼ��ClientMoreBuilder����*/
    public MoreBuilder(File configFile) throws Exception {
        if (configFile == null)
            this.config = MoreBuilder.DefaultConfig;
        else
            this.config = configFile;
    };
    /**�ṩһ�����߼��ķ�ʽ����ClientMoreBuilder���󣬸ù��췽����ʹ��ָ����BeanFactory���Ͷ�����Ϊ����ActionContext��ʹ�õ�����Դ��*/
    public MoreBuilder(BeanFactory beanFactory) {
        this.factory = beanFactory;
    };
    //==========================================================================================Job
    public void init(Config config) throws Exception {
        if (this.factory != null)
            return;
        if (this.config.exists() == false || this.config.canRead() == false)
            throw new IOException("�����ļ�[" + this.config.getAbsolutePath() + "]�����ڣ������޷���ȡ��");
        factory = ContextFactory.create(this.config);//ʹ��ResourceFactory����һ��BeanResource
        factory.init();
    };
    @Override
    public ActionContext getActionContext() {
        return new MoreContext(this.factory);
    }
}