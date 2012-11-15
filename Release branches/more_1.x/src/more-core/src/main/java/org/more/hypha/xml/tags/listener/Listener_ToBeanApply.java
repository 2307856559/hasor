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
package org.more.hypha.xml.tags.listener;
import org.more.core.event.Event.Sequence;
import org.more.core.event.EventListener;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.aop.AopService;
import org.more.hypha.aop.assembler.AopService_Impl;
import org.more.hypha.xml.XmlDefineResource;
import org.more.hypha.xml.XmlLoadedEvent;
import org.more.util.StringUtil;
import org.more.util.attribute.IAttribute;
/**
 * �����ǵ�{@link XmlDefineResource}����{@link XmlLoadedEvent}�����¼�ʱ����anno:apply��ǩ���õ�Ӧ��Bean���������
 * @version 2010-10-11
 * @author ������ (zyc@byshell.org)
 */
public class Listener_ToBeanApply implements EventListener<XmlLoadedEvent> {
    private String config = null, toBeanExp = "*";
    //----------------------------------------------
    /**����{@link Listener_ToBeanApply}����*/
    public Listener_ToBeanApply(String config, String toBeanExp) {
        this.config = config;
        this.toBeanExp = toBeanExp;
    };
    /**ִ��BeanӦ�á�*/
    public void onEvent(final XmlLoadedEvent event, final Sequence sequence) {
        XmlDefineResource config = event.toParams(sequence).xmlDefineResource;
        IAttribute<Object> flash = config.getFlash();
        AopService aopPlugin = (AopService) flash.getAttribute(AopService_Impl.ServiceName);
        for (String defineName : config.getBeanDefinitionIDs())
            if (StringUtil.matchWild(this.toBeanExp, defineName) == true) {
                BeanDefine define = config.getBeanDefine(defineName);
                aopPlugin.setAop(define, this.config);
            }
    };
};