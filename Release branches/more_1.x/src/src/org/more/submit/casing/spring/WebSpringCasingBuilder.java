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
import org.more.submit.ActionFactory;
import org.more.submit.CasingBuild;
import org.more.submit.Config;
import org.more.submit.FilterFactory;
import org.springframework.context.support.AbstractApplicationContext;
/**
 * ��������Spring��listenerʱ����Ҫʹ��WebSpringCasingBuilder���м���spring��submit�С�
 * ע��WebSpringCasingBuilder������˳��һ��Ҫ��spring��listener֮������������More����ȡ����SpringContext��
 * Date : 2009-6-30
 * @author ������
 */
public class WebSpringCasingBuilder extends CasingBuild {
    private SpringActionFactory        saf           = null;
    private SpringFilterFactory        sff           = null;
    private AbstractApplicationContext springContext = null;
    /** ʹ��Web��ʽ����More��ActionManager������ڹ��췽����û��ָ���κβ�����ActionManager�ڳ�ʼ��ʱ����ù�init������ȡ��SpringContext */
    public WebSpringCasingBuilder() {}
    /**
     * �ù��췽������ʹ������Աͨ��ָ��ServletContext����ȡSpringContext������ڸù��췽���л�ȡ����SpringContext����init����ʱ�����Ի�ȡ
     * SpringContext�Ĺ��̡�����÷�����û�л�ȡ��SpringContext�������InvokeException�쳣
     * @param sc �����ServletContext����
     * @throws InvokeException ���û�л�ȡ��SpringContextʱ������������쳣��
     */
    public WebSpringCasingBuilder(ServletContext sc) throws InvokeException {
        //��ȡspring������,�÷�����ȷ����ȡ�����������ȡ�����������ʹ��󽫻������쳣��
        this.springContext = this.getSpringContext(sc);
    }
    @Override
    public void init(Config config) throws InvokeException {
        super.init(config);
        if (this.springContext == null) {
            ServletContext sc = (ServletContext) this.getConfig().getContext();
            this.springContext = this.getSpringContext(sc);//�÷�����ȷ����ȡ�����������ȡ�����������ʹ��󽫻������쳣��
        }
        //��������
        saf = new SpringActionFactory((AbstractApplicationContext) springContext);//Spring��action����
        sff = new SpringFilterFactory((AbstractApplicationContext) springContext);//Spring��filter����
    }
    /** �÷��������ServletContext�л�ȡSpring�������� */
    private AbstractApplicationContext getSpringContext(ServletContext sc) {
        //��ȡspring������
        Object context = sc.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
        if (context == null || context instanceof AbstractApplicationContext == false)
            throw new InvokeException("�޷���ȡorg.springframework.web.context.WebApplicationContext.ROOT���Զ��󣬸����Զ������Ϊ�ջ���û�м̳�spring��AbstractApplicationContext������");
        return (AbstractApplicationContext) context;
    }
    @Override
    public ActionFactory getActionFactory() {
        return this.saf;
    }
    @Override
    public FilterFactory getFilterFactory() {
        return this.sff;
    }
}