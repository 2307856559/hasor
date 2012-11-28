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
package org.more.hypha.define;
/**
 * ��ʾһ��bean�����е�һ������
 * @version 2010-9-15
 * @author ������ (zyc@byshell.org)
 */
public class PropertyDefine extends AbstractPropertyDefine {
    private String  name     = null; //������
    private boolean lazyMark = false; //�Ƿ��ӳ�ע������ԡ�
    //-------------------------------------------------------------
    /**������������*/
    public String getName() {
        return this.name;
    };
    /**����������*/
    public void setName(String name) {
        this.name = name;
    };
    /**����һ��booleanֵ����ʾ���bean�Ƿ�Ϊ�ӳ�ע��ġ�*/
    public boolean isLazyMark() {
        return lazyMark;
    };
    /**�����Ƿ��ӳٳ�ʼ��������*/
    public void setLazyMark(boolean lazyMark) {
        this.lazyMark = lazyMark;
    };
    /**���ؾ����������ַ�����*/
    public String toString() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + " name=" + this.getName();
    };
}