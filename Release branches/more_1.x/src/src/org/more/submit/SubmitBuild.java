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
package org.more.submit;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import org.more.submit.ext.filter.FilterDecorator;
import org.more.submit.web.WebSubmitContext;
import org.more.submit.web.WebSubmitContextImpl;
import org.more.util.AttributeConfigBridge;
import org.more.util.Config;
import org.more.util.attribute.AttBase;
/**
 * submit����buildģʽ����{@link SubmitContext}�������build��WebSubmitContext��SubmitContext���ֽӿڡ�
 * @version 2010-9-5
 * @author ������ (zyc@byshell.org)
 */
public class SubmitBuild extends AttBase {
    //========================================================================================Field
    private static final long                                  serialVersionUID    = 2922698361958221493L;
    private SubmitContext                                      result              = null;                                                    //���֮�����ɵ�SubmitContext����
    private ArrayList<Class<? extends ActionContextDecorator>> actionDecoratorList = new ArrayList<Class<? extends ActionContextDecorator>>();
    private ArrayList<Class<? extends SubmitContextDecorator>> submitDecoratorList = new ArrayList<Class<? extends SubmitContextDecorator>>();
    //==========================================================================================Job
    public SubmitBuild() {
        this.actionDecoratorList.add(FilterDecorator.class);
    };
    /**��config�Ĳ�����ӵ�SubmitBuild�Ļ����С�*/
    public void setConfig(Config config) {
        Enumeration<String> e = config.getInitParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            this.setAttribute(name, config.getInitParameter(name));
        }
    };
    /**��������������SubmitContext�������ɵ�SubmitContext�Ķ��������Ҫͨ��getResult������ȡ��*/
    public SubmitContext build(ActionContextBuild build) throws Exception {
        if (this.contains("baseDir") == true)
            build.setBaseDir(new File((String) this.getAttribute("baseDir")));
        //
        build.init(new AttributeConfigBridge(this, null));
        ActionContext actionContext = this.decorator(build.getActionContext());
        this.result = new SubmitContextImpl(actionContext);
        this.result.setSessionManager(new SimpleSessionManager());
        this.result = this.decorator(this.result);
        return this.result;
    };
    /**
     * ��������������SubmitContext�������ɵ�SubmitContext�Ķ��������Ҫͨ��getResult������ȡ��
     * @param context ����SubmitContext����ʱ��Ҫ�õ�����������
     */
    public WebSubmitContext buildWeb(ActionContextBuild build, ServletContext context) throws Exception {
        File baseDir = null;
        if (this.contains("baseDir") == false)
            baseDir = new File(context.getRealPath("/WEB-INF/classes"));
        else
            baseDir = new File((String) this.getAttribute("baseDir"));
        //
        build.setBaseDir(baseDir);
        System.out.println("buildWeb More WebContext BaseDir=" + baseDir.getAbsolutePath());
        build.init(new AttributeConfigBridge(this, context));
        ActionContext actionContext = this.decorator(build.getActionContext());
        WebSubmitContextImpl webContext = new WebSubmitContextImpl(actionContext, context);
        Object protocol = this.getAttribute("protocol");// �������Э����
        if (protocol != null)
            webContext.setProtocol(protocol.toString());
        //
        this.result = webContext;
        this.result.setSessionManager(new SimpleSessionManager());
        this.result = this.decorator(this.result);
        return (WebSubmitContext) this.result;
    };
    /**װ��ActionContextװ������*/
    private ActionContext decorator(ActionContext ac) throws InstantiationException, IllegalAccessException {
        ActionContext context = ac;
        if (actionDecoratorList.isEmpty() == true)
            return context;
        for (Class<? extends ActionContextDecorator> decorator : this.actionDecoratorList) {
            ActionContextDecorator acd = decorator.newInstance();
            if (acd.initDecorator(context) == true)
                context = acd;
        }
        return context;
    };
    /**װ��SubmitContextװ������*/
    private SubmitContext decorator(SubmitContext sc) throws InstantiationException, IllegalAccessException {
        SubmitContext context = sc;
        if (submitDecoratorList.isEmpty() == true)
            return context;
        for (Class<? extends SubmitContextDecorator> decorator : this.submitDecoratorList) {
            SubmitContextDecorator acd = decorator.newInstance();
            if (acd.initDecorator(context) == true)
                context = acd;
        }
        return context;
    };
    /**
     * ��ȡ���֮�����ɵ�SubmitContext����
     * @return �������֮�����ɵ�SubmitContext����
     */
    public SubmitContext getResult() {
        return result;
    };
};