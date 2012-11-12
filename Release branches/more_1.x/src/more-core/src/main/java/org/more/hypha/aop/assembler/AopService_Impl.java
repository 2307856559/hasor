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
import org.more.core.error.DefineException;
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.DefineResource;
import org.more.hypha.aop.AopService;
import org.more.hypha.commons.AbstractService;
import org.more.hypha.define.AbstractBeanDefine;
import org.more.hypha.define.AopAbstractPointcutDefine;
import org.more.hypha.define.AopConfigDefine;
import org.more.util.attribute.IAttribute;
/**
 * �����Ŀ����Ϊ����չ{@link DefineResource}�ӿڶ����Խ�aop��Ϣ���ӵ�������Դ�ӿ��С�
 * @version 2010-10-8
 * @author ������ (zyc@byshell.org)
 */
public class AopService_Impl extends AbstractService implements AopService {
    public static final String                  ServiceName  = "$more_aop_service";
    private static final String                 AopInfoName  = "$more_aop_info";
    private Map<String, AopAbstractPointcutDefine> pointcutList = new HashMap<String, AopAbstractPointcutDefine>();
    private Map<String, AopConfigDefine>        configList   = new HashMap<String, AopConfigDefine>();
    //
    private AopBuilder                          aopBuilder   = null;
    //
    private IAttribute<Object> getFungi(AbstractBeanDefine define) {
        if (define instanceof AbstractBeanDefine)
            return ((AbstractBeanDefine) define).getFungi();
        return null;
    }
    //
    public void start() {
        this.aopBuilder = new AopBuilder(this.getContext());
        this.aopBuilder.init();
    };
    public void stop() {
        this.aopBuilder.destroy();//ִ������
        this.aopBuilder = null;
    };
    public AopBuilder getAopBuilder() {
        return this.aopBuilder;
    }
    //
    /**����һ��{@link AbstractBeanDefine}��������Ƿ����Aop���á�*/
    public boolean containAop(AbstractBeanDefine define) {
        return this.getFungi(define).contains(AopInfoName);
    }
    /**��һ��aop����Я����{@link AbstractBeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(AbstractBeanDefine define, String config) {
        AopConfigDefine configDefine = this.configList.get(config);
        if (configDefine != null)
            this.getFungi(define).setAttribute(AopInfoName, configDefine);
    }
    /**��һ��aop����Я����{@link AbstractBeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(AbstractBeanDefine define, AopConfigDefine config) {
        if (define == null || config == null)
            throw new NullPointerException("define����Ϊ��.");
        if (config != null)
            this.getFungi(define).setAttribute(AopInfoName, config);
    }
    /**�Ƴ�{@link AbstractBeanDefine}�����ϵ�aop���ã����{@link AbstractBeanDefine}û������aop��ô�Ƴ������������ԡ�*/
    public void removeAop(AbstractBeanDefine define) {
        this.getFungi(define).removeAttribute(AopInfoName);
    }
    /**��ȡ{@link AbstractBeanDefine}�����ϵ�aop���ã����Ŀ��û������aop�򷵻�null��*/
    public AopConfigDefine getAopDefine(AbstractBeanDefine define) {
        IAttribute<Object> att = this.getFungi(define);
        if (att.contains(AopInfoName) == true)
            return (AopConfigDefine) att.getAttribute(AopInfoName);
        return null;
    }
    /**��ȡaop���ö��塣*/
    public AopConfigDefine getAopDefine(String name) {
        return this.configList.get(name);
    }
    /**��ȡһ�����������㡣*/
    public AopAbstractPointcutDefine getPointcutDefine(String name) throws DefineException {
        if (this.pointcutList.containsKey(name) == false)
            throw new DefineException("����������Ϊ[" + name + "]��AbstractPointcutDefine���塣");
        return this.pointcutList.get(name);
    }
    /**����е㶨�塣*/
    public void addPointcutDefine(AopAbstractPointcutDefine define) {
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