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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.more.submit.impl.DefaultSubmitService;
import org.more.util.AttributeConfigBridge;
import org.more.util.Config;
import org.more.util.attribute.AttBase;
/**
 * submit����buildģʽ����{@link SubmitService}��
 * @version : 2011-7-15
 * @author ������ (zyc@byshell.org)
 */
public class SubmitBuild extends AttBase {
    //========================================================================================Field
    private static final long          serialVersionUID = 2922698361958221493L;
    private Object                     context          = null;
    private String                     defaultNS        = null;
    private List<ActionContextBuilder> config_acb       = new ArrayList<ActionContextBuilder>();
    //==========================================================================================Job
    public SubmitBuild() {};
    public SubmitBuild(Object context) {
        this.context = context;
    };
    /**��config�Ĳ�����ӵ�SubmitBuild�Ļ����У�config���������ָ����Context�����滻���췽�������Context.*/
    public void setConfig(Config<?> config) {
        Enumeration<String> e = config.getInitParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            this.setAttribute(name, config.getInitParameter(name));
        }
        this.context = config.getContext();
    };
    public void setContext(Object context) {
        this.context = context;
    };
    /**��������������SubmitContext�������ɵ�SubmitContext�Ķ��������Ҫͨ��getResult������ȡ��*/
    public SubmitService build() throws Throwable {
        //1.����SubmitContextʵ����        
        SubmitService service = null;
        Object serviceImplType = this.getAttribute(SubmitService.class.getName());
        if (serviceImplType != null)
            service = (SubmitService) Thread.currentThread().getContextClassLoader().loadClass(serviceImplType.toString()).newInstance();
        else
            service = new DefaultSubmitService();
        //2.ע��ActionContext
        Config<?> config = new AttributeConfigBridge(this, this.context);
        for (ActionContextBuilder builder : this.config_acb) {
            builder.init(config);
            service.regeditNameSpace(builder.getPrefix(), builder.builder());
        }
        //4.����Ĭ�������ռ�
        service.changeDefaultNameSpace(defaultNS);
        return service;
    }
    /**����Ĭ�������ռ�*/
    public void setDefaultNameSpace(String defaultNS) {
        this.defaultNS = defaultNS;
    };
    /**���{@link ActionContextBuilder}*/
    public void addActionContexBuilder(ActionContextBuilder acb) {
        if (acb != null)
            this.config_acb.add(acb);
    };
};