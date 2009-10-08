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
package org.more.core.copybean;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.more.log.ILog;
import org.more.log.LogFactory;
/**
 * Bean������������֧�ֵ����ͻ��࣬����붯̬��CopyBean�����ӿ��Կ�����Bean��������Ҫ��д��Ӧ�����Ͷ��塣
 * ��д�����Ͷ�����Ҫ�̳�BeanType�ࡣ
 * Date : 2009-5-20
 * @author ������
 */
public abstract class BeanType implements Serializable {
    /** �����־ */
    protected static transient ILog log = LogFactory.getLog("org_more_core_copybean");
    /**
     * ��ȡ������bean��������пɶ����д����Map��
     * @param object Ŀ�����
     * @return ����������bean��������пɶ����д����Map��
     */
    Map<String, PropertyReaderWrite> getPropertys(Object object) {
        BeanType.log.debug("BeanType getPropertys at class =" + object.getClass().getName() + " obj= " + object);
        Hashtable<String, PropertyReaderWrite> map = new Hashtable<String, PropertyReaderWrite>();
        //
        Iterator<String> iterator = this.iteratorNames(object);
        while (iterator.hasNext()) {
            String ns = iterator.next();
            BeanType.log.debug("BeanType see property name is " + ns);
            PropertyReaderWrite rw = this.getPropertyRW(object, ns);
            if (rw != null)
                map.put(ns, rw);
            BeanType.log.debug("BeanType see property name is " + ns + " rw=" + rw);
        }
        BeanType.log.debug("BeanType see property coount " + map.size());
        return map;
    }
    /**
     * ���Bean������������Ƶ�������BeanType��getPropertys����ͨ���õ�������������PropertyReaderWrite����
     * @return ����Bean������������Ƶ�������BeanType��getPropertys����ͨ���õ�������������PropertyReaderWrite����
     */
    protected abstract Iterator<String> iteratorNames(Object obj);
    /**
     * ���������ĳһ�ض����Զ�д�����ö�д���������ڶ�Ŀ���������Խ��ж�д�Ĳ����ࡣ������Ҫʵ�ָ÷�����
     * @param obj Ŀ�걻��д�Ķ���
     * @param name ����д�����Ķ�����������
     * @return ���ض�������Զ�д�����÷���ֵ����Ϊ�գ����Ϊ������Ը����ԡ�
     */
    protected abstract PropertyReaderWrite getPropertyRW(Object obj, String name);
    /**
     * ���һ�������Ƿ���Ա���ǰ������֧�֡�ϵͳͨ��ö�����п�֧�ֵ�����ͨ���÷���ȷ��ʹ���ĸ�BeanType����
     * һ��ϵͳѡ��һ��BeanType���󽫺�������BeanType����������ע��ʱ��Ҫע��˳����ע����������Ͷ���
     * ϵͳĬ�ϵ����ȼ�Ҫ�ߡ�������Ҫʵ�ָ÷�����
     * @param object �����Ķ���
     * @return ���ؼ���������ǰBeanType������Խ����ö����򷵻�true���򷵻�false��
     */
    protected abstract boolean checkObject(Object object);
}