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
package org.more.hypha.define.aop;
import java.util.ArrayList;
import org.more.hypha.define.AbstractDefine;
/**
 * aop����
 * @version 2010-9-25
 * @author ������ (zyc@byshell.org)
 */
public class AopConfigDefine extends AbstractDefine<AopConfigDefine> {
    /*������*/
    private String                         name                  = null;
    /*aop�����ɲ���Super|Propxy*/
    private String                         aopMode               = "super";
    /*Ĭ���е�*/
    private AopAbstractPointcutDefine      defaultPointcutDefine = null;
    /*�е�ִ����*/
    private ArrayList<AopAbstractInformed> aopInformedList       = new ArrayList<AopAbstractInformed>();
    /*------------------------------------------------------------------*/
    //
    /**��ȡaop��������*/
    public String getName() {
        return this.name;
    }
    /**����aop��������*/
    public void setName(String name) {
        this.name = name;
    }
    /**��ȡĬ��aop�е�����*/
    public AopAbstractPointcutDefine getDefaultPointcutDefine() {
        return this.defaultPointcutDefine;
    }
    /**����Ĭ��aop�е�����*/
    public void setDefaultPointcutDefine(AopAbstractPointcutDefine defaultPointcutDefine) {
        this.defaultPointcutDefine = defaultPointcutDefine;
    }
    /**��ȡaop�����ɲ���Super|Propxy*/
    public String getAopMode() {
        return aopMode;
    }
    /**����aop�����ɲ���Super|Propxy*/
    public void setAopMode(String aopMode) {
        this.aopMode = aopMode;
    }
    /**��ȡ�е�ִ����*/
    public ArrayList<AopAbstractInformed> getAopInformedList() {
        return aopInformedList;
    }
    /**�����е�ִ����*/
    public void setAopInformedList(ArrayList<AopAbstractInformed> aopInformedList) {
        this.aopInformedList = aopInformedList;
    }
}