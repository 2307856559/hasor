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
package org.more.hypha.beans.support;
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.DefineResource;
import org.more.hypha.beans.define.EL_ValueMetaData;
/**
 * ���ڽ���el��ǩ
 * @version 2010-11-10
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_EL extends TagBeans_AbstractValueMetaDataDefine<EL_ValueMetaData> {
    /**����{@link TagBeans_EL}����*/
    public TagBeans_EL(DefineResource resource) {
        super(resource);
    }
    /**����{@link EL_ValueMetaData}����*/
    protected EL_ValueMetaData createDefine() {
        return new EL_ValueMetaData();
    }
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        elText
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        return null;
    }
    /**��������*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        String elText = event.getAttributeValue("elText");
        if (elText == null)
            return;
        EL_ValueMetaData metaData = this.getDefine(context);
        metaData.setElText(elText);
    }
}