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
package org.more.beans.define.beans;
/**
 * ClassBeanDefine�����ڶ���һ�������bean�����bean��һ�������class�����
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class ClassBeanDefine extends TemplateBeanDefine {
    private Class<?> source = null; //class��
    /**���ء�ClassBean����*/
    public String getBeanType() {
        return "ClassBean";
    }
    /**��ȡ���class�����޶�����*/
    public Class<?> getSource() {
        return source;
    }
    /**�����������޶�����*/
    public void setSource(Class<?> source) {
        this.source = source;
    }
}