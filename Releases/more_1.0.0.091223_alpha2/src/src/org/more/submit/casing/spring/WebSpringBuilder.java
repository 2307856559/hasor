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
import javax.servlet.ServletContext;
import org.more.InvokeException;
import org.more.submit.Config;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 * WebSpringBuilder����չ��ClientSpringBuilder�ṩ��web��֧�֣�init��������configFile����WebSpringBuilder��
 * ���ȴ�ServletContext�в���Springʹ�ü�����װ�ص�Spring�����ģ�����Ҳ�����ʹ��FileSystemXmlApplicationContext��������
 * ͬʱ����configFile�ر�ת��Ϊ���վ��Ŀ¼��
 * <br/>Date : 2009-12-2
 * @author ������
 */
public class WebSpringBuilder extends ClientSpringBuilder {
    //==================================================================================Constructor
    /** ʹ��Web��ʽ����More��ActionManager������ڹ��췽����û��ָ���κβ�����ActionManager�ڳ�ʼ��ʱ����ù�init������ȡ��SpringContext */
    public WebSpringBuilder() {}
    /** �ù��췽������ʹ������Աͨ��ָ��ServletContext����ȡSpringContext������ڸù��췽���л�ȡ����SpringContext����init����ʱ�����Ի�ȡSpringContext�Ĺ��̡�����÷�����û�л�ȡ��SpringContext�������InvokeException�쳣*/
    public WebSpringBuilder(ServletContext sc) {
        this.springContext = this.getSpringContext(sc);//��ȡspring������,�÷�����ȷ����ȡ�����������ȡ�����������ʹ��󽫻������쳣��
    }
    //==========================================================================================Job
    /**���ȴ�ServletContext�в���Springʹ�ü�����װ�ص�Spring�����ģ�����Ҳ�����ʹ��FileSystemXmlApplicationContext��������ͬʱ����configFile�ر�ת��Ϊ���վ��Ŀ¼��*/
    @Override
    public void init(Config config) throws InvokeException {
        this.config = config;
        System.out.println("init WebMoreBuilder...");
        ServletContext sc = (ServletContext) this.getConfig().getContext();
        if (this.springContext == null)
            this.springContext = this.getSpringContext(sc);//�÷�����ȷ����ȡ�����������ȡ�����������ʹ��󽫻������쳣��
        if (this.springContext == null) {
            String configFile = sc.getRealPath(config.getInitParameter("configFile"));
            this.springContext = new FileSystemXmlApplicationContext(configFile);
        }
        System.out.println("init WebMoreBuilder OK");
    }
    /** �÷��������ServletContext�л�ȡSpring�������� */
    private AbstractApplicationContext getSpringContext(ServletContext sc) {
        //��ȡspring������
        Object context = sc.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
        if (context == null)
            return null;
        else if (context instanceof AbstractApplicationContext == false)
            throw new InvokeException("�޷���ȡorg.springframework.web.context.WebApplicationContext.ROOT���Զ��󣬸����Զ���û�м̳�spring��AbstractApplicationContext������");
        return (AbstractApplicationContext) context;
    }
}