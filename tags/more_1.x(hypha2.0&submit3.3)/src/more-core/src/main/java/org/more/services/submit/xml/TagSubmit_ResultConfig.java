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
package org.more.services.submit.xml;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.context.xml.XmlDefineResource;
import org.more.services.submit.ResultProcess;
/**
 * ���ڽ���submit:result��ǩ��
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class TagSubmit_ResultConfig extends TagSubmit_NS implements XmlElementHook {
    //private B_Config config = null;
    /**����{@link TagSubmit_ResultConfig}����*/
    public TagSubmit_ResultConfig(XmlDefineResource configuration, B_Config config) {
        super(configuration);
        //this.config = config;
    };
    /**��ʼ��ǩ����expression���ԡ�*/
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) {
        String key = event.getAttributeValue("key");
        String value = event.getAttributeValue("value");
        //
        if (key == null || key.equals("") == true)
            return;
        ResultProcess<?> rp = (ResultProcess<?>) context.getAttribute(TagSubmit_Result.ResultProcess_Name);
        rp.addParam(key, value);
    };
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) {};
};