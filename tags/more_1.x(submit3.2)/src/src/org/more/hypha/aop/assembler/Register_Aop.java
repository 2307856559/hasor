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
package org.more.hypha.aop.assembler;
import org.more.core.xml.XmlParserKit;
import org.more.hypha.Event;
import org.more.hypha.aop.AopService;
import org.more.hypha.aop.xml.TagAop_Apply;
import org.more.hypha.aop.xml.TagAop_Before;
import org.more.hypha.aop.xml.TagAop_Config;
import org.more.hypha.aop.xml.TagAop_Filter;
import org.more.hypha.aop.xml.TagAop_Informed;
import org.more.hypha.aop.xml.TagAop_PointGroup;
import org.more.hypha.aop.xml.TagAop_Pointcut;
import org.more.hypha.aop.xml.TagAop_Returning;
import org.more.hypha.aop.xml.TagAop_Throwing;
import org.more.hypha.aop.xml.TagAop_UseConfig;
import org.more.hypha.context.InitEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.hypha.context.xml.XmlNameSpaceRegister;
/**
 * ����ʵ����{@link XmlNameSpaceRegister}�ӿڲ����ṩ�˶������ռ䡰http://project.byshell.org/more/schema/beans-aop���Ľ���֧�֡�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class Register_Aop implements XmlNameSpaceRegister {
    /**���û��ָ��namespaceURL������ó�������ָ��Ĭ�ϵ������ռ䡣*/
    public static final String DefaultNameSpaceURL = "http://project.byshell.org/more/schema/beans-aop";
    /**ִ�г�ʼ��ע�ᡣ*/
    public void initRegister(String namespaceURL, XmlDefineResource resource) {
        //1.���Aop���
        AopService service = new AopService_Impl();
        resource.getFlash().setAttribute(AopService_Impl.ServiceName, service);
        //2.ע���ǩ������
        XmlParserKit kit = new XmlParserKit();
        kit.regeditHook("/config", new TagAop_Config(resource));
        kit.regeditHook("*/pointcut", new TagAop_Pointcut(resource));
        kit.regeditHook("*/pointGroup", new TagAop_PointGroup(resource));
        kit.regeditHook("/useConfig", new TagAop_UseConfig(resource));
        kit.regeditHook("/@useConfig", new TagAop_UseConfig(resource));
        kit.regeditHook("/config/filter", new TagAop_Filter(resource));
        kit.regeditHook("/config/informed", new TagAop_Informed(resource));
        kit.regeditHook("/config/before", new TagAop_Before(resource));
        kit.regeditHook("/config/returning", new TagAop_Returning(resource));
        kit.regeditHook("/config/throwing", new TagAop_Throwing(resource));
        kit.regeditHook("/apply", new TagAop_Apply(resource));
        //3.ע�������ռ�
        if (namespaceURL == null)
            namespaceURL = DefaultNameSpaceURL;
        resource.regeditXmlParserKit(namespaceURL, kit);
        //4.ע���¼�
        resource.getEventManager().addEventListener(Event.getEvent(InitEvent.class), new OnInit(service));
    }
}