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
package org.more.hypha.aop.support;
import org.more.hypha.DefineResource;
import org.more.hypha.Event;
import org.more.hypha.EventListener;
import org.more.hypha.aop.AopDefineExpand_Impl;
import org.more.hypha.aop.AopResourceExpand;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.context.XmlDefineResource;
import org.more.hypha.event.Config_LoadedXmlEvent;
import org.more.util.StringUtil;
/**
 * �����ǵ�{@link XmlDefineResource}����{@link Config_LoadedXmlEvent}�����¼�ʱ����anno:apply��ǩ���õ�Ӧ��Bean���������
 * @version 2010-10-11
 * @author ������ (zyc@byshell.org)
 */
public class Listener_ToBeanApply implements EventListener {
    private String config = null, toBeanExp = "*";
    //----------------------------------------------
    /**����{@link Listener_ToBeanApply}����*/
    public Listener_ToBeanApply(String config, String toBeanExp) {
        this.config = config;
        this.toBeanExp = toBeanExp;
    }
    /**ִ��BeanӦ�á�*/
    public void onEvent(Event event) {
        Config_LoadedXmlEvent eve = (Config_LoadedXmlEvent) event;
        DefineResource config = eve.getResource();
        AopResourceExpand aopPlugin = (AopResourceExpand) config.getPlugin(AopResourceExpand.AopDefineResourcePluginName);
        for (String defineName : config.getBeanDefineNames())
            if (StringUtil.matchWild(this.toBeanExp, defineName) == true) {
                AbstractBeanDefine define = config.getBeanDefine(defineName);
                if (define.getPlugin(AopDefineExpand_Impl.AopPluginName) == null)
                    aopPlugin.setAop(define, this.config);
            }
    }
}