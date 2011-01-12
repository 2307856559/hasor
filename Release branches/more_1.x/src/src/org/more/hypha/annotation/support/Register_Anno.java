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
package org.more.hypha.annotation.support;
import org.more.core.xml.XmlParserKit;
import org.more.hypha.annotation.AnnoResourceExpand;
import org.more.hypha.annotation.Aop;
import org.more.hypha.annotation.Bean;
import org.more.hypha.annotation.assembler.AnnoResourceExpand_Impl;
import org.more.hypha.annotation.assembler.Watch_Aop;
import org.more.hypha.annotation.assembler.Watch_Bean;
import org.more.hypha.context.XmlDefineResource;
import org.more.hypha.context.XmlNameSpaceRegister;
import org.more.util.attribute.IAttribute;
/**
 * ����ʵ����{@link XmlNameSpaceRegister}�ӿڲ����ṩ�˶������ռ䡰http://project.byshell.org/more/schema/annotation���Ľ���֧�֡�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class Register_Anno implements XmlNameSpaceRegister {
    /**���û��ָ��namespaceURL������ó�������ָ��Ĭ�ϵ������ռ䡣*/
    public static final String DefaultNameSpaceURL = "http://project.byshell.org/more/schema/annotation";
    /**ִ�г�ʼ��ע�ᡣ*/
    public void initRegister(String namespaceURL, XmlDefineResource resource, IAttribute flash) throws Throwable {
        //1.ע��ע�������
        AnnoResourceExpand plugin = new AnnoResourceExpand_Impl(resource);
        plugin.registerAnnoKeepWatch(Bean.class, new Watch_Bean());//����Bean
        plugin.registerAnnoKeepWatch(Aop.class, new Watch_Aop());//����Aop
        resource.setPlugin(AnnoResourceExpand.AnnoDefineResourcePluginName, plugin);
        //2.ע���ǩ������
        XmlParserKit kit = new XmlParserKit();
        kit.regeditHook("/anno", new TagAnno_Anno(resource));
        //3.ע�������ռ�
        if (namespaceURL == null)
            namespaceURL = DefaultNameSpaceURL;
        resource.regeditXmlParserKit(namespaceURL, kit);
    }
}