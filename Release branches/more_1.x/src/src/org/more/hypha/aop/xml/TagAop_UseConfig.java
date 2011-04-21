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
package org.more.hypha.aop.xml;
import org.more.NotFoundException;
import org.more.core.xml.XmlAttributeHook;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.AttributeEvent;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.aop.AopInfoConfig;
import org.more.hypha.aop.define.AopConfigDefine;
import org.more.hypha.beans.xml.TagBeans_AbstractBeanDefine;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ���ڽ���aop:useConfig��ǩ��useConfig����
 * @version 2010-10-9
 * @author ������ (zyc@byshell.org)
 */
public class TagAop_UseConfig extends TagAop_NS implements XmlElementHook, XmlAttributeHook {
    /**����{@link TagAop_UseConfig}����*/
    public TagAop_UseConfig(XmlDefineResource configuration) {
        super(configuration);
    }
    /**�����ǩ��ʽ��*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        String name = event.getAttributeValue("name");
        this.processElement(context, name);
    }
    /**����������ʽ��*/
    public void attribute(XmlStackDecorator context, String xpath, AttributeEvent event) {
        String name = event.getValue();
        this.processElement(context, name);
    }
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {}
    /**����aop:useConfig*/
    private void processElement(XmlStackDecorator context, String name) {
        AopInfoConfig service = (AopInfoConfig) this.getFlash().getAttribute(AopInfoConfig.ServiceName);
        AbstractBeanDefine bean = (AbstractBeanDefine) context.getAttribute(TagBeans_AbstractBeanDefine.BeanDefine);
        //
        AopConfigDefine aopConfig = service.getAopDefine(name);
        if (aopConfig == null)
            throw new NotFoundException("useConfig ��[" + bean.getName() + "]�ϵ�[" + name + "]���ã��޷���aop���ÿ����ҵ���");
        service.setAop(bean, aopConfig);
    }
}