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
package org.more.hypha.define.beans;
import java.util.ArrayList;
/**
 * ScriptBeanDefine�����ڶ���һ���ű��е�bean�����á�
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class ScriptBeanDefine extends TemplateBeanDefine {
    private String            scriptText    = null;                   //�ű�����
    private String            sourcePath    = null;                   //�ű��ļ�λ��
    private String            language      = "JavaScript";           //�ű�������
    private ArrayList<String> implementList = new ArrayList<String>(); //��bean��Ӧ�Ľӿ���
    /**���ء�ScriptBean����*/
    public String getBeanType() {
        return "ScriptBean";
    }
    /**��ȡ�ű�����*/
    public String getScriptText() {
        return scriptText;
    }
    /**���ýű�����*/
    public void setScriptText(String scriptText) {
        this.scriptText = scriptText;
    }
    /**��ȡ�ű��ļ�λ��*/
    public String getSourcePath() {
        return sourcePath;
    }
    /**���ýű��ļ�λ��*/
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
    /**��ȡ�ű���������*/
    public String getLanguage() {
        return language;
    }
    /**���ýű���������*/
    public void setLanguage(String language) {
        this.language = language;
    }
    /**��ȡ�ű�beanʵ�ֵĽӿڡ�*/
    public String[] getImplementList() {
        return (String[]) implementList.toArray();
    }
    /**���һ���ӿ�ʵ�֡�*/
    public void addImplement(String faceName) {
        if (this.implementList.contains(faceName) == false)
            this.implementList.add(faceName);
    }
    /**ɾ��һ���ӿ�ʵ�֡�*/
    public void removeImplement(String faceName) {
        if (this.implementList.contains(faceName) == true)
            this.implementList.remove(faceName);
    }
}