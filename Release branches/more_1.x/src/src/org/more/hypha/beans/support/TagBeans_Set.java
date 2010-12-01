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
import java.util.HashSet;
import org.more.hypha.beans.define.Set_ValueMetaData;
import org.more.hypha.context.XmlDefineResource;
/**
 * ���ڽ���set��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_Set extends TagBeans_AbstractCollection<Set_ValueMetaData> {
    /**����{@link TagBeans_Set}����*/
    public TagBeans_Set(XmlDefineResource configuration) {
        super(configuration);
    }
    /**����{@link Set_ValueMetaData}����*/
    protected Set_ValueMetaData createDefine() {
        return new Set_ValueMetaData();
    }
    protected Class<?> getDefaultCollectionType() {
        return HashSet.class;
    }
}