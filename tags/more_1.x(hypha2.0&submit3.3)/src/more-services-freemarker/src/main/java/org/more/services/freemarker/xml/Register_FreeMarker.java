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
package org.more.services.freemarker.xml;
import org.more.core.xml.XmlParserKit;
import org.more.hypha.Event;
import org.more.hypha.EventManager;
import org.more.hypha.anno.BeginScanEvent;
import org.more.hypha.context.StartingServicesEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.hypha.context.xml.XmlNameSpaceRegister;
import org.more.services.freemarker.FreemarkerService;
import org.more.services.freemarker.assembler.FreemarkerService_Impl;
/**
 * ����ʵ����{@link XmlNameSpaceRegister}�ӿڲ����ṩ�˶������ռ䡰http://project.byshell.org/more/schema/freemarker���Ľ���֧�֡�
 * @version : 2011-8-15
 * @author ������ (zyc@byshell.org)
 */
public class Register_FreeMarker implements XmlNameSpaceRegister {
    /**���û��ָ��namespaceURL������ó�������ָ��Ĭ�ϵ������ռ䡣*/
    public static final String DefaultNameSpaceURL = "http://project.byshell.org/more/schema/freemarker";
    /**ִ�г�ʼ��ע�ᡣ*/
    public void initRegister(String namespaceURL, XmlDefineResource resource) {
        //1.FreemarkerService
        FreemarkerService service = new FreemarkerService_Impl();
        //2.ע���ǩ������
        XmlParserKit kit = new XmlParserKit();
        kit.regeditHook("/defaultInEncoding", new TagFK_DefaultInEncoding(resource, service));
        kit.regeditHook("/defaultOutcoding", new TagFK_DefaultOutEncoding(resource, service));
        kit.regeditHook("/ddTemplateDir", new TagFK_AddTemplateDir(resource, service));
        kit.regeditHook("/addClassPath", new TagFK_AddClassPath(resource, service));
        kit.regeditHook("/templates/template", new TagFK_Template(resource, service));
        //3.ע�������ռ�
        if (namespaceURL == null)
            namespaceURL = DefaultNameSpaceURL;
        resource.regeditXmlParserKit(namespaceURL, kit);
        //4.ע���¼�
        EventManager manager = resource.getEventManager();
        manager.addEventListener(Event.getEvent(BeginScanEvent.class), new OnBeginScan(service));
        manager.addEventListener(Event.getEvent(StartingServicesEvent.class), new OnStartingServices(service));
    };
};