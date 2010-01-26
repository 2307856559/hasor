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
import org.more.NoDefinitionException;
import org.more.beans.info.BeanProp;
import org.more.beans.info.BeanProperty;
import org.more.beans.info.PropRefValue;
import org.more.beans.info.PropVarValue;
import org.more.beans.resource.xml.TagProcess;
import org.more.beans.resource.xml.XmlContextStack;
/**
 * ���ฺ�����property��ǩ<br/>
 * id="" name="a" value="12" refValue="refBean|{#attName}|{@number}|{$mime}" type="int|byte|char|double|float|long|short|boolean|String"
 * @version 2009-11-22
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public class Tag_Property extends TagProcess {
    protected String tagName = "property";
    protected BeanProperty createProperty() {
        return new BeanProperty();
    }
    @Override
    public void doStartEvent(String xPath, XMLStreamReader xmlReader, XmlContextStack context) {
        //һ������property��ǩ���ԡ�
        BeanProperty bp = this.createProperty();
        int attCount = xmlReader.getAttributeCount();
        for (int i = 0; i < attCount; i++) {
            String key = xmlReader.getAttributeLocalName(i);
            String var = xmlReader.getAttributeValue(i);
            if (key.equals("id") == true)
                bp.setId(var);
            else if (key.equals("name") == true)
                bp.setName(var);
            else if (key.equals("value") == true) {
                PropVarValue propVar = new PropVarValue();
                propVar.setValue(var);
                bp.setRefValue(propVar);
            } else if (key.equals("type") == true)
                bp.setPropType(var);
            else if (key.equals("refValue") == true) {
                PropRefValue propRef = PropRefValue.getPropRefValue(var);
                bp.setRefValue(propRef);
            } else
                throw new NoDefinitionException(tagName + "��ǩ����δ��������[" + key + "]");
        }
        //��������ǩ���Զ�����Ϊ��ǰ��ջ��ֵ���浽��ջֵ�ϡ�
        context.context = bp;
    }
    @Override
    public void doEndEvent(String xPath, XMLStreamReader xmlReader, XmlContextStack context) {
        //һ����ȡ��ջ�ĸ���ջ��bean��ǩ��ջ��
        ArrayList elementList = (ArrayList) context.get("tag_element");
        if (elementList == null || elementList.size() == 0) {} else {
            BeanProp bp = (BeanProp) elementList.get(0);
            BeanProperty prop = (BeanProperty) context.context;
            prop.setRefValue(bp);
        }
        //�������뵽bean�������С�
        XmlContextStack parent = context.getParent();
        ArrayList propertyList = (ArrayList) parent.get("tag_Property");
        if (propertyList == null) {
            propertyList = new ArrayList();
            parent.put("tag_Property", propertyList);
        }
        propertyList.add(context.context);
    }
}