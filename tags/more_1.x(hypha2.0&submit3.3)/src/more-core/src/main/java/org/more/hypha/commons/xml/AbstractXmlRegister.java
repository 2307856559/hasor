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
package org.more.hypha.commons.xml;
import org.more.core.xml.XmlParserKit;
import org.more.core.xml.register.XmlRegister;
import org.more.hypha.context.xml.XmlNameSpaceRegister;
/**
 * 
 * @version : 2012-1-12
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractXmlRegister implements XmlNameSpaceRegister {
    /**�÷���������̳У��÷����лὫ{@link #createKit(String, XmlRegister)}������Kitע�ᵽmanager�У�
     * �����{@link #createKit(String, XmlRegister)}�������Ѿ�ע������Զ������ظ�ע�ᡣ*/
    public final XmlParserKit createXmlParserKit(String namespace, XmlRegister manager) {
        XmlParserKit kit = this.createKit(namespace, manager);
        if (manager.isRegeditKit(namespace, kit) == false)
            manager.regeditKit(namespace, kit);/*û��ע��ִ��ע��*/
        return kit;
    }
    /**���������д�÷����Դﵽ�����Զ���kit��Ŀ�ġ�*/
    public XmlParserKit createKit(String namespace, XmlRegister manager) {
        return new XmlParserKit();
    };
}