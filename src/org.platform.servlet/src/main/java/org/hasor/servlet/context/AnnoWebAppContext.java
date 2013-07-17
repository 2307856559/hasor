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
package org.hasor.servlet.context;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletContext;
import org.hasor.annotation.context.AnnoAppContext;
import org.hasor.context.Environment;
import org.hasor.context.HasorModule;
import org.hasor.context.WorkSpace;
import org.hasor.context.environment.StandardEnvironment;
import org.hasor.servlet.binder.support.WebApiBinderModule;
import com.google.inject.Binder;
import com.google.inject.Provider;
/**
 * 
 * @version : 2013-7-16
 * @author ������ (zyc@byshell.org)
 */
public class AnnoWebAppContext extends AnnoAppContext {
    private ServletContext servletContext = null;
    public AnnoWebAppContext(ServletContext servletContext) throws IOException {
        super();
        this.setContext(servletContext);
        this.servletContext = servletContext;
    }
    public AnnoWebAppContext(String mainConfig, ServletContext servletContext) throws IOException {
        super(mainConfig);
        this.setContext(servletContext);
        this.servletContext = servletContext;
    }
    public ServletContext getServletContext() {
        return this.servletContext;
    }
    @Override
    protected Environment createEnvironment() {
        return new WebStandardEnvironment(this.getWorkSpace(), this.servletContext);
    }
    @Override
    protected WebApiBinderModule newApiBinder(final HasorModule forModule, final Binder binder) {
        return new WebApiBinderModule(this) {
            @Override
            public Binder getGuiceBinder() {
                return binder;
            }
            @Override
            public void configure(Binder binder) {
                super.configure(binder);
                /*��ServletContext�����Provider*/
                binder.bind(ServletContext.class).toProvider(new Provider<ServletContext>() {
                    @Override
                    public ServletContext get() {
                        return getServletContext();
                    }
                });
            }
        };
    }
}
/**
 * ����ע��MORE_WEB_ROOT���������Լ�Web����������ά����
 * @version : 2013-7-17
 * @author ������ (zyc@byshell.org)
 */
class WebStandardEnvironment extends StandardEnvironment {
    private ServletContext servletContext = null;
    public WebStandardEnvironment(WorkSpace workSpace, ServletContext servletContext) {
        super(workSpace);
        this.servletContext = servletContext;
    }
    @Override
    protected Map<String, String> getHasorEnvironment() {
        Map<String, String> hasorEnv = super.getHasorEnvironment();
        hasorEnv.put("hasor_webroot", servletContext.getRealPath("/"));
        return hasorEnv;
    }
}