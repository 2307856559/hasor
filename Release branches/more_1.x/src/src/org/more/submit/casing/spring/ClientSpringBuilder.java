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
import org.more.InitializationException;
import org.more.InvokeException;
import org.more.submit.ActionContext;
import org.more.submit.CasingBuild;
import org.more.submit.Config;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 * �ṩ��springΪ������submit3.0֧�Ż����������ṩ��һЩ��������������
 * ��ʹ��Ĭ�Ϲ��췽������ClientSpringBuilder֮�����ʹ��init�������ݲ���configFile��ָ�������ļ�λ�ôӶ���ʼ��ClientSpringBuilder��
 * <br/>Date : 2009-11-21
 * @author ������
 */
public class ClientSpringBuilder extends CasingBuild {
    //========================================================================================Field
    protected AbstractApplicationContext springContext = null;
    //==================================================================================Constructor
    public ClientSpringBuilder() {}
    /**ͨ��һ��SpringContext���󴴽�SpringCasingBuilder��*/
    public ClientSpringBuilder(AbstractApplicationContext springContext) {
        if (springContext == null)
            throw new NullPointerException("springContext��������Ϊ�ա�");
        this.springContext = springContext;
    }
    /**ͨ��һ��SpringContext���󴴽�SpringCasingBuilder��*/
    public ClientSpringBuilder(String configLocation) {
        this.springContext = new FileSystemXmlApplicationContext(configLocation);
        if (this.springContext == null)
            throw new InvokeException("�޷�����FileSystemXmlApplicationContext�������������ļ�λ�á�");
    }
    //==========================================================================================Job
    /**�÷�������ʹ��ClientMoreBuilder���췽����������ʱ��Ч��*/
    @Override
    public void init(Config config) {
        super.init(config);
        if (this.springContext == null)
            this.springContext = new FileSystemXmlApplicationContext(config.getInitParameter("configFile"));
    }
    @Override
    public ActionContext getActionFactory() {
        if (springContext == null)
            throw new InitializationException("û��ִ�г�ʼ��������");
        return new SpringActionContext(this.springContext);
    }
}