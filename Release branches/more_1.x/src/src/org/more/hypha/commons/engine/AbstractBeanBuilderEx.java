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
package org.more.hypha.commons.engine;
import org.more.hypha.AbstractBeanDefine;
/**
 * �ýӿ���չ��{@link AbstractBeanBuilder}�ӿڲ����ṩ�������·�����ֵ��ע��������ʹ�øýӿ��򸸽ӿڵ�
 * loadType��������ʧЧ��ȡ����֮���Ǳ��ӿ��е�loadType���ط�������Ч��
 * @version : 2011-5-12
 * @author ������ (zyc@byshell.org)
 */
public abstract class AbstractBeanBuilderEx<T extends AbstractBeanDefine> extends AbstractBeanBuilder<T> {
    /**
     * ��ȡĿ��bean������ֽ������ݡ�
     * @param define Ҫװ���ֽ����Bean���塣
     * @param params ��ȡbeanʱ��getBean��������Ĳ�����
     */
    public abstract byte[] loadBytes(T define, Object[] params);
    /**
     * ���ֽ�������װ�س����Ͷ���
     * @param bytes �Ѿ�װ�ص��ֽ������ݣ�ֻ�轫���ֽ���ת��Ϊ���ͼ��ɡ�
     * @param define Ҫװ�����͵�Bean���塣
     * @param params ��ȡbeanʱ��getBean��������Ĳ�����
     */
    public abstract Class<?> loadType(byte[] bytes, T define, Object[] params);
    /**ʧЧ������*/
    public Class<?> loadType(T define, Object[] params) {
        return null;/*�����������չ��,�÷��������ڱ����á�*/
    }
};