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
 * GenerateBeanDefine�����ڶ���һ���Զ����ɵ�bean��
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class GenerateBeanDefine extends TemplateBeanDefine {
    private String nameStrategy     = null; //���������ɲ���Bean��
    private String aopStrategy      = null; //aop���ɲ���Bean��
    private String delegateStrategy = null; //ί�����ɲ���Bean��
    private String methodStrategy   = null; //�������ɲ���Bean��
    private String propertyStrategy = null; //�������ɲ���Bean��
    /**���ء�GenerateBean����*/
    public String getBeanType() {
        return "GenerateBean";
    }
    /**��ȡ���������ɲ��ԡ�*/
    public String getNameStrategy() {
        return this.nameStrategy;
    }
    /**�������������ɲ��ԡ�*/
    public void setNameStrategy(String nameStrategy) {
        this.nameStrategy = nameStrategy;
    }
    /**��ȡaop���ɲ��ԡ�*/
    public String getAopStrategy() {
        return this.aopStrategy;
    }
    /**����aop���ɲ��ԡ�*/
    public void setAopStrategy(String aopStrategy) {
        this.aopStrategy = aopStrategy;
    }
    /**��ȡί�����ɲ��ԡ�*/
    public String getDelegateStrategy() {
        return this.delegateStrategy;
    }
    /**����ί�����ɲ��ԡ�*/
    public void setDelegateStrategy(String delegateStrategy) {
        this.delegateStrategy = delegateStrategy;
    }
    /**��ȡ�������ɲ��ԡ�*/
    public String getMethodStrategy() {
        return this.methodStrategy;
    }
    /**���÷������ɲ��ԡ�*/
    public void setMethodStrategy(String methodStrategy) {
        this.methodStrategy = methodStrategy;
    }
    /**��ȡ�������ɲ��ԡ�*/
    public String getPropertyStrategy() {
        return this.propertyStrategy;
    }
    /**�����������ɲ��ԡ�*/
    public void setPropertyStrategy(String propertyStrategy) {
        this.propertyStrategy = propertyStrategy;
    }
}