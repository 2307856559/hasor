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
package org.more.hypha.beans.define;
/**
 * ClassPathBeanDefine�����ڶ���һ�������bean�����bean��һ�������class�����
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class ClassPathBeanDefine extends AbstractBaseBeanDefine {
    private String useTemplate = null; //Ӧ�õ�ģ��
    private String source      = null; //class��
    /**���ء�ClassBean����*/
    public String getBeanType() {
        return "ClassPathBean";
    }
    /**��ȡbeanʹ�õ�ģ�塣*/
    public String getUseTemplate() {
        return this.useTemplate;
    };
    /**����beanʹ�õ�ģ�塣*/
    public void setUseTemplate(String useTemplate) {
        this.useTemplate = useTemplate;
    }
    /**��ȡ���class�����޶�����*/
    public String getSource() {
        return source;
    }
    /**�����������޶�����*/
    public void setSource(String source) {
        this.source = source;
    }
}