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
import org.more.core.error.MoreStateException;
import org.more.hypha.ApplicationContext;
import org.more.hypha.context.AbstractApplicationContext;
import org.more.hypha.context.AbstractDefineResource;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * �򵥵�{@link ApplicationContext}�ӿ�ʵ���ࡣ
 * Date : 2011-4-8
 * @author ������ (zyc@byshell.org)
 */
public class HyphaApplicationContext extends AbstractApplicationContext {
    private AbstractDefineResource defineResource = null;
    // 
    public HyphaApplicationContext() throws Throwable {
        super(null);
        XmlDefineResource adr = new XmlDefineResource();//������XmlDefineResource����beans�����޷���װ�ء�
        adr.loadDefine();
        this.defineResource = adr;
    }
    public HyphaApplicationContext(AbstractDefineResource defineResource) throws NullPointerException, MoreStateException {
        super(null);
        if (defineResource == null)
            throw new NullPointerException("����defineResourceû��ָ��һ����Ч��ֵ���ò���������Ϊ�ա�");
        if (defineResource.isReady() == false)
            throw new MoreStateException("����defineResource����û��׼���á�");
        this.defineResource = defineResource;
    }
    public AbstractDefineResource getBeanResource() {
        return this.defineResource;
    }
};