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
import java.util.ArrayList;
import java.util.List;
/**
 * �ýӿ����ڶ���{@link BeanDefine}�ϵ�һ��������
 * @version 2010-10-13
 * @author ������ (zyc@byshell.org)
 */
public class MethodDefine extends AbstractDefine<MethodDefine> {
    /*��̬�������*/
    private boolean           staticMark = false;
    /*����ķ��������ڲ����ƣ�.*/
    private String            name       = null;
    /*��������ʵ����.*/
    private String            codeName   = null;
    /*�����ķ���ֵ.*/
    private String            returnType = null;
    /*�������������б� */
    private List<ParamDefine> params     = new ArrayList<ParamDefine>();
    /*------------------------------------------------------------------*/
    /**��ȡ��̬�������*/
    public boolean isStaticMark() {
        return staticMark;
    }
    /**���þ�̬�������*/
    public void setStaticMark(boolean staticMark) {
        this.staticMark = staticMark;
    }
    /**��ȡ����ķ��������ڲ����ƣ�.*/
    public String getName() {
        return name;
    }
    /**���ö���ķ��������ڲ����ƣ�.*/
    public void setName(String name) {
        this.name = name;
    }
    /**��ȡ��������ʵ����.*/
    public String getCodeName() {
        return codeName;
    }
    /**���÷�������ʵ����.*/
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    /**��ȡ�����ķ���ֵ.*/
    public String getReturnType() {
        return returnType;
    }
    /**���÷����ķ���ֵ.*/
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    /**��ȡ�������������б� */
    public List<ParamDefine> getParams() {
        return params;
    }
    /**���÷������������б� */
    public void setParams(List<ParamDefine> params) {
        this.params = params;
    }
}