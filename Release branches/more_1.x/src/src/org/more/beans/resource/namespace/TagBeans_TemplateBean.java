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
package org.more.beans.resource.namespace;
import java.util.HashMap;
import java.util.Map;
import org.more.NoDefinitionException;
import org.more.beans.AbstractBeanDefine;
import org.more.beans.define.TemplateBeanDefine;
import org.more.beans.resource.XmlConfiguration;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.util.attribute.StackDecorator;
/**
 * ���ڽ���/beans/templateBean��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_TemplateBean extends TagBeans_AbstractDefine {
    /**�������������е�ɨ�����*/
    public static final String BeanDefine = "$more_BeanDefine";
    /**��ȡ�������������е�ɨ�������*/
    protected String getDefineName() {
        return BeanDefine;
    };
    /**����{@link TemplateBeanDefine}����*/
    protected Object createDefine(StackDecorator context) {
        return new TemplateBeanDefine();
    };
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        name, iocType, scope, boolAbstract, boolInterface, boolSingleton, boolLazyInit, description, factoryName, factoryMethod, useTemplate
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.name, "name");
        propertys.put(PropertyKey.iocType, "iocType");
        propertys.put(PropertyKey.scope, "scope");
        propertys.put(PropertyKey.boolAbstract, "abstract");
        propertys.put(PropertyKey.boolInterface, "interface");
        propertys.put(PropertyKey.boolSingleton, "singleton");
        propertys.put(PropertyKey.boolLazyInit, "lazy");
        propertys.put(PropertyKey.description, "description");
        propertys.put(PropertyKey.factoryName, "factoryName");
        propertys.put(PropertyKey.factoryMethod, "factoryMethod");
        //propertys.put(PropertyKey.useTemplate, "useTemplate");
        return propertys;
    }
    /**���⴦����useTemplate���Ե�ע��*/
    public void beginElement(StackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        String useTemplate = event.getAttributeValue("useTemplate");
        if (useTemplate != null) {
            XmlConfiguration beanDefineManager = (XmlConfiguration) context.getAttribute(TagBeans_Beans.BeanDefineManager);
            Object define = this.getDefine(context);
            AbstractBeanDefine template = beanDefineManager.getBeanDefine(useTemplate);
            if (template == null)
                throw new NoDefinitionException("�Ҳ���[" + useTemplate + "]��Beanģ�嶨��.");
            this.putAttribute(define, "useTemplate", template);
        }
    }
    /**����������ǩ��*/
    public void endElement(StackDecorator context, String xpath, EndElementEvent event) {
        AbstractBeanDefine define = (AbstractBeanDefine) this.getDefine(context);
        context.removeAttribute(this.getDefineName());
        XmlConfiguration beanDefineManager = (XmlConfiguration) context.getAttribute(TagBeans_Beans.BeanDefineManager);
        if (define != null)
            beanDefineManager.addBeanDefine(define);
        super.endElement(context, xpath, event);
    }
}