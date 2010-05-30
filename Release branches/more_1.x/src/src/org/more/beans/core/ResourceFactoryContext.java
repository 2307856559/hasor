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
package org.more.beans.core;
import java.io.File;
import java.net.URI;
import org.more.beans.BeanFactory;
import org.more.beans.BeanResource;
import org.more.beans.resource.ResourceFactory;
/**
 * ��BeanContext�����ṩ��BeanResourceΪ������BeanContext�ӿ�ʵ�֡�
 * ResourceFactoryContext����ĸ���Context��LoadDefaultConfigContext��
 * @version 2010-2-26
 * @author ������ (zyc@byshell.org)
 */
public class ResourceFactoryContext extends AbstractBeanContext {
    //==================================================================================Constructor
    public ResourceFactoryContext() throws Exception {
        this.setParent(new LoadDefaultConfigContext());
    }
    /**����ResourceFactoryContext����*/
    public ResourceFactoryContext(BeanFactory beanFactory) throws Exception {
        this();
        defaultBeanFactory = beanFactory;
    }
    /**����ResourceFactoryContext����*/
    public ResourceFactoryContext(BeanResource beanResource) throws Exception {
        this();
        defaultBeanFactory = new ResourceBeanFactory(beanResource);
    }
    /**����ResourceFactoryContext����*/
    public ResourceFactoryContext(File configFile) throws Exception {
        this();
        BeanResource beanResource = ResourceFactory.create(configFile);
        defaultBeanFactory = new ResourceBeanFactory(beanResource);
    }
    /**����ResourceFactoryContext����*/
    public ResourceFactoryContext(URI configURI) throws Exception {
        this();
        BeanResource beanResource = ResourceFactory.create(configURI);
        defaultBeanFactory = new ResourceBeanFactory(beanResource);
    }
    @Override
    public void destroy() throws Exception {
        if (this.getParent() instanceof LoadDefaultConfigContext)
            this.getParent().destroy();
        super.destroy();
    }
    @Override
    public void init() throws Exception {
        if (this.getParent() instanceof LoadDefaultConfigContext)
            this.getParent().init();
        super.init();
    }
}