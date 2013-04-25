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
package org.platform.context;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import org.platform.Assert;
import org.platform.context.setting.Config;
/**
 * {@link InitContext}�ӿڵ�ʵ���ࡣ
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractInitContext implements InitContext {
    private Config config    = null;
    private long   startTime = 0;   //ϵͳ����ʱ��
    protected AbstractInitContext(Config config) {
        this.config = config;
        this.startTime = System.currentTimeMillis();
        Assert.isNotNull(config);
    }
    /*----------------------------------------------------------------------*/
    public long getStartTime() {
        return startTime;
    }
    /**��ȡ������ʼ��������*/
    public String getInitParameter(String name) {
        return this.config.getInitParameter(name);
    };
    /**��ȡ������ʼ���������Ƽ��ϡ�*/
    public Enumeration<String> getInitParameterNames() {
        return this.config.getInitParameterNames();
    };
    /**��ȡӦ�ó������á�*/
    public Config getConfig() {
        return this.config;
    }
    @Override
    public ServletContext getServletContext() {
        return this.config.getServletContext();
    }
}