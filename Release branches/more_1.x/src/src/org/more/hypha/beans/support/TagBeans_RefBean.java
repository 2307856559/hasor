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
import org.more.hypha.beans.define.RelationBeanDefine;
import org.more.hypha.context.XmlDefineResource;
/**
 * ���ڽ���/beans/refBean��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_RefBean extends TagBeans_AbstractBeanDefine<RelationBeanDefine> {
    /**����{@link TagBeans_RefBean}����*/
    public TagBeans_RefBean(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link RelationBeanDefine}����*/
    protected RelationBeanDefine createDefine() {
        return new RelationBeanDefine();
    }
    /**������������Bean������*/
    public enum PropertyKey {
        ref
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        Map<Enum<?>, String> propertys = super.getPropertyMappings();
        propertys.put(PropertyKey.ref, "ref");
        return propertys;
    }
}