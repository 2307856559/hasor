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
import org.more.NoDefinitionException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.DefineResource;
import org.more.hypha.Plugin;
import org.more.hypha.aop.define.AbstractPointcutDefine;
import org.more.hypha.aop.define.AopConfigDefine;
/**
 * �ýӿ�ͨ��{@link Plugin}�����ʽ��ǿ��{@link DefineResource}�ӿڣ����ṩ�˸�Ϊ�ḻ��aop��ط�����
 * @version 2010-10-8
 * @author ������ (zyc@byshell.org)
 */
public interface AopResourceExpand extends Plugin<DefineResource> {
    /**Ҫע��Ĳ����*/
    public static final String AopDefineResourcePluginName = "$more_aop_ResourcePlugin";
    /**��ȡһ�����������㣬����Ҳ����������{@link NoDefinitionException}�쳣��*/
    public AbstractPointcutDefine getPointcutDefine(String name) throws NoDefinitionException;
    /**����е㶨�塣*/
    public void addPointcutDefine(AbstractPointcutDefine define);
    /**ɾ���е㶨�塣*/
    public void removePointcutDefine(String name);
    /**����Ƿ��Ѿ�����ĳ�����Ƶ�{@link AbstractPointcutDefine}��*/
    public boolean containPointcutDefine(String defineName);
    //------------------
    /**����һ��aop�����Ƿ���ڡ�*/
    public boolean containAopDefine(String name);
    /**��ȡһ��aop���ö��塣*/
    public AopConfigDefine getAopDefine(String name);
    /**���aop���ö��塣*/
    public void addAopDefine(AopConfigDefine define);
    /**ɾ��aop���ö��塣*/
    public void removeAopDefine(String name);
    //------------------
    /**����һ��{@link AbstractBeanDefine}��������Ƿ����Aop���á�*/
    public boolean containAop(AbstractBeanDefine define);
    /**��һ��aop����Я����{@link AbstractBeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(AbstractBeanDefine define, String config);
    /**��һ��aop����Я����{@link AbstractBeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(AbstractBeanDefine define, AopConfigDefine config);
    /**�Ƴ�{@link AbstractBeanDefine}�����ϵ�aop���ã����{@link AbstractBeanDefine}û������aop��ô�Ƴ������������ԡ�*/
    public void removeAop(AbstractBeanDefine define);
    /**��ȡ{@link AbstractBeanDefine}�����ϵ�aop���ã����Ŀ��û������aop�򷵻�null��*/
    public AopConfigDefine getAopDefine(AbstractBeanDefine define);
}