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
import org.more.InitializationException;
import org.more.beans.BeanFactory;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.resource.XmlFileResource;
import org.more.submit.ActionContext;
import org.more.submit.CasingBuild;
import org.more.submit.Config;
/**
 * �ṩ��beans�����Ϊ������submit3.0֧�Ż����������ṩ��һЩ��������������
 * ��ʹ��Ĭ�Ϲ��췽������ClientMoreBuilder֮�����ʹ��init�������ݲ���configFile��ָ�������ļ�λ�ôӶ���ʼ��ClientMoreBuilder��
 * <br/>Date : 2009-11-21
 * @author ������
 */
public class ClientMoreBuilder extends CasingBuild {
    //========================================================================================Field
    protected BeanFactory factory = null;
    //==================================================================================Constructor
    public ClientMoreBuilder() {}
    /**����submit3.0֧�Ż�����ʹ��ָ����more.beans��������������*/
    public ClientMoreBuilder(BeanFactory factory) {
        if (factory == null)
            throw new NullPointerException("factory��������Ϊ�ա�");
        this.factory = factory;
    }
    /**����submit3.0֧�Ż�����ʹ��ָ����more.beans�����ļ���ʼ��more.beans������*/
    public ClientMoreBuilder(String configFile) {
        this.factory = new ResourceBeanFactory(new XmlFileResource(configFile), null);
    }
    /**����submit3.0֧�Ż�����ʹ��ָ����more.beans�����ļ���ʼ��more.beans������*/
    public ClientMoreBuilder(File configFile) {
        this.factory = new ResourceBeanFactory(new XmlFileResource(configFile), null);
    }
    //==========================================================================================Job
    /**�÷�������ʹ��ClientMoreBuilder���췽����������ʱ��Ч��*/
    @Override
    public void init(Config config) {
        super.init(config);
        if (this.factory == null)
            this.factory = new ResourceBeanFactory(new XmlFileResource(config.getInitParameter("configFile")), null);
    }
    @Override
    public ActionContext getActionFactory() {
        if (factory == null)
            throw new InitializationException("û��ִ�г�ʼ��������");
        return new MoreActionContext(this.factory);
    }
}