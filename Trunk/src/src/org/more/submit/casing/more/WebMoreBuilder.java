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
import javax.servlet.ServletContext;
import org.more.InvokeException;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.resource.XmlFileResource;
import org.more.submit.Config;
/**
 * WebMoreBuilder����չ��ClientMoreBuilder�ṩ��web��֧�֣�init��������configFile����WebMoreBuilder����Զ�ʹ��
 * ServletContext����ת����·��Ȼ�󴴽�ResourceBeanFactory����ɳ�ʼ����
 * <br/>Date : 2009-12-2
 * @author ������
 */
public class WebMoreBuilder extends ClientMoreBuilder {
    //==================================================================================Constructor
    /** ʹ��Web��ʽ����More��ActionManager������ڹ��췽����û��ָ���κβ�����ActionManager�ڳ�ʼ��ʱ����ù�init������ȡ��SpringContext */
    public WebMoreBuilder() {}
    //==========================================================================================Job
    @Override
    public void init(Config config) throws InvokeException {
        this.config = config;
        System.out.println("init WebMoreBuilder...");
        ServletContext sc = (ServletContext) this.getConfig().getContext();
        String configFile = sc.getRealPath(config.getInitParameter("configFile"));
        this.factory = new ResourceBeanFactory(new XmlFileResource(configFile), null); //��������
        System.out.println("init WebMoreBuilder OK");
    }
}