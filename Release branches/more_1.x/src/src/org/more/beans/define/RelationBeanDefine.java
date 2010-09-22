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
package org.more.beans.define;
/**
 * RelationBeanDefine�����ڶ���һ��������һ��bean�����á�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class RelationBeanDefine extends TemplateBeanDefine {
    private String ref      = null; //�����õ�Bean��
    private String refScope = null; //�����õ�Bean������
    /**��ȡ���õ�Bean����*/
    public String getRef() {
        return this.ref;
    }
    /**�������õ�Bean����*/
    public void setRef(String ref) {
        this.ref = ref;
    }
    /**��ȡ���õ�Bean����������*/
    public String getRefScope() {
        return this.refScope;
    }
    /**�������õ�Bean����������*/
    public void setRefScope(String refScope) {
        this.refScope = refScope;
    }
}