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
package org.more.hypha.commons.engine.xml;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.more.core.error.LoadException;
import org.more.core.event.Event;
import org.more.core.xml.XmlParserKit;
import org.more.hypha.commons.xml.AbstractXmlRegister;
import org.more.hypha.context.InitEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.hypha.context.xml.XmlNameSpaceRegister;
/**
 * ����ʵ����{@link XmlNameSpaceRegister}�ӿڲ����ṩ�˶������ռ䡰http://project.byshell.org/more/schema/engine���Ľ���֧�֡�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class TagRegister_Engine extends AbstractXmlRegister {
    /**ִ�г�ʼ��ע�ᡣ*/
    public void initRegister(XmlParserKit parserKit, XmlDefineResource resource) throws LoadException, IOException {
        Map<String, String> xmlConfig = new HashMap<String, String>();
        //1.ע���ǩ������
        parserKit.regeditHook("/engine", new TagEngine_Engine(resource, xmlConfig));
        //2.ע���¼�.
        resource.getEventManager().addEventListener(Event.getEvent(InitEvent.class), new OnInit(xmlConfig));
    }
};