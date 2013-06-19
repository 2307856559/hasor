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
package org.more.hypha.xml.tags.beans;
import java.util.HashMap;
import java.util.Map;
import org.more.core.error.DefineException;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.AbstractMethodDefine;
import org.more.hypha.DefineResource;
import org.more.hypha.define.BeanDefine;
import org.more.hypha.xml.XmlDefineResource;
import org.more.util.BeanUtil;
/**
 * ���ڽ���/beans/*Bean��ǩ�Ļ���
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public abstract class TagBeans_AbstractBeanDefine<T extends BeanDefine> extends TagBeans_AbstractDefine<T> {
    /**����{@link TagBeans_AbstractBeanDefine}����*/
    public TagBeans_AbstractBeanDefine(XmlDefineResource configuration) {
        super(configuration);
    }
    /**�������������е�bean����*/
    public static final String BeanDefine = "$more_Beans_BeanDefine";
    /**��ȡ�������������е�ɨ�������*/
    protected String getAttributeName() {
        return BeanDefine;
    };
    /**����ģ�����ԡ�*/
    public enum PropertyKey {
        id, name, logicPackage, iocEngine, //
        boolAbstract, boolInterface, boolSingleton, boolLazyInit, boolCheckType, //
        description, factoryName, factoryMethod, initMethod, destroyMethod, useTemplate
    }
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.id, "id");
        propertys.put(PropertyKey.name, "name");
        propertys.put(PropertyKey.logicPackage, "package");
        propertys.put(PropertyKey.iocEngine, "iocType");
        propertys.put(PropertyKey.boolAbstract, "abstract");
        propertys.put(PropertyKey.boolSingleton, "singleton");
        propertys.put(PropertyKey.boolLazyInit, "lazy");
        propertys.put(PropertyKey.boolCheckType, "check");
        propertys.put(PropertyKey.description, "description");
        //propertys.put(PropertyKey.factoryName, "factoryName");
        //propertys.put(PropertyKey.factoryMethod, "factoryMethod");
        propertys.put(PropertyKey.initMethod, "init");
        propertys.put(PropertyKey.destroyMethod, "destroy");
        //propertys.put(PropertyKey.useTemplate, "useTemplate");
        return propertys;
    }
    /**���⴦����useTemplate���Ե�ע��*/
    public void beginElement(XmlStackDecorator<Object> context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        BeanDefine define = this.getDefine(context);
        XmlDefineResource beanDefineManager = this.getDefineResource();
        /*1.logicPackage����*/
        if (define.getPackage() == null) {
            String logicPackage = (String) context.getAttribute(TagBeans_Package.LogicPackage);
            if (logicPackage == null)
                logicPackage = TagBeans_DefaultPackage.DefaultPackage;
            BeanUtil.writePropertyOrField(define, "logicPackage", logicPackage);
        }
        /*2.factoryName��factoryMethod*/
        String factoryName = event.getAttributeValue("factoryName");
        if (factoryName != null) {
            if (beanDefineManager.containsBeanDefine(factoryName) == false)
                throw new DefineException("[" + define.getName() + "]�Ҳ��������Ĺ���[" + factoryName + "]Bean����");
            BeanDefine factoryBean = (BeanDefine) beanDefineManager.getBeanDefine(factoryName);
            String factoryMethod = event.getAttributeValue("factoryMethod");
            AbstractMethodDefine methodDefine = factoryBean.getMethod(factoryMethod);
            BeanUtil.writePropertyOrField(define, "factoryBean", factoryBean);
            BeanUtil.writePropertyOrField(define, "factoryMethod", methodDefine);
        }
        /*3.useTemplate����*/
        String useTemplate = event.getAttributeValue("useTemplate");
        if (useTemplate != null) {
            BeanDefine template = null;
            if (beanDefineManager.containsBeanDefine(useTemplate) == true)
                template = beanDefineManager.getBeanDefine(useTemplate);
            else {
                /**��bean�������ڰ����ҡ�*/
                String packageStr = define.getPackage();
                packageStr = (packageStr == null) ? useTemplate : packageStr + "." + useTemplate;
                template = beanDefineManager.getBeanDefine(packageStr);
            }
            if (template == null)
                throw new DefineException("[" + define.getName() + "]�Ҳ���[" + useTemplate + "]��Beanģ�嶨��.");
            BeanUtil.writePropertyOrField(define, "useTemplate", template);
        }
    }
    /**����������ǩ��*/
    public void endElement(XmlStackDecorator<Object> context, String xpath, EndElementEvent event) {
        BeanDefine define = this.getDefine(context);
        //context.removeAttribute(this.getAttributeName());
        // ����Ҫremove��ԭ����super.endElement���������ٵ�ǰջ
        DefineResource beanDefineManager = this.getDefineResource();
        if (define != null)
            beanDefineManager.addBeanDefine(define);
        super.endElement(context, xpath, event);
    }
}