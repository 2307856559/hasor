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
import org.more.core.classcode.BuilderMode;
/**
 * aop����
 * @version 2010-9-25
 * @author ������ (zyc@byshell.org)
 */
public class AopConfigDefine extends AbstractDefine<AopConfigDefine> {
    private String                         name                  = null; //������
    private BuilderMode                    aopMode               = null; //aop�����ɲ���
    private AopAbstractPointcutDefine      defaultPointcutDefine = null; //Ĭ���е�
    //
    private ArrayList<AopAbstractInformed> aopInformedList       = null;
    //
    /**����{@link AopConfigDefine}����*/
    public AopConfigDefine() {
        this.aopMode = BuilderMode.Super;
        this.aopInformedList = new ArrayList<AopAbstractInformed>();
    }
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
    /**��ȡaop�е�ִ����*/
    public List<AopAbstractInformed> getAopInformedList() {
        return this.aopInformedList;
    }
    /**���һ���е�ִ����*/
    public void addInformed(AopAbstractInformed informed, AopAbstractPointcutDefine refPointcut) {
        informed.setRefPointcut(refPointcut);
        this.aopInformedList.add(informed);
    };
    /**���һ���е�ִ����*/
    public void addInformed(AopAbstractInformed informed) {
        if (informed.getRefPointcut() == null)
            informed.setRefPointcut(this.defaultPointcutDefine);
        this.aopInformedList.add(informed);
    }
    /**��ȡaop�����ɲ��ԡ�*/
    public BuilderMode getAopMode() {
        return aopMode;
    }
    /**����aop�����ɲ��ԡ�*/
    public void setAopMode(BuilderMode aopMode) {
        this.aopMode = aopMode;
    };
}