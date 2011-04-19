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
package org.more.hypha.xml.support.aop;
import org.more.NoDefinitionException;
import org.more.NotFoundException;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.Event;
import org.more.hypha.EventManager;
import org.more.hypha.assembler.aop.AopInfoConfig;
import org.more.hypha.define.aop.AopConfigDefine;
import org.more.hypha.xml.context.XmlDefineResource;
import org.more.hypha.xml.event.XmlLoadedEvent;
/**
 * ���ڽ���aop:apply��ǩ
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagAop_Apply extends TagAop_NS implements XmlElementHook {
    public TagAop_Apply(XmlDefineResource configuration) {
        super(configuration);
    }
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        //1.ȡֵ
        String config = event.getAttributeValue("config");
        String toBeanExp = event.getAttributeValue("toBeanExp");//���ȼ���
        String toPackageExp = event.getAttributeValue("toPackageExp");
        //2.���
        if (config == null)
            throw new NoDefinitionException("apply��ǩ����⵽δ����config���Ի�������ֵΪ�ա�");
        AopInfoConfig plugin = (AopInfoConfig) this.getFlash().getAttribute(AopInfoConfig.ServiceName);
        AopConfigDefine aopConfig = plugin.getAopDefine(config);
        if (aopConfig == null)
            throw new NotFoundException("apply��ǩ��Ӧ��[" + config + "]aop����ʱ�޷��ҵ��䶨���AopConfigDefine���Ͷ���");
        //3.ע������� 
        EventManager manager = this.getDefineResource().getEventManager();
        Event e = Event.getEvent(XmlLoadedEvent.class);
        if (toBeanExp != null)
            manager.addEventListener(e, new Listener_ToBeanApply(config, toBeanExp));
        else
            manager.addEventListener(e, new Listener_ToPackageApply(config, toPackageExp));
    }
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {}
}