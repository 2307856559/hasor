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
package org.more.hypha.context.app;
import java.net.URL;
import java.util.List;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.hypha.context.AbstractDefineResource;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.util.ResourcesUtil;
/**
 * �ṩһ���ļ�·�������ļ��Ǵ���classpath�С�
 * Date : 2011-4-8
 * @author ������ (zyc@byshell.org)
 */
public class ClassPathApplicationContext extends AbstractApplicationContext {
    private AbstractDefineResource defineResource = null;
    // 
    public ClassPathApplicationContext() throws Throwable {
        this(null);
    };
    public ClassPathApplicationContext(String configFile) throws Throwable {
        super(null);
        XmlDefineResource adr = new XmlDefineResource();//������XmlDefineResource����beans�����޷���װ�ء�
        if (configFile != null) {
            List<URL> urls = ResourcesUtil.getResources(configFile);
            for (URL url : urls)
                adr.addSource(url);
        }
        adr.loadDefine();
        this.defineResource = adr;
    };
    public AbstractDefineResource getBeanResource() {
        return this.defineResource;
    };
};