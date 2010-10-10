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
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.AbstractPropertyDefine;
import org.more.hypha.beans.define.QuickProperty_ValueMetaData;
import org.more.hypha.configuration.XmlConfiguration;
/**
 * {@link QuickPropertyParser}�ӿ��¼����󡣸��¼����𽫴���һЩ������
 * @version 2010-9-22
 * @author ������ (zyc@byshell.org)
 */
public class QuickParserEvent {
    private XmlConfiguration            configuration = null; //
    private AbstractBeanDefine          define        = null; //
    private AbstractPropertyDefine      property      = null; //
    private QuickProperty_ValueMetaData oldMetaData   = null; //�ȴ�������ValueMetaData
    public QuickParserEvent(XmlConfiguration configuration, AbstractBeanDefine define, AbstractPropertyDefine property, QuickProperty_ValueMetaData oldMetaData) {
        this.configuration = configuration;
        this.define = define;
        this.property = property;
        this.oldMetaData = oldMetaData;
    }
    /**��ȡ��ǰ��������{@link XmlConfiguration}����*/
    public XmlConfiguration getConfiguration() {
        return this.configuration;
    }
    /**��ȡ��ǰ��������{@link AbstractBeanDefine}����*/
    public AbstractBeanDefine getDefine() {
        return this.define;
    }
    /**��ȡ��ǰ��������{@link AbstractPropertyDefine}����*/
    public AbstractPropertyDefine getProperty() {
        return this.property;
    }
    /**��ȡ�ȴ�������ValueMetaData��Ϣ��*/
    public QuickProperty_ValueMetaData getOldMetaData() {
        return this.oldMetaData;
    }
}