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
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.Event.Sequence;
import org.more.hypha.EventListener;
import org.more.hypha.aop.AopInfoConfig;
import org.more.hypha.aop.assembler.AopInfoConfig_Impl;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.hypha.context.xml.XmlLoadedEvent;
import org.more.util.StringUtil;
import org.more.util.attribute.IAttribute;
/**
 * �����ǵ�{@link XmlDefineResource}����{@link XmlLoadedEvent}�����¼�ʱ����anno:apply��ǩ���õ�Ӧ��Package���������
 * @version 2010-10-11
 * @author ������ (zyc@byshell.org)
 */
public class Listener_ToPackageApply implements EventListener<XmlLoadedEvent> {
    private String config = null, toPackageExp = "*";
    //----------------------------------------------
    /**����{@link Listener_ToPackageApply}����*/
    public Listener_ToPackageApply(String config, String toPackageExp) {
        this.config = config;
        this.toPackageExp = toPackageExp;
    };
    /**ִ��PackageӦ�á�*/
    public void onEvent(final XmlLoadedEvent event, final Sequence sequence) {
        XmlDefineResource config = event.toParams(sequence).xmlDefineResource;
        IAttribute flash = config.getFlash();
        AopInfoConfig aopPlugin = (AopInfoConfig) flash.getAttribute(AopInfoConfig_Impl.ServiceName);
        for (String defineName : config.getBeanDefinitionIDs()) {
            AbstractBeanDefine define = config.getBeanDefine(defineName);
            if (StringUtil.matchWild(this.toPackageExp, define.getID()) == true)
                aopPlugin.setAop(define, this.config);
        }
    };
};