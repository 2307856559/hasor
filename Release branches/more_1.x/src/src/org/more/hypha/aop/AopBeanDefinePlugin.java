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
package org.more.hypha.aop;
import org.more.hypha.aop.define.AopConfigDefine;
import org.more.hypha.beans.BeanDefinePlugin;
/**
 * ����{@link AopBeanDefinePlugin}����
 * @version 2010-9-25
 * @author ������ (zyc@byshell.org)
 */
public class AopBeanDefinePlugin implements BeanDefinePlugin {
    /**Ҫע��Ĳ����*/
    public static final String AopPluginName = "$more_aop_Plugin";
    private Object             target        = null;              //��չĿ��
    private AopConfigDefine    aopConfig     = null;              //��չ��aop����
    /***/
    public AopBeanDefinePlugin(Object target, AopConfigDefine aopConfig) {
        this.target = target;
        this.aopConfig = aopConfig;
    }
    /**��ȡ��չ��aop���ԡ�*/
    public AopConfigDefine getAopConfig() {
        return aopConfig;
    }
    /**��ȡ��չĿ�ꡣ*/
    public Object getTarget() {
        return this.target;
    }
}