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
package org.more.hypha.commons.engine;
import java.util.HashMap;
import java.util.Map;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.util.attribute.IAttribute;
/**
 * ����e:engine
 * @version : 2011-6-3
 * @author ������ (zyc@byshell.org)
 */
public class TagEngine_Engine extends TagRegister_NS implements XmlElementHook {
    public static final String ConfigList = "$more_Engine_List";
    public TagEngine_Engine(XmlDefineResource configuration) {
        super(configuration);
    }
    private Map<String, String> getMapping() {
        Map<String, String> mapping = null;
        IAttribute flash = this.getDefineResource().getFlash();
        if (flash.contains(ConfigList) == false) {
            mapping = new HashMap<String, String>();
            flash.setAttribute(ConfigList, mapping);
        } else
            mapping = (Map<String, String>) flash.getAttribute(ConfigList);
        return mapping;
    }
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        Map<String, String> mapping = this.getMapping();
        String name = event.getAttributeValue("name");
        String classname = event.getAttributeValue("class");
        mapping.put(name, classname);//�ظ�ע��ᵼ���滻�����á�
    }
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {}
}