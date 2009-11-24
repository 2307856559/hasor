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
package org.more.beans.resource.xml.core;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamReader;

import org.more.beans.info.BeanProperty;
import org.more.beans.resource.xml.ContextStack;
/**
 * ���ฺ�����constructor-arg��ǩ
 * Date : 2009-11-22
 * @author ������
 */
@SuppressWarnings("unchecked")
public class Tag_ConstructorArg extends Tag_Property {
    @Override
    public void doStartEvent(String xPath, XMLStreamReader xmlReader, ContextStack context) {
        this.tagName = "constructor-arg";
        super.doStartEvent(xPath, xmlReader, context);
    }
    @Override
    public void doEndEvent(String xPath, XMLStreamReader xmlReader, ContextStack context) {
        //һ����ȡ��ջ�ĸ���ջ��bean��ǩ��ջ��
        ContextStack parent = context.getParent();
        ArrayList propList = (ArrayList) parent.get("tag_ConstructorArg");
        if (propList == null) {
            propList = new ArrayList();
            parent.put("tag_ConstructorArg", propList);
        }
        //�����������
        propList.add((BeanProperty) context.context);
    }
}