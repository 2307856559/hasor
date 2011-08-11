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
package org.more.submit.xml;
import java.util.ArrayList;
import org.more.core.xml.XmlParserKit;
import org.more.hypha.Event;
import org.more.hypha.EventManager;
import org.more.hypha.anno.BeginScanEvent;
import org.more.hypha.context.StartingServicesEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.hypha.context.xml.XmlNameSpaceRegister;
import org.more.submit.SubmitBuild;
/**
 * ����ʵ����{@link XmlNameSpaceRegister}�ӿڲ����ṩ�˶������ռ䡰http://project.byshell.org/more/schema/submit���Ľ���֧�֡�
 * @version : 2011-7-15
 * @author ������ (zyc@byshell.org)
 */
public class Register_Submit implements XmlNameSpaceRegister {
    /**���û��ָ��namespaceURL������ó�������ָ��Ĭ�ϵ������ռ䡣*/
    public static final String DefaultNameSpaceURL = "http://project.byshell.org/more/schema/submit";
    /**ִ�г�ʼ��ע�ᡣ*/
    public void initRegister(String namespaceURL, XmlDefineResource resource) {
        //1.SubmitBuild
        B_Config config = new B_Config();
        config.acList = new ArrayList<B_AC>();
        config.acMappingList = new ArrayList<B_AnnoActionInfo>();
        config.build = new SubmitBuild();
        //2.ע���ǩ������
        XmlParserKit kit = new XmlParserKit();
        kit.regeditHook("/acBuilder", new TagSubmit_ACBuilder(resource, config));
        kit.regeditHook("/config", new TagSubmit_Config(resource, config));
        kit.regeditHook("/actionContext", new TagSubmit_AC(resource, config));
        kit.regeditHook("/defaultAC", new TagSubmit_DefaultAC(resource, config));
        kit.regeditHook("/result", new TagSubmit_Result(resource, config));
        kit.regeditHook("/result/config", new TagSubmit_ResultConfig(resource, config));
        //3.ע�������ռ�
        if (namespaceURL == null)
            namespaceURL = DefaultNameSpaceURL;
        resource.regeditXmlParserKit(namespaceURL, kit);
        //4.ע���¼�
        EventManager manager = resource.getEventManager();
        manager.addEventListener(Event.getEvent(BeginScanEvent.class), new OnBeginScan(config));
        manager.addEventListener(Event.getEvent(StartingServicesEvent.class), new OnStartingServices(config));
    };
};