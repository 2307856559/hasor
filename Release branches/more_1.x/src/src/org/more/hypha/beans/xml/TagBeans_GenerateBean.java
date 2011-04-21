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
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.hypha.beans.define.GenerateBeanDefine;
import org.more.hypha.context.xml.XmlDefineResource;
/**
 * ���ڽ���/beans/generateBean��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_GenerateBean extends TagBeans_AbstractBeanDefine<GenerateBeanDefine> {
    /**����{@link TagBeans_GenerateBean}����*/
    public TagBeans_GenerateBean(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link GenerateBeanDefine}����*/
    protected GenerateBeanDefine createDefine(XmlStackDecorator context) {
        return new GenerateBeanDefine();
    }
    /**��������Bean�����ԡ�*/
    public enum PropertyKey {
        nameStrategy, aopStrategy, delegateStrategy, methodStrategy, propertyStrategy,
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        Map<Enum<?>, String> propertys = super.getPropertyMappings();
        propertys.put(PropertyKey.nameStrategy, "nameStrategy");
        propertys.put(PropertyKey.aopStrategy, "aopStrategy");
        propertys.put(PropertyKey.delegateStrategy, "delegateStrategy");
        propertys.put(PropertyKey.methodStrategy, "methodStrategy");
        propertys.put(PropertyKey.propertyStrategy, "propertyStrategy");
        return propertys;
    }
}