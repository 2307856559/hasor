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
package org.more.hypha.aop.assembler;
import java.util.HashMap;
import java.util.Map;
import org.more.NoDefinitionException;
import org.more.hypha.DefineResource;
import org.more.hypha.aop.AopBeanDefinePlugin;
import org.more.hypha.aop.AopDefineResourcePlugin;
import org.more.hypha.aop.define.AbstractPointcutDefine;
import org.more.hypha.aop.define.AopConfigDefine;
import org.more.hypha.beans.AbstractBeanDefine;
import org.more.hypha.beans.BeanDefinePlugin;
/**
 * �����Ŀ����Ϊ����չ{@link DefineResource}�ӿڶ����Խ�aop��Ϣ���ӵ�������Դ�ӿ��С�
 * @version 2010-10-8
 * @author ������ (zyc@byshell.org)
 */
public class AopDefineResourcePlugin_Impl implements AopDefineResourcePlugin {
    /***/
    private DefineResource                      target       = null;
    private Map<String, AbstractPointcutDefine> pointcutList = new HashMap<String, AbstractPointcutDefine>();
    private Map<String, AopConfigDefine>        configList   = new HashMap<String, AopConfigDefine>();
    /**����{@link AopDefineResourcePlugin_Impl}����*/
    public AopDefineResourcePlugin_Impl(DefineResource target) {
        this.target = target;
    }
    //======================================================�ӿ�ʵ��
    public DefineResource getTarget() {
        return this.target;
    }
    /**����һ��{@link AbstractBeanDefine}��������Ƿ����Aop���á�*/
    public boolean containAop(AbstractBeanDefine define) {
        return define.getPlugin(AopBeanDefinePlugin.AopPluginName) != null;
    }
    /**��һ��aop����Я����{@link AbstractBeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(AbstractBeanDefine define, String config) {
        AopConfigDefine configDefine = this.configList.get(config);
        if (configDefine != null)
            define.setPlugin(AopBeanDefinePlugin.AopPluginName, new AopBeanDefinePlugin(define, configDefine));
    }
    /**��һ��aop����Я����{@link AbstractBeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(AbstractBeanDefine define, AopConfigDefine config) {
        if (config != null)
            define.setPlugin(AopBeanDefinePlugin.AopPluginName, new AopBeanDefinePlugin(define, config));
    }
    /**�Ƴ�{@link AbstractBeanDefine}�����ϵ�aop���ã����{@link AbstractBeanDefine}û������aop��ô�Ƴ������������ԡ�*/
    public void removeAop(AbstractBeanDefine define) {
        define.removePlugin(AopBeanDefinePlugin.AopPluginName);
    }
    /**��ȡ{@link AbstractBeanDefine}�����ϵ�aop���ã����Ŀ��û������aop�򷵻�null��*/
    public AopConfigDefine getAopDefine(AbstractBeanDefine define) {
        BeanDefinePlugin plugin = define.getPlugin(AopBeanDefinePlugin.AopPluginName);
        if (plugin instanceof AopBeanDefinePlugin)
            return ((AopBeanDefinePlugin) plugin).getAopConfig();
        return null;
    }
    /**��ȡaop���ö��塣*/
    public AopConfigDefine getAopDefine(String name) {
        return this.configList.get(name);
    }
    /**��ȡһ�����������㡣*/
    public AbstractPointcutDefine getPointcutDefine(String name) throws NoDefinitionException {
        if (this.pointcutList.containsKey(name) == false)
            throw new NoDefinitionException("����������Ϊ[" + name + "]��AbstractPointcutDefine���塣");
        return this.pointcutList.get(name);
    }
    /**����е㶨�塣*/
    public void addPointcutDefine(AbstractPointcutDefine define) {
        this.pointcutList.put(define.getName(), define);
    }
    /**ɾ���е㶨�塣*/
    public void removePointcutDefine(String name) {
        this.pointcutList.remove(name);
    }
    public void addAopDefine(AopConfigDefine define) {
        this.configList.put(define.getName(), define);
    }
    public void removeAopDefine(String name) {
        this.configList.remove(name);
    }
    public boolean containPointcutDefine(String defineName) {
        return this.pointcutList.containsKey(defineName);
    }
    public boolean containAopDefine(String name) {
        return this.configList.containsKey(name);
    }
}