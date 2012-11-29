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
import org.more.hypha.AbstractBeanDefine;
import org.more.hypha.Service;
import org.more.hypha.define.aop.AopPointcut;
import org.more.hypha.define.aop.AopConfig;
/**
 * �ṩ�˸�Ϊ�ḻ��aop��ط�����
 * @version 2010-10-8
 * @author ������ (zyc@byshell.org)
 */
public interface AopService extends Service {
    /**��ȡһ�����������㡣*/
    public AopPointcut getPointcutDefine(String name);
    /**����е㶨�塣*/
    public void addPointcutDefine(AopPointcut define);
    /**ɾ���е㶨�塣*/
    public void removePointcutDefine(String name);
    /**����Ƿ��Ѿ�����ĳ�����Ƶ�{@link AopPointcut}��*/
    public boolean containPointcutDefine(String defineName);
    //------------------
    /**����һ��aop�����Ƿ���ڡ�*/
    public boolean containAopDefine(String name);
    /**��ȡһ��aop���ö��塣*/
    public AopConfig getAopDefine(String name);
    /**���aop���ö��塣*/
    public void addAopDefine(AopConfig define);
    /**ɾ��aop���ö��塣*/
    public void removeAopDefine(String name);
    //------------------
    /**����һ��{@link BeanDefine}��������Ƿ����Aop���á�*/
    public boolean containAop(BeanDefine define);
    /**��һ��aop����Я����{@link BeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(BeanDefine define, String config);
    /**��һ��aop����Я����{@link BeanDefine}�����ϣ��÷��������ڴ��뼶���޸�aop���á�*/
    public void setAop(BeanDefine define, AopConfig config);
    /**�Ƴ�{@link BeanDefine}�����ϵ�aop���ã����{@link BeanDefine}û������aop��ô�Ƴ������������ԡ�*/
    public void removeAop(BeanDefine define);
    /**��ȡ{@link BeanDefine}�����ϵ�aop���ã����Ŀ��û������aop�򷵻�null��*/
    public AopConfig getAopDefine(BeanDefine define);
}