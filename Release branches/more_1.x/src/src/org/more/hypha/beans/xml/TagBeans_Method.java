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
package org.more.hypha.beans.xml;
import java.util.HashMap;
import java.util.Map;
import org.more.NoDefinitionException;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.hypha.beans.define.MethodDefine;
import org.more.hypha.beans.define.TemplateBeanDefine;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ����method��ǩ��
 * @version 2010-10-13
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Method extends TagBeans_AbstractDefine<MethodDefine> {
    /**����Ԫ��Ϣ.*/
    public static final String MethodDefine = "$more_Beans_MethodDefine";
    /**����{@link TagBeans_Method}����*/
    public TagBeans_Method(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link MethodDefine}����*/
    protected MethodDefine createDefine(XmlStackDecorator context) {
        TemplateBeanDefine define = (TemplateBeanDefine) context.getAttribute(TagBeans_TemplateBean.BeanDefine);
        return new MethodDefine(define);
    }
    /**���ԵĶ�������*/
    protected String getAttributeName() {
        return MethodDefine;
    }
    /**���巽�������ԡ�*/
    public enum PropertyKey {
        name, codeName, boolStatic
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.name, "name");
        propertys.put(PropertyKey.codeName, "codeName");
        propertys.put(PropertyKey.boolStatic, "static");
        return propertys;
    }
    /**������ע�ᵽBean�С�*/
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {
        MethodDefine method = this.getDefine(context);
        TemplateBeanDefine define = (TemplateBeanDefine) context.getAttribute(TagBeans_TemplateBean.BeanDefine);
        //
        if (method.getCodeName() == null)
            throw new NoDefinitionException("[" + define.getName() + "]�ķ�������δ����codeName���ԡ�");
        if (method.getName() == null)
            this.putAttribute(method, "name", method.getCodeName());
        //
        define.addMethod(method);
        super.endElement(context, xpath, event);
    }
}