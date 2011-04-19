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
package org.more.hypha.xml.support.beans;
import org.more.core.xml.XmlStackDecorator;
import org.more.hypha.define.beans.TemplateBeanDefine;
import org.more.hypha.xml.context.XmlDefineResource;
/**
 * ���ڽ���/beans/templateBean��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_TemplateBean extends TagBeans_AbstractBeanDefine<TemplateBeanDefine> {
    /**����{@link TagBeans_TemplateBean}����*/
    public TagBeans_TemplateBean(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link TemplateBeanDefine}����*/
    protected TemplateBeanDefine createDefine(XmlStackDecorator context) {
        return new TemplateBeanDefine();
    }
}