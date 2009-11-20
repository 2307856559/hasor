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
package org.more.beans.core.propparser;
import org.more.beans.core.ResourceBeanFactory;
import org.more.beans.info.BeanDefinition;
import org.more.beans.info.BeanProp;
import org.more.beans.info.BeanProperty;
/**
 * ���Խ������ӿڣ���ʵ����������������������ͣ�{@link BeanProp}������ࣩ���������propparser���Ѿ����info������Ѿ���������Զ������ͽ�����һ��һʵ�֡�
 * <br/>Date : 2009-11-18
 * @author ������
 */
public interface PropertyParser {
    /**
     * �������Բ��ҷ��ض�����Ƭ�εĽ��������
     * @param context ��ǰҪ�������������ĸ������ϵ����ԣ���������������ǹ��췽������������Ŀ�����û�д��������ڹ��췽��������context��null������֮�⹤������Ҳһ����
     * @param contextParams ����beanʱ�򸽴��Ļ������������������ResourceBeanFactory.getBean()ʱ���ݵĲ�����
     * @param prop Ҫ����������Ƭ�Ρ�
     * @param propContext ����������Ƭ���������Ǹ�{@link BeanDefinition}�ж�������Զ����������Ƭ���ж����νṹ���ǽ�����ЩƬ��֮�����Ҫע�������ȴ�ǲ���ģ�������ԵĶ�����Ǹò�����
     * @param definition ����propContext�������Ǹ�{@link BeanDefinition}����
     * @param factory ��������Ƭ������������ʱ����Ҫ�������õĶ�����ʱ����Ҫ{@link ResourceBeanFactory}���Ͷ����������
     * @param contextParser ������ĳһ����������ʱ��Ҫ����һ���������Խ���������ڶ���
     * @return ���ؽ����Ľ����
     * @throws Exception ��������ڼ䷢���쳣��
     */
    public Object parser(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception;
    /**
     * �����������Ͳ��ҷ��ؽ������������ͽ����
     * @param context ��ǰҪ�������������ĸ������ϵ����ԣ���������������ǹ��췽������������Ŀ�����û�д��������ڹ��췽��������context��null������֮�⹤������Ҳһ����
     * @param contextParams ����beanʱ�򸽴��Ļ������������������ResourceBeanFactory.getBean()ʱ���ݵĲ�����
     * @param prop Ҫ����������Ƭ�Ρ�
     * @param propContext ����������Ƭ���������Ǹ�{@link BeanDefinition}�ж�������Զ����������Ƭ���ж����νṹ���ǽ�����ЩƬ��֮�����Ҫע�������ȴ�ǲ���ģ�������ԵĶ�����Ǹò�����
     * @param definition ����propContext�������Ǹ�{@link BeanDefinition}����
     * @param factory ��������Ƭ������������ʱ����Ҫ�������õĶ�����ʱ����Ҫ{@link ResourceBeanFactory}���Ͷ����������
     * @param contextParser ������ĳһ����������ʱ��Ҫ����һ���������Խ���������ڶ���
     * @return ���ؽ����Ľ����
     * @throws Exception ��������ڼ䷢���쳣��
     */
    public Class<?> parserType(Object context, Object[] contextParams, BeanProp prop, BeanProperty propContext, BeanDefinition definition, ResourceBeanFactory factory, PropertyParser contextParser) throws Exception;
}